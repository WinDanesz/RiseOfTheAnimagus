package com.windanesz.morphspellpack.entity.living;

import com.windanesz.morphspellpack.spell.SpellTransformation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityTemporaryRabbit extends EntityRabbit {

	private NBTTagCompound entityInNbt = new NBTTagCompound();
	private int lifetimeAsRabbit = -1;

	public EntityTemporaryRabbit(World worldIn) {
		super(worldIn);
	}

	@Nullable
	@Override
	protected ResourceLocation getLootTable() {
		return null;
	}

	@Override
	protected int getExperiencePoints(EntityPlayer player) {
		return 0;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (this.lifetimeAsRabbit > 0 && !SpellTransformation.isShapeLocked(this)) {
			lifetimeAsRabbit--;
		} else if (this.lifetimeAsRabbit == 0) {
			restoreEntity();
		}
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		// these little fellas have a bit more hp than usual
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(9.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) { return false; }

	@Override
	public boolean isInLove() { return false; }

	public int getLifetimeAsRabbit() {
		return lifetimeAsRabbit;
	}

	public void setLifetimeAsRabbit(int lifetimeAsRabbit) {
		this.lifetimeAsRabbit = lifetimeAsRabbit;
	}

	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setTag("StoredEntity", this.entityInNbt);
	}

	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		if (compound.hasKey("StoredEntity")) {
			this.entityInNbt = compound.getCompoundTag("StoredEntity");
		}
	}

	public void storeEntity(EntityLivingBase entity) {
		if (!this.world.isRemote) {
			this.entityInNbt = entity.serializeNBT();
		}
	}

	public void restoreEntity() {
		if (!this.world.isRemote) {
			Entity mob = EntityList.createEntityFromNBT(entityInNbt, this.world);
			if (mob != null) {
				mob.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
				this.world.spawnEntity(mob);
				this.world.removeEntity(this);
			}
		}
	}

	@Override
	public void onDeath(DamageSource cause) {
		this.restoreEntity();
		super.onDeath(cause);
	}
}
