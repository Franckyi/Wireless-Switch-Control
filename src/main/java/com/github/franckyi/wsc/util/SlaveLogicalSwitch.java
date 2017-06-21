package com.github.franckyi.wsc.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;

public class SlaveLogicalSwitch extends BaseLogicalSwitch {

	private Set<BlockPos> controllers;

	public SlaveLogicalSwitch(boolean linked, String name, boolean enabled, int power, Set<BlockPos> controllers) {
		super(linked, name, enabled, power);
		this.controllers = controllers;
	}

	public SlaveLogicalSwitch(BaseLogicalSwitch ls, Set<BlockPos> controllers) {
		super(ls.isLinked(), ls.getName(), ls.isEnabled(), ls.getPower());
		this.controllers = controllers;
	}

	public SlaveLogicalSwitch() {
	}

	public Set<BlockPos> getControllers() {
		return controllers;
	}

	public void setControllers(Set<BlockPos> controllers) {
		this.controllers = controllers;
	}

	@Override
	public void read(NBTTagCompound c) {
		super.read(c);
		setControllers(new HashSet<BlockPos>());
		NBTTagList l = c.getTagList("controllers", 10);
		for (Iterator<NBTBase> i = l.iterator(); i.hasNext();) {
			NBTTagCompound c2 = (NBTTagCompound) i.next();
			controllers.add(new BlockPos(c2.getInteger("x"), c2.getInteger("y"), c2.getInteger("z")));
		}
	}

	@Override
	public NBTTagCompound write() {
		NBTTagCompound c = super.write();
		NBTTagList l = new NBTTagList();
		for (BlockPos pos : getControllers()) {
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
