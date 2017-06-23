package com.github.franckyi.wsc.logic;

import java.util.List;

public class BaseRedstoneController {
	
	private List<MasterRedstoneSwitch> switches;

	public BaseRedstoneController(List<MasterRedstoneSwitch> switches) {
		this.switches = switches;
	}

	public List<MasterRedstoneSwitch> getSwitches() {
		return switches;
	}

	public void setSwitches(List<MasterRedstoneSwitch> switches) {
		this.switches = switches;
	}
	
	

}
