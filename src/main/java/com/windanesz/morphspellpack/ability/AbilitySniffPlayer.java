package com.windanesz.morphspellpack.ability;

import com.google.common.base.Predicate;
import com.windanesz.wizardryutils.tools.WizardryUtilsTools;
import electroblob.wizardry.util.EntityUtils;
import me.ichun.mods.morph.api.ability.Ability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

public class AbilitySniffPlayer extends Ability implements IActiveAbility {

	public static final String name = "locatePlayer";
	public Boolean toggled;

	public int maxCooldown = 0;
	public int cooldown = 0;
	public int maxDistance = 16;

	@SuppressWarnings("unused")
	public AbilitySniffPlayer() {
	}

	public AbilitySniffPlayer(int maxCooldown, int maxDistance) {
		this.maxCooldown = maxCooldown;
		this.maxDistance = maxDistance;
	}

	@Override
	public Ability parse(String[] args) {
		maxCooldown = Integer.parseInt(args[0]);
		maxDistance = Integer.parseInt(args[1]);

		return this;
	}

	@Override
	public Ability clone() {
		return new AbilitySniffPlayer(maxCooldown, maxDistance);
	}

	@Override
	public String getType() {
		return name;
	}

	@Override
	public void tick() {
		if (cooldown > 0) {
			cooldown--;
			toggled = false;
		}

		if (!getParent().world.isRemote && toggled != null && toggled && conditionPredicate()) {

			if (getParent() instanceof EntityPlayerMP) {
				List<EntityPlayer> players = EntityUtils.getEntitiesWithinRadius(maxDistance, getParent().posX, getParent().posY, getParent().posZ, getParent().world, EntityPlayer.class);
				if (!players.isEmpty()) {
					for (EntityPlayer player : players) {
						if (player != this.getParent()) {
							WizardryUtilsTools.sendMessage(getParent(), "ability.morphspellpack:locate_player.msg", false,
									player.getDisplayName(), player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getY());
						}
					}
				}
				cooldown = maxCooldown;
			}
		}

		toggled = false;
	}

	@Override
	public void toggleAbility() {
		toggled = true;
	}
}
