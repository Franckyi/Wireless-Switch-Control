package com.github.franckyi.wsc.capability.redstoneswitch;

import com.github.franckyi.wsc.util.SlaveRedstoneSwitch;

public class RedstoneSwitchImpl implements IRedstoneSwitch {

	private SlaveRedstoneSwitch sls;

	@Override
	public SlaveRedstoneSwitch getSwitch() {
		return sls;
	}

	@Override
	public void setSwitch(SlaveRedstoneSwitch ls) {
		this.sls = ls;
	}

}
