package pantz.mod.core.registry.datapack;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.PMTiers.PMArmorMaterials;
import pantz.mod.core.registry.PMItems;

import java.util.Map;

public class PMTrimMaterials {
    public static final ResourceKey<TrimMaterial> STEEL = createKey("steel");
    public static final ResourceKey<TrimMaterial> STEEL_DARKER = createKey("steel_darker");
    public static final ResourceKey<TrimMaterial> SULFUR = createKey("sulfur");

    public static void bootstrap(BootstrapContext<TrimMaterial> context) {
        register(context, STEEL, PMItems.STEEL_INGOT.get(), Style.EMPTY.withColor(0x818181), Map.of(PMArmorMaterials.STEEL, PantzMod.MOD_ID + "_steel_darker"));
        register(context, SULFUR, PMItems.SULFUR_CRYSTAL.get(), Style.EMPTY.withColor(0xf6e484), Map.of());

    }


    private static ResourceKey<TrimMaterial> createKey(String name) {
        return ResourceKey.create(Registries.TRIM_MATERIAL, PantzMod.location(name));
    }

    private static void register(BootstrapContext<TrimMaterial> context, ResourceKey<TrimMaterial> key, Item item, Style style, Map<Holder<ArmorMaterial>, String> overrideArmorMaterials) {
        ResourceLocation location = key.location();
        TrimMaterial trimmaterial = TrimMaterial.create(location.getNamespace() + "_" + location.getPath(), item, -1.0f, Component.translatable(Util.makeDescriptionId("trim_material", key.location())).withStyle(style), overrideArmorMaterials);
        context.register(key, trimmaterial);
    }
}
