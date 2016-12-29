package mchorse.imaginary.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
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
    public String picture = "";

    public EntityImage(World worldIn)
    {
        super(worldIn);
    }

    @Override
    protected void entityInit()
    {}

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        this.picture = compound.getString("Picture");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setString("Picture", this.picture);
    }

    @Override
    public void readSpawnData(ByteBuf buffer)
    {
        this.picture = ByteBufUtils.readUTF8String(buffer);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        ByteBufUtils.writeUTF8String(buffer, this.picture);
    }
}