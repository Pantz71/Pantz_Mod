package pantz.mod.core.registry;

import com.teamabnormals.blueprint.common.effect.BlueprintMobEffect;
import com.teamabnormals.blueprint.core.util.DataUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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

    public static final RegistryObject<MobEffect> PRECISION = MOB_EFFECTS.register("precision", () -> new BlueprintMobEffect(MobEffectCategory.BENEFICIAL, 0x91610e).addAttributeModifier(PMAttributes.PROJECTILE_BONUS_DAMAGE.get(), "1f3af57e-62e9-4f8f-92ec-63ea20acf371", 3, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<MobEffect> CLUMSINESS = MOB_EFFECTS.register("clumsiness", () -> new BlueprintMobEffect(MobEffectCategory.HARMFUL, 0x4a1900).addAttributeModifier(PMAttributes.PROJECTILE_BONUS_DAMAGE.get(), "5cb92f9d-d0ad-4180-9ab7-fd4a2c968679", -3, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<Potion> NORMAL_PRECISION = POTIONS.register("precision", () -> new Potion("precision", new MobEffectInstance(PRECISION.get(), 3600)));
    public static final RegistryObject<Potion> LONG_PRECISION = POTIONS.register("long_precision", () -> new Potion("precision", new MobEffectInstance(PRECISION.get(), 9600)));
    public static final RegistryObject<Potion> STRONG_PRECISION = POTIONS.register("strong_precision", () -> new Potion("precision", new MobEffectInstance(PRECISION.get(), 1800, 1)));

    public static final RegistryObject<Potion> NORMAL_CLUMSINESS = POTIONS.register("clumsiness", () -> new Potion("clumsiness", new MobEffectInstance(CLUMSINESS.get(), 1800)));
    public static final RegistryObject<Potion> LONG_CLUMSINESS = POTIONS.register("long_clumsiness", () -> new Potion("clumsiness", new MobEffectInstance(CLUMSINESS.get(), 4800)));
    public static final RegistryObject<Potion> STRONG_CLUMSINESS = POTIONS.register("strong_clumsiness", () -> new Potion("clumsiness", new MobEffectInstance(CLUMSINESS.get(), 900, 1)));

    public static void registerBrewingRecipes() {
        DataUtil.addMix(Potions.AWKWARD, Items.FLINT, NORMAL_PRECISION.get());
        DataUtil.addMix(NORMAL_PRECISION.get(), Items.REDSTONE, LONG_PRECISION.get());
        DataUtil.addMix(NORMAL_PRECISION.get(), Items.GLOWSTONE_DUST, STRONG_PRECISION.get());

        DataUtil.addMix(NORMAL_PRECISION.get(), Items.FERMENTED_SPIDER_EYE, NORMAL_CLUMSINESS.get());
        DataUtil.addMix(LONG_PRECISION.get(), Items.FERMENTED_SPIDER_EYE, LONG_CLUMSINESS.get());
        DataUtil.addMix(STRONG_PRECISION.get(), Items.FERMENTED_SPIDER_EYE, STRONG_CLUMSINESS.get());

        DataUtil.addMix(NORMAL_CLUMSINESS.get(), Items.REDSTONE, LONG_CLUMSINESS.get());
        DataUtil.addMix(NORMAL_CLUMSINESS.get(), Items.GLOWSTONE_DUST, STRONG_PRECISION.get());
    }

    public static void register(IEventBus bus) {
        MOB_EFFECTS.register(bus);
        POTIONS.register(bus);
    }
}
