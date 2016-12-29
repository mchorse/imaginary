package mchorse.imaginary;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import mchorse.imaginary.client.PicturePack;
import mchorse.imaginary.client.render.RenderImage;
import mchorse.imaginary.entity.EntityImage;
import net.minecraft.client.resources.IResourcePack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Client proxy of Imaginary mod
 * 
 * This proxy is responsible for registering items, picture resource pack and 
 * image entity's renderer.
 */
public class ClientProxy extends CommonProxy
{
    /**
     * Pictures pack which is responsible for accessing the pictures 
     */
    public static PicturePack picturesPack;

    /**
     * Pre-load stuff on the client side 
     */
    @Override
    public void preLoad(FMLPreInitializationEvent event)
    {
        super.preLoad(event);

        RenderingRegistry.registerEntityRenderingHandler(EntityImage.class, new RenderImage.ImageFactory());

        this.injectResourcePack(event.getModConfigurationDirectory().toString());
    }

    /**
     * Inject actors skin pack into FML's resource packs list
     *
     * It's done by accessing private FMLClientHandler list (via reflection) and
     * appending actor pack.
     *
     * Thanks to diesieben07 for giving the idea.
     */
    @SuppressWarnings("unchecked")
    private void injectResourcePack(String path)
    {
        try
        {
            File pictures = new File(path, "imaginary/pictures/");
            Field field = FMLClientHandler.class.getDeclaredField("resourcePackList");
            field.setAccessible(true);

            List<IResourcePack> packs = (List<IResourcePack>) field.get(FMLClientHandler.instance());
            packs.add(picturesPack = new PicturePack(pictures));
            pictures.mkdirs();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}