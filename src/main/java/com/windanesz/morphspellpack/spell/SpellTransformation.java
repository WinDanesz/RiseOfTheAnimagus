package com.windanesz.morphspellpack.spell;

import com.windanesz.morphspellpack.MorphSpellPack;
import com.windanesz.morphspellpack.entity.living.EntityLich;
import com.windanesz.morphspellpack.handler.LichHandler;
import com.windanesz.morphspellpack.registry.MSItems;
import com.windanesz.morphspellpack.registry.MSPotions;
import electroblob.wizardry.data.IStoredVariable;
import electroblob.wizardry.data.Persistence;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import me.ichun.mods.morph.common.Morph;
import me.ichun.mods.morph.common.handler.PlayerMorphHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

public class SpellTransformation extends Spell {

	public static final IStoredVariable<Integer> MORPH_DURATION = IStoredVariable.StoredVariable.ofInt("morphCountdown", Persistence.ALWAYS).withTicker(SpellTransformation::update);
	public static final IStoredVariable<String> LAST_MORPH = IStoredVariable.StoredVariable.ofString("lastMorph", Persistence.ALWAYS);
	String morph;

	public SpellTransformation(String name, EnumAction action, boolean isContinuous, String morph) {
		super(MorphSpellPack.MODID, name, action, isContinuous);
		this.morph = morph;
		this.addProperties(DURATION);
		WizardData.registerStoredVariables(MORPH_DURATION, LAST_MORPH);
	}

	public static boolean isShapeLocked(EntityLivingBase entityLiving) {
		return (entityLiving.isPotionActive(MSPotions.shape_lock) || entityLiving.isPotionActive(MSPotions.curse_of_transformation));
	}

	private static Integer update(EntityPlayer player, Integer countdown) {
		 if (LichHandler.isLich(player) && player.ticksExisted == 20) {
			morphPlayer((EntityLivingBase) player, LichHandler.LICH, 5);
		}

		if (countdown == null) { return 0; }
		if (!player.world.isRemote && !isShapeLocked(player)) {
			if (countdown > 0) {
				countdown--;
			}

			if (countdown == 1) {
				if (Morph.eventHandlerServer.morphsActive.containsKey(player.getName())) {
					// demorph
					demorphPlayer(player);
//					PlayerMorphHandler.getInstance().forceDemorph((EntityPlayerMP) player);
				}
			}
		}
		return countdown;
	}

	public static boolean morphPlayer(EntityLivingBase player, String ent, int duration) {
		if (!(player instanceof EntityPlayerMP)) {
			if (isShapeLocked(player)) {
				return false;
			}
		}

		Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(ent), player.world);
		if (entity instanceof EntityLivingBase && player instanceof EntityPlayerMP) {
			if (!PlayerMorphHandler.getInstance().forceMorph((EntityPlayerMP) player, (EntityLivingBase) entity)) {
				return false;
			}
			WizardData data = WizardData.get((EntityPlayer) player);
			if (data != null) {

				// this ring doubles the duration
				if (ItemArtefact.isArtefactActive((EntityPlayer) player, MSItems.ring_transformation)) {
					duration = duration * 2;
				}
				data.setVariable(LAST_MORPH, ent);
				data.setVariable(MORPH_DURATION, duration);
				data.sync();
			}
			return true;
		}
		return false;
	}

	public static boolean demorphPlayer(EntityLivingBase player) {
		if (player != null && isShapeLocked(player)) {
			return false;
		}

		if (player instanceof EntityPlayerMP) {
			if (LichHandler.isLich(player)) {
				morphPlayer(player, LichHandler.LICH, -1);
				return true;
			}

			if (PlayerMorphHandler.getInstance().forceDemorph((EntityPlayerMP) player)) {
				WizardData data = WizardData.get((EntityPlayer) player);
				if (data != null) {
					data.setVariable(MORPH_DURATION, 0);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		return morph(world, caster, hand, ticksInUse, modifiers);
	}

	public boolean morph(World world, EntityLivingBase caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers) {
		if (world.isRemote) { this.playSound(world, caster, ticksInUse, -1, modifiers); }

		if (!world.isRemote && caster instanceof EntityPlayer) {
			// check if the player is morphing or morphed already. Revert to human if already morphed
			if (Morph.eventHandlerServer.morphsActive.containsKey(caster.getName())
					&& !Morph.eventHandlerServer.morphsActive.get(caster.getName()).isMorphing() &&
					!(PlayerMorphHandler.getInstance().getMorphEntity(world, caster.getName(), Side.SERVER) instanceof EntityLich)) {
				demorphPlayer(caster);
				return false;
			}
			int duration = getProperty(DURATION).intValue();

			WizardData.get((EntityPlayer) caster).stopCastingContinuousSpell();;
			boolean flag = morphPlayer(caster, morph, duration);
			if (flag) {
				morphExtra(world, caster, morph, duration);
			}
			return flag;
		}

		return false;
	}

	public void morphExtra(World world, EntityLivingBase caster, String morph, int morphDuration) {}
}
