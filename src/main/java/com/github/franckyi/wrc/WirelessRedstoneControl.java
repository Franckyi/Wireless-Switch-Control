package com.github.franckyi.wrc;

import com.github.franckyi.wrc.item.ModCreativeTab;
import com.github.franckyi.wrc.proxy.IProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.Logger;

@Mod(modid = WirelessRedstoneControl.MODID, name = WirelessRedstoneControl.NAME, version = WirelessRedstoneControl.VERSION, acceptedMinecraftVersions = WirelessRedstoneControl.MCVERSION)
public class WirelessRedstoneControl {

    public static final String MODID = "wrc";
    public static final String NAME = "Wireless Redstone Control";
    public static final String VERSION = "0.2.0";
    public static final String MCVERSION = "[1.12]";

    public static final String CLIENTPROXY = "com.github.franckyi.wrc.proxy.ClientProxy";
    public static final String SERVERPROXY = "com.github.franckyi.wrc.proxy.ServerProxy";

    @Mod.Instance
    public static WirelessRedstoneControl instance;

    @SidedProxy(clientSide = CLIENTPROXY, serverSide = SERVERPROXY)
    public static IProxy proxy;

    public static Logger logger;
    public static final SimpleNetworkWrapper netWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
    public static final CreativeTabs creativeTab = new ModCreativeTab();

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

}
