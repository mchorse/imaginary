package mchorse.imaginary.entity;

import io.netty.buffer.ByteBuf;
import mchorse.imaginary.GuiHandler;
import mchorse.imaginary.Imaginary;
import mchorse.imaginary.network.Dispatcher;
import mchorse.imaginary.network.common.PacketModifyImage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    /**
     * In which direction this image is faced 
     */
    public EnumFacing facing = EnumFacing.DOWN;

    /**
     * Block position of this image 
     */
    public BlockPos blockPos = BlockPos.ORIGIN;

    /**
     * Size width in blocks units
     */
    public float sizeW = 1;

    /**
     * Size height in blocks units 
     */
    public float sizeH = 1;

    /* Entity shifting, very helpful for adjusting correct position within one 
     * block space */
    public float shiftX = 0;
    public float shiftY = 0;
    public float shiftZ = 0;

    /**
     * Fit AABB within given size. This is useful only for rendering, since when 
     * an entity's AABB out of sight, it doesn't gets rendered.
     */
    public boolean fitAABB = false;

    /**
     * Rotation roll (useful for facing DOWN and UP) 
     */
    public float rotationRoll;

    public EntityImage(World worldIn)
    {
        super(worldIn);

        this.setSize(1.0F, 1.0F);
    }

    /**
     * Kill this image 
     */
    @Override
    public boolean hitByEntity(Entity entityIn)
    {
        return entityIn instanceof EntityPlayer ? this.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) entityIn), 0.0F) : false;
    }

    /**
     * This is really weird, but if entity cannot be collided with, neither it 
     * can be punched...
     */
    @Override
    public boolean canBeCollidedWith()
    {
        return true;
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else
        {
            if (!this.isDead && !this.world.isRemote)
            {
                this.setDead();
                this.setBeenAttacked();

                if (!source.isCreativePlayer())
                {
                    this.entityDropItem(new ItemStack(Imaginary.imageItem), 0.0F);
                }
            }

            return true;
        }
    }

    /**
     * Drop entity item
     * 
     * This method overwrites (and copies) code from parent class so that it 
     * would have correct position on spawn. Otherwise the item will stuck in 
     * the wall and simply fly up and away.
     */
    @Override
    public EntityItem entityDropItem(ItemStack stack, float offsetY)
    {
        if (!stack.isEmpty() && stack.getItem() != null)
        {
            Vec3i vec = this.facing.getDirectionVec();

            float x = this.blockPos.getX() + 0.5F + vec.getX();
            float y = this.blockPos.getY() + 0.5F + vec.getY();
            float z = this.blockPos.getZ() + 0.5F + vec.getZ();

            EntityItem item = new EntityItem(this.world, x, y + (double) offsetY, z, stack);
            item.setDefaultPickupDelay();

            if (captureDrops)
            {
                this.capturedDrops.add(item);
            }
            else
            {
                this.world.spawnEntity(item);
            }

            return item;
        }
        else
        {
            return null;
        }
    }

    @Override
    protected void entityInit()
    {}

    /**
     * Modify this entity (more arguments coming soon)
     */
    public void modify(String picture, float width, float height, float shiftX, float shiftY, float shiftZ, float rotateX, float rotateY, float rotateZ, boolean fitAABB)
    {
        this.setPicture(picture);

        this.sizeW = width;
        this.sizeH = height;

        this.shiftX = shiftX;
        this.shiftY = shiftY;
        this.shiftZ = shiftZ;

        this.rotationPitch = this.prevRotationPitch = rotateX;
        this.rotationYaw = this.prevRotationYaw = rotateY;
        this.rotationRoll = rotateZ;

        this.fitAABB = fitAABB;

        this.updatePosition();
    }

    /**
     * Notify mother-trackers :D
     */
    public void notifyTrackers()
    {
        if (!this.world.isRemote)
        {
            Dispatcher.sendToTracked(this, new PacketModifyImage(this));
        }
    }

    /**
     * Process initial interact
     * 
     * This method is responsible for opening GUI interface and renaming this 
     * entity if player holds a name tag.
     */
    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);

        /* Fuck EntityLivingBase! */
        if (stack != null && stack.getItem() instanceof ItemNameTag && stack.hasDisplayName())
        {
            this.setCustomNameTag(stack.getDisplayName());

            return true;
        }

        GuiHandler.open(player, GuiHandler.PICTURE, this.getEntityId(), 0, 0);

        return true;
    }

    /**
     * Set picture 
     */
    public void setPicture(String picture)
    {
        this.picture = picture.isEmpty() ? null : new ResourceLocation("imaginary.pictures", picture);
    }

    /**
     * Get picture 
     */
    public String getPicture()
    {
        return this.picture == null ? "" : this.picture.getResourcePath();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender()
    {
        return Imaginary.proxy.config.disable_lighting ? 15728880 : super.getBrightnessForRender();
    }

    /**
     * Update the position based on the coordinates
     */
    public void updatePosition()
    {
        Vec3i vec = this.facing.getDirectionVec();

        float x = this.blockPos.getX() + 0.5F + ((float) vec.getX() * 0.511F) + this.shiftX;
        float y = this.blockPos.getY() + 0.5F + ((float) vec.getY() * 0.511F) + this.shiftY;
        float z = this.blockPos.getZ() + 0.5F + ((float) vec.getZ() * 0.511F) + this.shiftZ;

        this.setPosition(x, y, z);
    }

    /**
     * Set position and also update the AABB according to this image's 
     * properties  
     */
    @Override
    public void setPosition(double x, double y, double z)
    {
        this.posX = x;
        this.posY = y;
        this.posZ = z;

        float w = 1.0F;
        float h = 1.0F;
        float d = 1.0F;

        if (this.facing == null)
        {
            this.facing = EnumFacing.DOWN;
        }

        if (this.facing.getAxis() == EnumFacing.Axis.X)
        {
            if (this.fitAABB)
            {
                h = this.sizeH;
                d = this.sizeW;
            }

            w = 0.05F;
        }
        else if (this.facing.getAxis() == EnumFacing.Axis.Y)
        {
            if (this.fitAABB)
            {
                w = this.sizeW;
                d = this.sizeH;
            }

            h = 0.05F;
        }
        else if (this.facing.getAxis() == EnumFacing.Axis.Z)
        {
            if (this.fitAABB)
            {
                w = this.sizeW;
                h = this.sizeH;
            }

            d = 0.05F;
        }

        this.setEntityBoundingBox(new AxisAlignedBB(x - w / 2, y - h / 2, z - d / 2, x + w / 2, y + h / 2, z + d / 2));
    }

    /* Persisting and restoring this entity */

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        this.setPicture(compound.getString("Picture"));

        this.sizeW = compound.getFloat("SizeW");
        this.sizeH = compound.getFloat("SizeH");

        this.shiftX = compound.getFloat("ShiftX");
        this.shiftY = compound.getFloat("ShiftY");
        this.shiftZ = compound.getFloat("ShiftZ");

        this.rotationRoll = compound.getFloat("Roll");

        this.facing = EnumFacing.getFront(compound.getByte("Facing"));

        if (compound.hasKey("BlockX") && compound.hasKey("BlockY") && compound.hasKey("BlockZ"))
        {
            this.blockPos = new BlockPos(compound.getInteger("BlockX"), compound.getInteger("BlockY"), compound.getInteger("BlockZ"));
        }
        else
        {
            this.blockPos = new BlockPos(this.posX, this.posY, this.posZ);
        }

        this.fitAABB = compound.getBoolean("FitAABB");

        this.updatePosition();
        this.notifyTrackers();
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setString("Picture", this.getPicture());

        compound.setFloat("SizeW", this.sizeW);
        compound.setFloat("SizeH", this.sizeH);

        compound.setFloat("ShiftX", this.shiftX);
        compound.setFloat("ShiftY", this.shiftY);
        compound.setFloat("ShiftZ", this.shiftZ);

        compound.setFloat("Roll", this.rotationRoll);

        compound.setByte("Facing", (byte) this.facing.getIndex());

        if (this.blockPos != null)
        {
            compound.setInteger("BlockX", this.blockPos.getX());
            compound.setInteger("BlockY", this.blockPos.getY());
            compound.setInteger("BlockZ", this.blockPos.getZ());
        }

        compound.setBoolean("FitAABB", this.fitAABB);
    }

    /**
     * Read image data from the server 
     */
    @Override
    public void readSpawnData(ByteBuf buffer)
    {
        this.setPicture(ByteBufUtils.readUTF8String(buffer));

        this.sizeW = buffer.readFloat();
        this.sizeH = buffer.readFloat();

        this.shiftX = buffer.readFloat();
        this.shiftY = buffer.readFloat();
        this.shiftZ = buffer.readFloat();

        this.rotationRoll = buffer.readFloat();

        this.facing = EnumFacing.getFront(buffer.readByte());
        this.blockPos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());

        this.fitAABB = buffer.readBoolean();

        this.updatePosition();
    }

    /**
     * Write image data on the server 
     */
    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        ByteBufUtils.writeUTF8String(buffer, this.getPicture());

        buffer.writeFloat(this.sizeW);
        buffer.writeFloat(this.sizeH);

        buffer.writeFloat(this.shiftX);
        buffer.writeFloat(this.shiftY);
        buffer.writeFloat(this.shiftZ);

        buffer.writeFloat(this.rotationRoll);

        buffer.writeByte(this.facing.getIndex());

        buffer.writeInt(this.blockPos.getX());
        buffer.writeInt(this.blockPos.getY());
        buffer.writeInt(this.blockPos.getZ());

        buffer.writeBoolean(this.fitAABB);
    }
}