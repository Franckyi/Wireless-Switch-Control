package com.github.franckyi.wsc.network;

import java.util.List;

import com.github.franckyi.wsc.WSCMod;
import com.github.franckyi.wsc.capability.Capabilities;
import com.github.franckyi.wsc.handlers.GuiHandler;
import com.github.franckyi.wsc.util.MasterLogicalSwitch;
import com.github.franckyi.wsc.util.SlaveLogicalSwitch;

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

public class SwitchDataMessage implements IMessage {

	private Side source;
	private SlaveLogicalSwitch sls;
	private BlockPos pos;

	public SwitchDataMessage(Side dataSource, SlaveLogicalSwitch sls, BlockPos pos) {
		this.source = dataSource;
		this.sls = sls;
		this.pos = pos;
	}

	public SwitchDataMessage() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		sls = new SlaveLogicalSwitch();
		source = (buf.readByte() == 0) ? Side.CLIENT : Side.SERVER;
		sls.read(ByteBufUtils.readTag(buf));
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte((source.equals(Side.CLIENT)) ? 0 : 1);
		ByteBufUtils.writeTag(buf, sls.write());
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
	}

	public static class SwitchDataMessageHandler implements IMessageHandler<SwitchDataMessage, IMessage> {

		@Override
		public IMessage onMessage(final SwitchDataMessage message, MessageContext ctx) {
			if (message.source.equals(Side.SERVER)) {
				final World world = Minecraft.getMinecraft().world;
				IThreadListener mainThread = Minecraft.getMinecraft();
				mainThread.addScheduledTask(new Runnable() {
					@Override
					public void run() {
						Capabilities.setSwitch(world, message.pos, message.sls);
						Minecraft.getMinecraft().player.openGui(WSCMod.instance, GuiHandler.REDSTONE_SWITCH_GUI,
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
						Capabilities.setSwitch(p.world, message.pos, message.sls);
						for (BlockPos pos : Capabilities.getSwitch(p.world, message.pos).getControllers()) {
							List<MasterLogicalSwitch> list = Capabilities.getControllerSwitches(p.world, pos);
							for (MasterLogicalSwitch mls : list) {
								if (mls.getPos().equals(message.pos)) {
									list.set(list.indexOf(mls), new MasterLogicalSwitch(message.sls, message.pos));
									Capabilities.updateTileEntity(p.world, pos);
									break;
								}
							}
						}
						Capabilities.getLink(p).clear();
					}
				});
			}
			return null;
		}

	}

}
