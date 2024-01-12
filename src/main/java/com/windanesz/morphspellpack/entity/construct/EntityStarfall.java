package com.windanesz.morphspellpack.entity.construct;

import com.windanesz.morphspellpack.entity.projectile.EntityRadiantSpark;
import com.windanesz.morphspellpack.registry.MSItems;
import com.windanesz.morphspellpack.registry.MSSpells;
import electroblob.wizardry.entity.construct.EntityScaledConstruct;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.spell.Spell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityStarfall extends EntityScaledConstruct {

	public EntityStarfall(World world) {
		super(world);
		setSize(MSSpells.starfall.getProperty(Spell.EFFECT_RADIUS).floatValue() * 2, 5);
	}

	public void setRadius(float radiusIn) {
		this.setSize(radiusIn, 5);
	}

	@Override
	protected boolean shouldScaleHeight() {
		return false;
	}

	public void onUpdate() {

		super.onUpdate();

		if (!this.world.isRemote) {
			boolean hasArtefact = getCaster() instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer) getCaster(), MSItems.ring_starfall);
			int count = 1;
			if (hasArtefact) {
				count = 10;
			}

			for (int i = 0; i < count; i++) {
				double x = posX + (world.rand.nextDouble() - 0.5D) * (double) width;
				double y = posY + world.rand.nextDouble() * (double) height;
				double z = posZ + (world.rand.nextDouble() - 0.5D) * (double) width;

				EntityRadiantSpark radiantSpark = new EntityRadiantSpark(world);
				radiantSpark.setPosition(x, y, z);
				if (!hasArtefact) {
					radiantSpark.motionX = MathHelper.cos((float) Math.toRadians(this.rotationYaw + 90));
					radiantSpark.motionZ = MathHelper.sin((float) Math.toRadians(this.rotationYaw + 90));
				}
				radiantSpark.motionY = -0.6f;

				radiantSpark.setCaster(this.getCaster());
				radiantSpark.damageMultiplier = this.damageMultiplier;

				this.world.spawnEntity(radiantSpark);
			}
		}
	}

}
