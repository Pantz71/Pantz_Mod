package pantz.mod.core.registry;

import com.teamabnormals.blueprint.core.util.registry.SoundSubRegistryHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;
import pantz.mod.core.PantzMod;

public class PMSoundEvents {
    public static final SoundSubRegistryHelper SOUND_EVENTS = PantzMod.REGISTRY_HELPER.getSoundSubHelper();

    public static final RegistryObject<SoundEvent> TRASH_CAN_OPEN = SOUND_EVENTS.createSoundEvent("block.trash_can.open");
    public static final RegistryObject<SoundEvent> TRASH_CAN_CLOSE = SOUND_EVENTS.createSoundEvent("block.trash_can.close");
    public static final RegistryObject<SoundEvent> TRASH_CAN_DESTROY = SOUND_EVENTS.createSoundEvent("block.trash_can.destroy");
}
