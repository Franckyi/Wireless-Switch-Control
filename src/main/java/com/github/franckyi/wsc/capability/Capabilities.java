package com.github.franckyi.wsc.capability;

import java.util.Collections;
import java.util.List;

import com.github.franckyi.wsc.capability.controllercap.ControllerProvider;
import com.github.franckyi.wsc.capability.linkcap.ILink;
import com.github.franckyi.wsc.capability.linkcap.LinkProvider;
import com.github.franckyi.wsc.capability.switchcap.SwitchProvider;
import com.github.franckyi.wsc.tileentity.TileEntityController;
import com.github.franckyi.wsc.tileentity.TileEntitySwitch;
import com.github.franckyi.wsc.util.BaseLogicalSwitch;
import com.github.franckyi.wsc.util.MasterLogicalSwitch;
import com.github.franckyi.wsc.util.SlaveLogicalSwitch;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Capabilities {

	public static List<MasterLogicalSwitch> getControllerSwitches(World world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityController)
			return ((TileEntityController) te).getSwitches();
		return Collections.EMPTY_LIST;
	}

	public static ILink getLink(EntityPlayer player) {
		return player.getCapability(LinkProvider.LINK_CAP, null);
	}

	public static SlaveLogicalSwitch getSwitch(World world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntitySwitch)
			return ((TileEntitySwitch) te).getSwitch();
		return null;
	}

	public static void setControllerSwitches(World world, BlockPos pos, List<MasterLogicalSwitch> switches) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityController) {
			((TileEntityController) te).setSwitches(switches);
			updateTileEntity(world, pos);
		}
	}

	public static void setSwitch(World world, BlockPos pos, SlaveLogicalSwitch sls) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntitySwitch) {
			((TileEntitySwitch) te).setSwitch(sls);
			updateTileEntity(world, pos);
		}
	}

	public static void updateTileEntity(World world, BlockPos pos) {
		world.getTileEntity(pos).markDirty();
	}

	public static void updateSwitch(World world, BlockPos pos, BaseLogicalSwitch ls1) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntitySwitch) {
			BaseLogicalSwitch ls = ((TileEntitySwitch) te).getSwitch();
			ls.setLinked(ls1.isEnabled());
			ls.setName(ls1.getName());
			ls.setEnabled(ls1.isEnabled());
			ls.setPower(ls1.getPower());
			updateTileEntity(world, pos);
		}
	}

}
