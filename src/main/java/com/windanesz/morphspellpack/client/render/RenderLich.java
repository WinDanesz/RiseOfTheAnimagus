package com.windanesz.morphspellpack.client.render;

import com.windanesz.morphspellpack.MorphSpellPack;
import com.windanesz.morphspellpack.client.model.ModelLich;
import com.windanesz.morphspellpack.entity.living.EntityLich;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderLich extends RenderBiped<EntityLich> {
	private static final ResourceLocation SKELETON_TEXTURES = new ResourceLocation(MorphSpellPack.MODID, "textures/entity/lich.png");

	public RenderLich(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelLich(), 0.5F);
		this.addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerBipedArmor(this) {
			protected void initArmor() {
				this.modelLeggings = new ModelLich(0.5F, true);
				this.modelArmor = new ModelLich(1.0F, true);
			}
		});
	}

	public void transformHeldFull3DItemLayer() {
		GlStateManager.translate(0.09375F, 0.1875F, 0.0F);
	}

	protected ResourceLocation getEntityTexture(EntityLich entity) {
		return SKELETON_TEXTURES;
	}

	@Override
	protected void preRenderCallback(EntityLich entity, float partialTickTime) {
		super.preRenderCallback(entity, partialTickTime);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		if (entity instanceof EntityLich) { // Always true
			GlStateManager.color(1, 1, 1, 0.8f);
		}

	}

	@Override
	public void doRender(EntityLich entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		if (!entity.isInvisible()) {
			for (int i = 0; i < 2; i++) {
				ParticleBuilder.create(ParticleBuilder.Type.FLASH)
						.entity(entity)
						.time(10)
						.pos((entity.world.rand.nextFloat() - 0.5f) / 2, 0.5f, (entity.world.rand.nextFloat() - 0.5f) / 2)
						.vel(0, -1 * (entity.world.rand.nextFloat() / 5), 0)
						.scale(entity.world.rand.nextFloat() / 2)
						.clr(2, 199, 140)
						.spawn(entity.world);
			}
			if (entity.ticksExisted % 5 == 0) {
				ParticleBuilder.create(ParticleBuilder.Type.FLASH)
						.entity(entity)
						.time(10)
						.pos(0, 1f, 0)
						.spin(0.1, 0.1)
						.clr(2, 199, 140)
						.spawn(entity.world);
				ParticleBuilder.create(ParticleBuilder.Type.FLASH)
						.entity(entity)
						.time(10)
						.pos(0, 0.6f, 0)
						.spin(0.1, 0.1)
						.clr(2, 199, 140)
						.spawn(entity.world);
			}
		}
	}
}
