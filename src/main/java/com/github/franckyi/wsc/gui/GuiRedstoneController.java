package com.github.franckyi.wsc.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.franckyi.wsc.handlers.PacketHandler;
import com.github.franckyi.wsc.logic.BaseRedstoneController;
import com.github.franckyi.wsc.logic.BaseRedstoneSwitch;
import com.github.franckyi.wsc.logic.MasterRedstoneSwitch;
import com.github.franckyi.wsc.network.RedstoneUnlinkingMessage;
import com.github.franckyi.wsc.network.UpdateRedstoneControllerMessage;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.GuiScrollingList;

public class GuiRedstoneController extends GuiScreen {

	private class GraphicalSwitch {

		private GuiTextField nameField;
		private GuiOnOffButton enabledButton;
		private GuiPower power;
		private GuiButton unlinkButton;

		private GraphicalSwitch(BaseRedstoneSwitch ls, int delta) {
			this.nameField = new GuiTextField(1 + 10 * delta, fontRenderer, width / 2 + 40, height / 2 - 50, 100, 20);
			this.nameField.setText(ls.getName());
			this.enabledButton = new GuiOnOffButton(2 + 10 * delta, width / 2 + 75, height / 2 - 25, ls.isEnabled());
			this.power = new GuiPower(3 + 10 * delta, fontRenderer, width / 2 + 55, height / 2);
			this.power.getPower().setText(ls.getPower() + "");
			this.unlinkButton = new GuiButton(6 + 10 * delta, width / 2 + 65, height / 2 + 25, 50, 20, "§cUnlink");
			setVisible(false);
		}

		private void setVisible(boolean visible) {
			this.nameField.setVisible(visible);
			this.enabledButton.visible = visible;
			this.power.getPower().setVisible(visible);
			this.power.getAdd().visible = visible;
			this.power.getSubstract().visible = visible;
			this.unlinkButton.visible = visible;
		}

	}

	private class GuiSlotSwitchList extends GuiScrollingList {

		public GuiSlotSwitchList() {
			super(mc, 100, height, 60, height - 60, 20, 30, width, height);
		}

		@Override
		protected void drawBackground() {
		}

		@Override
		protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
			MasterRedstoneSwitch mls = controller.getSwitches().get(slotIdx);
			String name = StringUtils.stripControlCodes(mls.getName());
			drawCenteredString(fontRenderer, name, this.left + this.listWidth / 2 - 4, slotTop + 3,
					(mls.isEnabled()) ? 0x55FF55 : 0xFF5555);
			drawString(fontRenderer, new String(new char[mls.getPower()]).replace("\0", "| "), this.left + 4,
					slotTop + 15, (mls.isEnabled()) ? 0x55FF55 : 0xFF5555);
		}

		@Override
		protected void elementClicked(int index, boolean doubleClick) {
			selectSwitchIndex(index);
		}

		@Override
		protected int getContentHeight() {
			return this.getSize() * this.slotHeight + 1;
		}

		@Override
		protected int getSize() {
			return controller.getSwitches().size();
		}

