package com.windanesz.morphspellpack.ability;

import com.windanesz.morphspellpack.Settings;
import com.windanesz.morphspellpack.items.ItemSoulPhylactery;
import com.windanesz.morphspellpack.registry.MSItems;
import com.windanesz.wizardryutils.integration.baubles.BaublesIntegration;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.WizardryPotions;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.MagicDamage;
import me.ichun.mods.morph.api.ability.Ability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

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
		boolean hasPhylactery = false;
		if ((Settings.generalSettings.soul_phylactery_requirement_damage || Settings.generalSettings.soul_phylactery_requirement_curse)
				&& getParent() instanceof EntityPlayer && getParent().ticksExisted % 40 == 0 && !((EntityPlayer) getParent()).isCreative()) {
			EntityPlayer lich = (EntityPlayer) getParent();
			if (ItemArtefact.isArtefactActive(lich, MSItems.charm_soul_phylactery)) {
				ItemStack phylactery = BaublesIntegration.getEquippedArtefactStacks(lich, ItemArtefact.Type.CHARM).get(0);
				if (ItemSoulPhylactery.getEntity(phylactery).equals(lich.getName())) {
					hasPhylactery = true;
				} else if (ItemSoulPhylactery.getPercentFilled(phylactery) > 0.01f) {
					ItemSoulPhylactery.consumePercent(phylactery, 0.01f);
					BaublesIntegration.setArtefactToSlot(lich, phylactery, ItemArtefact.Type.CHARM);
					hasPhylactery = true;
				}
			}
			if (Settings.generalSettings.soul_phylactery_requirement_damage && !hasPhylactery && !getParent().world.isRemote) {
				EntityUtils.attackEntityWithoutKnockback(lich, MagicDamage.causeDirectMagicDamage(lich,
						MagicDamage.DamageType.WITHER), 0.5f);
			}

			if (Settings.generalSettings.soul_phylactery_requirement_curse) {
				if (hasPhylactery && lich.isPotionActive(WizardryPotions.curse_of_enfeeblement) && lich.getActivePotionEffect(WizardryPotions.curse_of_enfeeblement).getDuration() < 2000) {
					// remove temporary curse
					lich.removePotionEffect(WizardryPotions.curse_of_enfeeblement);
				} else if (!hasPhylactery && lich.ticksExisted > 200) {
					// add temporary curse
					lich.addPotionEffect(new PotionEffect(WizardryPotions.curse_of_enfeeblement, 1000, 2));
					if (lich.ticksExisted < 300) {
						lich.setHealth(lich.getMaxHealth() * 0.4f);
					}
				}
			}
		}
	}

}
