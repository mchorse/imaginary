package mchorse.imaginary.client.render;

import org.lwjgl.opengl.GL11;

import mchorse.imaginary.ClientProxy;
import mchorse.imaginary.Imaginary;
import mchorse.imaginary.entity.EntityImage;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPainting;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

/**
 * Render image class
 * 
 * This class is responsible for rendering an image entity. Animated pictures 
 * (GIFs) coming soon.
 */
public class RenderImage extends Render<EntityImage>
{
    public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation("imaginary", "textures/items/image.png");

    /**
     * Initiate this renderer with render manager 
     */
    public RenderImage(RenderManager manager)
    {
        super(manager);
    }

    /**
     * Should hopefully fix the problem with disappearing big pictures 
     */
    @Override
    public boolean shouldRender(EntityImage livingEntity, ICamera camera, double camX, double camY, double camZ)
    {
        return true;
    }

    /**
     * If image entity's picture isn't null and it actually exist, then use 
     * its picture, otherwise, use default one. 
     */
    @Override
    protected ResourceLocation getEntityTexture(EntityImage entity)
    {
        if (entity.picture != null && ClientProxy.picturesPack.resourceExists(entity.picture))
        {
            return entity.picture;
        }

        return DEFAULT_TEXTURE;
    }

    /**
     * Render this image 
     */
    @Override
    public void doRender(EntityImage entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F - entity.rotationPitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(entity.rotationRoll, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.bindEntityTexture(entity);

        /* Texture filtering, probably not the best idea */
        if (Imaginary.proxy.config.enable_linear_filtering)
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        }
        else
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        }

        if (Imaginary.proxy.config.disable_lighting)
        {
            GlStateManager.disableLighting();
        }

        float f = 0.0625F;

        /* Render the back side */
        GlStateManager.scale(f, f, f);
        this.renderImage(entity, true);

        /* Render the front side of this image */
        GlStateManager.scale(-1.0F, 1.0F, 1.0F);
        this.renderImage(entity, false);

        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        if (Imaginary.proxy.config.disable_lighting)
        {
            GlStateManager.enableLighting();
        }
    }

    /**
     * Render the image
     * 
     * The code is partially taken from the {@link RenderPainting} class or 
     * maybe from {@link Gui#drawTexturedModalRect(int, int, int, int, int, int)},
     * I don't know.
     */
    private void renderImage(EntityImage entity, boolean mirror)
    {
        float w = entity.sizeW * 16;
        float h = entity.sizeH * 16;

        double x1 = -w / 2;
        double x2 = w / 2;
        double y1 = -h / 2;
        double y2 = h / 2;

        double u1 = 1.0F;
        double u2 = 0.0F;
        double v1 = 1.0F;
        double v2 = 0.0F;

        float ry = (float) Math.sin(entity.rotationPitch);
        float rz = (float) Math.cos(entity.rotationPitch + Math.PI);

        if (mirror)
        {
            double tmp = u2;

            u2 = u1;
            u1 = tmp;
        }

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);

        vertexbuffer.pos(x1, y2, 0).tex(u2, v1).normal(0.0F, ry, rz).endVertex();
        vertexbuffer.pos(x2, y2, 0).tex(u1, v1).normal(0.0F, ry, rz).endVertex();
        vertexbuffer.pos(x2, y1, 0).tex(u1, v2).normal(0.0F, ry, rz).endVertex();
        vertexbuffer.pos(x1, y1, 0).tex(u2, v2).normal(0.0F, ry, rz).endVertex();

        tessellator.draw();
    }

    /**
     * Image rendering factory
     * 
     * This class is responsible for creating an image renderer
     */
    public static class ImageFactory implements IRenderFactory<EntityImage>
    {
        @Override
        public Render<? super EntityImage> createRenderFor(RenderManager manager)
        {
            return new RenderImage(manager);
        }
    }
}