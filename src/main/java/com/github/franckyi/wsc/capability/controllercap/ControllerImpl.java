package com.github.franckyi.wsc.capability.controllercap;

import java.util.List;

import com.github.franckyi.wsc.util.MasterLogicalSwitch;

public class ControllerImpl implements IController {

	private List<MasterLogicalSwitch> switches;

	@Override
	public List<MasterLogicalSwitch> getSwitches() {
		return switches;
	}

	@Override
	public void setSwitches(List<MasterLogicalSwitch> switches) {
		this.switches = switches;
	}

}
