package com.github.franckyi.wsc.tileentity;

import java.util.HashSet;

import com.github.franckyi.wsc.capability.redstoneswitch.IRedstoneSwitch;
import com.github.franckyi.wsc.capability.redstoneswitch.RedstoneSwitchProvider;
import com.github.franckyi.wsc.util.SlaveRedstoneSwitch;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityRedstoneSwitch extends TileEntity {

	public TileEntityRedstoneSwitch() {
		setSwitch(new SlaveRedstoneSwitch(false, "default", false, 0, new HashSet<BlockPos>()));
		markDirty();
	}

	public SlaveRedstoneSwitch getSwitch() {
		IRedstoneSwitch s = getCapability(RedstoneSwitchProvider.SWITCH_CAP, null);
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

	public void setSwitch(SlaveRedstoneSwitch sls) {
		IRedstoneSwitch s = getCapability(RedstoneSwitchProvider.SWITCH_CAP, null);
		if (s != null)
			s.setSwitch(sls);
	}

}
