package pantz.mod.core.registry;

import com.teamabnormals.blueprint.core.util.registry.BlockEntitySubRegistryHelper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import pantz.mod.common.block.*;
import pantz.mod.common.block.entity.*;
import pantz.mod.core.PantzMod;

public class PMBlockEntityTypes {
    public static final BlockEntitySubRegistryHelper BLOCK_ENTITY_TYPES = PantzMod.REGISTRY_HELPER.getBlockEntitySubHelper();

    public static final RegistryObject<BlockEntityType<PedestalBlockEntity>> PEDESTAL = BLOCK_ENTITY_TYPES.createBlockEntity("pedestal", PedestalBlockEntity::new, PedestalBlock.class);
    public static final RegistryObject<BlockEntityType<EnderScannerBlockEntity>> ENDER_SCANNER = BLOCK_ENTITY_TYPES.createBlockEntity("ender_scanner", EnderScannerBlockEntity::new, EnderScannerBlock.class);
    public static final RegistryObject<BlockEntityType<EntityDetectorBlockEntity>> ENTITY_DETECTOR = BLOCK_ENTITY_TYPES.createBlockEntity("entity_detector", EntityDetectorBlockEntity::new, EntityDetectorBlock.class);
    public static final RegistryObject<BlockEntityType<GlobeBlockEntity>> GLOBE = BLOCK_ENTITY_TYPES.createBlockEntity("globe", GlobeBlockEntity::new, GlobeBlock.class);
    public static final RegistryObject<BlockEntityType<ItemStandBlockEntity>> ITEM_STAND = BLOCK_ENTITY_TYPES.createBlockEntity("item_stand", ItemStandBlockEntity::new, ItemStandBlock.class);
    public static final RegistryObject<BlockEntityType<TrashCanBlockEntity>> TRASH_CAN = BLOCK_ENTITY_TYPES.createBlockEntity("trash_can", TrashCanBlockEntity::new, TrashCanBlock.class);
    public static final RegistryObject<BlockEntityType<LockBlockEntity>> LOCK = BLOCK_ENTITY_TYPES.createBlockEntity("lock", LockBlockEntity::new, LockBlock.class);
    public static final RegistryObject<BlockEntityType<SafeBlockEntity>> SAFE = BLOCK_ENTITY_TYPES.createBlockEntity("safe", SafeBlockEntity::new, SafeBlock.class);
    public static final RegistryObject<BlockEntityType<SpikeBlockEntity>> SPIKE = BLOCK_ENTITY_TYPES.createBlockEntity("spike", SpikeBlockEntity::new, SpikeBlock.class);
    public static final RegistryObject<BlockEntityType<SprinklerBlockEntity>> SPRINKLER = BLOCK_ENTITY_TYPES.createBlockEntity("sprinkler", SprinklerBlockEntity::new, SprinklerBlock.class);

}
