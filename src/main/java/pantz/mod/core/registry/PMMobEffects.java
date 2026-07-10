package pantz.mod.core.registry;

import com.teamabnormals.blueprint.common.effect.BlueprintMobEffect;
import com.teamabnormals.blueprint.core.util.DataUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import pantz.mod.core.PantzMod;

@EventBusSubscriber(modid = PantzMod.MOD_ID)
public class PMMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, PantzMod.MOD_ID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(BuiltInRegistries.POTION, PantzMod.MOD_ID);

    public static final DeferredHolder<MobEffect, MobEffect> PRECISION = MOB_EFFECTS.register("precision", () -> new BlueprintMobEffect(MobEffectCategory.BENEFICIAL, 0x91610e).addAttributeModifier(PMAttributes.PROJECTILE_BONUS_DAMAGE, PantzMod.location("effect.precision"), 3, AttributeModifier.Operation.ADD_VALUE));
    public static final DeferredHolder<MobEffect, MobEffect> CLUMSINESS = MOB_EFFECTS.register("clumsiness", () -> new BlueprintMobEffect(MobEffectCategory.HARMFUL, 0x4a1900).addAttributeModifier(PMAttributes.PROJECTILE_BONUS_DAMAGE, PantzMod.location("effect.clumsiness"), -3, AttributeModifier.Operation.ADD_VALUE));

    public static final DeferredHolder<Potion, Potion> NORMAL_PRECISION = POTIONS.register("precision", () -> new Potion("precision", new MobEffectInstance(PRECISION, 3600)));
    public static final DeferredHolder<Potion, Potion> LONG_PRECISION = POTIONS.register("long_precision", () -> new Potion("precision", new MobEffectInstance(PRECISION, 9600)));
    public static final DeferredHolder<Potion, Potion> STRONG_PRECISION = POTIONS.register("strong_precision", () -> new Potion("precision", new MobEffectInstance(PRECISION, 1800, 1)));

    public static final DeferredHolder<Potion, Potion> NORMAL_CLUMSINESS = POTIONS.register("clumsiness", () -> new Potion("clumsiness", new MobEffectInstance(CLUMSINESS, 1800)));
    public static final DeferredHolder<Potion, Potion> LONG_CLUMSINESS = POTIONS.register("long_clumsiness", () -> new Potion("clumsiness", new MobEffectInstance(CLUMSINESS, 4800)));
    public static final DeferredHolder<Potion, Potion> STRONG_CLUMSINESS = POTIONS.register("strong_clumsiness", () -> new Potion("clumsiness", new MobEffectInstance(CLUMSINESS, 900, 1)));

    @SubscribeEvent
    public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();

        builder.addMix(Potions.AWKWARD, Items.FLINT, NORMAL_PRECISION);
        builder.addMix(NORMAL_PRECISION, Items.REDSTONE, LONG_PRECISION);
        builder.addMix(NORMAL_PRECISION, Items.GLOWSTONE_DUST, STRONG_PRECISION);

        builder.addMix(NORMAL_PRECISION, Items.FERMENTED_SPIDER_EYE, NORMAL_CLUMSINESS);
        builder.addMix(LONG_PRECISION, Items.FERMENTED_SPIDER_EYE, LONG_CLUMSINESS);
        builder.addMix(STRONG_PRECISION, Items.FERMENTED_SPIDER_EYE, STRONG_CLUMSINESS);

        builder.addMix(NORMAL_CLUMSINESS, Items.REDSTONE, LONG_CLUMSINESS);
        builder.addMix(NORMAL_CLUMSINESS, Items.GLOWSTONE_DUST, STRONG_PRECISION);
    }

    public static void register(IEventBus bus) {
        MOB_EFFECTS.register(bus);
        POTIONS.register(bus);
    }
}
