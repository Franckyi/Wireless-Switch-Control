package com.github.franckyi.wsc.logic;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class MasterRedstoneSwitch extends BaseRedstoneSwitch {

	private BlockPos switchPos;

	public MasterRedstoneSwitch() {
	}

	public MasterRedstoneSwitch(BaseRedstoneSwitch ls, BlockPos pos) {
		this(ls.getName(), ls.isEnabled(), ls.getPower(), pos);
	}

	public MasterRedstoneSwitch(String name, boolean enabled, int power, BlockPos pos) {
		super(name, enabled, power);
		this.switchPos = pos;
	}

	@Override
	public void deserializeNBT(NBTTagCompound c) {
		super.deserializeNBT(c);
		setSwitchPos(BlockPos.fromLong(c.getLong("pos")));
	}

	public BlockPos getSwitchPos() {
		return switchPos;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound c = super.serializeNBT();
		c.setLong("pos", switchPos.toLong());
		return c;
	}

	public void setSwitchPos(BlockPos pos) {
		this.switchPos = pos;
	}

}
