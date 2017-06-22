package com.github.franckyi.wsc.network;

import java.util.ArrayList;
import java.util.List;

import com.github.franckyi.wsc.WSCMod;
import com.github.franckyi.wsc.capability.RedstoneCapabilities;
import com.github.franckyi.wsc.handlers.GuiHandler;
import com.github.franckyi.wsc.util.MasterRedstoneSwitch;
import com.github.franckyi.wsc.util.SlaveRedstoneSwitch;
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

	public static class ControllerDataMessageHandler implements IMessageHandler<RedstoneControllerDataMessage, IMessage> {

		@Override
		public IMessage onMessage(final RedstoneControllerDataMessage message, MessageContext ctx) {
			if (message.source.equals(Side.SERVER)) {
				final World world = Minecraft.getMinecraft().world;
				IThreadListener mainThread = Minecraft.getMinecraft();
				mainThread.addScheduledTask(new Runnable() {
					@Override
					public void run() {
						RedstoneCapabilities.setControllerSwitches(world, message.pos, message.switches);
						Minecraft.getMinecraft().player.openGui(WSCMod.instance, GuiHandler.REDSTONE_CONTROLLER_GUI,
								Minecraft.getMinecraft().world, message.pos.getX(), message.pos.getY(),
								message.pos.getZ());
					}
				});
			} else {
				final EntityPlayerMP p = ctx.getServerHandler().player;
				IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.world;
				mainThread.addScheduledTask(new Runnable() {
					@Override
					public void run() {
						RedstoneCapabilities.setControllerSwitches(p.world, message.pos, message.switches);
						for (MasterRedstoneSwitch updatedControllerSwitch : RedstoneCapabilities.getControllerSwitches(p.world,
								message.pos)) {
							RedstoneCapabilities.updateSwitch(p.world, updatedControllerSwitch.getPos(),
									updatedControllerSwitch);
							Optional<SlaveRedstoneSwitch> updatedSwitchBlock = RedstoneCapabilities.getSwitch(p.world,
									updatedControllerSwitch.getPos());
							if (updatedSwitchBlock.isPresent()) {
								for (BlockPos pos : updatedSwitchBlock.get().getControllers()) {
									List<MasterRedstoneSwitch> ss = RedstoneCapabilities.getControllerSwitches(p.world, pos);
									for (MasterRedstoneSwitch oldControllerSwitch : RedstoneCapabilities
											.getControllerSwitches(p.world, pos)) {
										if (oldControllerSwitch.getPos().equals(updatedControllerSwitch.getPos())) {
											ss.set(ss.indexOf(oldControllerSwitch), updatedControllerSwitch);
											RedstoneCapabilities.updateTileEntity(p.world, updatedControllerSwitch.getPos());
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

	private BlockPos pos;

	public RedstoneControllerDataMessage() {
	}

	public RedstoneControllerDataMessage(Side source, List<MasterRedstoneSwitch> switches, BlockPos pos) {
		this.source = source;
		this.switches = switches;
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		switches = new ArrayList<MasterRedstoneSwitch>();
		source = (buf.readByte() == 0) ? Side.CLIENT : Side.SERVER;
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		byte size = buf.readByte();
		for (int i = 0; i < size; i++) {
			MasterRedstoneSwitch mls = new MasterRedstoneSwitch();
			mls.read(ByteBufUtils.readTag(buf));
			switches.add(mls);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte((source.equals(Side.CLIENT)) ? 0 : 1);
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeByte(switches.size());
		for (MasterRedstoneSwitch mls : switches)
			ByteBufUtils.writeTag(buf, mls.write());
	}

}
