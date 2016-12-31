package mchorse.imaginary.client.gui;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import mchorse.imaginary.ClientProxy;
import mchorse.imaginary.ImageUtils;
import mchorse.imaginary.client.render.RenderImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

/**
 * Pictures GUI picker
 * 
 * This class is responsible for displaying pictures and then 
 */
public class GuiPictures extends GuiScreen
{
    /* Other stuff */
    public List<ImageInfo> images = new ArrayList<ImageInfo>();
    public IPicturePicker listener;
    public int selected = -1;

    /* Space information */
    public int x;
    public int y;
    public int w;
    public int h;

    /** 
     * Initiate this GUI container
     * 
     * Cache all available pictures for later rendering and stuff.
     */
    public GuiPictures(String current)
    {
        this.images.add(new ImageInfo("", RenderImage.DEFAULT_TEXTURE, new Dimension(16, 16)));
        int i = 1;

        for (File file : ClientProxy.picturesPack.pictures.listFiles())
        {
            String name = file.getName();
            String ext = FilenameUtils.getExtension(file.getName());

            if (ext.equals("png") || ext.equals("gif") || ext.equals("jpeg") || ext.equals("jpg"))
            {
                try
                {
                    this.images.add(new ImageInfo(name, ImageUtils.getImageDimension(file)));

                    if (name.equals(current))
                    {
                        this.selected = i;
                    }

                    i++;
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get selected image's info
     */
    public ImageInfo getSelected()
    {
        if (this.selected >= 0 && this.selected < this.images.size())
        {
            return this.images.get(this.selected);
        }

        return this.images.get(0);
    }

    /**
     * Set selected image 
     */
    public void setSelected(String picture)
    {
        int i = 0;

        for (ImageInfo info : this.images)
        {
            if (info.filename.equals(picture))
            {
                this.selected = i;

                break;
            }

            i++;
        }
    }

    /**
     * Mouse clicked
     * 
     * When the mouse is clicked we're going to convert mouse's X and Y to 
     * image index 
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        boolean xOut = mouseX < this.x + 2 || mouseX > this.x + this.w - 2;
        boolean yOut = mouseY < this.y + 2 || mouseY > this.y + this.h - 2;

        if (xOut || yOut)
        {
            return;
        }

        int cap = (this.w - 2) / 42;

        int x = MathHelper.clamp_int((mouseX - this.x - 2) / 42, 0, cap - 1);
        int y = (mouseY - this.y - 2) / 42;

        int index = x + y * cap;

        System.out.println(index);

        if (index < 0 || index >= this.images.size())
        {
            return;
        }

        if (this.listener != null)
        {
            this.selected = index;
            this.listener.pickPicture(this, this.images.get(index).filename);
        }
    }

    /**
     * Draw the thing 
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        GlStateManager.enableBlend();

        int cap = (this.w - 2) / 42;

        for (int i = 0, c = this.images.size(); i < c; i++)
        {
            ImageInfo image = this.images.get(i);

            int x = this.x + (i % cap) * 42 + 2;
            int y = this.y + (i / cap) * 42 + 2;

            GlStateManager.color(1.0F, 1.0F, 1.0F, i == this.selected ? 0.5F : 1.0F);
            Minecraft.getMinecraft().renderEngine.bindTexture(image.texture);
            this.drawPicture(image, x, y, 40, 40);
        }

        GlStateManager.disableBlend();
    }

    /**
     * Draw the picture in given rect
     * 
     * This method is not only responsible for drawing a texture on the screen, 
     * but also for aligning and resizing pictures with non square aspect ratio 
     * correctly.
     */
    public void drawPicture(ImageInfo image, int x, int y, int width, int height)
    {
        if (image.size.width != image.size.height)
        {
            int diff = image.size.width - image.size.height;

            /* If diff is positive, image is landscape otherwise it's portrait */
            if (diff > 0)
            {
                height = (int) ((float) (image.size.width - diff) / (float) image.size.width * (float) width);
                y += (width - height) / 2;
            }
            else
            {
                width = (int) ((float) (image.size.height + diff) / (float) image.size.height * (float) height);
                x += (height - width) / 2;
            }
        }

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();

        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, this.zLevel).tex(0, 1).endVertex();
        buffer.pos(x + width, y + height, this.zLevel).tex(1, 1).endVertex();
        buffer.pos(x + width, y, this.zLevel).tex(1, 0).endVertex();
        buffer.pos(x, y, this.zLevel).tex(0, 0).endVertex();

        tessellator.draw();
    }

    /**
     * Image info class
     * 
     * This class is responsible for holding information about pictures located 
     * in the mod config's pictures folder.
     */
    public static class ImageInfo
    {
        public String filename;
        public ResourceLocation texture;
        public Dimension size;

        public ImageInfo(String filename, Dimension size)
        {
            this(filename, new ResourceLocation("imaginary.pictures", filename), size);
        }

        public ImageInfo(String filename, ResourceLocation texture, Dimension size)
        {
            this.filename = filename;
            this.texture = texture;
            this.size = size;
        }
    }
}