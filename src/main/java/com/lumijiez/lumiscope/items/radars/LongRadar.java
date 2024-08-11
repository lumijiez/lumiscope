package com.lumijiez.lumiscope.items.radars;

import com.lumijiez.lumiscope.items.ItemBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class LongRadar extends ItemBase {

    public LongRadar() {
        super("long_radar");
        setMaxStackSize(1);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        ITextComponent info = new TextComponentString("Checks for faraway players.")
                .setStyle(new Style().setColor(TextFormatting.AQUA));
        tooltip.add(info.getFormattedText());

        ITextComponent warning = new TextComponentString("Can be imprecise!")
                .setStyle(new Style().setColor(TextFormatting.RED));
        tooltip.add(warning.getFormattedText());

        tooltip.add(new TextComponentString("Does not detect invisible players!")
                .setStyle(new Style().setColor(TextFormatting.DARK_RED).setBold(true).setItalic(true)).getFormattedText());


        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
