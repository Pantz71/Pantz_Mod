package pantz.mod.core.other;

import com.teamabnormals.blueprint.core.util.TradeUtil;
import com.teamabnormals.blueprint.core.util.TradeUtil.BlueprintTrade;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import pantz.mod.core.PantzMod;

import static pantz.mod.core.registry.PMBlocks.*;
import static pantz.mod.core.registry.PMItems.*;

@Mod.EventBusSubscriber(modid = PantzMod.MOD_ID)
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
                new BlueprintTrade(5, RED_ENVELOPE.get(), 1, 2, 30)
        );

    }

    @SubscribeEvent
    public static void onVillagerTradesEvent(VillagerTradesEvent event) {
        TradeUtil.addVillagerTrades(event, VillagerProfession.CARTOGRAPHER, TradeUtil.EXPERT,
                new BlueprintTrade(3, EARTH_GLOBE.get().asItem(), 1, 8, 10)
        );

        TradeUtil.addVillagerTrades(event, VillagerProfession.ARMORER, TradeUtil.JOURNEYMAN,
                new ItemsForEmeralds(STEEL_CHESTPLATE.get(), 12, 3, 30, 0.2f),
                new ItemsForEmeralds(STEEL_HELMET.get(), 8, 3, 30, 0.2f),
                new ItemsForEmeralds(STEEL_LEGGINGS.get(), 8, 3, 30, 0.2f),
                new ItemsForEmeralds(STEEL_BOOTS.get(), 6, 3, 30, 0.2f),
                new EmeraldForItems(STEEL_INGOT.get(), 4, 12, 20)
        );

        TradeUtil.addVillagerTrades(event, VillagerProfession.TOOLSMITH, TradeUtil.JOURNEYMAN,
                new ItemsForEmeralds(STEEL_AXE.get(), 8, 3, 30, 0.2f),
                new ItemsForEmeralds(STEEL_PICKAXE.get(), 8, 3, 30, 0.2f),
                new ItemsForEmeralds(EXCAVATOR.get(), 8, 3, 30, 0.2f),
                new ItemsForEmeralds(HAMMER.get(), 8, 3, 30, 0.2f),
                new EmeraldForItems(STEEL_INGOT.get(), 4, 12, 20)
        );

        TradeUtil.addVillagerTrades(event, VillagerProfession.TOOLSMITH, TradeUtil.EXPERT,
                new EnchantedItemForEmeralds(DIAMOND_EXCAVATOR.get(), 16, 3, 30, 0.2F),
                new EnchantedItemForEmeralds(DIAMOND_HAMMER.get(), 16, 3, 30, 0.2F)
        );

        TradeUtil.addVillagerTrades(event, VillagerProfession.WEAPONSMITH, TradeUtil.JOURNEYMAN,
                new ItemsForEmeralds(STEEL_SWORD.get(), 6, 3, 30, 0.2f),
                new EmeraldForItems(STEEL_INGOT.get(), 4, 12, 20)
        );
    }

    static class EmeraldForItems implements VillagerTrades.ItemListing {
        private final Item item;
        private final int cost;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public EmeraldForItems(ItemLike pItem, int pCost, int pMaxUses, int pVillagerXp) {
            this.item = pItem.asItem();
            this.cost = pCost;
            this.maxUses = pMaxUses;
            this.villagerXp = pVillagerXp;
            this.priceMultiplier = 0.05F;
        }

        public MerchantOffer getOffer(Entity pTrader, RandomSource pRandom) {
            ItemStack itemstack = new ItemStack(this.item, this.cost);
            return new MerchantOffer(itemstack, new ItemStack(Items.EMERALD), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }

    static class EnchantedItemForEmeralds implements VillagerTrades.ItemListing {
        private final ItemStack itemStack;
        private final int baseEmeraldCost;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public EnchantedItemForEmeralds(Item pItem, int pBaseEmeraldCost, int pMaxUses, int pVillagerXp) {
            this(pItem, pBaseEmeraldCost, pMaxUses, pVillagerXp, 0.05F);
        }

        public EnchantedItemForEmeralds(Item pItem, int pBaseEmeraldCost, int pMaxUses, int pVillagerXp, float pPriceMultiplier) {
            this.itemStack = new ItemStack(pItem);
            this.baseEmeraldCost = pBaseEmeraldCost;
            this.maxUses = pMaxUses;
            this.villagerXp = pVillagerXp;
            this.priceMultiplier = pPriceMultiplier;
        }

        public MerchantOffer getOffer(Entity pTrader, RandomSource pRandom) {
            int i = 5 + pRandom.nextInt(15);
            ItemStack itemstack = EnchantmentHelper.enchantItem(pRandom, new ItemStack(this.itemStack.getItem()), i, false);
            int j = Math.min(this.baseEmeraldCost + i, 64);
            ItemStack itemstack1 = new ItemStack(Items.EMERALD, j);
            return new MerchantOffer(itemstack1, itemstack, this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }

    static class ItemsForEmeralds implements VillagerTrades.ItemListing {
        private final ItemStack itemStack;
        private final int emeraldCost;
        private final int numberOfItems;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public ItemsForEmeralds(Block pBlock, int pEmeraldCost, int pNumberOfItems, int pMaxUses, int pVillagerXp) {
            this(new ItemStack(pBlock), pEmeraldCost, pNumberOfItems, pMaxUses, pVillagerXp);
        }

        public ItemsForEmeralds(Item pItem, int pEmeraldCost, int pNumberOfItems, int pVillagerXp) {
            this(new ItemStack(pItem), pEmeraldCost, pNumberOfItems, 12, pVillagerXp);
        }

        public ItemsForEmeralds(Item pItem, int pEmeraldCost, int pMaxUses, int pVillagerXp, float priceMultiplier) {
            this(new ItemStack(pItem), pEmeraldCost, 1, pMaxUses, pVillagerXp, priceMultiplier);
        }

        public ItemsForEmeralds(Item pItem, int pEmeraldCost, int pNumberOfItems, int pMaxUses, int pVillagerXp, float priceMultiplier) {
            this(new ItemStack(pItem), pEmeraldCost, pNumberOfItems, pMaxUses, pVillagerXp, priceMultiplier);
        }

        public ItemsForEmeralds(ItemStack pItemStack, int pEmeraldCost, int pNumberOfItems, int pMaxUses, int pVillagerXp) {
            this(pItemStack, pEmeraldCost, pNumberOfItems, pMaxUses, pVillagerXp, 0.05F);
        }

        public ItemsForEmeralds(ItemStack pItemStack, int pEmeraldCost, int pNumberOfItems, int pMaxUses, int pVillagerXp, float pPriceMultiplier) {
            this.itemStack = pItemStack;
            this.emeraldCost = pEmeraldCost;
            this.numberOfItems = pNumberOfItems;
            this.maxUses = pMaxUses;
            this.villagerXp = pVillagerXp;
            this.priceMultiplier = pPriceMultiplier;
        }

        public MerchantOffer getOffer(Entity pTrader, RandomSource pRandom) {
            return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(this.itemStack.getItem(), this.numberOfItems), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }
}
