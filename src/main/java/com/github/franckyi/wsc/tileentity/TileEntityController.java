package com.github.franckyi.wsc.tileentity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.github.franckyi.wsc.WSCMod;
import com.github.franckyi.wsc.capability.controllercap.ControllerProvider;
import com.github.franckyi.wsc.capability.controllercap.IController;
import com.github.franckyi.wsc.util.MasterLogicalSwitch;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityController extends TileEntity {

	public TileEntityController() {
		WSCMod.LOGGER.info("controller");
		WSCMod.LOGGER.info(getCapability(ControllerProvider.CONTROLLER_CAP, null) != null);
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
