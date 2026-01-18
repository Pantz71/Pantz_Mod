package pantz.mod.common.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import pantz.mod.common.entity.projectile.Dynamite;
import pantz.mod.common.utils.DynamiteType;
import pantz.mod.core.other.PMCriteriaTriggers;

public class DynamiteItem extends Item {
    private final DynamiteType type;
    public DynamiteItem(Properties pProperties, DynamiteType type) {
        super(pProperties);
        this.type = type;
    }

    public DynamiteType getType() {
        return this.type;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            Dynamite dynamite = new Dynamite(level, player);
            dynamite.setItem(stack);
            dynamite.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.9F, 1.0F);
            level.addFreshEntity(dynamite);
        }
        player.getCooldowns().addCooldown(this, 20);
        player.awardStat(Stats.ITEM_USED.get(this));
        if (player instanceof ServerPlayer serverPlayer) {
            PMCriteriaTriggers.USE_DYNAMITE.trigger(serverPlayer);
        }
        if (!player.isCreative()) {
            stack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
