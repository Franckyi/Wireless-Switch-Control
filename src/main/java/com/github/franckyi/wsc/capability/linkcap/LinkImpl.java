package com.github.franckyi.wsc.capability.linkcap;

import com.github.franckyi.wsc.util.MasterLogicalSwitch;

public class LinkImpl implements ILink {

	private MasterLogicalSwitch mls;

	@Override
	public void clear() {
		mls = null;
	}

	@Override
	public MasterLogicalSwitch getSwitch() {
		return mls;
	}

	@Override
	public boolean isPresent() {
		return mls != null;
	}

	@Override
	public void setSwitch(MasterLogicalSwitch mls) {
		this.mls = mls;
	}

}
