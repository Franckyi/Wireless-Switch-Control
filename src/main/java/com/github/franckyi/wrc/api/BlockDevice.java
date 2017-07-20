package com.github.franckyi.wrc.api;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class BlockDevice<TE extends TileEntityDevice> extends Block {

    public BlockDevice(Material materialIn) {
        super(materialIn);
        isBlockContainer = true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public final TileEntity createTileEntity(World world, IBlockState state) {
        return createTileEntityDevice(world, state);
    }

    protected abstract TE createTileEntityDevice(World world, IBlockState state);

    protected TE getTileEntity(IBlockAccess world, BlockPos pos) {
        return (TE) world.getTileEntity(pos);
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
        return side != null && (getTileEntity(world, pos).getReceivingFaces().contains(side) || getTileEntity(world, pos).getTransmittingFaces().contains(side));
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return getTileEntity(blockAccess, pos).getTransmittingPower();
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public boolean shouldCheckWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return false;
    }
}
