package pantz.mod.core.other;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import pantz.mod.core.PMConfig;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.tags.PMBlockTags;
import pantz.mod.core.registry.PMItems;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = PantzMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PMClientEvents {
    private static int particleCounter = 0;
    private static final int cooldown = 5;

    private static final String[] PRIORITIES = { "copper", "exposed", "weathered", "oxidized" };

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft minecraft = Minecraft.getInstance();
            spawnWaxParticles(minecraft);
        }
    }

    private static void spawnWaxParticles(Minecraft mc) {
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        if (player == null || level == null) return;

        if (mc.isPaused()) {
            particleCounter = 0;
            return;
        }

        if (particleCounter++ % cooldown != 0) return;
        if (!isHoldingDeserializer(player)) return;

        BlockPos center = player.blockPosition();
        int range = PMConfig.Common.COMMON.waxedBlocksDetectionRadius.get();

        List<BlockPos> priority = new ArrayList<>();
        List<BlockPos> normal = new ArrayList<>();

        for (int dx = -range; dx <= range; dx++) {
            for (int dy = -range; dy <= range; dy++) {
                for (int dz = -range; dz <= range; dz++) {
                    BlockPos pos = center.offset(dx, dy, dz);
                    BlockState state = level.getBlockState(pos);

                    if (state.isAir() || state.is(PMBlockTags.NON_WAXED_BLOCKS)) continue;

                    ResourceLocation id = ForgeRegistries.BLOCKS.getKey(state.getBlock());
                    if (id == null) continue;

                    String path = id.getPath();
                    if (!path.startsWith("waxed_")) continue;

                    (containsAny(path) ? priority : normal).add(pos);
                }
            }
        }

        List<BlockPos> targets = priority.isEmpty() ? normal : concat(priority, normal);
        targets.forEach(pos -> spawnParticles(level, pos));
    }

    private static void spawnParticles(ClientLevel level, BlockPos pos) {
        RandomSource random = level.getRandom();
        double spread = 0.6;

        double blockX = pos.getX();
        double blockY = pos.getY();
        double blockZ = pos.getZ();

        for (int i = 0; i < 6; i++) {
            int face = random.nextInt(6);

            double x = blockX + 0.5;
            double y = blockY + 0.5;
            double z = blockZ + 0.5;

            int axis = face >> 1;
            double side = (face & 1) == 0 ? -0.1 : 1.1;

            if (axis == 0) {
                x = blockX + side;
            } else if (axis == 1) {
                y = blockY + side;
            } else {
                z = blockZ + side;
            }
            double randomSpread = (random.nextDouble() - 0.5) * spread;
            if (axis != 0) {
                x += randomSpread;
            }
            if (axis != 1) {
                y += randomSpread;
            }
            if (axis != 2) {
                z += randomSpread;
            }

            level.addParticle(ParticleTypes.WAX_ON, x, y, z, 0.0, 0.05, 0.0);
        }
    }

    private static boolean isHoldingDeserializer(LocalPlayer player) {
        return player.getMainHandItem().is(PMItems.HONEY_DESERIALIZER.get())
                || player.getOffhandItem().is(PMItems.HONEY_DESERIALIZER.get());
    }

    private static List<BlockPos> concat(List<BlockPos> a, List<BlockPos> b) {
        List<BlockPos> out = new ArrayList<>(a.size() + b.size());
        out.addAll(a);
        out.addAll(b);
        return out;
    }

    private static boolean containsAny(String string) {
        for (String name : PRIORITIES) {
            if (string.contains(name)) {
                return true;
            }
        }
        return false;
    }
}
