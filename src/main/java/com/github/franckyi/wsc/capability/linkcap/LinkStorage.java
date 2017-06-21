package com.github.franckyi.wsc.capability.linkcap;

import com.github.franckyi.wsc.util.MasterLogicalSwitch;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class LinkStorage implements IStorage<ILink> {

	@Override
	public void readNBT(Capability<ILink> capability, ILink instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound c = (NBTTagCompound) nbt;
		if (c.getBoolean("present")) {
			MasterLogicalSwitch mls = new MasterLogicalSwitch();
			mls.read(c);
			instance.setSwitch(mls);
		} else
			instance.clear();
	}

	@Override
	public NBTBase writeNBT(Capability<ILink> capability, ILink instance, EnumFacing side) {
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
