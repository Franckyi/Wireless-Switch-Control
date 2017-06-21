package com.github.franckyi.wsc.handlers;

import com.github.franckyi.wsc.ModReference;
import com.github.franckyi.wsc.capability.controllercap.ControllerProvider;
import com.github.franckyi.wsc.capability.linkcap.LinkProvider;
import com.github.franckyi.wsc.capability.switchcap.SwitchProvider;
import com.github.franckyi.wsc.tileentity.TileEntityController;
import com.github.franckyi.wsc.tileentity.TileEntitySwitch;

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
			event.addCapability(LINK_CAP, new LinkProvider());
	}

	@SubscribeEvent
	public void attachCapabilityTileEntity(AttachCapabilitiesEvent<TileEntity> event) {
		if (event.getObject() instanceof TileEntitySwitch)
			event.addCapability(SWITCH_CAP, new SwitchProvider());
		else if (event.getObject() instanceof TileEntityController)
			event.addCapability(CONTROLLER_CAP, new ControllerProvider());

	}

}
