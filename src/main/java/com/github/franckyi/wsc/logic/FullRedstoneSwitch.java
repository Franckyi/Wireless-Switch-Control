package com.github.franckyi.wsc.logic;

import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class FullRedstoneSwitch extends SlaveRedstoneSwitch {

	private BlockPos switchPos;

	public FullRedstoneSwitch() {
	}

	public FullRedstoneSwitch(BaseRedstoneSwitch s, Set<BlockPos> controllerPos, BlockPos switchPos) {
		this(s.getName(), s.isEnabled(), s.getPower(), controllerPos, switchPos);
	}

	public FullRedstoneSwitch(MasterRedstoneSwitch s, Set<BlockPos> controllerPos) {
		this(s.getName(), s.isEnabled(), s.getPower(), controllerPos, s.getSwitchPos());
	}

	public FullRedstoneSwitch(SlaveRedstoneSwitch s, BlockPos switchPos) {
		this(s.getName(), s.isEnabled(), s.getPower(), s.getControllerPos(), switchPos);
	}

	public FullRedstoneSwitch(String name, boolean enabled, int power, Set<BlockPos> controllerPos,
			BlockPos switchPos) {
		super(name, enabled, power, controllerPos);
		this.switchPos = switchPos;
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

	public void setSwitchPos(BlockPos switchPos) {
		this.switchPos = switchPos;
	}

}
