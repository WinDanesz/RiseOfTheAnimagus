package com.windanesz.morphspellpack.client;

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
public final class ModelRegistry {

	/**
	 * Keeps track of all items whose models have been registered manually to exclude them from automatic registry of
	 * standard item models. Internal only, this gets cleared once model registry is complete.
	 */
	private static final List<Item> registeredItems = new ArrayList<>();

	// no instances
	private ModelRegistry() {}

	@SubscribeEvent
	public static void register(ModelRegistryEvent event) {

		// Automatic item model registry...
		for(Item item : Item.REGISTRY){
			if(!registeredItems.contains(item) && item.getRegistryName().getNamespace().equals(MorphSpellPack.MODID)){
				registerItemModel(item); // Standard item model
			}
		}

		registeredItems.clear(); // Might as well clean this up
	}

	/**
	 * Registers an item model, using the item's registry name as the model name (this convention makes it easier to
	 * keep track of everything). Variant defaults to "normal". Registers the model for all metadata values.
	 * Author: Electroblob
	 */
	private static void registerItemModel(Item item) {
		// Changing the last parameter from null to "inventory" fixed the item/block model weirdness. No idea why!
		ModelBakery.registerItemVariants(item, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		// Assigns the model for all metadata values
		ModelLoader.setCustomMeshDefinition(item, s -> new ModelResourceLocation(item.getRegistryName(), "inventory"));
		registeredItems.add(item);
	}
}

