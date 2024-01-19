package com.windanesz.morphspellpack.ability;

import me.ichun.mods.morph.api.ability.Ability;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class AbilityPassivePotion extends Ability {

	public static final String name = "passivePotion";
	private List<PotionInfo> potions = new ArrayList<>();

	public int cooldown = 0;

	public static class PotionInfo {
		public String potionName;
		public int amplifier;
		public int effectDuration;
		public Potion potion;

		public PotionInfo(String potionName, int amplifier, int effectDuration) {
			this.potionName = potionName;
			this.amplifier = amplifier;
			this.effectDuration = effectDuration;
			this.potion = ForgeRegistries.POTIONS.getValue(new ResourceLocation(potionName));
		}
	}

	@SuppressWarnings("unused")
	public AbilityPassivePotion() {
	}

	public AbilityPassivePotion(List<PotionInfo> potions) {
		this.potions = potions;
	}

	@Override
	public Ability parse(String[] args) {
		for (int i = 0; i < args.length; i += 3) {
			String potionName = args[i];
			int amplifier = Integer.parseInt(args[i + 1]);
			int effectDuration = Integer.parseInt(args[i + 2]);
			potions.add(new PotionInfo(potionName, amplifier, effectDuration));
		}
		return this;
	}

	@Override
	public Ability clone() {
		return new AbilityPassivePotion(new ArrayList<>(potions));
	}

	@Override
	public String getType() {
		return name;
	}

	@Override
	public void tick() {
		if (getParent() instanceof EntityPlayerMP && getParent().ticksExisted % 20 == 0) {
			EntityPlayerMP caster = (EntityPlayerMP) getParent();
			for (PotionInfo potionInfo : potions) {
				if (potionInfo.potion != null) {
					caster.addPotionEffect(new PotionEffect(potionInfo.potion, potionInfo.effectDuration, potionInfo.amplifier));
				}
			}
		}
	}
}
