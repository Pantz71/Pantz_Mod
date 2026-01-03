package pantz.mod.core.data.client;

import com.teamabnormals.blueprint.core.data.client.BlueprintItemModelProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import pantz.mod.core.PantzMod;

import static pantz.mod.core.registry.PMBlocks.*;
import static pantz.mod.core.registry.PMItems.*;

public class PMItemModelProvider extends BlueprintItemModelProvider {
    public PMItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PantzMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.generatedItem(STEEL_INGOT, STEEL_NUGGET, STEEL_HORSE_ARMOR,
                SULFUR_DUST, SULFUR_CRYSTAL, HONEY_DESERIALIZER, ENTITY_FILTER, CACTUS_KEY);

        this.generatedItem(NOT_GATE,
                AND_GATE, OR_GATE, NOR_GATE, NAND_GATE, XNOR_GATE, XOR_GATE,
                ADVANCED_AND_GATE, ADVANCED_OR_GATE, ADVANCED_NOR_GATE, ADVANCED_NAND_GATE, ADVANCED_XNOR_GATE, ADVANCED_XOR_GATE,
                MAJORITY_GATE, MINORITY_GATE);

        this.trimmableArmorItem(STEEL_HELMET, STEEL_CHESTPLATE, STEEL_LEGGINGS, STEEL_BOOTS);

        this.handheldItem(STEEL_SWORD, STEEL_SHOVEL, STEEL_PICKAXE, STEEL_AXE, STEEL_HOE,
                TROWEL, EXCAVATOR, DIAMOND_EXCAVATOR, NETHERITE_EXCAVATOR);
    }
}
