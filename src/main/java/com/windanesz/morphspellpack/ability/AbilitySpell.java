package com.windanesz.morphspellpack.ability;

import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.packet.PacketCastSpell;
import electroblob.wizardry.packet.WizardryPacketHandler;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import me.ichun.mods.morph.api.ability.Ability;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class AbilitySpell extends Ability implements IActiveAbility {

	public static final String name = "spellCast";
	public Boolean castingToggled; //reset motionY when the triggered?
	public Spell spell = Spells.none;

	public int cooldown = 0;
	public int maxCooldown = 0;
	public int duration = 0;

	@SuppressWarnings("unused")
	public AbilitySpell() {
	}

	public AbilitySpell(Spell spell, int maxCooldown, int duration) {
		this.spell = spell;
		this.maxCooldown = maxCooldown;
		this.duration = duration;
	}

	@Override
	public Ability parse(String[] args) {
		spell = Spell.get(args[0]);
		maxCooldown = Integer.parseInt(args[1]);
		duration = Integer.parseInt(args[2]);
		return this;
	}

	@Override
	public Ability clone() {
		return new AbilitySpell(spell, maxCooldown, duration);
	}

	@Override
	public String getType() {
		return name;
	}

	@Override
	public void tick() {
		if (cooldown > 0) {
			cooldown--;
			castingToggled = false;
		}

		if (castingToggled != null && castingToggled && conditionPredicate()) {

			if (getParent() instanceof EntityPlayerMP) {

				EntityPlayerMP caster = (EntityPlayerMP) getParent();

				if (spell.isContinuous) {

					WizardData data = WizardData.get(caster);

					// Events/packets for continuous spell casting via commands are dealt with in WizardData.

					if (data != null) {

						//if (data.isCasting()) {
						//	data.stopCastingContinuousSpell();
						//} else {
							SpellModifiers modifiers = new SpellModifiers();
							int duration = 60000;
							data.startCastingContinuousSpell(spell, modifiers, duration);
							cooldown = 0;
						//}
					}

				} else {
					SpellModifiers modifiers = new SpellModifiers();
					if (spell.cast(caster.world, caster, EnumHand.MAIN_HAND, 0, modifiers)) {
						MinecraftForge.EVENT_BUS.post(new SpellCastEvent.Post(SpellCastEvent.Source.COMMAND, spell, caster, modifiers));
						this.cooldown = maxCooldown;
						if (spell.requiresPacket()) {
							// Sends a packet to all players in dimension to tell them to spawn particles.
							// Only sent if the spell succeeded, because if the spell failed, you wouldn't
							// need to spawn any particles!
							IMessage msg = new PacketCastSpell.Message(caster.getEntityId(), null, spell, modifiers);
							WizardryPacketHandler.net.sendToDimension(msg, caster.world.provider.getDimension());
						}
					}
				}
			}
		}

		castingToggled = false;
	}

	public void setSpell(Spell spell) {
		this.spell = spell;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	@Override
	public void toggleAbility() {
		castingToggled = true;
	}
}
