package com.windanesz.morphspellpack.handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.windanesz.morphspellpack.MorphSpellPack;
import me.ichun.mods.morph.api.ability.Ability;
import me.ichun.mods.morph.common.handler.AbilityHandler;
import me.ichun.mods.morph.common.handler.NBTHandler;
import net.minecraft.entity.EntityLivingBase;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ThreadGetResourcesImpl extends Thread {

	public String sitePrefix = "https://raw.github.com/iChun/Morph/1.12.2/src/main/resources/assets/morph/mod/";//TODO change this before release.

	public ThreadGetResourcesImpl(String prefix) {
		if (!prefix.isEmpty()) {
			sitePrefix = prefix;
		}
		this.setName("Morph Resource Thread");
		this.setDaemon(true);
	}

	@Override
	public void run() {
		HashMap<String, HashMap<String, String>> json = getResource("nbt_modifiers.json", new TypeToken<HashMap<String, HashMap<String, String>>>() {}.getType());

		int mcNBTModifiers = 0;

		NBTHandler.nbtModifiers.clear();
		for (Map.Entry<String, HashMap<String, String>> e : json.entrySet()) {
			try {
				if (e.getKey().startsWith("example.class.")) {
					continue;
				}

				Class clz = Class.forName(e.getKey());

				NBTHandler.TagModifier tagModifier = new NBTHandler.TagModifier();
				HashMap<String, String> map = e.getValue();

				for (Map.Entry<String, String> modifier : map.entrySet()) {
					String value = modifier.getValue();
					NBTHandler.handleModifier(tagModifier, modifier.getKey(), value);
				}
				if (!tagModifier.modifiers.isEmpty() && EntityLivingBase.class.isAssignableFrom(clz)) {
					NBTHandler.nbtModifiers.put(clz, tagModifier);
					if (clz.getName().startsWith("net.minecraft")) {
						mcNBTModifiers++;
					} else {
						MorphSpellPack.logger.info("Adding NBT modifiers for morphs for class: " + clz.getName());
					}
				}
			}
			catch (ClassNotFoundException ignored) {}
		}

		if (mcNBTModifiers > 0) {
			MorphSpellPack.logger.info("Loaded NBT modifiers for presumably " + mcNBTModifiers + " Minecraft mobs");
		} else {
			MorphSpellPack.logger.warn("No NBT modifiers for Minecraft mobs? This might be an issue!");
		}

		HashMap<String, String[]> abilitiesJson = getResource("ability_support.json", new TypeToken<HashMap<String, String[]>>() {}.getType());
		int mcMappings = 0;
		for (Map.Entry<String, String[]> e : abilitiesJson.entrySet()) {
			try {
				Class.forName(e.getKey());

				ArrayList<Ability> abilities = new ArrayList<>();
				for (String ability : e.getValue()) {
					boolean hasArgs = false;
					if (ability == null) { continue; }
					if (ability.contains("|")) {
						ArrayList<String> argVars = new ArrayList<>();
						hasArgs = true;
						String args = ability.split("\\|")[1];
						ability = ability.split("\\|")[0];
						if (args.contains(",")) {
							for (String arg : args.split(",")) {
								argVars.add(arg.trim());
							}
						} else {
							argVars.add(args.trim());
						}
						try {
							Class abilityClass = AbilityHandler.getInstance().STRING_TO_CLASS_MAP.get(ability);
							if (abilityClass != null) {
								Ability ab = ((Ability) abilityClass.getConstructor().newInstance());
								try {
									ab.parse(argVars.toArray(new String[0]));
								}
								catch (Exception e2) {
									MorphSpellPack.logger.warn("Mappings are erroring! These mappings are probably invalid or outdated: " + abilityClass.getName() + ", " + ability + ", args: " + args);
								}
								abilities.add(ab);
							} else {
								MorphSpellPack.logger.warn("Ability \"" + ability + "\" does not exist for: " + e.getKey() + ", args: " + args);
							}
						}
						catch (Exception e2) {
							e2.printStackTrace();
						}
					}
					if (!ability.isEmpty() && !hasArgs) {
						Class abilityClass = AbilityHandler.getInstance().STRING_TO_CLASS_MAP.get(ability);
						if (abilityClass != null) {
							try {
								abilities.add((Ability) abilityClass.getConstructor().newInstance());
							}
							catch (Exception e2) {
								e2.printStackTrace();
							}
						} else {
							MorphSpellPack.logger.warn("Ability \"" + ability + "\" does not exist for: " + e.getKey());
						}
					}
				}

				if (abilities.size() > 0) {
					Class<? extends EntityLivingBase> entityClass = (Class<? extends EntityLivingBase>) Class.forName(e.getKey());
					if (entityClass != null) {
						if (AbilityHandler.getInstance().ABILITY_MAP.containsKey(entityClass)) {
							MorphSpellPack.logger.warn("Ignoring ability mapping for " + e.getKey() + "! Already has abilities mapped!");
						} else {
							if (entityClass.getName().startsWith("net.minecraft")) {
								mcMappings++;
							} else {
								StringBuilder sb = new StringBuilder();
								sb.append("Adding ability mappings ");
								for (int i = 0; i < abilities.size(); i++) {
									Ability a = abilities.get(i);
									sb.append(a.getType());
									if (i != abilities.size() - 1) {
										sb.append(", ");
									}
								}
								sb.append(" to ");
								sb.append(entityClass);

								MorphSpellPack.logger.info(sb.toString());
							}
							AbilityHandler.getInstance().mapAbilities(entityClass, abilities.toArray(new Ability[0]));
						}
					}
				}
			}
			catch (ClassNotFoundException ignored) {}
		}
		MorphSpellPack.logger.info("Found and mapped ability mappings for " + mcMappings + " presumably Minecraft mobs.");
	}

	public <T> T getResource(String name, Type mapType) {
		T objectType;
		Gson gson = new Gson();

		Reader fileIn = new InputStreamReader(MorphSpellPack.class.getResourceAsStream("/assets/morphspellpack/morphdata/" + name));
		objectType = gson.fromJson(fileIn, mapType);
		try {
			fileIn.close();
		}
		catch (IOException ignored) {}

		return objectType;
	}

}
