package com.github.franckyi.wsc.proxy;

import com.github.franckyi.wsc.handlers.PacketHandler.ClientHandler;
import com.github.franckyi.wsc.handlers.PacketHandler.ServerHandler;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface IProxy {
	
	public void preInit(FMLPreInitializationEvent event);
	
	public void init(FMLInitializationEvent event);
	
	public void clientHandler(ClientHandler<? extends IMessage> clientHandler, MessageContext ctx);

	public void serverHandler(ServerHandler<? extends IMessage> serverHandler, MessageContext ctx);

}
