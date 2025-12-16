package pantz.mod.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;

public class TrowelItem extends Item {
    public TrowelItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Player player = ctx.getPlayer();
        Level level = ctx.getLevel();
        RandomSource random = level.getRandom();

        if (player == null || level.isClientSide()) return InteractionResult.PASS;

        BlockPos pos = ctx.getClickedPos();
        Direction direction = ctx.getClickedFace();
        InteractionHand hand = ctx.getHand();
        ItemStack trowel = ctx.getItemInHand();
        Inventory playerInv = player.getInventory();

        int trowelSlot = playerInv.selected;

        List<ItemStack> placeableBlocks = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (i == trowelSlot) continue;
            ItemStack stack = playerInv.getItem(i);

            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                placeableBlocks.add(stack);
            }
        }

        if (placeableBlocks.isEmpty()) return InteractionResult.PASS;

        ItemStack selectedStack = placeableBlocks.get(random.nextInt(placeableBlocks.size()));
        BlockItem blockItem = (BlockItem) selectedStack.getItem();

        UseOnContext fakeContext = new UseOnContext(player, hand, new BlockHitResult(ctx.getClickLocation(), direction, pos, ctx.isInside())) {
            @Override
            public ItemStack getItemInHand() {
                return selectedStack;
            }

            @Override
            public InteractionHand getHand() {
                return hand;
            }
        };

        InteractionResult result = blockItem.useOn(fakeContext);

        if (result.consumesAction()) {
            BlockState placeState = blockItem.getBlock().getStateForPlacement(new BlockPlaceContext(fakeContext));

            if (placeState != null) {
                SoundEvent sound = placeState.getSoundType().getPlaceSound();
                level.playSound(null, pos.relative(direction), sound, SoundSource.BLOCKS, 1.0f, 1.0f);
            }

            if (!player.isCreative()) {
                selectedStack.shrink(1);
                trowel.hurtAndBreak(1, player, level1 -> level1.broadcastBreakEvent(ctx.getHand()));
            }

            return InteractionResult.SUCCESS;
        }
        return result;
    }
}
