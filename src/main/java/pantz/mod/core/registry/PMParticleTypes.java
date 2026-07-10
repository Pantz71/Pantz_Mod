package pantz.mod.core.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import pantz.mod.client.particle.EndSparkleParticle;
import pantz.mod.client.particle.FeatherParticle;
import pantz.mod.core.PantzMod;

public class PMParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, PantzMod.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> END_SPARKLE = registerSimpleParticleType(false, "end_sparkle");
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FEATHER = registerSimpleParticleType(false, "feather");

    private static DeferredHolder<ParticleType<?>, SimpleParticleType> registerSimpleParticleType(boolean alwaysShow, String name) {
        return PARTICLE_TYPES.register(name, () -> new SimpleParticleType(alwaysShow));
    }

    @EventBusSubscriber(modid = PantzMod.MOD_ID, value = Dist.CLIENT)
    public static class RegisterParticles {
        @SubscribeEvent
        public static void registerParticleTypes(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(END_SPARKLE.get(), EndSparkleParticle.Provider::new);
            event.registerSpriteSet(FEATHER.get(), FeatherParticle.Provider::new);

        }
    }
}
