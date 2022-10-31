package com.windanesz.morphspellpack.items;

import com.windanesz.morphspellpack.MorphSpellPack;
import com.windanesz.morphspellpack.client.gui.MSGuiHandler;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemLichTome extends ItemArtefact {

	public ItemLichTome(EnumRarity rarity, Type type) {
		super(rarity, type);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		player.openGui(MorphSpellPack.instance, MSGuiHandler.LICH_TOME, world, 0, 0, 0);
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

}
