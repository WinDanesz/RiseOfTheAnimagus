package com.windanesz.morphspellpack.ability;

import electroblob.wizardry.item.ItemArtefact;
import me.ichun.mods.morph.api.ability.Ability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class AbilityTimedPotionFromArtefact extends Ability implements IActiveAbility {

	public static final String name = "timedPotionWithArtefact";
	private String potionName;
	public Boolean toggled; //reset motionY when the triggered?

	public int cooldown = 0;
	public int maxCooldown = 0;
	public int effectDuration = 0;
	Potion potion;
	private Item artefact;

	@SuppressWarnings("unused")
	public AbilityTimedPotionFromArtefact() {
	}

	public AbilityTimedPotionFromArtefact(String potion, int maxCooldown, int effectDuration, Item artefact) {
		this.potionName = potion;
		this.maxCooldown = maxCooldown;
		this.effectDuration = effectDuration;
		this.potion = ForgeRegistries.POTIONS.getValue(new ResourceLocation(potion));
		this.artefact = artefact;
	}

	@Override
	public Ability parse(String[] args) {
		potionName = args[0];
		maxCooldown = Integer.parseInt(args[1]);
		effectDuration = Integer.parseInt(args[2]);
		artefact = ForgeRegistries.ITEMS.getValue(new ResourceLocation(args[3]));
		return this;
	}

	@Override
	public Ability clone() {
		return new AbilityTimedPotionFromArtefact(potionName, maxCooldown, effectDuration, artefact);
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

	@Override
	public boolean conditionPredicate() {
		return (this.getParent() instanceof EntityPlayer && this.artefact != null
				&& ItemArtefact.isArtefactActive((EntityPlayer) getParent(), this.artefact));
	}
}
