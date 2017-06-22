package com.github.franckyi.wsc.tileentity;

import java.util.ArrayList;
import java.util.List;

import com.github.franckyi.wsc.capability.redstonecontroller.IRedstoneController;
import com.github.franckyi.wsc.capability.redstonecontroller.RedstoneControllerProvider;
import com.github.franckyi.wsc.util.MasterRedstoneSwitch;

import net.minecraft.tileentity.TileEntity;

public class TileEntityRedstoneController extends TileEntity {

	public TileEntityRedstoneController() {
		setSwitches(new ArrayList<MasterRedstoneSwitch>());
		markDirty();

	}

	public List<MasterRedstoneSwitch> getSwitches() {
		IRedstoneController c = getCapability(RedstoneControllerProvider.CONTROLLER_CAP, null);
		if (c != null)
			return c.getSwitches();
		return null;
	}

	public void setSwitches(List<MasterRedstoneSwitch> switches) {
		IRedstoneController c = getCapability(RedstoneControllerProvider.CONTROLLER_CAP, null);
		if (c != null)
			c.setSwitches(switches);

	}

}
