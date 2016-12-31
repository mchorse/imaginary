package mchorse.imaginary.item;

import mchorse.imaginary.entity.EntityImage;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Item image class
 * 
 * This item is responsible for spawning an image entity.
 */
public class ItemImage extends Item
{
    public ItemImage()
    {
        this.setMaxStackSize(64);
        this.setRegistryName("image");
        this.setUnlocalizedName("imaginary.image");
        this.setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            EntityImage image = new EntityImage(worldIn);

            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("ImageData", 10))
            {
                image.readFromNBT(stack.getTagCompound().getCompoundTag("ImageData"));
            }

            float rx = 0.0F;
            float ry = 0.0F;

            if (facing == EnumFacing.DOWN || facing == EnumFacing.UP)
            {
                ry = facing == EnumFacing.DOWN ? 90.0F : -90.0F;
            }
            else if (facing == EnumFacing.WEST || facing == EnumFacing.EAST)
            {
                rx = facing == EnumFacing.WEST ? 90.0F : -90.0F;
            }
            else if (facing == EnumFacing.NORTH)
            {
                rx = 180.0F;
            }

            image.facing = facing;
            image.blockPos = pos;

            if (facing.getAxis() == Axis.Y)
            {
                float rz = (float) Math.floor((-playerIn.rotationYaw + 180 + 45.0F) / 90.0F);

                image.rotationRoll = rz * 90;
            }

            image.rotationYaw = image.prevRotationYaw = rx;
            image.rotationPitch = image.prevRotationPitch = ry;
            image.updatePosition();

            worldIn.spawnEntityInWorld(image);
        }

        return EnumActionResult.SUCCESS;
    }
}