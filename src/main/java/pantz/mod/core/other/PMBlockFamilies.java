package pantz.mod.core.other;

import net.minecraft.data.BlockFamily;

import static pantz.mod.core.registry.PMBlocks.*;

public class PMBlockFamilies {
    public static final BlockFamily SULFUR_BRICKS_FAMILY = new BlockFamily.Builder(SULFUR_BRICKS.get()).stairs(SULFUR_BRICK_STAIRS.get()).slab(SULFUR_BRICK_SLAB.get()).wall(SULFUR_BRICK_WALL.get()).chiseled(CHISELED_SULFUR_BRICKS.get()).getFamily();
}
