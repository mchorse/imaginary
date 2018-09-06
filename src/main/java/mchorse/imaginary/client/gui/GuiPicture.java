package mchorse.imaginary.client.gui;

import java.io.IOException;

import mchorse.imaginary.client.gui.GuiPictures.ImageInfo;
import mchorse.imaginary.client.gui.GuiTrackpad.ITrackpadListener;
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
public class GuiPicture extends GuiScreen implements IPicturePicker, ITrackpadListener
{
    /* GUI text fields */
    public GuiButton save;
    public GuiButton pick;
    public GuiButton aspect;
    public GuiButton fit;

    public GuiTextField sizeW;
    public GuiTextField sizeH;

    public GuiTrackpad shiftX;
    public GuiTrackpad shiftY;
    public GuiTrackpad shiftZ;

    public GuiTrackpad rotateX;
    public GuiTrackpad rotateY;
    public GuiTrackpad rotateZ;

    public GuiPictures picker;

    /* Image entity */
    public EntityImage entity;

    /* Restorable information */
    public String oldPicture;
    public float oldSizeW;
    public float oldSizeH;
    public float oldShiftX;
    public float oldShiftY;
    public float oldShiftZ;
    public float oldRX;
    public float oldRY;
    public float oldRZ;
    public boolean oldFitAABB;

    boolean keepAspect = true;

    public GuiPicture(EntityImage entity)
    {
        this.entity = entity;

        this.oldPicture = entity.getPicture();
        this.oldSizeW = entity.sizeW;
        this.oldSizeH = entity.sizeH;
        this.oldShiftX = entity.shiftX;
        this.oldShiftY = entity.shiftY;
        this.oldShiftZ = entity.shiftZ;
        this.oldRX = entity.rotationPitch;
        this.oldRY = entity.rotationYaw;
        this.oldRZ = entity.rotationRoll;
        this.oldFitAABB = entity.fitAABB;

        this.picker = new GuiPictures(this.oldPicture);
        this.picker.listener = this;
    }

