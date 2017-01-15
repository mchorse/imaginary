package mchorse.imaginary;

import mchorse.imaginary.entity.EntityImage;
import mchorse.imaginary.item.ItemImage;
import mchorse.imaginary.network.Dispatcher;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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
     * Add a recipe for survival people 
     */
    public void load(FMLInitializationEvent event)
    {
        ItemStack red = new ItemStack(Items.DYE, 1, 1);
        ItemStack green = new ItemStack(Items.DYE, 1, 2);
        ItemStack blue = new ItemStack(Items.DYE, 1, 12);

        GameRegistry.addRecipe(new ItemStack(Imaginary.imageItem), "PRP", "PGP", "PBP", 'P', Items.PAPER, 'R', red, 'G', green, 'B', blue);
    }
}