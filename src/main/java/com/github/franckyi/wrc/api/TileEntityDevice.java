package com.github.franckyi.wrc.api;

import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class TileEntityDevice extends TileEntity implements ITickable {

    private int transmittingPower, receivingPower;

    public TileEntityDevice() {
        super();
    }

    public int getTransmittingPower() {
        return transmittingPower;
    }

    public void setTransmittingPower(int transmittingPower) {
        this.transmittingPower = transmittingPower;
        blockUpdate();
    }

    public int getReceivingPower() {
        return receivingPower;
    }

    public void setReceivingPower(int receivingPower) {
        this.receivingPower = receivingPower;
        blockUpdate();
    }

    public abstract Set<EnumFacing> getTransmittingFaces();

    public abstract Set<EnumFacing> getReceivingFaces();

    @Override
    public void update() {
        int power = 0;
        for(EnumFacing enumFacing : getReceivingFaces()) {
            int facingPower = calculateInputStrength(world, pos, enumFacing);
            if(facingPower > power) {
                power = facingPower;
            }
        }
        if(power != receivingPower) {
            receivingPower = power;
            blockUpdate();
        }
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
        transmittingPower = compound.getInteger("TransmittingPower");
        receivingPower = compound.getInteger("ReceivingPower");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("TransmittingPower", transmittingPower);
        compound.setInteger("ReceivingPower", receivingPower);
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

    protected final Set<EnumFacing> FACES_NONE = new HashSet<>();
    protected final Set<EnumFacing> FACES_ALL = new HashSet<>(Arrays.asList(EnumFacing.VALUES));
    protected final Set<EnumFacing> FACES_HORIZONTALS = new HashSet<>(Arrays.asList(EnumFacing.HORIZONTALS));

}
