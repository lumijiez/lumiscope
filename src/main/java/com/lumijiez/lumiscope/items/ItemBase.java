package com.lumijiez.lumiscope.items;

import com.lumijiez.lumiscope.Lumiscope;
import com.lumijiez.lumiscope.init.ModItems;
import com.lumijiez.lumiscope.util.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {
    public ItemBase(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CreativeTabs.COMBAT);

        ModItems.ITEMS.add(this);
    }
    @Override
    public void registerModels() {
        Lumiscope.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
