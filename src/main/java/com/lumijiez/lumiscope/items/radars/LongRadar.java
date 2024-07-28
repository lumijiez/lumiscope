package com.lumijiez.lumiscope.items.radars;

import com.lumijiez.lumiscope.items.ItemBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
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
import java.util.Random;

public class LongRadar extends ItemBase {
    private static final Random RANDOM = new Random();

    public LongRadar() {
        super("long_radar");
        setMaxStackSize(1);
        setMaxDamage(100);
    }

    @Override
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

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (!world.isRemote) {
            player.getHeldItem(hand).damageItem(1, player);

            ITextComponent warningMessage = new TextComponentString("WARNING: Cannot detect players less than 300 meters from you!")
                    .setStyle(new Style().setColor(TextFormatting.RED));
            player.sendMessage(warningMessage);

            boolean playerNearby = false;

            for (EntityPlayer otherPlayer : world.playerEntities) {
                if (!otherPlayer.equals(player)
                    && otherPlayer.getActivePotionEffects().stream()
                        .noneMatch(potionEffect -> potionEffect.getPotion() == MobEffects.INVISIBILITY)) {

                    double deltaX = otherPlayer.posX - player.posX;
                    double deltaZ = otherPlayer.posZ - player.posZ;
                    double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

                    if (distance <= 300) continue;

                    double angle = Math.atan2(deltaZ, deltaX);
                    double angleDegrees = Math.toDegrees(angle) - 90;
                    angleDegrees = (angleDegrees + 360) % 360;

                    angleDegrees = (angleDegrees > 180) ? angleDegrees - 360 : angleDegrees;

                    double margin = RANDOM.nextDouble() * 20;
                    double halfMargin = margin / 2;

                    double startAngle = (angleDegrees - halfMargin + 360) % 360;
                    startAngle = (startAngle > 180) ? startAngle - 360 : startAngle;

                    double endAngle = (angleDegrees + halfMargin + 360) % 360;
                    endAngle = (endAngle > 180) ? endAngle - 360 : endAngle;

                    String distanceMessage = distance > 500000 ? " (extremely far)" :
                            distance > 50000 ? " (very far)" :
                                    distance > 5000 ? " (far)" : "";


                    ITextComponent intervalMessage;

                    intervalMessage = new TextComponentString(String.format("Look in the interval of %.1f - %.1f degrees%s", startAngle, endAngle, distanceMessage))
                            .setStyle(new Style().setColor(TextFormatting.GREEN));

                    player.sendMessage(intervalMessage);
                    playerNearby = true;
                }
            }

            if (!playerNearby) {
                ITextComponent noPlayersMessage = new TextComponentString("No players found.")
                        .setStyle(new Style().setColor(TextFormatting.GRAY));
                player.sendMessage(noPlayersMessage);
            }

            player.getCooldownTracker().setCooldown(this, 3600);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }
}
