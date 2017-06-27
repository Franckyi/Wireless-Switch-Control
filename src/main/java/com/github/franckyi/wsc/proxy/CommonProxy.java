package com.github.franckyi.wsc.proxy;

import com.github.franckyi.wsc.handlers.PacketHandler.ClientHandler;
import com.github.franckyi.wsc.handlers.PacketHandler.ServerHandler;
import com.github.franckyi.wsc.util.RegisterUtil;

import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy implements IProxy {

	@Override
	public void clientHandler(ClientHandler<? extends IMessage> clientHandler, MessageContext ctx) {
	}

	@Override
	public void init(FMLInitializationEvent event) {
		RegisterUtil.registerRecipes();
		RegisterUtil.registerEventHandlers();
		RegisterUtil.registerMessages();
		RegisterUtil.registerGuiHandler();
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		RegisterUtil.registerBlocks();
		RegisterUtil.registerItems();
		RegisterUtil.registerTileEntities();
		RegisterUtil.registerCapabilities();
	}

	@Override
	public void serverHandler(ServerHandler<? extends IMessage> serverHandler, MessageContext ctx) {
		serverHandler.world = ctx.getServerHandler().player.world;
		serverHandler.mainThread = (WorldServer) serverHandler.world;
		serverHandler.mainThread.addScheduledTask(serverHandler);
	}

}
