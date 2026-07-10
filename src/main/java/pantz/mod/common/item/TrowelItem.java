package pantz.mod.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
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

        if (player == null) return InteractionResult.PASS;
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        RandomSource random = level.getRandom();
        InteractionHand hand = ctx.getHand();
        ItemStack trowel = ctx.getItemInHand();
        Inventory inventory = player.getInventory();

        int trowelSlot = inventory.selected;

        List<ItemStack> placeableBlocks = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (i == trowelSlot) continue;
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                placeableBlocks.add(stack);
            }
        }

        if (placeableBlocks.isEmpty()) return InteractionResult.PASS;

        ItemStack selectedStack = placeableBlocks.get(random.nextInt(placeableBlocks.size()));
        BlockItem blockItem = (BlockItem) selectedStack.getItem();

        UseOnContext fakeContext = new UseOnContext(player, hand, new BlockHitResult(ctx.getClickLocation(), ctx.getClickedFace(), ctx.getClickedPos(), ctx.isInside())) {
            @Override
            public ItemStack getItemInHand() {
                return selectedStack;
            }
        };

        InteractionResult result = blockItem.useOn(fakeContext);

        if (result.consumesAction()) {
            BlockPos placePos = fakeContext.getClickedPos().relative(fakeContext.getClickedFace());
            BlockState state = level.getBlockState(placePos);
            SoundType sound = state.getSoundType();

            level.playSound(null, placePos, sound.getPlaceSound(), SoundSource.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
            trowel.hurtAndBreak(1, player,
                    p -> p.broadcastBreakEvent(hand));

            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }

}
