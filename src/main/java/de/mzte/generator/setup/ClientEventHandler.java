package de.mzte.generator.setup;

import de.mzte.generator.Generator;
import de.mzte.generator.block.GeneratorScreen;
import de.mzte.generator.block.ModBlocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Generator.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEventHandler {


	@SubscribeEvent
	public static void setup(final FMLClientSetupEvent e) {
		ScreenManager.registerFactory(ModBlocks.GENERATOR_CONTAINER, GeneratorScreen :: new);

	}
}
