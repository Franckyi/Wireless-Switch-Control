package com.github.franckyi.wsc.capability;

import java.util.Collections;
import java.util.List;

import com.github.franckyi.wsc.capability.redstonelink.IRedstoneLink;
import com.github.franckyi.wsc.capability.redstonelink.RedstoneLinkProvider;
import com.github.franckyi.wsc.logic.BaseRedstoneController;
import com.github.franckyi.wsc.logic.BaseRedstoneSwitch;
import com.github.franckyi.wsc.logic.MasterRedstoneSwitch;
import com.github.franckyi.wsc.logic.SlaveRedstoneSwitch;
import com.github.franckyi.wsc.tileentity.TileEntityRedstoneController;
import com.github.franckyi.wsc.tileentity.TileEntityRedstoneSwitch;
import com.google.common.base.Optional;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class RedstoneCapabilities {

	public static Optional<BaseRedstoneController> getController(IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityRedstoneController)
			return Optional.of(((TileEntityRedstoneController) te).getController());
		return Optional.absent();
	}

	public static IRedstoneLink getLink(EntityPlayer player) {
		return player.getCapability(RedstoneLinkProvider.LINK_CAP, null);
	}

	public static Optional<SlaveRedstoneSwitch> getSwitch(IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityRedstoneSwitch) {
			SlaveRedstoneSwitch sls = ((TileEntityRedstoneSwitch) te).getSwitch();
			return Optional.of(sls);
		}
		return Optional.absent();
	}

	public static void setController(IBlockAccess world, BlockPos pos, BaseRedstoneController controller) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityRedstoneController)
			((TileEntityRedstoneController) te).setController(controller);
	}

	public static void setSwitch(IBlockAccess world, BlockPos pos, SlaveRedstoneSwitch sls) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityRedstoneSwitch)
			((TileEntityRedstoneSwitch) te).setSwitch(sls);
	}
//
//	public static void updateSwitch(IBlockAccess world, BlockPos pos, BaseRedstoneSwitch ls1) {
//		TileEntity te = world.getTileEntity(pos);
//		if (te != null && te instanceof TileEntityRedstoneSwitch) {
//			SlaveRedstoneSwitch sls = ((TileEntityRedstoneSwitch) te).getSwitch();
//			((TileEntityRedstoneSwitch) te).setSwitch(new SlaveRedstoneSwitch(ls1, sls.getControllerPos()));
//			updateTileEntity(world, pos);
//		}
//	}
//
//	public static void updateTileEntity(IBlockAccess world, BlockPos pos) {
//		world.getTileEntity(pos).markDirty();
//	}

}
