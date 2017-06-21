package com.github.franckyi.wsc.tileentity;

import java.util.ArrayList;
import java.util.List;

import com.github.franckyi.wsc.capability.controllercap.ControllerProvider;
import com.github.franckyi.wsc.capability.controllercap.IController;
import com.github.franckyi.wsc.util.MasterLogicalSwitch;

import net.minecraft.tileentity.TileEntity;

public class TileEntityController extends TileEntity {

	public TileEntityController() {
		setSwitches(new ArrayList<MasterLogicalSwitch>());
		markDirty();

	}

	public List<MasterLogicalSwitch> getSwitches() {
		IController c = getCapability(ControllerProvider.CONTROLLER_CAP, null);
		if (c != null)
			return c.getSwitches();
		return null;
	}

	public void setSwitches(List<MasterLogicalSwitch> switches) {
		IController c = getCapability(ControllerProvider.CONTROLLER_CAP, null);
		if (c != null)
			c.setSwitches(switches);

	}

}
