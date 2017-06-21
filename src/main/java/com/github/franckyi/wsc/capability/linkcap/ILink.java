package com.github.franckyi.wsc.capability.linkcap;

import com.github.franckyi.wsc.util.MasterLogicalSwitch;

public interface ILink {

	public void clear();

	public void setSwitch(MasterLogicalSwitch mls);

	public MasterLogicalSwitch getSwitch();

	public boolean isPresent();

}
