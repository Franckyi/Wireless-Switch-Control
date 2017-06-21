package com.github.franckyi.wsc.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiOnOffButton extends GuiButton {

	private boolean on;
	
	public GuiOnOffButton(int buttonId, int x, int y, boolean on) {
		super(buttonId, x, y, 30, 20, on ? "§aON" : "§cOFF");
		this.on = on;
	}

	public boolean value() {
		return on;
	}

	public void set(boolean on) {
		this.on = on;
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		if(this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height) {
			on = !on;
			displayString = on ? "§aON" : "§cOFF";
		}
		return super.mousePressed(mc, mouseX, mouseY);
	}

	public void setOnAction(Object object) {
		// TODO Auto-generated method stub
		
	}

}
