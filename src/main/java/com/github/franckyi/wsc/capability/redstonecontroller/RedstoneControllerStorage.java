package com.github.franckyi.wsc.capability.redstonecontroller;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class RedstoneControllerStorage implements IStorage<IRedstoneController> {

	@Override
	public void readNBT(Capability<IRedstoneController> capability, IRedstoneController instance, EnumFacing side,
			NBTBase nbt) {
		instance.getController().deserializeNBT((NBTTagCompound) nbt);
	}

	@Override
	public NBTBase writeNBT(Capability<IRedstoneController> capability, IRedstoneController instance, EnumFacing side) {
		return instance.getController().serializeNBT();
	}

}
