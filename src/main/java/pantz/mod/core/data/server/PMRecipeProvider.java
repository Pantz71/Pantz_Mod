package pantz.mod.core.data.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.teamabnormals.blueprint.core.api.conditions.ConfigValueCondition;
import com.teamabnormals.blueprint.core.data.server.BlueprintRecipeProvider;
import com.teamabnormals.blueprint.core.other.tags.BlueprintItemTags;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.BlockFamily.Variant;
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
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.PMBlockFamilies;
import pantz.mod.core.other.PMConstant;
import pantz.mod.core.other.tags.PMItemTags;
import pantz.mod.core.registry.PMItems;
import pantz.mod.core.registry.PMRecipes.*;

import java.util.List;
import java.util.function.Consumer;

import static net.minecraft.data.recipes.RecipeCategory.*;
import static pantz.mod.core.PMConfig.Common.COMMON;
import static pantz.mod.core.PantzMod.location;
import static pantz.mod.core.registry.PMBlocks.*;
import static pantz.mod.core.registry.PMItems.*;

public class PMRecipeProvider extends BlueprintRecipeProvider {
    private static final ModLoadedCondition CAVERNS_AND_CHASMS = new ModLoadedCondition(PMConstant.CAVERNS_AND_CHASMS);
    private static final ConfigValueCondition FLINT_AND_STEEL = config(COMMON.flintAndSteel, "flint_and_steel");
    private static final ConfigValueCondition ENTITY_FILTERING = config(COMMON.enableEntityFilter, "entity_filter");
    private static final ConfigValueCondition CRAFTABLE_SPONGE = config(COMMON.craftableSponge, "craftable_sponge");

    public static final ImmutableList<ItemLike> SULFUR_SMELTABLES = ImmutableList.of(SULFUR.get(), SULFUR_ORE.get(), DEEPSLATE_SULFUR_ORE.get(), NETHER_SULFUR_ORE.get());

