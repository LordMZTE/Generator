package de.mzte.generator.data;

import de.mzte.generator.block.ModBlocks;
import net.minecraft.data.DataGenerator;

public class LootTables extends BaseLootTableProvider{


	public LootTables(DataGenerator dataGeneratorIn) {
		super(dataGeneratorIn);
	}

	@Override
	protected void addTables() {
		lootTables.put(ModBlocks.GENERATOR, createCopyInventoryDropTable(ModBlocks.GENERATOR));
	}


}
