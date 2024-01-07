package com.windanesz.morphspellpack.packet;

import com.windanesz.morphspellpack.MorphSpellPack;
import com.windanesz.morphspellpack.ability.IActiveAbility;
import io.netty.buffer.ByteBuf;
import me.ichun.mods.morph.api.ability.Ability;
import me.ichun.mods.morph.common.Morph;
import me.ichun.mods.morph.common.morph.MorphInfo;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;

/**
 * <b>[Client -> Server]</b> This packet is for handling the ability toggle.
 */
public class PacketToggleAbility implements IMessageHandler<PacketToggleAbility.Message, IMessage> {

	@Override
	public IMessage onMessage(Message message, MessageContext ctx) {

		// Just to make sure that the side is correct
		if (ctx.side.isServer()) {
			final EntityPlayerMP player = ctx.getServerHandler().player;
			player.getServerWorld().addScheduledTask(() -> toggleAbility(player));
		}
		return null;
	}

	private void toggleAbility(EntityPlayerMP player) {
		MorphInfo info = Morph.eventHandlerServer.morphsActive.get(player.getName());

		if (info != null) {
			ArrayList<Ability> list = info.nextState.abilities;
			for (Ability ability : list) {
				if (ability instanceof IActiveAbility) {
					((IActiveAbility) ability).toggleAbility();
				}
			}
		}
	}

	public static class Message implements IMessage {

		// This constructor is required otherwise you'll get errors (used somewhere in fml through reflection)
		public Message() {
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			// The order is important
		}

		@Override
		public void toBytes(ByteBuf buf) {
		}
	}
}
