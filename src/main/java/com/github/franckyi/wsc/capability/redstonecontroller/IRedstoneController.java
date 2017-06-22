package com.github.franckyi.wsc.capability.redstonecontroller;

import java.util.List;

import com.github.franckyi.wsc.util.MasterRedstoneSwitch;

public interface IRedstoneController {

	public List<MasterRedstoneSwitch> getSwitches();

	public void setSwitches(List<MasterRedstoneSwitch> switches);

}
