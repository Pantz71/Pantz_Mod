package pantz.mod.core.other;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import pantz.mod.core.PantzMod;

import java.util.Optional;

public class PMCriteriaTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, PantzMod.MOD_ID);

    public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> LOOK_AT_SCANNER = TRIGGERS.register("look_at_scanner", PlayerTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> REDIRECT_TELEPORTATION = TRIGGERS.register("redirect_teleportation", PlayerTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> USE_DYNAMITE = TRIGGERS.register("use_dynamite", PlayerTrigger::new);

    public static Criterion<PlayerTrigger.TriggerInstance> lookAtScanner() {
        return LOOK_AT_SCANNER.get().createCriterion(new PlayerTrigger.TriggerInstance(EntityPredicate.wrap(Optional.empty())));
    }

    public static Criterion<PlayerTrigger.TriggerInstance> redirectTeleportation() {
        return REDIRECT_TELEPORTATION.get().createCriterion(new PlayerTrigger.TriggerInstance(EntityPredicate.wrap(Optional.empty())));
    }

    public static Criterion<PlayerTrigger.TriggerInstance> useDynamite() {
        return USE_DYNAMITE.get().createCriterion(new PlayerTrigger.TriggerInstance(EntityPredicate.wrap(Optional.empty())));
    }
}
