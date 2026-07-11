package pantz.mod.core.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import pantz.mod.common.utils.FilterMode;
import pantz.mod.core.PantzMod;

import java.util.function.UnaryOperator;

public class PMDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, PantzMod.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> WAX_LEVEL = register("wax_level", builder -> builder.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FilterMode>> FILTER_MODE = register("filter_mode", builder -> builder.persistent(FilterMode.CODEC).networkSynchronized(FilterMode.STREAM_CODEC));


    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENTS.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

}
