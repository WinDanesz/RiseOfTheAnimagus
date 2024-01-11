package com.windanesz.morphspellpack.spell;

import com.windanesz.morphspellpack.MorphSpellPack;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.ParticleBuilder.Type;
import electroblob.wizardry.util.SpellModifiers;
import me.ichun.mods.morph.common.Morph;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class LichFlight extends Spell {

	public static final String SPEED = "speed";
	public static final String ACCELERATION = "acceleration";

	private static final double Y_NUDGE_ACCELERATION = 0.075;

	public LichFlight(){
		super(MorphSpellPack.MODID, "lich_flight", SpellActions.POINT, true);
		addProperties(SPEED, ACCELERATION);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers){

		if(!caster.isElytraFlying()){

			float speed = getProperty(SPEED).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			float acceleration = getProperty(ACCELERATION).floatValue() * modifiers.get(SpellModifiers.POTENCY);

			// The division thingy checks if the look direction is the opposite way to the velocity. If this is the
			// case then the velocity should be added regardless of the player's current speed.
			if((Math.abs(caster.motionX) < speed || caster.motionX / caster.getLookVec().x < 0)
					&& (Math.abs(caster.motionZ) < speed || caster.motionZ / caster.getLookVec().z < 0)){
				caster.addVelocity(caster.getLookVec().x * acceleration, 0, caster.getLookVec().z * acceleration);
			}
			// y velocity is handled separately to stop the player from falling from the sky when they reach maximum
			// horizontal speed.
			if(Math.abs(caster.motionY) < speed || caster.motionY / caster.getLookVec().y < 0){
				caster.motionY += caster.getLookVec().y * acceleration + Y_NUDGE_ACCELERATION;
			}
			if(!Wizardry.settings.replaceVanillaFallDamage) caster.fallDistance = 0.0f;
			caster.fallDistance *= 0.85f;
		}
		
		if(world.isRemote){

			double x = caster.posX - 0.5 + world.rand.nextDouble() * 0.5;
			double y = caster.posY + caster.getEyeHeight() - 1.5 + world.rand.nextDouble();
			double z = caster.posZ - 0.5 + world.rand.nextDouble() * 0.5;
			ParticleBuilder.create(Type.FLASH).pos(x, y, z).vel(0, -0.1, 0).time(30).clr(2, 199, 140).spawn(world);
			x = caster.posX - 0.5 + world.rand.nextDouble()  * 0.5;
			y = caster.posY + caster.getEyeHeight() - 1.5 + world.rand.nextDouble();
			z = caster.posZ - 0.5 + world.rand.nextDouble() * 0.5;
			ParticleBuilder.create(Type.FLASH).pos(x, y, z).vel(0, -0.1, 0).time(30).clr(2, 199, 140).spawn(world);
		}
		
		//if(ticksInUse % 24 == 0) playSound(world, caster, ticksInUse, -1, modifiers);
		
		return true;
	}

}
