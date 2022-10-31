package com.windanesz.morphspellpack.registry;

import com.windanesz.morphspellpack.MorphSpellPack;
import com.windanesz.morphspellpack.entity.living.EntityBatMinion;
import com.windanesz.morphspellpack.entity.living.EntityLightWisp;
import com.windanesz.morphspellpack.entity.projectile.EntityRadiantSpark;
import com.windanesz.morphspellpack.spell.CurseOfTransformation;
import com.windanesz.morphspellpack.spell.Demorph;
import com.windanesz.morphspellpack.spell.Rabbitify;
import com.windanesz.morphspellpack.spell.ShapeBinding;
import com.windanesz.morphspellpack.spell.ShapeLock;
import com.windanesz.morphspellpack.spell.Skinchanger;
import com.windanesz.morphspellpack.spell.SoulConjuration;
import com.windanesz.morphspellpack.spell.SpellTransformation;
import com.windanesz.morphspellpack.spell.Starfall;
import com.windanesz.morphspellpack.spell.TrueResurrection;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.spell.SpellArrow;
import electroblob.wizardry.spell.SpellMinion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@ObjectHolder(MorphSpellPack.MODID)
@EventBusSubscriber
public final class MSSpells {

	public static final Spell bat_form = placeholder();
	public static final Spell spider_form = placeholder();
	public static final Spell skinchanger = placeholder();
	public static final Spell rabbitify = placeholder();
	public static final Spell disguise = placeholder();
	public static final Spell blaze_form = placeholder();
	public static final Spell shadow_wraith_form = placeholder();
	public static final Spell ice_wraith_form = placeholder();
	public static final Spell lightning_wraith_form = placeholder();
	public static final Spell phoenix_form = placeholder();
	public static final Spell creeper_form = placeholder();
	public static final Spell curse_of_transformation = placeholder();
	public static final Spell shape_lock = placeholder();
	public static final Spell radiant_spark = placeholder();
	public static final Spell starfall = placeholder();
	public static final Spell light_wisp = placeholder();
	public static final Spell lightbringer = placeholder();
	public static final Spell shape_binding = placeholder();
	public static final Spell soul_conjuration = placeholder();
	public static final Spell true_resurrection = placeholder();

	private MSSpells() {} // no instances

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder() { return null; }

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Spell> event) {

		IForgeRegistry<Spell> registry = event.getRegistry();

		registry.register(new SpellTransformation("bat_form", SpellActions.SUMMON, false, "morphspellpack:bat_minion") {
			@Override
			public void morphExtra(World world, EntityLivingBase caster, String morph, int morphDuration) {
				if (!world.isRemote && caster instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer) caster, MSItems.ring_bat)) {
					for (int i = 0; i < 8; i++) {
						EntityBatMinion bat = new EntityBatMinion(world);
						bat.setCaster(caster);
						bat.setLifetime(morphDuration / 2);
						//bat.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, morphDuration, 3));
						bat.setPosition(caster.posX, caster.posY, caster.posZ);
						world.spawnEntity(bat);
					}
				}
			}
		});
		registry.register(new SpellTransformation("blaze_form", SpellActions.SUMMON, false, "minecraft:blaze"));
		registry.register(new SpellTransformation("shadow_wraith_form", SpellActions.SUMMON, false, "ebwizardry:shadow_wraith"));
		registry.register(new SpellTransformation("ice_wraith_form", SpellActions.SUMMON, false, "ebwizardry:ice_wraith"));
		registry.register(new SpellTransformation("lightning_wraith_form", SpellActions.SUMMON, false, "ebwizardry:lightning_wraith"));
		registry.register(new SpellTransformation("spider_form", SpellActions.SUMMON, false, "minecraft:spider"));
		registry.register(new SpellTransformation("phoenix_form", SpellActions.SUMMON, false, "ebwizardry:phoenix"));
		registry.register(new SpellTransformation("creeper_form", SpellActions.SUMMON, false, "minecraft:creeper"));
		registry.register(new SpellTransformation("disguise", SpellActions.SUMMON, false, "morphspellpack:disguise"));
		registry.register(new SpellMinion<>(MorphSpellPack.MODID, "light_wisp", EntityLightWisp::new).soundValues(1, 1.1f, 0.2f));
		registry.register(new Skinchanger());
		registry.register(new Rabbitify());
		registry.register(new CurseOfTransformation());
		registry.register(new SpellArrow<>(MorphSpellPack.MODID, "radiant_spark", EntityRadiantSpark::new)
				.addProperties(Spell.DAMAGE, Spell.EFFECT_DURATION, Spell.EFFECT_STRENGTH).soundValues(1, 1.6f, 0.4f));
		registry.register(new Starfall());
		registry.register(new ShapeLock());
		registry.register(new Demorph());
		registry.register(new SpellTransformation("lightbringer", SpellActions.SUMMON, false, "morphspellpack:light_wisp"));
	//	registry.register(new SoulTransformation());
		registry.register(new ShapeBinding());
		registry.register(new SoulConjuration());
		registry.register(new TrueResurrection());

	}
}
