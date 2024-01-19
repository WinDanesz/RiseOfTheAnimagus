package com.windanesz.morphspellpack.ability;

import com.windanesz.morphspellpack.spell.SpellTransformation;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.util.EntityUtils;
import me.ichun.mods.morph.api.ability.Ability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;

public class AbilitySelfDetonate extends Ability implements IActiveAbility {

	public static final String name = "selfDetonate";
	public Boolean castingToggled;
	public Boolean suicide = false;
	public Boolean damagesTerrain = true;
	int countdown = 40;
	float strength = 1.0f;

	@SuppressWarnings("unused")
	public AbilitySelfDetonate() { }

	public AbilitySelfDetonate(boolean damagesTerrain, boolean suicide, float strength) {
		this.damagesTerrain = damagesTerrain;
		this.suicide = suicide;
		this.strength = strength;
	}

	@Override
	public Ability parse(String[] args) {
		damagesTerrain = Boolean.parseBoolean(args[0]);
		suicide = Boolean.parseBoolean(args[1]);
		strength = Float.parseFloat(args[2]);
		return this;
	}

	@Override
	public Ability clone() {
		return new AbilitySelfDetonate(damagesTerrain, suicide, strength);
	}

	@Override
	public String getType() {
		return name;
	}

	@Override
	public void tick() {
		if (castingToggled != null && castingToggled) {
			countdown--;

			if (countdown > 0) {

				return;
			}

			// explode and revert
			if (countdown == 0) {
				getParent().world.createExplosion(getParent(), getParent().posX, getParent().posY, getParent().posZ, strength, damagesTerrain);
				if (suicide && getParent() instanceof EntityPlayerMP) {
					DamageSource source = DamageSource.MAGIC;
					EntityUtils.attackEntityWithoutKnockback(getParent(), source, 200);
				} else {
					WizardData data = WizardData.get((EntityPlayer) getParent());
					if (data != null) {
						data.setVariable(SpellTransformation.MORPH_DURATION, 4);
					}
					//SpellTransformation.demorphPlayer(getParent());
				}
			}

		}
	}

	@Override
	public void toggleAbility() {
		castingToggled = true;
	}
}
