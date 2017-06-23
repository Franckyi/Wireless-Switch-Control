package com.github.franckyi.wsc.logic;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class MasterRedstoneSwitch extends BaseRedstoneSwitch {

	private BlockPos switchPos;

	public MasterRedstoneSwitch() {
	}

	public MasterRedstoneSwitch(BaseRedstoneSwitch ls, BlockPos pos) {
		super(ls.getName(), ls.isEnabled(), ls.getPower());
		this.switchPos = pos;
	}

	public MasterRedstoneSwitch(String name, boolean enabled, int power, BlockPos pos) {
		super(name, enabled, power);
		this.switchPos = pos;
	}

	public BlockPos getSwitchPos() {
		return switchPos;
	}

	@Override
	public void read(NBTTagCompound c) {
		super.read(c);
		setSwitchPos(new BlockPos(c.getInteger("x"), c.getInteger("y"), c.getInteger("z")));
	}

	public void setSwitchPos(BlockPos pos) {
		this.switchPos = pos;
	}

	@Override
	public NBTTagCompound write() {
		NBTTagCompound c = super.write();
		c.setInteger("x", getSwitchPos().getX());
		c.setInteger("y", getSwitchPos().getY());
		c.setInteger("z", getSwitchPos().getZ());
		return c;
	}

}
