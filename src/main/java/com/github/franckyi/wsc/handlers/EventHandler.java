package com.github.franckyi.wsc.handlers;

import com.github.franckyi.wsc.capability.RedstoneCapabilities;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class EventHandler {

	@SubscribeEvent
	public void onPlayerLogsIn(PlayerLoggedInEvent event) {
		RedstoneCapabilities.getLink(event.player).reset();
	}

}
