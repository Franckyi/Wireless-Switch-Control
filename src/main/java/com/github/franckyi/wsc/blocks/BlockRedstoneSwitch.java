package com.github.franckyi.wsc.blocks;

import com.github.franckyi.wsc.capability.Capabilities;
import com.github.franckyi.wsc.capability.linkcap.ILink;
import com.github.franckyi.wsc.handlers.PacketHandler;
import com.github.franckyi.wsc.network.SwitchDataMessage;
import com.github.franckyi.wsc.network.UnlinkingMessage;
import com.github.franckyi.wsc.tileentity.TileEntitySwitch;
import com.github.franckyi.wsc.util.ChatUtil;
import com.github.franckyi.wsc.util.MasterLogicalSwitch;
import com.github.franckyi.wsc.util.SlaveLogicalSwitch;
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
		Optional<SlaveLogicalSwitch> osls = Capabilities.getSwitch(world, pos);
		if (osls.isPresent())
			for (BlockPos controller : Capabilities.getSwitch(world, pos).get().getControllers())
				PacketHandler.INSTANCE.sendToServer(new UnlinkingMessage(pos, controller));
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
		return new TileEntitySwitch();
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		Optional<SlaveLogicalSwitch> bls = Capabilities.getSwitch(world, pos);
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
			Optional<SlaveLogicalSwitch> osls = Capabilities.getSwitch(worldIn, pos);
			if (osls.isPresent()) {
				if (playerIn.isSneaking()) {
					ILink link = Capabilities.getLink(playerIn);
					link.setSwitch(new MasterLogicalSwitch(osls.get(), pos));
					ChatUtil.sendInfo(playerIn, "Switch selected.");
				} else
					PacketHandler.INSTANCE.sendTo(new SwitchDataMessage(Side.SERVER, osls.get(), pos),
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
