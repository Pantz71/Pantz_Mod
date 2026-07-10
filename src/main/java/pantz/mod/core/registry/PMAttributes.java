package pantz.mod.core.registry;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import pantz.mod.core.PantzMod;

public class PMAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, PantzMod.MOD_ID);

    public static final RegistryObject<Attribute> PROJECTILE_BONUS_DAMAGE = register("projectile_bonus_damage", 0.0D, -1024.0D, 1024.0D, true);

    private static RegistryObject<Attribute> register(String name, double defaultValue, double minimumValue, double maximumValue, boolean syncable) {
        return ATTRIBUTES.register(name, () -> new RangedAttribute("attribute." + PantzMod.MOD_ID + ".name.generic." + name, defaultValue, minimumValue, maximumValue).setSyncable(syncable));
    }

}
