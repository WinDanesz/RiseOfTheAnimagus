package com.windanesz.morphspellpack.ability.active;

import com.windanesz.morphspellpack.Settings;
import com.windanesz.morphspellpack.items.ItemSoulPhylactery;
import com.windanesz.morphspellpack.registry.MSItems;
import com.windanesz.wizardryutils.integration.baubles.BaublesIntegration;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.MagicDamage;
import me.ichun.mods.morph.api.ability.Ability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class AbilityLichPhylactery extends Ability {

	public static final String name = "lichNeedsPhylactery";

	@SuppressWarnings("unused")
	public AbilityLichPhylactery() {
	}

	@Override
	public String getType() {
		return name;
	}

	@Override
	public void tick() {
		boolean hurtLich = true;
		if (Settings.generalSettings.soul_phylactery_requirement && getParent() instanceof EntityPlayer && getParent().ticksExisted % 20 == 0 && !((EntityPlayer) getParent()).isCreative()) {
			EntityPlayer lich = (EntityPlayer) getParent();
			if (ItemArtefact.isArtefactActive(lich, MSItems.charm_soul_phylactery)) {
				ItemStack phylactery = BaublesIntegration.getEquippedArtefactStacks(lich, ItemArtefact.Type.CHARM).get(0);
				if (ItemSoulPhylactery.getEntity(phylactery).equals(lich.getName())) {
					hurtLich = false;
				} else if (ItemSoulPhylactery.getPercentFilled(phylactery) > 0.01f) {
					ItemSoulPhylactery.consumePercent(phylactery, 0.01f);
					BaublesIntegration.setArtefactToSlot(lich, phylactery, ItemArtefact.Type.CHARM);
					hurtLich = false;
				}
			}
			if (hurtLich) {
				EntityUtils.attackEntityWithoutKnockback(lich, MagicDamage.causeDirectMagicDamage(lich,
						MagicDamage.DamageType.WITHER), 0.5f);
			}
		}
	}

}
