package com.lumijiez.lumiscope.items.radars;

import com.lumijiez.lumiscope.items.ItemBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ShortRadar extends ItemBase {
    public ShortRadar() {
        super("short_radar");
        setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TextComponentString("Checks for nearby players.")
                .setStyle(new Style().setColor(TextFormatting.AQUA)).getFormattedText());

        tooltip.add(new TextComponentString("Does not detect invisible players!")
                .setStyle(new Style().setColor(TextFormatting.DARK_RED).setBold(true).setItalic(true)).getFormattedText());

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
