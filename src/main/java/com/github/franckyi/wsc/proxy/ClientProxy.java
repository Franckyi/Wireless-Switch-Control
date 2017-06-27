package com.github.franckyi.wsc.proxy;

import com.github.franckyi.wsc.handlers.PacketHandler.ClientHandler;
import com.github.franckyi.wsc.util.RegisterUtil;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		RegisterUtil.registerRenders();
	}

	@Override
	public void clientHandler(ClientHandler<? extends IMessage> clientHandler, MessageContext ctx) {
		clientHandler.world = Minecraft.getMinecraft().world;
		clientHandler.mainThread = Minecraft.getMinecraft();
		clientHandler.mainThread.addScheduledTask(clientHandler);
	}

	
	
}
