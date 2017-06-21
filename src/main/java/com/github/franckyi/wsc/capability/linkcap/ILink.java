package com.github.franckyi.wsc.capability.linkcap;

import com.github.franckyi.wsc.util.MasterLogicalSwitch;

public interface ILink {

	public void clear();

	public MasterLogicalSwitch getSwitch();

	public boolean isPresent();

	public void setSwitch(MasterLogicalSwitch mls);

}
