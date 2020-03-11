package de.mzte.generator.block;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {


	@ObjectHolder("mztegenerator:generator")
	public static Generator GENERATOR;

	@ObjectHolder("mztegenerator:generator")
	public static TileEntityType<GeneratorTile> GENERATOR_TILE;

	@ObjectHolder("mztegenerator:generator")
	public static ContainerType<GeneratorContainer> GENERATOR_CONTAINER;
}