    @Override
    public void setTrackpadValue(GuiTrackpad trackpad, float value)
    {
        if (trackpad == this.rotateX)
        {
            this.entity.rotationPitch = this.entity.prevRotationPitch = value;
        }
        else if (trackpad == this.rotateY)
        {
            this.entity.rotationYaw = this.entity.prevRotationYaw = value;
        }
        else if (trackpad == this.rotateZ)
        {
            this.entity.rotationRoll = value;
        }

        if (trackpad == this.shiftX || trackpad == this.shiftY || trackpad == this.shiftZ)
        {
            this.entity.shiftX = this.shiftX.value;
            this.entity.shiftY = this.shiftY.value;
            this.entity.shiftZ = this.shiftZ.value;

            this.entity.updatePosition();
            this.entity.prevPosX = this.entity.lastTickPosX = this.entity.posX;
            this.entity.prevPosY = this.entity.lastTickPosY = this.entity.posY;
            this.entity.prevPosZ = this.entity.lastTickPosZ = this.entity.posZ;
        }
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
        this.sizeW = new GuiTextField(0, fontRenderer, this.width - 11 - w, 30, w, 18);
        this.sizeH = new GuiTextField(0, fontRenderer, this.width - 11 - w, 55, w, 18);

        this.shiftX = new GuiTrackpad(this, this.fontRenderer).setTitle("X").update(this.width - 125, 100, 115, 20);
        this.shiftY = new GuiTrackpad(this, this.fontRenderer).setTitle("Y").update(this.width - 125, 125, 115, 20);
        this.shiftZ = new GuiTrackpad(this, this.fontRenderer).setTitle("Z").update(this.width - 125, 150, 115, 20);

        this.shiftX.setValue(this.oldShiftX);
        this.shiftY.setValue(this.oldShiftY);
        this.shiftZ.setValue(this.oldShiftZ);

        this.rotateX = new GuiTrackpad(this, this.fontRenderer).setTitle("Pitch").update(this.width - 125, 195, w, 20);
        this.rotateY = new GuiTrackpad(this, this.fontRenderer).setTitle("Yaw").update(this.width - 125, 220, w, 20);
        this.rotateZ = new GuiTrackpad(this, this.fontRenderer).setTitle("Roll").update(this.width - 125, 245, w, 20);

        this.rotateX.setValue(this.oldRX);
        this.rotateY.setValue(this.oldRY);
        this.rotateZ.setValue(this.oldRZ);

        this.save = new GuiButton(0, 10, this.height - 30, 100, 20, "Save");
        this.pick = new GuiButton(1, 10, this.height - 55, 100, 20, "Pick picture...");
        this.aspect = new GuiButton(2, this.width - 90, 4, 80, 20, "Keep Aspect");
        this.fit = new GuiButton(3, this.width - 125, 270, 115, 20, "");

        /* Adding buttons */
        this.buttonList.add(this.save);
        this.buttonList.add(this.pick);
        this.buttonList.add(this.aspect);
        this.buttonList.add(this.fit);

        /* Configuring up the fields */
        this.sizeW.setText(Float.toString(this.entity.sizeW));
        this.sizeH.setText(Float.toString(this.entity.sizeH));
        this.fit.displayString = this.entity.fitAABB ? "Fit within AABB: Yes" : "Fit within AABB: No";

        this.picker.x = 130;
        this.picker.y = 10;
        this.picker.w = this.width - 120 - 135 - 20;
        this.picker.h = this.height - 20;

        this.picker.hidden = true;
        this.picker.setupHeight();
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
        else if (button.id == 3)
        {
            this.entity.fitAABB = !this.entity.fitAABB;
            this.fit.displayString = this.entity.fitAABB ? "Fit within AABB: Yes" : "Fit within AABB: No";
            this.entity.updatePosition();
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
            this.entity.shiftX = this.oldShiftX;
            this.entity.shiftY = this.oldShiftY;
            this.entity.shiftZ = this.oldShiftZ;
            this.entity.rotationRoll = this.oldRZ;
            this.entity.rotationPitch = this.entity.prevRotationPitch = this.oldRX;
            this.entity.rotationYaw = this.entity.prevRotationYaw = this.oldRY;
            this.entity.fitAABB = this.oldFitAABB;
        }

        this.sizeW.textboxKeyTyped(typedChar, keyCode);
        this.sizeH.textboxKeyTyped(typedChar, keyCode);

        this.rotateX.keyTyped(typedChar, keyCode);
        this.rotateY.keyTyped(typedChar, keyCode);
        this.rotateZ.keyTyped(typedChar, keyCode);
        this.shiftX.keyTyped(typedChar, keyCode);
        this.shiftY.keyTyped(typedChar, keyCode);
        this.shiftZ.keyTyped(typedChar, keyCode);

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

                this.entity.updatePosition();
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

                this.entity.updatePosition();
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

            this.rotateX.mouseClicked(mouseX, mouseY, mouseButton);
            this.rotateY.mouseClicked(mouseX, mouseY, mouseButton);
            this.rotateZ.mouseClicked(mouseX, mouseY, mouseButton);
            this.shiftX.mouseClicked(mouseX, mouseY, mouseButton);
            this.shiftY.mouseClicked(mouseX, mouseY, mouseButton);
            this.shiftZ.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        if (!this.picker.isInside(mouseX, mouseY))
        {
            super.mouseReleased(mouseX, mouseY, state);

            this.rotateX.mouseReleased(mouseX, mouseY, state);
            this.rotateY.mouseReleased(mouseX, mouseY, state);
            this.rotateZ.mouseReleased(mouseX, mouseY, state);
            this.shiftX.mouseReleased(mouseX, mouseY, state);
            this.shiftY.mouseReleased(mouseX, mouseY, state);
            this.shiftZ.mouseReleased(mouseX, mouseY, state);
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
        Gui.drawRect(0, 0, 120, this.height, 0x88000000);
        Gui.drawRect(this.width - 135, 0, this.width, this.height, 0x88000000);

        this.fontRenderer.drawStringWithShadow("Imaginary Picture", 10, 10, 0xffffffff);
        this.fontRenderer.drawStringWithShadow("Size", this.width - 120, 10, 0xffffffff);
        this.fontRenderer.drawStringWithShadow("Shifting", this.width - 120, 85, 0xffffffff);
        this.fontRenderer.drawStringWithShadow("Rotation", this.width - 120, 180, 0xffffffff);

        /* Draw picture */
        Gui.drawRect(10, 30, 110, 130, 0xff000000);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GuiPictures.drawPicture(this.picker.getSelected(), 10, 30, this.zLevel + 1, 100, 100);

        /* Draw GUI fields */
        this.sizeW.drawTextBox();
        this.sizeH.drawTextBox();

        this.fontRenderer.drawStringWithShadow("Width", this.width - 39, 35, 0xff888888);
        this.fontRenderer.drawStringWithShadow("Height", this.width - 45, 60, 0xff888888);

        super.drawScreen(mouseX, mouseY, partialTicks);
        this.rotateX.draw(mouseX, mouseY, partialTicks);
        this.rotateY.draw(mouseX, mouseY, partialTicks);
        this.rotateZ.draw(mouseX, mouseY, partialTicks);
        this.shiftX.draw(mouseX, mouseY, partialTicks);
        this.shiftY.draw(mouseX, mouseY, partialTicks);
        this.shiftZ.draw(mouseX, mouseY, partialTicks);

        /* Draw picker */
        this.picker.drawScreen(mouseX, mouseY, partialTicks);
    }
}