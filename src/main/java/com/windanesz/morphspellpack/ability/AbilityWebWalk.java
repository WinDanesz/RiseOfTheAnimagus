package com.windanesz.morphspellpack.ability;

import me.ichun.mods.morph.api.ability.Ability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class AbilityWebWalk extends Ability {

	public static final String name = "webWalk";
	public static final String isInWeb = "field_70134_J";

	@SuppressWarnings("unused")
	public AbilityWebWalk() {
	}

	@Override
	public String getType() {
		return name;
	}

	@Override
	public void tick() {
		if (getParent() instanceof EntityPlayer) {
			ObfuscationReflectionHelper.setPrivateValue(Entity.class, getParent(), false, "createMinion", isInWeb);
		}
	}
}
