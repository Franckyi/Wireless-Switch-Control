package com.github.franckyi.wsc.network;

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
						RedstoneCapabilities.setSwitch(world, message.switchPos, message.sls);
						if (message.openGui)
							Minecraft.getMinecraft().player.openGui(WSCMod.instance, GuiHandler.REDSTONE_SWITCH_GUI,
									Minecraft.getMinecraft().world, message.switchPos.getX(), message.switchPos.getY(),
									message.switchPos.getZ());
					}
				});
			} else {
				final EntityPlayerMP p = ctx.getServerHandler().player;
				IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.world;
				mainThread.addScheduledTask(new Runnable() {
					@Override
					public void run() {
						RedstoneCapabilities.setSwitch(p.world, message.switchPos, message.sls);
						Optional<SlaveRedstoneSwitch> osls = RedstoneCapabilities.getSwitch(p.world, message.switchPos);
						if (osls.isPresent()) {
							for (BlockPos pos : osls.get().getControllerPos()) {
								List<MasterRedstoneSwitch> list = RedstoneCapabilities.getControllerSwitches(p.world,
										pos);
								for (MasterRedstoneSwitch mls : list) {
									if (mls.getSwitchPos().equals(message.switchPos)) {
										list.set(list.indexOf(mls),
												new MasterRedstoneSwitch(message.sls, message.switchPos));
										RedstoneCapabilities.updateTileEntity(p.world, pos);
										break;
									}
								}
								PacketHandler.INSTANCE
										.sendToAll(new RedstoneControllerDataMessage(Side.SERVER, list, pos, false));
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
	private BlockPos switchPos;
	private boolean openGui;

	public RedstoneSwitchDataMessage() {
	}

	public RedstoneSwitchDataMessage(Side dataSource, SlaveRedstoneSwitch sls, BlockPos switchPos, boolean openGui) {
		this.source = dataSource;
		this.sls = sls;
		this.switchPos = switchPos;
		this.openGui = openGui;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		sls = new SlaveRedstoneSwitch();
		source = (buf.readByte() == 0) ? Side.CLIENT : Side.SERVER;
		sls.read(ByteBufUtils.readTag(buf));
		switchPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		openGui = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte((source.equals(Side.CLIENT)) ? 0 : 1);
		ByteBufUtils.writeTag(buf, sls.write());
		buf.writeInt(switchPos.getX());
		buf.writeInt(switchPos.getY());
		buf.writeInt(switchPos.getZ());
		buf.writeBoolean(openGui);
	}

}
