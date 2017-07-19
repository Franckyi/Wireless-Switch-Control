package com.github.franckyi.wrc.item;

import com.github.franckyi.wrc.WirelessRedstoneControl;
import com.github.franckyi.wrc.init.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ModCreativeTab extends CreativeTabs {

    public ModCreativeTab() {
        super(WirelessRedstoneControl.MODID);
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ModBlocks.CONTROLLER);
    }

}