    public PMRecipeProvider(PackOutput output) {
        super(PantzMod.MOD_ID, output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        // --- Dyes ---
        List<Item> dyes = List.of(Items.BLACK_DYE, Items.BLUE_DYE, Items.BROWN_DYE, Items.CYAN_DYE, Items.GRAY_DYE, Items.GREEN_DYE,
                Items.LIGHT_BLUE_DYE, Items.LIGHT_GRAY_DYE, Items.LIME_DYE, Items.MAGENTA_DYE, Items.ORANGE_DYE,
                Items.PINK_DYE, Items.PURPLE_DYE, Items.RED_DYE, Items.YELLOW_DYE, Items.WHITE_DYE);
        // ----------------------

        // --- Recipe Tweaks ---
        conditionalRecipe(consumer, FLINT_AND_STEEL, TOOLS,
                ShapelessRecipeBuilder.shapeless(TOOLS, Items.FLINT_AND_STEEL)
                        .requires(PMItemTags.INGOTS_STEEL)
                        .requires(Items.FLINT)
                        .unlockedBy(getHasName(Items.FLINT), has(Items.FLINT))
                        .unlockedBy(getHasName(Items.OBSIDIAN), has(Tags.Items.OBSIDIAN)),
                PantzMod.location("flint_and_steel"));

        conditionalRecipe(consumer, new NotCondition(FLINT_AND_STEEL), TOOLS,
                ShapelessRecipeBuilder.shapeless(TOOLS, Items.FLINT_AND_STEEL)
                        .requires(Tags.Items.INGOTS_IRON)
                        .requires(Items.FLINT)
                        .unlockedBy(getHasName(Items.FLINT), has(Items.FLINT))
                        .unlockedBy(getHasName(Items.OBSIDIAN), has(Tags.Items.OBSIDIAN)));

        conditionalRecipe(consumer, CRAFTABLE_SPONGE, BUILDING_BLOCKS,
                ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, Blocks.SPONGE)
                        .define('/', Items.BAMBOO).define('#', ItemTags.WOOL).define('$', Items.HONEYCOMB)
                        .pattern(" $ ")
                        .pattern("/#/")
                        .pattern(" / ")
                        .unlockedBy("has_wool", has(ItemTags.WOOL)));

        SpecialRecipeBuilder.special(PMRecipeSerializers.POTTERY_SHERD_DUPLICATION.get()).save(consumer, PantzMod.MOD_ID + ":pottery_sherd_duplication");
        // ----------------------

        // --- Steel ---
        ShapedRecipeBuilder.shaped(MISC, PMItems.STEEL_INGOT.get())
                .define('I', Tags.Items.INGOTS_IRON).define('C', PMItemTags.COALS)
                .pattern(" I ")
                .pattern("ICI")
                .pattern(" I ")
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(consumer);

        storageRecipesWithCustomPacking(consumer, MISC, STEEL_NUGGET.get(), MISC, PMItems.STEEL_INGOT.get(), "steel_ingot_from_nuggets", "steel_ingot");
        storageRecipesWithCustomUnpacking(consumer, MISC, PMItems.STEEL_INGOT.get(), BUILDING_BLOCKS, STEEL_BLOCK.get(), "steel_ingot_from_steel_block", "steel_ingot");

        doorBuilder(STEEL_DOOR.get(), Ingredient.of(PMItemTags.INGOTS_STEEL)).unlockedBy(getHasName(PMItems.STEEL_INGOT.get()), has(PMItemTags.INGOTS_STEEL)).save(consumer);
        trapdoorBuilder(STEEL_TRAPDOOR.get(), Ingredient.of(PMItemTags.INGOTS_STEEL)).unlockedBy(getHasName(PMItems.STEEL_INGOT.get()), has(PMItemTags.INGOTS_STEEL)).save(consumer);

        conditionalRecipe(consumer, CAVERNS_AND_CHASMS, DECORATIONS,
                ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, STEEL_BARS.get(), 16)
                        .define('#', PMItemTags.INGOTS_STEEL)
                        .pattern("###")
                        .pattern("###")
                        .unlockedBy(getHasName(PMItems.STEEL_INGOT.get()), has(PMItemTags.INGOTS_STEEL)));

        ShapedRecipeBuilder.shaped(DECORATIONS, STEEL_CHAIN.get()).define('I', PMItemTags.INGOTS_STEEL).define('n', PMItemTags.NUGGETS_STEEL)
                .pattern("n").pattern("I").pattern("n")
                .unlockedBy(getHasName(PMItems.STEEL_INGOT.get()), has(PMItemTags.INGOTS_STEEL))
                .unlockedBy(getHasName(STEEL_NUGGET.get()), has(PMItemTags.NUGGETS_STEEL)).save(consumer);

        platedBricksRecipe(consumer, STEEL_BRICKS.get(), PMItemTags.INGOTS_STEEL, "steel");
        platedBricksRecipes(consumer, PMBlockFamilies.STEEL_BRICKS_FAMILY);

        ShapedRecipeBuilder.shaped(DECORATIONS, STEEL_LANTERN.get())
                .define('*', PMItemTags.NUGGETS_STEEL).define('#', Items.BLAZE_POWDER)
                .pattern("***")
                .pattern("*#*")
                .pattern("***")
                .unlockedBy(getHasName(PMItems.STEEL_INGOT.get()), has(PMItemTags.NUGGETS_STEEL))
                .save(consumer);

