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

    public static final RegistryObject<SoundEvent> ENDERPORTER_CHARGE = SOUND_EVENTS.createSoundEvent("block.enderporter.charge");
    public static final RegistryObject<SoundEvent> ENDERPORTER_DEPLETE = SOUND_EVENTS.createSoundEvent("block.enderporter.deplete");

    public static final RegistryObject<SoundEvent> KEY_LOCK = SOUND_EVENTS.createSoundEvent("block.lockable.key_lock");
    public static final RegistryObject<SoundEvent> KEY_SET = SOUND_EVENTS.createSoundEvent("block.lockable.key_set");
    public static final RegistryObject<SoundEvent> SAFE_OPEN = SOUND_EVENTS.createSoundEvent("block.safe.open");
    public static final RegistryObject<SoundEvent> SAFE_CLOSE = SOUND_EVENTS.createSoundEvent("block.safe.close");
    public static final RegistryObject<SoundEvent> SAFE_LOCKED = SOUND_EVENTS.createSoundEvent("block.safe.locked");
}
