package com.github.franckyi.wsc.network;

import java.util.List;

import com.github.franckyi.wsc.capability.Capabilities;
import com.github.franckyi.wsc.util.MasterLogicalSwitch;
import com.github.franckyi.wsc.util.SlaveLogicalSwitch;
import com.google.common.base.Optional;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UnlinkingMessage implements IMessage {

	public static class UnlinkingMessageHandler implements IMessageHandler<UnlinkingMessage, IMessage> {

		@Override
		public IMessage onMessage(final UnlinkingMessage message, MessageContext ctx) {
			final World world = ctx.getServerHandler().player.world;
			IThreadListener mainThread = (WorldServer) world;
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					List<MasterLogicalSwitch> switches = Capabilities.getControllerSwitches(world,
							message.controllerPos);
					MasterLogicalSwitch toRemove = null;
					for (MasterLogicalSwitch mls : switches)
						if (mls.getPos().equals(message.switchPos)) {
							toRemove = mls;
							break;
						}
					if (toRemove != null)
						switches.remove(toRemove);
					Capabilities.setControllerSwitches(world, message.controllerPos, switches);
					Optional<SlaveLogicalSwitch> osls = Capabilities.getSwitch(world, message.switchPos);
					if (osls.isPresent()) {
						osls.get().getControllers().remove(message.controllerPos);
						if (osls.get().getControllers().isEmpty())
							osls.get().setLinked(false);
						Capabilities.setSwitch(world, message.switchPos, osls.get());
					}
				}
			});
			return null;
		}

	}

	private BlockPos switchPos;

	private BlockPos controllerPos;

	public UnlinkingMessage() {
	}

	public UnlinkingMessage(BlockPos switchPos, BlockPos controllerPos) {
		this.switchPos = switchPos;
		this.controllerPos = controllerPos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		switchPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		controllerPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(switchPos.getX());
		buf.writeInt(switchPos.getY());
		buf.writeInt(switchPos.getZ());
		buf.writeInt(controllerPos.getX());
		buf.writeInt(controllerPos.getY());
		buf.writeInt(controllerPos.getZ());
	}

}
