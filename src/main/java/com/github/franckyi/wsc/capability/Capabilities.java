package com.github.franckyi.wsc.capability;

import java.util.Collections;
import java.util.List;

import com.github.franckyi.wsc.capability.linkcap.ILink;
import com.github.franckyi.wsc.capability.linkcap.LinkProvider;
import com.github.franckyi.wsc.tileentity.TileEntityController;
import com.github.franckyi.wsc.tileentity.TileEntitySwitch;
import com.github.franckyi.wsc.util.BaseLogicalSwitch;
import com.github.franckyi.wsc.util.MasterLogicalSwitch;
import com.github.franckyi.wsc.util.SlaveLogicalSwitch;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class Capabilities {

	public static List<MasterLogicalSwitch> getControllerSwitches(IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityController)
			return ((TileEntityController) te).getSwitches();
		return Collections.EMPTY_LIST;
	}

	public static ILink getLink(EntityPlayer player) {
		return player.getCapability(LinkProvider.LINK_CAP, null);
	}

	public static SlaveLogicalSwitch getSwitch(IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntitySwitch) {
			SlaveLogicalSwitch sls = ((TileEntitySwitch) te).getSwitch();
			return sls;
		}
		return null;
	}

	public static void setControllerSwitches(IBlockAccess world, BlockPos pos, List<MasterLogicalSwitch> switches) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityController) {
			((TileEntityController) te).setSwitches(switches);
			updateTileEntity(world, pos);
		}
	}

	public static void setSwitch(IBlockAccess world, BlockPos pos, SlaveLogicalSwitch sls) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntitySwitch) {
			((TileEntitySwitch) te).setSwitch(sls);
			updateTileEntity(world, pos);
		}
	}

	public static void updateTileEntity(IBlockAccess world, BlockPos pos) {
		world.getTileEntity(pos).markDirty();
	}

	public static void updateSwitch(IBlockAccess world, BlockPos pos, BaseLogicalSwitch ls1) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntitySwitch) {
			SlaveLogicalSwitch sls = ((TileEntitySwitch) te).getSwitch();
			((TileEntitySwitch) te).setSwitch(new SlaveLogicalSwitch(ls1, sls.getControllers()));
			updateTileEntity(world, pos);
		}
	}

}
