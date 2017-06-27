package com.github.franckyi.wsc.proxy;

import com.github.franckyi.wsc.util.RegisterUtil;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		RegisterUtil.registerRenders();
	}

}
