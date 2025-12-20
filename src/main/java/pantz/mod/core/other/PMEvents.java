package pantz.mod.core.other;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import pantz.mod.common.item.AreaDiggerItem;
import pantz.mod.core.PantzMod;

import java.util.List;


@Mod.EventBusSubscriber(modid = PantzMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PMEvents {


    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof AreaDiggerItem)) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        event.setCanceled(true);

        List<BlockPos> blocks = AreaDiggerItem.mineArea(level, player, event.getPos(), 1);
        for (BlockPos pos : blocks) {
            if (!level.getBlockState(pos).isAir() && stack.isCorrectToolForDrops(level.getBlockState(pos))) {
                level.destroyBlock(pos, !player.isCreative());
            }
        }
    }
}
