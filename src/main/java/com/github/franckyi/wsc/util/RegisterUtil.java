package com.github.franckyi.wsc.util;

import com.github.franckyi.wsc.capability.redstonecontroller.IRedstoneController;
import com.github.franckyi.wsc.capability.redstonecontroller.RedstoneController;
import com.github.franckyi.wsc.capability.redstonecontroller.RedstoneControllerStorage;
import com.github.franckyi.wsc.capability.redstonelink.IRedstoneLink;
import com.github.franckyi.wsc.capability.redstonelink.RedstoneLinkImpl;
import com.github.franckyi.wsc.capability.redstonelink.RedstoneLinkStorage;
import com.github.franckyi.wsc.capability.redstoneswitch.IRedstoneSwitch;
import com.github.franckyi.wsc.capability.redstoneswitch.RedstoneSwitchImpl;
import com.github.franckyi.wsc.capability.redstoneswitch.RedstoneSwitchStorage;
import com.github.franckyi.wsc.handlers.CapabilityHandler;
import com.github.franckyi.wsc.handlers.EventHandler;
import com.github.franckyi.wsc.handlers.PacketHandler;
import com.github.franckyi.wsc.init.ModBlocks;
import com.github.franckyi.wsc.network.RedstoneControllerDataMessage;
import com.github.franckyi.wsc.network.RedstoneControllerDataMessage.ControllerDataMessageHandler;
import com.github.franckyi.wsc.network.RedstoneSwitchDataMessage;
import com.github.franckyi.wsc.network.RedstoneSwitchDataMessage.SwitchDataMessageHandler;
import com.github.franckyi.wsc.network.RedstoneUnlinkingMessage;
import com.github.franckyi.wsc.network.RedstoneUnlinkingMessage.UnlinkingMessageHandler;
import com.github.franckyi.wsc.tileentity.TileEntityRedstoneController;
import com.github.franckyi.wsc.tileentity.TileEntityRedstoneSwitch;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
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
		CapabilityManager.INSTANCE.register(IRedstoneLink.class, new RedstoneLinkStorage(), RedstoneLinkImpl.class);
		CapabilityManager.INSTANCE.register(IRedstoneController.class, new RedstoneControllerStorage(),
				RedstoneController.class);
		CapabilityManager.INSTANCE.register(IRedstoneSwitch.class, new RedstoneSwitchStorage(),
				RedstoneSwitchImpl.class);
	}

	private static void registerEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	public static void registerInit(FMLInitializationEvent e) {
		registerEventHandlers();
		registerMessages();
		registerRecipes();
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
		PacketHandler.INSTANCE.registerMessage(SwitchDataMessageHandler.class, RedstoneSwitchDataMessage.class, 0,
				Side.CLIENT);
		PacketHandler.INSTANCE.registerMessage(SwitchDataMessageHandler.class, RedstoneSwitchDataMessage.class, 1,
				Side.SERVER);
		PacketHandler.INSTANCE.registerMessage(ControllerDataMessageHandler.class, RedstoneControllerDataMessage.class,
				2, Side.CLIENT);
		PacketHandler.INSTANCE.registerMessage(ControllerDataMessageHandler.class, RedstoneControllerDataMessage.class,
				3, Side.SERVER);
		PacketHandler.INSTANCE.registerMessage(UnlinkingMessageHandler.class, RedstoneUnlinkingMessage.class, 4,
				Side.SERVER);
	}

	public static void registerPreInit(FMLPreInitializationEvent e) {
		registerBlocks(e, ModBlocks.REDSTONE_CONTROLLER, ModBlocks.REDSTONE_SWITCH);
		registerItems(e);
		registerTileEntities();
		registerCapabilities();
	}

	private static void registerRecipes() {
		GameRegistry.addRecipe(new ItemStack(Item.getItemFromBlock(ModBlocks.REDSTONE_CONTROLLER)), "ABA", "CDC", "AEA",
				'A', Items.IRON_INGOT, 'B', Items.REPEATER, 'C', Blocks.REDSTONE_BLOCK, 'D', Blocks.IRON_BLOCK, 'E',
				Items.REDSTONE);
		GameRegistry.addRecipe(new ItemStack(Item.getItemFromBlock(ModBlocks.REDSTONE_SWITCH)), "ABA", "CDC", "ACA",
				'A', Items.IRON_INGOT, 'B', Blocks.LEVER, 'C', Items.REDSTONE, 'D', Blocks.REDSTONE_BLOCK);
	}

	private static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityRedstoneController.class, "controller_tile_entity");
		GameRegistry.registerTileEntity(TileEntityRedstoneSwitch.class, "switch_tile_entity");
	}

}
