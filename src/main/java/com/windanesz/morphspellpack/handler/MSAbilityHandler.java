package com.windanesz.morphspellpack.handler;

import com.windanesz.morphspellpack.ability.active.AbilityDisguise;
import com.windanesz.morphspellpack.ability.active.AbilityLichPhylactery;
import com.windanesz.morphspellpack.ability.active.AbilityPotionResistance;
import com.windanesz.morphspellpack.ability.active.AbilitySelfDetonate;
import com.windanesz.morphspellpack.ability.active.AbilitySpell;
import com.windanesz.morphspellpack.ability.active.AbilitySpellFromArtefact;
import com.windanesz.morphspellpack.ability.active.AbilityTimedPotion;
import com.windanesz.morphspellpack.ability.active.AbilityWaterBreath;
import com.windanesz.morphspellpack.ability.active.AbilityWebWalk;
import com.windanesz.morphspellpack.ability.trait.AbilityHover;
import com.windanesz.morphspellpack.ability.trait.AbilityHoverTogglable;
import me.ichun.mods.morph.common.handler.AbilityHandler;

public class MSAbilityHandler {

	public static void preInit() {
		AbilityHandler.getInstance().registerAbility(AbilitySpell.name, AbilitySpell.class);
		AbilityHandler.getInstance().registerAbility(AbilitySpellFromArtefact.name, AbilitySpellFromArtefact.class);
		AbilityHandler.getInstance().registerAbility(AbilityHover.name, AbilityHover.class);
		AbilityHandler.getInstance().registerAbility(AbilitySelfDetonate.name, AbilitySelfDetonate.class);
		AbilityHandler.getInstance().registerAbility(AbilityWebWalk.name, AbilityWebWalk.class);
		AbilityHandler.getInstance().registerAbility(AbilityDisguise.name, AbilityDisguise.class);
		AbilityHandler.getInstance().registerAbility(AbilityTimedPotion.name, AbilityTimedPotion.class);
		AbilityHandler.getInstance().registerAbility(AbilityLichPhylactery.name, AbilityLichPhylactery.class);
		AbilityHandler.getInstance().registerAbility(AbilityPotionResistance.name, AbilityPotionResistance.class);
		AbilityHandler.getInstance().registerAbility(AbilityWaterBreath.name, AbilityWaterBreath.class);
	}
}
