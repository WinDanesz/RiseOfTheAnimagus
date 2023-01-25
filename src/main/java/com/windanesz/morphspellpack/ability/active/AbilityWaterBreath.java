package com.windanesz.morphspellpack.ability.active;

import me.ichun.mods.morph.api.ability.Ability;
import net.minecraft.entity.player.EntityPlayer;

public class AbilityWaterBreath extends Ability {

	public static final String name = "waterBreath";

	@SuppressWarnings("unused")
	public AbilityWaterBreath() {
	}

	@Override
	public String getType() {
		return name;
	}

	@Override
	public void tick() {
		if (getParent() instanceof EntityPlayer && getParent().ticksExisted % 5 == 0) {
			((EntityPlayer) getParent()).setAir(300);
		}
	}
}
