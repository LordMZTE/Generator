package de.mzte.generator.block;

import de.mzte.generator.Config;
import de.mzte.generator.tools.CustomEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public class GeneratorTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

	//Burn time stuff
	private int burnTime = 0;
	private int totalBurnTime = 0;
	private ItemStack fuelItem = ItemStack.EMPTY;
	private int fuelItemBurnTime = 0;

	private int energyStored = 0;
	private int maxEnergy = 0;
	private GeneratorItemStackHandler generatorItemStackHandler = createGeneratorHandler();
	private LazyOptional<GeneratorItemStackHandler> LazyOptionalHandler = LazyOptional.of(() -> generatorItemStackHandler).cast();
	public ItemStackHandler internalHandler = getInternalHandler(generatorItemStackHandler);
	private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy).cast();



	public GeneratorTile() {
		super(ModBlocks.GENERATOR_TILE);
	}


	@Override
	public void tick() {
		energy.ifPresent(h -> energyStored = h.getEnergyStored());
		energy.ifPresent(h -> maxEnergy = h.getMaxEnergyStored());
		sendEnergy();
		fuelItem = generatorItemStackHandler.getStackInSlot(0);
		fuelItemBurnTime = ForgeHooks.getBurnTime(fuelItem);
		if((burnTime == 0) && (energyStored != maxEnergy)) {
			internalHandler.extractItem(0, 1, false);
			totalBurnTime = fuelItemBurnTime;
			burnTime = fuelItemBurnTime;
			markDirty();
		}

		BlockState generatorState = world.getBlockState(pos);
		if(generatorState.get(BlockStateProperties.POWERED) != burnTime > 0) {
			world.setBlockState(pos, generatorState.with(BlockStateProperties.POWERED, burnTime > 0));
		}

		if(burnTime > 0){
			burnTime -= Config.GENERATOR_FUEL_USAGE.get();

			if(energyStored < maxEnergy) {
				energy.ifPresent(h -> ((CustomEnergyStorage) h).addEnergy(Config.GENERATOR_ENERGY_PRODUCTION.get()));
				markDirty();
			}
		}
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return LazyOptionalHandler.cast();
		}
		if(cap == CapabilityEnergy.ENERGY) {
			return energy.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		LazyOptionalHandler.ifPresent(h -> ((INBTSerializable<CompoundNBT>)h).deserializeNBT(compound.getCompound("inv")));
		CompoundNBT energyTag = compound.getCompound("power");
		energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(energyTag));
		burnTime = compound.getInt("burnTime");
		totalBurnTime = compound.getInt("totalBurnTime");
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		LazyOptionalHandler.ifPresent(h -> compound.put("inv", ((INBTSerializable<CompoundNBT>)h).serializeNBT()));
		energy.ifPresent(h -> {
			CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
			compound.put("power", tag);
		});
		compound.putInt("burnTime", burnTime);
		compound.putInt("totalBurnTime", totalBurnTime);
		return compound;
	}

	private IEnergyStorage createEnergy() {
		return new CustomEnergyStorage(Config.GENERATOR_ENERGY_CAPACITY.get(), 0, Config.GENERATOR_ENERGY_TRANSFER.get(), 0);
	}

	private GeneratorItemStackHandler createGeneratorHandler() {
		return new GeneratorItemStackHandler(1);
	}

	private ItemStackHandler getInternalHandler(GeneratorItemStackHandler generatorItemStackHandler) {
		return generatorItemStackHandler.getInternalHandler();
	}


	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container.mztegenerator.generator");
	}

	@Nullable
	@Override
	public Container createMenu(int winId, PlayerInventory playerInv, PlayerEntity playerEnt) {
		return new GeneratorContainer(winId, world, pos, playerInv, playerEnt);
	}

	private void sendEnergy() {
		energy.ifPresent(e -> {
			AtomicInteger storedEnergy = new AtomicInteger(e.getEnergyStored());
			if(storedEnergy.get() > 0) {
				for(Direction direction : Direction.values()) {
					TileEntity te = world.getTileEntity(pos.offset(direction));
					if(te != null) {
						boolean doContinue = te.getCapability(CapabilityEnergy.ENERGY, direction).map(h -> {
							if(h.canReceive()) {
								int sent = h.receiveEnergy(
										Math.min(storedEnergy.get(), Config.GENERATOR_ENERGY_TRANSFER.get()),
										false);
								storedEnergy.addAndGet(- sent);
								((CustomEnergyStorage) e).consumeEnergy(sent);
								markDirty();
								return storedEnergy.get() > 0;
							} else {
								return true;
							}
						}).orElse(true);
						if(!doContinue) {
							return;
						}
					}
				}
			}
		});
	}
	public int getBurnTimeRelative() {
		if(totalBurnTime == 0) {return 0;}
		//first parameter is height of flame texture
		return (14 *  burnTime) / totalBurnTime;
	}

	public int getEnergyRelative() {
		AtomicInteger energyMax = new AtomicInteger();
		AtomicInteger energyStored = new AtomicInteger();
		energy.ifPresent(h -> {
			energyMax.set(h.getMaxEnergyStored());
			energyStored.set(h.getEnergyStored());
		});
		if(energyMax.get() == 0) {return 0;}
		//first parameter is height of energy meter texture
		return (56 *  energyStored.get()) / energyMax.get();
	}
}

