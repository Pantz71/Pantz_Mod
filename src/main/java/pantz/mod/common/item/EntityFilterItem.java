package pantz.mod.common.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import pantz.mod.common.block.entity.EntityDetectorBlockEntity;
import pantz.mod.common.utils.FilterMode;

import java.util.List;

public class EntityFilterItem extends Item {
    public static final String FILTER_KEY = "FilteredEntities";
    public static final String MODE_KEY = "FilterMode";

    public EntityFilterItem(Properties props) {
        super(props);
    }

    public boolean addMobToStack(ItemStack stack, EntityType<?> type) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag list = tag.getList(FILTER_KEY, Tag.TAG_COMPOUND);
        String id = EntityType.getKey(type).toString();

        for (int i = 0; i < list.size(); i++) {
            if (list.getCompound(i).getString("Id").equals(id)) return false;
        }

        CompoundTag newEntry = new CompoundTag();
        newEntry.putString("Id", id);
        list.add(newEntry);
        tag.put(FILTER_KEY, list);
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        Player player = ctx.getPlayer();
        ItemStack stack = ctx.getItemInHand();
        CompoundTag tag = stack.getOrCreateTag();

        if (player == null || level.isClientSide()) return InteractionResult.PASS;

        BlockEntity be = level.getBlockEntity(ctx.getClickedPos());
        if (!(be instanceof EntityDetectorBlockEntity detector)) return InteractionResult.PASS;

        if (player.isShiftKeyDown()) {
            ListTag list = tag.getList(FILTER_KEY, Tag.TAG_COMPOUND);
            if (!list.isEmpty()) {
                detector.setFilterSettings(FilterMode.byId(stack.getOrCreateTag().getInt(MODE_KEY)), list);
                player.displayClientMessage(Component.translatable("message.pantz_mod.detector.applied", list.size()), true);
                return InteractionResult.SUCCESS;
            }
        }
        if (detector.hasFilters()) {
            ResourceLocation removedId = detector.removeLastEntity();
            if (removedId != null) {
                Component entityName = ForgeRegistries.ENTITY_TYPES.getValue(removedId).getDescription();
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

    public void removeLastEntityFromStack(ItemStack stack, Player player) {
        CompoundTag tag = stack.getOrCreateTag();

        if (tag.contains(FILTER_KEY, Tag.TAG_LIST)) {
            ListTag list = tag.getList(FILTER_KEY, Tag.TAG_COMPOUND);

            if (!list.isEmpty()) {
                CompoundTag lastEntry = list.getCompound(list.size() - 1);
                String entityId = lastEntry.getString("Id");
                Component entityName = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(entityId)).getDescription();
                list.remove(list.size() - 1);

                if (list.isEmpty()) {
                    tag.remove(FILTER_KEY);
                }

                player.displayClientMessage(Component.translatable("message.pantz_mod.entity.removed", entityName), true);
            }
        }
    }

    private void cycleMode(ItemStack stack, Player player) {
        CompoundTag tag = stack.getOrCreateTag();
        FilterMode currentMode = FilterMode.byId(tag.getInt(MODE_KEY));
        FilterMode nextMode = currentMode.next();

        tag.putInt(MODE_KEY, nextMode.getId());

        player.displayClientMessage(Component.translatable("message.pantz_mod.mode_change", Component.translatable(nextMode.getTranslationKey()).withStyle(nextMode.getColor())), true);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag list = tag.getList(FILTER_KEY, Tag.TAG_COMPOUND);

        FilterMode currentMode = FilterMode.byId(tag.getInt(MODE_KEY));
        tooltip.add(Component.translatable("tooltip.pantz_mod.current_mode", Component.translatable(currentMode.getTranslationKey()).withStyle(currentMode.getColor())));

        if (list.isEmpty()) {
            tooltip.add(Component.translatable("tooltip.pantz_mod.entity.none"));
        } else {
            for (int i = 0; i < list.size(); i++) {
                ResourceLocation id = new ResourceLocation(list.getCompound(i).getString("Id"));
                EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(id);
                Component name = (type != null) ? type.getDescription() : Component.literal(id.toString());
                tooltip.add(Component.translatable("tooltip.pantz_mod.entity", name));
            }
        }
    }
}