package mchorse.imaginary.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Picture pack
 *
 * This class allows access to the pictures that are located in the config 
 * folder of this mod.
 */
@SideOnly(Side.CLIENT)
public class PicturePack implements IResourcePack
{
    /**
     * Default resource domain name for this resource pack 
     */
    private static final Set<String> DEFAULT_DOMAIN = ImmutableSet.<String> of("imaginary.pictures");

    /**
     * Location of teh pictures
     */
    public File pictures;

    /**
     * Create a picture pack with path to pictures 
     */
    public PicturePack(File pictures)
    {
        this.pictures = pictures;
    }

    /* IResourcePack implementation */

    /**
     * Get the input stream of a picture from this pack
     */
    @Override
    public InputStream getInputStream(ResourceLocation location) throws IOException
    {
        return new FileInputStream(new File(this.pictures, location.getResourcePath()));
    }

    /**
     * Check if a picture in this pack does exist 
     */
    @Override
    public boolean resourceExists(ResourceLocation location)
    {
        File file = new File(this.pictures, location.getResourcePath());

        return file.isFile();
    }

    @Override
    public Set<String> getResourceDomains()
    {
        return DEFAULT_DOMAIN;
    }

    @Override
    public String getPackName()
    {
        return "Imaginary pictures";
    }

    @Override
    public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException
    {
        return null;
    }

    @Override
    public BufferedImage getPackImage() throws IOException
    {
        return null;
    }
}