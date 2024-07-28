package com.lumijiez.lumiscope.items.radars;

import com.lumijiez.lumiscope.items.ItemBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
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
        setMaxDamage(70);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TextComponentString("Checks for nearby players.")
                .setStyle(new Style().setColor(TextFormatting.AQUA)).getFormattedText());

        tooltip.add(new TextComponentString("Does not detect invisible players!")
                        .setStyle(new Style().setColor(TextFormatting.DARK_RED).setBold(true).setItalic(true)).getFormattedText());
        
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (!world.isRemote) {
            player.getHeldItem(hand).damageItem(1, player);

            boolean playerNearby = false;

            for (EntityPlayer otherPlayer : world.playerEntities) {

                if (!otherPlayer.equals(player)
                        && player.getDistance(otherPlayer) <= 100
                        && otherPlayer.getActivePotionEffects().stream()
                        .noneMatch(potionEffect -> potionEffect.getPotion() == MobEffects.INVISIBILITY)) {

                    playerNearby = true;
                    String direction = getPlayerDirection(player, otherPlayer);
                    ITextComponent message = new TextComponentString(otherPlayer.getName() + " to the " + direction + "!")
                            .setStyle(new Style().setColor(TextFormatting.GREEN));
                    player.sendMessage(message);
                }
            }

            if (!playerNearby) {
                ITextComponent noPlayersMessage = new TextComponentString("Could not detect players within 100 meters.")
                        .setStyle(new Style().setColor(TextFormatting.RED));
                player.sendMessage(noPlayersMessage);
            }

            player.getCooldownTracker().setCooldown(this, 200);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    private String getPlayerDirection(EntityPlayer player, EntityPlayer otherPlayer) {
        double deltaX = otherPlayer.posX - player.posX;
        double deltaZ = otherPlayer.posZ - player.posZ;
        double angle = MathHelper.atan2(deltaZ, deltaX) * (180 / Math.PI) - 90;
        if (angle < 0) {
            angle += 360;
        }

        angle = (angle + 180) % 360;

        if (angle >= 337.5 || angle < 22.5) return "north";
        if (angle >= 22.5 && angle < 67.5) return "northeast";
        if (angle >= 67.5 && angle < 112.5) return "east";
        if (angle >= 112.5 && angle < 157.5) return "southeast";
        if (angle >= 157.5 && angle < 202.5) return "south";
        if (angle >= 202.5 && angle < 247.5) return "southwest";
        if (angle >= 247.5 && angle < 292.5) return "west";
        if (angle >= 292.5) return "northwest";
        return "unknown direction";
    }
}