		@Override
		protected boolean isSelected(int index) {
			return switchIndexSelected(index);
		}

	}

	private BaseRedstoneController controller;
	private BlockPos pos;
	private int selected = -1;

	private MasterRedstoneSwitch selectedSwitch;

	private GraphicalSwitch selectedGSwitch;
	private GuiButton done, cancel;

	private GuiSlotSwitchList list;

	private List<GraphicalSwitch> gswitches = new ArrayList<GraphicalSwitch>();

	public GuiRedstoneController(BaseRedstoneController controller, BlockPos pos) {
		this.controller = controller;
		this.pos = pos;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == done) {
			if (selected != -1)
				saveCache();
			PacketHandler.INSTANCE.sendToServer(new UpdateRedstoneControllerMessage(pos, controller));
		}
		if (button == done || button == cancel) {
			mc.displayGuiScreen(null);
			if (this.mc.currentScreen == null)
				this.mc.setIngameFocus();
		}
		boolean unlinking = false;
		for (GraphicalSwitch gs : gswitches) {
			if (button == gs.enabledButton) {
				controller.getSwitches().get(selected).setEnabled(gs.enabledButton.value());
				break;
			}
			if (button == gs.unlinkButton) {
				PacketHandler.INSTANCE.sendToServer(new RedstoneUnlinkingMessage(selectedSwitch.getSwitchPos(), pos));
				unlinking = true;
				break;
			}
			if (button == gs.power.getAdd() || button == gs.power.getSubstract()) {
				controller.getSwitches().get(selected).setPower(gs.power.getPower().getInt());
			}
		}
		if (unlinking) {
			selectedGSwitch.setVisible(false);
			controller.getSwitches().remove(selected);
			gswitches.remove(selected);
			selectedGSwitch = null;
			selectedSwitch = null;
			selected = -1;
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(fontRenderer, "Redstone Controller", width / 2, 20, 0xffffff);
		if (selected == -1)
			this.drawCenteredString(fontRenderer, "Select a linked switch in the menu.", width / 2 + 60,
					height / 2 - 10, 0x5555FF);
		else {
			this.drawString(fontRenderer, "Name :", width / 2 - 40, height / 2 - 43, 0xffffff);
			this.drawString(fontRenderer, "Enabled :", width / 2 - 40, height / 2 - 18, 0xffffff);
			this.drawString(fontRenderer, "Power :", width / 2 - 40, height / 2 + 8, 0xffffff);
		}
		if (selected != -1) {
			selectedGSwitch.nameField.drawTextBox();
			selectedGSwitch.power.getPower().drawTextBox();
			selectedGSwitch.enabledButton.x = width / 2 + 75;
			selectedGSwitch.enabledButton.y = height / 2 - 25;
			selectedGSwitch.unlinkButton.x = width / 2 + 65;
			selectedGSwitch.unlinkButton.y = height / 2 + 25;
		}

		list.drawScreen(mouseX, mouseY, partialTicks);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		int i = 0;
		for (BaseRedstoneSwitch ls : controller.getSwitches()) {
			GraphicalSwitch gs = new GraphicalSwitch(ls, i++);
			gswitches.add(gs);
			buttonList.add(gs.enabledButton);
			buttonList.add(gs.unlinkButton);
			buttonList.add(gs.power.getAdd());
			buttonList.add(gs.power.getSubstract());
		}
		this.list = new GuiSlotSwitchList();
		buttonList.add(cancel = new GuiButton(3, width / 2 - 100, height - 40, 90, 20, "§cCancel"));
		buttonList.add(done = new GuiButton(4, width / 2 + 10, height - 40, 90, 20, "§aDone"));
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (selected != -1) {
			selectedGSwitch.nameField.textboxKeyTyped(typedChar, keyCode);
			selectedGSwitch.power.getPower().textboxKeyTyped(typedChar, keyCode);
			selectedSwitch.setName(selectedGSwitch.nameField.getText());
			selectedSwitch.setPower(selectedGSwitch.power.getPower().getInt());
		}
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (selected != -1) {
			selectedGSwitch.nameField.mouseClicked(mouseX, mouseY, mouseButton);
			selectedGSwitch.power.getPower().mouseClicked(mouseX, mouseY, mouseButton);
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	private void saveCache() {
		selectedSwitch.setName(selectedGSwitch.nameField.getText());
		selectedSwitch.setPower(selectedGSwitch.power.getPower().getInt());
	}

	private void selectSwitchIndex(int index) {
		if (index == this.selected)
			return;
		if (selected != -1)
			this.selectedGSwitch.setVisible(false);
		this.selected = index;
		this.selectedSwitch = (index >= 0 && index <= controller.getSwitches().size())
				? controller.getSwitches().get(selected)
				: null;
		this.selectedGSwitch = (index >= 0 && index <= gswitches.size()) ? gswitches.get(selected) : null;
		if (selected != -1)
			this.selectedGSwitch.setVisible(true);
	}

	private boolean switchIndexSelected(int index) {
		return index == selected;
	}

	@Override
	public void updateScreen() {
		if (selected != -1) {
			selectedGSwitch.nameField.updateCursorCounter();
			selectedGSwitch.power.getPower().updateCursorCounter();
		}
	}

}
