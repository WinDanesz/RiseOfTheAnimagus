package com.windanesz.morphspellpack.items;

import com.windanesz.morphspellpack.Settings;
import com.windanesz.morphspellpack.spell.SpellTransformation;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class ItemDruidStone extends ItemArtefact {

	public static final String ENTITY_TAG = "Entity";

	public ItemDruidStone(EnumRarity rarity, Type type) {
		super(rarity, type);
	}

	public static boolean isAllowedEnity(String entity) {
		List<String> entities = Arrays.asList(Settings.generalSettings.druid_stone_mob_list);
		boolean blacklist = Settings.generalSettings.druid_stone_list_is_blacklist;
		if (blacklist && entities.contains(entity)) {
			return false;
		}
		if (!blacklist && !entities.contains(entity)) {
			return false;
		}
		return true;
	}

	public static void setEntity(ItemStack stack, String entity) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString(ENTITY_TAG, entity);
		stack.setTagCompound(compound);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);

		//noinspection ConstantConditions
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey(ENTITY_TAG)) {
			String entity = stack.getTagCompound().getString(ENTITY_TAG);
			if (isAllowedEnity(entity)) {
				if (SpellTransformation.morphPlayer(player, entity, Settings.generalSettings.druid_stone_duration))
				player.getCooldownTracker().setCooldown(stack.getItem(), 200);
			}
		} else {
			// Setting a random entity
			boolean blacklist = Settings.generalSettings.druid_stone_list_is_blacklist;
			if (!blacklist) {
				List<String> entities = Arrays.asList(Settings.generalSettings.druid_stone_mob_list);
				if (!entities.isEmpty()) {
					String entity = entities.get(world.rand.nextInt(entities.size() - 1));
					setEntity(stack, entity);
				}
			} else {
				List<String> blacklistedEntities = Arrays.asList(Settings.generalSettings.druid_stone_mob_list);
				List<EntityEntry> list = ForgeRegistries.ENTITIES.getValues();
				if (!list.isEmpty()) {
					for (int i = 0; i < 30; i++) {
						EntityEntry entry = list.get(world.rand.nextInt(list.size() - 1));
						Entity entity = entry.newInstance(world);
						if (entity instanceof EntityCreature || entity instanceof EntityAmbientCreature) {
							String name = EntityList.getKey(entity).toString();
							if (blacklistedEntities.isEmpty() || !blacklistedEntities.contains(name)) {
								setEntity(stack, name);
								break;
							}
						}
					}
				}
			}
		}

		return new ActionResult<>(EnumActionResult.PASS, stack);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		if (world != null && stack.hasTagCompound() && stack.getTagCompound().hasKey(ENTITY_TAG)) {
			EntityEntry entry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(stack.getTagCompound().getString(ENTITY_TAG)));
			if (entry != null) {
				tooltip.add(entry.newInstance(world).getName());
			}
		} else {
			Wizardry.proxy.addMultiLineDescription(tooltip, "item.morphspellpack:charm_druid_stone.desc_initial");
		}
		super.addInformation(stack, world, tooltip, advanced);
	}
}
