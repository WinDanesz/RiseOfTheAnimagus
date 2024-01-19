package com.windanesz.morphspellpack.ability;

import com.windanesz.morphspellpack.MorphSpellPack;
import me.ichun.mods.morph.api.ability.Ability;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class AbilityPotionResistance extends Ability {

	public static final String name = "potionResistance";
	private String potionName;

	List<Potion> potions = new ArrayList<>();

	@SuppressWarnings("unused")
	public AbilityPotionResistance() {
	}

	public AbilityPotionResistance(List<Potion> potions) {
		this.potions = potions;
	}

	@Override
	public void tick() {

		for(PotionEffect effect : new ArrayList<>(getParent().getActivePotionEffects())){ // Get outta here, CMEs
			// The PotionEffect version (as opposed to Potion) does not call cleanup callbacks
			if (potions.contains(effect.getPotion())) {
				getParent().removePotionEffect(effect.getPotion());
			}
		}
	}

	@Override
	public Ability parse(String[] args) {
		for (String arg : args) {
			Potion potion = ForgeRegistries.POTIONS.getValue(new ResourceLocation(arg));
			if (potion != null) {
				this.potions.add(potion);
			} else {
				MorphSpellPack.logger.error("An ability listed an incorrect potion name for potionResistance ability. No such potion '" + arg + "'");
			}
		}

		return this;
	}

	@Override
	public Ability clone() {
		return new AbilityPotionResistance(potions);
	}

	@Override
	public String getType() {
		return name;
	}


}
