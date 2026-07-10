package pantz.mod.core.other;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import pantz.mod.common.network.C2SDrinkSatchelPotionPacket;
import pantz.mod.core.PantzMod;

@EventBusSubscriber(modid = PantzMod.MOD_ID)
public class PMNetwork {
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(C2SDrinkSatchelPotionPacket.TYPE, C2SDrinkSatchelPotionPacket.STREAM_CODEC, C2SDrinkSatchelPotionPacket::handle);
    }

    public static void sendToServer(C2SDrinkSatchelPotionPacket message) {
        PacketDistributor.sendToServer(message);
    }
}
