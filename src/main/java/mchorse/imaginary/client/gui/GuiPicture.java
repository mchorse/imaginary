package mchorse.imaginary.client.gui;

import java.io.IOException;

import mchorse.imaginary.entity.EntityImage;
import mchorse.imaginary.network.Dispatcher;
import mchorse.imaginary.network.common.PacketModifyImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

/**
 * Picture configuration GUI 
 *
 * This GUI is responsible for configuring given image entity. 
 */
public class GuiPicture extends GuiScreen implements IPicturePicker
{
    /* GUI text fields */
    public GuiButton save;
    public GuiTextField picture;
    public GuiPictures picker;

    /* Image entity */
    public EntityImage entity;
    public String oldPicture;

    public GuiPicture(EntityImage entity)
    {
        this.entity = entity;
        this.oldPicture = entity.getPicture();

        this.picker = new GuiPictures(this.oldPicture);
        this.picker.listener = this;
    }

    @Override
    public void pickPicture(GuiPictures gui, String filename)
    {
        if (!picture.getText().equals(filename))
        {
            this.picture.setText(filename);
            this.entity.setPicture(filename);
        }
    }

    @Override
    public void initGui()
    {
        this.picture = new GuiTextField(0, fontRendererObj, 11, this.height - 10 - 19, this.width - 22 - 100, 18);
        this.save = new GuiButton(0, this.width - 100, this.height - 30, 90, 20, "Save");

        this.buttonList.add(save);

        this.picker.x = 10;
        this.picker.y = 10;
        this.picker.w = this.width - 20;
        this.picker.h = this.height - 10 - 40;
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height)
    {
        super.setWorldAndResolution(mc, width, height);

        this.picker.setWorldAndResolution(mc, width, height);
    }

    /* Input */

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == 0)
        {
            this.saveAndQuit();
        }
    }

    private void saveAndQuit()
    {
        Dispatcher.sendToServer(new PacketModifyImage(this.entity.getEntityId(), this.entity.getPicture()));
        this.mc.displayGuiScreen(null);
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        this.picker.handleMouseInput();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1)
        {
            this.entity.setPicture(this.oldPicture);
        }

        this.picture.textboxKeyTyped(typedChar, keyCode);

        if (this.picture.isFocused())
        {
            this.entity.setPicture(this.picture.getText());
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        this.picture.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /* Rendering */

    /**
     * Draw the screen
     * 
     * This method is responsible for drawing the screen.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        Gui.drawRect(0, 0, this.width, this.height, 0x88000000);

        /* Draw picker */
        this.picker.drawScreen(mouseX, mouseY, partialTicks);

        /* Draw GUI fields */
        this.picture.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}