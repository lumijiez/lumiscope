package com.lumijiez.lumiscope.handlers;

import com.lumijiez.lumiscope.init.ModItems;
import com.lumijiez.lumiscope.network.handlers.LongRadarPacketHandler;
import com.lumijiez.lumiscope.network.handlers.ShortRadarPacketHandler;
import com.lumijiez.lumiscope.potions.PotionManager;
import com.lumijiez.lumiscope.util.IHasModel;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class RegistryHandler {
    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        for (Item item : ModItems.ITEMS) {
            if(item instanceof IHasModel) {
                ((IHasModel) item).registerModels();
            }
        }
    }

    public static void preInitRegistry() {
        PotionManager.registerPotions();

        ShortRadarPacketHandler.registerMessages();
        LongRadarPacketHandler.registerMessages();
    }
}
