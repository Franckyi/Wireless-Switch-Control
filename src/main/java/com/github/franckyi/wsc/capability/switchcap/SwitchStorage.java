package com.github.franckyi.wsc.capability.switchcap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class SwitchStorage implements IStorage<ISwitch> {

	@Override
	public NBTBase writeNBT(Capability<ISwitch> capability, ISwitch instance, EnumFacing side) {
		return instance.getSwitch().write();
	}

	@Override
	public void readNBT(Capability<ISwitch> capability, ISwitch instance, EnumFacing side, NBTBase nbt) {
		instance.getSwitch().read((NBTTagCompound) nbt);
	}

}
