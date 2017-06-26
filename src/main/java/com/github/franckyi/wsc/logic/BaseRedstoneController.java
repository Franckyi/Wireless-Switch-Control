package com.github.franckyi.wsc.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;

public class BaseRedstoneController implements INBTSerializable<NBTTagCompound> {
	
	private List<MasterRedstoneSwitch> switches;
	private int maxSize;

	public BaseRedstoneController(List<MasterRedstoneSwitch> switches, int maxSize) {
		this.switches = switches;
		this.maxSize = maxSize;
	}

	public BaseRedstoneController() {
	}

	public List<MasterRedstoneSwitch> getSwitches() {
		return switches;
	}

	public void setSwitches(List<MasterRedstoneSwitch> switches) {
		this.switches = switches;
	}
	
	public int getMaxSize() {
		return maxSize;
	}
	
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		for (MasterRedstoneSwitch s : switches)
			list.appendTag(s.serializeNBT());
		nbt.setTag("switches", list);
		nbt.setInteger("maxSize", maxSize);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		NBTTagList list = nbt.getTagList("switches", 10);
		switches = new ArrayList<MasterRedstoneSwitch>();
		for (Iterator<NBTBase> i = list.iterator(); i.hasNext();) {
			NBTTagCompound c = (NBTTagCompound) i.next();
			MasterRedstoneSwitch s = new MasterRedstoneSwitch();
			s.deserializeNBT(c);
			switches.add(s);
		}
		maxSize = nbt.getInteger("maxSize");
	}

	public void set(List<MasterRedstoneSwitch> switches, int maxSize) {
		setSwitches(switches);
		setMaxSize(maxSize);
	}
	
	

}
