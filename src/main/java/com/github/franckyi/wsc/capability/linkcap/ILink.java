package com.github.franckyi.wsc.capability.linkcap;

import com.github.franckyi.wsc.util.MasterLogicalSwitch;

public interface ILink {

	public void reset();

	public MasterLogicalSwitch getSwitch();

	public boolean isPresent();

	public void setSwitch(MasterLogicalSwitch mls);

}
