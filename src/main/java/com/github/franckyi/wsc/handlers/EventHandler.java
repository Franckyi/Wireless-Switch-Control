package com.github.franckyi.wsc.handlers;

import com.github.franckyi.wsc.capability.linkcap.ILink;
import com.github.franckyi.wsc.capability.linkcap.LinkProvider;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class EventHandler {

	@SubscribeEvent
	public void onPlayerLogsIn(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		ILink link = player.getCapability(LinkProvider.LINK_CAP, null);
		link.reset();
	}

}
