package com.windanesz.morphspellpack.items;

import com.windanesz.morphspellpack.spell.SpellTransformation;
import com.windanesz.wizardryutils.item.ITickableArtefact;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ItemShadowBottle extends ItemArtefact implements ITickableArtefact {

	public ItemShadowBottle(EnumRarity rarity, Type type) {
		super(rarity, type);
	}

	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		if (player.isSprinting() && !((EntityPlayer) player).getCooldownTracker().hasCooldown(itemstack.getItem())) {
			SpellTransformation.morphPlayer(player, "ebwizardry:shadow_wraith", 20);
			((EntityPlayer) player).getCooldownTracker().setCooldown(itemstack.getItem(),60);
		}
	}
}
