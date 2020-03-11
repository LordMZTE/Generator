package de.mzte.generator;

import de.mzte.generator.block.GeneratorContainer;
import de.mzte.generator.block.GeneratorTile;
import de.mzte.generator.block.ModBlocks;
import de.mzte.generator.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod("mztegenerator")
public class Generator {
	public static final String MODID = "mztegenerator";

	public Generator() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void onBlocksRegistry(final RegistryEvent.Register<Block> e) {
			e.getRegistry().register(new de.mzte.generator.block.Generator());
		}

		@SubscribeEvent
		public static void onItemsRegistry(final RegistryEvent.Register<Item> e) {
			Item.Properties properties = new Item.Properties()
					.group(ModItems.itemGroup);


			e.getRegistry().register(new BlockItem(ModBlocks.GENERATOR, properties)
					.setRegistryName("generator"));

		}

		@SubscribeEvent
		public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> e) {
			e.getRegistry().register(TileEntityType.Builder
					.create(GeneratorTile ::new, ModBlocks.GENERATOR)
					.build(null)
					.setRegistryName("generator"));
		}

		@SubscribeEvent
		public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> e) {
			e.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
				return new GeneratorContainer(windowId,
						inv.player.world,
						data.readBlockPos(),
						inv,
						inv.player);
			}).setRegistryName("generator"));
		}
	}
}
