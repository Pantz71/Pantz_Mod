package pantz.mod.core.other;

import com.mojang.serialization.MapCodec;
import com.teamabnormals.blueprint.core.api.conditions.ConfigValueCondition.*;
import com.teamabnormals.blueprint.core.util.DataUtil;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import pantz.mod.core.PMConfig;
import pantz.mod.core.PantzMod;

public class PMConditionSerializers {
    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_SERIALIZERS =DeferredRegister.create(NeoForgeRegistries.CONDITION_SERIALIZERS, PantzMod.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends ICondition>, Serializer> CONFIG = CONDITION_SERIALIZERS.register("config", () -> new Serializer(DataUtil.getConfigValues(PMConfig.Common.COMMON)));
}
