package com.github.franckyi.wsc.capability.linkcap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class LinkProvider implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(ILink.class)
	public static final Capability<ILink> LINK_CAP = null;

	private ILink instance = LINK_CAP.getDefaultInstance();

	@Override
	public void deserializeNBT(NBTBase nbt) {
		LINK_CAP.getStorage().readNBT(LINK_CAP, this.instance, null, nbt);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == LINK_CAP ? LINK_CAP.<T>cast(this.instance) : null;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == LINK_CAP;
	}

	@Override
	public NBTBase serializeNBT() {
		return LINK_CAP.getStorage().writeNBT(LINK_CAP, this.instance, null);
	}

}
