package pantz.mod.core.other;

import com.teamabnormals.blueprint.common.advancement.EmptyTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import pantz.mod.core.PantzMod;

@Mod.EventBusSubscriber(modid = PantzMod.MOD_ID)
public class PMCriteriaTriggers {
    public static final EmptyTrigger LOOK_AT_SCANNER = CriteriaTriggers.register(new EmptyTrigger(name("look_at_scanner")));

    private static ResourceLocation name(String name) {
        return PantzMod.location(name);
    }
}
