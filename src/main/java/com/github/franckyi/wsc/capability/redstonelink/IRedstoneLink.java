package com.github.franckyi.wsc.capability.redstonelink;

import com.github.franckyi.wsc.logic.MasterRedstoneSwitch;

public interface IRedstoneLink {

	public MasterRedstoneSwitch getSwitch();

	public boolean isPresent();

	public void reset();

	public void setSwitch(MasterRedstoneSwitch mls);

}
