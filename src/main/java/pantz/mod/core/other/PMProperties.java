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
    public static final BlockSetType ICE = BlockSetType.register(new BlockSetType(PantzMod.MOD_ID + ":ice", true, SoundType.GLASS, SoundEvents.WOODEN_DOOR_CLOSE, SoundEvents.WOODEN_DOOR_OPEN, SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundEvents.WOODEN_TRAPDOOR_OPEN,  SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));

    public static final BlockBehaviour.Properties STEEL_BLOCK = BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().sound(SoundType.METAL).strength(3.0f, 6.0f).mapColor(MapColor.COLOR_GRAY);
    public static final BlockBehaviour.Properties STEEL_DOOR = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(5.0F).noOcclusion().pushReaction(PushReaction.DESTROY).mapColor(MapColor.COLOR_GRAY);
    public static final BlockBehaviour.Properties STEEL_TRAPDOOR = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(5.0F).noOcclusion().isValidSpawn(PMProperties::never).mapColor(MapColor.COLOR_GRAY);
    public static final BlockBehaviour.Properties STEEL_BARS = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion();
    public static final BlockBehaviour.Properties STEEL_LANTERN = BlockBehaviour.Properties.of().mapColor(MapColor.METAL).forceSolidOn().requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).lightLevel(litBlockEmission(15));

    public static final BlockBehaviour.Properties SULFUR_BLOCK = BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F).mapColor(MapColor.COLOR_YELLOW).sound(SoundType.AMETHYST).lightLevel(litBlockEmission(12)).ignitedByLava().randomTicks();
    public static final BlockBehaviour.Properties SULFUR_BRICKS = BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F).mapColor(MapColor.COLOR_YELLOW).sound(SoundType.AMETHYST).ignitedByLava();
    public static final BlockBehaviour.Properties SULFUR_LAMP = BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F).mapColor(MapColor.COLOR_YELLOW).sound(SoundType.AMETHYST).lightLevel(light -> 15).ignitedByLava();

    public static final BlockBehaviour.Properties PEDESTAL = BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5f, 6.0f).sound(SoundType.STONE);
    public static final BlockBehaviour.Properties DEEPSLATE_PEDESTAL = BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0f, 6.0f).sound(SoundType.DEEPSLATE);
    public static final BlockBehaviour.Properties BLACKSTONE_PEDESTAL = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5f, 6.0f).sound(SoundType.STONE);
    public static final BlockBehaviour.Properties QUARTZ_PEDESTAL = BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5f, 6.0f).sound(SoundType.STONE);
    public static final BlockBehaviour.Properties PURPUR_PEDESTAL = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_MAGENTA).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5f, 6.0f).sound(SoundType.STONE);
    public static final BlockBehaviour.Properties PRISMARINE_PEDESTAL = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5f, 6.0f).sound(SoundType.STONE);

    public static final BlockBehaviour.Properties SULFUR_CLUSTER = BlockBehaviour.Properties.of().randomTicks().mapColor(MapColor.COLOR_YELLOW).forceSolidOn().noOcclusion().sound(SoundType.AMETHYST_CLUSTER).strength(1.5F).pushReaction(PushReaction.DESTROY).lightLevel(light -> 12);
    public static final BlockBehaviour.Properties SMALL_SULFUR_BUD = BlockBehaviour.Properties.of().randomTicks().mapColor(MapColor.COLOR_YELLOW).forceSolidOn().noOcclusion().sound(SoundType.SMALL_AMETHYST_BUD).strength(1.5F).pushReaction(PushReaction.DESTROY).lightLevel(light -> 3);
    public static final BlockBehaviour.Properties MEDIUM_SULFUR_BUD = BlockBehaviour.Properties.of().randomTicks().mapColor(MapColor.COLOR_YELLOW).forceSolidOn().noOcclusion().sound(SoundType.MEDIUM_AMETHYST_BUD).strength(1.5F).pushReaction(PushReaction.DESTROY).lightLevel(light -> 6);
    public static final BlockBehaviour.Properties LARGE_SULFUR_BUD = BlockBehaviour.Properties.of().randomTicks().mapColor(MapColor.COLOR_YELLOW).forceSolidOn().noOcclusion().sound(SoundType.LARGE_AMETHYST_BUD).strength(1.5F).pushReaction(PushReaction.DESTROY).lightLevel(light -> 9);

    public static final BlockBehaviour.Properties ENDER_SCANNER = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0f, 1200.0f);
    public static final BlockBehaviour.Properties REDSTONE_CONFIGURATOR = BlockBehaviour.Properties.of().strength(3.5F, 6.0f).sound(SoundType.METAL).instrument(NoteBlockInstrument.XYLOPHONE).mapColor(MapColor.METAL).isRedstoneConductor(PMProperties::never);
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

    public static final BlockBehaviour.Properties QUARTZ_GLASS = BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(PMProperties::never).isRedstoneConductor(PMProperties::never).isSuffocating(PMProperties::never).isViewBlocking(PMProperties::never).strength(0.5f).mapColor(MapColor.QUARTZ).noCollission();
    public static final BlockBehaviour.Properties QUARTZ_GLASS_PANE = BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).strength(0.3F).sound(SoundType.GLASS).noOcclusion().strength(0.5f).mapColor(MapColor.QUARTZ).noCollission();
    public static final BlockBehaviour.Properties LAPIS_GLASS = BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(PMProperties::never).isRedstoneConductor(PMProperties::never).isSuffocating(PMProperties::never).isViewBlocking(PMProperties::never).strength(0.5f).mapColor(MapColor.LAPIS);
    public static final BlockBehaviour.Properties LAPIS_GLASS_PANE = BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).strength(0.3F).sound(SoundType.GLASS).noOcclusion().strength(0.5f).mapColor(MapColor.LAPIS);
    public static final BlockBehaviour.Properties REDSTONE_GLASS = BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).strength(0.3F).sound(SoundType.GLASS).noOcclusion().isValidSpawn(PMProperties::never).isRedstoneConductor(PMProperties::never).isSuffocating(PMProperties::never).isViewBlocking(PMProperties::never).strength(0.5f).mapColor(MapColor.COLOR_RED);
    public static final BlockBehaviour.Properties REDSTONE_GLASS_PANE = BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).strength(0.3F).sound(SoundType.GLASS).noOcclusion().strength(0.5f).mapColor(MapColor.COLOR_RED);

    public static final BlockBehaviour.Properties SNOW_BRICKS = BlockBehaviour.Properties.of().sound(SoundType.SNOW).mapColor(MapColor.SNOW).instrument(NoteBlockInstrument.GUITAR).strength(3.0f, 2.0f);
    public static final BlockBehaviour.Properties PACKED_ICE_BRICKS = BlockBehaviour.Properties.of().sound(SoundType.GLASS).mapColor(MapColor.ICE).instrument(NoteBlockInstrument.CHIME).strength(3.0f, 2.0f);
    public static final BlockBehaviour.Properties BLUE_ICE_BRICKS = BlockBehaviour.Properties.of().sound(SoundType.GLASS).mapColor(MapColor.ICE).strength(5.2f, 3.0f);

    public static final BlockBehaviour.Properties ICE_DOOR = BlockBehaviour.Properties.of().sound(SoundType.GLASS).mapColor(MapColor.ICE).strength(3.0f, 5.0f);
    public static final BlockBehaviour.Properties ICE_TRAPDOOR = BlockBehaviour.Properties.of().sound(SoundType.GLASS).mapColor(MapColor.ICE).strength(3.0f, 5.0f);
    public static final BlockBehaviour.Properties ICE_LANTERN = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().sound(SoundType.GLASS).lightLevel(litBlockEmission(15)).strength(3.0f).mapColor(MapColor.ICE);

    public static final BlockBehaviour.Properties PAPER_LANTERN = BlockBehaviour.Properties.of().lightLevel(light -> 15).strength(0.3f).sound(SoundType.WOOL).mapColor(MapColor.GOLD).forceSolidOn().ignitedByLava();
    public static final BlockBehaviour.Properties ORNAMENT = BlockBehaviour.Properties.of().instabreak().sound(SoundType.WOOL).mapColor(MapColor.COLOR_RED).ignitedByLava().isSuffocating(PMProperties::never).isViewBlocking(PMProperties::never).isRedstoneConductor(PMProperties::never).isValidSpawn(PMProperties::never).noCollission().pushReaction(PushReaction.DESTROY);

    public static final BlockBehaviour.Properties LOCK = BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(3.0f, 4.5f).instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD);
    public static final BlockBehaviour.Properties UNIVERSAL_LOCK = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(3.0f, 4.5f).instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD);

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
