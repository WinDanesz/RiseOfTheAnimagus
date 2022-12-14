package com.windanesz.morphspellpack.entity.living;

import electroblob.wizardry.Wizardry;
import electroblob.wizardry.entity.living.ISummonedCreature;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityBatMinion extends EntityBat implements ISummonedCreature {
	private static final DataParameter<Boolean> SPAWN_PARTICLES = EntityDataManager.createKey(EntityBatMinion.class, DataSerializers.BOOLEAN);

	// Field implementations
	private int lifetime = -1;
	private UUID casterUUID;

	// Setter + getter implementations
	@Override
	public int getLifetime() { return lifetime; }

	@Override
	public void setLifetime(int lifetime) { this.lifetime = lifetime; }

	@Override
	public UUID getOwnerId() { return casterUUID; }

	@Override
	public void setOwnerId(UUID uuid) { this.casterUUID = uuid; }

	public EntityBatMinion(World worldIn) {
		super(worldIn);
		this.experienceValue = 0;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(SPAWN_PARTICLES, true);
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(9.0D);
	}

	@Override
	public boolean isChild() { return false; }

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {} // They don't have equipment!

	@Override
	public void onKillEntity(EntityLivingBase entityLivingIn) {} // Turns villagers to zombies in EntityZombie

	// Implementations

	@Override
	public void setRevengeTarget(EntityLivingBase entity) {
		if (this.shouldRevengeTarget(entity)) { super.setRevengeTarget(entity); }
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.updateDelegate();
	}

	@Override
	public void onSpawn() {
		if (this.dataManager.get(SPAWN_PARTICLES)) { this.spawnParticleEffect(); }
	}

	@Override
	public void onDespawn() {
		this.spawnParticleEffect();
	}

	private void spawnParticleEffect() {
		if (this.world.isRemote) {
			for (int i = 0; i < 15; i++) {
				this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + this.rand.nextFloat() - 0.5f,
						this.posY + this.rand.nextFloat() * 2, this.posZ + this.rand.nextFloat() - 0.5f, 0, 0, 0);
			}
		}
	}

	@Override
	public boolean hasParticleEffect() {
		return true;
	}

	@Override
	public boolean hasAnimation() {
		return this.dataManager.get(SPAWN_PARTICLES) || this.ticksExisted > 20;
	}

	public void hideParticles() {
		this.dataManager.set(SPAWN_PARTICLES, false);
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		// In this case, the delegate method determines whether super is called.
		// Rather handily, we can make use of Java's short-circuiting method of evaluating OR statements.
		return this.interactDelegate(player, hand) || super.processInteract(player, hand);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		this.writeNBTDelegate(nbttagcompound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.readNBTDelegate(nbttagcompound);
	}

	// Recommended overrides

	@Override
	protected int getExperiencePoints(EntityPlayer player) { return 0; }

	@Override
	protected boolean canDropLoot() { return false; }

	@Override
	protected Item getDropItem() { return null; }

	@Override
	protected ResourceLocation getLootTable() { return null; }

	@Override
	public boolean canPickUpLoot() { return false; }

	// This vanilla method has nothing to do with the custom despawn() method.
	@Override
	protected boolean canDespawn() {
		return getCaster() == null && getOwnerId() == null;
	}

	@Override
	public boolean getCanSpawnHere() {
		return this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
	}

	@Override
	public boolean canAttackClass(Class<? extends EntityLivingBase> entityType) {
		// Returns true unless the given entity type is a flying entity.
		return !EntityFlying.class.isAssignableFrom(entityType);
	}

	@Override
	public ITextComponent getDisplayName() {
		if (getCaster() != null) {
			return new TextComponentTranslation(NAMEPLATE_TRANSLATION_KEY, getCaster().getName(),
					new TextComponentTranslation("entity." + this.getEntityString() + ".name"));
		} else {
			return super.getDisplayName();
		}
	}

	@Override
	public boolean hasCustomName() {
		// If this returns true, the renderer will show the nameplate when looking directly at the entity
		return Wizardry.settings.summonedCreatureNames && getCaster() != null;
	}

}
