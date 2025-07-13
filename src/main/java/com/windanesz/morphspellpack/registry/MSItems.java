package com.windanesz.morphspellpack.registry;

import com.windanesz.morphspellpack.MorphSpellPack;
import com.windanesz.morphspellpack.items.*;
import com.windanesz.wizardryutils.registry.ItemRegistry;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.ItemSpellBook;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@ObjectHolder(MorphSpellPack.MODID)
@Mod.EventBusSubscriber
public final class MSItems {


	public static final Item ring_bat = placeholder();
	public static final Item ring_web = placeholder();
	public static final Item ring_transformation = placeholder();
	public static final Item ring_starfall = placeholder();
	public static final Item ring_skinchanger = placeholder();
	public static final Item ring_shadows = placeholder();
	public static final Item amulet_transformation_protection = placeholder();
	public static final Item amulet_fleshcloak = placeholder();
	public static final Item amulet_soul_ward = placeholder();
	public static final Item amulet_spellforge = placeholder();
	public static final Item charm_druid_stone = placeholder();
	public static final Item charm_phoenix_feather = placeholder();
	public static final Item charm_shapeshifter_orb = placeholder();
	public static final Item charm_bottled_shadow = placeholder();
	public static final Item charm_soul_phylactery = placeholder();
	public static final Item charm_lich_tome = placeholder();
	public static final Item charm_lich_tome_2 = placeholder();
	public static final Item charm_lich_tome_3 = placeholder();
	public static final Item charm_clay_of_creation = placeholder();
	public static final Item body_undead_sunburn = placeholder();
	public static final Item charm_soul_gem = placeholder();
	public static final Item head_soulbane = placeholder();
	public static final Item lich_spell_book = placeholder();

	private MSItems() {
	} // No instances!

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Item> event) {

		IForgeRegistry<Item> registry = event.getRegistry();
		ItemRegistry.registerItemArtefact(registry, "ring_web", MorphSpellPack.MODID, new ItemArtefact(EnumRarity.UNCOMMON, ItemArtefact.Type.RING));
		ItemRegistry.registerItemArtefact(registry, "ring_bat", MorphSpellPack.MODID, new ItemArtefact(EnumRarity.UNCOMMON, ItemArtefact.Type.RING));
		ItemRegistry.registerItemArtefact(registry, "ring_transformation", MorphSpellPack.MODID, new ItemArtefact(EnumRarity.RARE, ItemArtefact.Type.RING));
		ItemRegistry.registerItemArtefact(registry, "ring_starfall", MorphSpellPack.MODID, new ItemArtefact(EnumRarity.EPIC, ItemArtefact.Type.RING));
		ItemRegistry.registerItemArtefact(registry, "ring_skinchanger", MorphSpellPack.MODID, new ItemArtefact(EnumRarity.RARE, ItemArtefact.Type.RING));
		ItemRegistry.registerItemArtefact(registry, "ring_shadows", MorphSpellPack.MODID, new ItemArtefact(EnumRarity.RARE, ItemArtefact.Type.RING));
		//	ItemRegistry.registerItemArtefact(registry, "ring_phoenix", MorphSpellPack.MODID, new ItemArtefact(EnumRarity.EPIC, ItemArtefact.Type.RING));

		ItemRegistry.registerItemArtefact(registry, "amulet_transformation_protection", MorphSpellPack.MODID, new ItemArtefact(EnumRarity.RARE, ItemArtefact.Type.AMULET));
		ItemRegistry.registerItemArtefact(registry, "amulet_fleshcloak", MorphSpellPack.MODID, new ItemArtefact(EnumRarity.RARE, ItemArtefact.Type.AMULET));
		ItemRegistry.registerItemArtefact(registry, "amulet_soul_ward", MorphSpellPack.MODID, new ItemArtefact(EnumRarity.RARE, ItemArtefact.Type.AMULET));
		ItemRegistry.registerItemArtefact(registry, "amulet_spellforge", MorphSpellPack.MODID, new ItemArtefact(EnumRarity.EPIC, ItemArtefact.Type.AMULET));

		ItemRegistry.registerItemArtefact(registry, "charm_soul_phylactery", MorphSpellPack.MODID, new ItemSoulPhylactery(EnumRarity.UNCOMMON, ItemArtefact.Type.CHARM));
		ItemRegistry.registerItemArtefact(registry, "charm_druid_stone", MorphSpellPack.MODID, new ItemDruidStone(EnumRarity.RARE, ItemArtefact.Type.CHARM));
		ItemRegistry.registerItemArtefact(registry, "charm_shapeshifter_orb", MorphSpellPack.MODID, new ItemArtefact(EnumRarity.EPIC, ItemArtefact.Type.CHARM));
		ItemRegistry.registerItemArtefact(registry, "charm_phoenix_feather", MorphSpellPack.MODID, new ItemArtefact(EnumRarity.EPIC, ItemArtefact.Type.CHARM));
		ItemRegistry.registerItemArtefact(registry, "charm_lich_tome", MorphSpellPack.MODID, new ItemLichTome(EnumRarity.RARE, ItemArtefact.Type.CHARM));
		ItemRegistry.registerItemArtefact(registry, "charm_lich_tome_2", MorphSpellPack.MODID, new ItemLichTome(EnumRarity.RARE, ItemArtefact.Type.CHARM));
		ItemRegistry.registerItemArtefact(registry, "charm_lich_tome_3", MorphSpellPack.MODID, new ItemLichTome(EnumRarity.RARE, ItemArtefact.Type.CHARM));
		ItemRegistry.registerItemArtefact(registry, "charm_bottled_shadow", MorphSpellPack.MODID, new ItemShadowBottle(EnumRarity.EPIC, ItemArtefact.Type.CHARM));
		ItemRegistry.registerItemArtefact(registry, "body_undead_sunburn", MorphSpellPack.MODID, new ItemArtefact(EnumRarity.EPIC, ItemArtefact.Type.BODY));
		ItemRegistry.registerItemArtefact(registry, "charm_soul_gem", MorphSpellPack.MODID, new ItemSoulGem(EnumRarity.EPIC, ItemArtefact.Type.CHARM));

		ItemRegistry.registerItemArtefact(registry, "head_soulbane", MorphSpellPack.MODID, new ItemArtefact(EnumRarity.RARE, ItemArtefact.Type.HEAD));

		ItemRegistry.registerItem(registry, "lich_spell_book", MorphSpellPack.MODID, new ItemSpellBook());
		//	ItemRegistry.registerItemArtefact(registry, "clay_of_creation", MorphSpellPack.MODID, new ItemClayOfCreation());

		ItemRegistry.registerItemBlock(registry, MSBlocks.lich_phial_holder_empty, new ItemBlock(MSBlocks.lich_phial_holder_empty));
	}

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	public static <T> T placeholder() {
		return null;
	}

	public static void registerItemBlock(IForgeRegistry<Item> registry, Block block, Item itemblock) {
		itemblock.setRegistryName(block.getRegistryName());
		registry.register(itemblock);

	}

	@SubscribeEvent
	public static void onMissingMappings(RegistryEvent.MissingMappings<Item> event) {
		for (RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getMappings()) {
			if (mapping.key.equals(new ResourceLocation("morphspellpack", "charm_undead_sunburn"))) {
				mapping.remap(body_undead_sunburn);
			}
		}
	}

}