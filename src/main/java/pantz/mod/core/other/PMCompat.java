package pantz.mod.core.other;

import net.minecraft.world.level.block.DispenserBlock;
import pantz.mod.core.registry.PMBlocks;
import pantz.mod.core.registry.PMDispenserBehaviors;

public class PMCompat {
    public static void registerCompat() {
        registerDispenserBehaviors();
    }

    private static void registerDispenserBehaviors() {
        DispenserBlock.registerBehavior(PMBlocks.ROPE_LADDER.get(), PMDispenserBehaviors.EXTEND_ROPE_LADDER);
    }

}
