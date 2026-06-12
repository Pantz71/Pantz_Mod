package pantz.mod.core.other;

import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import pantz.mod.common.network.C2SDrinkSatchelPotionPacket;
import pantz.mod.core.PantzMod;

import java.util.Optional;

public class PMNetwork {
    private static int id = -1;
    private static int nextId() {
        return id++;
    }

    private static final String PROTOCOL_VERSION = "PM1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(PantzMod.location("net")).networkProtocolVersion(() -> PROTOCOL_VERSION).clientAcceptedVersions(PROTOCOL_VERSION::equals).serverAcceptedVersions(PROTOCOL_VERSION::equals).simpleChannel();

    public static void register() {
        CHANNEL.registerMessage(nextId(), C2SDrinkSatchelPotionPacket.class, C2SDrinkSatchelPotionPacket::toBytes, C2SDrinkSatchelPotionPacket::new, C2SDrinkSatchelPotionPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));

    }

    public static <MSG> void sendToServer(MSG message) {
        CHANNEL.sendToServer(message);
    }
}
