package com.github.franckyi.wrc.item;

import com.github.franckyi.wrc.WirelessRedstoneControl;
import com.github.franckyi.wrc.util.ChatUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemLinkingTool extends Item {

    public ItemLinkingTool() {
        super();
        setRegistryName("linking_tool");
        setUnlocalizedName("linking_tool");
        setCreativeTab(WirelessRedstoneControl.creativeTab);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote && playerIn.isSneaking()) {
            playerIn.getHeldItem(handIn).removeSubCompound("LinkData");
            ChatUtil.sendInfo(playerIn, "Selected device reset.");
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
