package pantz.mod.core.data.client;

import com.teamabnormals.blueprint.core.data.client.BlueprintBlockStateProvider;
import com.teamabnormals.blueprint.core.data.client.BlueprintItemModelProvider;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import pantz.mod.common.block.*;
import pantz.mod.common.utils.CarpetColor;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.PMBlockFamilies;

import static pantz.mod.core.registry.PMBlocks.*;

public class PMBlockStateProvider extends BlueprintBlockStateProvider {
    public PMBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, PantzMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.block(STEEL_BLOCK);
        this.ironBarsBlock(STEEL_BARS);
        this.doorBlocks(STEEL_DOOR.get(), STEEL_TRAPDOOR.get());

        this.litableBlock(SULFUR_BLOCK, "_lit");
        this.blockFamily(PMBlockFamilies.SULFUR_BRICKS_FAMILY);
        this.block(NETHER_SULFUR_ORE);
        this.block(CHISELED_SULFUR_BRICKS);

        this.clusterBlock(SMALL_SULFUR_BUD);
        this.clusterBlock(MEDIUM_SULFUR_BUD);
        this.clusterBlock(LARGE_SULFUR_BUD);
        this.clusterBlock(SULFUR_CLUSTER);

        this.pedestalBlock(STONE_PEDESTAL);
        this.pedestalBlock(DEEPSLATE_PEDESTAL);
        this.pedestalBlock(BLACKSTONE_PEDESTAL);
        this.pedestalBlock(QUARTZ_PEDESTAL);
        this.pedestalBlock(PURPUR_PEDESTAL);
        this.pedestalBlock(PRISMARINE_PEDESTAL);

        this.enderScannerBlock(ENDER_SCANNER);
        this.redstoneConfiguratorBlock(REDSTONE_CONFIGURATOR);
        this.blockItem(WEATHER_DETECTOR);
        this.blockItem(ENTITY_DETECTOR);
        this.powerDisplayerBlock(POWER_DISPLAYER);

        this.globeBlock(MERCURY_GLOBE, "normal", "normal", "golden", Blocks.GOLD_BLOCK);
        this.globeBlock(VENUS_GLOBE, "normal", "normal", "golden", Blocks.GOLD_BLOCK);
        this.globeBlock(EARTH_GLOBE, "normal", "normal", "golden", Blocks.GOLD_BLOCK);
        this.globeBlock(MARS_GLOBE, "normal", "normal", "golden", Blocks.GOLD_BLOCK);
        this.globeBlock(JUPITER_GLOBE, "large", "normal", "copper", Blocks.COPPER_BLOCK);
        this.globeBlock(SATURN_GLOBE, "saturn", "normal", "copper", Blocks.COPPER_BLOCK);
        this.globeBlock(URANUS_GLOBE, "uranus", "normal", "iron", Blocks.IRON_BLOCK);
        this.globeBlock(NEPTUNE_GLOBE, "large", "normal", "iron", Blocks.IRON_BLOCK);
        this.globeBlock(MOON_GLOBE, "small", "normal", "emerald", Blocks.EMERALD_BLOCK);
        this.globeBlock(IO_GLOBE, "small", "normal", "emerald", Blocks.EMERALD_BLOCK);
        this.globeBlock(EUROPA_GLOBE, "small", "normal", "emerald", Blocks.EMERALD_BLOCK);
        this.globeBlock(GANYMEDE_GLOBE, "small", "normal", "emerald", Blocks.EMERALD_BLOCK);
        this.globeBlock(CALLISTO_GLOBE, "small", "normal", "emerald", Blocks.EMERALD_BLOCK);
        this.globeBlock(PLUTO_GLOBE, "tiny", "normal", "quartz", mcLoc("block/quartz_block_top"));
        this.globeBlock(CERES_GLOBE, "tiny", "normal", "quartz", mcLoc("block/quartz_block_top"));
        this.globeBlock(MAKEMAKE_GLOBE, "tiny", "normal", "quartz", mcLoc("block/quartz_block_top"));
        this.globeBlock(SUN_GLOBE, "giant", "giant", "amethyst", Blocks.AMETHYST_BLOCK);
        this.globeBlock(BLUE_SUN_GLOBE, "giant", "giant", "amethyst", Blocks.AMETHYST_BLOCK);
        this.globeBlock(IRIS_GLOBE, "large", "normal", "diamond", Blocks.DIAMOND_BLOCK);

