package com.github.franckyi.wrc.api;

import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class TileEntityDevice extends TileEntity implements ITickable {

    private Map<EnumFacing, Integer> transmittingPower = new HashMap<>();
    private Map<EnumFacing, Integer> receivingPower = new HashMap<>();

    public TileEntityDevice() {
        super();
        getTransmittingFaces().forEach(enumFacing -> transmittingPower.put(enumFacing, 0));
        getReceivingFaces().forEach(enumFacing -> receivingPower.put(enumFacing, 0));
    }

    public Map<EnumFacing, Integer> getTransmittingPower() {
        return transmittingPower;
    }

    public Map<EnumFacing, Integer> getReceivingPower() {
        return receivingPower;
    }

    public abstract Set<EnumFacing> getTransmittingFaces();

    public abstract Set<EnumFacing> getReceivingFaces();

    @Override
    public void update() {
        receivingPower.forEach((enumFacing, power) -> {
            int newPower = calculateInputStrength(world, pos, enumFacing);
            if(newPower != power) {
                receivingPower.put(enumFacing, newPower);
                blockUpdate();
            }
        });
    }

    protected int calculateInputStrength(World worldIn, BlockPos pos, EnumFacing enumFacing) {
        BlockPos blockpos = pos.offset(enumFacing);
        int i = worldIn.getRedstonePower(blockpos, enumFacing);
        if (i >= 15) {
            return i;
        } else {
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            return Math.max(i, iblockstate.getBlock() == Blocks.REDSTONE_WIRE ? iblockstate.getValue(BlockRedstoneWire.POWER) : 0);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        transmittingPower = tagListToMap(compound.getTagList("TransmittingPower", 10));
        receivingPower = tagListToMap(compound.getTagList("ReceivingPower", 10));
    }

    private NBTTagList mapToTagList(Map<EnumFacing, Integer> map) {
        NBTTagList r = new NBTTagList();
        map.forEach((key, value) -> {
            NBTTagCompound c = new NBTTagCompound();
            c.setInteger("Facing", key.getIndex());
            c.setInteger("Power", value);
            r.appendTag(c);
        });
        return r;
    }

    private Map<EnumFacing, Integer> tagListToMap(NBTTagList list) {
        Map<EnumFacing, Integer> map = new HashMap<>();
        list.forEach(nbtBase -> {
            NBTTagCompound c = (NBTTagCompound) nbtBase;
            map.put(EnumFacing.getFront(c.getInteger("Facing")), c.getInteger("Power"));
        });
        return map;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("TransmittingPower", mapToTagList(transmittingPower));
        compound.setTag("ReceivingPower", mapToTagList(receivingPower));
        return super.writeToNBT(compound);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    public void blockUpdate() {
        markDirty();
        if (world != null) {
            IBlockState state = world.getBlockState(getPos());
            world.notifyBlockUpdate(getPos(), state, state, 3);
        }
    }

}
