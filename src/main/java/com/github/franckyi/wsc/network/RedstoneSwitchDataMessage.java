package com.github.franckyi.wsc.network;

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

public class RedstoneSwitchDataMessage implements IMessage {

	public static class SwitchDataMessageHandler implements IMessageHandler<RedstoneSwitchDataMessage, IMessage> {

		@Override
		public IMessage onMessage(final RedstoneSwitchDataMessage message, MessageContext ctx) {
			if (message.source.equals(Side.SERVER)) {
				final World world = Minecraft.getMinecraft().world;
				IThreadListener mainThread = Minecraft.getMinecraft();
				mainThread.addScheduledTask(new Runnable() {
					@Override
					public void run() {
						RedstoneCapabilities.setSwitch(world, message.pos, message.sls);
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
						RedstoneCapabilities.setSwitch(p.world, message.pos, message.sls);
						Optional<SlaveRedstoneSwitch> osls = RedstoneCapabilities.getSwitch(p.world, message.pos);
						if (osls.isPresent()) {
							for (BlockPos pos : RedstoneCapabilities.getSwitch(p.world, message.pos).get().getControllers()) {
								List<MasterRedstoneSwitch> list = RedstoneCapabilities.getControllerSwitches(p.world, pos);
								for (MasterRedstoneSwitch mls : list) {
									if (mls.getPos().equals(message.pos)) {
										list.set(list.indexOf(mls), new MasterRedstoneSwitch(message.sls, message.pos));
										RedstoneCapabilities.updateTileEntity(p.world, pos);
										break;
									}
								}
							}
							RedstoneCapabilities.getLink(p).reset();
						}

					}
				});
			}
			return null;
		}

	}

	private Side source;
	private SlaveRedstoneSwitch sls;

	private BlockPos pos;

	public RedstoneSwitchDataMessage() {
	}

	public RedstoneSwitchDataMessage(Side dataSource, SlaveRedstoneSwitch sls, BlockPos pos) {
		this.source = dataSource;
		this.sls = sls;
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		sls = new SlaveRedstoneSwitch();
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

}
