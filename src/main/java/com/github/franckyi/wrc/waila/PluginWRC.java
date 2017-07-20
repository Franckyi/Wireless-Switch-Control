package com.github.franckyi.wrc.waila;

import com.github.franckyi.wrc.api.BlockBasicDevice;
import com.github.franckyi.wrc.block.BlockController;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class PluginWRC implements IWailaPlugin {

    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new BasicDeviceDataProvider(), BlockBasicDevice.class);
        registrar.registerBodyProvider(new ControllerDataProvider(), BlockController.class);
    }

}
