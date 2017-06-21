package com.github.franckyi.wsc.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.franckyi.wsc.handlers.PacketHandler;
import com.github.franckyi.wsc.network.ControllerDataMessage;
import com.github.franckyi.wsc.util.BaseLogicalSwitch;
import com.github.franckyi.wsc.util.MasterLogicalSwitch;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.relauncher.Side;

public class GuiRedstoneController extends GuiScreen {

	private List<MasterLogicalSwitch> switches;
	private BlockPos pos;

	private int selected = -1;
	private MasterLogicalSwitch selectedSwitch;
	private GraphicalSwitch selectedGSwitch;

	private GuiButton done, cancel;

	private GuiSlotSwitchList list;
	private List<GraphicalSwitch> gswitches = new ArrayList<GraphicalSwitch>();

	public GuiRedstoneController(List<MasterLogicalSwitch> switches, BlockPos pos) {
		this.switches = switches;
		this.pos = pos;
	}

	@Override
	public void initGui() {
		int i = 0;
		for (BaseLogicalSwitch ls : switches) {
			GraphicalSwitch gs = new GraphicalSwitch(ls, this, i++);
			gswitches.add(gs);
			buttonList.add(gs.enabledButton);
			buttonList.add(gs.unlinkButton);
		}
		this.list = new GuiSlotSwitchList(100, 20);
		buttonList.add(cancel = new GuiButton(3, width/2 - 100, height - 40, 90, 20, "§cCancel"));
		buttonList.add(done = new GuiButton(4, width/2 + 10, height - 40, 90, 20, "§aDone"));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button == done) {
			if (selected != -1)
				saveCache();
			PacketHandler.INSTANCE.sendToServer(new ControllerDataMessage(Side.CLIENT, switches, pos));
		}
		if(button == done || button == cancel) {
			mc.displayGuiScreen(null);
			if (this.mc.currentScreen == null)
	            this.mc.setIngameFocus();
		}
		for(GraphicalSwitch gs : gswitches) {
			if(button == gs.enabledButton)
				switches.get(gswitches.indexOf(gs)).setEnabled(gs.enabledButton.value());
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(fontRenderer, "Redstone Controller", width / 2, 20, 0xffffff);
		if (selected == -1)
			this.drawCenteredString(fontRenderer, "Select a linked switch in the menu.", width / 2 + 60, height / 2 - 10, 0x5555FF);
		else {
			this.drawString(fontRenderer, "Name :", width / 2 - 40, height / 2 - 43, 0xffffff);
			this.drawString(fontRenderer, "Enabled :", width / 2 - 40, height / 2 - 18, 0xffffff);
			this.drawString(fontRenderer, "Power :", width / 2 - 40, height / 2 + 8, 0xffffff);
		}
		if (selected != -1) {
			selectedGSwitch.nameField.drawTextBox();
			selectedGSwitch.powerField.drawTextBox();
		}
		list.drawScreen(mouseX, mouseY, partialTicks);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (selected != -1) {
			selectedGSwitch.nameField.textboxKeyTyped(typedChar, keyCode);
			selectedGSwitch.powerField.textboxKeyTyped(typedChar, keyCode);
		}
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (selected != -1) {
			selectedGSwitch.nameField.mouseClicked(mouseX, mouseY, mouseButton);
			selectedGSwitch.powerField.mouseClicked(mouseX, mouseY, mouseButton);
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void updateScreen() {
		if (selected != -1) {
			selectedGSwitch.nameField.updateCursorCounter();
			selectedGSwitch.powerField.updateCursorCounter();
		}
	}

	private void selectSwitchIndex(int index) {
		if (index == this.selected)
			return;
		if (selected != -1)
			saveCache();
		this.selected = index;
		this.selectedSwitch = (index >= 0 && index <= switches.size()) ? switches.get(selected) : null;
		this.selectedGSwitch = (index >= 0 && index <= gswitches.size()) ? gswitches.get(selected) : null;
		this.selectedGSwitch.setVisible(true);
	}

	private boolean switchIndexSelected(int index) {
		return index == selected;
	}

	private void saveCache() {
		selectedGSwitch.setVisible(false);
		selectedSwitch.setName(selectedGSwitch.nameField.getText());
//		selectedSwitch.setEnabled(selectedGSwitch.enabledButton.value());
		selectedSwitch.setPower(selectedGSwitch.powerField.getInt());
	}

	private class GuiSlotSwitchList extends GuiScrollingList {

		public GuiSlotSwitchList(int listWidth, int slotHeight) {
			super(mc, listWidth, height, 60, height - 60, 20, slotHeight, width, height);
		}

		@Override
		protected int getSize() {
			return switches.size();
		}

		@Override
		protected void elementClicked(int index, boolean doubleClick) {
			selectSwitchIndex(index);
		}

		@Override
		protected boolean isSelected(int index) {
			return switchIndexSelected(index);
		}

		@Override
		protected void drawBackground() {
		}

		@Override
		protected int getContentHeight() {
			return (this.getSize()) * 20 + 1;
		}

		@Override
		protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
			MasterLogicalSwitch mls = switches.get(slotIdx);
			String name = StringUtils.stripControlCodes(mls.getName());
			drawCenteredString(fontRenderer, name, this.left + this.listWidth / 2 - 4, slotTop + 4,
					(mls.isEnabled()) ? 0x55FF55 : 0xFF5555);
		}

	}

	private class GraphicalSwitch {

		private boolean visible;
		private GuiRedstoneController parent;
		private GuiTextField nameField;
		private GuiOnOffButton enabledButton;
		private GuiIntTextField powerField;
		private GuiButton unlinkButton;

		private GraphicalSwitch(BaseLogicalSwitch ls, GuiRedstoneController parent, int delta) {
			this.parent = parent;
			this.nameField = new GuiTextField(1 + 10 * delta, fontRenderer, width / 2 + 40, height / 2 - 50, 100, 20);
			this.nameField.setText(ls.getName());
			this.enabledButton = new GuiOnOffButton(2 + 10 * delta, width / 2 + 75, height / 2 - 25, ls.isEnabled());
			this.powerField = new GuiIntTextField(3 + 10 * delta, fontRenderer, width / 2 + 80, height / 2, 20, 20, 15);
			this.powerField.setText(ls.getPower() + "");
			this.unlinkButton = new GuiButton(4 + 10 * delta, width / 2 + 65, height / 2 + 25, 50, 20, "§cUnlink");
			setVisible(false);
		}

		private void setVisible(boolean visible) {
			this.visible = visible;
			this.nameField.setVisible(visible);
			this.enabledButton.visible = visible;
			this.powerField.setVisible(visible);
			this.unlinkButton.visible = visible;
		}

	}

}
