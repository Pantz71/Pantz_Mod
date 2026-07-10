package pantz.mod.core.data.server.tags;

import com.teamabnormals.blueprint.core.other.tags.BlueprintBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import pantz.mod.common.block.LogicGateBlock;
import pantz.mod.common.block.NotGateBlock;
import pantz.mod.common.block.PaperLanternBlock;
import pantz.mod.common.block.PedestalBlock;
import pantz.mod.core.PantzMod;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import static pantz.mod.core.other.tags.PMBlockTags.*;
import static pantz.mod.core.registry.PMBlocks.*;

public class PMBlockTagsProvider extends BlockTagsProvider {
    public PMBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, PantzMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).addTag(PEDESTALS).add(STEEL_BLOCK.get(), STEEL_BARS.get(), STEEL_DOOR.get(), STEEL_TRAPDOOR.get(), STEEL_LANTERN.get(), STEEL_CHAIN.get(),
                STEEL_BRICKS.get(), STEEL_BRICK_STAIRS.get(), STEEL_BRICK_SLAB.get(), STEEL_BRICK_WALL.get(), STEEL_INGOT.get(),
                SULFUR.get(), SULFUR_BLOCK.get(), POLISHED_SULFUR.get(), POLISHED_SULFUR_STAIRS.get(), POLISHED_SULFUR_SLAB.get(), POLISHED_SULFUR_WALL.get(),
                SULFUR_BRICKS.get(), SULFUR_BRICK_STAIRS.get(), SULFUR_BRICK_SLAB.get(), CHISELED_SULFUR_BRICKS.get(), NETHER_SULFUR_ORE.get(), SULFUR_LAMP.get(),
                SULFUR_CLUSTER.get(), SMALL_SULFUR_BUD.get(), MEDIUM_SULFUR_BUD.get(), LARGE_SULFUR_BUD.get(),
                ENDER_SCANNER.get(), WEATHER_DETECTOR.get(), ENTITY_DETECTOR.get(), TRASH_CAN.get(), ENDERPORTER.get(), REDSTONE_CONFIGURATOR.get(),
                SAFE.get(), SPIKE.get(), FIERY_LAMP.get());

        this.tag(BlockTags.MINEABLE_WITH_AXE).add(LOCK.get(), UNIVERSAL_LOCK.get(), SPRINKLER.get(), FEEDING_TROUGH.get());
        this.tag(BlockTags.MINEABLE_WITH_HOE).add(LEATHER_BLOCK.get(), RABBIT_HIDE_BLOCK.get(), PHANTOM_MEMBRANE_BLOCK.get(), SUGAR_CANE_BLOCK.get(), FEATHER_BLOCK.get());
        this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(SUGAR_BLOCK.get());

        this.tag(BlockTags.NEEDS_STONE_TOOL).add(STEEL_BLOCK.get(), STEEL_BARS.get(), STEEL_DOOR.get(), STEEL_TRAPDOOR.get(), STEEL_LANTERN.get(),
                STEEL_BRICKS.get(), STEEL_BRICK_STAIRS.get(), STEEL_BRICK_SLAB.get(), STEEL_BRICK_WALL.get(), STEEL_INGOT.get(),
                SULFUR.get(), SULFUR_BLOCK.get(), POLISHED_SULFUR.get(), POLISHED_SULFUR_STAIRS.get(), POLISHED_SULFUR_SLAB.get(), POLISHED_SULFUR_WALL.get(),
                SULFUR_BRICKS.get(), SULFUR_BRICK_STAIRS.get(), SULFUR_BRICK_SLAB.get(), CHISELED_SULFUR_BRICKS.get(), NETHER_SULFUR_ORE.get(), SULFUR_LAMP.get(),
                SULFUR_CLUSTER.get(), SMALL_SULFUR_BUD.get(), MEDIUM_SULFUR_BUD.get(), LARGE_SULFUR_BUD.get(),
                REDSTONE_CONFIGURATOR.get(), ENTITY_DETECTOR.get(), TRASH_CAN.get(), ENDERPORTER.get(), SAFE.get(), FIERY_LAMP.get());

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(ENDER_SCANNER.get());

        this.tag(BlockTags.INFINIBURN_OVERWORLD).add(SULFUR.get(), SULFUR_BLOCK.get(), SULFUR_BRICKS.get(), CHISELED_SULFUR_BRICKS.get());
        this.tag(BlockTags.INSIDE_STEP_SOUND_BLOCKS).add(SMALL_SULFUR_BUD.get());
        this.tag(BlockTags.BEACON_BASE_BLOCKS).add(STEEL_BLOCK.get());
        this.tag(Tags.Blocks.CHAINS).add(STEEL_CHAIN.get());

        this.tag(Tags.Blocks.STORAGE_BLOCKS).addTag(STORAGE_BLOCKS_STEEL).addTag(STORAGE_BLOCKS_SULFUR)
                .addTag(STORAGE_BLOCKS_LEATHER).addTag(STORAGE_BLOCKS_RABBIT_HIDE).addTag(STORAGE_BLOCKS_PHANTOM_MEMBRANE)
                .addTag(STORAGE_BLOCKS_FEATHER).addTag(STORAGE_BLOCKS_SUGAR).addTag(STORAGE_BLOCKS_SUGAR_CANE)
                .addTag(STORAGE_BLOCKS_BLAZE_POWDER);
        this.tag(STORAGE_BLOCKS_STEEL).add(STEEL_BLOCK.get());
        this.tag(STORAGE_BLOCKS_SULFUR).add(SULFUR_BLOCK.get());
        this.tag(STORAGE_BLOCKS_LEATHER).add(LEATHER_BLOCK.get());
        this.tag(STORAGE_BLOCKS_RABBIT_HIDE).add(RABBIT_HIDE_BLOCK.get());
        this.tag(STORAGE_BLOCKS_PHANTOM_MEMBRANE).add(PHANTOM_MEMBRANE_BLOCK.get());
        this.tag(STORAGE_BLOCKS_FEATHER).add(FEATHER_BLOCK.get());
        this.tag(STORAGE_BLOCKS_SUGAR).add(SUGAR_BLOCK.get());
        this.tag(STORAGE_BLOCKS_SUGAR_CANE).add(SUGAR_CANE_BLOCK.get());
        this.tag(STORAGE_BLOCKS_BLAZE_POWDER).add(FIERY_LAMP.get());

        this.tag(Tags.Blocks.ORES).addTag(ORES_SULFUR);
        this.tag(ORES_SULFUR).add(SULFUR_ORE.get(), DEEPSLATE_SULFUR_ORE.get(), NETHER_SULFUR_ORE.get());

        this.tag(Tags.Blocks.ORE_RATES_DENSE).add(SULFUR_ORE.get(), DEEPSLATE_SULFUR_ORE.get());
        this.tag(Tags.Blocks.ORE_RATES_SINGULAR).add(NETHER_SULFUR_ORE.get());
        this.tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(SULFUR_ORE.get());
        this.tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(DEEPSLATE_SULFUR_ORE.get());
        this.tag(Tags.Blocks.ORES_IN_GROUND_NETHERRACK).add(NETHER_SULFUR_ORE.get());

        this.tag(GLASS_BLOCKS_CHORUS).add(CHORUS_GLASS.get());
        this.tag(GLASS_BLOCKS_SOUL).add(SOUL_GLASS.get());
        this.tag(GLASS_PANES_CHORUS).add(CHORUS_GLASS_PANE.get());
        this.tag(GLASS_PANES_SOUL).add(SOUL_GLASS_PANE.get());
        this.tag(GLASS_BLOCKS_ECHO).add(ECHO_GLASS.get());
        this.tag(GLASS_PANES_ECHO).add(ECHO_GLASS_PANE.get());

        this.tag(Tags.Blocks.GLASS_BLOCKS).addTag(GLASS_BLOCKS_CHORUS).addTag(GLASS_BLOCKS_SOUL).addTag(GLASS_BLOCKS_ECHO);
        this.tag(Tags.Blocks.GLASS_PANES).addTag(GLASS_PANES_CHORUS).addTag(GLASS_PANES_SOUL).addTag(GLASS_PANES_ECHO);

        BLOCKS.getDeferredRegister().getEntries().forEach((registry -> {
            Block block = registry.get();
            if (block instanceof RedstoneLampBlock) {
                this.tag(REDSTONE_LAMPS).add(block, Blocks.REDSTONE_LAMP);
            }
            if (block instanceof PaperLanternBlock) {
                this.tag(PAPER_LANTERNS).add(block);
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

        this.addColored(Tags.Blocks.DYED,"{color}_redstone_lamp");
        this.addColored(Tags.Blocks.DYED,"{color}_paper_lantern");
    }

    private void addColored(TagKey<Block> group, String pattern) {
        String prefix = group.location().getPath().toUpperCase(Locale.ENGLISH) + '_';
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation key = ResourceLocation.fromNamespaceAndPath(PantzMod.MOD_ID, pattern.replace("{color}", color.getName()));
            TagKey<Block> tag = getForgeBlockTag(prefix + color.getName());
            Block block = BuiltInRegistries.BLOCK.get(key);
            if (block == Blocks.AIR)
                throw new IllegalStateException("Unknown item: " + key);
            tag(tag).add(block);
        }
    }

    @SuppressWarnings("unchecked")
    private TagKey<Block> getForgeBlockTag(String name) {
        try {
            name = name.toUpperCase(Locale.ENGLISH);
            return (TagKey<Block>) Tags.Blocks.class.getDeclaredField(name).get(null);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new IllegalStateException(Tags.Blocks.class.getName() + " is missing tag name: " + name);
        }
    }
}
