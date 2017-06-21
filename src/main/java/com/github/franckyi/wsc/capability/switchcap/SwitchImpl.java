package com.github.franckyi.wsc.capability.switchcap;

import com.github.franckyi.wsc.util.SlaveLogicalSwitch;

public class SwitchImpl implements ISwitch {

	private SlaveLogicalSwitch sls;

	@Override
	public SlaveLogicalSwitch getSwitch() {
		return sls;
	}

	@Override
	public void setSwitch(SlaveLogicalSwitch ls) {
		this.sls = ls;
	}

}
