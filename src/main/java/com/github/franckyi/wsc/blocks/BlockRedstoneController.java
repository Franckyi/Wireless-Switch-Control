package com.github.franckyi.wsc.blocks;

import java.util.HashSet;
import java.util.Set;

import com.github.franckyi.wsc.WSCMod;
import com.github.franckyi.wsc.capability.RedstoneCapabilities;
import com.github.franckyi.wsc.capability.redstonelink.IRedstoneLink;
import com.github.franckyi.wsc.handlers.GuiHandler;
import com.github.franckyi.wsc.handlers.PacketHandler;
import com.github.franckyi.wsc.items.ItemRedstoneControllerUpgrade;
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
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockRedstoneController extends Block {

	public static final PropertyInteger UPGRADES = PropertyInteger.create("upgrades", 0, 15);

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
		setDefaultState(this.blockState.getBaseState().withProperty(UPGRADES, 0));
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
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { UPGRADES });
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityRedstoneController(state.getValue(UPGRADES));
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(UPGRADES);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(state));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(UPGRADES, meta);
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
		list.add(new ItemStack(itemIn, 1, 0));
		list.add(new ItemStack(itemIn, 1, 15));
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
				if (playerIn.getHeldItemMainhand().getItem() instanceof ItemRedstoneControllerUpgrade) {
					if (state.getValue(UPGRADES).intValue() != 15) {
						playerIn.getHeldItemMainhand().shrink(1);
						worldIn.setBlockState(pos, state.cycleProperty(UPGRADES));
						controller.get().upgrade();
						PacketHandler.INSTANCE.sendToAll(new UpdateRedstoneControllerMessage(pos, controller.get()));
						ChatUtil.sendSuccess(playerIn,
								"The controller has been upgraded to level " + (state.getValue(UPGRADES) + 2) + " on 16 !");
					} else
						ChatUtil.sendError(playerIn, "The controller is already fully upgraded !");
					return true;
				}
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
