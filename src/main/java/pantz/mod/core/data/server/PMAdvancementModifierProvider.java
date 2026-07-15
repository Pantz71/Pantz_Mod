package pantz.mod.core.data.server;

import com.teamabnormals.blueprint.common.advancement.modification.AdvancementModifierProvider;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.EffectsChangedModifier;
import net.minecraft.advancements.critereon.MobEffectsPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import pantz.mod.core.PantzMod;
import pantz.mod.core.registry.PMMobEffects;

import java.util.concurrent.CompletableFuture;

public class PMAdvancementModifierProvider extends AdvancementModifierProvider {
    public PMAdvancementModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(PantzMod.MOD_ID, output, lookupProvider);
    }

    @Override
    protected void registerEntries(HolderLookup.Provider provider) {
        MobEffectsPredicate.Builder predicate = MobEffectsPredicate.Builder.effects();
        PMMobEffects.MOB_EFFECTS.getEntries().forEach(mobEffect -> {
            if (!mobEffect.get().isInstantenous()) predicate.and(mobEffect);
        });
        this.entry("nether/all_potions").selects("nether/all_potions").addModifier(new EffectsChangedModifier("all_effects", false, predicate.build().get()));
        this.entry("nether/all_effects").selects("nether/all_effects").addModifier(new EffectsChangedModifier("all_effects", false, predicate.build().get()));
    }
}
