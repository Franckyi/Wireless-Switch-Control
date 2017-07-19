package com.github.franckyi.wrc.init;

import com.github.franckyi.wrc.WirelessRedstoneControl;
import com.github.franckyi.wrc.tileentity.TileEntityController;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class ModInitialization {

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        WirelessRedstoneControl.logger.info("Registring Blocks");
        event.getRegistry().registerAll(ModBlocks.BLOCKS);
        registerTileEntities();
    }

    private static void registerTileEntities() {
        WirelessRedstoneControl.logger.info("Registring Tile Entities");
        GameRegistry.registerTileEntity(TileEntityController.class, "controller");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        WirelessRedstoneControl.logger.info("Registring Items");
        event.getRegistry().registerAll(ModItems.ITEMS);
        for (Block block : ModBlocks.BLOCKS) {
            event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }
    }

    @SideOnly(value = Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        WirelessRedstoneControl.logger.info("Registring Models");
        for (Block block : ModBlocks.BLOCKS) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
        }
        for (Item item : ModItems.ITEMS) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }
    }

}
