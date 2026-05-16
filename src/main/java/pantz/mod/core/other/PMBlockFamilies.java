package pantz.mod.core.other;

import net.minecraft.data.BlockFamily;

import static pantz.mod.core.registry.PMBlocks.*;

public class PMBlockFamilies {
    public static final BlockFamily POLISHED_SULFUR_FAMILY = new BlockFamily.Builder(POLISHED_SULFUR.get()).stairs(POLISHED_SULFUR_STAIRS.get()).slab(POLISHED_SULFUR_SLAB.get()).wall(POLISHED_SULFUR_WALL.get()).getFamily();
    public static final BlockFamily SULFUR_BRICKS_FAMILY = new BlockFamily.Builder(SULFUR_BRICKS.get()).stairs(SULFUR_BRICK_STAIRS.get()).slab(SULFUR_BRICK_SLAB.get()).wall(SULFUR_BRICK_WALL.get()).chiseled(CHISELED_SULFUR_BRICKS.get()).getFamily();
    public static final BlockFamily STEEL_BRICKS_FAMILY = new BlockFamily.Builder(STEEL_BRICKS.get()).stairs(STEEL_BRICK_STAIRS.get()).slab(STEEL_BRICK_SLAB.get()).wall(STEEL_BRICK_WALL.get()).chiseled(CHISELED_STEEL_BRICKS.get()).getFamily();

}
