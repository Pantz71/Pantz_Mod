package pantz.mod.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import pantz.mod.common.block.entity.EntityDetectorBlockEntity;
import pantz.mod.common.utils.FilterMode;
import pantz.mod.core.PMConfig;
import pantz.mod.core.registry.PMDataComponents;

import java.util.ArrayList;
import java.util.List;

public class EntityFilterItem extends Item {
    public EntityFilterItem(Properties props) {
        super(props);
    }

    public boolean addMobToStack(ItemStack stack, EntityType<?> type) {
        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(type);

        List<ResourceLocation> currentList = stack.getOrDefault(PMDataComponents.FILTERED_ENTITIES.get(), List.of());

        if (currentList.contains(id)) return false;

        List<ResourceLocation> updatedList = new ArrayList<>(currentList);
        updatedList.add(id);

        stack.set(PMDataComponents.FILTERED_ENTITIES.get(), updatedList);
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        Player player = ctx.getPlayer();
        ItemStack stack = ctx.getItemInHand();

        if (player == null || level.isClientSide() || !PMConfig.Common.COMMON.enableEntityFilter.get()) {
            return InteractionResult.PASS;
        }

        BlockEntity be = level.getBlockEntity(ctx.getClickedPos());
        if (!(be instanceof EntityDetectorBlockEntity detector)) return InteractionResult.PASS;

        FilterMode currentMode = stack.getOrDefault(PMDataComponents.FILTER_MODE.get(), FilterMode.INCLUDE);
        List<ResourceLocation> entities = stack.getOrDefault(PMDataComponents.FILTERED_ENTITIES.get(), List.of());

        if (player.isShiftKeyDown()) {
            if (!entities.isEmpty()) {
                detector.setFilterSettings(currentMode, entities);
                player.displayClientMessage(Component.translatable("message.pantz_mod.detector.applied", entities.size()), true);
                return InteractionResult.SUCCESS;
            }
        }

        if (detector.hasFilters()) {
            ResourceLocation entityId = detector.removeLastEntity();
            if (entityId != null) {
                Component entityName = BuiltInRegistries.ENTITY_TYPE.get(entityId).getDescription();
                player.displayClientMessage(Component.translatable("message.pantz_mod.detector.removed", entityName), true);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) return InteractionResultHolder.pass(stack);

        HitResult hit = player.pick(5.0D, 0.0F, false);
        if (hit.getType() == HitResult.Type.MISS) {
            if (player.isShiftKeyDown()) {
                cycleMode(stack, player);
            } else {
                removeLastEntityFromStack(stack, player);
            }
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);

    }

    private void cycleMode(ItemStack stack, Player player) {
        FilterMode nextMode = stack.update(PMDataComponents.FILTER_MODE.get(), FilterMode.INCLUDE, FilterMode::next);

        if (nextMode != null) {
            player.displayClientMessage(Component.translatable("message.pantz_mod.mode_change", Component.translatable(nextMode.getTranslationKey()).withStyle(nextMode.getColor())), true);
        }
    }

    public void removeLastEntityFromStack(ItemStack stack, Player player) {
        List<ResourceLocation> currentList = stack.getOrDefault(PMDataComponents.FILTERED_ENTITIES.get(), List.of());

        if (!currentList.isEmpty()) {
            List<ResourceLocation> updatedList = new ArrayList<>(currentList);
            ResourceLocation removedId = updatedList.remove(updatedList.size() - 1);

            Component entityName = BuiltInRegistries.ENTITY_TYPE.get(removedId).getDescription();

            if (updatedList.isEmpty()) {
                stack.remove(PMDataComponents.FILTERED_ENTITIES.get());
            } else {
                stack.set(PMDataComponents.FILTERED_ENTITIES.get(), updatedList);
            }

            player.displayClientMessage(Component.translatable("message.pantz_mod.entity.removed", entityName), true);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        FilterMode currentMode = stack.getOrDefault(PMDataComponents.FILTER_MODE.get(), FilterMode.INCLUDE);
        tooltip.add(Component.translatable("tooltip.pantz_mod.current_mode",
                Component.translatable(currentMode.getTranslationKey()).withStyle(currentMode.getColor())));

        List<ResourceLocation> entities = stack.getOrDefault(PMDataComponents.FILTERED_ENTITIES.get(), List.of());

        if (entities.isEmpty()) {
            tooltip.add(Component.translatable("tooltip.pantz_mod.entity.none"));
        } else {
            for (ResourceLocation id : entities) {
                Component name = BuiltInRegistries.ENTITY_TYPE.get(id).getDescription();
                tooltip.add(Component.translatable("tooltip.pantz_mod.entity", name).withStyle(ChatFormatting.AQUA));
            }
        }
    }
}