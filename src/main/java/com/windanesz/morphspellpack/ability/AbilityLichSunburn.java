package com.windanesz.morphspellpack.ability;

import com.windanesz.morphspellpack.handler.LichHandler;
import com.windanesz.morphspellpack.registry.MSItems;
import com.windanesz.morphspellpack.registry.MSPotions;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.item.ItemArtefact;
import me.ichun.mods.morph.api.ability.Ability;
import me.ichun.mods.morph.api.ability.type.AbilityFireImmunity;
import me.ichun.mods.morph.common.Morph;
import me.ichun.mods.morph.common.handler.AbilityHandler;
import me.ichun.mods.morph.common.morph.MorphInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AbilityLichSunburn extends Ability {
	public AbilityFireImmunity fireImmunityInstance = new AbilityFireImmunity();
	public static final ResourceLocation iconResource = new ResourceLocation("morph", "textures/icon/sunburn.png");

	public static final String name = "lichSunburn";
	private boolean hasHealingCrystals = false;

	@Override
	public String getType() {
		return name;
	}

	public void tick() {
		boolean isChild = false;
		if (!this.getParent().getEntityWorld().isRemote && this.getParent() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) this.getParent();
			MorphInfo info = (MorphInfo) Morph.eventHandlerServer.morphsActive.get(player.getName());
			if (player.capabilities.isCreativeMode) {
				isChild = true;
			}

			if (info != null && info.nextState.getEntInstance(this.getParent().getEntityWorld()).isChild()) {
				isChild = true;
			}
		}

		if (this.getParent() instanceof EntityPlayer && this.getParent().getEntityWorld().isDaytime() && !this.getParent().getEntityWorld().isRemote && !isChild) {
			float f = this.getParent().getBrightness();
			if (getParent().ticksExisted % 60 == 0) {
				hasHealingCrystals = LichHandler.getCrystalPyramid(this.getParent()).get(4).filter(el -> el == Element.HEALING).isPresent();
			}
			if (f > 0.5F && this.getParent().getRNG().nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.getParent().getEntityWorld().canBlockSeeSky(new BlockPos(MathHelper.floor(this.getParent().posX), MathHelper.floor(this.getParent().posY), MathHelper.floor(this.getParent().posZ)))) {
				boolean shouldBurn = !ItemArtefact.isArtefactActive((EntityPlayer) this.getParent(), MSItems.body_undead_sunburn) && !hasHealingCrystals && !this.getParent().isPotionActive(MSPotions.umbral_veil);
				ItemStack itemstack = this.getParent().getItemStackFromSlot(EntityEquipmentSlot.HEAD);

				if (shouldBurn && !(itemstack.getItem() instanceof ItemAir)) {
					if (itemstack.isItemStackDamageable()) {
						itemstack.setItemDamage(itemstack.getItemDamage() + this.getParent().getRNG().nextInt(2));
						if (itemstack.getItemDamage() >= itemstack.getMaxDamage()) {
							this.getParent().renderBrokenItemStack(itemstack);
							this.getParent().setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
						}
					}

					shouldBurn = false;
				}

				if (shouldBurn) {
					this.getParent().setFire(8);
				}
			}
		}

	}

	@SideOnly(Side.CLIENT)
	public boolean entityHasAbility(EntityLivingBase living) {
		if (AbilityHandler.getInstance().hasAbility(living.getClass(), "fireImmunity") && this.fireImmunityInstance.entityHasAbility(living)) {
			return false;
		} else {
			return !living.isChild();
		}
	}

	@SideOnly(Side.CLIENT)
	public ResourceLocation getIcon() {
		return iconResource;
	}
}

