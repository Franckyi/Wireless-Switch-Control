package com.github.franckyi.wsc.capability.switchcap;

import com.github.franckyi.wsc.util.SlaveLogicalSwitch;

public interface ISwitch {

	public SlaveLogicalSwitch getSwitch();

	public void setSwitch(SlaveLogicalSwitch ls);

}
