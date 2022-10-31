package com.windanesz.morphspellpack.entity.living;

import com.windanesz.morphspellpack.registry.MSSounds;
import com.windanesz.morphspellpack.registry.MSSpells;
import com.windanesz.wizardryutils.entity.ai.EntityAIMinionOwnerHurtByTarget;
import com.windanesz.wizardryutils.entity.ai.EntityAIMinionOwnerHurtTarget;
import com.windanesz.wizardryutils.entity.ai.EntitySummonAIFollowOwner;
import electroblob.wizardry.entity.living.EntityAIAttackSpell;
import electroblob.wizardry.entity.living.EntitySummonedCreature;
import electroblob.wizardry.entity.living.ISpellCaster;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.ParticleBuilder.Type;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;

public class EntityLightWisp extends EntitySummonedCreature implements ISpellCaster {

	// TODO: This currently doesn't fly like it used to. Should it, or does it not matter?

	private double AISpeed = 1.0;

	private EntityAIAttackSpell<EntityLightWisp> spellAttackAI = new EntityAIAttackSpell<EntityLightWisp>(this, AISpeed, 15f, 2, 0);

	private static final List<Spell> attack = Collections.singletonList(MSSpells.radiant_spark);

	/** Creates a new light wisp in the given world. */
	public EntityLightWisp(World world){
		super(world);
		// For some reason this can't be in initEntityAI
		this.tasks.addTask(0, this.spellAttackAI);
	}

	@Override
	protected void initEntityAI(){

		this.tasks.addTask(1, new EntityAIAttackMelee(this, AISpeed, false));
		this.tasks.addTask(3, new EntityAIWander(this, AISpeed));
		this.tasks.addTask(5, new EntityAILookIdle(this));
		this.tasks.addTask(2, new EntitySummonAIFollowOwner(this, 1.0D, 4.0F, 2.0F));

		// target enemies that hurt the owner
		this.targetTasks.addTask(1, new EntityAIMinionOwnerHurtByTarget(this));
		// target enemies targeted by the owner
		this.targetTasks.addTask(2, new EntityAIMinionOwnerHurtTarget(this));


		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityLivingBase.class,
				0, false, true, this.getTargetSelector()));

		//this.setAIMoveSpeed((float)AISpeed);
	}

	@Override
	public boolean hasRangedAttack(){
		return true;
	}

	@Override
	public List<Spell> getSpells(){
		return attack;
	}

	@Override
	protected void applyEntityAttributes(){
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(AISpeed);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
	}

	@Override
	public boolean isPotionApplicable(PotionEffect potion){
		return potion.getPotion() == MobEffects.WITHER ? false : super.isPotionApplicable(potion);
	}

//	@Override
//	protected SoundEvent getAmbientSound(){
//		return WizardrySounds.ENTITY_SHADOW_WRAITH_AMBIENT;
//	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source){
		return MSSounds.LIGHT_WISP_HURT;
	}

	@Override
	protected SoundEvent getDeathSound(){
		return MSSounds.LIHT_WISP_DEATH;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(){
		return 15728880;
	}

	@Override
	public float getBrightness(){
		return 1.0F;
	}

	@Override
	public void onSpawn(){
		if(this.world.isRemote){
			for(int i = 0; i < 15; i++){
				ParticleBuilder.create(Type.SPARKLE, this).vel(0, 0.05, 0).time(20 + rand.nextInt(10))
				.clr(0.9f, 0.9f, 0.9f).spawn(world);
			}
		}
	}

	@Override
	public void onLivingUpdate(){
		// Makes the wisp hover.
//		Integer floorLevel = BlockUtils.getNearestFloor(world, new BlockPos(this), 4);
//
//		if(floorLevel == null || this.posY - floorLevel > 2){
//			this.motionY = -0.1;
//		}else if(this.posY - floorLevel < 1){
//			this.motionY = 0.1;
//		}else{
//			this.motionY = 0.0;
//		}

		if(this.rand.nextInt(24) == 0){
			this.playSound(MSSounds.LIGHT_WISP_NOISE, 1.0F + this.rand.nextFloat(),
					this.rand.nextFloat() * 0.7F + 1F);
		}

		// Slow fall
		if(!this.onGround && this.motionY < 0.0D){
			this.motionY *= 0.6D;
		}

		if(world.isRemote){

			for(int i=0; i<1; i++){

//				world.spawnParticle(EnumParticleTypes.PORTAL,
//						this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width,
//						this.posY + this.rand.nextDouble() * (double)this.height,
//						this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0, 0, 0);
				ParticleBuilder.create(Type.SPARKLE).pos(
						this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width,
						this.posY + 0.5f + (double)this.height * 0.25 +  this.rand.nextDouble() * (double)this.height * 0.25,
						this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width)
						.spin(0.5 * this.rand.nextFloat(),0.05)
						.vel(0,0.01,0)
						.fade(1f,1f,1f)
						.clr(0.9f,0.9f,0.9f)
						.spawn(world);


				ParticleBuilder.create(Type.SPARKLE, this).pos(0,1,0).vel(0, 0.05, 0).time(20 + rand.nextInt(10))
				.clr(0.9f, 0.9f,0.9f).spawn(world);

				//ParticleBuilder.create(Type.DARK_MAGIC, this).clr(0.9f, 0.9f, 0.9f).spawn(world);

			}
			ParticleBuilder.create(Type.FLASH).entity(this).pos(0,1.3,0).time(15).clr(0.9f, 0.9f, 0.9f).spawn(world);
			ParticleBuilder.create(Type.FLASH).entity(this).scale(0.5f).pos(0,1.3,0).time(15).clr(0xfff385).spawn(world);
		}

		super.onLivingUpdate();
	}

	@Override
	public void fall(float distance, float damageMultiplier){
		// Immune to fall damage.
	}

}
