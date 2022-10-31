package com.windanesz.morphspellpack.ability.active;

import com.windanesz.morphspellpack.ability.IActiveAbility;
import me.ichun.mods.morph.api.ability.Ability;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class AbilityTimedPotion extends Ability implements IActiveAbility {

	public static final String name = "timedPotion";
	private String potionName;
	public Boolean toggled; //reset motionY when the triggered?

	public int cooldown = 0;
	public int maxCooldown = 0;
	public int effectDuration = 0;
	Potion potion;

	@SuppressWarnings("unused")
	public AbilityTimedPotion() {
	}

	public AbilityTimedPotion(String potion, int maxCooldown, int effectDuration) {
		this.potionName = potion;
		this.maxCooldown = maxCooldown;
		this.effectDuration = effectDuration;
		this.potion = ForgeRegistries.POTIONS.getValue(new ResourceLocation(potion));
	}

	@Override
	public Ability parse(String[] args) {
		potionName = args[0];
		maxCooldown = Integer.parseInt(args[1]);
		effectDuration = Integer.parseInt(args[2]);
		return this;
	}

	@Override
	public Ability clone() {
		return new AbilityTimedPotion(potionName, maxCooldown, effectDuration);
	}

	@Override
	public String getType() {
		return name;
	}

	@Override
	public void tick() {
		if (cooldown > 0) {
			cooldown--;
			toggled = false;
		}

		if (toggled != null && toggled && conditionPredicate() && potion != null) {

			if (getParent() instanceof EntityPlayerMP) {

				EntityPlayerMP caster = (EntityPlayerMP) getParent();
				caster.addPotionEffect(new PotionEffect(potion, effectDuration));
			}
		}

		toggled = false;
	}

	@Override
	public void toggleAbility() {
		toggled = true;
	}
}
