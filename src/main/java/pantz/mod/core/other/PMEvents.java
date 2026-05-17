package pantz.mod.core.other;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.*;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import pantz.mod.common.block.ItemStandBlock;
import pantz.mod.common.block.PedestalBlock;
import pantz.mod.common.block.entity.PedestalBlockEntity;
import pantz.mod.common.block.entity.SpikeBlockEntity;
import pantz.mod.common.item.AreaDiggerItem;
import pantz.mod.common.item.EntityFilterItem;
import pantz.mod.common.utils.*;
import pantz.mod.common.world.WardenWorldData;
import pantz.mod.core.PantzMod;
import pantz.mod.core.registry.PMSoundEvents;

import java.util.List;
import java.util.UUID;


@Mod.EventBusSubscriber(modid = PantzMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PMEvents {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Warden)) return;
        if (!(event.getSource().getEntity() instanceof Player player)) return;

        Level level = player.level();
        if (level.isClientSide()) return;

        WardenWorldData data = WardenWorldData.get(level);
        data.addPlayer(player.getUUID());
    }

    @SubscribeEvent
    public static void onEntityInteract(EntityInteract event) {
        Player player = event.getEntity();
        Level level = player.level();
        ItemStack stack = player.getItemInHand(event.getHand());

        if (!(stack.getItem() instanceof EntityFilterItem filterItem)) return;
        if (level.isClientSide()) return;

        Entity target = event.getTarget();
        if (target instanceof LivingEntity) {
            if (filterItem.addMobToStack(stack, target.getType())) {
                player.swing(event.getHand(), true);
                player.displayClientMessage(Component.translatable("message.pantz_mod.entity.added", target.getType().getDescription()), true);

                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }

    @SubscribeEvent
    public static void onBlockInteract(RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) return;

        Player player = event.getEntity();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        InteractionHand hand = event.getHand();
        BlockState state = level.getBlockState(pos);

        if (!player.isShiftKeyDown()) return;

        decoratePedestal(event, level, pos, state, player, hand);
        setKey(event, player, level, pos, hand);
        putGlassBoxOn(event, level, pos, state, player, hand);
    }

    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        if (!(event.getProjectile() instanceof ThrownPotion potion)) return;

        Level level = potion.level();
        ItemStack stack = potion.getItem();

        if (!(stack.getItem() instanceof SplashPotionItem) && !(stack.getItem() instanceof LingeringPotionItem)) {
            return;
        }

        Vec3 impactPos = potion.position();
        BlockPos center = BlockPos.containing(impactPos);

        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-1, -1, -1), center.offset(1, 1, 1))) {
            BlockEntity be = level.getBlockEntity(pos);

            if (be instanceof SpikeBlockEntity spike) {
                List<MobEffectInstance> effects = PotionUtils.getMobEffects(stack);

                for (MobEffectInstance effect : effects) {
                    spike.addOrUpgradeEffect(new MobEffectInstance(effect));
                }

                spike.setChanged();
            }
        }
    }

    private static void putGlassBoxOn(RightClickBlock event, Level level, BlockPos pos, BlockState state, Player player, InteractionHand hand) {
        if (!(state.getBlock() instanceof ItemStandBlock itemStand)) return;
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown() && itemStand.isGlass(stack) && !state.getValue(ItemStandBlock.GLASS)) {
            if (!level.isClientSide()) {
                player.swing(hand);
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                level.setBlock(pos, state.setValue(ItemStandBlock.GLASS, true), 3);
                cancel(event);
            }
        }
    }

    private static void setKey(RightClickBlock event, Player player, Level level, BlockPos pos, InteractionHand hand) {
        if (!(level.getBlockEntity(pos) instanceof Lockable lockable)) return;
        if (!(level.getBlockState(pos).getBlock() instanceof ILockableBlock lockableBlock)) return;

        ItemStack stack = player.getItemInHand(hand);

        ItemStack storedKey = lockable.getItem();
        UUID playerId = player.getUUID();
        UUID owner = lockable.getOwner();

        if (storedKey.isEmpty() && lockableBlock.isValidItem(stack) && owner == null) {
            lockable.setItem(stack, playerId);
            player.displayClientMessage(Component.translatable("message.pantz_mod.set_key", stack.getHoverName()), true);
            level.playSound(null, pos, PMSoundEvents.KEY_SET.get(), SoundSource.BLOCKS);
            player.swing(hand);
            cancel(event);
            return;
        }

        if (!storedKey.isEmpty() && stack.equals(storedKey) && owner != null) {
            if (!owner.equals(playerId)) {
                player.displayClientMessage(Component.translatable("message.pantz_mod.ownership"), true);
            } else {
                lockable.removeItem(playerId);
                player.displayClientMessage(Component.translatable("message.pantz_mod.remove_key"), true);
            }
            player.swing(hand);
            cancel(event);
        }
    }

    private static void decoratePedestal(RightClickBlock event, Level level, BlockPos pos, BlockState state, Player player, InteractionHand hand) {
        if (!(state.getBlock() instanceof PedestalBlock) ||
                !(level.getBlockEntity(pos) instanceof PedestalBlockEntity pedestal)) return;

        if (!pedestal.getItem().isEmpty()) return;

        ItemStack held = player.getItemInHand(hand);
        CarpetColor currentColor = state.getValue(PedestalBlock.CARPET);

        if (held.is(ItemTags.WOOL_CARPETS) && currentColor == CarpetColor.NONE) {
            CarpetColor newColor = PedestalUtils.getCarpetColor(held.getItem());
            if (newColor != CarpetColor.NONE) {
                updateCarpet(event, level, pos, state, newColor, player, held, true);
            }
        } else if (held.isEmpty() && currentColor != CarpetColor.NONE) {
            ItemStack carpetStack = new ItemStack(PedestalUtils.getCarpetForColor(currentColor));
            if (!player.getInventory().add(carpetStack)) {
                player.drop(carpetStack, false);
            }
            updateCarpet(event, level, pos, state, CarpetColor.NONE, player, held, false);
        }
    }

    private static void updateCarpet(RightClickBlock event, Level level, BlockPos pos, BlockState state, CarpetColor color, Player player, ItemStack held, boolean shrink) {
        player.swing(event.getHand());
        if (!level.isClientSide()) {
            level.setBlock(pos, state.setValue(PedestalBlock.CARPET, color), 3);
            if (shrink && !player.isCreative()) {
                held.shrink(1);
            }
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
