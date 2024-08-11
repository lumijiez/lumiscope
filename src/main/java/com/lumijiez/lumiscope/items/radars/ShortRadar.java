package com.lumijiez.lumiscope.items.radars;

import com.lumijiez.lumiscope.items.ItemBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ShortRadar extends ItemBase {
    public ShortRadar() {
        super("short_radar");
        setMaxStackSize(1);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TextComponentString("Checks for nearby players.")
                .setStyle(new Style().setColor(TextFormatting.AQUA)).getFormattedText());

        tooltip.add(new TextComponentString("Does detect invisible players!")
                .setStyle(new Style().setColor(TextFormatting.GREEN).setBold(true).setItalic(true)).getFormattedText());

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