        this.redstoneLampBlock(WHITE_REDSTONE_LAMP);
        this.redstoneLampBlock(ORANGE_REDSTONE_LAMP);
        this.redstoneLampBlock(MAGENTA_REDSTONE_LAMP);
        this.redstoneLampBlock(LIGHT_BLUE_REDSTONE_LAMP);
        this.redstoneLampBlock(YELLOW_REDSTONE_LAMP);
        this.redstoneLampBlock(LIME_REDSTONE_LAMP);
        this.redstoneLampBlock(PINK_REDSTONE_LAMP);
        this.redstoneLampBlock(GRAY_REDSTONE_LAMP);
        this.redstoneLampBlock(LIGHT_GRAY_REDSTONE_LAMP);
        this.redstoneLampBlock(CYAN_REDSTONE_LAMP);
        this.redstoneLampBlock(PURPLE_REDSTONE_LAMP);
        this.redstoneLampBlock(BLUE_REDSTONE_LAMP);
        this.redstoneLampBlock(BROWN_REDSTONE_LAMP);
        this.redstoneLampBlock(GREEN_REDSTONE_LAMP);
        this.redstoneLampBlock(RED_REDSTONE_LAMP);
        this.redstoneLampBlock(BLACK_REDSTONE_LAMP);

        this.generatedItem(ITEM_STAND.get(), "item");
        this.generatedItem(GLOW_ITEM_STAND.get(), "item");
        this.blockItem(TRASH_CAN);
        this.enderporterBlock(ENDERPORTER);
        this.ropeLadderBlock(ROPE_LADDER);

