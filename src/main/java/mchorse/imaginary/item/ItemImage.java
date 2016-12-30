package mchorse.imaginary.item;

import mchorse.imaginary.entity.EntityImage;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
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

            Vec3i vec = facing.getDirectionVec();

            float x = pos.getX() + 0.5F + ((float) vec.getX() * 1.01F) / 2;
            float y = pos.getY() + ((float) vec.getY() * 1.01F) / 2;
            float z = pos.getZ() + 0.5F + ((float) vec.getZ() * 1.01F) / 2;

            float rx = facing.equals(EnumFacing.NORTH) ? 180.0F : 0.0F;
            float ry = 0.0F;

            float width = 1.0F;
            float height = 1.0F;
            float deep = 0.1F;

            if (facing.equals(EnumFacing.DOWN))
            {
                ry = 90.0F;
                y += 0.45F;

                deep = 1.0F;
                height = 0.1F;
            }
            else if (facing.equals(EnumFacing.UP))
            {
                ry = -90.0F;
                y += 0.45F;

                deep = 1.0F;
                height = 0.1F;
            }
            else if (facing.equals(EnumFacing.EAST))
            {
                rx = -90;

                deep = 1.0F;
                width = 0.1F;
            }
            else if (facing.equals(EnumFacing.WEST))
            {
                rx = 90;

                deep = 1.0F;
                width = 0.1F;
            }

            image.setPositionAndRotation(x, y, z, rx, ry);
            image.setSize(width, height, deep);

            image.setPicture("lol.png");
            worldIn.spawnEntityInWorld(image);
        }

        return EnumActionResult.SUCCESS;
    }
}