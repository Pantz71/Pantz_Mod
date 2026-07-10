package pantz.mod.core.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import pantz.mod.common.inventory.*;
import pantz.mod.core.PantzMod;

@EventBusSubscriber(modid = PantzMod.MOD_ID, value = Dist.CLIENT)
public class PMMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, PantzMod.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<TrashCanMenu>> TRASH_CAN = MENUS.register("trash_can", () -> new MenuType<>(TrashCanMenu::new, FeatureFlags.VANILLA_SET));
    public static final DeferredHolder<MenuType<?>, MenuType<FeedingTroughMenu>> FEEDING_TROUGH = MENUS.register("feeding_trough", () -> new MenuType<>(FeedingTroughMenu::new, FeatureFlags.VANILLA_SET));
    public static final DeferredHolder<MenuType<?>, MenuType<PotionSatchelMenu>> POTION_SATCHEL = MENUS.register("potion_satchel", () -> new MenuType<>(PotionSatchelMenu::new, FeatureFlags.VANILLA_SET));

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(TRASH_CAN.get(), TrashCanScreen::new);
        event.register(FEEDING_TROUGH.get(), FeedingTroughScreen::new);
        event.register(POTION_SATCHEL.get(), PotionSatchelScreen::new);
    }
}