        toolsAndArmor(consumer, STEEL_SWORD.get(), STEEL_SHOVEL.get(), STEEL_PICKAXE.get(), STEEL_AXE.get(), STEEL_HOE.get(), STEEL_HELMET.get(), STEEL_CHESTPLATE.get(), STEEL_LEGGINGS.get(), STEEL_BOOTS.get(), PMItems.STEEL_INGOT.get(), PMItemTags.INGOTS_STEEL);
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(STEEL_PICKAXE.get(), STEEL_SHOVEL.get(), STEEL_AXE.get(), STEEL_HOE.get(), STEEL_SWORD.get(), STEEL_HELMET.get(), STEEL_CHESTPLATE.get(), STEEL_LEGGINGS.get(), STEEL_BOOTS.get(), STEEL_HORSE_ARMOR.get()), RecipeCategory.MISC, STEEL_NUGGET.get(), 0.1F, 200).unlockedBy("has_steel_pickaxe", has(STEEL_PICKAXE.get())).unlockedBy("has_steel_shovel", has(STEEL_SHOVEL.get())).unlockedBy("has_steel_axe", has(STEEL_AXE.get())).unlockedBy("has_steel_hoe", has(STEEL_HOE.get())).unlockedBy("has_steel_sword", has(STEEL_SWORD.get())).unlockedBy("has_steel_helmet", has(STEEL_HELMET.get())).unlockedBy("has_steel_chestplate", has(STEEL_CHESTPLATE.get())).unlockedBy("has_steel_leggings", has(STEEL_LEGGINGS.get())).unlockedBy("has_steel_boots", has(STEEL_BOOTS.get())).unlockedBy("has_steel_horse_armor", has(STEEL_HORSE_ARMOR.get())).save(consumer, location(getSmeltingRecipeName(STEEL_NUGGET.get())));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(STEEL_PICKAXE.get(), STEEL_SHOVEL.get(), STEEL_AXE.get(), STEEL_HOE.get(), STEEL_SWORD.get(), STEEL_HELMET.get(), STEEL_CHESTPLATE.get(), STEEL_LEGGINGS.get(), STEEL_BOOTS.get(), STEEL_HORSE_ARMOR.get()), RecipeCategory.MISC, STEEL_NUGGET.get(), 0.1F, 100).unlockedBy("has_steel_pickaxe", has(STEEL_PICKAXE.get())).unlockedBy("has_steel_shovel", has(STEEL_SHOVEL.get())).unlockedBy("has_steel_axe", has(STEEL_AXE.get())).unlockedBy("has_steel_hoe", has(STEEL_HOE.get())).unlockedBy("has_steel_sword", has(STEEL_SWORD.get())).unlockedBy("has_steel_helmet", has(STEEL_HELMET.get())).unlockedBy("has_steel_chestplate", has(STEEL_CHESTPLATE.get())).unlockedBy("has_steel_leggings", has(STEEL_LEGGINGS.get())).unlockedBy("has_steel_boots", has(STEEL_BOOTS.get())).unlockedBy("has_steel_horse_armor", has(STEEL_HORSE_ARMOR.get())).save(consumer, location(getBlastingRecipeName(STEEL_NUGGET.get())));
        // ----------------------

        // --- Sulfur ---
        polished(consumer, BUILDING_BLOCKS, POLISHED_SULFUR.get(), SULFUR.get());
        polished(consumer, BUILDING_BLOCKS, SULFUR_BRICKS.get(), POLISHED_SULFUR.get());
        storageRecipesWithCustomUnpacking(consumer, MISC, SULFUR_CRYSTAL.get(), BUILDING_BLOCKS, SULFUR_BLOCK.get(), "sulfur_shard_from_sulfur_block", "sulfur_crystal");

        ShapelessRecipeBuilder.shapeless(MISC, Items.GUNPOWDER, 2)
                .requires(Ingredient.of(PMItemTags.DUSTS_SULFUR), 2).requires(PMItemTags.COALS)
                .unlockedBy(getHasName(SULFUR_CRYSTAL.get()), has(PMItemTags.DUSTS_SULFUR)).save(consumer);

        conditionalRecipe(consumer, CAVERNS_AND_CHASMS, BUILDING_BLOCKS, lampRecipe(SULFUR_LAMP.get(), PMItemTags.GEMS_SULFUR));
        generateRecipes(consumer, PMBlockFamilies.POLISHED_SULFUR_FAMILY);
        generateRecipes(consumer, PMBlockFamilies.SULFUR_BRICKS_FAMILY);

        stonecutterRecipes(consumer, PMBlockFamilies.POLISHED_SULFUR_FAMILY, SULFUR.get(), POLISHED_SULFUR.get());
        stonecutterRecipes(consumer, PMBlockFamilies.SULFUR_BRICKS_FAMILY, SULFUR.get(), POLISHED_SULFUR.get(), SULFUR_BRICKS.get());

        conversionRecipe(consumer, SULFUR_DUST.get(), SULFUR_CRYSTAL.get(), null, 2);
        oreRecipes(consumer, SULFUR_SMELTABLES, MISC, SULFUR_CRYSTAL.get(), 0.2f, 200, "sulfur");
        // ----------------------

        // --- Tools & Utilities ---
        ShapedRecipeBuilder.shaped(TOOLS, TROWEL.get())
                .define('#', PMItemTags.INGOTS_STEEL).define('/', Tags.Items.RODS_WOODEN)
                .pattern(" #")
                .pattern("/ ")
                .unlockedBy(getHasName(PMItems.STEEL_INGOT.get()), has(PMItemTags.INGOTS_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(TOOLS, HONEY_DESERIALIZER.get())
                .define('#', Tags.Items.INGOTS_COPPER).define('@', Items.HONEYCOMB)
                .pattern(" # ")
                .pattern("#@#")
                .pattern(" # ")
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Tags.Items.INGOTS_COPPER))
                .save(consumer);

        excavator(consumer, EXCAVATOR.get(), PMItems.STEEL_INGOT.get(), PMItemTags.INGOTS_STEEL);
        excavator(consumer, DIAMOND_EXCAVATOR.get(), Items.DIAMOND, Tags.Items.GEMS_DIAMOND);
        netheriteSmithingRecipe(consumer, DIAMOND_EXCAVATOR.get(), TOOLS, NETHERITE_EXCAVATOR.get());

        hammer(consumer, HAMMER.get(), PMItems.STEEL_INGOT.get(), PMItemTags.INGOTS_STEEL);
        hammer(consumer, DIAMOND_HAMMER.get(), Items.DIAMOND, Tags.Items.GEMS_DIAMOND);
        netheriteSmithingRecipe(consumer, DIAMOND_HAMMER.get(), TOOLS, NETHERITE_HAMMER.get());

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

        ShapedRecipeBuilder.shaped(TOOLS, KEY.get())
                .define('I', Tags.Items.INGOTS_GOLD).define('*', Tags.Items.NUGGETS_GOLD)
                .pattern("*I*")
                .pattern(" * ")
                .pattern(" * ")
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Tags.Items.INGOTS_GOLD))
                .save(consumer);

        ShapedRecipeBuilder.shaped(TOOLS, POTION_SATCHEL.get())
                .define('L', Tags.Items.LEATHER).define('G', Tags.Items.INGOTS_GOLD)
                .define('I', Tags.Items.NUGGETS_IRON)
                .pattern("GIG")
                .pattern("L L")
                .pattern("LLL")
                .unlockedBy(getHasName(Items.LEATHER), has(Tags.Items.LEATHER))
                .save(consumer);
        // ----------------------

        // --- Redstone ---
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
                        .define('#', PMItemTags.INGOTS_STEEL).define('$', Items.ROTTEN_FLESH)
                        .pattern(" # ")
                        .pattern("#$#")
                        .pattern(" # ")
                        .unlockedBy(getHasName(Items.ROTTEN_FLESH), has(Items.ROTTEN_FLESH)));

        conditionalRecipe(consumer, new NotCondition(CAVERNS_AND_CHASMS), REDSTONE,
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

        List<Item> lamps = List.of(BLACK_REDSTONE_LAMP.get().asItem(), BLUE_REDSTONE_LAMP.get().asItem(), BROWN_REDSTONE_LAMP.get().asItem(), CYAN_REDSTONE_LAMP.get().asItem(), GRAY_REDSTONE_LAMP.get().asItem(), GREEN_REDSTONE_LAMP.get().asItem(),
                LIGHT_BLUE_REDSTONE_LAMP.get().asItem(), LIGHT_GRAY_REDSTONE_LAMP.get().asItem(), LIME_REDSTONE_LAMP.get().asItem(), MAGENTA_REDSTONE_LAMP.get().asItem(), ORANGE_REDSTONE_LAMP.get().asItem(),
                PINK_REDSTONE_LAMP.get().asItem(), PURPLE_REDSTONE_LAMP.get().asItem(), RED_REDSTONE_LAMP.get().asItem(), YELLOW_REDSTONE_LAMP.get().asItem(), WHITE_REDSTONE_LAMP.get().asItem(), Items.REDSTONE_LAMP);

        colorBlockWithDye(consumer, dyes, lamps, REDSTONE, "redstone_lamps");

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

        conditionalRecipe(consumer, new NotCondition(CAVERNS_AND_CHASMS), REDSTONE,
                ShapedRecipeBuilder.shaped(REDSTONE, RANDOMIZER.get())
                        .define('S', Blocks.STONE).define('Q', Tags.Items.GEMS_PRISMARINE)
                        .define('T', Items.REDSTONE_TORCH)
                        .pattern("TQT")
                        .pattern("SSS")
                        .unlockedBy(getHasName(Items.REDSTONE_TORCH), has(Items.REDSTONE_TORCH)));

        ShapedRecipeBuilder.shaped(REDSTONE, EQUALIZER.get())
                .define('S', Blocks.STONE).define('E', Tags.Items.GEMS_EMERALD)
                .define('T', Items.REDSTONE_TORCH).define('R', Tags.Items.DUSTS_REDSTONE)
                .pattern("RET")
                .pattern("SSS")
                .unlockedBy(getHasName(Items.REDSTONE_TORCH), has(Items.REDSTONE_TORCH))
                .save(consumer);

        ShapedRecipeBuilder.shaped(REDSTONE, SPIKE.get())
                .define('S', Blocks.COBBLESTONE).define('d', Blocks.POINTED_DRIPSTONE)
                .pattern("ddd")
                .pattern("SSS")
                .unlockedBy(getHasName(Blocks.POINTED_DRIPSTONE), has(Blocks.POINTED_DRIPSTONE))
                .save(consumer);

        // ----------------------

        // --- Functional ---
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

        magicGlassBuilder(CHORUS_GLASS.get(), Blocks.CHORUS_FLOWER, BUILDING_BLOCKS).unlockedBy(getHasName(Blocks.CHORUS_PLANT), has(Blocks.CHORUS_PLANT)).save(consumer);
        magicGlassBuilder(SOUL_GLASS.get(), Blocks.SOUL_SAND, BUILDING_BLOCKS).unlockedBy(getHasName(Blocks.SOUL_SAND), has(Blocks.SOUL_SAND)).save(consumer);
        magicGlassBuilder(ECHO_GLASS.get(), Items.ECHO_SHARD, BUILDING_BLOCKS).unlockedBy(getHasName(Items.ECHO_SHARD), has(Items.ECHO_SHARD)).save(consumer);
        glassPaneBuilder(CHORUS_GLASS_PANE.get(), CHORUS_GLASS.get(), DECORATIONS).unlockedBy(getHasName(CHORUS_GLASS.get()), has(PMItemTags.GLASS_CHORUS)).save(consumer);
        glassPaneBuilder(SOUL_GLASS_PANE.get(), SOUL_GLASS.get(), DECORATIONS).unlockedBy(getHasName(SOUL_GLASS.get()), has(PMItemTags.GLASS_SOUL)).save(consumer);
        glassPaneBuilder(ECHO_GLASS_PANE.get(), ECHO_GLASS.get(), DECORATIONS).unlockedBy(getHasName(ECHO_GLASS.get()), has(PMItemTags.GLASS_ECHO)).save(consumer);

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

        ShapedRecipeBuilder.shaped(DECORATIONS, SAFE.get())
                .define('I', PMItemTags.INGOTS_STEEL).define('O', Tags.Items.OBSIDIAN)
                .pattern("III")
                .pattern("O O")
                .pattern("III")
                .unlockedBy(getHasName(PMItems.STEEL_INGOT.get()), has(PMItemTags.INGOTS_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(DECORATIONS, SPRINKLER.get())
                .define('I', PMItemTags.INGOTS_STEEL).define('W', ItemTags.PLANKS)
                .define('_', ItemTags.WOODEN_SLABS)
                .pattern("IWI")
                .pattern(" W ")
                .pattern("___")
                .unlockedBy(getHasName(PMItems.STEEL_INGOT.get()), has(PMItemTags.INGOTS_STEEL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(DECORATIONS, FEEDING_TROUGH.get())
                .define('_', ItemTags.WOODEN_SLABS)
                .pattern("_ _")
                .pattern("___")
                .unlockedBy("has_wooden_slabs", has(ItemTags.WOODEN_SLABS))
                .save(consumer);
        // ----------------------

        // --- Compressed ---
        storageRecipes(consumer, MISC, Items.LEATHER, BUILDING_BLOCKS, LEATHER_BLOCK.get());
        storageRecipes(consumer, MISC, Items.RABBIT_HIDE, BUILDING_BLOCKS, RABBIT_HIDE_BLOCK.get());
        storageRecipes(consumer, MISC, Items.PHANTOM_MEMBRANE, BUILDING_BLOCKS, PHANTOM_MEMBRANE_BLOCK.get());
        storageRecipes(consumer, MISC, Items.SUGAR_CANE, BUILDING_BLOCKS, SUGAR_CANE_BLOCK.get());
        storageRecipes(consumer, MISC, Items.FEATHER, BUILDING_BLOCKS, FEATHER_BLOCK.get());
        storageRecipes(consumer, MISC, Items.SUGAR, BUILDING_BLOCKS, SUGAR_BLOCK.get());
        storageRecipes(consumer, MISC, Items.BLAZE_POWDER, BUILDING_BLOCKS, FIERY_LAMP.get());

        // ----------------------

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

    public void platedBricksRecipe(Consumer<FinishedRecipe> consumer, ItemLike block, TagKey<Item> ingotTag, String hasName) {
        conditionalRecipe(consumer, CAVERNS_AND_CHASMS, BUILDING_BLOCKS, ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, block, 4).define('#', ingotTag).define('X', Blocks.DEEPSLATE).pattern("#X").pattern("X#").group(getItemName(block)).unlockedBy("has_" + hasName, has(ingotTag)));
    }

    public void platedBricksRecipes(Consumer<FinishedRecipe> consumer, BlockFamily family) {
        generateRecipes(consumer, family);
        conditionalStonecutterRecipes(consumer, family, CAVERNS_AND_CHASMS);
    }

    public void stonecutterRecipes(Consumer<FinishedRecipe> consumer, BlockFamily family) {
        stonecutterRecipes(consumer, family, family.getBaseBlock());
    }

    public void stonecutterRecipes(Consumer<FinishedRecipe> consumer, BlockFamily family, ItemLike... inputs) {
        for (ItemLike input : inputs) {
            if (family.getBaseBlock().asItem() != input.asItem()) {
                stonecutterRecipe(consumer, BUILDING_BLOCKS, family.getBaseBlock(), input);
            }

            if (family.getVariants().containsKey(Variant.STAIRS)) {
                stonecutterRecipe(consumer, BUILDING_BLOCKS, family.get(Variant.STAIRS), input);
            }

            if (family.getVariants().containsKey(Variant.SLAB)) {
                stonecutterRecipe(consumer, BUILDING_BLOCKS, family.get(Variant.SLAB), input, 2);
            }

            if (family.getVariants().containsKey(Variant.WALL)) {
                stonecutterRecipe(consumer, DECORATIONS, family.get(Variant.WALL), input);
            }

            if (family.getVariants().containsKey(Variant.CHISELED)) {
                stonecutterRecipe(consumer, BUILDING_BLOCKS, family.get(Variant.CHISELED), input);
            }
        }
    }

    public void conditionalStonecutterRecipes(Consumer<FinishedRecipe> consumer, BlockFamily family, ICondition condition, ItemLike... inputs) {
        for (ItemLike input : inputs) {
            if (family.getBaseBlock().asItem() != input.asItem()) {
                conditionalRecipe(consumer, condition, BUILDING_BLOCKS, stonecutterRecipe(BUILDING_BLOCKS, family.getBaseBlock(), input), new ResourceLocation(this.getModConversionRecipeName(family.getBaseBlock(), input) + "_stonecutting"));
            }

            if (family.getVariants().containsKey(Variant.STAIRS)) {
                conditionalRecipe(consumer, condition, BUILDING_BLOCKS, stonecutterRecipe(BUILDING_BLOCKS, family.get(Variant.STAIRS), input), new ResourceLocation(this.getModConversionRecipeName(family.get(Variant.STAIRS), input) + "_stonecutting"));
            }

            if (family.getVariants().containsKey(Variant.SLAB)) {
                conditionalRecipe(consumer, condition, BUILDING_BLOCKS, stonecutterRecipe(BUILDING_BLOCKS, family.get(Variant.SLAB), input, 2), new ResourceLocation(this.getModConversionRecipeName(family.get(Variant.SLAB), input) + "_stonecutting"));
            }

            if (family.getVariants().containsKey(Variant.WALL)) {
                conditionalRecipe(consumer, condition, DECORATIONS, stonecutterRecipe(DECORATIONS, family.get(Variant.WALL), input), new ResourceLocation(this.getModConversionRecipeName(family.get(Variant.WALL), input) + "_stonecutting"));
            }

            if (family.getVariants().containsKey(Variant.CHISELED)) {
                conditionalRecipe(consumer, condition, BUILDING_BLOCKS, stonecutterRecipe(DECORATIONS, family.get(Variant.CHISELED), input), new ResourceLocation(this.getModConversionRecipeName(family.get(Variant.CHISELED), input) + "_stonecutting"));
            }
        }
    }

    public static RecipeBuilder stonecutterRecipe(RecipeCategory category, ItemLike output, ItemLike input, int count) {
        return SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), category, output, count).unlockedBy(getHasName(input), has(input));
    }

    public static RecipeBuilder stonecutterRecipe(RecipeCategory category, ItemLike output, ItemLike input) {
        return SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), category, output, 1).unlockedBy(getHasName(input), has(input));
    }

    public static RecipeBuilder lampRecipe(ItemLike output, TagKey<Item> input) {
        return ShapedRecipeBuilder.shaped(BUILDING_BLOCKS, output).define('#', input).define('G', Blocks.GLOWSTONE).pattern(" # ").pattern("#G#").pattern(" # ").unlockedBy("has_glowstone", has(Blocks.GLOWSTONE));
    }

    private static RecipeBuilder advancedLogicGate(ItemLike gate, ItemLike advGate) {
        return ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, advGate)
                .requires(gate).requires(Tags.Items.DUSTS_REDSTONE)
                .requires(Items.REDSTONE_TORCH).requires(Ingredient.of(PMItemTags.INGOTS_STEEL), 2)
                .unlockedBy(getHasName(Items.REDSTONE_TORCH), has(Items.REDSTONE_TORCH));
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

    private static RecipeBuilder magicGlassBuilder(ItemLike glass, ItemLike ingredient, RecipeCategory category) {
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

    public static void conditionalRecipe(Consumer<FinishedRecipe> consumer, ICondition condition, RecipeCategory category, RecipeBuilder recipe, ResourceLocation id) {
        BlueprintRecipeProvider.conditionalRecipe(consumer, condition, category, recipe, id);
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