        this.block(QUARTZ_GLASS);
        this.block(LAPIS_GLASS);
        this.glassPaneBlock(QUARTZ_GLASS_PANE, QUARTZ_GLASS);
        this.glassPaneBlock(LAPIS_GLASS_PANE, LAPIS_GLASS);

    }

    private void redstoneConfiguratorBlock(RegistryObject<Block> block) {
        String name = name(block.get());
        ResourceLocation texture = blockTexture(block.get());
        for (int power = 0; power <= 15; power++) {
            String modelName = power == 0 ? name : name + "_" + power;
            ResourceLocation frontTexture = power == 0 ? texture : suffix(texture, "_" + power);

            ModelFile model = models()
                    .withExistingParent(modelName, modLoc("block/template_redstone_configurator"))
                    .texture("front", frontTexture);
            for (Direction dir : Direction.values()) {
                int rotX = 0;
                int rotY = 0;
                switch (dir) {
                    case UP -> rotX = 90;
                    case DOWN -> rotX = 270;
                    case NORTH -> rotY = 180;
                    case SOUTH -> {}
                    case WEST -> rotY = 90;
                    case EAST -> rotY = 270;
                }
                getVariantBuilder(block.get()).partialState()
                        .with(RedstoneConfiguratorBlock.FACING, dir)
                        .with(RedstoneConfiguratorBlock.POWER, power)
                        .modelForState().modelFile(model)
                        .rotationX(rotX).rotationY(rotY).addModel();
            }
        }
        blockItem(block);
    }

    private void enderScannerBlock(RegistryObject<Block> block) {
        for (int power = 0; power <= 15; power++) {
            String modelName = power == 0 ? name(block.get())
                    : name(block.get()) + "_" + power;
            ResourceLocation texture = modLoc("block/" + modelName);
            getVariantBuilder(block.get())
                    .partialState().with(BlockStateProperties.POWER, power)
                    .modelForState()
                    .modelFile(models().cubeAll(modelName, texture))
                    .addModel();
        }
        blockItem(block);
    }

    private void redstoneLampBlock(RegistryObject<Block> block) {
        litableBlock(block, "_on");
    }

    private void litableBlock(RegistryObject<Block> block, String suffix) {
        ResourceLocation unlit = blockTexture(block.get());
        ResourceLocation lit = suffix(unlit, suffix);
        getVariantBuilder(block.get())
                .partialState().with(BlockStateProperties.LIT, false)
                .modelForState().modelFile(models().cubeAll(name(block.get()), unlit)).addModel()
                .partialState().with(BlockStateProperties.LIT, true)
                .modelForState().modelFile(models().cubeAll(name(block.get()) + suffix, lit)).addModel();
        blockItem(block);
    }

    private void pedestalBlock(RegistryObject<Block> block) {
        getVariantBuilder(block.get()).forAllStates(state -> {
            Direction dir = state.getValue(PedestalBlock.FACING);
            CarpetColor carpet = state.getValue(PedestalBlock.CARPET);

            ModelFile model;
            if (carpet == CarpetColor.NONE) {
                model = models()
                        .withExistingParent(name(block.get()), modLoc("block/template_pedestal"))
                        .texture("pedestal", blockTexture(block.get()));
            } else {
                model = models()
                        .withExistingParent(name(block.get()) + "_" + carpet.getSerializedName(),
                                modLoc("block/template_decorated_pedestal"))
                        .texture("pedestal", blockTexture(block.get()))
                        .texture("carpet", modLoc("block/" + carpet.getSerializedName() + "_carpet_pedestal"));
            }

            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationY((int) dir.toYRot())
                    .build();
        });
        blockItem(block);
    }

    public void clusterBlock(RegistryObject<Block> block) {
        String name = name(block.get());
        ModelFile model = models().cross(name, blockTexture(block.get()));

        for (Direction dir : Direction.values()) {
            int rotX = 0;
            int rotY = 0;

            switch (dir) {
                case DOWN -> rotX = 180;
                case NORTH -> rotX = 90;
                case SOUTH -> { rotX = 90; rotY = 180; }
                case EAST  -> { rotX = 90; rotY = 90; }
                case WEST  -> { rotX = 90; rotY = 270; }
                case UP -> {}
            }

            getVariantBuilder(block.get()).partialState().with(BlockStateProperties.FACING, dir).modelForState()
                    .modelFile(model).rotationX(rotX).rotationY(rotY).addModel();
        }
        generatedItem(block.get(), blockTexture(block.get()));
    }

    private void powerDisplayerBlock(RegistryObject<Block> block) {
        String name = name(block.get());
        ResourceLocation texture = blockTexture(block.get());
        for (int power = 0; power <= 15; power++) {
            String modelName = power == 0 ? name : name + "_" + power;
            ResourceLocation frontTexture = power == 0 ? texture : suffix(texture, "_" + power);
            ModelFile model = models()
                    .withExistingParent(modelName, modLoc("block/template_power_displayer"))
                    .texture("front", frontTexture);

            for (Direction dir : Direction.values()) {
                int rotX = 0;
                int rotY = 0;
                switch (dir) {
                    case UP -> rotX = 90;
                    case DOWN -> rotX = 270;
                    case NORTH -> rotY = 180;
                    case SOUTH -> {}
                    case WEST -> rotY = 90;
                    case EAST -> rotY = 270;
                }
                getVariantBuilder(block.get()).partialState()
                        .with(PowerDisplayerBlock.FACING, dir).with(PowerDisplayerBlock.POWER, power)
                        .modelForState().modelFile(model)
                        .rotationX(rotX).rotationY(rotY).addModel();
            }
        }
        generatedItem(block.get(), texture);
    }

    private void globeBlock(RegistryObject<Block> block, String model, String stand, String standTexture, Block blockParticle) {
        globeBlock(block, modLoc("item/" + model + "_globe_stand"), modLoc("block/" + stand + "_globe_stand"), modLoc("block/" + standTexture + "_globe_stand"), blockTexture(blockParticle));
    }

    private void globeBlock(RegistryObject<Block> block, String model, String stand, String standTexture, ResourceLocation texture) {
        globeBlock(block, modLoc("item/" + model + "_globe_stand"), modLoc("block/" + stand + "_globe_stand"), modLoc("block/" + standTexture + "_globe_stand"), texture);
    }

    private void globeBlock(RegistryObject<Block> block, ResourceLocation model, ResourceLocation stand, ResourceLocation standTexture, ResourceLocation particle) {
        getVariantBuilder(block.get()).forAllStatesExcept(state -> {
            Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
            return ConfiguredModel.builder()
                    .modelFile(models()
                            .withExistingParent(name(block.get()), stand)
                            .texture("stand", standTexture)
                            .texture("particle", particle))
                    .rotationY((int) facing.toYRot())
                    .build();
        }, BlockStateProperties.WATERLOGGED);

        ResourceLocation texturePath = PantzMod.location("planets/earth");
        if (block.get() instanceof GlobeBlock globeBlock) {
            texturePath = globeBlock.getTexture();
        }

        this.itemModels().withExistingParent(name(block.get()), model)
                .texture("stand", standTexture)
                .texture("globe", prefix("block/globe/", texturePath));
    }

    private void enderporterBlock(RegistryObject<Block> block) {
        String name = name(block.get());
        for (int charge = 0; charge <= 4; charge++) {
            String textureName = name + "_side" + charge;
            String topTextureName = charge > 0 ? "_on" : "";
            String modelName = charge == 0 ? name : name + charge;
            ResourceLocation texture = modLoc("block/" + textureName);
            ResourceLocation topTexture = modLoc("block/" + "enderporter_top" + topTextureName);

            ModelFile model = models()
                    .withExistingParent(modelName, modLoc("block/template_enderporter"))
                    .texture("top", topTexture)
                    .texture("side", texture);

            getVariantBuilder(block.get()).partialState()
                    .with(EnderporterBlock.ENDERPORTER_CHARGE, charge)
                    .modelForState().modelFile(model).addModel();

        }
        blockItem(block.get());
    }

    private void ropeLadderBlock(RegistryObject<Block> block) {
        String name = name(block.get());
        ResourceLocation texture = blockTexture(block.get());

        ModelFile model = models()
                .withExistingParent(name, mcLoc("block/ladder"))
                .texture("particle", texture)
                .texture("texture", texture);

        ModelFile topModel = models()
                .withExistingParent(name + "_top", mcLoc("block/ladder"))
                .texture("particle", suffix(texture, "_top"))
                .texture("texture", suffix(texture, "_top"));

        ModelFile bottomModel = models()
                .withExistingParent(name + "_bottom", mcLoc("block/ladder"))
                .texture("particle", suffix(texture, "_bottom"))
                .texture("texture", suffix(texture, "_bottom"));

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            int rotY = switch (dir) {
                case SOUTH -> 180;
                case WEST  -> 270;
                case EAST  -> 90;
                default    -> 0;
            };

            for (boolean top : new boolean[]{false, true}) {
                for (boolean bottom : new boolean[]{false, true}) {

                    ModelFile modelFile = top ? topModel : bottom ? bottomModel : model;

                    getVariantBuilder(block.get()).partialState()
                            .with(RopeLadderBlock.FACING, dir).with(RopeLadderBlock.TOP, top).with(RopeLadderBlock.BOTTOM, bottom)
                            .modelForState().modelFile(modelFile).rotationY(rotY).addModel();
                }
            }
        }

        generatedItem(block.get(), texture);
    }

    public void glassPaneBlock(RegistryObject<Block> pane, RegistryObject<Block> glass) {
        Block block = pane.get();
        String name = name(block);

        ResourceLocation texture = blockTexture(glass.get());
        ResourceLocation edgeTexture = texture.withSuffix("_pane_top");

        ModelFile post = glassPaneBlock(name, "post").texture("pane", texture).texture("edge", edgeTexture);
        ModelFile side = glassPaneBlock(name, "side").texture("pane", texture).texture("edge", edgeTexture);
        ModelFile sideAlt = glassPaneBlock(name, "side_alt").texture("pane", texture).texture("edge", edgeTexture);
        ModelFile noSide = glassPaneBlock(name, "noside").texture("pane", texture);
        ModelFile noSideAlt = glassPaneBlock(name, "noside_alt").texture("pane", texture);

        this.glassPaneBlock(block, post, side, sideAlt, noSide, noSideAlt);
        this.generatedItem(block, prefix("block/", BlueprintItemModelProvider.key(glass.get())));
    }

    public void glassPaneBlock(Block block, ModelFile post, ModelFile side, ModelFile sideAlt, ModelFile noSide, ModelFile noSideAlt) {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block).part().modelFile(post).addModel().end();
        PipeBlock.PROPERTY_BY_DIRECTION.forEach((dir, value) -> {
            if (dir.getAxis().isHorizontal()) {
                builder.part().modelFile(dir == Direction.SOUTH || dir == Direction.WEST ? sideAlt : side).rotationY(dir.getAxis() == Direction.Axis.X ? 90 : 0).addModel().condition(value, true).end();
                builder.part().modelFile(dir == Direction.SOUTH || dir == Direction.EAST ? noSideAlt : noSide).rotationY(dir == Direction.WEST ? 270 : dir == Direction.SOUTH ? 90 : 0).addModel().condition(value, false).end();
            }
        });
    }

    public BlockModelBuilder glassPaneBlock(String name, String suffix) {
        return models().getBuilder(name + "_" + suffix).parent(new ModelFile.UncheckedModelFile(new ResourceLocation("block/template_glass_pane_" + suffix)));
    }
}
