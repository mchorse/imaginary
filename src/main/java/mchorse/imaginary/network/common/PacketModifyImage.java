package mchorse.imaginary.network.common;

import io.netty.buffer.ByteBuf;
import mchorse.imaginary.entity.EntityImage;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketModifyImage implements IMessage
{
    public int id;

    public String picture;

    public float width;
    public float height;

    public float shiftX;
    public float shiftY;
    public float shiftZ;

    public boolean fitAABB;

    public PacketModifyImage()
    {}

    public PacketModifyImage(int id, String picture, float width, float height, float shiftX, float shiftY, float shiftZ, boolean fitAABB)
    {
        this.id = id;

        this.picture = picture;

        this.width = width;
        this.height = height;

        this.shiftX = shiftX;
        this.shiftY = shiftY;
        this.shiftZ = shiftZ;

        this.fitAABB = fitAABB;
    }

    public PacketModifyImage(EntityImage image)
    {
        this(image.getEntityId(), image.getPicture(), image.sizeW, image.sizeH, image.shiftX, image.shiftY, image.shiftZ, image.fitAABB);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.id = buf.readInt();
        this.picture = ByteBufUtils.readUTF8String(buf);
        this.width = buf.readFloat();
        this.height = buf.readFloat();
        this.shiftX = buf.readFloat();
        this.shiftY = buf.readFloat();
        this.shiftZ = buf.readFloat();
        this.fitAABB = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.id);
        ByteBufUtils.writeUTF8String(buf, this.picture);
        buf.writeFloat(this.width);
        buf.writeFloat(this.height);
        buf.writeFloat(this.shiftX);
        buf.writeFloat(this.shiftY);
        buf.writeFloat(this.shiftZ);
        buf.writeBoolean(this.fitAABB);
    }
}