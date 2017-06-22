package com.github.franckyi.wsc.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class MasterRedstoneSwitch extends BaseRedstoneSwitch {

	private BlockPos pos;

	public MasterRedstoneSwitch() {
	}

	public MasterRedstoneSwitch(BaseRedstoneSwitch ls, BlockPos pos) {
		super(ls.isLinked(), ls.getName(), ls.isEnabled(), ls.getPower());
		this.pos = pos;
	}

	public MasterRedstoneSwitch(boolean linked, String name, boolean enabled, int power, BlockPos pos) {
		super(linked, name, enabled, power);
		this.pos = pos;
	}

	public BlockPos getPos() {
		return pos;
	}

	@Override
	public void read(NBTTagCompound c) {
		super.read(c);
		setPos(new BlockPos(c.getInteger("x"), c.getInteger("y"), c.getInteger("z")));
	}

	public void setPos(BlockPos pos) {
		this.pos = pos;
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
