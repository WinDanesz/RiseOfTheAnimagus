package com.windanesz.morphspellpack.ability;

import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.spell.Spell;
import me.ichun.mods.morph.api.ability.Ability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class AbilitySpellFromArtefact extends AbilitySpell {

	public static final String name = "spellCastWithArtefact";
	private Item artefact;

	public AbilitySpellFromArtefact(Spell spell, int maxCooldown, Item artefact, int duration) {
		super(spell, maxCooldown, duration);
		this.artefact = artefact;
	}

	@Override
	public Ability parse(String[] args) {
		spell = Spell.get(args[0]);
		maxCooldown = Integer.parseInt(args[1]);
		artefact = ForgeRegistries.ITEMS.getValue(new ResourceLocation(args[2]));
		duration = Integer.parseInt(args[3]);
		return this;
	}

	@SuppressWarnings("unused")
	public AbilitySpellFromArtefact() {
		super();
	}

	@Override
	public Ability clone() {
		return new AbilitySpellFromArtefact(spell,maxCooldown, artefact, duration);
	}

	@Override
	public String getType() {
		return name;
	}

	@Override
	public boolean conditionPredicate() {
		return (this.getParent() instanceof EntityPlayer && this.artefact != null
				&& ItemArtefact.isArtefactActive((EntityPlayer) getParent(), this.artefact));
	}
}
