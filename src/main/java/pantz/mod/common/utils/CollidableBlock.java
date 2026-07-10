package pantz.mod.common.utils;

import net.minecraft.world.phys.shapes.CollisionContext;

public interface CollidableBlock {
    boolean isAllowedToPass(CollisionContext context);
}
