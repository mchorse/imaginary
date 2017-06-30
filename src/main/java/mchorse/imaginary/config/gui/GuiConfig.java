package mchorse.imaginary.config.gui;

import java.util.ArrayList;
import java.util.List;

import mchorse.imaginary.Imaginary;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Config GUI
 *
 * This config GUI is responsible for managing Blockbuster's config. Most of
 * the code that implements config features is located in the parent of the
 * class.
 */
@SideOnly(Side.CLIENT)
public class GuiConfig extends net.minecraftforge.fml.client.config.GuiConfig
{
    public GuiConfig(GuiScreen parent)
    {
        super(parent, getConfigElements(), Imaginary.MODID, false, false, "Imaginary");
    }

    private static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> elements = new ArrayList<IConfigElement>();

        for (Property prop : Imaginary.proxy.forge.getCategory(Configuration.CATEGORY_GENERAL).getOrderedValues())
        {
            elements.add(new ConfigElement(prop));
        }

        return elements;
    }
}