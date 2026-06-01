package pantz.mod.core.other;

import com.teamabnormals.blueprint.core.util.DataUtil;
import net.minecraft.world.level.block.DispenserBlock;
import pantz.mod.core.registry.PMBlocks;
import pantz.mod.core.registry.PMDispenserBehaviors;
import pantz.mod.core.registry.PMItems;
import pantz.mod.core.registry.PMMobEffects;

public class PMCompat {
    public static void registerCompat() {
        registerDispenserBehaviors();
        registerFlammables();
        PMMobEffects.registerBrewingRecipes();
    }

    private static void registerDispenserBehaviors() {
        DispenserBlock.registerBehavior(PMBlocks.ROPE_LADDER.get(), PMDispenserBehaviors.EXTEND_ROPE_LADDER);
        DispenserBlock.registerBehavior(PMItems.STEEL_HORSE_ARMOR.get(), PMDispenserBehaviors.EQUIP_HORSE_ARMOR);
    }

    private static void registerFlammables() {
        DataUtil.registerFlammable(PMBlocks.ORNAMENT_FIRECRACKERS.get(), 5, 15);
        DataUtil.registerFlammable(PMBlocks.ORNAMENT_LUCKY_COINS.get(), 5, 15);
        DataUtil.registerFlammable(PMBlocks.LEATHER_BLOCK.get(), 5, 20);
        DataUtil.registerFlammable(PMBlocks.RABBIT_HIDE_BLOCK.get(), 5, 20);
        DataUtil.registerFlammable(PMBlocks.PHANTOM_MEMBRANE_BLOCK.get(), 5, 20);
        DataUtil.registerFlammable(PMBlocks.FEATHER_BLOCK.get(), 30, 60);
    }

}
