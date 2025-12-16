package pantz.mod.core.other;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import pantz.mod.core.PantzMod;

public class PMProperties {
    public static final BlockSetType STEEL = BlockSetType.register(new BlockSetType(PantzMod.MOD_ID + ":steel", false, SoundType.METAL, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));

    public static final BlockBehaviour.Properties STEEL_BLOCK = BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(3.0f, 6.0f).mapColor(MapColor.COLOR_GRAY);
    public static final BlockBehaviour.Properties STEEL_DOOR = BlockBehaviour.Properties.copy(Blocks.IRON_DOOR).mapColor(MapColor.COLOR_GRAY);
    public static final BlockBehaviour.Properties STEEL_TRAPDOOR = BlockBehaviour.Properties.copy(Blocks.IRON_TRAPDOOR).mapColor(MapColor.COLOR_GRAY);
    public static final BlockBehaviour.Properties STEEL_BARS = BlockBehaviour.Properties.copy(Blocks.IRON_BARS).strength(4.0f, 6.0f);

    public static final BlockBehaviour.Properties SULFUR_BLOCK = BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK).mapColor(MapColor.COLOR_YELLOW).ignitedByLava();
}
