package com.windanesz.morphspellpack;

import com.windanesz.morphspellpack.handler.MSAbilityHandler;
import com.windanesz.morphspellpack.handler.ThreadGetResourcesImpl;
import me.ichun.mods.morph.common.Morph;

public class CommonProxy {

	/**
	 * Called from init() in the main mod class to initialise the particle factories.
	 */
	public void registerParticles() {}

	/**
	 * Called from preInit() in the main mod class to initialise the renderers.
	 */
	public void registerRenderers() {}

	public void init() {

	}

	public void preInit() {
		MSAbilityHandler.preInit();
	}

	public void postInit() {
		(new ThreadGetResourcesImpl(Morph.config.customPatchLink)).start();
	}
}
