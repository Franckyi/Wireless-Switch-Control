package com.github.franckyi.wsc.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class MasterLogicalSwitch extends BaseLogicalSwitch {

	private BlockPos pos;

	public MasterLogicalSwitch(boolean linked, String name, boolean enabled, int power, BlockPos pos) {
		super(linked, name, enabled, power);
		this.pos = pos;
	}

	public MasterLogicalSwitch(BaseLogicalSwitch ls, BlockPos pos) {
		super(ls.isLinked(), ls.getName(), ls.isEnabled(), ls.getPower());
		this.pos = pos;
	}

	public MasterLogicalSwitch() {
	}

	public BlockPos getPos() {
		return pos;
	}

	public void setPos(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void read(NBTTagCompound c) {
		super.read(c);
		setPos(new BlockPos(c.getInteger("x"), c.getInteger("y"), c.getInteger("z")));
	}

	@Override
	public NBTTagCompound write() {
		NBTTagCompound c = super.write();
		c.setInteger("x", getPos().getX());
		c.setInteger("y", getPos().getY());
		c.setInteger("z", getPos().getZ());
		return c;
	}

}
