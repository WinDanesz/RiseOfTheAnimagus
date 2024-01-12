package com.windanesz.morphspellpack.ability.active;

import com.windanesz.morphspellpack.ability.IActiveAbility;
import com.windanesz.wizardryutils.tools.WizardryUtilsTools;
import me.ichun.mods.morph.api.ability.Ability;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class AbilityItemSpawn extends Ability implements IActiveAbility {

	public static final String name = "itemSpawn";
	private String itemName;
	public Boolean toggled; //reset motionY when the triggered?

	public int maxCooldown = 0;
	public int cooldown = 0;
	public int count = 0;
	public boolean dropItem = true;
	Item item = Items.AIR;

	@SuppressWarnings("unused")
	public AbilityItemSpawn() {
	}

	public AbilityItemSpawn(String itemName, int count, int maxCooldown, boolean dropItem) {
		this.itemName = itemName;
		this.count = count;
		this.maxCooldown = maxCooldown;
		this.dropItem = dropItem;
		if (ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName))) {
			this.item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
		}
	}

	@Override
	public Ability parse(String[] args) {
		itemName = args[0];
		count = Integer.parseInt(args[1]);
		maxCooldown = Integer.parseInt(args[2]);
		dropItem = Boolean.parseBoolean(args[3]);
		if (ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName))) {
			this.item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
		}
		return this;
	}

	@Override
	public Ability clone() {
		return new AbilityItemSpawn(itemName, count, maxCooldown, dropItem);
	}

	@Override
	public String getType() {
		return name;
	}

	@Override
	public void tick() {
		if (cooldown > 0) {
			cooldown--;
			toggled = false;
		}

		if (!getParent().world.isRemote && toggled != null && toggled && conditionPredicate()) {

			if (getParent() instanceof EntityPlayerMP) {

				EntityPlayerMP caster = (EntityPlayerMP) getParent();
				if (dropItem) {
				    caster.dropItem(new ItemStack(item, count), true);
				} else {
					WizardryUtilsTools.giveStackToPlayer(caster, new ItemStack(item, count));
				}
				cooldown = maxCooldown;
			}
		}

		toggled = false;
	}

	@Override
	public void toggleAbility() {
		toggled = true;
	}
}
