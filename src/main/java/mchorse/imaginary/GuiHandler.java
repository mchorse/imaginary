package mchorse.imaginary;

import mchorse.imaginary.client.gui.GuiPicture;
import mchorse.imaginary.entity.EntityImage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * Gui handler class
 *
 * This class is responsible for opening GUIs.
 */
public class GuiHandler implements IGuiHandler
{
    /* GUI ids */
    public static final int PICTURE = 0;

    /**
     * Shortcut for {@link EntityPlayer#openGui(Object, int, World, int, int, int)}
     */
    public static void open(EntityPlayer player, int ID, int x, int y, int z)
    {
        player.openGui(Imaginary.instance, ID, player.world, x, y, z);
    }

    /**
     * There's two types of GUI are available right now:
     *
     * - Actor configuration GUI
     * - Director block management GUIs
     * - Director map block management GUI
     *
     * IGuiHandler is used to centralize GUI invocations
     */
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        Entity entity = world.getEntityByID(x);

        if (ID == PICTURE)
        {
            return new GuiPicture((EntityImage) entity);
        }

        return null;
    }

    /**
     * This method is empty, because there's no need for this method to be
     * filled with code. This mod doesn't seem to provide any interaction with
     * Containers.
     */
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }
}