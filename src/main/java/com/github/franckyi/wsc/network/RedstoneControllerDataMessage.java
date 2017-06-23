package com.github.franckyi.wsc.network;

import java.util.ArrayList;
import java.util.List;

import com.github.franckyi.wsc.WSCMod;
import com.github.franckyi.wsc.capability.RedstoneCapabilities;
import com.github.franckyi.wsc.handlers.GuiHandler;
import com.github.franckyi.wsc.handlers.PacketHandler;
import com.github.franckyi.wsc.logic.MasterRedstoneSwitch;
import com.github.franckyi.wsc.logic.SlaveRedstoneSwitch;
import com.google.common.base.Optional;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class RedstoneControllerDataMessage implements IMessage {

	public static class ControllerDataMessageHandler
			implements IMessageHandler<RedstoneControllerDataMessage, IMessage> {

		@Override
		public IMessage onMessage(final RedstoneControllerDataMessage message, MessageContext ctx) {
			if (message.source.equals(Side.SERVER)) {
				final World world = Minecraft.getMinecraft().world;
				IThreadListener mainThread = Minecraft.getMinecraft();
				mainThread.addScheduledTask(new Runnable() {
					@Override
					public void run() {
						RedstoneCapabilities.setControllerSwitches(world, message.controllerPos, message.switches);
						if (message.openGui)
							Minecraft.getMinecraft().player.openGui(WSCMod.instance, GuiHandler.REDSTONE_CONTROLLER_GUI,
									Minecraft.getMinecraft().world, message.controllerPos.getX(),
									message.controllerPos.getY(), message.controllerPos.getZ());
					}
				});
			} else {
				final EntityPlayerMP p = ctx.getServerHandler().player;
				IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.world;
				mainThread.addScheduledTask(new Runnable() {
					@Override
					public void run() {
						RedstoneCapabilities.setControllerSwitches(p.world, message.controllerPos, message.switches);
						for (MasterRedstoneSwitch updatedControllerSwitch : RedstoneCapabilities
								.getControllerSwitches(p.world, message.controllerPos)) {
							RedstoneCapabilities.updateSwitch(p.world, updatedControllerSwitch.getSwitchPos(),
									updatedControllerSwitch);
							Optional<SlaveRedstoneSwitch> fromServer = RedstoneCapabilities.getSwitch(p.world,
									updatedControllerSwitch.getSwitchPos());
							if (fromServer.isPresent())
								PacketHandler.INSTANCE.sendToAll(new RedstoneSwitchDataMessage(Side.SERVER,
										fromServer.get(), updatedControllerSwitch.getSwitchPos(), false));
							Optional<SlaveRedstoneSwitch> updatedSwitchBlock = RedstoneCapabilities.getSwitch(p.world,
									updatedControllerSwitch.getSwitchPos());
							if (updatedSwitchBlock.isPresent()) {
								for (BlockPos pos : updatedSwitchBlock.get().getControllerPos()) {
									List<MasterRedstoneSwitch> ss = RedstoneCapabilities.getControllerSwitches(p.world,
											pos);
									for (MasterRedstoneSwitch oldControllerSwitch : RedstoneCapabilities
											.getControllerSwitches(p.world, pos)) {
										if (oldControllerSwitch.getSwitchPos()
												.equals(updatedControllerSwitch.getSwitchPos())) {
											ss.set(ss.indexOf(oldControllerSwitch), updatedControllerSwitch);
											RedstoneCapabilities.updateTileEntity(p.world,
													updatedControllerSwitch.getSwitchPos());
											break;
										}
									}
								}
							}
						}
						RedstoneCapabilities.getLink(p).reset();
					}
				});
			}
			return null;
		}

	}

	private Side source;
	private List<MasterRedstoneSwitch> switches;
	private BlockPos controllerPos;
	private boolean openGui;

	public RedstoneControllerDataMessage() {
	}

	public RedstoneControllerDataMessage(Side source, List<MasterRedstoneSwitch> switches, BlockPos controllerPos,
			boolean openGui) {
		this.source = source;
		this.switches = switches;
		this.controllerPos = controllerPos;
		this.openGui = openGui;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		switches = new ArrayList<MasterRedstoneSwitch>();
		source = (buf.readByte() == 0) ? Side.CLIENT : Side.SERVER;
		controllerPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		byte size = buf.readByte();
		for (int i = 0; i < size; i++) {
			MasterRedstoneSwitch mls = new MasterRedstoneSwitch();
			mls.read(ByteBufUtils.readTag(buf));
			switches.add(mls);
		}
		openGui = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte((source.equals(Side.CLIENT)) ? 0 : 1);
		buf.writeInt(controllerPos.getX());
		buf.writeInt(controllerPos.getY());
		buf.writeInt(controllerPos.getZ());
		buf.writeByte(switches.size());
		for (MasterRedstoneSwitch mls : switches)
			ByteBufUtils.writeTag(buf, mls.write());
		buf.writeBoolean(openGui);
	}

}
