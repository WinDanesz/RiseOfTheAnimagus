package com.windanesz.morphspellpack.entity.living;

import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;

public class EntityDisguise extends EntityLiving {

	private static final DataParameter<String> STACK = EntityDataManager.<String>createKey(EntityDisguise.class, DataSerializers.STRING);

	public int ticksToRender = 20;
	public ItemStack stack = new ItemStack(Item.getItemFromBlock(Blocks.PUMPKIN));

	public EntityDisguise(World worldIn) {
		super(worldIn);
	}

	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(STACK, "minecraft:pumpkin");
	}

	public String getStack() {

		return (this.dataManager.get(STACK));
	}

	public void setStack(String string) {
		this.dataManager.set(STACK, string);

	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (!stack.getItem().getRegistryName().toString().equals(getStack())) {
			stack = new ItemStack(Item.getItemFromBlock(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(getStack()))));
		}

		if (ticksToRender > 0) {
			ticksToRender--;
			if (world.isRemote) {
				for (int i = 0; i < 2; i++) {
					float brightness = rand.nextFloat() * 0.1f + 0.1f;
					ParticleBuilder.create(ParticleBuilder.Type.CLOUD, rand, posX, posY, posZ, 1.2F, false)
							.clr(brightness, brightness, brightness).time(20 + this.rand.nextInt(12)).shaded(true).spawn(world);
				}
			}
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		NBTTagCompound serialized = new NBTTagCompound();
		stack.writeToNBT(serialized);
		compound.setTag("itemStack", serialized);
		super.writeEntityToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("itemStack")) {
			this.stack = new ItemStack(compound.getCompoundTag("itemStack"));
		}
		super.readFromNBT(compound);
	}

	@Nullable
	@Override
	protected ResourceLocation getLootTable() {
		return null;
	}

	@Override
	protected int getExperiencePoints(EntityPlayer player) {
		return 0;
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		// these little fellas have a bit more hp than usual
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(9.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.00000001192092896D);
	}
}
