package com.github.franckyi.wsc.capability.linkcap;

import com.github.franckyi.wsc.util.MasterLogicalSwitch;
import com.google.common.base.Optional;

import net.minecraft.util.math.BlockPos;

public class LinkImpl implements ILink {
	
	private MasterLogicalSwitch mls;

	@Override
	public void clear() {
		mls = null;
	}


	@Override
	public void setSwitch(MasterLogicalSwitch mls) {
		this.mls = mls;
	}


	@Override
	public MasterLogicalSwitch getSwitch() {
		return mls;
	}

	@Override
	public boolean isPresent() {
		return mls != null;
	}


}
