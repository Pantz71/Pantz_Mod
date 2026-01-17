package pantz.mod.core.other;

import net.minecraft.world.level.block.DispenserBlock;
import pantz.mod.core.registry.PMBlocks;
import pantz.mod.core.registry.PMDispenserBehaviors;
import pantz.mod.core.registry.PMItems;

public class PMCompat {
    public static void registerCompat() {
        registerDispenserBehaviors();
    }

    private static void registerDispenserBehaviors() {
        DispenserBlock.registerBehavior(PMBlocks.ROPE_LADDER.get(), PMDispenserBehaviors.EXTEND_ROPE_LADDER);
        DispenserBlock.registerBehavior(PMItems.STEEL_HORSE_ARMOR.get(), PMDispenserBehaviors.EQUIP_HORSE_ARMOR);
    }

}
