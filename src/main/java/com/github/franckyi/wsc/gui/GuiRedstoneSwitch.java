package com.github.franckyi.wsc.gui;

import java.io.IOException;

import com.github.franckyi.wsc.handlers.PacketHandler;
import com.github.franckyi.wsc.network.RedstoneSwitchDataMessage;
import com.github.franckyi.wsc.util.SlaveRedstoneSwitch;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

public class GuiRedstoneSwitch extends GuiScreen {

	private SlaveRedstoneSwitch sls;
	private BlockPos pos;

	private GuiTextField name;
	private GuiOnOffButton enabled;
	private GuiPower power;
	private GuiButton done, cancel;

	public GuiRedstoneSwitch(SlaveRedstoneSwitch sls, BlockPos pos) {
		this.pos = pos;
		this.sls = sls;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == done) {
			sls.setName(name.getText());
			sls.setPower(power.getPower().getInt());
			sls.setEnabled(enabled.value());
			PacketHandler.INSTANCE.sendToServer(new RedstoneSwitchDataMessage(Side.CLIENT, sls, pos, false));
		}
		if (button == done || button == cancel) {
			mc.displayGuiScreen(null);
			if (this.mc.currentScreen == null)
				this.mc.setIngameFocus();
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(fontRenderer, "Redstone Switch", width / 2, 20, 0xffffff);
		if (sls.isLinked())
			this.drawCenteredString(fontRenderer, "This switch is linked to " + sls.getControllerPos().size()
					+ " controller" + ((sls.getControllerPos().size() > 1) ? "s." : "."), width / 2, 50, 0x55FF55);
		else
			this.drawCenteredString(fontRenderer, "This switch isn't linked to a controller.", width / 2, 50, 0xFF5555);
		this.drawString(fontRenderer, "Name :", width / 2 - 80, height / 2 - 28, 0xffffff);
		this.drawString(fontRenderer, "Enabled :", width / 2 - 80, height / 2 - 3, 0xffffff);
		this.drawString(fontRenderer, "Power :", width / 2 - 80, height / 2 + 23, 0xffffff);
		name.drawTextBox();
		power.getPower().drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		name = new GuiTextField(0, fontRenderer, width / 2 - 20, height / 2 - 35, 100, 20);
		name.setText(sls.getName());
		buttonList.add(enabled = new GuiOnOffButton(1, width / 2 + 15, height / 2 - 10, sls.isEnabled()));
		power = new GuiPower(2, fontRenderer, width / 2 - 5, height / 2 + 15);
		power.getPower().setText(sls.getPower() + "");
		buttonList.add(power.getAdd());
		buttonList.add(power.getSubstract());
		buttonList.add(cancel = new GuiButton(5, width / 2 - 100, height - 40, 90, 20, "§cCancel"));
		buttonList.add(done = new GuiButton(6, width / 2 + 10, height - 40, 90, 20, "§aDone"));
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		name.textboxKeyTyped(typedChar, keyCode);
		power.getPower().textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		name.mouseClicked(mouseX, mouseY, mouseButton);
		power.getPower().mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void updateScreen() {
		name.updateCursorCounter();
		power.getPower().updateCursorCounter();
	}

}
