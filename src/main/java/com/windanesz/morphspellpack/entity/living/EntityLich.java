package com.windanesz.morphspellpack.entity.living;

import net.minecraft.entity.monster.EntityStray;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityLich extends EntityStray {

	public EntityLich(World worldIn) {
		super(worldIn);
	}

	@Override
	public boolean isSwingingArms() {
		return false;
	}

	public void setSwingingArms(boolean swingingArms)
	{
		//this.dataManager.set(SWINGING_ARMS, Boolean.valueOf(swingingArms));
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (this.world.isDaytime() && !this.world.isRemote && !this.isChild())
		{
			float f = this.getBrightness();

			if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.canSeeSky(new BlockPos(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ)))
			{
				boolean flag = true;
				ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

				if (!itemstack.isEmpty())
				{
					if (itemstack.isItemStackDamageable())
					{
						itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));

						if (itemstack.getItemDamage() >= itemstack.getMaxDamage())
						{
							this.renderBrokenItemStack(itemstack);
							this.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
						}
					}

					flag = false;
				}

				if (flag)
				{
					this.setFire(8);
				}
			}
		}

	}
}
