package pantz.mod.core.other;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import pantz.mod.core.PantzMod;

public class PMProperties {
    public static final BlockSetType STEEL = BlockSetType.register(new BlockSetType(PantzMod.MOD_ID + ":steel", false, SoundType.METAL, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));

    public static final BlockBehaviour.Properties STEEL_BLOCK = BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(3.0f, 6.0f).mapColor(MapColor.COLOR_GRAY);
    public static final BlockBehaviour.Properties STEEL_DOOR = BlockBehaviour.Properties.copy(Blocks.IRON_DOOR).mapColor(MapColor.COLOR_GRAY);
    public static final BlockBehaviour.Properties STEEL_TRAPDOOR = BlockBehaviour.Properties.copy(Blocks.IRON_TRAPDOOR).mapColor(MapColor.COLOR_GRAY);
    public static final BlockBehaviour.Properties STEEL_BARS = BlockBehaviour.Properties.copy(Blocks.IRON_BARS).strength(4.0f, 6.0f);

    public static final BlockBehaviour.Properties SULFUR_BLOCK = BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK).mapColor(MapColor.COLOR_YELLOW).sound(SoundType.AMETHYST).ignitedByLava().randomTicks();
    public static final BlockBehaviour.Properties SULFUR_BRICKS = BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK).mapColor(MapColor.COLOR_YELLOW).sound(SoundType.AMETHYST).ignitedByLava();

    public static final BlockBehaviour.Properties PEDESTAL = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5f, 6.0f).sound(SoundType.STONE);
    public static final BlockBehaviour.Properties DEEPSLATE_PEDESTAL = BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0f, 6.0f).sound(SoundType.DEEPSLATE);
    public static final BlockBehaviour.Properties BLACKSTONE_PEDESTAL = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5f, 6.0f).sound(SoundType.STONE);
    public static final BlockBehaviour.Properties QUARTZ_PEDESTAL = BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5f, 6.0f).sound(SoundType.STONE);
    public static final BlockBehaviour.Properties PURPUR_PEDESTAL = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_MAGENTA).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5f, 6.0f).sound(SoundType.STONE);
    public static final BlockBehaviour.Properties PRISMARINE_PEDESTAL = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5f, 6.0f).sound(SoundType.STONE);

    public static final BlockBehaviour.Properties SULFUR_CLUSTER = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).forceSolidOn().noOcclusion().randomTicks().sound(SoundType.AMETHYST_CLUSTER).strength(1.5F).pushReaction(PushReaction.DESTROY).lightLevel(light -> 12).randomTicks();
    public static final BlockBehaviour.Properties SMALL_SULFUR_BUD = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).forceSolidOn().noOcclusion().randomTicks().sound(SoundType.SMALL_AMETHYST_BUD).strength(1.5F).pushReaction(PushReaction.DESTROY).lightLevel(light -> 3).randomTicks();
    public static final BlockBehaviour.Properties MEDIUM_SULFUR_BUD = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).forceSolidOn().noOcclusion().randomTicks().sound(SoundType.MEDIUM_AMETHYST_BUD).strength(1.5F).pushReaction(PushReaction.DESTROY).lightLevel(light -> 6).randomTicks();
    public static final BlockBehaviour.Properties LARGE_SULFUR_BUD = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).forceSolidOn().noOcclusion().randomTicks().sound(SoundType.LARGE_AMETHYST_BUD).strength(1.5F).pushReaction(PushReaction.DESTROY).lightLevel(light -> 9).randomTicks();

}
