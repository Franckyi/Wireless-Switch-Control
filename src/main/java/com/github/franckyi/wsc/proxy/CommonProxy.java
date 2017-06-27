package com.github.franckyi.wsc.proxy;

import com.github.franckyi.wsc.util.RegisterUtil;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy implements IProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		RegisterUtil.registerBlocks();
		RegisterUtil.registerItems();
		RegisterUtil.registerTileEntities();
		RegisterUtil.registerCapabilities();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		RegisterUtil.registerRecipes();
		RegisterUtil.registerEventHandlers();
		RegisterUtil.registerMessages();
	}

}
