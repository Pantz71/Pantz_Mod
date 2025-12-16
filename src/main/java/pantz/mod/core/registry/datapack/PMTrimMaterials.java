package pantz.mod.core.registry.datapack;

import com.teamabnormals.blueprint.core.api.BlueprintTrims;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraftforge.registries.ForgeRegistries;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.PMTiers.PMArmorMaterials;
import pantz.mod.core.registry.PMItems;

import java.util.Map;

public class PMTrimMaterials {
    public static final ResourceKey<TrimMaterial> STEEL = createKey("steel");
    public static final ResourceKey<TrimMaterial> STEEL_DARKER = createKey("steel_darker");
    public static final ResourceKey<TrimMaterial> SULFUR = createKey("sulfur");

    public static void bootstrap(BootstapContext<TrimMaterial> context) {
        register(context, STEEL, PMItems.STEEL_INGOT.get(), Style.EMPTY.withColor(0x818181), Map.of());
        register(context, SULFUR, PMItems.SULFUR.get(), Style.EMPTY.withColor(0xf6e484), Map.of());
    }

    public static void registerArmorMaterialOverrides() {
        registerArmorMaterialOverrides(STEEL, PMArmorMaterials.STEEL, STEEL_DARKER);
    }

    public static void registerArmorMaterialOverrides(ResourceKey<TrimMaterial> trim, ArmorMaterial material, ResourceKey<TrimMaterial> darkerTrim) {
        BlueprintTrims.registerArmorMaterialOverrides(trim, Map.of(material, darkerTrim.location().getNamespace() + "_" + darkerTrim.location().getPath()));
    }

    private static ResourceKey<TrimMaterial> createKey(String name) {
        return ResourceKey.create(Registries.TRIM_MATERIAL, PantzMod.location(name));
    }

    private static void register(BootstapContext<TrimMaterial> context, ResourceKey<TrimMaterial> key, Item item, Style style, Map<ArmorMaterials, String> overrides) {
        ResourceLocation location = key.location();
        context.register(key, new TrimMaterial(location.getNamespace() + "_" + location.getPath(), ForgeRegistries.ITEMS.getHolder(item).get(), -1.0F, overrides, Component.translatable(Util.makeDescriptionId("trim_material", location)).withStyle(style)));
    }
}
