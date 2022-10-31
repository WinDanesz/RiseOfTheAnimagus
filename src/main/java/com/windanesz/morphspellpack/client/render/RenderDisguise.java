package com.windanesz.morphspellpack.client.render;

import com.windanesz.morphspellpack.MorphSpellPack;
import com.windanesz.morphspellpack.client.model.ModelDisguise;
import com.windanesz.morphspellpack.entity.living.EntityDisguise;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderDisguise extends RenderLiving<EntityDisguise> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(MorphSpellPack.MODID, "textures/entity/disguise.png");

	public RenderDisguise(RenderManager renderManager) {
		super(renderManager, new ModelDisguise(), 0.0F);
	}

	@Override
	public void doRender(EntityDisguise entity, double x, double y, double z, float entityYaw, float partialTicks) {
		if (entity.ticksToRender == 0) {
			ItemStack stack = null;
			Block block = entity.world.getBlockState(entity.getPosition().down()).getBlock();
			if (block != Blocks.AIR) {
				stack = new ItemStack(Item.getItemFromBlock(block));
			}
			if (stack == null) {
				stack = new ItemStack(Item.getItemFromBlock(Blocks.PUMPKIN));
			}

			if (!stack.isEmpty()) {
				GlStateManager.pushMatrix();
				GlStateManager.translate(0.0F, 0.5F, 0.0F);
				GlStateManager.rotate(180, 0, 1, 0);
				GlStateManager.scale(2F, 2F, 2F);
				Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
				GlStateManager.popMatrix();
			}
		}
	}

	protected ResourceLocation getEntityTexture(EntityDisguise entity) {
		return TEXTURE;

	}
}
