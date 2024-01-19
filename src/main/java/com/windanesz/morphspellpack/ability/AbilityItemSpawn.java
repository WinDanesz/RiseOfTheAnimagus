package com.windanesz.morphspellpack.ability;

import com.windanesz.wizardryutils.tools.WizardryUtilsTools;
import me.ichun.mods.morph.api.ability.Ability;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AbilityItemSpawn extends Ability implements IActiveAbility {

	public static final String name = "itemSpawn";
	private List<String> itemNames = new ArrayList<>();
	public Boolean toggled;

	public int maxCooldown = 0;
	public int cooldown = 0;
	public int count = 0;
	public boolean dropItem = true;
	List<Item> items = new ArrayList<>();

	@SuppressWarnings("unused")
	public AbilityItemSpawn() {
	}

	public AbilityItemSpawn(List<String> itemNames, int count, int maxCooldown, boolean dropItem) {
		this.itemNames = itemNames;
		this.count = count;
		this.maxCooldown = maxCooldown;
		this.dropItem = dropItem;

		for (String name : itemNames) {
			if (ForgeRegistries.ITEMS.containsKey(new ResourceLocation(name))) {
				this.items.add(ForgeRegistries.ITEMS.getValue(new ResourceLocation(name)));
			}
		}
	}

	@Override
	public Ability parse(String[] args) {
		count = Integer.parseInt(args[0]);
		maxCooldown = Integer.parseInt(args[1]);
		dropItem = Boolean.parseBoolean(args[2]);
		if (args.length == 4) {
			this.itemNames.add(args[3]);
		} else if (args.length > 4) {
			this.itemNames.addAll(Arrays.asList(args).subList(3, args.length));
		}
		return this;
	}

	@Override
	public Ability clone() {
		return new AbilityItemSpawn(itemNames, count, maxCooldown, dropItem);
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
					caster.dropItem(new ItemStack(getRandomItem(), count), true);
				} else {
					WizardryUtilsTools.giveStackToPlayer(caster, new ItemStack(getRandomItem(), count));
				}
				cooldown = maxCooldown;
			}
		}

		toggled = false;
	}

	public Item getRandomItem() {
		return this.items.get(getParent().world.rand.nextInt(this.items.size()));
	}

	@Override
	public void toggleAbility() {
		toggled = true;
	}
}
