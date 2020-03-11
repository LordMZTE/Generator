package de.mzte.generator.items;

import de.mzte.generator.block.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItems {
	public static ItemGroup itemGroup = new ItemGroup("mztegenerator") {

		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModBlocks.GENERATOR);
		}
	};

}
