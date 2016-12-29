package mchorse.imaginary;

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
@Mod(modid = Imaginary.MODID, version = Imaginary.VERSION)
public class Imaginary
{
    public static final String MODID = "imaginary";
    public static final String VERSION = "1.0";

    public static final String CLIENT_PROXY = "mchorse.imaginary.ClientProxy";
    public static final String SERVER_PROXY = "mchorse.imaginary.CommonProxy";

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
    public static CommonProxy proxy;
    @Mod.Instance
    public static Imaginary instance;

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