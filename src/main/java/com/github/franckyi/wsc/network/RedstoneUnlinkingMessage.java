package com.github.franckyi.wsc.network;

import com.github.franckyi.wsc.capability.RedstoneCapabilities;
import com.github.franckyi.wsc.handlers.PacketHandler;
import com.github.franckyi.wsc.logic.BaseRedstoneController;
import com.github.franckyi.wsc.logic.MasterRedstoneSwitch;
import com.github.franckyi.wsc.logic.SlaveRedstoneSwitch;
import com.google.common.base.Optional;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class RedstoneUnlinkingMessage implements IMessage {

	private BlockPos switchPos, controllerPos;

	public RedstoneUnlinkingMessage(BlockPos switchPos, BlockPos controllerPos) {
		this.switchPos = switchPos;
		this.controllerPos = controllerPos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		switchPos = BlockPos.fromLong(buf.readLong());
		controllerPos = BlockPos.fromLong(buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(switchPos.toLong());
		buf.writeLong(controllerPos.toLong());
	}

	public static class ServerHandler extends PacketHandler.ServerHandler<RedstoneUnlinkingMessage> {

		@Override
		public void run() {
			Optional<BaseRedstoneController> controller = RedstoneCapabilities.getController(world,
					message.controllerPos);
			if (controller.isPresent()) {
				Optional<MasterRedstoneSwitch> toRemove = Optional.absent();
				for (MasterRedstoneSwitch s : controller.get().getSwitches())
					if (s.getSwitchPos().equals(message.switchPos)) {
						toRemove = Optional.of(s);
						break;
					}
				if (toRemove.isPresent()) {
					controller.get().getSwitches().remove(toRemove.get());
					world.getTileEntity(message.controllerPos).markDirty();
					PacketHandler.INSTANCE
							.sendToAll(new UpdateRedstoneControllerMessage(message.controllerPos, controller.get()));
				}
			}
			Optional<SlaveRedstoneSwitch> s = RedstoneCapabilities.getSwitch(world, message.switchPos);
			if (s.isPresent()) {
				if (s.get().getControllerPos().remove(message.controllerPos)) {
					world.getTileEntity(message.switchPos).markDirty();
					PacketHandler.INSTANCE.sendToAll(new UpdateRedstoneSwitchMessage(message.switchPos, s.get()));
				}
			}
		}

	}

}
