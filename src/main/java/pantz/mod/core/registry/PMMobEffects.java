package pantz.mod.core.registry;

import com.teamabnormals.blueprint.common.effect.BlueprintMobEffect;
import com.teamabnormals.blueprint.core.util.DataUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import pantz.mod.core.PantzMod;

public class PMMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, PantzMod.MOD_ID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, PantzMod.MOD_ID);

    public static final RegistryObject<MobEffect> HARVESTING = MOB_EFFECTS.register("harvesting", () -> new BlueprintMobEffect(MobEffectCategory.BENEFICIAL, 0x8a633f));

    public static final RegistryObject<Potion> HARVESTING_NORMAL = POTIONS.register("harvesting", () -> new Potion("harvesting", new MobEffectInstance(HARVESTING.get(), 1200)));
    public static final RegistryObject<Potion> HARVESTING_LONGER = POTIONS.register("longer_harvesting", () -> new Potion("harvesting", new MobEffectInstance(HARVESTING.get(), 1800)));
    public static final RegistryObject<Potion> HARVESTING_STRONGER = POTIONS.register("stronger_harvesting", () -> new Potion("harvesting", new MobEffectInstance(HARVESTING.get(), 900, 1)));

    public static void registerBrewingRecipes() {
        DataUtil.addMix(Potions.AWKWARD, Items.AMETHYST_BLOCK, HARVESTING_NORMAL.get());
        DataUtil.addMix(HARVESTING_NORMAL.get(), Items.REDSTONE, HARVESTING_LONGER.get());
        DataUtil.addMix(HARVESTING_NORMAL.get(), Items.GLOWSTONE_DUST, HARVESTING_STRONGER.get());
    }


    public static void register(IEventBus bus) {
        MOB_EFFECTS.register(bus);
        POTIONS.register(bus);
    }
}
