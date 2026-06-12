package pantz.mod.core.other;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import pantz.mod.common.item.EntityFilterItem;
import pantz.mod.core.PantzMod;
import pantz.mod.core.registry.PMMenuTypes;
import pantz.mod.core.registry.datapack.PMTrimMaterials;

import static pantz.mod.core.registry.PMBlocks.*;
import static pantz.mod.core.registry.PMItems.*;

@Mod.EventBusSubscriber(modid = PantzMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PMClientCompat {
    public static void registerClientCompat() {
        registerRenderLayers();
        registerItemProperties();
        PMMenuTypes.registerScreenFactories();
        PMTrimMaterials.registerArmorMaterialOverrides();

    }

    @SuppressWarnings("deprecation")
    private static void registerRenderLayers() {
        for (RegistryObject<?> block : new RegistryObject[]{
                STEEL_BARS, STEEL_DOOR, STEEL_TRAPDOOR, STEEL_LANTERN, STEEL_CHAIN,
                SULFUR_CLUSTER, SMALL_SULFUR_BUD, MEDIUM_SULFUR_BUD, LARGE_SULFUR_BUD,
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
            ItemBlockRenderTypes.setRenderLayer((Block) block.get(), RenderType.cutout());
        }

        for (RegistryObject<?> block : new RegistryObject[]{
                ITEM_STAND, GLOW_ITEM_STAND,
                CHORUS_GLASS, CHORUS_GLASS_PANE, SOUL_GLASS, SOUL_GLASS_PANE, ECHO_GLASS, ECHO_GLASS_PANE
        }) {
            ItemBlockRenderTypes.setRenderLayer((Block) block.get(), RenderType.cutoutMipped());
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
        event.register((stack, index) -> index > 0 ? -1 : ((DyeableLeatherItem) stack.getItem()).getColor(stack), POTION_SATCHEL.get());
    }

    private static void registerItemProperties() {
        ItemProperties.register(ENTITY_FILTER.get(), PantzMod.location("mode"),
                (stack, level, entity, seed) -> {
            CompoundTag tag = stack.getOrCreateTag();
            int mode = tag.getInt(EntityFilterItem.MODE_KEY);
            return mode == 0 ? 0.0f : 1.0f;
        });
    }
}
