package com.windanesz.morphspellpack.registry;

import com.windanesz.morphspellpack.MorphSpellPack;
import com.windanesz.morphspellpack.entity.construct.EntityStarfall;
import com.windanesz.morphspellpack.entity.living.EntityBatMinion;
import com.windanesz.morphspellpack.entity.living.EntityDisguise;
import com.windanesz.morphspellpack.entity.living.EntityLich;
import com.windanesz.morphspellpack.entity.living.EntityLightWisp;
import com.windanesz.morphspellpack.entity.living.EntityTemporaryRabbit;
import com.windanesz.morphspellpack.entity.projectile.EntityRadiantSpark;
import com.windanesz.wizardryutils.registry.EntityRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class MSEntities {

	private MSEntities() {}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<EntityEntry> event) {

		IForgeRegistry<EntityEntry> registry = event.getRegistry();

		registry.register(EntityRegistry.createEntry(EntityTemporaryRabbit.class, "temporary_rabbit", MorphSpellPack.MODID, EntityRegistry.TrackingType.LIVING).build());
		registry.register(EntityRegistry.createEntry(EntityBatMinion.class, "bat_minion", MorphSpellPack.MODID, EntityRegistry.TrackingType.LIVING).build());
		registry.register(EntityRegistry.createEntry(EntityDisguise.class, "disguise", MorphSpellPack.MODID, EntityRegistry.TrackingType.LIVING).build());
		registry.register(EntityRegistry.createEntry(EntityLightWisp.class, "light_wisp", MorphSpellPack.MODID, EntityRegistry.TrackingType.LIVING).build());
		registry.register(EntityRegistry.createEntry(EntityRadiantSpark.class, "radiant_spark", MorphSpellPack.MODID, EntityRegistry.TrackingType.PROJECTILE).build());
		registry.register(EntityRegistry.createEntry(EntityStarfall.class, "starfall", MorphSpellPack.MODID, EntityRegistry.TrackingType.CONSTRUCT).build());
		registry.register(EntityRegistry.createEntry(EntityLich.class, "lich", MorphSpellPack.MODID, EntityRegistry.TrackingType.LIVING).build());

	}
}
