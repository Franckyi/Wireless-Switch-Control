package com.github.franckyi.wrc.api;

import com.github.franckyi.wrc.item.ItemLinkingTool;
import com.github.franckyi.wrc.util.ChatUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockBasicDevice<TE extends TileEntityBasicDevice> extends BlockDevice<TE> {

    public BlockBasicDevice(Material materialIn) {
        super(materialIn);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote && !playerIn.isSneaking() && playerIn.getHeldItemMainhand().getItem() instanceof ItemLinkingTool) {
            if (getTileEntity(worldIn, pos).isLinked()) {
                ChatUtil.sendError(playerIn, "Device already linked !");
            } else {
                NBTTagCompound subCompound = playerIn.getHeldItemMainhand().getSubCompound("LinkData");
                if (subCompound == null) {
                    playerIn.getHeldItemMainhand().getOrCreateSubCompound("LinkData").setLong("Pos", pos.toLong());
                    ChatUtil.sendInfo(playerIn, "Device selected.");
                } else {
                    BlockPos linkedPos = BlockPos.fromLong(subCompound.getLong("Pos"));
                    if (pos.equals(linkedPos)) {
                        ChatUtil.sendError(playerIn, "Can't self-link device !");
                    } else {
                        if (getTileEntity(worldIn, linkedPos) != null) {
                            if (getTileEntity(worldIn, linkedPos).isLinked()) {
                                ChatUtil.sendError(playerIn, "Selected device already linked !");
                            } else {
                                if (canBeLinked(worldIn, pos, linkedPos)) {
                                    getTileEntity(worldIn, pos).setLinkedDevicePos(linkedPos);
                                    getTileEntity(worldIn, linkedPos).setLinkedDevicePos(pos);
                                    playerIn.getHeldItemMainhand().removeSubCompound("LinkData");
                                    ChatUtil.sendSuccess(playerIn, "Device linked !");
                                } else {
                                    ChatUtil.sendError(playerIn, "These devices can't be linked !");
                                }
                            }
                        } else {
                            playerIn.getHeldItemMainhand().removeSubCompound("LinkData");
                            ChatUtil.sendError(playerIn, "Can't find the selected device !");
                        }
                    }
                }
            }
        }
        return !playerIn.isSneaking();
    }

    private boolean canBeLinked(World worldIn, BlockPos pos1, BlockPos pos2) {
        TE te1 = getTileEntity(worldIn, pos1);
        TE te2 = getTileEntity(worldIn, pos2);
        return !te1.getReceivingFaces().isEmpty() && !te2.getTransmittingFaces().isEmpty() || !te1.getTransmittingFaces().isEmpty() && !te2.getReceivingFaces().isEmpty();
    }
}
