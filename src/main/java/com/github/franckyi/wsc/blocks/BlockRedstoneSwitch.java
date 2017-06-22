package com.github.franckyi.wsc.blocks;

import com.github.franckyi.wsc.capability.RedstoneCapabilities;
import com.github.franckyi.wsc.capability.redstonelink.IRedstoneLink;
import com.github.franckyi.wsc.handlers.PacketHandler;
import com.github.franckyi.wsc.network.RedstoneSwitchDataMessage;
import com.github.franckyi.wsc.network.RedstoneUnlinkingMessage;
import com.github.franckyi.wsc.tileentity.TileEntityRedstoneSwitch;
import com.github.franckyi.wsc.util.ChatUtil;
import com.github.franckyi.wsc.util.MasterRedstoneSwitch;
import com.github.franckyi.wsc.util.SlaveRedstoneSwitch;
import com.google.common.base.Optional;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

public class BlockRedstoneSwitch extends Block {

	public BlockRedstoneSwitch(String name, Material mat, CreativeTabs tab, float hardness, float resistance,
			String tool, int harvest, float light) {
		super(mat);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(tab);
		setHardness(hardness);
		setResistance(resistance);
		setHarvestLevel(tool, harvest);
		setLightLevel(light);
		isBlockContainer = true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		Optional<SlaveRedstoneSwitch> osls = RedstoneCapabilities.getSwitch(world, pos);
		if (osls.isPresent())
			for (BlockPos controller : RedstoneCapabilities.getSwitch(world, pos).get().getControllers())
				PacketHandler.INSTANCE.sendToServer(new RedstoneUnlinkingMessage(pos, controller));
		super.breakBlock(world, pos, state);
		world.removeTileEntity(pos);
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityRedstoneSwitch();
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		Optional<SlaveRedstoneSwitch> bls = RedstoneCapabilities.getSwitch(world, pos);
		if (bls.isPresent() && bls.get().isEnabled())
			return bls.get().getPower();
		return 0;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			Optional<SlaveRedstoneSwitch> osls = RedstoneCapabilities.getSwitch(worldIn, pos);
			if (osls.isPresent()) {
				if (playerIn.isSneaking()) {
					IRedstoneLink link = RedstoneCapabilities.getLink(playerIn);
					link.setSwitch(new MasterRedstoneSwitch(osls.get(), pos));
					ChatUtil.sendInfo(playerIn, "Switch selected.");
				} else
					PacketHandler.INSTANCE.sendTo(new RedstoneSwitchDataMessage(Side.SERVER, osls.get(), pos),
							(EntityPlayerMP) playerIn);
			} else
				ChatUtil.sendError(playerIn, "Unable to access the Capability.");
		}
		return true;
	}

	@Override
	public boolean shouldCheckWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}

}
