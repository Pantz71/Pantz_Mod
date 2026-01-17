package pantz.mod.core.data.server;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.common.data.ForgeAdvancementProvider.AdvancementGenerator;
import net.minecraftforge.registries.ForgeRegistries;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.PMCriteriaTriggers;
import pantz.mod.core.registry.PMBlocks;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PMAdvancementProvider implements AdvancementGenerator {
    public static ForgeAdvancementProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        return new ForgeAdvancementProvider(output, provider, helper, List.of(new PMAdvancementProvider()));
    }

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> consumer, ExistingFileHelper existingFileHelper) {
        createAdvancement("look_at_scanner", "adventure", new ResourceLocation("adventure/root"),
                PMBlocks.ENDER_SCANNER.get(), FrameType.TASK, true, true, false)
                .addCriterion("look_at_scanner", PMCriteriaTriggers.LOOK_AT_SCANNER.createInstance())
                .save(consumer, advancement("adventure/look_at_scanner"));

        createAdvancement("redirect_teleportation", "end", new ResourceLocation("end/root"),
                Items.ENDER_PEARL, FrameType.TASK, true, true, false)
                .addCriterion("redirect_teleportation", PMCriteriaTriggers.REDIRECT_TELEPORTATION.createInstance())
                .save(consumer, advancement("end/redirect_teleportation"));

        createItemAdvancement("obtain_moon_globe", "adventure", new ResourceLocation("adventure/trade"),
                PMBlocks.MOON_GLOBE.get(), FrameType.TASK, true, true, false)
                .save(consumer, advancement("adventure/obtain_moon_globe"));

        createItemAdvancement("obtain_saturn_globe", "nether", new ResourceLocation("nether/find_fortress"),
                PMBlocks.SATURN_GLOBE.get(), FrameType.TASK, true, true, false)
                .save(consumer, advancement("nether/obtain_saturn_globe"));

        createItemAdvancement("obtain_jupiter_globe", "nether", new ResourceLocation("nether/find_fortress"),
                PMBlocks.JUPITER_GLOBE.get(), FrameType.TASK, true, true, false)
                .save(consumer, advancement("nether/obtain_jupiter_globe"));

        createItemAdvancement("obtain_neptune_globe", "nether", new ResourceLocation("nether/loot_bastion"),
                PMBlocks.NEPTUNE_GLOBE.get(), FrameType.TASK, true, true, false)
                .save(consumer, advancement("nether/obtain_neptune_globe"));

        createItemAdvancement("obtain_iris_globe", "end", new ResourceLocation("end/find_end_city"),
                PMBlocks.IRIS_GLOBE.get(), FrameType.TASK, true, true, false)
                .save(consumer, advancement("end/obtain_iris_globe"));
    }

    private static Advancement.Builder createAdvancement(String name, String category, Advancement parent, ItemLike icon, FrameType frame, boolean showToast, boolean announceToChat, boolean hidden) {
        return Advancement.Builder.advancement().parent(parent).display(icon,
                Component.translatable("advancements." + PantzMod.MOD_ID + "." + category + "." + name + ".title"),
                Component.translatable("advancements." + PantzMod.MOD_ID + "." + category + "." + name + ".description"),
                null, frame, showToast, announceToChat, hidden);
    }

    private static Advancement.Builder createItemAdvancement(String name, String category, Advancement parent, ItemLike item, FrameType frame, boolean showToast, boolean announceToChat, boolean hidden) {
        return Advancement.Builder.advancement().parent(parent).display(item,
                Component.translatable("advancements." + PantzMod.MOD_ID + "." + category + "." + name + ".title"),
                Component.translatable("advancements." + PantzMod.MOD_ID + "." + category + "." + name + ".description"),
                null, frame, showToast, announceToChat, hidden)
                .addCriterion(ForgeRegistries.ITEMS.getKey(item.asItem()).getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(item));
    }


    private static Advancement.Builder createAdvancement(String name, String category, ResourceLocation parent, ItemLike icon, FrameType frame, boolean showToast, boolean announceToChat, boolean hidden) {
        return createAdvancement(name, category, Advancement.Builder.advancement().build(parent), icon, frame, showToast, announceToChat, hidden);
    }

    private static Advancement.Builder createItemAdvancement(String name, String category, ResourceLocation parent, ItemLike icon, FrameType frame, boolean showToast, boolean announceToChat, boolean hidden) {
        return createItemAdvancement(name, category, Advancement.Builder.advancement().build(parent), icon, frame, showToast, announceToChat, hidden);
    }

    public static String advancement(String path) {
        return PantzMod.MOD_ID + ":" + path;
    }
}
