package com.windanesz.morphspellpack.registry;

import com.windanesz.morphspellpack.MorphSpellPack;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@GameRegistry.ObjectHolder(MorphSpellPack.MODID)
@Mod.EventBusSubscriber
public class BlockRegistry {

	public static final Block deepslate = placeholder();


	private BlockRegistry() {} // no instances

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	public static <T> T placeholder() { return null; }

	public static void registerBlock(IForgeRegistry<Block> registry, String name, Block block) {
		block.setRegistryName(MorphSpellPack.MODID, name);
		block.setTranslationKey(block.getRegistryName().toString());
		registry.register(block);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

//		registerBlock(registry, "deepslate", (new DeepslateBlock()).setHardness(3F).setResistance(10.0F));
	}

	public static void registerOreDictionaryEntries() {
	}

	//	/** Called from the preInit method in the main mod class to register all the tile entities. */
	//	public static void registerTileEntities(){
	//		// TODO
	//	}

}
