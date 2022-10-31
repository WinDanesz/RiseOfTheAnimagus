package com.windanesz.morphspellpack.spell;

import com.windanesz.morphspellpack.MorphSpellPack;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import me.ichun.mods.morph.common.Morph;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ShapeBinding extends Spell {

	public ShapeBinding() {
		super(MorphSpellPack.MODID, "shape_binding", SpellActions.SUMMON, false);
		addProperties(DURATION);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (!world.isRemote && Morph.eventHandlerServer.morphsActive.containsKey(caster.getName())
				&& !Morph.eventHandlerServer.morphsActive.get(caster.getName()).isMorphing()) {
			WizardData data = WizardData.get(caster);
			if (data != null) {
				Integer duration = data.getVariable(SpellTransformation.MORPH_DURATION);
				if (duration != null && duration > 0) {
					duration = duration + getProperty(DURATION).intValue();
					data.setVariable(SpellTransformation.MORPH_DURATION, duration);
					data.sync();
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) {
		return false;
	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) {
		return false;
	}

}
