package mchorse.imaginary.network.common;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketModifyImage implements IMessage
{
    public int id;
    public String picture;
    public float width;
    public float height;

    public PacketModifyImage()
    {}

    public PacketModifyImage(int id, String picture, float width, float height)
    {
        this.id = id;
        this.picture = picture;
        this.width = width;
        this.height = height;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.id = buf.readInt();
        this.picture = ByteBufUtils.readUTF8String(buf);
        this.width = buf.readFloat();
        this.height = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.id);
        ByteBufUtils.writeUTF8String(buf, this.picture);
        buf.writeFloat(this.width);
        buf.writeFloat(this.height);
    }
}