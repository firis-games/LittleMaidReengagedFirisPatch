package net.blacklab.lmc.common.tileentity;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class AbstractTileEntity extends TileEntity {
	
	/**
	 * ログインのタイミングの同期に使われてる？
	 * チャンクデータの同期のタイミングでつかわれてるっぽい
	 * SPacketChunkData
	 */
	@Override
    public NBTTagCompound getUpdateTag()
    {
		//NBTTagにTileEntityの情報を書き込む
		return this.writeToNBT(new NBTTagCompound());
    }
	
    /**
     * Called when the chunk's TE update tag, gotten from {@link #getUpdateTag()}, is received on the client.
     * <p>
     * Used to handle this tag in a special way. By default this simply calls {@link #readFromNBT(NBTTagCompound)}.
     *
     * @param tag The {@link NBTTagCompound} sent from {@link #getUpdateTag()}
     * 
     * チャンクのほう
     */
	@Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
        this.readFromNBT(tag);
    }
	
	/**
	 * 旧getDescriptionPacketっぽい
	 */
	@Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
		return new SPacketUpdateTileEntity(this.pos, 0, this.writeToNBT(new NBTTagCompound()));
    }

    /**
     * Called when you receive a TileEntityData packet for the location this
     * TileEntity is currently in. On the client, the NetworkManager will always
     * be the remote server. On the server, it will be whomever is responsible for
     * sending the packet.
     *
     * @param net The NetworkManager the packet originated from
     * @param pkt The data packet
     */
	/**
	 * クライアントサイドでgetDescriptionPacketの処理のあとに呼び出されるとおもわれる
	 * NBTからクラス情報への反映等を行う?
	 * 
	 * 
	 * コメントによるとパケットを受け取ったタイミングで処理が行われるらしい
	 */
	@Override
    public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity pkt)
    {
		this.readFromNBT(pkt.getNbtCompound());
    }
}
