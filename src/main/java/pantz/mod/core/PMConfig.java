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



        public final IntValue waxedBlocksDetectionRadius;
        public final IntValue enderScannerDetectionRadius;
        public final IntValue entityDetectorDetectionRadius;
        public final IntValue enderporterDetectionRadius;

        @ConfigKey("flint_and_steel")
        public final BooleanValue flintAndSteel;

        @ConfigKey("entity_filter")
        public final BooleanValue enableEntityFilter;

        @ConfigKey("cactus_key")
        public final BooleanValue enableCactusKey;


        Common(Builder builder) {
            builder.push("Recipes");
            flintAndSteel = builder.comment("Change Flint and Steel recipe from using Iron to Steel")
                    .define("Using steel for flint and steel", true);
            builder.pop();

            builder.push("Tools");
            waxedBlocksDetectionRadius = builder.comment("How far can the Honey Deserializer detect waxed blocks?")
                    .defineInRange("Honey Deserializer detection radius", 16, 0, Integer.MAX_VALUE);

            enableEntityFilter = builder.comment("Allow filter entities feature of Entity Detector?")
                    .define("Entity filtering", true);

            enableCactusKey = builder.comment("Allow Trash Can to require players to hold a Cactus Key instead of empty hand")
                    .define("Cactus Key", true);

            builder.pop();

            builder.push("Redstone");
            enderScannerDetectionRadius = builder.comment("How far can the Ender Scanners detect players looking at them?")
                    .defineInRange("Ender Scanner detection radius", 64, 0, Integer.MAX_VALUE);

            entityDetectorDetectionRadius = builder.comment("How far can the Entity Detector detect entities?")
                    .defineInRange("Entity Detector detection radius", 64, 0, Integer.MAX_VALUE);

            builder.pop();

            builder.push("Functional");
            enderporterDetectionRadius = builder.comment("How far can the Enderporter detect thrown Ender Pearls?")
                    .defineInRange("Enderporter detection radius", 32, 0, Integer.MAX_VALUE);

            builder.pop();

        }
    }
}
