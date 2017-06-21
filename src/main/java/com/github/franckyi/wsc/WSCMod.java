package com.github.franckyi.wsc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.franckyi.wsc.proxy.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModReference.MODID, name = ModReference.NAME, version = ModReference.VERSION, acceptedMinecraftVersions = ModReference.MCVERSIONS)
public class WSCMod {

	public static final Logger LOGGER = LogManager.getLogger(ModReference.MODID);

	@Instance
	public static WSCMod instance;

	@SidedProxy(clientSide = ModReference.CLIENTPROXY, serverSide = ModReference.COMMONPROXY)
	public static CommonProxy proxy;

	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);
	}

}
