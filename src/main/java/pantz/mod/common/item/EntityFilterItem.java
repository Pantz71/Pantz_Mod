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

import java.util.List;

public class EntityFilterItem extends Item {
    public static final String FILTER_KEY = "FilteredEntities";
    public static final String MODE_KEY = "FilterMode";

    public EntityFilterItem(Properties props) {
        super(props);
    }

    public boolean addMobToStack(ItemStack stack, EntityType<?> type) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();

        ListTag list = tag.getList(FILTER_KEY, Tag.TAG_COMPOUND);
        String id = BuiltInRegistries.ENTITY_TYPE.getKey(type).toString();

        for (int i = 0; i < list.size(); i++) {
            if (list.getCompound(i).getString("Id").equals(id)) return false;
        }

        CompoundTag newEntry = new CompoundTag();
        newEntry.putString("Id", id);
        list.add(newEntry);

        tag.put(FILTER_KEY, list);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
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

        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (player.isShiftKeyDown()) {
            ListTag list = tag.getList(FILTER_KEY, Tag.TAG_COMPOUND);
            if (!list.isEmpty()) {
                detector.setFilterSettings(FilterMode.byId(tag.getInt(MODE_KEY)), list);
                player.displayClientMessage(Component.translatable("message.pantz_mod.detector.applied", list.size()), true);
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
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();

        if (tag.contains(FILTER_KEY, Tag.TAG_LIST)) {
            ListTag list = tag.getList(FILTER_KEY, Tag.TAG_COMPOUND);

            if (!list.isEmpty()) {
                CompoundTag lastEntry = list.getCompound(list.size() - 1);
                String entityId = lastEntry.getString("Id");
                Component entityName = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(entityId)).getDescription();
                list.removeLast();
                if (list.isEmpty()) {
                    tag.remove(FILTER_KEY);
                } else {
                    tag.put(FILTER_KEY, list);
                }
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                player.displayClientMessage(Component.translatable("message.pantz_mod.entity.removed", entityName), true);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        ListTag list = tag.getList(FILTER_KEY, Tag.TAG_COMPOUND);

        FilterMode currentMode = FilterMode.byId(tag.getInt(MODE_KEY));
        tooltip.add(Component.translatable("tooltip.pantz_mod.current_mode",
                Component.translatable(currentMode.getTranslationKey()).withStyle(currentMode.getColor())));

        if (list.isEmpty()) {
            tooltip.add(Component.translatable("tooltip.pantz_mod.entity.none"));
        } else {
            for (int i = 0; i < list.size(); i++) {
                ResourceLocation id = ResourceLocation.parse(list.getCompound(i).getString("Id"));
                EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(id);
                Component name = type.getDescription();
                tooltip.add(Component.translatable("tooltip.pantz_mod.entity", name).withStyle(ChatFormatting.AQUA));
            }
        }
    }
}