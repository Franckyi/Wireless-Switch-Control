package com.github.franckyi.wsc.capability.controllercap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.franckyi.wsc.util.MasterLogicalSwitch;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class ControllerStorage implements IStorage<IController> {

	@Override
	public void readNBT(Capability<IController> capability, IController instance, EnumFacing side, NBTBase nbt) {
		NBTTagList list = (NBTTagList) nbt;
		List<MasterLogicalSwitch> switches = new ArrayList<MasterLogicalSwitch>();
		for (Iterator<NBTBase> i = list.iterator(); i.hasNext();) {
			NBTTagCompound c = (NBTTagCompound) i.next();
			MasterLogicalSwitch mls = new MasterLogicalSwitch();
			mls.read(c);
			switches.add(mls);
		}

		instance.setSwitches(switches);

	}

	@Override
	public NBTBase writeNBT(Capability<IController> capability, IController instance, EnumFacing side) {
		NBTTagList nbt = new NBTTagList();
		for (MasterLogicalSwitch mls : instance.getSwitches())
			nbt.appendTag(mls.write());
		return nbt;
	}

}
