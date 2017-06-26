package com.github.franckyi.wsc.handlers;

import com.github.franckyi.wsc.ModReference;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class PacketHandler {

	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModReference.MODID);
	
	public static abstract class CommonHandler<REQ extends IMessage, REPLY extends IMessage> implements IMessageHandler<REQ, REPLY>, Runnable {
		
		protected World world;
		protected IThreadListener mainThread;
		protected REQ message;
		protected REPLY reply;
		protected MessageContext ctx;

		@Override
		public REPLY onMessage(REQ message, MessageContext ctx) {
			this.message = message;
			this.ctx = ctx;
			return reply;
		}
		
	}
	
	public static abstract class ServerHandler<REQ extends IMessage> extends CommonHandler<REQ, IMessage> {

		@Override
		public IMessage onMessage(REQ message, MessageContext ctx) {
			super.onMessage(message, ctx);
			this.world = ctx.getServerHandler().player.world;
			this.mainThread = (WorldServer) this.world;
			this.mainThread.addScheduledTask(this);
			return null;
		}
		
	}
	
	public static abstract class ClientHandler<REQ extends IMessage> extends CommonHandler<REQ, IMessage> {

		@Override
		public IMessage onMessage(REQ message, MessageContext ctx) {
			super.onMessage(message, ctx);
			this.world = Minecraft.getMinecraft().world;
			this.mainThread = Minecraft.getMinecraft();
			this.mainThread.addScheduledTask(this);
			return null;
		}
		
	}
	
	

}
