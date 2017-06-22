package com.github.franckyi.wsc.capability.redstonecontroller;

import java.util.List;

import com.github.franckyi.wsc.util.MasterRedstoneSwitch;

public class RedstoneController implements IRedstoneController {

	private List<MasterRedstoneSwitch> switches;

	@Override
	public List<MasterRedstoneSwitch> getSwitches() {
		return switches;
	}

	@Override
	public void setSwitches(List<MasterRedstoneSwitch> switches) {
		this.switches = switches;
	}

}
