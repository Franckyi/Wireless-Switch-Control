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
import com.github.franckyi.wsc.init.ModItems;
import com.github.franckyi.wsc.network.RedstoneUnlinkingMessage;
import com.github.franckyi.wsc.network.UpdateRedstoneControllerMessage;
import com.github.franckyi.wsc.network.UpdateRedstoneSwitchMessage;
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
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class RegisterUtil {

	public static void registerRenders() {
		for (Block block : ModBlocks.BLOCKS)
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0,
					new ModelResourceLocation(block.getRegistryName(), "inventory"));
		for (Item item : ModItems.ITEMS)
			ModelLoader.setCustomModelResourceLocation(item, 0,
					new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

	public static void registerBlocks() {
		for (Block block : ModBlocks.BLOCKS) {
			final ItemBlock itemblock = new ItemBlock(block);
			itemblock.setRegistryName(block.getRegistryName());
			ForgeRegistries.BLOCKS.register(block);
			ForgeRegistries.ITEMS.register(itemblock);
		}
	}

	public static void registerCapabilities() {
		CapabilityManager.INSTANCE.register(IRedstoneLink.class, new RedstoneLinkStorage(), RedstoneLinkImpl.class);
		CapabilityManager.INSTANCE.register(IRedstoneController.class, new RedstoneControllerStorage(),
				RedstoneController.class);
		CapabilityManager.INSTANCE.register(IRedstoneSwitch.class, new RedstoneSwitchStorage(),
				RedstoneSwitchImpl.class);
	}

	public static void registerEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	public static void registerItems() {
		for (Item item : ModItems.ITEMS)
			ForgeRegistries.ITEMS.register(item);
	}

	public static void registerMessages() {
		PacketHandler.INSTANCE.registerMessage(RedstoneUnlinkingMessage.ServerHandler.class,
				RedstoneUnlinkingMessage.class, 0, Side.SERVER);
		PacketHandler.INSTANCE.registerMessage(UpdateRedstoneSwitchMessage.ServerHandler.class,
				UpdateRedstoneSwitchMessage.class, 1, Side.SERVER);
		PacketHandler.INSTANCE.registerMessage(UpdateRedstoneControllerMessage.ServerHandler.class,
				UpdateRedstoneControllerMessage.class, 2, Side.SERVER);

		PacketHandler.INSTANCE.registerMessage(UpdateRedstoneSwitchMessage.ClientHandler.class,
				UpdateRedstoneSwitchMessage.class, 3, Side.CLIENT);
		PacketHandler.INSTANCE.registerMessage(UpdateRedstoneControllerMessage.ClientHandler.class,
				UpdateRedstoneControllerMessage.class, 4, Side.CLIENT);
	}

	public static void registerRecipes() {
		GameRegistry.addRecipe(new ItemStack(Item.getItemFromBlock(ModBlocks.REDSTONE_CONTROLLER)), "ABA", "CDC", "AEA",
				'A', Items.IRON_INGOT, 'B', Items.REPEATER, 'C', Blocks.REDSTONE_BLOCK, 'D', Blocks.IRON_BLOCK, 'E',
				Items.REDSTONE);
		GameRegistry.addRecipe(new ItemStack(Item.getItemFromBlock(ModBlocks.REDSTONE_SWITCH)), "ABA", "CDC", "ACA",
				'A', Items.IRON_INGOT, 'B', Blocks.LEVER, 'C', Items.REDSTONE, 'D', Blocks.REDSTONE_BLOCK);
	}

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityRedstoneController.class, "controller_tile_entity");
		GameRegistry.registerTileEntity(TileEntityRedstoneSwitch.class, "switch_tile_entity");
	}

}
