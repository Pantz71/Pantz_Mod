package pantz.mod.core.data.server.tags;


import com.teamabnormals.blueprint.core.other.tags.BlueprintBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import pantz.mod.common.block.LogicGateBlock;
import pantz.mod.common.block.NotGateBlock;
import pantz.mod.common.block.PedestalBlock;
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
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).addTag(PEDESTALS).add(STEEL_BLOCK.get(), STEEL_BARS.get(), STEEL_DOOR.get(), STEEL_TRAPDOOR.get(),
                SULFUR_BLOCK.get(), SULFUR_BRICKS.get(), SULFUR_BRICK_STAIRS.get(), SULFUR_BRICK_SLAB.get(), NETHER_SULFUR_ORE.get(),
                SULFUR_CLUSTER.get(), SMALL_SULFUR_BUD.get(), MEDIUM_SULFUR_BUD.get(), LARGE_SULFUR_BUD.get(),
                ENDER_SCANNER.get(), WEATHER_DETECTOR.get(), ENTITY_DETECTOR.get(), TRASH_CAN.get(), ENDERPORTER.get(),
                SNOW_BRICKS.get(), SNOW_BRICK_STAIRS.get(), SNOW_BRICK_SLAB.get(),
                PACKED_ICE_BRICKS.get(), PACKED_ICE_BRICK_STAIRS.get(), PACKED_ICE_BRICK_SLAB.get(), CHISELED_PACKED_ICE_BRICKS.get(),
                BLUE_ICE_BRICKS.get(), BLUE_ICE_BRICK_STAIRS.get(), BLUE_ICE_BRICK_SLAB.get(), CHISELED_BLUE_ICE_BRICKS.get(),
                PACKED_ICE_DOOR.get(), PACKED_ICE_TRAPDOOR.get(), BLUE_ICE_DOOR.get(), BLUE_ICE_TRAPDOOR.get(), ICE_LANTERN.get());

        this.tag(BlockTags.NEEDS_STONE_TOOL).add(STEEL_BLOCK.get(), STEEL_BARS.get(), STEEL_DOOR.get(), STEEL_TRAPDOOR.get(), STEEL_LANTERN.get(),
                SULFUR_BLOCK.get(), SULFUR_BRICKS.get(), SULFUR_BRICK_STAIRS.get(), SULFUR_BRICK_SLAB.get(), SULFUR_BRICK_WALL.get(), NETHER_SULFUR_ORE.get(), SULFUR_LAMP.get(),
                SULFUR_CLUSTER.get(), SMALL_SULFUR_BUD.get(), MEDIUM_SULFUR_BUD.get(), LARGE_SULFUR_BUD.get(),
                REDSTONE_CONFIGURATOR.get(), ENTITY_DETECTOR.get(), TRASH_CAN.get(), ENDERPORTER.get());

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(ENDER_SCANNER.get());

        this.tag(BlockTags.INFINIBURN_NETHER).add(SULFUR_BLOCK.get(), SULFUR_BRICKS.get(), CHISELED_SULFUR_BRICKS.get());
        this.tag(BlockTags.INSIDE_STEP_SOUND_BLOCKS).add(SMALL_SULFUR_BUD.get());

        this.tag(Tags.Blocks.STORAGE_BLOCKS).addTag(STORAGE_BLOCKS_STEEL).addTag(STORAGE_BLOCKS_SULFUR);
        this.tag(STORAGE_BLOCKS_STEEL).add(STEEL_BLOCK.get());
        this.tag(STORAGE_BLOCKS_SULFUR).add(SULFUR_BLOCK.get());

        this.tag(Tags.Blocks.ORES).addTag(ORES_SULFUR);
        this.tag(ORES_SULFUR).add(NETHER_SULFUR_ORE.get());

        this.tag(Tags.Blocks.ORE_RATES_SINGULAR).add(NETHER_SULFUR_ORE.get());
        this.tag(Tags.Blocks.ORES_IN_GROUND_NETHERRACK).add(NETHER_SULFUR_ORE.get());

        this.tag(GLASS_QUARTZ).add(QUARTZ_GLASS.get());
        this.tag(GLASS_LAPIS).add(LAPIS_GLASS.get());
        this.tag(GLASS_REDSTONE).add(REDSTONE_GLASS.get());
        this.tag(GLASS_PANES_QUARTZ).add(QUARTZ_GLASS_PANE.get());
        this.tag(GLASS_PANES_LAPIS).add(LAPIS_GLASS_PANE.get());
        this.tag(GLASS_PANES_REDSTONE).add(REDSTONE_GLASS_PANE.get());

        this.tag(Tags.Blocks.GLASS).addTag(GLASS_QUARTZ).addTag(GLASS_LAPIS).addTag(GLASS_REDSTONE);
        this.tag(Tags.Blocks.GLASS_PANES).addTag(GLASS_PANES_QUARTZ).addTag(GLASS_PANES_LAPIS).addTag(GLASS_PANES_REDSTONE);

        BLOCKS.getDeferredRegister().getEntries().forEach((registry -> {
            Block block = registry.get();
            if (block instanceof RedstoneLampBlock) {
                this.tag(REDSTONE_LAMPS).add(block, Blocks.REDSTONE_LAMP);
            }
            if (block instanceof PedestalBlock) {
                this.tag(PEDESTALS).add(block);
            }
            if (block instanceof LogicGateBlock || block instanceof NotGateBlock) {
                this.tag(LOGIC_GATES).add(block);
            }
            if (block instanceof SlabBlock) {
                this.tag(BlockTags.SLABS).add(block);
            }
            if (block instanceof StairBlock) {
                this.tag(BlockTags.STAIRS).add(block);
            }
            if (block instanceof WallBlock) {
                this.tag(BlockTags.WALLS).add(block);
            }
            if (block instanceof DoorBlock) {
                this.tag(BlockTags.DOORS).add(block);
            }
            if (block instanceof TrapDoorBlock) {
                this.tag(BlockTags.TRAPDOORS).add(block);
            }
            if (block instanceof LadderBlock) {
                this.tag(BlueprintBlockTags.WOODEN_LADDERS).add(block);
            }
        }
        ));
    }
}
