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

public class UpdateRedstoneSwitchMessage implements IMessage {

	public static class ClientHandler extends PacketHandler.ClientHandler<UpdateRedstoneSwitchMessage> {

		@Override
		public void run() {
			for (FullRedstoneSwitch s : message.switches)
				RedstoneCapabilities.setSwitch(world, s.getSwitchPos(), s);
		}

	}

	public static class ServerHandler extends PacketHandler.ServerHandler<UpdateRedstoneSwitchMessage> {

		@Override
		public void run() {
			Set<FullRedstoneController> updateControllers = new HashSet<FullRedstoneController>();
			Set<FullRedstoneSwitch> updateSwitches = new HashSet<FullRedstoneSwitch>();
			for (FullRedstoneSwitch s : message.switches) {
				RedstoneCapabilities.setSwitch(world, s.getSwitchPos(), s);
				world.getTileEntity(s.getSwitchPos()).markDirty();
				updateSwitches.add(s);
				for (BlockPos controllerPos : s.getControllerPos()) {
					Optional<BaseRedstoneController> controller = RedstoneCapabilities.getController(world,
							controllerPos);
					if (controller.isPresent()) {
						for (MasterRedstoneSwitch s2 : controller.get().getSwitches())
							if (s2.getSwitchPos().equals(s.getSwitchPos())) {
								s2.setEnabled(s.isEnabled());
								s2.setName(s.getName());
								s2.setPower(s.getPower());
							}
						world.getTileEntity(controllerPos).markDirty();
						updateControllers.add(new FullRedstoneController(controller.get(), controllerPos));
					}
				}
			}
			PacketHandler.INSTANCE.sendToAll(new UpdateRedstoneControllerMessage(updateControllers));
			PacketHandler.INSTANCE.sendToAll(new UpdateRedstoneSwitchMessage(updateSwitches));
		}

	}

	private Set<FullRedstoneSwitch> switches;

	public UpdateRedstoneSwitchMessage() {
	}

	public UpdateRedstoneSwitchMessage(BlockPos pos, SlaveRedstoneSwitch s) {
		this(new FullRedstoneSwitch(s, pos));
	}

	public UpdateRedstoneSwitchMessage(FullRedstoneSwitch s) {
		switches = new HashSet<FullRedstoneSwitch>();
		switches.add(s);
	}

	public UpdateRedstoneSwitchMessage(Set<FullRedstoneSwitch> switches) {
		this.switches = switches;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int size = buf.readInt();
		switches = new HashSet<FullRedstoneSwitch>();
		for (int i = 0; i < size; i++) {
			FullRedstoneSwitch s = new FullRedstoneSwitch();
			s.deserializeNBT(ByteBufUtils.readTag(buf));
			switches.add(s);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(switches.size());
		for (FullRedstoneSwitch s : switches)
			ByteBufUtils.writeTag(buf, s.serializeNBT());
	}

}
