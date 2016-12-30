package mchorse.imaginary.network;

import mchorse.imaginary.Imaginary;
import mchorse.imaginary.network.client.ClientHandlerModifyImage;
import mchorse.imaginary.network.common.PacketModifyImage;
import mchorse.imaginary.network.server.ServerHandlerModifyImage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Network dispatcher
 *
 * @author Ernio (Ernest Sadowski)
 */
public class Dispatcher
{
    private static final SimpleNetworkWrapper DISPATCHER = NetworkRegistry.INSTANCE.newSimpleChannel(Imaginary.MODID);
    private static byte PACKET_ID;

    public static SimpleNetworkWrapper get()
    {
        return DISPATCHER;
    }

    /**
     * Send message to players who are tracking given entity
     */
    public static void sendToTracked(Entity entity, IMessage message)
    {
        EntityTracker tracker = ((WorldServer) entity.worldObj).getEntityTracker();

        for (EntityPlayer player : tracker.getTrackingPlayers(entity))
        {
            DISPATCHER.sendTo(message, (EntityPlayerMP) player);
        }
    }

    /**
     * Send message to given player
     */
    public static void sendTo(IMessage message, EntityPlayerMP player)
    {
        DISPATCHER.sendTo(message, player);
    }

    /**
     * Send message to the server
     */
    public static void sendToServer(IMessage message)
    {
        DISPATCHER.sendToServer(message);
    }

    /**
     * Register all the networking messages and message handlers
     */
    public static void register()
    {
        register(PacketModifyImage.class, ClientHandlerModifyImage.class, Side.CLIENT);
        register(PacketModifyImage.class, ServerHandlerModifyImage.class, Side.SERVER);
    }

    /**
     * Register given message with given message handler on a given side
     */
    private static <REQ extends IMessage, REPLY extends IMessage> void register(Class<REQ> message, Class<? extends IMessageHandler<REQ, REPLY>> handler, Side side)
    {
        DISPATCHER.registerMessage(handler, message, PACKET_ID++, side);
    }
}