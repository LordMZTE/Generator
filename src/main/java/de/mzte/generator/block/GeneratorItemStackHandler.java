package de.mzte.generator.block;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class GeneratorItemStackHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<CompoundNBT> {
	public ItemStackHandler internalHandler;

	protected GeneratorItemStackHandler(int size) {
		internalHandler = new ItemStackHandler(size){
			@Override
			public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
				if(AbstractFurnaceTileEntity.isFuel(stack)) {
					return super.isItemValid(slot, stack);
				}
				return false;
			}
		};
	}

	public ItemStackHandler getInternalHandler() {
		return internalHandler;
	}

	public void setInternalHandler(ItemStackHandler internalHandler) {
		this.internalHandler = internalHandler;
	}
	//Relay

	@Override
	public int getSlots() {
		return internalHandler.getSlots();
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot) {
		return internalHandler.getStackInSlot(slot);
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		return internalHandler.insertItem(slot, stack, simulate);
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot) {
		return internalHandler.getSlotLimit(slot);
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		return internalHandler.isItemValid(slot, stack);
	}

	@Override
	public CompoundNBT serializeNBT() {
		return internalHandler.serializeNBT();
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		internalHandler.deserializeNBT(nbt);
	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		internalHandler.setStackInSlot(slot, stack);
	}
}
