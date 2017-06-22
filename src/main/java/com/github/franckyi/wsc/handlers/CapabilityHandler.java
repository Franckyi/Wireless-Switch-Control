package com.github.franckyi.wsc.handlers;

import com.github.franckyi.wsc.ModReference;
import com.github.franckyi.wsc.capability.redstonecontroller.RedstoneControllerProvider;
import com.github.franckyi.wsc.capability.redstonelink.RedstoneLinkProvider;
import com.github.franckyi.wsc.capability.redstoneswitch.RedstoneSwitchProvider;
import com.github.franckyi.wsc.tileentity.TileEntityRedstoneController;
import com.github.franckyi.wsc.tileentity.TileEntityRedstoneSwitch;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CapabilityHandler {

	public static final ResourceLocation LINK_CAP = new ResourceLocation(ModReference.MODID, "link");
	public static final ResourceLocation SWITCH_CAP = new ResourceLocation(ModReference.MODID, "switch");
	public static final ResourceLocation CONTROLLER_CAP = new ResourceLocation(ModReference.MODID, "controller");

	@SubscribeEvent
	public void attachCapabilityEntity(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityPlayer)
			event.addCapability(LINK_CAP, new RedstoneLinkProvider());
	}

	@SubscribeEvent
	public void attachCapabilityTileEntity(AttachCapabilitiesEvent<TileEntity> event) {
		if (event.getObject() instanceof TileEntityRedstoneSwitch)
			event.addCapability(SWITCH_CAP, new RedstoneSwitchProvider());
		else if (event.getObject() instanceof TileEntityRedstoneController)
			event.addCapability(CONTROLLER_CAP, new RedstoneControllerProvider());

	}

}
