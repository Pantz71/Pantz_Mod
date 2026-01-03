package pantz.mod.core.other;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import pantz.mod.core.PantzMod;

import java.util.function.ToIntFunction;

public class PMProperties {
    public static final BlockSetType STEEL = BlockSetType.register(new BlockSetType(PantzMod.MOD_ID + ":steel", false, SoundType.METAL, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));

    public static final BlockBehaviour.Properties STEEL_BLOCK = BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(3.0f, 6.0f).mapColor(MapColor.COLOR_GRAY);
    public static final BlockBehaviour.Properties STEEL_DOOR = BlockBehaviour.Properties.copy(Blocks.IRON_DOOR).mapColor(MapColor.COLOR_GRAY);
    public static final BlockBehaviour.Properties STEEL_TRAPDOOR = BlockBehaviour.Properties.copy(Blocks.IRON_TRAPDOOR).mapColor(MapColor.COLOR_GRAY);
    public static final BlockBehaviour.Properties STEEL_BARS = BlockBehaviour.Properties.copy(Blocks.IRON_BARS).strength(4.0f, 6.0f);

    public static final BlockBehaviour.Properties SULFUR_BLOCK = BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK).mapColor(MapColor.COLOR_YELLOW).sound(SoundType.AMETHYST).lightLevel(litBlockEmission(12)).ignitedByLava().randomTicks();
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

    public static final BlockBehaviour.Properties ENDER_SCANNER = BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).strength(3.0f, 1200.0f);
    public static final BlockBehaviour.Properties REDSTONE_CONFIGURATOR = BlockBehaviour.Properties.of().strength(3.5F, 6.0f).sound(SoundType.METAL).instrument(NoteBlockInstrument.XYLOPHONE).mapColor(MapColor.METAL);
    public static final BlockBehaviour.Properties WEATHER_DETECTOR = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(0.2F).sound(SoundType.STONE);
    public static final BlockBehaviour.Properties ENTITY_DETECTOR = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().mapColor(MapColor.TERRACOTTA_LIGHT_BLUE).instrument(NoteBlockInstrument.BASEDRUM).strength(0.2F).sound(SoundType.STONE);
    public static final BlockBehaviour.Properties POWER_DISPLAYER = BlockBehaviour.Properties.of().strength(0.3F).sound(SoundType.TUFF).instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.TERRACOTTA_GRAY);
    public static final BlockBehaviour.Properties DIODE = BlockBehaviour.Properties.of().instabreak().sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY);
    public static final BlockBehaviour.Properties GLOBE = BlockBehaviour.Properties.of().sound(SoundType.METAL).instabreak().noOcclusion();
    public static final BlockBehaviour.Properties GAS_GLOBE = BlockBehaviour.Properties.of().sound(SoundType.COPPER).instabreak().noOcclusion();
    public static final BlockBehaviour.Properties STAR_GLOBE = BlockBehaviour.Properties.of().sound(SoundType.AMETHYST).instabreak().noOcclusion();

    public static final BlockBehaviour.Properties REDSTONE_LAMP = BlockBehaviour.Properties.of().lightLevel(litBlockEmission(15)).strength(0.3F).sound(SoundType.GLASS).isValidSpawn(PMProperties::always);

    public static final BlockBehaviour.Properties ITEM_STAND = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().instabreak().noOcclusion();
    public static final BlockBehaviour.Properties TRASH_CAN = BlockBehaviour.Properties.of().strength(4.5f, 6f).sound(SoundType.NETHERITE_BLOCK).instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.COLOR_GRAY);
    public static final BlockBehaviour.Properties ENDERPORTER = BlockBehaviour.Properties.of().strength(2.0f, 6.0f).mapColor(MapColor.METAL).sound(SoundType.NETHERITE_BLOCK);

    public static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }

    public static boolean always(BlockState state, BlockGetter getter, BlockPos pos) {
        return true;
    }

    public static boolean always(BlockState state, BlockGetter blockGetter, BlockPos pos, EntityType<?> entityType) {
        return true;
    }

    public static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos, EntityType<?> entityType) {
        return false;
    }

    public static ToIntFunction<BlockState> litBlockEmission(int value) {
        return lit -> lit.getValue(BlockStateProperties.LIT) ? value : 0;
    }
}
