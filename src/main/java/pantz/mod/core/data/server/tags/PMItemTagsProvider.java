package pantz.mod.core.data.server.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.tags.PMBlockTags;

import java.util.concurrent.CompletableFuture;

import static pantz.mod.core.other.tags.PMItemTags.*;
import static pantz.mod.core.registry.PMItems.*;

public class PMItemTagsProvider extends ItemTagsProvider {
    public PMItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> blockTag, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, blockTag, PantzMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ItemTags.TRIMMABLE_ARMOR).add(STEEL_HELMET.get(), STEEL_CHESTPLATE.get(), STEEL_LEGGINGS.get(), STEEL_BOOTS.get());
        this.tag(Tags.Items.ARMORS_HELMETS).add(STEEL_HELMET.get());
        this.tag(Tags.Items.ARMORS_CHESTPLATES).add(STEEL_CHESTPLATE.get());
        this.tag(Tags.Items.ARMORS_LEGGINGS).add(STEEL_LEGGINGS.get());
        this.tag(Tags.Items.ARMORS_BOOTS).add(STEEL_BOOTS.get());

        this.tag(ItemTags.SWORDS).add(STEEL_SWORD.get());
        this.tag(ItemTags.SHOVELS).add(STEEL_SHOVEL.get());
        this.tag(ItemTags.PICKAXES).add(STEEL_PICKAXE.get());
        this.tag(ItemTags.AXES).add(STEEL_AXE.get());
        this.tag(ItemTags.HOES).add(STEEL_HOE.get());

        this.tag(Tags.Items.TOOLS).addTag(EXCAVATORS);
        this.tag(EXCAVATORS).add(EXCAVATOR.get(), DIAMOND_EXCAVATOR.get(), NETHERITE_EXCAVATOR.get());

        this.tag(ItemTags.TRIM_MATERIALS).add(STEEL_INGOT.get(), SULFUR.get());
        this.tag(COALS).add(Items.COAL, Items.CHARCOAL);

        this.tag(Tags.Items.INGOTS).addTag(INGOTS_STEEL);
        this.tag(Tags.Items.NUGGETS).addTag(NUGGETS_STEEL);
        this.tag(Tags.Items.DUSTS).addTag(DUSTS_SULFUR);

        this.tag(DUSTS_SULFUR).add(SULFUR.get());
        this.tag(INGOTS_STEEL).add(STEEL_INGOT.get());
        this.tag(NUGGETS_STEEL).add(STEEL_NUGGET.get());

        this.copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
        this.copy(PMBlockTags.STORAGE_BLOCKS_STEEL, STORAGE_BLOCKS_STEEL);
        this.copy(PMBlockTags.STORAGE_BLOCKS_SULFUR, STORAGE_BLOCKS_SULFUR);

        this.copy(PMBlockTags.ORES_SULFUR, ORES_SULFUR);

        this.copy(Tags.Blocks.ORE_RATES_SINGULAR, Tags.Items.ORE_RATES_SINGULAR);
        this.copy(Tags.Blocks.ORES_IN_GROUND_NETHERRACK, Tags.Items.ORES_IN_GROUND_NETHERRACK);
    }
}
