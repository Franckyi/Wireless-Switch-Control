package com.github.franckyi.wsc.blocks;

import java.util.Arrays;
import java.util.List;

import com.github.franckyi.wsc.capability.RedstoneCapabilities;
import com.github.franckyi.wsc.capability.redstonelink.IRedstoneLink;
import com.github.franckyi.wsc.handlers.PacketHandler;
import com.github.franckyi.wsc.logic.BaseRedstoneController;
import com.github.franckyi.wsc.logic.MasterRedstoneSwitch;
import com.github.franckyi.wsc.logic.SlaveRedstoneSwitch;
import com.github.franckyi.wsc.network.RedstoneControllerDataMessage;
import com.github.franckyi.wsc.network.RedstoneSwitchDataMessage;
import com.github.franckyi.wsc.network.RedstoneUnlinkingMessage;
import com.github.franckyi.wsc.tileentity.TileEntityRedstoneController;
import com.github.franckyi.wsc.util.ChatUtil;
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
		Optional<BaseRedstoneController> controller = RedstoneCapabilities.getController(world, pos);
		if(controller.isPresent())
			for (MasterRedstoneSwitch mls : controller.get().getSwitches())
				PacketHandler.INSTANCE.sendToServer(new RedstoneUnlinkingMessage(mls.getSwitchPos(), pos));
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
			Optional<BaseRedstoneController> controller = RedstoneCapabilities.getController(worldIn, pos);
			if (playerIn.isSneaking()) {
				IRedstoneLink link = RedstoneCapabilities.getLink(playerIn);
				if (link.isPresent() && controller.isPresent()) {
					for (MasterRedstoneSwitch mls : controller.get().getSwitches())
						if (mls.getSwitchPos().equals(link.getSwitch().getSwitchPos())) {
							ChatUtil.sendError(playerIn, "The switch is already linked to this controller !");
							return true;
						}
					if (controller.get().getSwitches().size() < 4) {
						controller.get().getSwitches().add(link.getSwitch());
						Optional<SlaveRedstoneSwitch> osls = RedstoneCapabilities.getSwitch(worldIn,
								link.getSwitch().getSwitchPos());
						if (osls.isPresent()) {
							osls.get().getControllerPos().add(pos);
							RedstoneCapabilities.updateTileEntity(worldIn, link.getSwitch().getSwitchPos());
							PacketHandler.INSTANCE.sendToAll(new RedstoneSwitchDataMessage(Side.SERVER, osls.get(),
									link.getSwitch().getSwitchPos(), false));
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
				PacketHandler.INSTANCE.sendTo(new RedstoneControllerDataMessage(Side.SERVER, controller.get().getSwitches(), pos, true),
						(EntityPlayerMP) playerIn);
		}
		return true;
	}

}
