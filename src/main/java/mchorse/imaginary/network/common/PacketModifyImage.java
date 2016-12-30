package mchorse.imaginary.network.common;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketModifyImage implements IMessage
{
    public int id;
    public String picture;

    public PacketModifyImage()
    {}

    public PacketModifyImage(int id, String picture)
    {
        this.id = id;
        this.picture = picture;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.id = buf.readInt();
        this.picture = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.id);
        ByteBufUtils.writeUTF8String(buf, this.picture);
    }
}