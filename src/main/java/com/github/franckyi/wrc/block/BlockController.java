package com.github.franckyi.wrc.block;

import com.github.franckyi.wrc.WirelessRedstoneControl;
import com.github.franckyi.wrc.tileentity.TileEntityController;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockController extends Block {

    public BlockController() {
        super(Material.IRON);
        setRegistryName("controller");
        setUnlocalizedName("controller");
        setCreativeTab(WirelessRedstoneControl.creativeTab);
        setLightOpacity(255);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            getTileEntity(worldIn, pos).upgradeSize();
        }
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityController();
    }

    private TileEntityController getTileEntity(IBlockAccess world, BlockPos pos) {
        return (TileEntityController) world.getTileEntity(pos);
    }

}
