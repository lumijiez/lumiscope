package com.lumijiez.lumiscope.items.radars;

import com.lumijiez.lumiscope.items.ItemBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
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
        ITextComponent info = new TextComponentString("Checks for nearby players.")
                .setStyle(new Style().setColor(TextFormatting.AQUA));
        tooltip.add(info.getFormattedText());

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (!world.isRemote) {
            boolean playerNearby = false;

            ITextComponent message;
            if (isPlayerNearby(world, player)) {
                message = new TextComponentString("There are players within 200 meters!")
                        .setStyle(new Style().setColor(TextFormatting.GREEN));
            } else {
                message = new TextComponentString("No players within 200 meters.")
                        .setStyle(new Style().setColor(TextFormatting.RED));
            }

            player.sendMessage(message);
            player.getCooldownTracker().setCooldown(this, 1200);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    private boolean isPlayerNearby(World world, EntityPlayer player) {
        for (EntityPlayer otherPlayer : world.playerEntities) {
            if (!otherPlayer.equals(player) && player.getDistance(otherPlayer) <= 200) {
                return true;
            }
        }
        return false;
    }
}
