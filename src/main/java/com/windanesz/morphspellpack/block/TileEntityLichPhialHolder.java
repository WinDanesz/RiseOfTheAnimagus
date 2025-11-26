package com.windanesz.morphspellpack.block;

import com.windanesz.morphspellpack.MSChunkLoader;
import com.windanesz.morphspellpack.MorphSpellPack;
import com.windanesz.morphspellpack.Settings;
import com.windanesz.morphspellpack.handler.LichHandler;
import com.windanesz.morphspellpack.items.ItemSoulPhylactery;
import com.windanesz.morphspellpack.registry.MSBlocks;
import com.windanesz.morphspellpack.registry.MSPotions;
import com.windanesz.wizardryutils.server.Attributes;
import com.windanesz.wizardryutils.tools.WizardryUtilsTools;
import electroblob.wizardry.block.BlockCrystal;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.item.ItemSpellBook;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.registry.WizardryPotions;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.Location;
import electroblob.wizardry.util.NBTExtras;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellProperties;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class TileEntityLichPhialHolder extends TileEntity implements IInventory, ITickable, IChunkLoaderTile {
	private UUID ownerUUID;
	private NonNullList<ItemStack> inventory;
	private int levels = -1;
	private final int lunarCycle = 192000;
	int countdownTillNextSpellBook = 40;

	List<Optional<Element>> pyramid = new ArrayList<>(Collections.nCopies(5, Optional.empty()));

	ForgeChunkManager.Ticket chunkTicket = null;

	public TileEntityLichPhialHolder() {
		inventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
	}

	@Override
	public void update() {
		if (!this.world.isRemote) { // Ensure this only runs on the server side
			applySpells();
			if (this.world.getTotalWorldTime() % 100 == 0) { // Every 100 ticks (5 seconds)
				if (!getPhylactery().isEmpty()) { // Check if there is a phylactery
					if (getOwner() != null) {
						Location loc = LichHandler.getReceptacleLocation(getOwner());
						if (loc != null && loc.pos.equals(pos)) {
							restorePercent(0.01f); // Restore 0.01f percent charges
							applyHPModifier(getOwner(), getPercentFilled());
						}
						updateLayers();
						applyEffects();

						if (getPercentFilled() < 0.1) {

							int skulls = 0;
							for (BlockPos blockPos : BlockUtils.getBlockSphere(pos, 4)) {
								if (world.getBlockState(blockPos).getBlock() == Blocks.SKULL) {
									skulls++;
								}
							}
							if (skulls >= 4) {
								return;
							}

							if (!((EntityPlayer) getOwner()).isSpectator()) {
								((EntityPlayer) getOwner()).setGameType(GameType.SPECTATOR);
							}
							WizardryUtilsTools.sendMessage(getOwner(), "Your receptacle is at " + (int) (getPercentFilled() * 100) + "%%", false);
							getOwner().addPotionEffect(new PotionEffect(WizardryPotions.containment, 140, 0));
						} else if (((EntityPlayer) getOwner()).isSpectator()) {
							((EntityPlayer) getOwner()).setGameType(GameType.SURVIVAL);
						}

					}
				}
			}
			if (this.world.getTotalWorldTime() % 20 == 0 && getOwner() != null && pyramid.get(2).isPresent() && pyramid.get(2).get() != Element.MAGIC) {
				if (countdownTillNextSpellBook >= 0) {
					countdownTillNextSpellBook -= 20;
				} else {
					if (getOwner().getDistanceSq(pos) < 4) {
						Element element = pyramid.get(2).get();
						WizardData data = WizardData.get((EntityPlayer) getOwner());
						List<Spell> elementalSpells = Spell.registry.getValuesCollection().stream().filter(spell -> spell.getElement() == element)
								.filter(spell -> spell.isEnabled(SpellProperties.Context.LOOTING) && spell.isEnabled(SpellProperties.Context.BOOK)).collect(Collectors.toList());

						Spell grantedSpell = Spells.none;
						for (int tier = 0; tier <= 3; tier++) {
							int finalTier = tier;
							List<Spell> unknownSpells = elementalSpells.stream().filter(spell -> spell.getTier().ordinal() == finalTier).filter(spell -> !data.hasSpellBeenDiscovered(spell)).collect(Collectors.toList());
							if (!unknownSpells.isEmpty()) {
								grantedSpell = unknownSpells.get(world.rand.nextInt(unknownSpells.size()));
								break;
							}
						}
						if (grantedSpell != Spells.none) {
							countdownTillNextSpellBook = lunarCycle;
							List<Item> bookTypeList = ForgeRegistries.ITEMS.getValuesCollection().stream().filter(i -> i instanceof ItemSpellBook).collect(Collectors.toList());
							for (Item currentBook : bookTypeList) {
								if (grantedSpell.applicableForItem(currentBook)) {
									ItemStack spellBook = new ItemStack(currentBook, 1, grantedSpell.metadata());
									WizardryUtilsTools.giveStackToPlayer((EntityPlayer) getOwner(), spellBook);
									data.discoverSpell(grantedSpell);
									data.sync();
									WizardryUtilsTools.sendMessage(getOwner(), "Your receptacle granted you the " + grantedSpell.getDisplayName() + " spell", false);
									break;
								}
							}
						} else {
							WizardryUtilsTools.sendMessage(getOwner(), "You know every spell of this element.", true);
						}
					} else {
						if (getOwner().ticksExisted % 6000 == 0) {
							WizardryUtilsTools.sendMessage(getOwner(), "Your receptacle is ready to grant you a new spell if you visit it", false);
						}
					}
				}
			}
		}
		if (world.isRemote && getOwner() != null && getOwner().getDistanceSq(pos) < 4) {
			ParticleBuilder.create(ParticleBuilder.Type.FLASH).clr(2, 199, 140).fade(0, 0, 0).spin(0.8f, -0.02f)
					.time(40).pos(new Vec3d(pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5)).scale(1.2f).spawn(world);
			ParticleBuilder.create(ParticleBuilder.Type.FLASH).clr(2, 199, 140).vel(0, 0.1, 0).fade(0, 0, 0)
					.spin(0.4f, 0.02f).time(20).pos(new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)).scale(1.2f).spawn(world);

		}
		//		if (world.isRemote && getOwner() != null) {
		//			for(int i = 0; i < 2 * 3; i++) ParticleBuilder.create(ParticleBuilder.Type.CLOUD)
		//					.scale(10)
		//					.time(600)
		//					.pos(pos.getX() + world.rand.nextInt(50) * (world.rand.nextBoolean() ? 1 : -1),
		//							pos.getY() + 40 + world.rand.nextInt(10) ,
		//							pos.getZ()  + world.rand.nextInt(50) * (world.rand.nextBoolean() ? 1 : -1))
		//					.clr(0.1f, 0.1f, 0.1f).shaded(false).spawn(world);
		//
		//		}
	}

	private void applySpells() {
		if (getOwner() instanceof EntityPlayer && getOwner().ticksExisted % 20 == 0) {
			if (getOwner().isPotionActive(MSPotions.phylacterys_grasp)) {
				if (getPercentFilled() > 0.12f) {
					consumePercent(0.05f);
				} else {
					getOwner().removePotionEffect(MSPotions.phylacterys_grasp);
				}
			}
		}
	}

	public void applyEffects() {
		for (int level = 1; level < pyramid.size(); level++) {
			Optional<Element> elementOptional = pyramid.get(level);
			if (elementOptional.isPresent() && getOwner() != null && !getOwner().world.isRemote) {
				Element element = elementOptional.get();
				if (element == Element.MAGIC) {
					if (level == 2) {
						if (getOwner() instanceof EntityPlayer && !getOwner().world.isRemote) {
							IAttributeInstance maxHealthAttribute = getOwner().getEntityAttribute(Attributes.CONDENSING);
							AttributeModifier modifier = new AttributeModifier(UUID.fromString("4018222c-cf85-4627-bfd9-efbd94a7d928"),
									"Phylactery mana regen", Settings.generalSettings.lich_soul_receptacle_layer_3_magic_crystal_block_mana_regen_amount, EntityUtils.Operations.ADD);
							if (maxHealthAttribute.getModifier(modifier.getID()) == null) {
								maxHealthAttribute.applyModifier(modifier);
							}
						}
					} else if (level == 3) {
						world.getPlayers(EntityPlayer.class, player -> player.getDistanceSq(pos) < 100).forEach(player -> {
							if (player != getOwner() && !AllyDesignationSystem.isAllied(this.getOwner(), player) && ForgeRegistries.POTIONS.containsKey(new ResourceLocation("ancientspellcraft:magical_exhaustion"))) {
								player.addPotionEffect(new PotionEffect(Objects.requireNonNull(ForgeRegistries.POTIONS.getValue(
										new ResourceLocation("ancientspellcraft:magical_exhaustion"))), 400, 3));
							}
						});
					} else if (level == 4) {
						getOwner().addPotionEffect(new PotionEffect(WizardryPotions.empowerment, 1000, 0));
					}
				} else if (element == Element.FIRE) {
					if (level == 4) {
						getOwner().addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 1000, 0));
					}
				} else if (element == Element.LIGHTNING) {
					if (level == 4) {
						getOwner().addPotionEffect(new PotionEffect(WizardryPotions.static_aura, 1000, 0));
					}
				} else if (element == Element.NECROMANCY) {
					// Apply necromancy effect
				} else if (element == Element.EARTH) {
					if (level == 4) {
						getOwner().addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 1000, 0));
					}
				}
				if (!pyramid.get(2).isPresent() || pyramid.get(2).get() != Element.MAGIC) {
					IAttributeInstance maxHealthAttribute = getOwner().getEntityAttribute(Attributes.CONDENSING);
					AttributeModifier modifier = new AttributeModifier(UUID.fromString("4018222c-cf85-4627-bfd9-efbd94a7d928"),
							"Phylactery mana regen", Settings.generalSettings.lich_soul_receptacle_layer_3_magic_crystal_block_mana_regen_amount, EntityUtils.Operations.ADD);
					if (maxHealthAttribute.getModifier(modifier.getID()) != null) {
						maxHealthAttribute.removeModifier(modifier);
					}
				}
			}
		}
	}

	//	public void updateLayers() {
	//		boolean clearLayer = false;
	//		// layer 0 is always just the receptacle itself
	//		for (int level = 1; level < pyramid.size(); level++) {
	//			if (clearLayer) {
	//				pyramid.set(level, Optional.empty());
	//			}
	//			if (!setLayerElement(level).isPresent()) {
	//				clearLayer = true;
	//			}
	//		}
	//
	//	}

	public void updateLayers() {
		float fullness = getPercentFilled();
		boolean clearLayer = false;

		// layer 0 is always just the receptacle itself
		for (int level = 1; level < pyramid.size(); level++) {
			if (clearLayer) {
				pyramid.set(level, Optional.empty());
			}
			// Check if the fullness is enough for the current layer
			if (fullness >= level * 0.25f) {
				if (!setLayerElement(level).isPresent()) {
					clearLayer = true;
				}
			} else {
				pyramid.set(level, Optional.empty());
				clearLayer = true;
			}
		}
	}

	private Optional<Element> setLayerElement(int layerIndex) {
		Optional<Element> element = Optional.empty();
		int xCoordinate = this.pos.getX();
		int yCoordinate = this.pos.getY() - layerIndex;
		int zCoordinate = this.pos.getZ();

		Block firstBlock = null;

		for (int x = xCoordinate - layerIndex; x <= xCoordinate + layerIndex; ++x) {
			for (int z = zCoordinate - layerIndex; z <= zCoordinate + layerIndex; ++z) {
				IBlockState state = this.world.getBlockState(new BlockPos(x, yCoordinate, z));
				Block currentBlock = state.getBlock();
				if (!(currentBlock instanceof BlockCrystal)) {
					pyramid.set(layerIndex, Optional.empty());
					return Optional.empty();
				}

				Element currentBlockElement = Element.values()[currentBlock.getMetaFromState(state)];
				if (firstBlock == null) {
					firstBlock = currentBlock;
					element = Optional.of(currentBlockElement);
				} else if (currentBlock != firstBlock || currentBlockElement != element.get()) {
					pyramid.set(layerIndex, Optional.empty());
					return Optional.empty();
				}
			}
		}

		// Assuming that the Element of a BlockCrystal can be obtained by calling getBlockElement
		pyramid.set(layerIndex, element);
		return element;
	}

	public List<Optional<Element>> getCrystalPyramid() {
		return pyramid;
	}

	public static void applyHPModifier(EntityLivingBase player, float percentFilled) {
		if (player != null && !player.world.isRemote) {
			IAttributeInstance maxHealthAttribute = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
			AttributeModifier modifier = new AttributeModifier(UUID.fromString("3c7db14f-4c7e-4e3c-8970-5fdaf7f6a9b3"), "Phylactery max health limiter", -1 + percentFilled, 1);
			if (maxHealthAttribute.getModifier(modifier.getID()) != null) {
				maxHealthAttribute.removeModifier(modifier);
			}
			maxHealthAttribute.applyModifier(modifier);
		}
	}

	public void sync() {
		this.world.markAndNotifyBlock(this.pos, (Chunk) null, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
	}

	@Nullable
	public EntityLivingBase getOwner() {
		ItemStack phylactery = getPhylactery();
		if (!phylactery.isEmpty() && phylactery.getItem() instanceof ItemSoulPhylactery && phylactery.hasTagCompound()) {
			String ownerName = ItemSoulPhylactery.getEntity(phylactery);
			List<EntityPlayer> players = this.world.playerEntities;
			for (EntityPlayer player : players) {
				if (player.getName().equals(ownerName)) {
					return player;
				}
			}
		}
		return null;
	}

	public void setOwner(@Nullable EntityLivingBase caster) {
		this.ownerUUID = caster == null ? null : caster.getUniqueID();
	}

	public final NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}

	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < getSizeInventory(); i++) {
			if (!getStackInSlot(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the stack in the given slot.
	 */
	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory.get(slot);
	}

	/**
	 * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
	 */
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack itemstack = ItemStackHelper.getAndSplit(this.inventory, slot, amount);
		if (!itemstack.isEmpty()) {
			this.markDirty();
		}
		return itemstack;
	}

	/**
	 * Removes a stack from the given slot and returns it.
	 */
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.inventory, index);
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		ItemStack itemstack = inventory.get(slot);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		inventory.set(slot, stack);
		markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return player.getUniqueID().equals(this.ownerUUID);
	}

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return stack.getItem() instanceof ItemSoulPhylactery;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		for (int i = 0; i < getSizeInventory(); i++) {
			setInventorySlotContents(i, ItemStack.EMPTY);
		}
		this.world.setBlockState(pos, MSBlocks.lich_phial_holder_empty.getDefaultState());
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	public void setPhylactery(ItemStack heldItem) {
		if (heldItem.getItem() instanceof ItemSoulPhylactery) {
			setInventorySlotContents(0, heldItem);
		}
	}

	public ItemStack getPhylactery() {
		return getStackInSlot(0);
	}

	public float getPercentFilled() {
		return getPhylactery().hasTagCompound() ? ItemSoulPhylactery.getPercentFilled(getPhylactery()) : 0f;
	}

	public void consumePercent(float percentToConsume) {
		if (getPhylactery().getItem() instanceof ItemSoulPhylactery) {
			ItemStack stack = getPhylactery().copy();
			ItemSoulPhylactery.consumePercent(stack, percentToConsume);
			setPhylactery(stack);
		}
	}

	public void restorePercent(float percentToRestore) {
		if (getPhylactery().getItem() instanceof ItemSoulPhylactery) {
			ItemStack stack = getPhylactery().copy();
			ItemSoulPhylactery.addPercent(stack, percentToRestore);
			setPhylactery(stack);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if (this.ownerUUID != null) {
			nbt.setUniqueId("ownerUUID", this.ownerUUID);
		}
		NBTTagList inventoryList = new NBTTagList();
		for (int i = 0; i < getSizeInventory(); i++) {
			ItemStack stack = getStackInSlot(i);
			if (!stack.isEmpty()) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				inventoryList.appendTag(tag);
			}
		}

		NBTExtras.storeTagSafely(nbt, "Inventory", inventoryList);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasUniqueId("ownerUUID")) {
			this.ownerUUID = nbt.getUniqueId("ownerUUID");
		}

		NBTTagList inventoryList = nbt.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < inventoryList.tagCount(); i++) {
			NBTTagCompound tag = inventoryList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < getSizeInventory()) {
				setInventorySlotContents(slot, new ItemStack(tag));
			}
		}
	}

	// -------------------- chunkloading stuff --------------------
	public void releaseTicket() {
		ForgeChunkManager.releaseTicket(chunkTicket);
		chunkTicket = null;
	}

	@Override
	public void invalidate() {
		super.invalidate();
		releaseTicket();
	}

	@Override
	public void setTicket(ForgeChunkManager.Ticket tk) {
		if (this.chunkTicket != tk) {
			releaseTicket();
			if (tk != null) {
				this.chunkTicket = tk;
				forceTicketChunks();
			}
		}
	}

	public void setupInitialTicket() {
		this.chunkTicket = ForgeChunkManager.requestTicket(MorphSpellPack.instance, world, ForgeChunkManager.Type.NORMAL);
		if (this.chunkTicket != null) {
			writeDataToTicket();
			forceTicketChunks();
		}
	}

	protected void writeDataToTicket() {
		MSChunkLoader.INSTANCE.writeDataToTicket(chunkTicket, pos);
	}

	protected void forceTicketChunks() {
		int cx = pos.getX() >> 4;
		int cz = pos.getZ() >> 4;
		for (int x = cx - 1; x <= cx + 1; x++) {
			for (int z = cz - 1; z <= cz + 1; z++) {
				ChunkPos chunkPos = new ChunkPos(x, z);
				ForgeChunkManager.forceChunk(this.chunkTicket, chunkPos);
			}
		}
		//TODO either uncomment and log chunk loading info or just remove this
		//  AWLog.logDebug("ticket now has chunks: "+tk.getChunkList());
		//  AWLog.logDebug("total forced chunks are: "+ForgeChunkManager.getPersistentChunksFor(world));
	}
}
