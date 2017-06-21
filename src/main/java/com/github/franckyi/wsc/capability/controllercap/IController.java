package com.github.franckyi.wsc.capability.controllercap;

import java.util.List;

import com.github.franckyi.wsc.util.MasterLogicalSwitch;

public interface IController {
	
	public List<MasterLogicalSwitch> getSwitches();
	public void setSwitches(List<MasterLogicalSwitch> switches);

}
