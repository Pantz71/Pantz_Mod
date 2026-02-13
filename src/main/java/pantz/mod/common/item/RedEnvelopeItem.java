package pantz.mod.common.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import pantz.mod.core.data.server.PMLootTableProvider;
import pantz.mod.core.other.PMLootContextParamSets;

public class RedEnvelopeItem extends Item {
    public RedEnvelopeItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) return InteractionResultHolder.pass(player.getItemInHand(hand));

        ServerLevel serverLevel = (ServerLevel) level;

        LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(PMLootTableProvider.ENVELOPE);
        LootParams lootParams = new LootParams.Builder(serverLevel)
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .create(PMLootContextParamSets.RED_ENVELOPE);

        lootTable.getRandomItems(lootParams).forEach(stack -> {
            if (!player.getInventory().add(stack)) {
                player.drop(stack, false);
            }
        });
        player.getItemInHand(hand).shrink(1);
        level.playSound(null, player.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS);

        return InteractionResultHolder.success(player.getItemInHand(hand));
    }


}
