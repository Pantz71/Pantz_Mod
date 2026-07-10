package pantz.mod.core.data.server.tags;

import com.teamabnormals.blueprint.core.other.tags.BlueprintBlockTags;
import com.teamabnormals.blueprint.core.other.tags.BlueprintItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.tags.PMBlockTags;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import static pantz.mod.core.other.tags.PMItemTags.*;
import static pantz.mod.core.other.tags.PMItemTags.EXCAVATORS;
import static pantz.mod.core.other.tags.PMItemTags.PLACEABLE_ITEMS;
import static pantz.mod.core.registry.PMItems.*;

public class PMItemTagsProvider extends ItemTagsProvider {
    public PMItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, PantzMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ItemTags.TRIMMABLE_ARMOR).add(STEEL_HELMET.get(), STEEL_CHESTPLATE.get(), STEEL_LEGGINGS.get(), STEEL_BOOTS.get());
        this.tag(ItemTags.HEAD_ARMOR).add(STEEL_HELMET.get());
        this.tag(ItemTags.CHEST_ARMOR).add(STEEL_CHESTPLATE.get());
        this.tag(ItemTags.LEG_ARMOR).add(STEEL_LEGGINGS.get());
        this.tag(ItemTags.FOOT_ARMOR).add(STEEL_BOOTS.get());

        this.tag(ItemTags.SWORDS).add(STEEL_SWORD.get());
        this.tag(ItemTags.SHOVELS).add(STEEL_SHOVEL.get());
        this.tag(ItemTags.PICKAXES).add(STEEL_PICKAXE.get());
        this.tag(ItemTags.AXES).add(STEEL_AXE.get());
        this.tag(ItemTags.HOES).add(STEEL_HOE.get());

        this.tag(Tags.Items.TOOLS).addTag(EXCAVATORS).addTag(HAMMERS).addTag(TOOLS_TROWEL);
        this.tag(EXCAVATORS).add(EXCAVATOR.get(), DIAMOND_EXCAVATOR.get(), NETHERITE_EXCAVATOR.get());
        this.tag(HAMMERS).add(HAMMER.get(), DIAMOND_HAMMER.get(), NETHERITE_HAMMER.get());
        this.tag(TOOLS_TROWEL).add(TROWEL.get());
        this.tag(DYNAMITES).add(DYNAMITE.get(), COMBAT_DYNAMITE.get(), FIERY_DYNAMITE.get());

        this.tag(ItemTags.DURABILITY_ENCHANTABLE).addTag(EXCAVATORS);
        this.tag(ItemTags.MINING_ENCHANTABLE).addTag(EXCAVATORS);
        this.tag(ItemTags.VANISHING_ENCHANTABLE).addTag(EXCAVATORS);

        this.tag(TOOLS).addTag(Tags.Items.TOOLS);
        this.tag(WEAPONS).addTag(ItemTags.SWORDS).addTag(Tags.Items.TOOLS_SPEAR).addTag(Tags.Items.TOOLS_MACE);
        this.tag(ItemTags.DYEABLE).add(POTION_SATCHEL.get());

        this.tag(PLACEABLE_ITEMS).add(STEEL_INGOT.get());
        this.tag(ItemTags.TRIM_MATERIALS).add(STEEL_INGOT.get(), SULFUR_CRYSTAL.get());
        this.tag(ItemTags.BEACON_PAYMENT_ITEMS).add(STEEL_INGOT.get());
        this.tag(COALS).add(Items.COAL, Items.CHARCOAL);

        this.tag(ENDER_SCANNER_IMMUNITIES).add(Items.CARVED_PUMPKIN);
        this.tag(KEYS).add(KEY.get());

        this.tag(Tags.Items.INGOTS).addTag(INGOTS_STEEL);
        this.tag(Tags.Items.NUGGETS).addTag(NUGGETS_STEEL);
        this.tag(Tags.Items.DUSTS).addTag(DUSTS_SULFUR);

        this.tag(DUSTS_SULFUR).add(SULFUR_DUST.get());
        this.tag(GEMS_SULFUR).add(SULFUR_CRYSTAL.get());
        this.tag(INGOTS_STEEL).add(STEEL_INGOT.get());
        this.tag(NUGGETS_STEEL).add(STEEL_NUGGET.get());

        this.copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
        this.copy(PMBlockTags.STORAGE_BLOCKS_STEEL, STORAGE_BLOCKS_STEEL);
        this.copy(PMBlockTags.STORAGE_BLOCKS_SULFUR, STORAGE_BLOCKS_SULFUR);
        this.copy(PMBlockTags.STORAGE_BLOCKS_LEATHER, STORAGE_BLOCKS_LEATHER);
        this.copy(PMBlockTags.STORAGE_BLOCKS_RABBIT_HIDE, STORAGE_BLOCKS_RABBIT_HIDE);
        this.copy(PMBlockTags.STORAGE_BLOCKS_PHANTOM_MEMBRANE, STORAGE_BLOCKS_PHANTOM_MEMBRANE);
        this.copy(PMBlockTags.STORAGE_BLOCKS_FEATHER, STORAGE_BLOCKS_FEATHER);
        this.copy(PMBlockTags.STORAGE_BLOCKS_SUGAR, STORAGE_BLOCKS_SUGAR);
        this.copy(PMBlockTags.STORAGE_BLOCKS_SUGAR_CANE, STORAGE_BLOCKS_SUGAR_CANE);
        this.copy(PMBlockTags.STORAGE_BLOCKS_BLAZE_POWDER, STORAGE_BLOCKS_BLAZE_POWDER);

        this.copy(PMBlockTags.ORES_SULFUR, ORES_SULFUR);

        this.copy(Tags.Blocks.ORE_RATES_SINGULAR, Tags.Items.ORE_RATES_SINGULAR);
        this.copy(Tags.Blocks.ORES_IN_GROUND_NETHERRACK, Tags.Items.ORES_IN_GROUND_NETHERRACK);

        this.copy(BlockTags.SLABS, ItemTags.SLABS);
        this.copy(BlockTags.STAIRS, ItemTags.STAIRS);
        this.copy(BlockTags.WALLS, ItemTags.WALLS);
        this.copy(BlockTags.DOORS, ItemTags.DOORS);
        this.copy(BlockTags.TRAPDOORS, ItemTags.TRAPDOORS);

        this.copy(PMBlockTags.PEDESTALS, PEDESTALS);
        this.copy(PMBlockTags.REDSTONE_LAMPS, REDSTONE_LAMPS);
        this.copy(PMBlockTags.LOGIC_GATES, LOGIC_GATES);
        this.copy(PMBlockTags.PAPER_LANTERNS, PAPER_LANTERNS);

        this.copy(PMBlockTags.GLASS_BLOCKS_CHORUS, GLASS_BLOCKS_CHORUS);
        this.copy(PMBlockTags.GLASS_BLOCKS_SOUL, GLASS_BLOCKS_SOUL);
        this.copy(PMBlockTags.GLASS_PANES_CHORUS, GLASS_PANES_CHORUS);
        this.copy(PMBlockTags.GLASS_PANES_SOUL, GLASS_PANES_SOUL);
        this.copy(PMBlockTags.GLASS_BLOCKS_ECHO, GLASS_BLOCKS_ECHO);
        this.copy(PMBlockTags.GLASS_PANES_ECHO, GLASS_PANES_ECHO);

        this.copy(Tags.Blocks.GLASS_BLOCKS, Tags.Items.GLASS_BLOCKS);
        this.copy(Tags.Blocks.GLASS_PANES, Tags.Items.GLASS_PANES);

        this.copy(BlueprintBlockTags.WOODEN_LADDERS, BlueprintItemTags.WOODEN_LADDERS);

        this.addColored(Tags.Items.DYED,"{color}_redstone_lamp");

    }

    private void addColored(TagKey<Item> group, String pattern) {
        String prefix = group.location().getPath().toUpperCase(Locale.ENGLISH) + '_';
        for (DyeColor color : DyeColor.values()) {
            ResourceLocation key = ResourceLocation.fromNamespaceAndPath(PantzMod.MOD_ID, pattern.replace("{color}", color.getName()));
            TagKey<Item> tag = getForgeItemTag(prefix + color.getName());
            Item item = BuiltInRegistries.ITEM.get(key);
            if (item == Items.AIR)
                throw new IllegalStateException("Unknown vanilla item: " + key);
            tag(tag).add(item);
        }
    }

    @SuppressWarnings("unchecked")
    private TagKey<Item> getForgeItemTag(String name) {
        try {
            name = name.toUpperCase(Locale.ENGLISH);
            return (TagKey<Item>) Tags.Items.class.getDeclaredField(name).get(null);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new IllegalStateException(Tags.Items.class.getName() + " is missing tag name: " + name);
        }
    }
}
