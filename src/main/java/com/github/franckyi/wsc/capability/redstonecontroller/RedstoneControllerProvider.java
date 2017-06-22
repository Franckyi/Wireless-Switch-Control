package com.github.franckyi.wsc.capability.redstonecontroller;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class RedstoneControllerProvider implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(IRedstoneController.class)
	public static final Capability<IRedstoneController> CONTROLLER_CAP = null;

	private IRedstoneController instance = CONTROLLER_CAP.getDefaultInstance();

	@Override
	public void deserializeNBT(NBTBase nbt) {
		CONTROLLER_CAP.getStorage().readNBT(CONTROLLER_CAP, this.instance, null, nbt);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == CONTROLLER_CAP ? CONTROLLER_CAP.<T>cast(this.instance) : null;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CONTROLLER_CAP;
	}

	@Override
	public NBTBase serializeNBT() {
		return CONTROLLER_CAP.getStorage().writeNBT(CONTROLLER_CAP, this.instance, null);
	}

}