package com.github.franckyi.wsc.logic;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class BaseRedstoneSwitch implements INBTSerializable<NBTTagCompound> {

	private String name;
	private boolean enabled;
	private int power;

	public BaseRedstoneSwitch() {
	}

	public BaseRedstoneSwitch(String name, boolean enabled, int power) {
		this.name = name;
		this.enabled = enabled;
		this.power = power;
	}

	@Override
	public void deserializeNBT(NBTTagCompound c) {
		setName(c.getString("name"));
		setEnabled(c.getBoolean("enabled"));
		setPower(c.getInteger("power"));
	}

	public String getName() {
		return name;
	}

	public int getPower() {
		return power;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound c = new NBTTagCompound();
		c.setString("name", getName());
		c.setBoolean("enabled", isEnabled());
		c.setInteger("power", getPower());
		return c;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPower(int power) {
		this.power = power;
	}

}
