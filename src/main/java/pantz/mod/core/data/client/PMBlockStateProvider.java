package pantz.mod.core.data.client;

import com.teamabnormals.blueprint.core.data.client.BlueprintBlockStateProvider;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import pantz.mod.common.block.PedestalBlock;
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

        this.block(SULFUR_BLOCK);
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
