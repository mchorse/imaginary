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

    public float rotateX;
    public float rotateY;
    public float rotateZ;

    public boolean fitAABB;

    public PacketModifyImage()
    {}

    public PacketModifyImage(EntityImage image)
    {
        this.id = image.getEntityId();

        this.picture = image.getPicture();

        this.width = image.sizeW;
        this.height = image.sizeH;

        this.shiftX = image.shiftX;
        this.shiftY = image.shiftY;
        this.shiftZ = image.shiftZ;

        this.rotateX = image.rotationPitch;
        this.rotateY = image.rotationYaw;
        this.rotateZ = image.rotationRoll;

        this.fitAABB = image.fitAABB;
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
        this.rotateX = buf.readFloat();
        this.rotateY = buf.readFloat();
        this.rotateZ = buf.readFloat();
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
        buf.writeFloat(this.rotateX);
        buf.writeFloat(this.rotateY);
        buf.writeFloat(this.rotateZ);
        buf.writeBoolean(this.fitAABB);
    }
}