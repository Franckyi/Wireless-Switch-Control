package com.github.franckyi.wsc.network;

import java.util.List;

import com.github.franckyi.wsc.capability.RedstoneCapabilities;
import com.github.franckyi.wsc.handlers.PacketHandler;
import com.github.franckyi.wsc.logic.BaseRedstoneController;
import com.github.franckyi.wsc.logic.MasterRedstoneSwitch;
import com.github.franckyi.wsc.logic.SlaveRedstoneSwitch;
import com.google.common.base.Optional;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class UpdateRedstoneSwitchMessage implements IMessage {

	private BlockPos switchPos;
	private SlaveRedstoneSwitch s;

	public UpdateRedstoneSwitchMessage(BlockPos switchPos, SlaveRedstoneSwitch s) {
		this.switchPos = switchPos;
		this.s = s;
	}
	
	public UpdateRedstoneSwitchMessage() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		switchPos = BlockPos.fromLong(buf.readLong());
		s = new SlaveRedstoneSwitch();
		s.deserializeNBT(ByteBufUtils.readTag(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(switchPos.toLong());
		ByteBufUtils.writeTag(buf, s.serializeNBT());
	}
	
	public static class ClientHandler extends PacketHandler.ClientHandler<UpdateRedstoneSwitchMessage> {

		@Override
		public void run() {
			RedstoneCapabilities.setSwitch(world, message.switchPos, message.s);
		}
		
	}
	
	public static class ServerHandler extends PacketHandler.ServerHandler<UpdateRedstoneSwitchMessage> {

		@Override
		public void run() {
			RedstoneCapabilities.setSwitch(world, message.switchPos, message.s);
			world.getTileEntity(message.switchPos).markDirty();
			PacketHandler.INSTANCE.sendToAll(new UpdateRedstoneSwitchMessage(message.switchPos, message.s));
			for(BlockPos controllerPos : message.s.getControllerPos()) {
				Optional<BaseRedstoneController> controller = RedstoneCapabilities.getController(world, controllerPos);
				if(controller.isPresent()) {
					for(MasterRedstoneSwitch s : controller.get().getSwitches())
						if(s.getSwitchPos().equals(message.switchPos)) {
							s.setEnabled(message.s.isEnabled());
							s.setName(message.s.getName());
							s.setPower(message.s.getPower());
						}	
					world.getTileEntity(controllerPos).markDirty();
					PacketHandler.INSTANCE.sendToAll(new UpdateRedstoneControllerMessage(controllerPos, controller.get()));
				}
			}
		}
		
	}

}
