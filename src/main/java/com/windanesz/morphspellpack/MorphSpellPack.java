package com.windanesz.morphspellpack;

import com.windanesz.morphspellpack.client.gui.MSGuiHandler;
import com.windanesz.morphspellpack.handler.LichHandler;
import com.windanesz.morphspellpack.handler.MSAbilityHandler;
import com.windanesz.morphspellpack.packet.MSPacketHandler;
import com.windanesz.morphspellpack.registry.BlockRegistry;
import electroblob.wizardry.api.WizardryEnumHelper;
import me.ichun.mods.morph.common.Morph;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;

@Mod(modid = MorphSpellPack.MODID, name = MorphSpellPack.NAME, version = "@VERSION@", acceptedMinecraftVersions = "[@MCVERSION@]",
		dependencies = "required-after:ebwizardry@[@WIZARDRY_VERSION@,4.4);required-after:morph@[7.2.0,8.0.0);required-after:wizardryutils@[1.1.1,2.0.0);")
public class MorphSpellPack {

	public static final String MODID = "morphspellpack";
	public static final String NAME = "Rise of the Animagus";

	public static Logger logger;

	// The instance of the mod that Forge uses.
	@Mod.Instance(MorphSpellPack.MODID)
	public static MorphSpellPack instance;

	// Location of the proxy code, used by Forge.
	@SidedProxy(clientSide = "com.windanesz.morphspellpack.client.ClientProxy", serverSide = "com.windanesz.morphspellpack.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		WizardryEnumHelper.addSpellType("TRANSFORMATION", "transformation");
		logger = event.getModLog();
		proxy.registerRenderers();
		MSAbilityHandler.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

		MinecraftForge.EVENT_BUS.register(instance);
		proxy.registerParticles();
		proxy.init();
		MSPacketHandler.initPackets();
		LichHandler.init();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new MSGuiHandler());

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		BlockRegistry.registerOreDictionaryEntries();
		Morph.config.morphTime = 30;
		Morph.config.maxMorphHealth = 1000;
		proxy.postInit();
	}

	@EventHandler
	public void serverStartup(FMLServerStartingEvent event) { }

}
