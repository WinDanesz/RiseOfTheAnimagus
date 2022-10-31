package com.windanesz.morphspellpack.entity.projectile;

import com.windanesz.morphspellpack.registry.MSSounds;
import com.windanesz.morphspellpack.registry.MSSpells;
import electroblob.wizardry.entity.projectile.EntityMagicArrow;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.MagicDamage.DamageType;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.ParticleBuilder.Type;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityRadiantSpark extends EntityMagicArrow {

	/** Creates a new Radiant Spark in the given world. */
	public EntityRadiantSpark(World world){
		super(world);
	}

	@Override public double getDamage(){ return MSSpells.radiant_spark.getProperty(Spell.DAMAGE).floatValue(); }

	@Override public DamageType getDamageType(){ return DamageType.RADIANT; }

	@Override public int getLifetime(){ return 30; }

	@Override public boolean doGravity(){ return false; }

	@Override public boolean doDeceleration(){ return false; }

	@Override public boolean canRenderOnFire(){ return false; }

	@Override
	public void onEntityHit(EntityLivingBase entityHit){
		this.playSound(MSSounds.RADIANT_SPARK_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
	}

	@Override
	protected void tickInAir() {
		if(this.world.isRemote){
			ParticleBuilder.create(Type.SPARKLE, rand, posX, posY, posZ, 0.03, true).clr(0.9f,0.9f,0.9f).fade(1f,1f,1f)
					.time(20 + rand.nextInt(10)).spawn(world);

			if(this.ticksExisted > 1){ // Don't spawn particles behind where it started!
				double x = posX - motionX / 2;
				double y = posY - motionY / 2;
				double z = posZ - motionZ / 2;
				ParticleBuilder.create(Type.SPARKLE, rand, x, y, z, 0.03, true).clr(0xfff385).fade(1f,1f,1f)
						.time(20 + rand.nextInt(10)).spawn(world);
				ParticleBuilder.create(Type.FLASH, rand, x, y, z, 0.03, true).clr(0xfff385).fade(1f,1f,1f)
						.time(20 + rand.nextInt(10)).spawn(world);
			}
		}

		super.tickInAir();

	}

	@Override
	public void onBlockHit(RayTraceResult hit){
		
		// Adds a particle effect when the ice shard hits a block.
		if(this.world.isRemote){
			// Gets a position slightly away from the block hit so the particle doesn't get cut in half by the block face
			Vec3d vec = hit.hitVec.add(new Vec3d(hit.sideHit.getDirectionVec()).scale(0.15));
			ParticleBuilder.create(Type.FLASH).pos(vec).clr(0xfff385).spawn(world);
			
		}
		// Parameters for sound: sound event name, volume, pitch.
		this.playSound(MSSounds.RADIANT_SPARK_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

	}

	@Override
	protected void entityInit(){}

}