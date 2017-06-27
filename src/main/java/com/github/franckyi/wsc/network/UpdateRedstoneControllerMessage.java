package com.github.franckyi.wsc.network;

import java.util.HashSet;
import java.util.Set;

import com.github.franckyi.wsc.capability.RedstoneCapabilities;
import com.github.franckyi.wsc.handlers.PacketHandler;
import com.github.franckyi.wsc.logic.BaseRedstoneController;
import com.github.franckyi.wsc.logic.FullRedstoneController;
import com.github.franckyi.wsc.logic.FullRedstoneSwitch;
import com.github.franckyi.wsc.logic.MasterRedstoneSwitch;
import com.github.franckyi.wsc.logic.SlaveRedstoneSwitch;
import com.google.common.base.Optional;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class UpdateRedstoneControllerMessage implements IMessage {

	public static class ClientHandler extends PacketHandler.ClientHandler<UpdateRedstoneControllerMessage> {

		@Override
		public void run() {
			for (FullRedstoneController controller : message.controllers)
				RedstoneCapabilities.setController(world, controller.getControllerPos(), controller);
		}

	}

	public static class ServerHandler extends PacketHandler.ServerHandler<UpdateRedstoneControllerMessage> {

		@Override
		public void run() {
			Set<FullRedstoneController> updateControllers = new HashSet<FullRedstoneController>();
			Set<FullRedstoneSwitch> updateSwitches = new HashSet<FullRedstoneSwitch>();
			for (FullRedstoneController controller : message.controllers) {
				RedstoneCapabilities.setController(world, controller.getControllerPos(), controller);
				world.getTileEntity(controller.getControllerPos()).markDirty();
				updateControllers.add(controller);
				for (MasterRedstoneSwitch controllerSwitch1 : controller.getSwitches()) {
					Optional<SlaveRedstoneSwitch> s = RedstoneCapabilities.getSwitch(world,
							controllerSwitch1.getSwitchPos());
					if (s.isPresent()) {
						s.get().setEnabled(controllerSwitch1.isEnabled());
						s.get().setName(controllerSwitch1.getName());
						s.get().setPower(controllerSwitch1.getPower());
						world.getTileEntity(controllerSwitch1.getSwitchPos()).markDirty();
						updateSwitches.add(new FullRedstoneSwitch(s.get(), controllerSwitch1.getSwitchPos()));
						for (BlockPos controllerPos : s.get().getControllerPos()) {
							Optional<BaseRedstoneController> controller2 = RedstoneCapabilities.getController(world,
									controllerPos);
							if (controller2.isPresent()) {
								for (MasterRedstoneSwitch controllerSwitch2 : controller2.get().getSwitches()) {
									if (controllerSwitch2.getSwitchPos().equals(controllerSwitch1.getSwitchPos())) {
										controllerSwitch2.setEnabled(controllerSwitch1.isEnabled());
										controllerSwitch2.setName(controllerSwitch1.getName());
										controllerSwitch2.setPower(controllerSwitch1.getPower());
										world.getTileEntity(controllerPos).markDirty();
										updateControllers
												.add(new FullRedstoneController(controller2.get(), controllerPos));
									}
								}
							}
						}
					}
				}
			}
			PacketHandler.INSTANCE.sendToAll(new UpdateRedstoneControllerMessage(updateControllers));
			PacketHandler.INSTANCE.sendToAll(new UpdateRedstoneSwitchMessage(updateSwitches));
		}

	}

	private Set<FullRedstoneController> controllers;

	public UpdateRedstoneControllerMessage() {
	}

	public UpdateRedstoneControllerMessage(BlockPos pos, BaseRedstoneController controller) {
		this(new FullRedstoneController(controller, pos));
	}

	public UpdateRedstoneControllerMessage(FullRedstoneController controller) {
		controllers = new HashSet<FullRedstoneController>();
		controllers.add(controller);
	}

	public UpdateRedstoneControllerMessage(Set<FullRedstoneController> controllers) {
		this.controllers = controllers;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int size = buf.readInt();
		controllers = new HashSet<FullRedstoneController>();
		for (int i = 0; i < size; i++) {
			FullRedstoneController controller = new FullRedstoneController();
			controller.deserializeNBT(ByteBufUtils.readTag(buf));
			controllers.add(controller);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(controllers.size());
		for (FullRedstoneController controller : controllers)
			ByteBufUtils.writeTag(buf, controller.serializeNBT());
	}

}
