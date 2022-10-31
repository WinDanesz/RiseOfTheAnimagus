package com.windanesz.morphspellpack.registry;

import com.windanesz.morphspellpack.MorphSpellPack;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(Side.CLIENT)
public final class ItemModelRegistry {
	private static List<String> ADDON_MODID_REGISTRY = new ArrayList<>();

	private ItemModelRegistry() { // no instances
	}

	public static void registerModForAutoItemModelRegistry(String modid) {
		if (!ADDON_MODID_REGISTRY.contains(modid)) {
			MorphSpellPack.logger.info("Registered modid " + modid + " for automatic item model registry");
			ADDON_MODID_REGISTRY.add(modid);
		}
	}

	@SubscribeEvent
	public static void register(ModelRegistryEvent event) {
		// Automatic item model registry for each mods
		for (Item item : Item.REGISTRY) {
			if (ADDON_MODID_REGISTRY.contains(item.getRegistryName().getNamespace())) {
				registerItemModel(item); // Standard item model
			}
		}

	}

	// below registry methods are courtesy of EB
	public static void registerItemModel(Item item) {
		ModelBakery.registerItemVariants(item, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(item, s -> new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

	private static void registerItemModel(Item item, int metadata, String variant) {
		ModelLoader.setCustomModelResourceLocation(item, metadata,
				new ModelResourceLocation(item.getRegistryName(), variant));
	}

}

