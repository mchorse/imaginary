package mchorse.imaginary.client.gui;

/**
 * Picture picker interface
 * 
 * Callback interface for picking a picture
 */
public interface IPicturePicker
{
    public void pickPicture(GuiPictures gui, String filename);
}