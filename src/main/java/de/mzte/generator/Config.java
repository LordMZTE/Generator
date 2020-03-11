package de.mzte.generator;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Config {
	public static final String CATEGORY_GENERATOR = "generator";

	private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
	private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

	public static ForgeConfigSpec COMMON_CONFIG;

	public static ForgeConfigSpec.IntValue GENERATOR_ENERGY_CAPACITY;
	public static ForgeConfigSpec.IntValue GENERATOR_ENERGY_TRANSFER;
	public static ForgeConfigSpec.IntValue GENERATOR_ENERGY_PRODUCTION;
	public static ForgeConfigSpec.IntValue GENERATOR_FUEL_USAGE;

	static {
		COMMON_BUILDER.comment("Generator Settings").push(CATEGORY_GENERATOR);

		GENERATOR_ENERGY_CAPACITY = COMMON_BUILDER.comment("Energy that the Generator Can store")
				.defineInRange("storageCapacity", 100000, 0, Integer.MAX_VALUE);

		GENERATOR_ENERGY_TRANSFER = COMMON_BUILDER.comment("How Fast the energy should be transferred to adjacent blocks")
				.defineInRange("energyTransfer", 512, 0, Integer.MAX_VALUE);

		GENERATOR_ENERGY_PRODUCTION = COMMON_BUILDER.comment("Energy that the Generator Will Produce")
				.defineInRange("energyProduction", 60, 0, Integer.MAX_VALUE);

		GENERATOR_FUEL_USAGE = COMMON_BUILDER.comment("How Fast the generator will use fuel")
				.defineInRange("fuelUsage", 1, 0, Integer.MAX_VALUE);



		COMMON_BUILDER.pop();


		COMMON_CONFIG = COMMON_BUILDER.build();
	}
}
