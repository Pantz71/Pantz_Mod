package pantz.mod.core.data.server.tags;


import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import pantz.mod.core.PantzMod;

import java.util.concurrent.CompletableFuture;

import static pantz.mod.core.registry.PMBlocks.*;
import static pantz.mod.core.other.tags.PMBlockTags.*;

public class PMBlockTagsProvider extends BlockTagsProvider {
    public PMBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, PantzMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(STEEL_BLOCK.get(), STEEL_BARS.get(), STEEL_DOOR.get(), STEEL_TRAPDOOR.get(),
                SULFUR_BLOCK.get(), SULFUR_BRICKS.get(), SULFUR_BRICK_STAIRS.get(), SULFUR_BRICK_SLAB.get(), NETHER_SULFUR_ORE.get());

        this.tag(BlockTags.NEEDS_STONE_TOOL).add(STEEL_BLOCK.get(), STEEL_BARS.get(), STEEL_DOOR.get(), STEEL_TRAPDOOR.get(),
                SULFUR_BLOCK.get(), SULFUR_BRICKS.get(), SULFUR_BRICK_STAIRS.get(), SULFUR_BRICK_SLAB.get(), SULFUR_BRICK_WALL.get(), NETHER_SULFUR_ORE.get());

        this.tag(BlockTags.STAIRS).add(SULFUR_BRICK_STAIRS.get());
        this.tag(BlockTags.SLABS).add(SULFUR_BRICK_SLAB.get());
        this.tag(BlockTags.WALLS).add(SULFUR_BRICK_WALL.get());

        this.tag(Tags.Blocks.STORAGE_BLOCKS).addTag(STORAGE_BLOCKS_STEEL).addTag(STORAGE_BLOCKS_SULFUR);
        this.tag(STORAGE_BLOCKS_STEEL).add(STEEL_BLOCK.get());
        this.tag(STORAGE_BLOCKS_SULFUR).add(SULFUR_BLOCK.get());

        this.tag(Tags.Blocks.ORES).addTag(ORES_SULFUR);
        this.tag(ORES_SULFUR).add(NETHER_SULFUR_ORE.get());

        this.tag(Tags.Blocks.ORE_RATES_SINGULAR).add(NETHER_SULFUR_ORE.get());
        this.tag(Tags.Blocks.ORES_IN_GROUND_NETHERRACK).add(NETHER_SULFUR_ORE.get());
    }
}
