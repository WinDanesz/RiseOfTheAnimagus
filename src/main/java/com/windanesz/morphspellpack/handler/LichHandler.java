package com.windanesz.morphspellpack.handler;

import com.windanesz.morphspellpack.registry.MSPotions;
import com.windanesz.morphspellpack.spell.SpellTransformation;
import electroblob.wizardry.data.IStoredVariable;
import electroblob.wizardry.data.Persistence;
import electroblob.wizardry.data.WizardData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class LichHandler {

	public static final String LICH = "morphspellpack:lich";
	public static final IStoredVariable<Boolean> IS_LICH = IStoredVariable.StoredVariable.ofBoolean("isLich", Persistence.ALWAYS).setSynced();

	private LichHandler() {}

	public static void init() {
		WizardData.registerStoredVariables(IS_LICH);
	}

	public static boolean isLich(Entity player) {
		if (player instanceof EntityPlayer) {
			WizardData data = WizardData.get((EntityPlayer) player);
			if (data != null) {
				Boolean lich = data.getVariable(IS_LICH);
				return lich != null && lich;
			}
		}
		return false;
	}

	public static void setLich(Entity player, boolean lich) {
		if (player instanceof EntityPlayer) {
			WizardData data = WizardData.get((EntityPlayer) player);
			if (data != null) {
				data.setVariable(IS_LICH, lich);
				data.sync();
			}
		}
	}

	@SubscribeEvent
	public static void onInteractItem(PlayerInteractEvent.RightClickItem event) {
		if (event.getItemStack().getItemUseAction() == EnumAction.EAT && isLich(event.getEntityPlayer())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		// liches doesn't like cakes
		if (event.getEntityPlayer().world.getBlockState(event.getPos()).getBlock() == Blocks.CAKE && isLich(event.getEntity())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof EntityPlayer && isLich(event.getEntity()) && !((EntityPlayer) event.getEntity()).isPotionActive(MSPotions.curse_of_transformation)) {
			SpellTransformation.morphPlayer((EntityPlayer) event.getEntity(), LichHandler.LICH, -1);
		}
	}
}
