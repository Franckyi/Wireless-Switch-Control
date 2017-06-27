package com.github.franckyi.wsc.init;

import com.github.franckyi.wsc.blocks.BlockRedstoneController;
import com.github.franckyi.wsc.blocks.BlockRedstoneSwitch;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class ModBlocks {

	public static final Block REDSTONE_CONTROLLER = new BlockRedstoneController("redstone_controller", Material.IRON,
			CreativeTabs.REDSTONE, 5F, 15F, "pickaxe", 1, 0.3F);
	public static final Block REDSTONE_SWITCH = new BlockRedstoneSwitch("redstone_switch", Material.IRON,
			CreativeTabs.REDSTONE, 5F, 15F, "pickaxe", 1, 0.3F);
	
	public static final Block[] BLOCKS = {REDSTONE_CONTROLLER, REDSTONE_SWITCH};

}
