package com.github.franckyi.wsc.waila;

import java.util.List;

import com.github.franckyi.wsc.capability.redstoneswitch.RedstoneSwitchProvider;
import com.github.franckyi.wsc.tileentity.TileEntityRedstoneSwitch;
import com.github.franckyi.wsc.util.SlaveRedstoneSwitch;

import mcp.mobius.waila.Waila;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "mcp.mobius.waila.api.IWailaDataProvider", modid = Waila.MODID)
public class RedstoneSwitchDataProvider implements IWailaDataProvider {

	public static final RedstoneSwitchDataProvider INSTANCE = new RedstoneSwitchDataProvider();

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world,
			BlockPos pos) {
		return tag;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		TileEntity tile = accessor.getTileEntity();
		if (tile instanceof TileEntityRedstoneSwitch) {
			SlaveRedstoneSwitch sls = tile.getCapability(RedstoneSwitchProvider.SWITCH_CAP, null).getSwitch();
			currenttip.add((sls.isLinked() ? "§a" : "§c") + "Linked : " + String.valueOf(sls.isLinked())
					+ (sls.isLinked() ? " (" + sls.getControllerPos().size() + ")§r" : "§r"));
			currenttip.add("Name : " + sls.getName());
			currenttip.add((sls.isEnabled() ? "§a" : "§c") + "Enabled : " + String.valueOf(sls.isEnabled()) + "§r");
			currenttip.add("Power : [" + String.valueOf(sls.getPower()) + "]");
		}
		return currenttip;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return null;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		return currenttip;
	}

}
