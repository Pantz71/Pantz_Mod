package pantz.mod.core.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import pantz.mod.core.PantzMod;

public class PMAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, PantzMod.MOD_ID);

    public static final DeferredHolder<Attribute, Attribute> PROJECTILE_BONUS_DAMAGE = register("projectile_bonus_damage", 0.0D, -1024.0D, 1024.0D, true);

    private static DeferredHolder<Attribute, Attribute> register(String name, double defaultValue, double minimumValue, double maximumValue, boolean syncable) {
        return ATTRIBUTES.register(name, () -> new RangedAttribute("attribute." + PantzMod.MOD_ID + ".name.generic." + name, defaultValue, minimumValue, maximumValue).setSyncable(syncable));
    }

}
