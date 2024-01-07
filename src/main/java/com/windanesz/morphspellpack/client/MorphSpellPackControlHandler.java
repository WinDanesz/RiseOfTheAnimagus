package com.windanesz.morphspellpack.client;

import com.windanesz.morphspellpack.MorphSpellPack;
import com.windanesz.morphspellpack.packet.MSPacketHandler;
import com.windanesz.morphspellpack.packet.PacketToggleAbility;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ControlHandler {

	static boolean abilityKeyPressed = false;

	@SubscribeEvent
	public static void onTickEvent(TickEvent.ClientTickEvent event) {

		if (event.phase == TickEvent.Phase.END) {
			return; // Only really needs to be once per tick
		}

		if (MorphSpellPack.proxy instanceof com.windanesz.morphspellpack.client.ClientProxy) {

			EntityPlayer player = Minecraft.getMinecraft().player;

			if (player != null) {

				if (ClientProxy.KEY_ACTIVATE_MORPH_ABILITY.isKeyDown() && Minecraft.getMinecraft().inGameHasFocus) {
					if (!abilityKeyPressed) {
						abilityKeyPressed = true;
						toggleAbility(player);
					}
				} else {
					abilityKeyPressed = false;
				}
			}
		}
	}

	private static void toggleAbility(EntityPlayer player) {
		IMessage msg = new PacketToggleAbility.Message();
		MSPacketHandler.net.sendToServer(msg);
	}
}
