package com.windanesz.morphspellpack.spell;

import com.windanesz.morphspellpack.MorphSpellPack;
import com.windanesz.morphspellpack.items.ItemSoulPhylactery;
import com.windanesz.morphspellpack.registry.MSItems;
import com.windanesz.wizardryutils.integration.baubles.BaublesIntegration;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class SoulTransformation extends Spell {

	private static final String SOUL_COST = "soul_cost";

	public SoulTransformation() {
		super(MorphSpellPack.MODID, "soul_transformation", SpellActions.SUMMON, false);
		this.addProperties(DURATION, SOUL_COST);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (ItemArtefact.isArtefactActive(caster, MSItems.charm_soul_phylactery)) {
			int duration = (int) (getProperty(DURATION).floatValue() * modifiers.get(WizardryItems.duration_upgrade));
			ItemStack stack = BaublesIntegration.getEquippedArtefactStacks(caster, ItemArtefact.Type.CHARM).get(0);
			if (ItemSoulPhylactery.hasEntity(stack)) {
				if (ItemSoulPhylactery.getPercentFilled(stack) >= getProperty(SOUL_COST).floatValue()) {
					boolean flag = SpellTransformation.morphPlayer(caster, ItemSoulPhylactery.getEntity(stack), duration);
					if (flag) {
						ItemStack stack1 = ItemSoulPhylactery.consumePercent(stack, getProperty(SOUL_COST).floatValue());
						BaublesIntegration.setArtefactToSlot(caster, stack1, ItemArtefact.Type.CHARM);
					}
					return flag;
				} else {
					if (!world.isRemote) {
						caster.sendStatusMessage(new TextComponentTranslation("spell.morphspellpack:soul_transformation.phylactery_no_charge"), false);
					}
				}
			} else {
				if (!world.isRemote) {
					caster.sendStatusMessage(new TextComponentTranslation("spell.morphspellpack:soul_transformation.phylactery_no_entity"), false);
				}
			}
		} else {
			if (!world.isRemote) {
				caster.sendStatusMessage(new TextComponentTranslation("spell.morphspellpack:soul_transformation.no_phylactery"), false);
			}
		}

		return false;
	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) {
		return false;
	}

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) {
		return false;
	}
}
