package com.github.franckyi.wsc.proxy;

import com.github.franckyi.wsc.WSCMod;
import com.github.franckyi.wsc.handlers.GuiHandler;
import com.github.franckyi.wsc.util.RegisterUtil;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

	public void init(FMLInitializationEvent e) {
		RegisterUtil.registerInit(e);
		NetworkRegistry.INSTANCE.registerGuiHandler(WSCMod.instance, new GuiHandler());
	}

	public void postInit(FMLPostInitializationEvent e) {
	}

	public void preInit(FMLPreInitializationEvent e) {
		RegisterUtil.registerPreInit(e);
	}

	public void serverStarting(FMLServerStartingEvent e) {
	}

	public void serverStopping(FMLServerStoppingEvent e) {
	}

}
