package com.github.franckyi.wsc.capability.redstonecontroller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.franckyi.wsc.logic.BaseRedstoneController;
import com.github.franckyi.wsc.logic.MasterRedstoneSwitch;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
