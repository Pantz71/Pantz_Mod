package pantz.mod.core;

import com.teamabnormals.blueprint.core.annotations.ConfigKey;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;
import org.apache.commons.lang3.tuple.Pair;

public class PMConfig {
    public static class Common {
        public static final ForgeConfigSpec COMMON_SPEC;
        public static final Common COMMON;

        static {
            Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
            COMMON_SPEC = commonSpecPair.getRight();
            COMMON = commonSpecPair.getLeft();
        }

        @ConfigKey("flint_and_steel")
        public final BooleanValue flintAndSteel;

        public final IntValue waxedBlocksDetectionRadius;
        public final IntValue enderScannerDetectionRadius;

        Common(Builder builder) {
            builder.push("Recipes");
            flintAndSteel = builder.comment("Change Flint and Steel recipe from using Iron to Steel")
                    .define("Using steel for flint and steel", true);
            builder.pop();

            builder.push("Tools");
            waxedBlocksDetectionRadius = builder.comment("How far can the Honey Deserializer detect waxed blocks?")
                    .defineInRange("Honey Deserializer detection radius", 16, 0, Integer.MAX_VALUE);

            builder.pop();

            builder.push("Redstone");
            enderScannerDetectionRadius = builder.comment("How far can the Ender Scanners detect players looking at them?")
                    .defineInRange("Ender Scanner detection radius", 64, 0, Integer.MAX_VALUE);
            builder.pop();

        }
    }
}
