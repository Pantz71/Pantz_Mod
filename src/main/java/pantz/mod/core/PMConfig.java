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
        public final IntValue entityDetectorDetectionRadius;

        @ConfigKey("entity_filter")
        public final BooleanValue enableEntityFilter;

        public final IntValue spectreSwordCapacity;
        public final IntValue spectreWandCapacity;
        public final IntValue spectreDetectorRevelationRadius;
        public final IntValue spectreCubeRevelationRadius;

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

            spectreSwordCapacity = builder.comment("How many Spectre Souls can Spectre Sword carry?")
                    .defineInRange("Spectre Sword Capacity", 1000, 0, Integer.MAX_VALUE);

            spectreWandCapacity = builder.comment("How many Spectre Souls can Spectre Wand carry?")
                    .defineInRange("Spectre Wand Capacity", 1000, 0, Integer.MAX_VALUE);

            spectreDetectorRevelationRadius = builder.comment("How far can the Spectre Detector reveal Spectre Blocks?")
                    .defineInRange("Spectre Detector revelation radius", 32, 0, Integer.MAX_VALUE);

            spectreCubeRevelationRadius = builder.comment("How far can the Spectre Cube reveal Spectre Blocks?")
                    .defineInRange("Spectre Cube revelation radius", 32, 0, Integer.MAX_VALUE);

            enableCactusKey = builder.comment("Allow Trash Can to require players to hold a Cactus Key instead of empty hand")
                    .define("Cactus Key", true);

            builder.pop();

            builder.push("Redstone");
            enderScannerDetectionRadius = builder.comment("How far can the Ender Scanners detect players looking at them?")
                    .defineInRange("Ender Scanner detection radius", 64, 0, Integer.MAX_VALUE);

            entityDetectorDetectionRadius = builder.comment("How far can the Entity Detector detect entities?")
                    .defineInRange("Entity Detector detection radius", 64, 0, Integer.MAX_VALUE);

            builder.pop();

        }
    }
}
