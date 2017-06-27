package com.github.franckyi.wsc.capability.redstonelink;

import com.github.franckyi.wsc.logic.MasterRedstoneSwitch;

public class RedstoneLinkImpl implements IRedstoneLink {

	private MasterRedstoneSwitch mls;

	@Override
	public MasterRedstoneSwitch getSwitch() {
		return mls;
	}

	@Override
	public boolean isPresent() {
		return mls != null;
	}

	@Override
	public void reset() {
		mls = null;
	}

	@Override
	public void setSwitch(MasterRedstoneSwitch mls) {
		this.mls = mls;
	}

}
