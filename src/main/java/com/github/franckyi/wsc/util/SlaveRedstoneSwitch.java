package com.github.franckyi.wsc.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;

public class SlaveRedstoneSwitch extends BaseRedstoneSwitch {

	private Set<BlockPos> controllerPos;

	public SlaveRedstoneSwitch() {
	}

	public SlaveRedstoneSwitch(BaseRedstoneSwitch ls, Set<BlockPos> controllers) {
		super(ls.isLinked(), ls.getName(), ls.isEnabled(), ls.getPower());
		this.controllerPos = controllers;
	}

	public SlaveRedstoneSwitch(boolean linked, String name, boolean enabled, int power, Set<BlockPos> controllers) {
		super(linked, name, enabled, power);
		this.controllerPos = controllers;
	}

	public Set<BlockPos> getControllerPos() {
		return controllerPos;
	}

	@Override
	public void read(NBTTagCompound c) {
		super.read(c);
		setControllerPos(new HashSet<BlockPos>());
		NBTTagList l = c.getTagList("controllers", 10);
		for (Iterator<NBTBase> i = l.iterator(); i.hasNext();) {
			NBTTagCompound c2 = (NBTTagCompound) i.next();
			controllerPos.add(new BlockPos(c2.getInteger("x"), c2.getInteger("y"), c2.getInteger("z")));
		}
	}

	public void setControllerPos(Set<BlockPos> controllers) {
		this.controllerPos = controllers;
	}

	@Override
	public NBTTagCompound write() {
		NBTTagCompound c = super.write();
		NBTTagList l = new NBTTagList();
		for (BlockPos pos : getControllerPos()) {
			NBTTagCompound c2 = new NBTTagCompound();
			c2.setInteger("x", pos.getX());
			c2.setInteger("y", pos.getY());
			c2.setInteger("z", pos.getZ());
			l.appendTag(c2);
		}
		c.setTag("controllers", l);
		return c;
	}

}
