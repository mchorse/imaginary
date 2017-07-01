package mchorse.imaginary.config;

import mchorse.imaginary.Imaginary;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ImaginaryConfig
{
    public boolean disable_lighting;

    public boolean enable_linear_filtering;

    public Configuration config;

    public ImaginaryConfig(Configuration config)
    {
        this.config = config;
        this.reload();
    }

    public void reload()
    {
        String cat = Configuration.CATEGORY_GENERAL;
        String pre = "imaginary.config.";

        this.disable_lighting = this.config.getBoolean("disable_lighting", cat, false, "Disables lighting (always yields white picture)", pre + "disable_lighting");
        this.enable_linear_filtering = this.config.getBoolean("enable_linear_filtering", cat, false, "Enables linear filtering of the pictures", pre + "enable_linear_filtering");

        if (this.config.hasChanged())
        {
            this.config.save();
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(Imaginary.MODID) && this.config.hasChanged())
        {
            this.reload();
        }
    }
}