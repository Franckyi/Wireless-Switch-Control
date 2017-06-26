package com.github.franckyi.wsc.network;

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

public class UpdateRedstoneControllerMessage implements IMessage {
	
	private BlockPos controllerPos;
	private BaseRedstoneController controller;

	public UpdateRedstoneControllerMessage(BlockPos controllerPos, BaseRedstoneController controller) {
		this.controllerPos = controllerPos;
		this.controller = controller;
	}
	
	public UpdateRedstoneControllerMessage() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		controllerPos = BlockPos.fromLong(buf.readLong());
		controller = new BaseRedstoneController();
		controller.deserializeNBT(ByteBufUtils.readTag(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(controllerPos.toLong());
		ByteBufUtils.writeTag(buf, controller.serializeNBT());
	}
	
	public static class ClientHandler extends PacketHandler.ClientHandler<UpdateRedstoneControllerMessage> {

		@Override
		public void run() {
			RedstoneCapabilities.setController(world, message.controllerPos, message.controller);
		}
		
	}
	
	public static class ServerHandler extends PacketHandler.ServerHandler<UpdateRedstoneControllerMessage> {

		@Override
		public void run() {
			RedstoneCapabilities.setController(world, message.controllerPos, message.controller);
			world.getTileEntity(message.controllerPos).markDirty();
			PacketHandler.INSTANCE.sendToAll(new UpdateRedstoneControllerMessage(message.controllerPos, message.controller));
			for(MasterRedstoneSwitch controllerSwitch1 : message.controller.getSwitches()) {
				Optional<SlaveRedstoneSwitch> s = RedstoneCapabilities.getSwitch(world, controllerSwitch1.getSwitchPos());
				if(s.isPresent()) {
					s.get().setEnabled(controllerSwitch1.isEnabled());
					s.get().setName(controllerSwitch1.getName());
					s.get().setPower(controllerSwitch1.getPower());
					world.getTileEntity(controllerSwitch1.getSwitchPos()).markDirty();
					PacketHandler.INSTANCE.sendToAll(new UpdateRedstoneSwitchMessage(controllerSwitch1.getSwitchPos(), s.get()));
					for(BlockPos controllerPos : s.get().getControllerPos()) {
						Optional<BaseRedstoneController> controller = RedstoneCapabilities.getController(world, controllerPos);
						if(controller.isPresent()) {
							for(MasterRedstoneSwitch controllerSwitch2 : controller.get().getSwitches()) {
								if(controllerSwitch2.getSwitchPos().equals(controllerSwitch1.getSwitchPos())) {
									controllerSwitch2.setEnabled(controllerSwitch1.isEnabled());
									controllerSwitch2.setName(controllerSwitch1.getName());
									controllerSwitch2.setPower(controllerSwitch1.getPower());
									world.getTileEntity(controllerPos).markDirty();
									PacketHandler.INSTANCE.sendToAll(new UpdateRedstoneControllerMessage(controllerPos, controller.get()));
								}
							}
						}
					}
				}
			}
		}
		
	}

}
