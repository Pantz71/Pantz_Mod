package pantz.mod.core.registry;

import com.teamabnormals.blueprint.core.util.registry.SoundSubRegistryHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.util.ForgeSoundType;
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

    public static final RegistryObject<SoundEvent> STEEL_BREAK = SOUND_EVENTS.createSoundEvent("block.steel.break");
    public static final RegistryObject<SoundEvent> STEEL_FALL = SOUND_EVENTS.createSoundEvent("block.steel.fall");
    public static final RegistryObject<SoundEvent> STEEL_HIT = SOUND_EVENTS.createSoundEvent("block.steel.hit");
    public static final RegistryObject<SoundEvent> STEEL_PLACE = SOUND_EVENTS.createSoundEvent("block.steel.place");
    public static final RegistryObject<SoundEvent> STEEL_STEP = SOUND_EVENTS.createSoundEvent("block.steel.step");

    public static final RegistryObject<SoundEvent> STEEL_DOOR_CLOSE = SOUND_EVENTS.createSoundEvent("block.steel_door.close");
    public static final RegistryObject<SoundEvent> STEEL_DOOR_OPEN = SOUND_EVENTS.createSoundEvent("block.steel_door.open");
    public static final RegistryObject<SoundEvent> STEEL_TRAPDOOR_CLOSE = SOUND_EVENTS.createSoundEvent("block.steel_trapdoor.close");
    public static final RegistryObject<SoundEvent> STEEL_TRAPDOOR_OPEN = SOUND_EVENTS.createSoundEvent("block.steel_trapdoor.open");

    public static final RegistryObject<SoundEvent> STEEL_CHAIN_BREAK = SOUND_EVENTS.createSoundEvent("block.steel_chain.break");
    public static final RegistryObject<SoundEvent> STEEL_CHAIN_FALL = SOUND_EVENTS.createSoundEvent("block.steel_chain.fall");
    public static final RegistryObject<SoundEvent> STEEL_CHAIN_HIT = SOUND_EVENTS.createSoundEvent("block.steel_chain.hit");
    public static final RegistryObject<SoundEvent> STEEL_CHAIN_PLACE = SOUND_EVENTS.createSoundEvent("block.steel_chain.place");
    public static final RegistryObject<SoundEvent> STEEL_CHAIN_STEP = SOUND_EVENTS.createSoundEvent("block.steel_chain.step");

    public static final RegistryObject<SoundEvent> STEEL_LANTERN_BREAK = SOUND_EVENTS.createSoundEvent("block.steel_lantern.break");
    public static final RegistryObject<SoundEvent> STEEL_LANTERN_FALL = SOUND_EVENTS.createSoundEvent("block.steel_lantern.fall");
    public static final RegistryObject<SoundEvent> STEEL_LANTERN_HIT = SOUND_EVENTS.createSoundEvent("block.steel_lantern.hit");
    public static final RegistryObject<SoundEvent> STEEL_LANTERN_PLACE = SOUND_EVENTS.createSoundEvent("block.steel_lantern.place");
    public static final RegistryObject<SoundEvent> STEEL_LANTERN_STEP = SOUND_EVENTS.createSoundEvent("block.steel_lantern.step");
    public static final RegistryObject<SoundEvent> STEEL_LANTERN_TOGGLE = SOUND_EVENTS.createSoundEvent("block.steel_lantern.toggle");

    public static final RegistryObject<SoundEvent> ORNAMENT_ADJUST = SOUND_EVENTS.createSoundEvent("block.ornament.adjust");

    public static class PMSoundTypes {
        public static final ForgeSoundType STEEL = new ForgeSoundType(1.0f, 1.0f, STEEL_BREAK, STEEL_STEP, STEEL_PLACE, STEEL_HIT, STEEL_FALL);
        public static final ForgeSoundType STEEL_CHAIN = new ForgeSoundType(1.0f, 1.0f, STEEL_CHAIN_BREAK, STEEL_CHAIN_STEP, STEEL_CHAIN_PLACE, STEEL_CHAIN_HIT, STEEL_CHAIN_FALL);
        public static final ForgeSoundType STEEL_LANTERN = new ForgeSoundType(1.0f, 1.0f, STEEL_LANTERN_BREAK, STEEL_LANTERN_STEP, STEEL_LANTERN_PLACE, STEEL_LANTERN_HIT, STEEL_LANTERN_FALL);
    }
}
