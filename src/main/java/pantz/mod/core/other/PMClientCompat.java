package pantz.mod.core.other;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.GrassColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import pantz.mod.common.item.EntityFilterItem;
import pantz.mod.common.utils.FilterMode;
import pantz.mod.core.PantzMod;
import pantz.mod.core.registry.PMBlocks;
import pantz.mod.core.registry.PMDataComponents;
import pantz.mod.core.registry.PMItems;

import static pantz.mod.core.registry.PMBlocks.*;
import static pantz.mod.core.registry.PMItems.HONEY_DESERIALIZER;
import static pantz.mod.core.registry.PMItems.POTION_SATCHEL;

@EventBusSubscriber(modid = PantzMod.MOD_ID, value = Dist.CLIENT)
public class PMClientCompat {
    public static void registerClientCompat() {
        registerRenderLayers();
        registerItemProperties();
        PMBlocks.setupTabs();
        PMItems.setupTabs();
    }

    @SuppressWarnings("deprecation")
    private static void registerRenderLayers() {
        for (DeferredBlock<?> block : new DeferredBlock[]{
                STEEL_BARS, STEEL_DOOR, STEEL_TRAPDOOR, STEEL_LANTERN, STEEL_CHAIN,
                SMALL_SULFUR_BUD, MEDIUM_SULFUR_BUD, LARGE_SULFUR_BUD, SULFUR_CLUSTER,
                STONE_PEDESTAL, DEEPSLATE_PEDESTAL, BLACKSTONE_PEDESTAL, QUARTZ_PEDESTAL, PURPUR_PEDESTAL, PRISMARINE_PEDESTAL,
                RANDOMIZER, EQUALIZER,
                NOT_GATE, AND_GATE, OR_GATE, NOR_GATE, NAND_GATE, XNOR_GATE, XOR_GATE,
                ADVANCED_AND_GATE, ADVANCED_OR_GATE, ADVANCED_NOR_GATE, ADVANCED_NAND_GATE, ADVANCED_XNOR_GATE, ADVANCED_XOR_GATE,
                MAJORITY_GATE, MINORITY_GATE,
                ROPE_LADDER,
                WHITE_PAPER_LANTERN, ORANGE_PAPER_LANTERN, MAGENTA_PAPER_LANTERN,
                LIGHT_BLUE_PAPER_LANTERN, YELLOW_PAPER_LANTERN, LIME_PAPER_LANTERN, PINK_PAPER_LANTERN, GRAY_PAPER_LANTERN, LIGHT_GRAY_PAPER_LANTERN,
                CYAN_PAPER_LANTERN, PURPLE_PAPER_LANTERN, BLUE_PAPER_LANTERN, BROWN_PAPER_LANTERN, GREEN_PAPER_LANTERN, RED_PAPER_LANTERN, BLACK_PAPER_LANTERN,
                ORNAMENT_FIRECRACKERS, ORNAMENT_LUCKY_COINS, SPIKE
        }) {
            ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout());
        }

        for (DeferredBlock<?> block : new DeferredBlock[]{
                ITEM_STAND, GLOW_ITEM_STAND,
                CHORUS_GLASS, CHORUS_GLASS_PANE, SOUL_GLASS, SOUL_GLASS_PANE
        }) {
            ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutoutMipped());
        }

    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, index) -> level != null && pos != null ? BiomeColors.getAverageWaterColor(level, pos) : -1, SPRINKLER.get());
        event.register((state, level, pos, index) -> level != null && pos != null ? BiomeColors.getAverageGrassColor(level, pos) : -1, SUGAR_CANE_BLOCK.get());

    }

    @SubscribeEvent
    public static void registerItemColor(RegisterColorHandlersEvent.Item event) {
        event.register((stack, index) -> GrassColor.get(0.5D, 1.0D), SUGAR_CANE_BLOCK.get());
        event.register((stack, index) -> index > 0 ? -1 : DyedItemColor.getOrDefault(stack, -6265536), POTION_SATCHEL.get());
    }

    private static void registerItemProperties() {
        ItemProperties.register(PMItems.ENTITY_FILTER.get(), PantzMod.location("mode"),
                (stack, level, entity, seed) -> {
                    FilterMode mode = stack.getOrDefault(PMDataComponents.FILTER_MODE, FilterMode.INCLUDE);
                    return mode == FilterMode.INCLUDE ? 0.0f : 1.0f;
                });
        ItemProperties.register(HONEY_DESERIALIZER.get(), PantzMod.location("level"),
                (stack, level, entity, seed) -> stack.getOrDefault(PMDataComponents.WAX_LEVEL.get(), 0.0F));
    }
}
