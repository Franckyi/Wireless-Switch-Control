package com.github.franckyi.wsc.handlers;

import com.github.franckyi.wsc.capability.RedstoneCapabilities;
import com.github.franckyi.wsc.capability.redstonecontroller.RedstoneControllerProvider;
import com.github.franckyi.wsc.capability.redstoneswitch.RedstoneSwitchProvider;
import com.github.franckyi.wsc.network.UpdateRedstoneControllerMessage;
import com.github.franckyi.wsc.network.UpdateRedstoneSwitchMessage;
import com.github.franckyi.wsc.tileentity.TileEntityRedstoneController;
import com.github.franckyi.wsc.tileentity.TileEntityRedstoneSwitch;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class EventHandler {

	@SubscribeEvent
	public void onPlayerLogsIn(PlayerLoggedInEvent event) {
		RedstoneCapabilities.getLink(event.player).reset();
	}

	@SubscribeEvent
	public void onChunkWatch(ChunkWatchEvent.Watch event) {
		WorldServer world = event.getPlayer().getServerWorld();
		if (!world.isRemote) {
			for (TileEntity te : world.loadedTileEntityList) {
				if (event.getChunk().equals(world.getChunkFromBlockCoords(te.getPos()).getPos())) {
					if (te instanceof TileEntityRedstoneSwitch) {
						PacketHandler.INSTANCE.sendTo(new UpdateRedstoneSwitchMessage(te.getPos(),
								te.getCapability(RedstoneSwitchProvider.SWITCH_CAP, null).getSwitch()), event.getPlayer());
					} else if (te instanceof TileEntityRedstoneController) {
						PacketHandler.INSTANCE.sendTo(new UpdateRedstoneControllerMessage(te.getPos(),
								te.getCapability(RedstoneControllerProvider.CONTROLLER_CAP, null).getController()), event.getPlayer());
					}
				}
			}
		}
	}

}
