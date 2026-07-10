package pantz.mod.common.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import pantz.mod.common.item.PotionSatchelItem;
import pantz.mod.core.PantzMod;

public class C2SDrinkSatchelPotionPacket implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<C2SDrinkSatchelPotionPacket> TYPE =
            new CustomPacketPayload.Type<>(PantzMod.location("drink_satchel_potion"));

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SDrinkSatchelPotionPacket> STREAM_CODEC =
            StreamCodec.unit(new C2SDrinkSatchelPotionPacket());

    public C2SDrinkSatchelPotionPacket() {}

    @Override
    public CustomPacketPayload.Type<?> type() {
        return TYPE;
    }

    public static void handle(C2SDrinkSatchelPotionPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player instanceof ServerPlayer serverPlayer) {

                ItemStack mainHand = serverPlayer.getMainHandItem();
                ItemStack offHand = serverPlayer.getOffhandItem();

                if (mainHand.getItem() instanceof PotionSatchelItem) {
                    PotionSatchelItem.tryQuickDrink(serverPlayer, mainHand);
                } else if (offHand.getItem() instanceof PotionSatchelItem) {
                    PotionSatchelItem.tryQuickDrink(serverPlayer, offHand);
                } else {
                    Inventory inventory = serverPlayer.getInventory();
                    for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
                        ItemStack stack = inventory.getItem(slot);
                        if (stack.getItem() instanceof PotionSatchelItem) {
                            PotionSatchelItem.tryQuickDrink(serverPlayer, stack);
                            break;
                        }
                    }
                }
            }
        });
    }
}
