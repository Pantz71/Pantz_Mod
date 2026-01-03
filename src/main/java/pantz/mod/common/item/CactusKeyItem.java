package pantz.mod.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import pantz.mod.common.block.entity.TrashCanBlockEntity;
import pantz.mod.core.PMConfig;
import pantz.mod.core.registry.PMSoundEvents;

public class CactusKeyItem extends Item {
    public CactusKeyItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        Level level = ctx.getLevel();
        Player player = ctx.getPlayer();

        if (player == null || !PMConfig.Common.COMMON.enableCactusKey.get()) {
            return InteractionResult.PASS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof TrashCanBlockEntity trashCanBlock) {
            if (player.isShiftKeyDown() && PMConfig.Common.COMMON.enableCactusKey.get()) {
                trashCanBlock.clearContentInside();
                level.playSound(null, pos, PMSoundEvents.TRASH_CAN_DESTROY.get(), SoundSource.BLOCKS);
                player.displayClientMessage(Component.translatable("message.pantz_mod.trash"), true);
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }
        return InteractionResult.PASS;
    }
}
