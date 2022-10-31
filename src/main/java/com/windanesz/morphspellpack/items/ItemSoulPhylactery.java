package com.windanesz.morphspellpack.items;

import com.windanesz.morphspellpack.Settings;
import com.windanesz.morphspellpack.handler.LichHandler;
import com.windanesz.morphspellpack.spell.SpellTransformation;
import com.windanesz.wizardryutils.integration.baubles.BaublesIntegration;
import com.windanesz.wizardryutils.item.ITickableArtefact;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.item.IWorkbenchItem;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.util.BlockUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class ItemSoulPhylactery extends ItemArtefact implements IWorkbenchItem, ITickableArtefact {

	public static final String ENTITY_TAG = "Entity";
	public static final String PERCENT_TAG = "PercentageFull";

	public ItemSoulPhylactery(EnumRarity rarity, Type type) {
		super(rarity, type);

		this.addPropertyOverride(new ResourceLocation("soul"), new IItemPropertyGetter() {
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				return getPercentFilled(stack);
			}
		});

	}

	public static boolean isAllowedEnity(String entity) {
		List<String> entities = Arrays.asList(Settings.generalSettings.soul_phylactery_mob_list);
		boolean blacklist = Settings.generalSettings.soul_phylactery_list_is_blacklist;
		if (blacklist && entities.contains(entity)) {
			return false;
		}
		if (!blacklist && !entities.contains(entity)) {
			return false;
		}
		return true;
	}

	public static boolean hasEntity(ItemStack stack) {
		return stack.hasTagCompound() && stack.getTagCompound().hasKey(ENTITY_TAG);
	}

	public static ItemStack setEntity(ItemStack stack, String entity) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString(ENTITY_TAG, entity);
		nbt.setFloat(PERCENT_TAG, 0f);
		stack.setTagCompound(nbt);
		return stack;
	}

	public static String getEntity(ItemStack stack) {
		return stack.hasTagCompound() && stack.getTagCompound().hasKey(ENTITY_TAG) ? stack.getTagCompound().getString(ENTITY_TAG) : "";
	}

	public static ItemStack addPercent(ItemStack stack, float percentToAdd) {
		float percent = percentToAdd;
		NBTTagCompound nbt = new NBTTagCompound();
		if (stack.hasTagCompound()) {
			nbt = stack.getTagCompound();
			if (nbt.hasKey(PERCENT_TAG)) {
				percent = nbt.getFloat(PERCENT_TAG);
				// 1f at max
				percent = Math.min(percent + percentToAdd, 1f);
			}
		}
		nbt.setFloat(PERCENT_TAG, percent);
		stack.setTagCompound(nbt);
		return stack;
	}

	public static ItemStack consumePercent(ItemStack stack, float percentToConsume) {
		float percent = percentToConsume;
		NBTTagCompound nbt = new NBTTagCompound();
		if (stack.hasTagCompound()) {
			nbt = stack.getTagCompound();
			if (nbt.hasKey(PERCENT_TAG)) {
				percent = nbt.getFloat(PERCENT_TAG);
				// 1f at max
				percent = Math.max(percent - percentToConsume, 0f);
				nbt.setFloat(PERCENT_TAG, percent);
			}
		}
		stack.setTagCompound(nbt);
		return stack;
	}

	public static float getPercentFilled(ItemStack stack) {
		return stack.hasTagCompound() && stack.getTagCompound().hasKey(PERCENT_TAG) ? stack.getTagCompound().getFloat(PERCENT_TAG) : 0f;
	}

	@Override
	public boolean hasEffect(ItemStack stack) {
		return getPercentFilled(stack) == 1f;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		if (world != null && stack.hasTagCompound() && stack.getTagCompound().hasKey(ENTITY_TAG)) {
			EntityEntry entry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(stack.getTagCompound().getString(ENTITY_TAG)));
			String entity = entry != null ? entry.newInstance(world).getName() : stack.getTagCompound().getString(ENTITY_TAG);
			int fill = (int) (getPercentFilled(stack) * 100);
			Wizardry.proxy.addMultiLineDescription(tooltip, "item.morphspellpack:charm_soul_phylactery.status", fill, entity);
		} else {
			Wizardry.proxy.addMultiLineDescription(tooltip, "item.morphspellpack:charm_soul_phylactery.desc_initial");
		}
		super.addInformation(stack, world, tooltip, advanced);
	}

	@Override
	public int getSpellSlotCount(ItemStack stack) { return 0; }

	@Override
	public boolean onApplyButtonPressed(EntityPlayer player, Slot centre, Slot crystals, Slot upgrade, Slot[] spellBooks) {
		if (centre.getStack().hasTagCompound()) {
			NBTTagCompound nbt = centre.getStack().getTagCompound();
			nbt.removeTag(ENTITY_TAG);
			nbt.removeTag(PERCENT_TAG);
			centre.getStack().setTagCompound(nbt);
			return true;
		}

		return false;
	}

	@Override
	public boolean showTooltip(ItemStack stack) {
		return false;
	}

	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.DRINK;
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		if (getPercentFilled(itemstack) >= Settings.generalSettings.soul_phylactery_cost_of_use) {
			playerIn.setActiveHand(handIn);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);

		} else {
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
		}
	}

	public ItemStack onItemUseFinish(ItemStack itemstack, World worldIn, EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityPlayer && LichHandler.isLich(entityLiving)
				&& getPercentFilled(itemstack) >= Settings.generalSettings.soul_phylactery_cost_of_use) {
			consumePercent(itemstack, Settings.generalSettings.soul_phylactery_cost_of_use);
			((EntityPlayer) entityLiving).getFoodStats().addStats(2, 0.1f);
			return itemstack;
		}

		if (entityLiving instanceof EntityPlayer && getPercentFilled(itemstack) >= Settings.generalSettings.soul_phylactery_cost_of_use) {
			EntityPlayer entityplayer = (EntityPlayer) entityLiving;
			worldIn.playSound((EntityPlayer) null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_WITCH_DRINK, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
			consumePercent(itemstack, Settings.generalSettings.soul_phylactery_cost_of_use);
			SpellTransformation.morphPlayer(entityplayer, getEntity(itemstack), 320);

		}
		return itemstack;
	}

	public int getMaxItemUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public void onWornTick(ItemStack itemStack, EntityLivingBase entityLivingBase) {
		if (entityLivingBase.ticksExisted % 20 != 0) {
			return;
		}

		if (hasEntity(itemStack) && !getEntity(itemStack).equals(entityLivingBase.getName())) {
			return;
		}

		if (entityLivingBase instanceof EntityPlayer && !entityLivingBase.world.isRemote) {
			EntityPlayer player = (EntityPlayer) entityLivingBase;
			if (player.isBurning() && !player.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
				BlockPos pos = player.getPosition();
				World world = player.world;
				int skulls = 0;
				int soulSand = 0;
				for (BlockPos blockPos : BlockUtils.getBlockSphere(pos, 4)) {
					if (world.getBlockState(blockPos).getBlock() == Blocks.SKULL) {
						skulls++;
					}
					if (world.getBlockState(blockPos).getBlock() == Blocks.SOUL_SAND) {
						soulSand++;
					}

				}

				if (skulls >= 4 && soulSand >= 8) {
					ItemStack stack = itemStack.copy();
					if (!hasEntity(stack)) {
						setEntity(stack, entityLivingBase.getName());
					}
					addPercent(stack, 0.05f);
					BaublesIntegration.setArtefactToSlot(player, stack, ItemArtefact.Type.CHARM);
				}

				if (getPercentFilled(itemStack) >= 1f) {
					SpellTransformation.morphPlayer(entityLivingBase, LichHandler.LICH, -1);
					LichHandler.setLich(entityLivingBase, true);
				}
			}
		}
	}
}
