package pantz.mod.core.other;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.tags.PMItemTags;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class PMTiers {
    public static class PMItemTiers {
        public static final Tier STEEL = new SimpleTier(BlockTags.INCORRECT_FOR_IRON_TOOL, 780, 7.0f, 3f, 12, () -> Ingredient.of(PMItemTags.INGOTS_STEEL));
    }

    public static class PMArmorMaterials {
        public static final Holder<ArmorMaterial> STEEL = register("steel",
                Util.make(new EnumMap<>(ArmorItem.Type.class), attribute -> {
                    attribute.put(ArmorItem.Type.BOOTS, 2);
                    attribute.put(ArmorItem.Type.LEGGINGS, 6);
                    attribute.put(ArmorItem.Type.CHESTPLATE, 7);
                    attribute.put(ArmorItem.Type.HELMET, 2);
                    attribute.put(ArmorItem.Type.BODY, 6);
                }), 11, 0f, 0f, Ingredient.of(PMItemTags.INGOTS_STEEL));

        private static Holder<ArmorMaterial> register(String name, EnumMap<ArmorItem.Type, Integer> typeProtection,
                                                      int enchantability, float toughness, float knockbackResistance,
                                                      Ingredient repairItem) {
            ResourceLocation location = PantzMod.location(name);
            Holder<SoundEvent> equipSound = SoundEvents.ARMOR_EQUIP_NETHERITE;
            Supplier<Ingredient> ingredient = () -> repairItem;
            List<ArmorMaterial.Layer> layers = List.of(new ArmorMaterial.Layer(location));

            EnumMap<ArmorItem.Type, Integer> typeMap = new EnumMap<>(ArmorItem.Type.class);
            for (ArmorItem.Type type : ArmorItem.Type.values()) {
                typeMap.put(type, typeProtection.get(type));
            }

            return Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL, location,
                    new ArmorMaterial(typeProtection, enchantability, equipSound, ingredient, layers, toughness, knockbackResistance));
        }
    }
}
