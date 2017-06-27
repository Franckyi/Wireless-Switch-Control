package com.github.franckyi.wsc.tileentity;

import java.util.ArrayList;

import com.github.franckyi.wsc.capability.redstonecontroller.IRedstoneController;
import com.github.franckyi.wsc.capability.redstonecontroller.RedstoneControllerProvider;
import com.github.franckyi.wsc.logic.BaseRedstoneController;
import com.github.franckyi.wsc.logic.MasterRedstoneSwitch;

import net.minecraft.tileentity.TileEntity;

public class TileEntityRedstoneController extends TileEntity {

	public TileEntityRedstoneController() {
		setController(new BaseRedstoneController(new ArrayList<MasterRedstoneSwitch>(), 4));
		markDirty();

	}

	public BaseRedstoneController getController() {
		IRedstoneController c = getCapability(RedstoneControllerProvider.CONTROLLER_CAP, null);
		if (c != null)
			return c.getController();
		return null;
	}

	public void setController(BaseRedstoneController controller) {
		IRedstoneController c = getCapability(RedstoneControllerProvider.CONTROLLER_CAP, null);
		if (c != null)
			c.setController(controller);

	}

}
