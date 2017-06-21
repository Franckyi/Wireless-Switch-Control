package com.github.franckyi.wsc.tileentity;

import java.util.HashSet;

import com.github.franckyi.wsc.capability.switchcap.ISwitch;
import com.github.franckyi.wsc.capability.switchcap.SwitchProvider;
import com.github.franckyi.wsc.util.SlaveLogicalSwitch;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntitySwitch extends TileEntity {

	public TileEntitySwitch() {
		setSwitch(new SlaveLogicalSwitch(false, "default", false, 0, new HashSet<BlockPos>()));
		markDirty();
	}

	public SlaveLogicalSwitch getSwitch() {
		ISwitch s = getCapability(SwitchProvider.SWITCH_CAP, null);
		if (s != null)
			return s.getSwitch();
		return null;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		if (world != null)
			world.notifyNeighborsOfStateChange(pos, blockType, false);
	}

	public void setSwitch(SlaveLogicalSwitch sls) {
		ISwitch s = getCapability(SwitchProvider.SWITCH_CAP, null);
		if (s != null)
			s.setSwitch(sls);
	}

}
