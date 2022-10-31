package com.windanesz.morphspellpack.entity.construct;

import com.windanesz.morphspellpack.entity.projectile.EntityRadiantSpark;
import com.windanesz.morphspellpack.registry.MSSpells;
import electroblob.wizardry.entity.construct.EntityScaledConstruct;
import electroblob.wizardry.spell.Spell;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityStarfall extends EntityScaledConstruct {

	public EntityStarfall(World world){
		super(world);
		setSize(MSSpells.starfall.getProperty(Spell.EFFECT_RADIUS).floatValue() * 2, 5);
	}

	@Override
	protected boolean shouldScaleHeight(){
		return false;
	}

	public void onUpdate(){

		super.onUpdate();

		if(!this.world.isRemote){

			double x = posX + (world.rand.nextDouble() - 0.5D) * (double)width;
			double y = posY + world.rand.nextDouble() * (double)height;
			double z = posZ + (world.rand.nextDouble() - 0.5D) * (double)width;

			EntityRadiantSpark iceshard = new EntityRadiantSpark(world);
			iceshard.setPosition(x, y, z);

			iceshard.motionX = MathHelper.cos((float)Math.toRadians(this.rotationYaw + 90));
			iceshard.motionY = -0.6;
			iceshard.motionZ = MathHelper.sin((float)Math.toRadians(this.rotationYaw + 90));

			iceshard.setCaster(this.getCaster());
			iceshard.damageMultiplier = this.damageMultiplier;

			this.world.spawnEntity(iceshard);
		}
	}

}
