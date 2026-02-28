package pantz.mod.core.data.server;

import com.google.common.collect.Maps;
import com.teamabnormals.blueprint.core.api.conditions.ConfigValueCondition;
import com.teamabnormals.blueprint.core.data.server.BlueprintRecipeProvider;
import com.teamabnormals.blueprint.core.other.tags.BlueprintItemTags;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.PMBlockFamilies;
import pantz.mod.core.other.PMConstant;
import pantz.mod.core.other.tags.PMItemTags;
import pantz.mod.core.registry.PMBlocks;

import java.util.List;
import java.util.function.Consumer;

import static net.minecraft.data.recipes.RecipeCategory.*;
import static pantz.mod.core.PMConfig.Common.COMMON;
import static pantz.mod.core.PantzMod.location;
import static pantz.mod.core.registry.PMBlocks.*;
import static pantz.mod.core.registry.PMItems.*;

public class PMRecipeProvider extends BlueprintRecipeProvider {
    private static final ModLoadedCondition CAVERNS_AND_CHASMS = new ModLoadedCondition(PMConstant.CAVERNS_AND_CHASMS);
    private static final NotCondition NOT_CAVERNS_AND_CHASMS = new NotCondition(CAVERNS_AND_CHASMS);
    private static final ConfigValueCondition FLINT_AND_STEEL = config(COMMON.flintAndSteel, "flint_and_steel");
    private static final ConfigValueCondition ENTITY_FILTERING = config(COMMON.enableEntityFilter, "entity_filter");
    private static final ConfigValueCondition REQUIRE_CACTUS_KEY = config(COMMON.enableCactusKey, "cactus_key");

