package com.github.franckyi.wsc.tileentity;

import java.util.ArrayList;

import com.github.franckyi.wsc.capability.redstonecontroller.IRedstoneController;
import com.github.franckyi.wsc.capability.redstonecontroller.RedstoneControllerProvider;
import com.github.franckyi.wsc.logic.BaseRedstoneController;
import com.github.franckyi.wsc.logic.MasterRedstoneSwitch;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityRedstoneController extends TileEntity {

	public TileEntityRedstoneController() {
		this(0);
	}
	
	public TileEntityRedstoneController(Integer upgrades) {
		setController(new BaseRedstoneController(new ArrayList<MasterRedstoneSwitch>(), 4 * (upgrades + 1)));
		markDirty();
	}

	public BaseRedstoneController getController() {
		IRedstoneController c = getCapability(RedstoneControllerProvider.CONTROLLER_CAP, null);
		if (c != null)
			return c.getController();
		return null;
	}

	public void setController(BaseRedstoneController controller) {
		IRedstoneController c = getCapability(RedstoneControllerProvider.CONTROLLER_CAP, null);
		if (c != null)
			c.setController(controller);

	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
	
	

}
