package com.windanesz.morphspellpack.ability.trait;

import com.windanesz.morphspellpack.ability.IActiveAbility;
import electroblob.wizardry.util.BlockUtils;
import me.ichun.mods.morph.api.ability.Ability;
import me.ichun.mods.morph.common.Morph;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

public class AbilityHoverTogglable extends Ability implements IActiveAbility {
	public static final String name = "hoverWithToggle";

	public Integer limit; //limit the number of times this can be triggered (until the entity hits the ground)
	public transient boolean onGround;
	public transient int limitCount;
	public transient boolean sneakHeld;
	public transient boolean enabled = true;

	@Override
	public String getType() {
		return name;
	}

	@Override
	public void init() {
		onGround = false;
		limitCount = limit != null ? limit : -1;
		sneakHeld = false;
	}

	@Override
	public void tick() {
		if (getParent().world.isRemote) return;
		if (!enabled) { return; }


		if (Morph.config.enableFlight == 0) {
			return;
		}
		if (getParent().world.isRemote) {
			tickClient();
		}
		getParent().fallDistance -= getParent().fallDistance * getStrength();
	}

	@SideOnly(Side.CLIENT)
	public void tickClient() {
		if (getParent() == Minecraft.getMinecraft().player && !Minecraft.getMinecraft().player.capabilities.isFlying) {
			Integer floorLevel = BlockUtils.getNearestFloor(getParent().world, new BlockPos(getParent()), 4);
			sneakHeld = Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode());
			boolean jumpKeyHeld = Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode());

			if (!(sneakHeld || getParent().motionY > 0)) {
				if (floorLevel == null || getParent().posY - floorLevel > 3) {
					getParent().motionY = -0.2;
				} else if (getParent().posY - floorLevel < 2) {
					getParent().motionY = 0.1;
				} else {
					getParent().motionY = 0.0;
				}
			}
		}
	}

	@Override
	public void toggleAbility() {
		if (getParent().isSneaking()) {
			enabled = !enabled;
		}
	}
}
