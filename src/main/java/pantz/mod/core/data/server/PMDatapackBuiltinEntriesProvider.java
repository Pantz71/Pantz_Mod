package pantz.mod.core.data.server;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import pantz.mod.core.PantzMod;
import pantz.mod.core.registry.PMFeatures.*;
import pantz.mod.core.registry.datapack.PMBiomeModifiers;
import pantz.mod.core.registry.datapack.PMDamageTypes;
import pantz.mod.core.registry.datapack.PMTrimMaterials;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class PMDatapackBuiltinEntriesProvider extends DatapackBuiltinEntriesProvider {
    public PMDatapackBuiltinEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(PantzMod.MOD_ID));
    }

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, PMDamageTypes::bootstrap)
            .add(Registries.TRIM_MATERIAL, PMTrimMaterials::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, PMConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, PMPlacedFeatures::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, PMBiomeModifiers::bootstrap);
}
