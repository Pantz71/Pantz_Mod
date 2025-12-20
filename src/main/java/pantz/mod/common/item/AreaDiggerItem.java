package pantz.mod.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class AreaDiggerItem extends DiggerItem {
    public AreaDiggerItem(float pAttackDamageModifier, float pAttackSpeedModifier, Tier pTier, TagKey<Block> pBlocks, Properties pProperties) {
        super(pAttackDamageModifier, pAttackSpeedModifier, pTier, pBlocks, pProperties);
    }

    public static List<BlockPos> mineArea(ServerLevel level, Player player, BlockPos pos, int range) {
        List<BlockPos> positions = new ArrayList<>();

        Vec3 start = player.getEyePosition(1f);
        Vec3 end = start.add(player.getViewVector(1f).scale(Integer.MAX_VALUE));

        BlockHitResult result = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        if (result.getType() == HitResult.Type.MISS) {
            return positions;
        }

        Direction face = result.getDirection();
        Direction.Axis axis = face.getAxis();

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        int posX = pos.getX(), posY = pos.getY(), posZ = pos.getZ();

        int startX = (axis == Direction.Axis.X) ? 0 : -range, endX = (axis == Direction.Axis.X) ? 0 : range;
        int startY = (axis == Direction.Axis.Y) ? 0 : -range, endY = (axis == Direction.Axis.Y) ? 0 : range;
        int startZ = (axis == Direction.Axis.Z) ? 0 : -range, endZ = (axis == Direction.Axis.Z) ? 0 : range;

        for (int dx = startX; dx <= endX; dx++) {
            for (int dy = startY; dy <= endY; dy++) {
                for (int dz = startZ; dz <= endZ; dz++) {
                    positions.add(mutablePos.set(posX + dx, posY + dy, posZ + dz).immutable());
                }
            }
        }
        return positions;
    }

}
