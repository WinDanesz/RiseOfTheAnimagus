package com.windanesz.morphspellpack.spell;

import com.windanesz.morphspellpack.MorphSpellPack;
import com.windanesz.morphspellpack.Settings;
import com.windanesz.morphspellpack.entity.living.EntityTemporaryRabbit;
import com.windanesz.morphspellpack.registry.MSItems;
import com.windanesz.morphspellpack.registry.MSPotions;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.SpellBuff;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.ParticleBuilder.Type;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

public class CurseOfTransformation extends SpellRay {

	public CurseOfTransformation() {
		super(MorphSpellPack.MODID, "curse_of_transformation", SpellActions.POINT, false);
		this.soundValues(1, 1.1f, 0.2f);
		addProperties(EFFECT_STRENGTH);
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {

		if (EntityUtils.isLiving(target)) {

			List<String> entities = Arrays.asList(Settings.generalSettings.curse_of_transformation_mob_list);

			if (target instanceof EntityPlayer) {
				if (!ItemArtefact.isArtefactActive((EntityPlayer) target, MSItems.amulet_transformation_protection)) {
					if (!entities.isEmpty()) {
						String entity = entities.get(world.rand.nextInt(entities.size() - 1));
						// duration doesn't matter here
						boolean flag = SpellTransformation.morphPlayer((EntityLivingBase) target, entity, 20);
						// This will actually run out in the end, but only if you leave Minecraft running for 3.4 years
						((EntityLivingBase) target).addPotionEffect(new PotionEffect(MSPotions.curse_of_transformation,
								Integer.MAX_VALUE, getProperty(EFFECT_STRENGTH).intValue() + SpellBuff.getStandardBonusAmplifier(modifiers.get(SpellModifiers.POTENCY))));
						return  flag;
					}
				} else {
					if (!world.isRemote && ticksInUse == 1 && caster instanceof EntityPlayer) {
						((EntityPlayer) caster)
								.sendStatusMessage(new TextComponentTranslation("spell.resist", target.getName(),
										this.getNameForTranslationFormatted()), true);
					return false;
					}
				}
			}
			if (target.isNonBoss() && !world.isRemote && !target.getIsInvulnerable() && !(target instanceof EntityTemporaryRabbit)) {
				EntityTemporaryRabbit rabbit = new EntityTemporaryRabbit(world);
				rabbit.storeEntity((EntityLivingBase) target);
				rabbit.setPositionAndRotation(target.posX, target.posY, target.posZ, target.rotationYaw, target.rotationPitch);
				rabbit.setLifetimeAsRabbit(Integer.MAX_VALUE);
				world.spawnEntity(rabbit);
				world.removeEntity(target);
			} else {
				if (!world.isRemote && ticksInUse == 1 && caster instanceof EntityPlayer) {
					((EntityPlayer) caster)
							.sendStatusMessage(new TextComponentTranslation("spell.resist", target.getName(),
									this.getNameForTranslationFormatted()), true);
				}
			}

		}

		return true;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	@Override
	protected boolean onMiss(World world, EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		return true;
	}

	@Override
	protected void spawnParticle(World world, double x, double y, double z, double vx, double vy, double vz) {
		ParticleBuilder.create(Type.DARK_MAGIC).pos(x, y, z).clr(0.2f, 0, 0.3f).spawn(world);
		ParticleBuilder.create(Type.DARK_MAGIC).pos(x, y, z).clr(0.1f, 0, 0).spawn(world);
		ParticleBuilder.create(Type.SPARKLE).pos(x, y, z).time(12 + world.rand.nextInt(8)).clr(0.4f, 0, 0).spawn(world);
	}

}
