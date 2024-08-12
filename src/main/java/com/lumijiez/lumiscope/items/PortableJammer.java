package com.lumijiez.lumiscope.items;

import com.lumijiez.lumiscope.potions.PotionManager;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

public class PortableJammer extends ItemBase {
    public PortableJammer() {
        super("portable_jammer");
        setMaxStackSize(1);
        setMaxDamage(5);
    }

    @Override
    @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);

        if (!worldIn.isRemote) {
            PotionEffect effect = new PotionEffect(PotionManager.JAMMERED_POTION_EFFECT, 24000, 0);
            playerIn.addPotionEffect(effect);

            itemStack.damageItem(1, playerIn);

            playerIn.getCooldownTracker().setCooldown(this, 36000);
        } else {
            return new ActionResult<>(EnumActionResult.PASS, itemStack);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }
}
