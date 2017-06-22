package com.github.franckyi.wsc.capability.redstonelink;

import com.github.franckyi.wsc.util.MasterRedstoneSwitch;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class RedstoneLinkStorage implements IStorage<IRedstoneLink> {

	@Override
	public void readNBT(Capability<IRedstoneLink> capability, IRedstoneLink instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound c = (NBTTagCompound) nbt;
		if (c.getBoolean("present")) {
			MasterRedstoneSwitch mls = new MasterRedstoneSwitch();
			mls.read(c);
			instance.setSwitch(mls);
		} else
			instance.reset();
	}

	@Override
	public NBTBase writeNBT(Capability<IRedstoneLink> capability, IRedstoneLink instance, EnumFacing side) {
		NBTTagCompound c;
		if (instance.isPresent()) {
			c = instance.getSwitch().write();
			c.setBoolean("present", true);
		} else
			c = new NBTTagCompound();
		c.setBoolean("present", false);
		return c;
	}

}
