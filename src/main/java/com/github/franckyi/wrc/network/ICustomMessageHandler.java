package com.github.franckyi.wrc.network;

import com.github.franckyi.wrc.WirelessRedstoneControl;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface ICustomMessageHandler<REQ extends IMessage> extends IMessageHandler<REQ, IMessage> {
	
	@Override
	default IMessage onMessage(REQ message, MessageContext ctx) {
		FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
			onMessage0(message, ctx);
		});
		return null;
	}

	public void onMessage0(REQ message, MessageContext ctx);

}
