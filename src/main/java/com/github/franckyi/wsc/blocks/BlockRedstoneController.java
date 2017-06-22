package com.github.franckyi.wsc.blocks;

import java.util.Arrays;
import java.util.List;

import com.github.franckyi.wsc.capability.RedstoneCapabilities;
import com.github.franckyi.wsc.capability.redstonelink.IRedstoneLink;
import com.github.franckyi.wsc.handlers.PacketHandler;
import com.github.franckyi.wsc.network.RedstoneControllerDataMessage;
import com.github.franckyi.wsc.network.RedstoneUnlinkingMessage;
import com.github.franckyi.wsc.tileentity.TileEntityRedstoneController;
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
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

public class BlockRedstoneController extends Block {

	public static ItemStack tileEntityToItemStack(ItemStack is, TileEntityRedstoneController te) {
		return is;
	}

	public BlockRedstoneController(String name, Material mat, CreativeTabs tab, float hardness, float resistance,
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
		List<MasterRedstoneSwitch> switches = RedstoneCapabilities.getControllerSwitches(world, pos);
		for (MasterRedstoneSwitch mls : switches)
			PacketHandler.INSTANCE.sendToServer(new RedstoneUnlinkingMessage(mls.getPos(), pos));
		super.breakBlock(world, pos, state);
		world.removeTileEntity(pos);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityRedstoneController();
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		TileEntityRedstoneController te = world.getTileEntity(pos) instanceof TileEntityRedstoneController
				? (TileEntityRedstoneController) world.getTileEntity(pos)
				: null;
		if (te != null)
			return Arrays.asList(tileEntityToItemStack(new ItemStack(state.getBlock()), te));
		return super.getDrops(world, pos, state, fortune);

	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			List<MasterRedstoneSwitch> list = RedstoneCapabilities.getControllerSwitches(worldIn, pos);
			if (playerIn.isSneaking()) {
				IRedstoneLink link = RedstoneCapabilities.getLink(playerIn);
				if (link.isPresent()) {
					for (MasterRedstoneSwitch mls : list)
						if (mls.getPos().equals(link.getSwitch().getPos())) {
							ChatUtil.sendError(playerIn, "The switch is already linked to this controller !");
							return true;
						}
					if (list.size() < 4) {
						link.getSwitch().setLinked(true);
						list.add(link.getSwitch());
						Optional<SlaveRedstoneSwitch> osls = RedstoneCapabilities.getSwitch(worldIn, link.getSwitch().getPos());
						if (osls.isPresent()) {
							osls.get().getControllers().add(pos);
							osls.get().setLinked(true);
							RedstoneCapabilities.updateTileEntity(worldIn, link.getSwitch().getPos());
							RedstoneCapabilities.getLink(playerIn).reset();
							ChatUtil.sendSuccess(playerIn,
									"The switch has been successfully linked to this controller !");
						} else
							ChatUtil.sendError(playerIn, "Unable to access the Capability.");
					} else
						ChatUtil.sendError(playerIn, "The controller is full !");
				} else
					ChatUtil.sendError(playerIn, "You must select a switch first.");

			} else
				PacketHandler.INSTANCE.sendTo(new RedstoneControllerDataMessage(Side.SERVER, list, pos),
						(EntityPlayerMP) playerIn);
		}
		return true;
	}

}
