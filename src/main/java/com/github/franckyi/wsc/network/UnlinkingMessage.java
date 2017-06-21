package com.github.franckyi.wsc.network;

import java.util.List;

import com.github.franckyi.wsc.capability.Capabilities;
import com.github.franckyi.wsc.util.MasterLogicalSwitch;
import com.github.franckyi.wsc.util.SlaveLogicalSwitch;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UnlinkingMessage implements IMessage {

	private MasterLogicalSwitch mls;
	private BlockPos pos;

	public UnlinkingMessage(MasterLogicalSwitch mls, BlockPos pos) {
		this.mls = mls;
		this.pos = pos;
	}

	public UnlinkingMessage() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		mls = new MasterLogicalSwitch();
		mls.read(ByteBufUtils.readTag(buf));
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, mls.write());
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
	}

	public static class UnlinkingMessageHandler implements IMessageHandler<UnlinkingMessage, IMessage> {

		@Override
		public IMessage onMessage(final UnlinkingMessage message, MessageContext ctx) {
			final World world = ctx.getServerHandler().player.world;
			IThreadListener mainThread = (WorldServer) world;
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					List<MasterLogicalSwitch> switches = Capabilities.getControllerSwitches(world, message.pos);
					MasterLogicalSwitch toRemove = null;
					for (MasterLogicalSwitch mls : switches)
						if (mls.getPos().equals(message.mls.getPos())) {
							toRemove = mls;
							break;
						}
					if (toRemove != null)
						switches.remove(toRemove);
					Capabilities.setControllerSwitches(world, message.pos, switches);
					SlaveLogicalSwitch sls = Capabilities.getSwitch(world, message.mls.getPos());
					sls.getControllers().remove(message.pos);
					if (sls.getControllers().isEmpty())
						sls.setLinked(false);
					Capabilities.setSwitch(world, message.mls.getPos(), sls);
				}
			});
			return null;
		}

	}

}
