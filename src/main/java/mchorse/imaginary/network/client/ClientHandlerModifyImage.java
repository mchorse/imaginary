package mchorse.imaginary.network.client;

import mchorse.imaginary.entity.EntityImage;
import mchorse.imaginary.network.common.PacketModifyImage;
import net.minecraft.client.entity.EntityPlayerSP;

public class ClientHandlerModifyImage extends ClientMessageHandler<PacketModifyImage>
{
    @Override
    public void run(EntityPlayerSP player, PacketModifyImage message)
    {
        EntityImage image = (EntityImage) player.worldObj.getEntityByID(message.id);

        image.modify(message.picture, message.width, message.height);
    }
}