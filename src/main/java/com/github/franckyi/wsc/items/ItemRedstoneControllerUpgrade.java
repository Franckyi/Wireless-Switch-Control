package com.github.franckyi.wsc.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemRedstoneControllerUpgrade extends Item {
	
	public ItemRedstoneControllerUpgrade(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.REDSTONE);
	}

}
