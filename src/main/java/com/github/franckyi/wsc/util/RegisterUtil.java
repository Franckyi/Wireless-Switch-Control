package com.github.franckyi.wsc.util;

import com.github.franckyi.wsc.capability.controllercap.ControllerImpl;
import com.github.franckyi.wsc.capability.controllercap.ControllerStorage;
import com.github.franckyi.wsc.capability.controllercap.IController;
import com.github.franckyi.wsc.capability.linkcap.ILink;
import com.github.franckyi.wsc.capability.linkcap.LinkImpl;
import com.github.franckyi.wsc.capability.linkcap.LinkStorage;
import com.github.franckyi.wsc.capability.switchcap.ISwitch;
import com.github.franckyi.wsc.capability.switchcap.SwitchImpl;
import com.github.franckyi.wsc.capability.switchcap.SwitchStorage;
import com.github.franckyi.wsc.handlers.CapabilityHandler;
import com.github.franckyi.wsc.handlers.EventHandler;
import com.github.franckyi.wsc.handlers.PacketHandler;
import com.github.franckyi.wsc.init.ModBlocks;
import com.github.franckyi.wsc.network.ControllerDataMessage;
import com.github.franckyi.wsc.network.ControllerDataMessage.ControllerDataMessageHandler;
import com.github.franckyi.wsc.network.SwitchDataMessage;
import com.github.franckyi.wsc.network.SwitchDataMessage.SwitchDataMessageHandler;
import com.github.franckyi.wsc.network.UnlinkingMessage;
import com.github.franckyi.wsc.network.UnlinkingMessage.UnlinkingMessageHandler;
import com.github.franckyi.wsc.tileentity.TileEntityController;
import com.github.franckyi.wsc.tileentity.TileEntitySwitch;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class RegisterUtil {

	private static void registerBlocks(FMLPreInitializationEvent e, Block... blocks) {
		for (Block block : blocks) {
			final ItemBlock itemblock = new ItemBlock(block);
			if (e.getSide() == Side.CLIENT) {
				GameRegistry.register(block);
				GameRegistry.register(itemblock, block.getRegistryName());
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0,
						new ModelResourceLocation(block.getRegistryName(), "inventory"));
			}
		}
	}

	private static void registerCapabilities() {
		CapabilityManager.INSTANCE.register(ILink.class, new LinkStorage(), LinkImpl.class);
		CapabilityManager.INSTANCE.register(IController.class, new ControllerStorage(), ControllerImpl.class);
		CapabilityManager.INSTANCE.register(ISwitch.class, new SwitchStorage(), SwitchImpl.class);
	}

	private static void registerEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	public static void registerInit(FMLInitializationEvent e) {
		registerEventHandlers();
		registerMessages();
	}

	private static void registerItems(FMLPreInitializationEvent e, Item... items) {
		for (Item item : items) {
			if (e.getSide() == Side.CLIENT) {
				GameRegistry.register(item);
				ModelLoader.setCustomModelResourceLocation(item, 0,
						new ModelResourceLocation(item.getRegistryName(), "inventory"));
			}
		}
	}

	private static void registerMessages() {
		PacketHandler.INSTANCE.registerMessage(SwitchDataMessageHandler.class, SwitchDataMessage.class, 0, Side.CLIENT);
		PacketHandler.INSTANCE.registerMessage(SwitchDataMessageHandler.class, SwitchDataMessage.class, 1, Side.SERVER);
		PacketHandler.INSTANCE.registerMessage(ControllerDataMessageHandler.class, ControllerDataMessage.class, 2,
				Side.CLIENT);
		PacketHandler.INSTANCE.registerMessage(ControllerDataMessageHandler.class, ControllerDataMessage.class, 3,
				Side.SERVER);
		PacketHandler.INSTANCE.registerMessage(UnlinkingMessageHandler.class, UnlinkingMessage.class, 4, Side.SERVER);
	}

	public static void registerPreInit(FMLPreInitializationEvent e) {
		registerBlocks(e, ModBlocks.REDSTONE_CONTROLLER, ModBlocks.REDSTONE_SWITCH);
		registerItems(e);
		registerTileEntities();
		registerCapabilities();
	}

	private static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityController.class, "controller_tile_entity");
		GameRegistry.registerTileEntity(TileEntitySwitch.class, "switch_tile_entity");
	}

}
