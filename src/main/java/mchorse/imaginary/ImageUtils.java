package mchorse.imaginary;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

/**
 * Image utils 
 */
public class ImageUtils
{
    /**
     * Gets image dimensions for given file 
     * 
     * @param file image file
     * @return dimensions of image
     * @link http://stackoverflow.com/a/12164026/6756869
     * @throws IOException if the file is not a known image
     */
    public static Dimension getImageDimension(File file) throws IOException
    {
        int pos = file.getName().lastIndexOf(".");

        if (pos == -1)
        {
            throw new IOException("No extension for file: " + file.getAbsolutePath());
        }

        String suffix = file.getName().substring(pos + 1);
        Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);

        while (iter.hasNext())
        {
            ImageReader reader = iter.next();

            try
            {
                ImageInputStream stream = new FileImageInputStream(file);

                reader.setInput(stream);

                int width = reader.getWidth(reader.getMinIndex());
                int height = reader.getHeight(reader.getMinIndex());

                return new Dimension(width, height);
            }
            catch (IOException e)
            {
                System.out.println("Error reading: " + file.getAbsolutePath() + " \n" + e);
            }
            finally
            {
                reader.dispose();
            }
        }

        throw new IOException("Not a known image file: " + file.getAbsolutePath());
    }
}