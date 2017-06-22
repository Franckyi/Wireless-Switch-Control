package com.github.franckyi.wsc.capability.redstoneswitch;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class RedstoneSwitchProvider implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(IRedstoneSwitch.class)
	public static final Capability<IRedstoneSwitch> SWITCH_CAP = null;

	private IRedstoneSwitch instance = SWITCH_CAP.getDefaultInstance();

	@Override
	public void deserializeNBT(NBTBase nbt) {
		SWITCH_CAP.getStorage().readNBT(SWITCH_CAP, this.instance, null, nbt);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == SWITCH_CAP ? SWITCH_CAP.<T>cast(this.instance) : null;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == SWITCH_CAP;
	}

	@Override
	public NBTBase serializeNBT() {
		return SWITCH_CAP.getStorage().writeNBT(SWITCH_CAP, this.instance, null);
	}

}
