package com.github.franckyi.wsc.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiIntTextField extends GuiTextField {

	private int max;

	public GuiIntTextField(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
		super(componentId, fontrendererObj, x, y, par5Width, par6Height);
	}

	public GuiIntTextField(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height,
			int max) {
		this(componentId, fontrendererObj, x, y, par5Width, par6Height);
		this.max = max;
	}

	public int getInt() {
		if (getText().length() != 0)
			return Integer.parseInt(getText());
		return 0;
	}

	@Override
	public boolean textboxKeyTyped(char typedChar, int keyCode) {
		if (keyCode == 14 || keyCode == 203 || keyCode == 205 || keyCode == 211)
			return super.textboxKeyTyped(typedChar, keyCode);
		if (Character.isDigit(typedChar)) {
			this.setCursorPositionEnd();
			if (max == 0)
				return super.textboxKeyTyped(typedChar, keyCode);
			int cur = Integer.parseInt(getText().concat(String.valueOf(typedChar)));
			if (cur <= max)
				return super.textboxKeyTyped(typedChar, keyCode);
		}
		return false;
	}

}