package pantz.mod.core.registry.datapack;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;
import pantz.mod.core.PantzMod;

public class PMDamageTypes {

    public static ResourceKey<DamageType> SPIKE = createKey("spike");

    public static void bootstrap(BootstapContext<DamageType> context) {
        context.register(SPIKE, new DamageType(PantzMod.MOD_ID + ".spike", 0.1f));
    }

    public static DamageSource spike(Level level) {
        return level.damageSources().source(SPIKE);
    }

    public static ResourceKey<DamageType> createKey(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, PantzMod.location(name));
    }
}
