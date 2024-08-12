package com.lumijiez.lumiscope.potions;

import com.lumijiez.lumiscope.util.Ref;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class CustomPotion extends Potion {

    public CustomPotion(String registryName, int indexStart, int indexFinal, boolean isBad, int color) {
        super(isBad, color);
        setRegistryName(registryName);
        setPotionName("effect." + registryName);
        setIconIndex(indexStart, indexFinal);
    }

    @Override
    public boolean hasStatusIcon() {
        Minecraft
                .getMinecraft()
                .getTextureManager()
                .bindTexture(new ResourceLocation(Ref.MODID , "textures/gui/potions/potion_effects.png"));
        return true;
    }
}
