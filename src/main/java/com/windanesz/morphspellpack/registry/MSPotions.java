package com.windanesz.morphspellpack.registry;

import com.windanesz.morphspellpack.MorphSpellPack;
import com.windanesz.morphspellpack.potion.PotionConjuredSoul;
import electroblob.wizardry.potion.Curse;
import electroblob.wizardry.potion.PotionMagicEffect;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@GameRegistry.ObjectHolder(MorphSpellPack.MODID)
@Mod.EventBusSubscriber
public class MSPotions {

	public static final Potion shape_lock = placeholder();
	public static final Potion curse_of_transformation = placeholder();
	public static final Potion conjured_soul = placeholder();

	private MSPotions() {}

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder() { return null; }

	/**
	 * Sets both the registry and unlocalised names of the given potion, then registers it with the given registry. Use
	 * this instead of {@link Potion#setRegistryName(String)} and {@link Potion#setPotionName(String)} during
	 * construction, for convenience and consistency.
	 *
	 * @param registry The registry to register the given potion to.
	 * @param name     The name of the potion, without the mod ID or the .name stuff. The registry name will be
	 *                 {@code morphspellpack:[name]}. The unlocalised name will be {@code potion.morphspellpack:[name].name}.
	 * @param potion   The potion to register.
	 */
	public static void registerPotion(IForgeRegistry<Potion> registry, String name, Potion potion) {
		potion.setRegistryName(MorphSpellPack.MODID, name);
		// For some reason, Potion#getName() doesn't prepend "potion." itself, so it has to be done here.
		potion.setPotionName("potion." + potion.getRegistryName().toString());
		registry.register(potion);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Potion> event) {

		IForgeRegistry<Potion> registry = event.getRegistry();

		registerPotion(registry, "shape_lock", new PotionMagicEffect(false, 0x7623bd,
				new ResourceLocation(MorphSpellPack.MODID, "textures/gui/potion_icons/shape_lock.png")));

		registerPotion(registry, "curse_of_transformation", new Curse(true, 0xc71447,
				new ResourceLocation(MorphSpellPack.MODID, "textures/gui/potion_icons/curse_of_transformation.png")));

		registerPotion(registry, "conjured_soul", new PotionConjuredSoul(false, 0x000000,
				new ResourceLocation(MorphSpellPack.MODID, "textures/gui/potion_icons/conjured_soul.png")));

	}

}
