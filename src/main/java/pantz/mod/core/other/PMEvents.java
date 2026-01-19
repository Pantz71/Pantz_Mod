package pantz.mod.core.other;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.*;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import pantz.mod.common.block.PedestalBlock;
import pantz.mod.common.block.entity.PedestalBlockEntity;
import pantz.mod.common.item.AreaDiggerItem;
import pantz.mod.common.item.EntityFilterItem;
import pantz.mod.common.utils.CarpetColor;
import pantz.mod.common.utils.EnderporterUtils;
import pantz.mod.common.utils.PedestalUtils;
import pantz.mod.core.PantzMod;

import java.util.List;


@Mod.EventBusSubscriber(modid = PantzMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PMEvents {

    @SubscribeEvent
    public static void onEntityInteract(EntityInteract event) {
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
                    Component.translatable("message.pantz_mod.entity.added", type.getDescription()),
                    true
            );

            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }

    @SubscribeEvent
    public static void onBlockInteract(RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) return;

        Player player = event.getEntity();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        InteractionHand hand = event.getHand();

        if (!player.isShiftKeyDown()) return;

        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof PedestalBlock)) return;

        if (!(level.getBlockEntity(pos) instanceof PedestalBlockEntity pedestal)) return;

        if (!pedestal.getItem().isEmpty()) return;

        ItemStack held = player.getItemInHand(hand);

        if (held.is(ItemTags.WOOL_CARPETS) && state.getValue(PedestalBlock.CARPET) == CarpetColor.NONE) {
            putCarpetOn(event, held, state, pos, player, level);
        } else if (held.isEmpty() && state.getValue(PedestalBlock.CARPET) != CarpetColor.NONE){
            takeCarpetOff(event, state, pos, player, level);
        }
    }

    private static void putCarpetOn(RightClickBlock event, ItemStack held, BlockState state, BlockPos pos, Player player, Level level) {
        CarpetColor color = PedestalUtils.getCarpetColor(held.getItem());
        if (color == CarpetColor.NONE) return;
        player.swing(InteractionHand.MAIN_HAND);
        if (!level.isClientSide()) {
            level.setBlock(pos, state.setValue(PedestalBlock.CARPET, color), 3);
            if (!player.isCreative()) {
                held.shrink(1);
            }
        }
        cancel(event);
    }

    private static void takeCarpetOff(RightClickBlock event, BlockState state, BlockPos pos, Player player, Level level) {
        CarpetColor color = state.getValue(PedestalBlock.CARPET);
        player.swing(InteractionHand.MAIN_HAND);
        if (!level.isClientSide()) {
            ItemStack carpet = new ItemStack(PedestalUtils.getCarpetForColor(color));
            if (!player.addItem(carpet)) {
                player.drop(carpet, false);
            }

            level.setBlock(pos, state.setValue(PedestalBlock.CARPET, CarpetColor.NONE), 3);
        }
        cancel(event);
    }

    private static void cancel(RightClickBlock event) {
        event.setCanceled(true);
        event.setUseBlock(Event.Result.DENY);
        event.setUseItem(Event.Result.DENY);
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = player.getMainHandItem();
        InteractionHand hand = player.getUsedItemHand();
        if (!(stack.getItem() instanceof AreaDiggerItem)) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        event.setCanceled(true);

        List<BlockPos> blocks = AreaDiggerItem.mineArea(level, player, event.getPos(), 1);
        for (BlockPos pos : blocks) {
            if (!level.getBlockState(pos).isAir() && stack.isCorrectToolForDrops(level.getBlockState(pos))) {
                level.destroyBlock(pos, !player.isCreative());
            }
        }
        stack.hurtAndBreak(3, player, p -> p.broadcastBreakEvent(hand));
    }

    @SubscribeEvent
    public static void onEnderpearlLands(EntityTeleportEvent.EnderPearl event) {
        ThrownEnderpearl pearl = event.getPearlEntity();
        ServerPlayer player = event.getPlayer();
        ServerLevel level = player.serverLevel();

        boolean teleported = EnderporterUtils.redirectTeleport(pearl, level, player);
        if (teleported) {
            event.setCanceled(true);
        }
    }
}
