package com.windanesz.morphspellpack.spell;

import com.windanesz.morphspellpack.entity.living.EntityTemporaryRabbit;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class Rabbitify extends SpellTransformTarget {

	public Rabbitify() {
		super("rabbitify", SpellActions.POINT, false);
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit,
			@Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {

		if (target instanceof EntityPlayer && !world.isRemote) {
			return SpellTransformation.morphPlayer((EntityLivingBase) target, "morphspellpack:temporary_rabbit", getProperty(DURATION).intValue());
		} else if (target instanceof EntityLivingBase && target.isNonBoss() && !world.isRemote && !target.getIsInvulnerable()) {
			EntityTemporaryRabbit rabbit = new EntityTemporaryRabbit(world);
			rabbit.storeEntity((EntityLivingBase) target);
			rabbit.setPositionAndRotation(target.posX, target.posY, target.posZ, target.rotationYaw, target.rotationPitch);
			rabbit.setLifetimeAsRabbit(this.getProperty(DURATION).intValue());
			world.spawnEntity(rabbit);
			world.removeEntity(target);
		} else {
			if (!world.isRemote && ticksInUse == 1 && caster instanceof EntityPlayer) {
				((EntityPlayer) caster)
						.sendStatusMessage(new TextComponentTranslation("spell.resist", target.getName(),
								this.getNameForTranslationFormatted()), true);
			}
		}

		return false;
	}

	@Override
	protected void spawnParticle(World world, double x, double y, double z, double vx, double vy, double vz){
		ParticleBuilder.create(ParticleBuilder.Type.DARK_MAGIC).pos(x, y, z).clr(0.2f, 0, 0.3f).spawn(world);
		ParticleBuilder.create(ParticleBuilder.Type.DARK_MAGIC).pos(x, y, z).clr(0.1f, 0, 0).spawn(world);
		ParticleBuilder.create(ParticleBuilder.Type.SPARKLE).pos(x, y, z).time(12 + world.rand.nextInt(8)).clr(0.4f, 0, 0).spawn(world);
	}
}
