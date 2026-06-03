package pantz.mod.core.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import pantz.mod.core.PantzMod;

public class PMStats {
    public static final ResourceLocation INTERACT_WITH_TRASH_CAN = PantzMod.location("interact_with_trash_can");

    public static void registerStats() {
        Registry.register(BuiltInRegistries.CUSTOM_STAT, INTERACT_WITH_TRASH_CAN.getPath(), INTERACT_WITH_TRASH_CAN);
        Stats.CUSTOM.get(INTERACT_WITH_TRASH_CAN, StatFormatter.DEFAULT);
    }
}
