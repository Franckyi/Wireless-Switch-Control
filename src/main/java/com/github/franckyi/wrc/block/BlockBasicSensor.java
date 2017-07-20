package com.github.franckyi.wrc.block;

import com.github.franckyi.wrc.WirelessRedstoneControl;
import com.github.franckyi.wrc.api.BlockBasicDevice;
import com.github.franckyi.wrc.tileentity.TileEntityBasicSensor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class BlockBasicSensor extends BlockBasicDevice<TileEntityBasicSensor> {

    public BlockBasicSensor() {
        super(Material.IRON);
        setRegistryName("basic_sensor");
        setUnlocalizedName("basic_sensor");
        setCreativeTab(WirelessRedstoneControl.creativeTab);
    }


    @Override
    protected TileEntityBasicSensor createTileEntityDevice(World world, IBlockState state) {
        return new TileEntityBasicSensor();
    }
}
