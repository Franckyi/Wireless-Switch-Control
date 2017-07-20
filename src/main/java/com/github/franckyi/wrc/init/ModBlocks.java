package com.github.franckyi.wrc.init;

import com.github.franckyi.wrc.block.BlockController;
import com.github.franckyi.wrc.block.BlockBasicSensor;
import net.minecraft.block.Block;

public class ModBlocks {

    public static final Block CONTROLLER = new BlockController();
    public static final Block BASIC_SENSOR = new BlockBasicSensor();

    public static final Block[] BLOCKS = {CONTROLLER, BASIC_SENSOR};

}
