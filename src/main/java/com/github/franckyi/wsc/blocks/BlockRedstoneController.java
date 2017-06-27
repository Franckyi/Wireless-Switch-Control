package com.github.franckyi.wsc.blocks;

import java.util.HashSet;
import java.util.Set;

import com.github.franckyi.wsc.WSCMod;
import com.github.franckyi.wsc.capability.RedstoneCapabilities;
import com.github.franckyi.wsc.capability.redstonelink.IRedstoneLink;
import com.github.franckyi.wsc.handlers.GuiHandler;
import com.github.franckyi.wsc.handlers.PacketHandler;
import com.github.franckyi.wsc.logic.BaseRedstoneController;
import com.github.franckyi.wsc.logic.FullRedstoneSwitch;
import com.github.franckyi.wsc.logic.MasterRedstoneSwitch;
import com.github.franckyi.wsc.logic.SlaveRedstoneSwitch;
import com.github.franckyi.wsc.network.UpdateRedstoneControllerMessage;
import com.github.franckyi.wsc.network.UpdateRedstoneSwitchMessage;
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
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

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
		if (controller.isPresent()) {
			Set<FullRedstoneSwitch> updateSwitches = new HashSet<FullRedstoneSwitch>();
			for (final MasterRedstoneSwitch controllerSwitch : controller.get().getSwitches()) {
				final Optional<SlaveRedstoneSwitch> s = RedstoneCapabilities.getSwitch(world,
						controllerSwitch.getSwitchPos());
				if (s.isPresent()) {
					if (s.get().getControllerPos().remove(pos))
						updateSwitches.add(new FullRedstoneSwitch(s.get(), controllerSwitch.getSwitchPos()));
				}
			}
			WorldServer worldServer = (WorldServer) world;
			for (EntityPlayer player : worldServer.playerEntities) {
				EntityPlayerMP playerMP = (EntityPlayerMP) player;
				if (worldServer.getPlayerChunkMap()
						.getEntry(world.getChunkFromBlockCoords(pos).x, world.getChunkFromBlockCoords(pos).z)
						.containsPlayer(playerMP))
					PacketHandler.INSTANCE.sendTo(new UpdateRedstoneSwitchMessage(updateSwitches), playerMP);
			}
		}
		super.breakBlock(world, pos, state);
		world.removeTileEntity(pos);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityRedstoneController();
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote && playerIn.isSneaking()) {
			Optional<BaseRedstoneController> controller = RedstoneCapabilities.getController(worldIn, pos);
			if (controller.isPresent()) {
				IRedstoneLink link = RedstoneCapabilities.getLink(playerIn);
				if (link.isPresent()) {
					if (controller.get().getSwitches().size() < controller.get().getMaxSize()) {
						for (MasterRedstoneSwitch s : controller.get().getSwitches())
							if (link.getSwitch().getSwitchPos().equals(s.getSwitchPos())) {
								ChatUtil.sendError(playerIn, "The switch is already linked to this controller !");
								return true;
							}
						Optional<SlaveRedstoneSwitch> s = RedstoneCapabilities.getSwitch(worldIn,
								link.getSwitch().getSwitchPos());
						if (s.isPresent()) {
							controller.get().getSwitches().add(link.getSwitch());
							PacketHandler.INSTANCE
									.sendToAll(new UpdateRedstoneControllerMessage(pos, controller.get()));
							s.get().getControllerPos().add(pos);
							PacketHandler.INSTANCE.sendToAll(
									new UpdateRedstoneSwitchMessage(link.getSwitch().getSwitchPos(), s.get()));
							link.reset();
							ChatUtil.sendSuccess(playerIn,
									"The switch '" + s.get().getName() + "' has been linked to this controller !");
						} else
							ChatUtil.sendError(playerIn,
									"Unable to get switch's data ! (it may have been broken during the linking process)");
					} else
						ChatUtil.sendError(playerIn, "The controller is full !");
				} else
					ChatUtil.sendError(playerIn, "You must select a switch first !");
			} else
				ChatUtil.sendError(playerIn, "Unable to get controller's data !");
		} else if (worldIn.isRemote && !playerIn.isSneaking()) {
			playerIn.openGui(WSCMod.instance, GuiHandler.REDSTONE_CONTROLLER_GUI, worldIn, pos.getX(), pos.getY(),
					pos.getZ());
		}
		return true;
	}

}
