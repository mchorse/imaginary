package mchorse.imaginary;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Image utils 
 */
public class ImageUtils
{
    /**
     * Gets image dimensions for given file 
     */
    public static Dimension getImageDimension(File file) throws IOException
    {
        BufferedImage img = ImageIO.read(file);

        return new Dimension(img.getWidth(), img.getHeight());
    }
}