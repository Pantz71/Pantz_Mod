package pantz.mod.core.other;

import com.teamabnormals.blueprint.core.util.TradeUtil;
import com.teamabnormals.blueprint.core.util.TradeUtil.BlueprintTrade;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import pantz.mod.core.PantzMod;
import pantz.mod.core.registry.PMItems;

import static pantz.mod.core.registry.PMBlocks.*;
import static pantz.mod.core.registry.PMItems.*;

@EventBusSubscriber(modid = PantzMod.MOD_ID)
public class PMTrades {

    @SubscribeEvent
    public static void onWandererTradesEvent(WandererTradesEvent event) {
        TradeUtil.addWandererTrades(event,
                new BlueprintTrade(3, EARTH_GLOBE.get().asItem(), 1, 4, 10),
                new BlueprintTrade(3, MERCURY_GLOBE.get().asItem(), 1, 4, 10),
                new BlueprintTrade(3, VENUS_GLOBE.get().asItem(), 1, 4, 10),
                new BlueprintTrade(3, MARS_GLOBE.get().asItem(), 1, 4, 10),
                new BlueprintTrade(3, MOON_GLOBE.get().asItem(), 1, 4, 10)
        );

        TradeUtil.addRareWandererTrades(event,
                new BlueprintTrade(2, ENTITY_FILTER.get(), 1, 2, 15),
                new BlueprintTrade(2, RED_ENVELOPE.get(), 1, 2, 15)
        );

    }

    @SubscribeEvent
    public static void onVillagerTradesEvent(VillagerTradesEvent event) {
        TradeUtil.addVillagerTrades(event, VillagerProfession.CARTOGRAPHER, TradeUtil.EXPERT,
                new BlueprintTrade(3, EARTH_GLOBE.get().asItem(), 1, 8, 10)
        );

        TradeUtil.addVillagerTrades(event, VillagerProfession.ARMORER, TradeUtil.JOURNEYMAN,
                new ItemsForEmeralds(STEEL_CHESTPLATE.get(), 12, 1, 3, 30, 0.2f),
                new ItemsForEmeralds(STEEL_HELMET.get(), 8, 1, 3, 30, 0.2f),
                new ItemsForEmeralds(STEEL_LEGGINGS.get(), 8, 1, 3, 30, 0.2f),
                new ItemsForEmeralds(STEEL_BOOTS.get(), 6, 1, 3, 30, 0.2f),
                new EmeraldForItems(PMItems.STEEL_INGOT.get(), 4, 12, 20)
        );

        TradeUtil.addVillagerTrades(event, VillagerProfession.TOOLSMITH, TradeUtil.JOURNEYMAN,
                new ItemsForEmeralds(STEEL_AXE.get(), 8, 1, 3, 30, 0.2f),
                new ItemsForEmeralds(STEEL_PICKAXE.get(), 1, 8, 3, 30, 0.2f),
                new ItemsForEmeralds(EXCAVATOR.get(), 8, 1, 3, 30, 0.2f),
                new ItemsForEmeralds(HAMMER.get(), 8, 1, 3, 30, 0.2f),
                new EmeraldForItems(PMItems.STEEL_INGOT.get(), 4, 12, 20)
        );

        TradeUtil.addVillagerTrades(event, VillagerProfession.TOOLSMITH, TradeUtil.EXPERT,
                new EnchantedItemForEmeralds(DIAMOND_EXCAVATOR.get(), 16, 3, 30, 0.2F),
                new EnchantedItemForEmeralds(DIAMOND_HAMMER.get(), 16, 3, 30, 0.2F)
        );

        TradeUtil.addVillagerTrades(event, VillagerProfession.WEAPONSMITH, TradeUtil.JOURNEYMAN,
                new ItemsForEmeralds(STEEL_SWORD.get(), 6, 1, 3, 30, 0.2f),
                new EmeraldForItems(PMItems.STEEL_INGOT.get(), 4, 12, 20)
        );
    }
}