    public PMRecipeProvider(PackOutput output) {
        super(PantzMod.MOD_ID, output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ///   ////////////////////////////////////////////////////////////////////////////
        ShapedRecipeBuilder.shaped(MISC, STEEL_INGOT.get())
                .define('I', Tags.Items.INGOTS_IRON).define('C', PMItemTags.COALS)
                .pattern(" I ")
                .pattern("ICI")
                .pattern(" I ")
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(consumer);

        storageRecipesWithCustomPacking(consumer, MISC, STEEL_NUGGET.get(), MISC, STEEL_INGOT.get(), "steel_ingot_from_nuggets", "steel_ingot");
        storageRecipesWithCustomUnpacking(consumer, MISC, STEEL_INGOT.get(), BUILDING_BLOCKS, STEEL_BLOCK.get(), "steel_ingot_from_steel_block", "steel_ingot");

        doorBuilder(STEEL_DOOR.get(), Ingredient.of(PMItemTags.INGOTS_STEEL)).unlockedBy(getHasName(STEEL_INGOT.get()), has(PMItemTags.INGOTS_STEEL)).save(consumer);
        trapdoorBuilder(STEEL_TRAPDOOR.get(), Ingredient.of(PMItemTags.INGOTS_STEEL)).unlockedBy(getHasName(STEEL_INGOT.get()), has(PMItemTags.INGOTS_STEEL)).save(consumer);

        conditionalRecipe(consumer, CAVERNS_AND_CHASMS, DECORATIONS,
                ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, STEEL_BARS.get(), 16)
                        .define('#', PMItemTags.INGOTS_STEEL)
                        .pattern("###")
                        .pattern("###")
                        .unlockedBy(getHasName(STEEL_INGOT.get()), has(PMItemTags.INGOTS_STEEL)));

        ShapedRecipeBuilder.shaped(DECORATIONS, STEEL_LANTERN.get())
                .define('*', PMItemTags.NUGGETS_STEEL).define('#', Items.BLAZE_POWDER)
                .pattern("***")
                .pattern("*#*")
                .pattern("***")
                .unlockedBy(getHasName(STEEL_INGOT.get()), has(PMItemTags.NUGGETS_STEEL))
                .save(consumer);

        conditionalRecipe(consumer, FLINT_AND_STEEL, TOOLS,
                ShapelessRecipeBuilder.shapeless(TOOLS, Items.FLINT_AND_STEEL)
                        .requires(PMItemTags.INGOTS_STEEL)
                        .requires(Items.FLINT)
                        .unlockedBy(getHasName(Items.FLINT), has(Items.FLINT))
                        .unlockedBy(getHasName(Items.OBSIDIAN), has(Tags.Items.OBSIDIAN)));

        toolsAndArmor(consumer, STEEL_SWORD.get(), STEEL_SHOVEL.get(), STEEL_PICKAXE.get(), STEEL_AXE.get(), STEEL_HOE.get(), STEEL_HELMET.get(), STEEL_CHESTPLATE.get(), STEEL_LEGGINGS.get(), STEEL_BOOTS.get(), STEEL_INGOT.get(), PMItemTags.INGOTS_STEEL);
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(STEEL_PICKAXE.get(), STEEL_SHOVEL.get(), STEEL_AXE.get(), STEEL_HOE.get(), STEEL_SWORD.get(), STEEL_HELMET.get(), STEEL_CHESTPLATE.get(), STEEL_LEGGINGS.get(), STEEL_BOOTS.get(), STEEL_HORSE_ARMOR.get()), RecipeCategory.MISC, STEEL_NUGGET.get(), 0.1F, 200).unlockedBy("has_steel_pickaxe", has(STEEL_PICKAXE.get())).unlockedBy("has_steel_shovel", has(STEEL_SHOVEL.get())).unlockedBy("has_steel_axe", has(STEEL_AXE.get())).unlockedBy("has_steel_hoe", has(STEEL_HOE.get())).unlockedBy("has_steel_sword", has(STEEL_SWORD.get())).unlockedBy("has_steel_helmet", has(STEEL_HELMET.get())).unlockedBy("has_steel_chestplate", has(STEEL_CHESTPLATE.get())).unlockedBy("has_steel_leggings", has(STEEL_LEGGINGS.get())).unlockedBy("has_steel_boots", has(STEEL_BOOTS.get())).unlockedBy("has_steel_horse_armor", has(STEEL_HORSE_ARMOR.get())).save(consumer, location(getSmeltingRecipeName(STEEL_NUGGET.get())));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(STEEL_PICKAXE.get(), STEEL_SHOVEL.get(), STEEL_AXE.get(), STEEL_HOE.get(), STEEL_SWORD.get(), STEEL_HELMET.get(), STEEL_CHESTPLATE.get(), STEEL_LEGGINGS.get(), STEEL_BOOTS.get(), STEEL_HORSE_ARMOR.get()), RecipeCategory.MISC, STEEL_NUGGET.get(), 0.1F, 100).unlockedBy("has_steel_pickaxe", has(STEEL_PICKAXE.get())).unlockedBy("has_steel_shovel", has(STEEL_SHOVEL.get())).unlockedBy("has_steel_axe", has(STEEL_AXE.get())).unlockedBy("has_steel_hoe", has(STEEL_HOE.get())).unlockedBy("has_steel_sword", has(STEEL_SWORD.get())).unlockedBy("has_steel_helmet", has(STEEL_HELMET.get())).unlockedBy("has_steel_chestplate", has(STEEL_CHESTPLATE.get())).unlockedBy("has_steel_leggings", has(STEEL_LEGGINGS.get())).unlockedBy("has_steel_boots", has(STEEL_BOOTS.get())).unlockedBy("has_steel_horse_armor", has(STEEL_HORSE_ARMOR.get())).save(consumer, location(getBlastingRecipeName(STEEL_NUGGET.get())));
        ///   ////////////////////////////////////////////////////////////////////////////

        ///   ////////////////////////////////////////////////////////////////////////////
        polished(consumer, BUILDING_BLOCKS, SULFUR_BRICKS.get(), SULFUR_BLOCK.get());
        storageRecipesWithCustomUnpacking(consumer, MISC, SULFUR_SHARD.get(), BUILDING_BLOCKS, SULFUR_BLOCK.get(), "sulfur_shard_from_sulfur_block", "sulfur_crystal");

        ShapelessRecipeBuilder.shapeless(MISC, Items.GUNPOWDER, 2)
                .requires(Ingredient.of(PMItemTags.DUSTS_SULFUR), 2).requires(PMItemTags.COALS)
                .unlockedBy(getHasName(SULFUR_SHARD.get()), has(PMItemTags.DUSTS_SULFUR)).save(consumer);

        lampRecipe(consumer, SULFUR_LAMP.get(), PMItemTags.GEMS_SULFUR);
        generateRecipes(consumer, PMBlockFamilies.SULFUR_BRICKS_FAMILY);

        stonecutterRecipe(consumer, BUILDING_BLOCKS, SULFUR_BRICKS.get(), SULFUR_BLOCK.get());
        stonecutterRecipe(consumer, BUILDING_BLOCKS, SULFUR_BRICK_STAIRS.get(), SULFUR_BLOCK.get());
        stonecutterRecipe(consumer, BUILDING_BLOCKS, SULFUR_BRICK_SLAB.get(), SULFUR_BLOCK.get(), 2);
        stonecutterRecipe(consumer, BUILDING_BLOCKS, SULFUR_BRICK_WALL.get(), SULFUR_BLOCK.get());
        stonecutterRecipe(consumer, BUILDING_BLOCKS, CHISELED_SULFUR_BRICKS.get(), SULFUR_BLOCK.get());
        stonecutterRecipe(consumer, BUILDING_BLOCKS, SULFUR_BRICK_STAIRS.get(), SULFUR_BRICKS.get());
        stonecutterRecipe(consumer, BUILDING_BLOCKS, SULFUR_BRICK_SLAB.get(), SULFUR_BRICKS.get(), 2);
        stonecutterRecipe(consumer, BUILDING_BLOCKS, SULFUR_BRICK_WALL.get(), SULFUR_BRICKS.get());
        stonecutterRecipe(consumer, BUILDING_BLOCKS, CHISELED_SULFUR_BRICKS.get(), SULFUR_BRICKS.get());

        conversionRecipe(consumer, SULFUR_DUST.get(), SULFUR_SHARD.get(), null, 2);
        oreRecipes(consumer, MISC, NETHER_SULFUR_ORE.get(), SULFUR_DUST.get(), 0.2f, 200);
        ///   ////////////////////////////////////////////////////////////////////////////

        ///   ////////////////////////////////////////////////////////////////////////////
        ShapedRecipeBuilder.shaped(TOOLS, TROWEL.get())
                .define('#', PMItemTags.INGOTS_STEEL).define('/', Tags.Items.RODS_WOODEN)
                .pattern(" #")
                .pattern("/ ")
                .unlockedBy(getHasName(STEEL_INGOT.get()), has(PMItemTags.INGOTS_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(TOOLS, HONEY_DESERIALIZER.get())
                .define('#', Tags.Items.INGOTS_COPPER).define('@', Items.HONEYCOMB)
                .pattern(" # ")
                .pattern("#@#")
                .pattern(" # ")
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Tags.Items.INGOTS_COPPER))
                .save(consumer);
        ///   ////////////////////////////////////////////////////////////////////////////

        ///   ////////////////////////////////////////////////////////////////////////////
        excavator(consumer, EXCAVATOR.get(), STEEL_INGOT.get(), PMItemTags.INGOTS_STEEL);
        excavator(consumer, DIAMOND_EXCAVATOR.get(), Items.DIAMOND, Tags.Items.GEMS_DIAMOND);
        netheriteSmithingRecipe(consumer, DIAMOND_EXCAVATOR.get(), TOOLS, NETHERITE_EXCAVATOR.get());

        hammer(consumer, HAMMER.get(), STEEL_INGOT.get(), PMItemTags.INGOTS_STEEL);
        hammer(consumer, DIAMOND_HAMMER.get(), Items.DIAMOND, Tags.Items.GEMS_DIAMOND);
        netheriteSmithingRecipe(consumer, DIAMOND_HAMMER.get(), TOOLS, NETHERITE_HAMMER.get());
        ///   ////////////////////////////////////////////////////////////////////////////

        ///   ////////////////////////////////////////////////////////////////////////////
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ENDER_SCANNER.get())
                .define('O', Tags.Items.OBSIDIAN).define('R', Tags.Items.DUSTS_REDSTONE).define('E', Items.ENDER_EYE)
                .pattern("ORO")
                .pattern("RER")
                .pattern("ORO")
                .unlockedBy(getHasName(Items.ENDER_EYE), has(Items.ENDER_EYE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, REDSTONE_CONFIGURATOR.get())
                .define('#', Tags.Items.STORAGE_BLOCKS_REDSTONE).define('S', PMItemTags.INGOTS_STEEL)
                .pattern("SSS")
                .pattern("S#S")
                .pattern("SSS")
                .unlockedBy(getHasName(Blocks.REDSTONE_BLOCK), has(Tags.Items.STORAGE_BLOCKS_REDSTONE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, WEATHER_DETECTOR.get())
                .define('_', Blocks.SMOOTH_STONE_SLAB).define('#', Tags.Items.INGOTS_COPPER)
                .define('G', Tags.Items.GLASS_COLORLESS)
                .pattern("GGG")
                .pattern("###")
                .pattern("___")
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Tags.Items.INGOTS_COPPER))
                .save(consumer);

        conditionalRecipe(consumer, ENTITY_FILTERING, TOOLS,
                ShapedRecipeBuilder.shaped(TOOLS, ENTITY_FILTER.get())
                        .define('#', Items.PAPER).define('$', Items.ROTTEN_FLESH)
                        .define('&', Items.STRING).define('*', Items.SPIDER_EYE)
                        .define('/', Items.BONE)
                        .pattern(" $ ")
                        .pattern("&#*")
                        .pattern(" / ")
                        .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER)));

        conditionalRecipe(consumer, NOT_CAVERNS_AND_CHASMS, REDSTONE,
                ShapedRecipeBuilder.shaped(REDSTONE, ENTITY_DETECTOR.get())
                        .define('_', Tags.Items.INGOTS_IRON).define('7', Tags.Items.GEMS_LAPIS)
                        .define('#', Blocks.GLASS)
                        .pattern("###")
                        .pattern("777")
                        .pattern("___")
                        .unlockedBy(getHasName(Items.LAPIS_LAZULI), has(Tags.Items.GEMS_LAPIS)));

        conditionalRecipe(consumer, CAVERNS_AND_CHASMS, REDSTONE,
                ShapedRecipeBuilder.shaped(REDSTONE, ENTITY_DETECTOR.get())
                        .define('_', PMItemTags.INGOTS_SILVER).define('7', Tags.Items.GEMS_LAPIS)
                        .define('#', Blocks.GLASS)
                        .pattern("###")
                        .pattern("777")
                        .pattern("___")
                        .unlockedBy(getHasName(Items.LAPIS_LAZULI), has(Tags.Items.GEMS_LAPIS)),
                location("entity_detector_from_silver_ingots"));

        ShapedRecipeBuilder.shaped(REDSTONE, POWER_DISPLAYER.get())
                .define('T', Blocks.TUFF).define('/', Tags.Items.GEMS_AMETHYST)
                .pattern("TTT")
                .pattern("///")
                .pattern("TTT")
                .unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Tags.Items.GEMS_AMETHYST))
                .save(consumer);

        logicGates(consumer);

        List<Item> dyes = List.of(Items.BLACK_DYE, Items.BLUE_DYE, Items.BROWN_DYE, Items.CYAN_DYE, Items.GRAY_DYE, Items.GREEN_DYE,
                Items.LIGHT_BLUE_DYE, Items.LIGHT_GRAY_DYE, Items.LIME_DYE, Items.MAGENTA_DYE, Items.ORANGE_DYE,
                Items.PINK_DYE, Items.PURPLE_DYE, Items.RED_DYE, Items.YELLOW_DYE, Items.WHITE_DYE);

        List<Item> lamps = List.of(BLACK_REDSTONE_LAMP.get().asItem(), BLUE_REDSTONE_LAMP.get().asItem(), BROWN_REDSTONE_LAMP.get().asItem(), CYAN_REDSTONE_LAMP.get().asItem(), GRAY_REDSTONE_LAMP.get().asItem(), GREEN_REDSTONE_LAMP.get().asItem(),
                LIGHT_BLUE_REDSTONE_LAMP.get().asItem(), LIGHT_GRAY_REDSTONE_LAMP.get().asItem(), LIME_REDSTONE_LAMP.get().asItem(), MAGENTA_REDSTONE_LAMP.get().asItem(), ORANGE_REDSTONE_LAMP.get().asItem(),
                PINK_REDSTONE_LAMP.get().asItem(), PURPLE_REDSTONE_LAMP.get().asItem(), RED_REDSTONE_LAMP.get().asItem(), YELLOW_REDSTONE_LAMP.get().asItem(), WHITE_REDSTONE_LAMP.get().asItem(), Items.REDSTONE_LAMP);

        colorBlockWithDye(consumer, dyes, lamps, REDSTONE, "redstone_lamps");
        ///   ////////////////////////////////////////////////////////////////////////////

        ///   ////////////////////////////////////////////////////////////////////////////
        pedestalBuilder(STONE_PEDESTAL.get(), Blocks.STONE, Blocks.STONE_SLAB).save(consumer);
        pedestalBuilder(DEEPSLATE_PEDESTAL.get(), Blocks.POLISHED_DEEPSLATE, Blocks.POLISHED_DEEPSLATE_SLAB).save(consumer);
        pedestalBuilder(BLACKSTONE_PEDESTAL.get(), Blocks.POLISHED_BLACKSTONE, Blocks.POLISHED_BLACKSTONE_SLAB).save(consumer);
        pedestalBuilder(QUARTZ_PEDESTAL.get(), Ingredient.of(Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_PILLAR), Ingredient.of(Blocks.QUARTZ_SLAB)).unlockedBy(getHasName(Blocks.QUARTZ_PILLAR), has(Blocks.QUARTZ_PILLAR)).unlockedBy(getHasName(Blocks.QUARTZ_BLOCK), has(Blocks.QUARTZ_BLOCK)).save(consumer);
        pedestalBuilder(PRISMARINE_PEDESTAL.get(), Blocks.PRISMARINE, Blocks.PRISMARINE_SLAB).save(consumer);
        pedestalBuilder(PURPUR_PEDESTAL.get(), Blocks.PURPUR_BLOCK, Blocks.PURPUR_SLAB).save(consumer);

        ShapedRecipeBuilder.shaped(DECORATIONS, ITEM_STAND.get())
                .define('#', Blocks.SMOOTH_STONE_SLAB)
                .define('/', Items.STICK)
                .pattern("/")
                .pattern("#")
                .unlockedBy(getHasName(Blocks.SMOOTH_STONE_SLAB), has(Blocks.SMOOTH_STONE_SLAB))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(DECORATIONS, GLOW_ITEM_STAND.get())
                .requires(ITEM_STAND.get())
                .requires(Items.GLOW_INK_SAC)
                .unlockedBy(getHasName(ITEM_STAND.get()), has(ITEM_STAND.get()))
                .unlockedBy(getHasName(Items.GLOW_INK_SAC), has(Items.GLOW_INK_SAC))
                .save(consumer);

        ShapedRecipeBuilder.shaped(DECORATIONS, TRASH_CAN.get())
                .define('I', PMItemTags.INGOTS_STEEL).define('#', BlueprintItemTags.WOODEN_CHESTS)
                .define('$', Blocks.CACTUS)
                .pattern("I#I")
                .pattern("I$I")
                .pattern("III")
                .unlockedBy(getHasName(Blocks.CACTUS), has(Blocks.CACTUS))
                .unlockedBy(getHasName(Blocks.CHEST), has(BlueprintItemTags.WOODEN_CHESTS))
                .save(consumer);

        conditionalRecipe(consumer, REQUIRE_CACTUS_KEY, TOOLS,
                ShapedRecipeBuilder.shaped(TOOLS, CACTUS_KEY.get())
                        .define('#', Tags.Items.INGOTS_IRON).define('*', Tags.Items.NUGGETS_IRON)
                        .define('$', Blocks.CACTUS)
                        .pattern(" ##")
                        .pattern(" $#")
                        .pattern("*  ")
                        .unlockedBy(getHasName(Blocks.CACTUS), has(Blocks.CACTUS)));

        ShapedRecipeBuilder.shaped(TRANSPORTATION, ENDERPORTER.get())
                .define('#', PMItemTags.INGOTS_STEEL).define('/', Items.ECHO_SHARD)
                .pattern("###")
                .pattern("///")
                .pattern("###")
                .unlockedBy(getHasName(Items.ECHO_SHARD), has(Items.ECHO_SHARD))
                .save(consumer);

        ShapedRecipeBuilder.shaped(DECORATIONS, ROPE_LADDER.get())
                .define('/', Tags.Items.RODS_WOODEN).define('%', Tags.Items.STRING)
                .pattern("% %")
                .pattern("///")
                .pattern("% %")
                .unlockedBy(getHasName(Items.STRING), has(Tags.Items.STRING))
                .save(consumer);

        mineralGlassBuilder(QUARTZ_GLASS.get(), Tags.Items.GEMS_QUARTZ, BUILDING_BLOCKS).unlockedBy(getHasName(Items.QUARTZ), has(Tags.Items.GEMS_QUARTZ)).save(consumer);
        mineralGlassBuilder(LAPIS_GLASS.get(), Tags.Items.GEMS_LAPIS, BUILDING_BLOCKS).unlockedBy(getHasName(Items.LAPIS_LAZULI), has(Tags.Items.GEMS_LAPIS)).save(consumer);
        mineralGlassBuilder(REDSTONE_GLASS.get(), Tags.Items.DUSTS_REDSTONE, BUILDING_BLOCKS).unlockedBy(getHasName(Items.REDSTONE), has(Tags.Items.DUSTS_REDSTONE)).save(consumer);
        glassPaneBuilder(QUARTZ_GLASS_PANE.get(), QUARTZ_GLASS.get(), DECORATIONS).unlockedBy(getHasName(QUARTZ_GLASS.get()), has(PMItemTags.GLASS_QUARTZ)).save(consumer);
        glassPaneBuilder(LAPIS_GLASS_PANE.get(), LAPIS_GLASS.get(), DECORATIONS).unlockedBy(getHasName(LAPIS_GLASS.get()), has(PMItemTags.GLASS_LAPIS)).save(consumer);
        glassPaneBuilder(REDSTONE_GLASS_PANE.get(), REDSTONE_GLASS.get(), DECORATIONS).unlockedBy(getHasName(REDSTONE_GLASS.get()), has(PMItemTags.GLASS_REDSTONE)).save(consumer);
        ///   ////////////////////////////////////////////////////////////////////////////

        ///   ////////////////////////////////////////////////////////////////////////////
        ShapelessRecipeBuilder.shapeless(TOOLS, DYNAMITE.get(), 3)
                .requires(Items.PAPER).requires(Ingredient.of(Tags.Items.GUNPOWDER), 2)
                .requires(Tags.Items.DYES_RED)
                .unlockedBy(getHasName(Items.GUNPOWDER), has(Tags.Items.GUNPOWDER))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(COMBAT, COMBAT_DYNAMITE.get(), 3)
                .requires(Items.PAPER).requires(Ingredient.of(Tags.Items.GUNPOWDER), 2)
                .requires(Tags.Items.DYES_PURPLE).requires(Tags.Items.NUGGETS_IRON)
                .unlockedBy(getHasName(Items.GUNPOWDER), has(Tags.Items.GUNPOWDER))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(TOOLS, FIERY_DYNAMITE.get(), 3)
                .requires(Items.PAPER).requires(Ingredient.of(Tags.Items.GUNPOWDER), 3)
                .requires(PMItemTags.DUSTS_SULFUR)
                .unlockedBy(getHasName(Items.GUNPOWDER), has(Tags.Items.GUNPOWDER))
                .save(consumer);
        ///   ////////////////////////////////////////////////////////////////////////////

        ///   ////////////////////////////////////////////////////////////////////////////
        generateRecipes(consumer, PMBlockFamilies.SNOW_BRICKS_FAMILY);
        generateRecipes(consumer, PMBlockFamilies.PACKED_ICE_BRICKS_FAMILY);
        generateRecipes(consumer, PMBlockFamilies.BLUE_ICE_BRICKS_FAMILY);

        polished(consumer, RecipeCategory.BUILDING_BLOCKS, PACKED_ICE_BRICKS.get(), Blocks.PACKED_ICE);
        polished(consumer, RecipeCategory.BUILDING_BLOCKS, BLUE_ICE_BRICKS.get(), Blocks.BLUE_ICE);
        ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, SNOW_BRICKS.get(), 8)
                .define('S', Blocks.SNOW_BLOCK).define('#', Blocks.STONE_BRICKS)
                .pattern("###").pattern("#S#").pattern("###")
                .unlockedBy(getHasName(Blocks.SNOW_BLOCK), has(Blocks.SNOW_BLOCK))
                .save(consumer);

        doorBuilder(PACKED_ICE_DOOR.get(), Ingredient.of(Blocks.PACKED_ICE)).unlockedBy(getHasName(Blocks.PACKED_ICE), has(Blocks.PACKED_ICE)).save(consumer);
        trapdoorBuilder(PACKED_ICE_TRAPDOOR.get(), Ingredient.of(Blocks.PACKED_ICE)).unlockedBy(getHasName(Blocks.PACKED_ICE), has(Blocks.PACKED_ICE)).save(consumer);

        doorBuilder(BLUE_ICE_DOOR.get(), Ingredient.of(Blocks.BLUE_ICE)).unlockedBy(getHasName(Blocks.BLUE_ICE), has(Blocks.BLUE_ICE)).save(consumer);
        trapdoorBuilder(BLUE_ICE_TRAPDOOR.get(), Ingredient.of(Blocks.BLUE_ICE)).unlockedBy(getHasName(Blocks.BLUE_ICE), has(Blocks.BLUE_ICE)).save(consumer);

        stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, SNOW_BRICK_STAIRS.get(), SNOW_BRICKS.get());
        stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, SNOW_BRICK_SLAB.get(), SNOW_BRICKS.get(), 2);
        stonecutterRecipe(consumer, RecipeCategory.DECORATIONS, SNOW_BRICK_WALL.get(), SNOW_BRICKS.get());

        stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, PACKED_ICE_BRICKS.get(), Blocks.PACKED_ICE);
        stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, PACKED_ICE_BRICK_STAIRS.get(), Blocks.PACKED_ICE);
        stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, PACKED_ICE_BRICK_SLAB.get(), Blocks.PACKED_ICE, 2);
        stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, PACKED_ICE_BRICK_WALL.get(), Blocks.PACKED_ICE);
        stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, CHISELED_PACKED_ICE_BRICKS.get(), Blocks.PACKED_ICE);

        stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, PACKED_ICE_BRICK_STAIRS.get(), PACKED_ICE_BRICKS.get());
        stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, PACKED_ICE_BRICK_SLAB.get(), PACKED_ICE_BRICKS.get(), 2);
        stonecutterRecipe(consumer, RecipeCategory.DECORATIONS, PACKED_ICE_BRICK_WALL.get(), PACKED_ICE_BRICKS.get());
        stonecutterRecipe(consumer, RecipeCategory.DECORATIONS, CHISELED_PACKED_ICE_BRICKS.get(), PACKED_ICE_BRICKS.get());

        stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, BLUE_ICE_BRICKS.get(), Blocks.BLUE_ICE);
        stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, BLUE_ICE_BRICK_STAIRS.get(), Blocks.BLUE_ICE);
        stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, BLUE_ICE_BRICK_SLAB.get(), Blocks.BLUE_ICE, 2);
        stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, BLUE_ICE_BRICK_WALL.get(), Blocks.BLUE_ICE);
        stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, CHISELED_BLUE_ICE_BRICKS.get(), Blocks.BLUE_ICE);

        stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, BLUE_ICE_BRICK_STAIRS.get(), BLUE_ICE_BRICKS.get());
        stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, BLUE_ICE_BRICK_SLAB.get(), BLUE_ICE_BRICKS.get(), 2);
        stonecutterRecipe(consumer, RecipeCategory.DECORATIONS, BLUE_ICE_BRICK_WALL.get(), BLUE_ICE_BRICKS.get());
        stonecutterRecipe(consumer, RecipeCategory.DECORATIONS, CHISELED_BLUE_ICE_BRICKS.get(), BLUE_ICE_BRICKS.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ICE_LANTERN.get())
                .define('*', Tags.Items.NUGGETS_IRON).define('#', Blocks.BLUE_ICE)
                .pattern("***")
                .pattern("*#*")
                .pattern("***")
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(consumer);
        ///   ////////////////////////////////////////////////////////////////////////////

        ///   ////////////////////////////////////////////////////////////////////////////

        ShapedRecipeBuilder.shaped(DECORATIONS, WHITE_PAPER_LANTERN.get())
                .define('#', Items.PAPER).define('i', Blocks.TORCH)
                .pattern("###")
                .pattern("#i#")
                .pattern("###")
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(consumer);

        List<Item> paperLanterns = List.of(BLACK_PAPER_LANTERN.get().asItem(), BLUE_PAPER_LANTERN.get().asItem(), BROWN_PAPER_LANTERN.get().asItem(), CYAN_PAPER_LANTERN.get().asItem(), GRAY_PAPER_LANTERN.get().asItem(), GREEN_PAPER_LANTERN.get().asItem(),
                LIGHT_BLUE_PAPER_LANTERN.get().asItem(), LIGHT_GRAY_PAPER_LANTERN.get().asItem(), LIME_PAPER_LANTERN.get().asItem(), MAGENTA_PAPER_LANTERN.get().asItem(), ORANGE_PAPER_LANTERN.get().asItem(),
                PINK_PAPER_LANTERN.get().asItem(), PURPLE_PAPER_LANTERN.get().asItem(), RED_PAPER_LANTERN.get().asItem(), YELLOW_PAPER_LANTERN.get().asItem(), WHITE_PAPER_LANTERN.get().asItem());

        colorBlockWithDye(consumer, dyes, paperLanterns, DECORATIONS, "paper_lanterns");

        ShapedRecipeBuilder.shaped(DECORATIONS, ORNAMENT_FIRECRACKERS.get())
                .define('#', Items.PAPER).define('R', Items.RED_DYE)
                .define('S', Tags.Items.STRING)
                .pattern("#S#")
                .pattern("#R#")
                .pattern("#S#")
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(DECORATIONS, ORNAMENT_LUCKY_COINS.get())
                .define('#', Items.PAPER).define('R', Items.RED_DYE)
                .define('S', Tags.Items.STRING).define('G', Tags.Items.INGOTS_GOLD)
                .pattern("#S#")
                .pattern("GRG")
                .pattern("#S#")
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(consumer);
        ///   ////////////////////////////////////////////////////////////////////////////

        ///   ////////////////////////////////////////////////////////////////////////////
        ShapedRecipeBuilder.shaped(REDSTONE, LOCK.get())
                .define('I', PMItemTags.INGOTS_STEEL).define('#', ItemTags.PLANKS)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .pattern("I#I")
                .pattern("#R#")
                .pattern("I#I")
                .unlockedBy(getHasName(Items.REDSTONE), has(Tags.Items.DUSTS_REDSTONE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(REDSTONE, UNIVERSAL_LOCK.get())
                .define('L', LOCK.get()).define('A', Tags.Items.GEMS_AMETHYST)
                .define('Q', Tags.Items.GEMS_QUARTZ)
                .pattern("QAQ")
                .pattern("ALA")
                .pattern("QAQ")
                .unlockedBy(getHasName(LOCK.get()), has(LOCK.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(TOOLS, KEY.get())
                .define('I', Tags.Items.INGOTS_GOLD).define('*', Tags.Items.NUGGETS_GOLD)
                .pattern("*I*")
                .pattern(" * ")
                .pattern(" * ")
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Tags.Items.INGOTS_GOLD))
                .save(consumer);

    }

    private static void logicGates(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, NOT_GATE.get())
                .define('S', Blocks.STONE).define('Q', Tags.Items.GEMS_QUARTZ)
                .define('R', Tags.Items.DUSTS_REDSTONE).define('T', Items.REDSTONE_TORCH)
                .pattern("QRT")
                .pattern("SSS")
                .unlockedBy(getHasName(Items.REDSTONE_TORCH), has(Items.REDSTONE_TORCH))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, AND_GATE.get())
                .define('S', Blocks.STONE)
                .define('R', Tags.Items.DUSTS_REDSTONE).define('i', Items.REDSTONE_TORCH)
                .define('C', Tags.Items.INGOTS_COPPER)
                .pattern(" C ")
                .pattern("iRi")
                .pattern("SSS")
                .unlockedBy(getHasName(Items.REDSTONE_TORCH), has(Items.REDSTONE_TORCH))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, OR_GATE.get())
                .define('S', Blocks.STONE).define('R', Tags.Items.DUSTS_REDSTONE)
                .define('i', Items.REDSTONE_TORCH).define('C', Tags.Items.GEMS_AMETHYST)
                .pattern(" C ")
                .pattern("RiR")
                .pattern("SSS")
                .unlockedBy(getHasName(Items.REDSTONE_TORCH), has(Items.REDSTONE_TORCH))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, NOR_GATE.get())
                .requires(NOT_GATE.get()).requires(OR_GATE.get())
                .requires(Ingredient.of(Tags.Items.GEMS_AMETHYST), 2)
                .requires(Ingredient.of(Tags.Items.GEMS_QUARTZ), 2)
                .unlockedBy(getHasName(Items.REDSTONE_TORCH), has(Items.REDSTONE_TORCH))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, NAND_GATE.get())
                .requires(NOT_GATE.get()).requires(AND_GATE.get())
                .requires(Ingredient.of(Tags.Items.INGOTS_COPPER), 2)
                .requires(Ingredient.of(Tags.Items.GEMS_QUARTZ), 2)
                .unlockedBy(getHasName(Items.REDSTONE_TORCH), has(Items.REDSTONE_TORCH))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, XNOR_GATE.get())
                .requires(NOR_GATE.get()).requires(Tags.Items.INGOTS_IRON)
                .requires(Tags.Items.GEMS_AMETHYST)
                .requires(Tags.Items.GEMS_QUARTZ)
                .unlockedBy(getHasName(Items.REDSTONE_TORCH), has(Items.REDSTONE_TORCH))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, XOR_GATE.get())
                .requires(OR_GATE.get()).requires(Tags.Items.INGOTS_IRON)
                .requires(Tags.Items.GEMS_AMETHYST)
                .unlockedBy(getHasName(Items.REDSTONE_TORCH), has(Items.REDSTONE_TORCH))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, MAJORITY_GATE.get())
                .requires(OR_GATE.get()).requires(AND_GATE.get())
                .requires(PMItemTags.INGOTS_STEEL)
                .requires(Tags.Items.GEMS_AMETHYST)
                .requires(Tags.Items.INGOTS_COPPER)
                .unlockedBy(getHasName(Items.REDSTONE_TORCH), has(Items.REDSTONE_TORCH))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, MINORITY_GATE.get())
                .requires(NOT_GATE.get()).requires(AND_GATE.get())
                .requires(PMItemTags.INGOTS_STEEL)
                .requires(Tags.Items.GEMS_QUARTZ)
                .requires(Tags.Items.INGOTS_COPPER)
                .unlockedBy(getHasName(Items.REDSTONE_TORCH), has(Items.REDSTONE_TORCH))
                .save(consumer);

        advancedLogicGate(AND_GATE.get(), ADVANCED_AND_GATE.get()).save(consumer);
        advancedLogicGate(NAND_GATE.get(), ADVANCED_NAND_GATE.get()).save(consumer);
        advancedLogicGate(OR_GATE.get(), ADVANCED_OR_GATE.get()).save(consumer);
        advancedLogicGate(NOR_GATE.get(), ADVANCED_NOR_GATE.get()).save(consumer);
        advancedLogicGate(XOR_GATE.get(), ADVANCED_XOR_GATE.get()).save(consumer);
        advancedLogicGate(XNOR_GATE.get(), ADVANCED_XNOR_GATE.get()).save(consumer);

    }

    public static void lampRecipe(Consumer<FinishedRecipe> consumer, ItemLike output, TagKey<Item> input) {
        ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, output).define('#', input).define('G', Blocks.GLOWSTONE).pattern(" # ").pattern("#G#").pattern(" # ").unlockedBy("has_glowstone", has(Blocks.GLOWSTONE)).save(consumer);
    }

    private static RecipeBuilder advancedLogicGate(ItemLike gate, ItemLike advGate) {
        return ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, advGate)
                .requires(gate).requires(Tags.Items.DUSTS_REDSTONE)
                .requires(Items.REDSTONE_TORCH).requires(Ingredient.of(PMItemTags.INGOTS_STEEL), 2)
                .unlockedBy(getHasName(Items.REDSTONE_TORCH), has(Items.REDSTONE_TORCH));
    }

    private void oreRecipes(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike input, ItemLike output, float experience, int time) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), category, output, experience, time).unlockedBy(getHasName(input), has(input)).save(consumer, new ResourceLocation(this.getModID(), getItemName(output) + "_from_smelting_" + getItemName(input)));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(input), category, output, experience, time).unlockedBy(getHasName(input), has(input)).save(consumer, new ResourceLocation(this.getModID(), getItemName(output) + "_from_blasting_" + getItemName(input)));
    }

    public static void hammer(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike item, TagKey<Item> material) {
        hammerBuilder(TOOLS, result, Ingredient.of(material), 1).unlockedBy(getHasName(item), has(material)).save(consumer);
    }

    public static RecipeBuilder hammerBuilder(RecipeCategory category, ItemLike result, Ingredient material, int count) {
        return ShapedRecipeBuilder.shaped(category, result, count).define('#', material).define('/', Tags.Items.RODS_WOODEN).pattern(" # ").pattern(" /#").pattern("/  ");
    }

    public static void excavator(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike item, TagKey<Item> material) {
        excavatorBuilder(TOOLS, result, Ingredient.of(material), 1).unlockedBy(getHasName(item), has(material)).save(consumer);
    }

    public static RecipeBuilder excavatorBuilder(RecipeCategory category, ItemLike result, Ingredient material, int count) {
        return ShapedRecipeBuilder.shaped(category, result, count).define('#', material).define('/', Tags.Items.RODS_WOODEN).pattern(" ##").pattern(" /#").pattern("/  ");
    }

    private static RecipeBuilder glassPaneBuilder(ItemLike glassPane, ItemLike glass, RecipeCategory category) {
        return ShapedRecipeBuilder.shaped(category, glassPane, 16).define('#', glass)
                .pattern("###").pattern("###");
    }

    private static RecipeBuilder mineralGlassBuilder(ItemLike glass, TagKey<Item> ingredient, RecipeCategory category) {
        return ShapedRecipeBuilder.shaped(category, glass, 8).define('#', Tags.Items.GLASS_COLORLESS).define('@', ingredient)
                .pattern("###").pattern("#@#").pattern("###");
    }

    private static RecipeBuilder pedestalBuilder(ItemLike pedestal, ItemLike block, ItemLike slab) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, pedestal)
                .define('S', slab).define('B', block)
                .pattern("SSS")
                .pattern(" B ")
                .pattern(" S ")
                .unlockedBy(getHasName(block), has(block)).unlockedBy(getHasName(slab), has(slab))
                .group("pedestal");
    }

    private static RecipeBuilder pedestalBuilder(ItemLike pedestal, Ingredient block, Ingredient slab) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, pedestal)
                .define('S', slab).define('B', block)
                .pattern("SSS")
                .pattern(" B ")
                .pattern(" S ")
                .group("pedestal");
    }

    private static void toolsAndArmor(Consumer<FinishedRecipe> consumer, ItemLike sword, ItemLike shovel, ItemLike pickaxe, ItemLike axe, ItemLike hoe, ItemLike helmet, ItemLike chestplate, ItemLike leggings, ItemLike boots, ItemLike ingot, TagKey<Item> ingotTag) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, sword)
                .define('/', Tags.Items.RODS_WOODEN).define('#', ingot)
                .pattern("#")
                .pattern("#")
                .pattern("/")
                .unlockedBy(getHasName(ingot), has(ingotTag))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, shovel)
                .define('/', Tags.Items.RODS_WOODEN).define('#', ingot)
                .pattern("#")
                .pattern("/")
                .pattern("/")
                .unlockedBy(getHasName(ingot), has(ingotTag))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, pickaxe)
                .define('/', Tags.Items.RODS_WOODEN).define('#', ingot)
                .pattern("###")
                .pattern(" / ")
                .pattern(" / ")
                .unlockedBy(getHasName(ingot), has(ingotTag))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, axe)
                .define('/', Tags.Items.RODS_WOODEN).define('#', ingot)
                .pattern("##")
                .pattern("#/")
                .pattern(" /")
                .unlockedBy(getHasName(ingot), has(ingotTag))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, hoe)
                .define('/', Tags.Items.RODS_WOODEN).define('#', ingot)
                .pattern("##")
                .pattern(" /")
                .pattern(" /")
                .unlockedBy(getHasName(ingot), has(ingotTag))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, helmet)
                .define('#', ingot)
                .pattern("###")
                .pattern("# #")
                .unlockedBy(getHasName(ingot), has(ingotTag))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, chestplate)
                .define('#', ingot)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(ingot), has(ingotTag))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, leggings)
                .define('#', ingot)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(ingot), has(ingotTag))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, boots)
                .define('#', ingot)
                .pattern("# #")
                .pattern("# #")
                .unlockedBy(getHasName(ingot), has(ingotTag))
                .save(consumer);

    }

    protected void colorBlockWithDye(Consumer<FinishedRecipe> consumer, List<Item> dyes, List<Item> dyeableItems, RecipeCategory category, String group) {
        for(int i = 0; i < dyes.size(); ++i) {
            Item item = dyes.get(i);
            Item item1 = dyeableItems.get(i);
            ShapelessRecipeBuilder.shapeless(category, item1).requires(item)
                    .requires(Ingredient.of(dyeableItems.stream().filter(items -> !items.equals(item1)).map(ItemStack::new)))
                    .group(group).unlockedBy("has_needed_dye", has(item)).save(consumer, new ResourceLocation(this.getModID(), "dye_" + getItemName(item1)));
        }
    }

    public static ConfigValueCondition config(ForgeConfigSpec.ConfigValue<?> value, String key, boolean inverted) {
        return new ConfigValueCondition(location("config"), value, key, Maps.newHashMap(), inverted);
    }

    public static ConfigValueCondition config(ForgeConfigSpec.ConfigValue<?> value, String key) {
        return config(value, key, false);
    }
}
