package com.github.franckyi.wsc.handlers;

import com.github.franckyi.wsc.ModReference;
import com.github.franckyi.wsc.WSCMod;

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

	public static abstract class ClientHandler<REQ extends IMessage> extends CommonHandler<REQ> {

		@Override
		public IMessage onMessage(REQ message, MessageContext ctx) {
			super.onMessage(message, ctx);
			WSCMod.proxy.clientHandler(this, ctx);
			return null;
		}

	}

	public static abstract class CommonHandler<REQ extends IMessage>
			implements IMessageHandler<REQ, IMessage>, Runnable {

		public World world;
		public IThreadListener mainThread;
		protected REQ message;
		protected MessageContext ctx;

		@Override
		public IMessage onMessage(REQ message, MessageContext ctx) {
			this.message = message;
			this.ctx = ctx;
			return null;
		}

	}

	public static abstract class ServerHandler<REQ extends IMessage> extends CommonHandler<REQ> {

		@Override
		public IMessage onMessage(REQ message, MessageContext ctx) {
			super.onMessage(message, ctx);
			WSCMod.proxy.serverHandler(this, ctx);
			return null;
		}

	}

	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModReference.MODID);

}
