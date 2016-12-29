package mchorse.imaginary.client.render;

import mchorse.imaginary.entity.EntityImage;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
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
    public RenderImage(RenderManager manager)
    {
        super(manager);
    }

    /**
     * Such a waste of resources 
     */
    @Override
    protected ResourceLocation getEntityTexture(EntityImage entity)
    {
        return new ResourceLocation("imaginary.pictures", entity.picture + ".png");
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
        GlStateManager.enableRescaleNormal();
        this.bindEntityTexture(entity);

        float f = 0.0625F;

        /* Render the image once */
        GlStateManager.scale(f, f, f);
        this.renderImage(entity);

        /* Render back side of the image */
        GlStateManager.scale(-1.0F, 1.0F, 1.0F);
        this.renderImage(entity);

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }

    /**
     * Render the image
     * 
     * The code is partially taken from the {@link RenderPainting} class.
     */
    private void renderImage(EntityImage entity)
    {
        double x1 = -5.0F;
        double x2 = 5.0F;
        double y1 = 5.0F;
        double y2 = -5.0F;

        double u1 = 1.0F;
        double u2 = 0.0F;
        double v1 = 1.0F;
        double v2 = 0.0F;

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);

        vertexbuffer.pos(x1, y2, 0).tex(u2, v1).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(x2, y2, 0).tex(u1, v1).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(x2, y1, 0).tex(u1, v2).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(x1, y1, 0).tex(u2, v2).normal(0.0F, 0.0F, -1.0F).endVertex();

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