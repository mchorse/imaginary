package mchorse.imaginary;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Main entry point of Imaginary mod
 * 
 * This mod adds only one feature in the game, and it is ability to insert 
 * custom images (like paintings) into the game.
 * 
 * Animated GIFs are planned in the future.
 */
@Mod(name = Imaginary.MODNAME, modid = Imaginary.MODID, version = Imaginary.VERSION, guiFactory = Imaginary.GUI_FACTORY)
public class Imaginary
{
    public static final String MODNAME = "Imaginary";
    public static final String MODID = "imaginary";
    public static final String VERSION = "1.0.3";

    public static final String CLIENT_PROXY = "mchorse.imaginary.ClientProxy";
    public static final String SERVER_PROXY = "mchorse.imaginary.CommonProxy";
    public static final String GUI_FACTORY = "mchorse.imaginary.config.gui.GuiFactory";

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
    public static CommonProxy proxy;
    @Mod.Instance
    public static Imaginary instance;

    public static Item imageItem;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preLoad(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.load(event);
    }
}
