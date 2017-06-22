package com.github.franckyi.wsc.waila;

import com.github.franckyi.wsc.blocks.BlockRedstoneController;
import com.github.franckyi.wsc.blocks.BlockRedstoneSwitch;

import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class WSCWailaPlugin implements IWailaPlugin {

	@Override
	public void register(IWailaRegistrar registrar) {
		registrar.registerBodyProvider(new RedstoneSwitchDataProvider(), BlockRedstoneSwitch.class);
		registrar.registerBodyProvider(new RedstoneControllerDataProvider(), BlockRedstoneController.class);
	}

}
