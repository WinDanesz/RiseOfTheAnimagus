package com.windanesz.morphspellpack.client;

import com.windanesz.morphspellpack.CommonProxy;
import com.windanesz.morphspellpack.MorphSpellPack;
import com.windanesz.morphspellpack.client.render.RenderDisguise;
import com.windanesz.morphspellpack.client.render.RenderLich;
import com.windanesz.morphspellpack.entity.construct.EntityStarfall;
import com.windanesz.morphspellpack.entity.living.EntityBatMinion;
import com.windanesz.morphspellpack.entity.living.EntityDisguise;
import com.windanesz.morphspellpack.entity.living.EntityLich;
import com.windanesz.morphspellpack.entity.living.EntityLightWisp;
import com.windanesz.morphspellpack.entity.living.EntityTemporaryRabbit;
import com.windanesz.morphspellpack.entity.projectile.EntityRadiantSpark;
import electroblob.wizardry.client.renderer.entity.RenderBlank;
import electroblob.wizardry.client.renderer.entity.RenderMagicArrow;
import net.minecraft.client.renderer.entity.RenderBat;
import net.minecraft.client.renderer.entity.RenderRabbit;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	public static KeyBinding KEY_ACTIVATE_MORPH_ABILITY;

	/**
	 * Called from preInit() in the main mod class to initialise the renderers.
	 */
	public void registerRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityTemporaryRabbit.class, RenderRabbit::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBatMinion.class, RenderBat::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDisguise.class, RenderDisguise::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityLightWisp.class, RenderBlank::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityStarfall.class, RenderBlank::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityLich.class, RenderLich::new);

		//projectiles
		RenderingRegistry.registerEntityRenderingHandler(EntityRadiantSpark.class, manager -> new RenderMagicArrow(manager,
				new ResourceLocation(MorphSpellPack.MODID, "textures/entity/radiant_spark.png"), true, 8.0, 2.0, 16, 5, false));
	}

	@Override
	public void init() {
		registerKeybindings();
	}

	@Override
	public void preInit() {
		//		MinecraftForge.EVENT_BUS.unregister(Morph.eventHandlerClient);
	}

	private void registerKeybindings() {
		// Initializing
		KEY_ACTIVATE_MORPH_ABILITY = new KeyBinding("key.morphspellpack.activate_morph_ability", Keyboard.KEY_K, "key.morphspellpack.category");
		ClientRegistry.registerKeyBinding(KEY_ACTIVATE_MORPH_ABILITY);
	}
}