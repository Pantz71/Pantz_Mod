package pantz.mod.core.data.client;

import com.teamabnormals.blueprint.core.data.client.BlueprintItemModelProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import pantz.mod.core.PantzMod;

import static pantz.mod.core.registry.PMItems.*;

public class PMItemModelProvider extends BlueprintItemModelProvider {
    public PMItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PantzMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.generatedItem(STEEL_INGOT, STEEL_NUGGET, STEEL_HORSE_ARMOR,
                SULFUR_DUST, SULFUR_CRYSTAL, HONEY_DESERIALIZER);

        this.trimmableArmorItem(STEEL_HELMET, STEEL_CHESTPLATE, STEEL_LEGGINGS, STEEL_BOOTS);

        this.handheldItem(STEEL_SWORD, STEEL_SHOVEL, STEEL_PICKAXE, STEEL_AXE, STEEL_HOE,
                TROWEL, EXCAVATOR, DIAMOND_EXCAVATOR, NETHERITE_EXCAVATOR);
    }
}
