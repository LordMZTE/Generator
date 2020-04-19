package de.mzte.generator.data;


import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Data {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent e) {
		if(e.includeServer()) {
			DataGenerator gen = e.getGenerator();
			gen.addProvider(new Recipes(gen));
			gen.addProvider(new LootTables(gen));
		}
	}
}
