package com.github.franckyi.wrc.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public abstract class TileEntityBasicDevice extends TileEntityDevice {

    private BlockPos linkedDevicePos;

    public boolean isLinked() {
        return linkedDevicePos != null;
    }

    @Nullable
    public BlockPos getLinkedDevicePos() {
        return linkedDevicePos;
    }

    @Nullable
    private TileEntityBasicDevice getLinkedDevice() {
        return isLinked() ? (TileEntityBasicDevice) world.getTileEntity(linkedDevicePos) : null;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("LinkedDevicePos")) {
            linkedDevicePos = BlockPos.fromLong(compound.getLong("LinkedDevicePos"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        if (isLinked()) {
            compound.setLong("LinkedDevicePos", linkedDevicePos.toLong());
        }
        return super.writeToNBT(compound);
    }

    public void setLinkedDevicePos(BlockPos linkedDevicePos) {
        this.linkedDevicePos = linkedDevicePos;
        blockUpdate();
    }
}
