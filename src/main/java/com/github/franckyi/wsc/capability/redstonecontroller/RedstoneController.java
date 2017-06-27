package com.github.franckyi.wsc.capability.redstonecontroller;

import com.github.franckyi.wsc.logic.BaseRedstoneController;

public class RedstoneController implements IRedstoneController {

	private BaseRedstoneController controller;

	@Override
	public BaseRedstoneController getController() {
		return controller;
	}

	@Override
	public void setController(BaseRedstoneController controller) {
		this.controller = controller;
	}

}
