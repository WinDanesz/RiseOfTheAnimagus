package com.windanesz.morphspellpack.handler;

import com.windanesz.morphspellpack.Settings;
import com.windanesz.morphspellpack.Utils;
import com.windanesz.morphspellpack.items.ItemSoulPhylactery;
import com.windanesz.morphspellpack.registry.MSItems;
import com.windanesz.morphspellpack.spell.SpellTransformation;
import com.windanesz.wizardryutils.integration.baubles.BaublesIntegration;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.ItemScroll;
import electroblob.wizardry.packet.PacketCastSpell;
import electroblob.wizardry.packet.WizardryPacketHandler;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.registry.WizardryPotions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.util.InventoryUtils;
import electroblob.wizardry.util.SpellModifiers;
import me.ichun.mods.morph.api.event.MorphAcquiredEvent;
import me.ichun.mods.morph.api.event.MorphEvent;
import me.ichun.mods.morph.common.Morph;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber
public final class EventHandler {

	@SubscribeEvent
	public static void onMorphAcquiredEvent(MorphAcquiredEvent event) {
		event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (event.isCancelable() && event.getEntityPlayer().isPotionActive(WizardryPotions.transience)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onPlayerInteractEvent(MorphEvent event) {
		if (event.isCancelable() && event.getEntityPlayer().isPotionActive(WizardryPotions.transience)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onLivingDeathEvent(LivingDeathEvent event) {
		// force demorph player
		if (event.getEntity() instanceof EntityPlayer && !event.getEntity().getEntityWorld().isRemote) {
			if (Morph.eventHandlerServer.morphsActive.containsKey(event.getEntity().getName())) {
				SpellTransformation.demorphPlayer((EntityLivingBase) event.getEntity());
			}
		} else {
			if (event.getSource() != null && event.getSource().getTrueSource() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();

				// Liches can refill phys from the hotbar
				if (LichHandler.isLich(player)) {
					String entity = EntityList.getKey(event.getEntityLiving()).toString();
					boolean foundEmpty = false;
					for (ItemStack stack : InventoryUtils.getHotbar((EntityPlayer) event.getSource().getTrueSource())) {
						if (stack.getItem() instanceof ItemSoulPhylactery) {
							if (ItemSoulPhylactery.getEntity(stack).equals(entity) && ItemSoulPhylactery.getPercentFilled(stack) < 1f) {
								ItemSoulPhylactery.addPercent(stack, Settings.generalSettings.soul_phylactery_percent_gain_per_kill);
								return;
							} else if (!ItemSoulPhylactery.hasEntity(stack)) {
								foundEmpty = true;
							}
						}
					}

					// find the first empty slot if no match was found
					if (foundEmpty) {
						for (ItemStack stack : InventoryUtils.getHotbar((EntityPlayer) event.getSource().getTrueSource())) {
							if (stack.getItem() instanceof ItemSoulPhylactery && !ItemSoulPhylactery.hasEntity(stack)) {
								ItemSoulPhylactery.setEntity(stack, entity);
								ItemSoulPhylactery.addPercent(stack, Settings.generalSettings.soul_phylactery_percent_gain_per_kill);
								return;
							}
						}
					}
				} else if (ItemArtefact.isArtefactActive(player, MSItems.charm_soul_phylactery)) {
					ItemStack stack = BaublesIntegration.getEquippedArtefactStacks(player, ItemArtefact.Type.CHARM).get(0);
					String entity = EntityList.getKey(event.getEntityLiving()).toString();
					if (!ItemSoulPhylactery.hasEntity(stack)) {
						ItemSoulPhylactery.setEntity(stack, entity);
						ItemSoulPhylactery.addPercent(stack, Settings.generalSettings.soul_phylactery_percent_gain_per_kill);
						BaublesIntegration.setArtefactToSlot(player, stack, ItemArtefact.Type.CHARM);
					} else if (ItemSoulPhylactery.getEntity(stack).equals(entity)) {
						ItemSoulPhylactery.addPercent(stack, Settings.generalSettings.soul_phylactery_percent_gain_per_kill);
						BaublesIntegration.setArtefactToSlot(player, stack, ItemArtefact.Type.CHARM);
					}
				}
			}
		}

	}

	@SubscribeEvent
	public static void onSpellCastEventPre(SpellCastEvent.Pre event) {
		if (event.getCaster() instanceof EntityPlayer && event.getSpell() instanceof SpellMinion
				&& ItemArtefact.isArtefactActive((EntityPlayer) event.getCaster(), MSItems.charm_shapeshifter_orb)) {
			SpellMinion spell = (SpellMinion) event.getSpell();
			Method meth = ReflectionHelper.findMethod(SpellMinion.class, "createMinion", "createMinion", World.class,
					EntityLivingBase.class, SpellModifiers.class);
			try {
				EntityLivingBase entity = (EntityLivingBase) meth.invoke(event.getSpell(), event.getWorld(), event.getCaster(), event.getModifiers()); // world, caster, modifiers
				String entityString = EntityList.getKey(entity).toString();
				List<String> bannedMobs = Arrays.asList(Settings.generalSettings.skinchanger_banned_mobs);
				if (bannedMobs.contains(entityString)) {
					Utils.sendMessage(event.getCaster(), "spell.morphspellpack:skinchanger.mob_not_allowed", true);
					return;
				}
				if (SpellTransformation.morphPlayer(event.getCaster(), entityString, (int) event.getModifiers().get(WizardryItems.duration_upgrade) / 3)) {
					if (event.getSource() == SpellCastEvent.Source.SCROLL) {
						ItemStack mainStack = event.getCaster().getHeldItem(EnumHand.MAIN_HAND);
						if (mainStack.getItem() instanceof ItemScroll && Spell.byMetadata(mainStack.getItemDamage()) instanceof SpellMinion) {
							mainStack.shrink(1);
						}
						event.setCanceled(true);
					}
				}

			}
			catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerDeath(LivingHurtEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			if (!event.isCanceled() && player.getHealth() - event.getAmount() <= 0 && ItemArtefact.isArtefactActive(player, MSItems.charm_phoenix_feather)
					&& !player.getCooldownTracker().hasCooldown(MSItems.charm_phoenix_feather)) {
				event.setCanceled(true);
				player.heal(6);
				SpellTransformation.morphPlayer(player, "ebwizardry:phoenix", 360);
				SpellModifiers modifiers = new SpellModifiers();
				Spell spell = Spells.firestorm;
				if (spell.cast(player.world, player, EnumHand.MAIN_HAND, 0, modifiers)) {
					MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Post(SpellCastEvent.Source.COMMAND, spell, player, modifiers));
					if (spell.requiresPacket()) {
						// Sends a packet to all players in dimension to tell them to spawn particles.
						// Only sent if the spell succeeded, because if the spell failed, you wouldn't
						// need to spawn any particles!
						IMessage msg = new PacketCastSpell.Message(player.getEntityId(), null, spell, modifiers);
						WizardryPacketHandler.net.sendToDimension(msg, player.world.provider.getDimension());
					}
				}
				player.getCooldownTracker().setCooldown(MSItems.charm_phoenix_feather, 72000);
			}
		}
	}

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			resumeMorph(event.getEntity());
		}
	}

	public static void resumeMorph(Entity player) {
		if (player instanceof EntityPlayer && !player.world.isRemote) {

			WizardData data = WizardData.get((EntityPlayer) player);
			if (data != null) {
				String lastMorph = data.getVariable(SpellTransformation.LAST_MORPH);
				Integer duration = data.getVariable(SpellTransformation.MORPH_DURATION);

				if (lastMorph != null && duration != null && duration > 0 && !lastMorph.equals(LichHandler.LICH)) {
					SpellTransformation.morphPlayer((EntityPlayer) player, lastMorph, duration);
				}
			}
		}
	}
}
