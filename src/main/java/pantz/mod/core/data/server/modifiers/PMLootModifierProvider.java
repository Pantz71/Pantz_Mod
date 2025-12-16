package pantz.mod.core.data.server.modifiers;

import com.teamabnormals.blueprint.common.loot.modification.LootModifierProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import pantz.mod.core.PantzMod;

import java.util.concurrent.CompletableFuture;

public class PMLootModifierProvider extends LootModifierProvider {
    public PMLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(PantzMod.MOD_ID, output, lookupProvider);
    }

    @Override
    protected void registerEntries(HolderLookup.Provider provider) {

    }
}
