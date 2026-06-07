package pantz.mod.core.registry;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import pantz.mod.common.inventory.FeedingTroughMenu;
import pantz.mod.common.inventory.FeedingTroughScreen;
import pantz.mod.common.inventory.TrashCanMenu;
import pantz.mod.common.inventory.TrashCanScreen;
import pantz.mod.core.PantzMod;

public class PMMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, PantzMod.MOD_ID);

    public static final RegistryObject<MenuType<TrashCanMenu>> TRASH_CAN = MENU_TYPES.register("trash_can", () -> new MenuType<>(TrashCanMenu::new, FeatureFlags.VANILLA_SET));
    public static final RegistryObject<MenuType<FeedingTroughMenu>> FEEDING_TROUGH = MENU_TYPES.register("feeding_trough", () -> new MenuType<>(FeedingTroughMenu::new, FeatureFlags.VANILLA_SET));

    public static void registerScreenFactories() {
        MenuScreens.register(TRASH_CAN.get(), TrashCanScreen::new);
        MenuScreens.register(FEEDING_TROUGH.get(), FeedingTroughScreen::new);
    }
}
