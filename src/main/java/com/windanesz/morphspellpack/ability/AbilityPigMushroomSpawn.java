package com.windanesz.morphspellpack.ability;

import net.minecraft.world.biome.BiomeForest;

public class AbilityPigMushroomSpawn extends AbilityItemSpawn {

	public static final String name = "itemSpawnPigMushroom";

	@Override
	public boolean conditionPredicate() {
		return super.conditionPredicate() && this.getParent() != null && this.getParent().world.getBiomeProvider().getBiome(this.getParent().getPosition()) instanceof BiomeForest;
	}
}
