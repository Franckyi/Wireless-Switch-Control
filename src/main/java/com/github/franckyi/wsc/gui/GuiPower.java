package com.github.franckyi.wsc.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

public class GuiPower {

	public class GuiPowerButton extends GuiButton {

		private boolean add;

		public GuiPowerButton(int id, int x, int y, boolean add) {
			super(id, x, y, 20, 20, add ? "+" : "-");
			this.add = add;
		}

		@Override
		public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
			if (super.mousePressed(mc, mouseX, mouseY)) {
				int nextVal = power.getInt() + (add ? 1 : -1);
				if(nextVal <= 15 && nextVal >= 0)
					power.setText(nextVal + "");
				return true;
			}
			return false;
		}

	}

	private GuiPowerButton add;
	private GuiIntTextField power;
	private GuiPowerButton substract;

	public GuiPower(int id, FontRenderer fontRenderer, int x, int y) {
		add = new GuiPowerButton(id, x, y, true);
		power = new GuiIntTextField(id + 1, fontRenderer, x + 25, y, 20, 20, 15);
		substract = new GuiPowerButton(id + 2, x + 50, y, false);
	}

	public GuiButton getAdd() {
		return add;
	}

	public GuiIntTextField getPower() {
		return power;
	}

	public GuiButton getSubstract() {
		return substract;
	}

}
