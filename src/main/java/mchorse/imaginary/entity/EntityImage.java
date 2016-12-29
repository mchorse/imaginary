package mchorse.imaginary.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

/**
 * Entity image
 * 
 * This entity will be able to draw custom images. It also got lots of super 
 * cool stuff.
 */
public class EntityImage extends Entity implements IEntityAdditionalSpawnData
{
    /**
     * Pictures which are going to used 
     */
    public ResourceLocation picture;

    public EntityImage(World worldIn)
    {
        super(worldIn);
    }

    @Override
    protected void entityInit()
    {}

    public void setPicture(String picture)
    {
        if (!picture.isEmpty())
        {
            this.picture = new ResourceLocation("imaginary.pictures", picture);
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        this.setPicture(compound.getString("Picture"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        if (this.picture != null)
        {
            compound.setString("Picture", this.picture.getResourcePath());
        }
    }

    /**
     * Read image data from the server 
     */
    @Override
    public void readSpawnData(ByteBuf buffer)
    {
        this.setPicture(ByteBufUtils.readUTF8String(buffer));
    }

    /**
     * Write image data on the server 
     */
    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        ByteBufUtils.writeUTF8String(buffer, this.picture == null ? "" : this.picture.getResourcePath());
    }
}