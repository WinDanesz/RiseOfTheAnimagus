package com.windanesz.morphspellpack.registry;

import com.windanesz.morphspellpack.MorphSpellPack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

@GameRegistry.ObjectHolder(MorphSpellPack.MODID)
@Mod.EventBusSubscriber(modid = MorphSpellPack.MODID)
public class MSSounds {
	private MSSounds() {}

	private static final List<SoundEvent> sounds = new ArrayList<>();

	public static final SoundEvent LIHT_WISP_DEATH = createSound("entity.light_wisp.death");
	public static final SoundEvent LIGHT_WISP_HURT = createSound("entity.light_wisp.hurt");
	public static final SoundEvent LIGHT_WISP_NOISE = createSound("entity.light_wisp.noise");
	public static final SoundEvent RADIANT_SPARK_HIT = createSound("entity.radiant_spark.hit");

	public static SoundEvent createSound(String name) {
		return createSound(MorphSpellPack.MODID, name);
	}

	/**
	 * Creates a sound with the given name, to be read from {@code assets/[modID]/sounds.json}.
	 */
	public static SoundEvent createSound(String modID, String name) {
		// All the setRegistryName methods delegate to this one, it doesn't matter which you use.
		return new SoundEvent(new ResourceLocation(modID, name)).setRegistryName(name);
	}


	// For some reason, sound events seem to work even when they aren't registered, without even so much as a warning.

	@SubscribeEvent
	public static void register(RegistryEvent.Register<SoundEvent> event){
		event.getRegistry().registerAll(sounds.toArray(new SoundEvent[0]));
	}
}