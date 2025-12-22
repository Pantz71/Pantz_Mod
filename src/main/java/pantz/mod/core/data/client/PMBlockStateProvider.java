package pantz.mod.core.data.client;

import com.teamabnormals.blueprint.core.data.client.BlueprintBlockStateProvider;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import pantz.mod.common.block.PedestalBlock;
import pantz.mod.common.block.RedstoneConfiguratorBlock;
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

        this.crossBlock(SMALL_SULFUR_BUD);
        this.crossBlock(MEDIUM_SULFUR_BUD);
        this.crossBlock(LARGE_SULFUR_BUD);
        this.crossBlock(SULFUR_CLUSTER);

        this.pedestalBlock(STONE_PEDESTAL);
        this.pedestalBlock(DEEPSLATE_PEDESTAL);
        this.pedestalBlock(BLACKSTONE_PEDESTAL);
        this.pedestalBlock(QUARTZ_PEDESTAL);
        this.pedestalBlock(PURPUR_PEDESTAL);
        this.pedestalBlock(PRISMARINE_PEDESTAL);

        this.enderScannerBlock(ENDER_SCANNER);
        this.redstoneConfiguratorBlock(REDSTONE_CONFIGURATOR);

        this.blockItem(WEATHER_DETECTOR);
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
}
