package mchorse.imaginary;

import mchorse.imaginary.entity.EntityImage;
import mchorse.imaginary.item.ItemImage;
import mchorse.imaginary.network.Dispatcher;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Common imaginary proxy 
 */
public class CommonProxy
{
    /**
     * Pre-load method stuff
     * 
     * This method is responsible for registering image entity and items 
     */
    public void preLoad(FMLPreInitializationEvent event)
    {
        Dispatcher.register();
        GameRegistry.register(Imaginary.imageItem = new ItemImage());

        EntityRegistry.registerModEntity(EntityImage.class, "Image", 0, Imaginary.instance, 128, 5, false);
        NetworkRegistry.INSTANCE.registerGuiHandler(Imaginary.instance, new GuiHandler());
    }

    /**
     * Looks like we don't need this method, but I'll leave it just in case. 
     */
    public void load(FMLInitializationEvent event)
    {}
}