package com.windanesz.morphspellpack.spell;

import com.windanesz.morphspellpack.MorphSpellPack;
import com.windanesz.morphspellpack.entity.construct.EntityStarfall;
import com.windanesz.morphspellpack.registry.MSItems;
import com.windanesz.morphspellpack.registry.MSSpells;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.spell.SpellConstructRanged;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.w3c.dom.Entity;

import javax.annotation.Nullable;

public class Starfall extends SpellConstructRanged<EntityStarfall> {

	public Starfall(){
		super(MorphSpellPack.MODID,"starfall", EntityStarfall::new, false);
		this.floor(false);
		addProperties(EFFECT_RADIUS);
	}

	@Override
	protected boolean spawnConstruct(World world, double x, double y, double z, EnumFacing side, @Nullable EntityLivingBase caster, SpellModifiers modifiers){

		// Moves the entity back towards the caster a bit, so the area of effect is better centred on the position.
		// 3 is the distance to move the entity back towards the caster.
		double dx = caster == null ? side.getDirectionVec().getX() : caster.posX - x;
		double dz = caster == null ? side.getDirectionVec().getZ() : caster.posZ - z;
		double dist = Math.sqrt(dx * dx + dz * dz);
		if(dist != 0){
			double distRatio = 3 / dist;
			x += dx * distRatio;
			z += dz * distRatio;
		}
		// Moves the entity up 5 blocks so that it is above mobs' heads.
		y += 5;

		if (caster instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer) caster, MSItems.ring_starfall)) {
			modifiers.set(WizardryItems.duration_upgrade, modifiers.get(WizardryItems.duration_upgrade) * 2f, true);
		}
		return super.spawnConstruct(world, x, y, z, side, caster, modifiers);
	}

	@Override
	protected void addConstructExtras(EntityStarfall construct, EnumFacing side, EntityLivingBase caster, SpellModifiers modifiers){
		if(caster != null){
			construct.rotationYaw = caster.rotationYawHead;
		}else{
			construct.rotationYaw = side.getHorizontalAngle();
		}
		float bonus = 1.0f;
		if (caster instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer) caster, MSItems.ring_starfall)) {
			bonus = 15f;
		}
		construct.setRadius(MSSpells.starfall.getProperty(Spell.EFFECT_RADIUS).floatValue() * modifiers.get(WizardryItems.blast_upgrade) + bonus);
	}

}
