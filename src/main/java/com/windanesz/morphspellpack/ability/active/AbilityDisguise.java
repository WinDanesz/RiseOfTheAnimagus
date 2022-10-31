package com.windanesz.morphspellpack.ability.active;

import me.ichun.mods.morph.api.ability.Ability;

public class AbilityDisguise extends Ability {

	public static final String name = "disguise";

	@SuppressWarnings("unused")
	public AbilityDisguise() {
	}

	@Override
	public String getType() {
		return name;
	}

	@Override
	public void tick() {
		getParent().motionX = 0;
		getParent().motionY = -0.1f;
		getParent().motionZ = 0;

	}

}
