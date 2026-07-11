package pantz.mod.core.data.client;

import com.teamabnormals.blueprint.core.data.client.BlueprintItemModelProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;
import pantz.mod.core.PantzMod;
import pantz.mod.core.registry.PMItems;

import static pantz.mod.core.registry.PMBlocks.*;
import static pantz.mod.core.registry.PMItems.*;

public class PMItemModelProvider extends BlueprintItemModelProvider {
    public PMItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PantzMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.generatedItem(PMItems.STEEL_INGOT, STEEL_NUGGET, STEEL_HORSE_ARMOR,
                SULFUR_DUST, SULFUR_CRYSTAL,
                DYNAMITE, COMBAT_DYNAMITE, FIERY_DYNAMITE, RED_ENVELOPE, KEY, SPIKE);

        this.generatedItem(RANDOMIZER, EQUALIZER, NOT_GATE,
                AND_GATE, OR_GATE, NOR_GATE, NAND_GATE, XNOR_GATE, XOR_GATE,
                ADVANCED_AND_GATE, ADVANCED_OR_GATE, ADVANCED_NOR_GATE, ADVANCED_NAND_GATE, ADVANCED_XNOR_GATE, ADVANCED_XOR_GATE,
                MAJORITY_GATE, MINORITY_GATE);

        this.trimmableArmorItem(STEEL_HELMET, STEEL_CHESTPLATE, STEEL_LEGGINGS, STEEL_BOOTS);

        this.handheldItem(STEEL_SWORD, STEEL_SHOVEL, STEEL_PICKAXE, STEEL_AXE, STEEL_HOE,
                TROWEL, EXCAVATOR, DIAMOND_EXCAVATOR, NETHERITE_EXCAVATOR, HAMMER, DIAMOND_HAMMER, NETHERITE_HAMMER);

        this.entityFilterItem(ENTITY_FILTER);
        this.honeyDeserializerItem(HONEY_DESERIALIZER);

    }

    private void entityFilterItem(DeferredItem<? extends ItemLike> item) {
        String name = name(item.get());
        ResourceLocation state = ResourceLocation.fromNamespaceAndPath(this.modid, "mode");

        for (String mode : new String[]{"include", "exclude"}) {
            this.withExistingParent(name + "_" + mode, "item/generated").texture("layer0", ResourceLocation.fromNamespaceAndPath(this.modid, "item/" + name + "_" + mode));
        }

        this.item(item, name + "_include", "generated")
                .override()
                .predicate(state, 0.0F)
                .model(new UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(this.modid, "item/" + name + "_include")))
                .end()
                .override()
                .predicate(state, 1.0F)
                .model(new UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(this.modid, "item/" + name + "_exclude")))
                .end();
    }

    private void honeyDeserializerItem(DeferredItem<? extends ItemLike> item) {
        String name = name(item.get());
        ResourceLocation state = ResourceLocation.fromNamespaceAndPath(this.modid, "level");
        float[] levels = new float[]{0.0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f};

        for (int i = 1; i < levels.length; i++) {
            String modelName = name + i;
            ResourceLocation texture = suffix(itemTexture(item.get()), String.valueOf(i));
            this.withExistingParent(modelName, "item/generated").texture("layer0", texture);
        }

        for (int i = 0; i < levels.length; i++) {
            float level = levels[i];
            String modelName = i == 0 ? name : name + i;

            this.item(item, name, "generated").override()
                    .predicate(state, level)
                    .model(new UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(this.modid, "item/" + modelName)))
                    .end();
        }
    }

    public static ResourceLocation suffix(ResourceLocation rl, String suffix) {
        return ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), rl.getPath() + suffix);
    }
}
