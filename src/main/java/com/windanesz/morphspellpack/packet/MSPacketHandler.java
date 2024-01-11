package com.windanesz.morphspellpack.packet;

import com.windanesz.morphspellpack.MorphSpellPack;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class MSPacketHandler {

	public static SimpleNetworkWrapper net;

	public static void initPackets(){
		net = NetworkRegistry.INSTANCE.newSimpleChannel(MorphSpellPack.MODID.toUpperCase());
		registerMessage(PacketToggleAbility.class, PacketToggleAbility.Message.class);
	}

	private static int nextPacketId = 0;

	private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(
			Class<? extends IMessageHandler<REQ, REPLY>> packet, Class<REQ> message){
		net.registerMessage(packet, message, nextPacketId, Side.CLIENT);
		net.registerMessage(packet, message, nextPacketId, Side.SERVER);
		nextPacketId++;
	}

}
