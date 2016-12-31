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
import net.minecraft.client.renderer.GlStateManager;

/**
 * Picture configuration GUI 
 *
 * This GUI is responsible for configuring given image entity. 
 */
public class GuiPicture extends GuiScreen implements IPicturePicker
{
    /* GUI text fields */
    public GuiButton save;
    public GuiButton pick;
    public GuiButton aspect;

    public GuiTextField sizeW;
    public GuiTextField sizeH;

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
        if (!this.entity.getPicture().equals(filename))
        {
            this.entity.setPicture(filename);
            this.picker.setHidden(true);
        }
    }

    @Override
    public void initGui()
    {
        int w = 113;

        /* Initializing GUI fields */
        this.sizeW = new GuiTextField(0, fontRendererObj, this.width - 11 - w, 30, w, 18);
        this.sizeH = new GuiTextField(0, fontRendererObj, this.width - 11 - w, 55, w, 18);

        this.save = new GuiButton(0, this.width - 90, this.height - 30, 80, 20, "Save");
        this.pick = new GuiButton(1, 10, 132, 100, 20, "Pick picture...");
        this.aspect = new GuiButton(2, this.width - 90, 4, 80, 20, "Keep Aspect");

        /* Adding buttons */
        this.buttonList.add(this.save);
        this.buttonList.add(this.pick);
        this.buttonList.add(this.aspect);

        /* Configuring up the fields */
        this.sizeW.setText(Float.toString(this.entity.sizeW));
        this.sizeH.setText(Float.toString(this.entity.sizeH));

        this.picker.x = 10;
        this.picker.y = 154;
        this.picker.w = ((this.width - 10) / 2);
        this.picker.w -= this.picker.w % 42;
        this.picker.w += 10;
        this.picker.h = this.height - 154 - 10;

        int cap = (this.picker.w - 2) / 42;

        this.picker.scrollHeight = this.picker.images.size() / cap * 42;
        this.picker.hidden = true;
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
            this.picker.setHidden(false);
        }
        else if (button.id == 2)
        {
            this.keepAspect = !this.keepAspect;
            button.displayString = this.keepAspect ? "Keep Aspect" : "Custom Size";
        }
    }

    private void saveAndQuit()
    {
        Dispatcher.sendToServer(new PacketModifyImage(this.entity));
        this.mc.displayGuiScreen(null);
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        this.picker.handleMouseInput();
        super.handleMouseInput();
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

        this.sizeW.textboxKeyTyped(typedChar, keyCode);
        this.sizeH.textboxKeyTyped(typedChar, keyCode);

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
        if (!this.picker.isInside(mouseX, mouseY))
        {
            super.mouseClicked(mouseX, mouseY, mouseButton);

            this.sizeW.mouseClicked(mouseX, mouseY, mouseButton);
            this.sizeH.mouseClicked(mouseX, mouseY, mouseButton);
        }
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
        this.fontRendererObj.drawStringWithShadow("Imaginary Picture", 10, 10, 0xffffffff);
        this.fontRendererObj.drawStringWithShadow("Size", this.width - 120, 10, 0xffffffff);

        /* Draw picture */
        Gui.drawRect(10, 30, 110, 130, 0xff000000);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GuiPictures.drawPicture(this.picker.getSelected(), 10, 30, this.zLevel + 1, 100, 100);

        /* Draw GUI fields */
        this.sizeW.drawTextBox();
        this.sizeH.drawTextBox();

        this.fontRendererObj.drawStringWithShadow("Width", this.width - 45, 35, 0xff888888);
        this.fontRendererObj.drawStringWithShadow("Height", this.width - 45, 57, 0xff888888);

        super.drawScreen(mouseX, mouseY, partialTicks);

        /* Draw picker */
        this.picker.drawScreen(mouseX, mouseY, partialTicks);
    }
}