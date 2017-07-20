package com.github.franckyi.wrc.tileentity;

import com.github.franckyi.wrc.api.TileEntityBasicDevice;
import net.minecraft.util.EnumFacing;

import java.util.Set;

public class TileEntityBasicSensor extends TileEntityBasicDevice {

    @Override
    public Set<EnumFacing> getTransmittingFaces() {
        return FACES_NONE;
    }

    @Override
    public Set<EnumFacing> getReceivingFaces() {
        return FACES_HORIZONTALS;
    }
}
