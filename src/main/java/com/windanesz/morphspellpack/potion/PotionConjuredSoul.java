package com.windanesz.morphspellpack.potion;

import electroblob.wizardry.potion.Curse;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class PotionConjuredSoul extends Curse {
	public PotionConjuredSoul(boolean isBadEffect, int liquidColour, ResourceLocation texture) {
		super(isBadEffect, liquidColour, texture);
	}


	@Override
	public List<ItemStack> getCurativeItems(){
		return new ArrayList<>();
	}
}
