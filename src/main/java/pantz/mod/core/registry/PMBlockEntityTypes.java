package pantz.mod.core.registry;

import com.teamabnormals.blueprint.core.util.registry.BlockEntitySubRegistryHelper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import pantz.mod.common.block.EnderScannerBlock;
import pantz.mod.common.block.EntityDetectorBlock;
import pantz.mod.common.block.PedestalBlock;
import pantz.mod.common.block.entity.EnderScannerBlockEntity;
import pantz.mod.common.block.entity.EntityDetectorBlockEntity;
import pantz.mod.common.block.entity.PedestalBlockEntity;
import pantz.mod.core.PantzMod;

public class PMBlockEntityTypes {
    public static final BlockEntitySubRegistryHelper BLOCK_ENTITY_TYPES = PantzMod.REGISTRY_HELPER.getBlockEntitySubHelper();

    public static final RegistryObject<BlockEntityType<PedestalBlockEntity>> PEDESTAL = BLOCK_ENTITY_TYPES.createBlockEntity("pedestal", PedestalBlockEntity::new, PedestalBlock.class);
    public static final RegistryObject<BlockEntityType<EnderScannerBlockEntity>> ENDER_SCANNER = BLOCK_ENTITY_TYPES.createBlockEntity("ender_scanner", EnderScannerBlockEntity::new, EnderScannerBlock.class);
    public static final RegistryObject<BlockEntityType<EntityDetectorBlockEntity>> ENTITY_DETECTOR = BLOCK_ENTITY_TYPES.createBlockEntity("entity_detector", EntityDetectorBlockEntity::new, EntityDetectorBlock.class);

}
