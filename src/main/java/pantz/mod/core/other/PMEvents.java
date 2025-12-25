package pantz.mod.core.other;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import pantz.mod.common.item.AreaDiggerItem;
import pantz.mod.common.item.EntityFilterItem;
import pantz.mod.core.PantzMod;

import java.util.List;


@Mod.EventBusSubscriber(modid = PantzMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PMEvents {

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        Level level = player.level();
        ItemStack stack = player.getMainHandItem();

        if (!(stack.getItem() instanceof EntityFilterItem)) return;

        if (level.isClientSide()) return;

        Entity target = event.getTarget();
        EntityType<?> type = target.getType();

        if (target instanceof LivingEntity) {
            player.swing(InteractionHand.MAIN_HAND);
            ((EntityFilterItem) stack.getItem()).addMobToStack(stack, type);
            player.displayClientMessage(
                    Component.translatable("message.pantz_mod.entity_filter.added", type.getDescription()),
                    true
            );

            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof AreaDiggerItem)) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        event.setCanceled(true);

        List<BlockPos> blocks = AreaDiggerItem.mineArea(level, player, event.getPos(), 1);
        for (BlockPos pos : blocks) {
            if (!level.getBlockState(pos).isAir() && stack.isCorrectToolForDrops(level.getBlockState(pos))) {
                level.destroyBlock(pos, !player.isCreative());
            }
        }
    }
}
