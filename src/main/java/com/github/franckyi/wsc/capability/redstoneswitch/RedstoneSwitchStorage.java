package com.github.franckyi.wsc.capability.redstoneswitch;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class RedstoneSwitchStorage implements IStorage<IRedstoneSwitch> {

	@Override
	public void readNBT(Capability<IRedstoneSwitch> capability, IRedstoneSwitch instance, EnumFacing side,
			NBTBase nbt) {
		instance.getSwitch().deserializeNBT((NBTTagCompound) nbt);
	}

	@Override
	public NBTBase writeNBT(Capability<IRedstoneSwitch> capability, IRedstoneSwitch instance, EnumFacing side) {
		return instance.getSwitch().serializeNBT();
	}

}
