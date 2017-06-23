package com.github.franckyi.wsc.network;

import java.util.List;

import com.github.franckyi.wsc.capability.RedstoneCapabilities;
import com.github.franckyi.wsc.logic.BaseRedstoneController;
import com.github.franckyi.wsc.logic.MasterRedstoneSwitch;
import com.github.franckyi.wsc.logic.SlaveRedstoneSwitch;
import com.google.common.base.Optional;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RedstoneUnlinkingMessage implements IMessage {

	public static class UnlinkingMessageHandler implements IMessageHandler<RedstoneUnlinkingMessage, IMessage> {

		@Override
		public IMessage onMessage(final RedstoneUnlinkingMessage message, MessageContext ctx) {
			final World world = ctx.getServerHandler().player.world;
			IThreadListener mainThread = (WorldServer) world;
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					BaseRedstoneController controller = RedstoneCapabilities.getController(world,
							message.controllerPos);
					MasterRedstoneSwitch toRemove = null;
					for (MasterRedstoneSwitch mls : controller.getSwitches())
						if (mls.getSwitchPos().equals(message.switchPos)) {
							toRemove = mls;
							break;
						}
					if (toRemove != null)
						controller.getSwitches().remove(toRemove);
					RedstoneCapabilities.setController(world, message.controllerPos, controller);
					Optional<SlaveRedstoneSwitch> osls = RedstoneCapabilities.getSwitch(world, message.switchPos);
					if (osls.isPresent()) {
						osls.get().getControllerPos().remove(message.controllerPos);
						RedstoneCapabilities.setSwitch(world, message.switchPos, osls.get());
					}
				}
			});
			return null;
		}

	}

	private BlockPos switchPos;
	private BlockPos controllerPos;

	public RedstoneUnlinkingMessage() {
	}

	public RedstoneUnlinkingMessage(BlockPos switchPos, BlockPos controllerPos) {
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
