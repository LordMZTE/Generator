package de.mzte.generator.data;

import de.mzte.generator.block.ModBlocks;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

	public Recipes(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.GENERATOR)
				.patternLine("IRI")
				.patternLine("RFR")
				.patternLine("IRI")
				.key('I', Tags.Items.INGOTS_IRON)
				.key('R', Tags.Items.DUSTS_REDSTONE)
				.key('F', Items.BLAST_FURNACE)
				.setGroup("mztegenerator")
				.addCriterion("redstone", InventoryChangeTrigger.Instance.forItems(Items.REDSTONE))
				.build(consumer);
	}
}
