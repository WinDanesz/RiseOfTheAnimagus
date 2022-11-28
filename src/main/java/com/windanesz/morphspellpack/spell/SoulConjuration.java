package com.windanesz.morphspellpack.spell;

import com.windanesz.morphspellpack.MorphSpellPack;
import com.windanesz.morphspellpack.items.ItemSoulPhylactery;
import com.windanesz.morphspellpack.registry.MSItems;
import com.windanesz.morphspellpack.registry.MSPotions;
import com.windanesz.wizardryutils.capability.SummonedCreatureData;
import com.windanesz.wizardryutils.entity.ai.EntityAIMinionOwnerHurtByTarget;
import com.windanesz.wizardryutils.entity.ai.EntityAIMinionOwnerHurtTarget;
import com.windanesz.wizardryutils.integration.baubles.BaublesIntegration;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SoulConjuration extends Spell {

	public static final String SOUL_COST = "soul_cost";
	private static final String SUMMON_RADIUS = "summon_radius";
	private static final String MINION_COUNT = "minion_count";
	private static final String PERCENT_COST_PER_HP = "percent_cost_per_hp";

	public SoulConjuration() {

		super(MorphSpellPack.MODID, "soul_conjuration", SpellActions.SUMMON, false);
		addProperties(DURATION, SOUL_COST, SUMMON_RADIUS, MINION_COUNT, PERCENT_COST_PER_HP);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (ItemArtefact.isArtefactActive(caster, MSItems.charm_soul_phylactery)) {
			ItemStack stack = BaublesIntegration.getEquippedArtefactStacks(caster, ItemArtefact.Type.CHARM).get(0);
			if (ItemSoulPhylactery.hasEntity(stack) && ItemSoulPhylactery.getPercentFilled(stack) >= getProperty(SOUL_COST).floatValue()) {
				String entity = ItemSoulPhylactery.getEntity(stack);
				return spawnMinions(world, caster, modifiers, entity);
			}

		}
		return false;
	}

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) {
		return false;
	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) {
		return false;
	}

	protected boolean spawnMinions(World world, EntityLivingBase caster, SpellModifiers modifiers, String ent) {

		if (!world.isRemote) {
			for (int i = 0; i < getProperty(MINION_COUNT).intValue(); i++) {

				int range = getProperty(SUMMON_RADIUS).intValue();

				// Try and find a nearby floor space
				BlockPos pos = BlockUtils.findNearbyFloorSpace(caster, range, range * 2);

				// If there was no floor around and the entity isn't a flying one, the spell fails.
				// As per the javadoc for findNearbyFloorSpace, there's no point trying the rest of the minions.
				if (pos == null) { return false; }

				Entity minion = EntityList.createEntityByIDFromName(new ResourceLocation(ent), caster.world);

				if (minion == null) { return false; }

				minion.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
				if (minion instanceof EntityLiving) {
					((EntityLiving) minion).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(minion)), (IEntityLivingData) null);
				}

				if (minion instanceof EntityCreature) {
					// target enemies that hurt the owner
					((EntityLiving) minion).targetTasks.addTask(1, new EntityAIMinionOwnerHurtByTarget((EntityCreature) minion));
					// target enemies targeted by the owner
					((EntityLiving) minion).targetTasks.addTask(2, new EntityAIMinionOwnerHurtTarget((EntityCreature) minion));
				}

				if (minion instanceof EntityLivingBase) {

					SummonedCreatureData data = SummonedCreatureData.get((EntityLivingBase) minion);
					if (data == null) return false;
					data.setLifetime((int) (getProperty(DURATION).intValue() * modifiers.get(WizardryItems.duration_upgrade)));
					data.setFollowOwner(true);
					data.setCaster(caster);
					((EntityLivingBase) minion).addPotionEffect(new PotionEffect(MSPotions.conjured_soul, Integer.MAX_VALUE));
					world.spawnEntity(minion);

					float cost = getProperty(PERCENT_COST_PER_HP).floatValue() * ((EntityLivingBase) minion).getMaxHealth();
					ItemStack stack = BaublesIntegration.getEquippedArtefactStacks((EntityPlayer) caster, ItemArtefact.Type.CHARM).get(0);
					ItemSoulPhylactery.consumePercent(stack, cost);
					BaublesIntegration.setArtefactToSlot((EntityPlayer) caster, stack, ItemArtefact.Type.CHARM);
				}
			}

		}
		return true;
	}

}
