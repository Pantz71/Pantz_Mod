package pantz.mod.core.other;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.*;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.registries.RegisterEvent;
import pantz.mod.common.block.ItemStandBlock;
import pantz.mod.common.block.PedestalBlock;
import pantz.mod.common.block.entity.*;
import pantz.mod.common.entity.ai.goal.MoveToFeedingTroughGoal;
import pantz.mod.common.item.AreaDiggerItem;
import pantz.mod.common.item.EntityFilterItem;
import pantz.mod.common.utils.*;
import pantz.mod.common.utils.PMBlockStateProperties.CarpetColor;
import pantz.mod.common.world.WardenWorldData;
import pantz.mod.core.PantzMod;
import pantz.mod.core.registry.PMAttributes;
import pantz.mod.core.registry.PMBlockEntityTypes;
import pantz.mod.core.registry.PMSoundEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EventBusSubscriber(modid = PantzMod.MOD_ID)
public class PMEvents {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.setProxyable(Capabilities.ItemHandler.BLOCK);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PMBlockEntityTypes.PEDESTAL.get(),
                (be, dir) -> be.getItemHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PMBlockEntityTypes.ITEM_STAND.get(),
                (be, dir) -> be.getItemHandler());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PMBlockEntityTypes.TRASH_CAN.get(),
                (be, dir) -> new InvWrapper(be));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PMBlockEntityTypes.SAFE.get(),
                (be, dir) -> new InvWrapper(be));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, PMBlockEntityTypes.FEEDING_TROUGH.get(),
                (be, dir) -> new InvWrapper(be));
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        if (event.getEntity() instanceof Animal animal && !animal.level().isClientSide()) {
            CompoundTag persistentData = animal.getPersistentData();

            if (persistentData.contains(MoveToFeedingTroughGoal.COOLDOWN_TAG)) {
                int currentCooldown = persistentData.getInt(MoveToFeedingTroughGoal.COOLDOWN_TAG);

                if (currentCooldown > 0) {
                    persistentData.putInt(MoveToFeedingTroughGoal.COOLDOWN_TAG, currentCooldown - 1);
                } else {
                    persistentData.remove(MoveToFeedingTroughGoal.COOLDOWN_TAG);
                }
            }
        }
    }


    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        if (event.getRegistryKey().equals(BuiltInRegistries.CUSTOM_STAT.key())) {
            PMStats.registerStats();
        }
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide() && event.getEntity() instanceof Animal animal) {
            animal.goalSelector.addGoal(4, new MoveToFeedingTroughGoal(animal, 1.25f, 16));
        }
    }

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
    public static void onLivingHurt(LivingIncomingDamageEvent event) {
        Entity sourceEntity = event.getSource().getDirectEntity();
        if (sourceEntity instanceof Projectile projectile) {
            Entity owner = projectile.getOwner();

            if (owner instanceof LivingEntity shooter) {
                AttributeInstance attribute = shooter.getAttribute(PMAttributes.PROJECTILE_BONUS_DAMAGE);
                if (attribute == null) return;

                double value = attribute.getValue();
                float baseAmount = event.getAmount();
                float modifier;

                Vec3 launchVelocity = projectile.getDeltaMovement().subtract(shooter.getDeltaMovement());
                double speed = launchVelocity.length();
                double motionFactor = Math.min(1.0D, speed / 3.0D);

                if (projectile instanceof AbstractArrow arrow) {
                    if (value >= 0.0D) {
                        modifier = (float) (value * motionFactor);
                    } else {
                        modifier = (float) (value * (1.0D - motionFactor));
                    }

                    if (arrow.isCritArrow()) {
                        modifier *= 1.5F;
                    }
                } else {
                    double genericMotion = speed / 1.5D;
                    modifier = (float) (value * Math.min(1.0D, genericMotion));
                }

                float finalDamage = baseAmount + modifier;
                event.setAmount(Math.max(0.5F, finalDamage));
            }
        }
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
                PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
                List<MobEffectInstance> effects = new ArrayList<>();

                if (contents != null) {
                    contents.getAllEffects().forEach(effects::add);
                }
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
                level.playSound(null, pos, PMSoundEvents.ITEM_STAND_ENCASE.get(), SoundSource.BLOCKS);
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
            level.playSound(null, pos, PMSoundEvents.PEDESTAL_DECORATE.get(), SoundSource.BLOCKS);
        }
        cancel(event);
    }

    private static void cancel(RightClickBlock event) {
        event.setCanceled(true);
        event.setUseBlock(TriState.FALSE);
        event.setUseItem(TriState.FALSE);
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = player.getMainHandItem();
        InteractionHand hand = player.getUsedItemHand();
        if (!(stack.getItem() instanceof AreaDiggerItem)) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        if (!stack.isCorrectToolForDrops(event.getState())) {
            event.setCanceled(true);
            return;
        }

        List<BlockPos> blocks = AreaDiggerItem.mineArea(level, player, event.getPos(), 1);
        for (BlockPos pos : blocks) {
            BlockState state = level.getBlockState(pos);
            if (pos.equals(event.getPos())) continue;
            if (!state.isAir() && stack.isCorrectToolForDrops(state)) {
                level.destroyBlock(pos, !player.isCreative());
            }
        }
        if (!player.isCreative()) {
            stack.hurtAndBreak(3, player, LivingEntity.getSlotForHand(hand));
        }
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
