package pantz.mod.core.data.server;

import com.google.common.collect.Maps;
import com.teamabnormals.blueprint.core.api.conditions.ConfigValueCondition;
import com.teamabnormals.blueprint.core.data.server.BlueprintRecipeProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.PMBlockFamilies;
import pantz.mod.core.other.PMConstant;
import pantz.mod.core.other.tags.PMItemTags;

import java.util.function.Consumer;

import static net.minecraft.data.recipes.RecipeCategory.*;
import static pantz.mod.core.PMConfig.Common.COMMON;
import static pantz.mod.core.registry.PMBlocks.*;
import static pantz.mod.core.registry.PMItems.*;

public class PMRecipeProvider extends BlueprintRecipeProvider {
    private static final ModLoadedCondition CAVERNS_AND_CHASMS = new ModLoadedCondition(PMConstant.CAVERNS_AND_CHASMS);
    private static final ConfigValueCondition FLINT_AND_STEEL = config(COMMON.flintAndSteel, "flint_and_steel");

    public PMRecipeProvider(PackOutput output) {
        super(PantzMod.MOD_ID, output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
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

        conditionalRecipe(consumer, FLINT_AND_STEEL, TOOLS,
                ShapelessRecipeBuilder.shapeless(TOOLS, Items.FLINT_AND_STEEL)
                        .requires(PMItemTags.INGOTS_STEEL)
                        .requires(Items.FLINT)
                        .unlockedBy(getHasName(Items.FLINT), has(Items.FLINT))
                        .unlockedBy(getHasName(Items.OBSIDIAN), has(Tags.Items.OBSIDIAN)));

        toolsAndArmor(consumer, STEEL_SWORD.get(), STEEL_SHOVEL.get(), STEEL_PICKAXE.get(), STEEL_AXE.get(), STEEL_HOE.get(), STEEL_HELMET.get(), STEEL_CHESTPLATE.get(), STEEL_LEGGINGS.get(), STEEL_BOOTS.get(), STEEL_INGOT.get(), PMItemTags.INGOTS_STEEL);

        polishedSingleOutput(consumer, BUILDING_BLOCKS, SULFUR_BLOCK.get(), SULFUR.get());
        polished(consumer, BUILDING_BLOCKS, SULFUR_BRICKS.get(), SULFUR_BLOCK.get());

        ShapelessRecipeBuilder.shapeless(MISC, Items.GUNPOWDER, 2)
                .requires(Ingredient.of(PMItemTags.DUSTS_SULFUR), 2).requires(PMItemTags.COALS)
                .unlockedBy(getHasName(SULFUR.get()), has(PMItemTags.DUSTS_SULFUR)).save(consumer);

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

        oreRecipes(consumer, MISC, NETHER_SULFUR_ORE.get(), SULFUR.get(), 0.2f, 200);

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

        excavator(consumer, EXCAVATOR.get(), STEEL_INGOT.get(), PMItemTags.INGOTS_STEEL);
        excavator(consumer, DIAMOND_EXCAVATOR.get(), Items.DIAMOND, Tags.Items.GEMS_DIAMOND);
        netheriteSmithing(consumer, DIAMOND_EXCAVATOR.get(), TOOLS, NETHERITE_EXCAVATOR.get());

        pedestalBuilder(STONE_PEDESTAL.get(), Blocks.STONE, Blocks.STONE_SLAB).save(consumer);
        pedestalBuilder(DEEPSLATE_PEDESTAL.get(), Blocks.POLISHED_DEEPSLATE, Blocks.POLISHED_DEEPSLATE_SLAB).save(consumer);
        pedestalBuilder(BLACKSTONE_PEDESTAL.get(), Blocks.POLISHED_BLACKSTONE, Blocks.POLISHED_BLACKSTONE_SLAB).save(consumer);
        pedestalBuilder(QUARTZ_PEDESTAL.get(), Ingredient.of(Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_PILLAR), Ingredient.of(Blocks.QUARTZ_SLAB)).unlockedBy(getHasName(Blocks.QUARTZ_PILLAR), has(Blocks.QUARTZ_PILLAR)).unlockedBy(getHasName(Blocks.QUARTZ_BLOCK), has(Blocks.QUARTZ_BLOCK)).save(consumer);
        pedestalBuilder(PRISMARINE_PEDESTAL.get(), Blocks.PRISMARINE, Blocks.PRISMARINE_SLAB).save(consumer);
        pedestalBuilder(PURPUR_PEDESTAL.get(), Blocks.PURPUR_BLOCK, Blocks.PURPUR_SLAB).save(consumer);

    }

    private void oreRecipes(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike input, ItemLike output, float experience, int time) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), category, output, experience, time).unlockedBy(getHasName(input), has(input)).save(consumer, new ResourceLocation(this.getModID(), getItemName(output) + "_from_smelting_" + getItemName(input)));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(input), category, output, experience, time).unlockedBy(getHasName(input), has(input)).save(consumer, new ResourceLocation(this.getModID(), getItemName(output) + "_from_blasting_" + getItemName(input)));
    }


    private static void polishedSingleOutput(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeCategory pCategory, ItemLike pResult, ItemLike pMaterial) {
        polishedBuilder(pCategory, pResult, Ingredient.of(pMaterial), 1).unlockedBy(getHasName(pMaterial), has(pMaterial)).save(pFinishedRecipeConsumer);
    }

    private static RecipeBuilder polishedBuilder(RecipeCategory pCategory, ItemLike pResult, Ingredient pMaterial, int count) {
        return ShapedRecipeBuilder.shaped(pCategory, pResult, count).define('S', pMaterial).pattern("SS").pattern("SS");
    }

    public static void excavator(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike item, TagKey<Item> material) {
        excavatorBuilder(TOOLS, result, Ingredient.of(material), 1).unlockedBy(getHasName(item), has(material)).save(consumer);
    }

    public static RecipeBuilder excavatorBuilder(RecipeCategory category, ItemLike result, Ingredient material, int count) {
        return ShapedRecipeBuilder.shaped(category, result, count).define('#', material).define('/', Tags.Items.RODS_WOODEN).pattern(" ##").pattern(" /#").pattern("/  ");
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

    public static ConfigValueCondition config(ForgeConfigSpec.ConfigValue<?> value, String key, boolean inverted) {
        return new ConfigValueCondition(PantzMod.location("config"), value, key, Maps.newHashMap(), inverted);
    }

    public static ConfigValueCondition config(ForgeConfigSpec.ConfigValue<?> value, String key) {
        return config(value, key, false);
    }
}
