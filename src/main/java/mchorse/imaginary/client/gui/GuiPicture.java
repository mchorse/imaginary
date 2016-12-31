package mchorse.imaginary.client.gui;

import java.io.IOException;

import mchorse.imaginary.client.gui.GuiPictures.ImageInfo;
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
    public GuiButton aspect;
    public GuiTextField sizeW;
    public GuiTextField sizeH;

    public GuiTextField picture;
    public GuiPictures picker;

    /* Image entity */
    public EntityImage entity;

    /* Restorable information */
    public String oldPicture;
    public float oldSizeW;
    public float oldSizeH;

    boolean keepAspect = true;

    public GuiPicture(EntityImage entity)
    {
        this.entity = entity;

        this.oldPicture = entity.getPicture();
        this.oldSizeW = entity.sizeW;
        this.oldSizeH = entity.sizeH;

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
        int w = (this.width - 20 - 100) / 2;

        this.picture = new GuiTextField(0, fontRendererObj, 11, this.height - 10 - 19, this.width - 22 - 100, 18);
        this.sizeW = new GuiTextField(0, fontRendererObj, 11, 11, w, 18);
        this.sizeH = new GuiTextField(0, fontRendererObj, this.width - 11 - w, 11, w, 18);

        this.save = new GuiButton(0, this.width - 100, this.height - 30, 90, 20, "Save");
        this.aspect = new GuiButton(1, this.width / 2 - 40, 10, 80, 20, "Keep Aspect");

        this.sizeW.setText(Float.toString(this.entity.sizeW));
        this.sizeH.setText(Float.toString(this.entity.sizeH));

        this.buttonList.add(this.save);
        this.buttonList.add(this.aspect);

        this.picker.x = 10;
        this.picker.y = 40;
        this.picker.w = this.width - 20;
        this.picker.h = this.height - 40 - 40;
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
        else if (button.id == 1)
        {
            this.keepAspect = !this.keepAspect;
            button.displayString = this.keepAspect ? "Keep Aspect" : "Custom Size";
        }
    }

    private void saveAndQuit()
    {
        float w = this.oldSizeW;
        float h = this.oldSizeH;

        try
        {
            w = Float.parseFloat(this.sizeW.getText());
        }
        catch (NumberFormatException e)
        {}

        try
        {
            h = Float.parseFloat(this.sizeH.getText());
        }
        catch (NumberFormatException e)
        {}

        Dispatcher.sendToServer(new PacketModifyImage(this.entity.getEntityId(), this.entity.getPicture(), w, h));
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
            this.entity.sizeW = this.oldSizeW;
            this.entity.sizeH = this.oldSizeH;
        }

        this.picture.textboxKeyTyped(typedChar, keyCode);
        this.sizeW.textboxKeyTyped(typedChar, keyCode);
        this.sizeH.textboxKeyTyped(typedChar, keyCode);

        if (this.picture.isFocused())
        {
            this.entity.setPicture(this.picture.getText());
            this.picker.setSelected(this.picture.getText());
        }

        if (this.sizeW.isFocused())
        {
            try
            {
                this.entity.sizeW = Float.parseFloat(this.sizeW.getText());

                if (this.keepAspect)
                {
                    ImageInfo info = this.picker.getSelected();

                    this.entity.sizeH = (float) info.size.height / (float) info.size.width * this.entity.sizeW;
                    this.sizeH.setText(Float.toString(this.entity.sizeH));
                }
            }
            catch (NumberFormatException e)
            {}
        }

        if (this.sizeH.isFocused())
        {
            try
            {
                this.entity.sizeH = Float.parseFloat(this.sizeH.getText());

                if (this.keepAspect)
                {
                    ImageInfo info = this.picker.getSelected();

                    this.entity.sizeW = (float) info.size.width / (float) info.size.height * this.entity.sizeH;
                    this.sizeW.setText(Float.toString(this.entity.sizeW));
                }
            }
            catch (NumberFormatException e)
            {}
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        this.picture.mouseClicked(mouseX, mouseY, mouseButton);
        this.sizeW.mouseClicked(mouseX, mouseY, mouseButton);
        this.sizeH.mouseClicked(mouseX, mouseY, mouseButton);
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
        this.sizeW.drawTextBox();
        this.sizeH.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}