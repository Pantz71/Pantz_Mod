package pantz.mod.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
import pantz.mod.core.PMConfig;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class EntityFilterItem extends Item {
    private static final String FILTER_KEY = "FilteredEntity";

    public EntityFilterItem(Properties props) {
        super(props);
    }



    public boolean addMobToStack(ItemStack stack, EntityType<?> type) {
        CompoundTag tag = stack.getOrCreateTag();

        ListTag list;
        if (tag.contains(FILTER_KEY, Tag.TAG_LIST)) {
            list = tag.getList(FILTER_KEY, Tag.TAG_STRING);
        } else {
            list = new ListTag();
            tag.put(FILTER_KEY, list);
        }

        String id = EntityType.getKey(type).toString();

        for (int i = 0; i < list.size(); i++) {
            if (list.getString(i).equals(id)) {
                return false;
            }
        }

        list.add(StringTag.valueOf(id));
        tag.put(FILTER_KEY, list);
        return true;
    }

    private ResourceLocation removeLast(Set<ResourceLocation> set) {
        ResourceLocation removedMobs = null;
        if (!set.isEmpty()) {
            int idx = 0;
            for (ResourceLocation id : set) {
                if (idx == set.size() - 1) {
                    removedMobs = id;
                }
                idx++;
            }
            set.remove(removedMobs);
        }
        return removedMobs;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        Player player = ctx.getPlayer();
        ItemStack stack = ctx.getItemInHand();

        if (player == null || level.isClientSide() || !PMConfig.Common.COMMON.enableEntityFilter.get()) {
            return InteractionResult.PASS;
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof EntityDetectorBlockEntity detector)) return InteractionResult.PASS;

        if (player.isShiftKeyDown()) {
            Set<ResourceLocation> filterMobs = getMobsFromStack(stack);
            if (!filterMobs.isEmpty()) {
                List<ResourceLocation> detectorMobs = detector.getMobList();
                for (ResourceLocation mob : filterMobs) {
                    if (!detectorMobs.contains(mob)) detectorMobs.add(mob);
                }
                detector.setFilterMobs(detectorMobs);
                player.displayClientMessage(
                        Component.translatable("message.pantz_mod.entity_filter.applied", filterMobs.size()),
                        true);
            }
        } else {
            List<ResourceLocation> mobList = detector.getMobList();
            if (!mobList.isEmpty()) {
                ResourceLocation removed = mobList.get(mobList.size() - 1);
                detector.removeLastMob();

                Component mobName = ForgeRegistries.ENTITY_TYPES.getValue(removed) != null
                        ? ForgeRegistries.ENTITY_TYPES.getValue(removed).getDescription()
                        : Component.translatable("tooltip.pantz_mod.entity_filter.unknown").withStyle(ChatFormatting.DARK_PURPLE);

                player.displayClientMessage(
                        Component.translatable("message.pantz_mod.entity_filter.removed", mobName),
                        true);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide() || !PMConfig.Common.COMMON.enableEntityFilter.get() || !player.isShiftKeyDown())
            return InteractionResultHolder.pass(stack);

        HitResult hit = player.pick(5.0D, 0.0F, false);
        if (hit.getType() != HitResult.Type.MISS) {
            return InteractionResultHolder.pass(stack);
        }
        Set<ResourceLocation> mobs = getMobsFromStack(stack);
        if (!mobs.isEmpty()) {
            ResourceLocation removed = removeLast(mobs);
            saveMobsToStack(stack, mobs);

            if (removed != null) {
                EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(removed);
                Component mobName = (type != null) ? type.getDescription() : Component.literal(removed.toString());

                player.displayClientMessage(
                        Component.translatable("message.pantz_mod.entity_filter.removed", mobName),
                        true
                );
            }
            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.pass(stack);
    }



    private void saveMobsToStack(ItemStack stack, Set<ResourceLocation> mobs) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag list = new ListTag();
        for (ResourceLocation id : mobs) {
            list.add(StringTag.valueOf(id.toString()));
        }
        tag.put(FILTER_KEY, list);
    }

    public static Set<ResourceLocation> getMobsFromStack(ItemStack stack) {
        Set<ResourceLocation> out = new LinkedHashSet<>();
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(FILTER_KEY, Tag.TAG_LIST)) {
            ListTag list = tag.getList(FILTER_KEY, Tag.TAG_STRING);
            for (int i = 0; i < list.size(); i++) {
                out.add(new ResourceLocation(list.getString(i)));
            }
        }
        return out;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        Set<ResourceLocation> set = getMobsFromStack(stack);

        if (set.isEmpty()) {
            tooltip.add(Component.translatable("tooltip.pantz_mod.entity_filter", Component.translatable("tooltip.pantz_mod.entity_filter.none")).withStyle(ChatFormatting.GRAY));
        } else {
            for (ResourceLocation id : set) {
                EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(id);
                Component name = (type != null) ? type.getDescription() : Component.literal(id.toString());
                tooltip.add(Component.translatable("tooltip.pantz_mod.entity_filter", name).withStyle(ChatFormatting.GREEN));
            }
        }

        super.appendHoverText(stack, level, tooltip, flag);
    }
}
