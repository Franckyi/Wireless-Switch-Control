package com.github.franckyi.wsc.logic;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class FullRedstoneController extends BaseRedstoneController {

	private BlockPos controllerPos;

	public FullRedstoneController() {
	}

	public FullRedstoneController(BaseRedstoneController controller, BlockPos controllerPos) {
		this(controller.getSwitches(), controller.getMaxSize(), controllerPos);
	}

	public FullRedstoneController(List<MasterRedstoneSwitch> switches, int maxSize, BlockPos controllerPos) {
		super(switches, maxSize);
		this.controllerPos = controllerPos;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		super.deserializeNBT(nbt);
		controllerPos = BlockPos.fromLong(nbt.getLong("pos"));
	}

	public BlockPos getControllerPos() {
		return controllerPos;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = super.serializeNBT();
		nbt.setLong("pos", controllerPos.toLong());
		return nbt;
	}

	public void setControllerPos(BlockPos controllerPos) {
		this.controllerPos = controllerPos;
	}

}
