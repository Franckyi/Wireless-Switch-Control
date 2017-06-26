package com.github.franckyi.wsc.logic;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.math.BlockPos;

public class SlaveRedstoneSwitch extends BaseRedstoneSwitch {

	private Set<BlockPos> controllerPos;

	public SlaveRedstoneSwitch() {
	}

	public SlaveRedstoneSwitch(BaseRedstoneSwitch ls, Set<BlockPos> controllers) {
		super(ls.getName(), ls.isEnabled(), ls.getPower());
		this.controllerPos = controllers;
	}

	public SlaveRedstoneSwitch(String name, boolean enabled, int power, Set<BlockPos> controllers) {
		super(name, enabled, power);
		this.controllerPos = controllers;
	}

	public Set<BlockPos> getControllerPos() {
		return controllerPos;
	}

	@Override
	public void deserializeNBT(NBTTagCompound c) {
		super.deserializeNBT(c);
		setControllerPos(new HashSet<BlockPos>());
		NBTTagList l = c.getTagList("controllers", 4);
		for (Iterator<NBTBase> i = l.iterator(); i.hasNext();)
			controllerPos.add(BlockPos.fromLong(((NBTTagLong) i.next()).getLong()));
	}

	public void setControllerPos(Set<BlockPos> controllers) {
		this.controllerPos = controllers;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound c = super.serializeNBT();
		NBTTagList l = new NBTTagList();
		for (BlockPos pos : getControllerPos())
			l.appendTag(new NBTTagLong(pos.toLong()));
		c.setTag("controllers", l);
		return c;
	}

}
