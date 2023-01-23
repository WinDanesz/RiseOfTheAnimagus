package com.windanesz.morphspellpack.client;

import com.windanesz.morphspellpack.MorphSpellPack;
import com.windanesz.morphspellpack.handler.LichHandler;
import com.windanesz.morphspellpack.registry.MSPotions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientEventHandler {

	public static final ResourceLocation ICONS = new ResourceLocation(MorphSpellPack.MODID, "textures/gui/icons.png");
	public static int left_height = 39;
	public static int right_height = 39;

	/**
	 * Lich GUI
	 */
	@SubscribeEvent
	public static void RenderGameOverlayEvent(RenderGameOverlayEvent.Post event) {
		ScaledResolution res = event.getResolution();
		Minecraft mc = Minecraft.getMinecraft();

		if (Minecraft.getMinecraft().player instanceof EntityPlayer) {

			EntityPlayer player = (EntityPlayer) Minecraft.getMinecraft().player;
			if (player.isSpectator()) {
				return; // Spectators shouldn't have HUD changes
			}

			if (!LichHandler.isLich(player)) { return; }

			GlStateManager.enableBlend();

			int width = res.getScaledWidth();
			int height = res.getScaledHeight();
			int left = width / 2 + 91;
			int top = height - right_height;
			right_height += 10;
			boolean unused = false;// Unused flag in vanilla, seems to be part of a 'fade out' mechanic

			GlStateManager.pushMatrix();

			Minecraft.getMinecraft().renderEngine.bindTexture(ICONS);
			FoodStats stats = mc.player.getFoodStats();
			int level = stats.getFoodLevel();
			for (int i = 0; i < 10; ++i) {
				int idx = i * 2 + 1;
				int x = left - i * 8 - 9;
				int y = top;
				int icon = 16;
				byte background = 0;

				if (mc.player.isPotionActive(MobEffects.HUNGER)) {
					icon += 36;
					background = 13;
				}
				if (unused) {
					background = 1; //Probably should be a += 1 but vanilla never uses this
				}

				drawTexturedModalRect(x, y, 16 + background * 9, 27, 9, 9);

				if (idx < level) { drawTexturedModalRect(x, y, icon + 36, 27, 9, 9); } else if (idx == level) {
					drawTexturedModalRect(x, y, icon + 45, 27, 9, 9);
				}
			}

			GlStateManager.popMatrix();
		}

	}

	public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
		int zIndex = 100;
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos((double) (x + 0), (double) (y + height), (double) zIndex).tex((double) ((float) (textureX + 0) * 0.00390625F), (double) ((float) (textureY + height) * 0.00390625F)).endVertex();
		bufferbuilder.pos((double) (x + width), (double) (y + height), (double) zIndex).tex((double) ((float) (textureX + width) * 0.00390625F), (double) ((float) (textureY + height) * 0.00390625F)).endVertex();
		bufferbuilder.pos((double) (x + width), (double) (y + 0), (double) zIndex).tex((double) ((float) (textureX + width) * 0.00390625F), (double) ((float) (textureY + 0) * 0.00390625F)).endVertex();
		bufferbuilder.pos((double) (x + 0), (double) (y + 0), (double) zIndex).tex((double) ((float) (textureX + 0) * 0.00390625F), (double) ((float) (textureY + 0) * 0.00390625F)).endVertex();
		tessellator.draw();
	}

	@SubscribeEvent
	public static void onRenderLivingEvent(RenderLivingEvent.Pre<?> event) {
		if (event.getEntity().isPotionActive(MSPotions.conjured_soul)) {
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();

			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE); // GhoOOoOostly OooOOOo00oOOo
			GlStateManager.color(0.7f, 1, 1, 0.5f);
		}
	}

	@SubscribeEvent
	public static void onRenderLivingEvent(RenderLivingEvent.Post<?> event) {
		if (event.getEntity().isPotionActive(MSPotions.conjured_soul)) {
			GlStateManager.popMatrix();
		}
	}

}
