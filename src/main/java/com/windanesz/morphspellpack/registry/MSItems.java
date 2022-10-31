package com.windanesz.morphspellpack.registry;

import com.windanesz.morphspellpack.MorphSpellPack;
import com.windanesz.morphspellpack.items.ItemClayOfCreation;
import com.windanesz.morphspellpack.items.ItemDruidStone;
import com.windanesz.morphspellpack.items.ItemLichTome;
import com.windanesz.morphspellpack.items.ItemShadowBottle;
import com.windanesz.morphspellpack.items.ItemSoulPhylactery;
import com.windanesz.wizardryutils.registry.ItemRegistry;
import electroblob.wizardry.item.ItemArtefact;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@ObjectHolder(MorphSpellPack.MODID)
@Mod.EventBusSubscriber
public final class MSItems {


	private MSItems() {} // No instances!

	public static final Item ring_bat = placeholder();
	public static final Item ring_web = placeholder();
	public static final Item ring_transformation = placeholder();
//	public static final Item ring_phoenix = placeholder();
	public static final Item amulet_transformation_protection = placeholder();
	public static final Item charm_druid_stone = placeholder();
	public static final Item charm_phoenix_feather = placeholder();
	public static final Item charm_shapeshifter_orb = placeholder();
	public static final Item charm_bottled_shadow = placeholder();
	public static final Item charm_soul_phylactery = placeholder();
	public static final Item charm_lich_tome = placeholder();
	public static final Item charm_clay_of_creation = placeholder();

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Item> event) {

		IForgeRegistry<Item> registry = event.getRegistry();
		ItemRegistry.registerItemArtefact(registry, "ring_web", MorphSpellPack.MODID, new ItemArtefact(EnumRarity.UNCOMMON, ItemArtefact.Type.RING));
		ItemRegistry.registerItemArtefact(registry, "ring_bat", MorphSpellPack.MODID, new ItemArtefact(EnumRarity.RARE, ItemArtefact.Type.RING));
		ItemRegistry.registerItemArtefact(registry, "ring_transformation", MorphSpellPack.MODID, new ItemArtefact(EnumRarity.RARE, ItemArtefact.Type.RING));
	//	ItemRegistry.registerItemArtefact(registry, "ring_phoenix", MorphSpellPack.MODID, new ItemArtefact(EnumRarity.EPIC, ItemArtefact.Type.RING));

		ItemRegistry.registerItemArtefact(registry, "amulet_transformation_protection", MorphSpellPack.MODID, new ItemArtefact(EnumRarity.RARE, ItemArtefact.Type.AMULET));

		ItemRegistry.registerItemArtefact(registry, "charm_soul_phylactery", MorphSpellPack.MODID, new ItemSoulPhylactery(EnumRarity.UNCOMMON, ItemArtefact.Type.CHARM));
		ItemRegistry.registerItemArtefact(registry, "charm_druid_stone", MorphSpellPack.MODID, new ItemDruidStone(EnumRarity.RARE, ItemArtefact.Type.CHARM));
		ItemRegistry.registerItemArtefact(registry, "charm_shapeshifter_orb", MorphSpellPack.MODID, new ItemArtefact(EnumRarity.EPIC, ItemArtefact.Type.CHARM));
		ItemRegistry.registerItemArtefact(registry, "charm_phoenix_feather", MorphSpellPack.MODID, new ItemArtefact(EnumRarity.EPIC, ItemArtefact.Type.CHARM));
		ItemRegistry.registerItemArtefact(registry, "charm_lich_tome", MorphSpellPack.MODID, new ItemLichTome(EnumRarity.RARE, ItemArtefact.Type.CHARM));
		ItemRegistry.registerItemArtefact(registry, "charm_bottled_shadow", MorphSpellPack.MODID, new ItemShadowBottle(EnumRarity.EPIC, ItemArtefact.Type.CHARM));

		ItemRegistry.registerItemArtefact(registry, "clay_of_creation", MorphSpellPack.MODID, new ItemClayOfCreation());
	}

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	public static <T> T placeholder() { return null; }


}