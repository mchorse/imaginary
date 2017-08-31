package mchorse.imaginary;

import java.io.File;

import mchorse.imaginary.config.ImaginaryConfig;
import mchorse.imaginary.entity.EntityImage;
import mchorse.imaginary.item.ItemImage;
import mchorse.imaginary.network.Dispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Common imaginary proxy 
 */
public class CommonProxy
{
    /**
     * Config
     */
    public ImaginaryConfig config;

    /**
     * Forge config
     */
    public Configuration forge;

    /**
     * Pre-load method stuff
     * 
     * This method is responsible for registering image entity and items 
     */
    public void preLoad(FMLPreInitializationEvent event)
    {
        Dispatcher.register();
        ForgeRegistries.ITEMS.register(Imaginary.imageItem = new ItemImage());

        /* Configuration */
        File config = new File(event.getModConfigurationDirectory(), "imaginary/config.cfg");

        this.forge = new Configuration(config);
        this.config = new ImaginaryConfig(this.forge);

        MinecraftForge.EVENT_BUS.register(this.config);

        EntityRegistry.registerModEntity(new ResourceLocation("imaginary:image"), EntityImage.class, "Image", 0, Imaginary.instance, 128, 5, false);
        NetworkRegistry.INSTANCE.registerGuiHandler(Imaginary.instance, new GuiHandler());
    }

    /**
     * Add a recipe for survival people 
     */
    public void load(FMLInitializationEvent event)
    {}
}