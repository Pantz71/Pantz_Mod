package pantz.mod.core.data.server;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.AdvancementProvider.AdvancementGenerator;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.PMCriteriaTriggers;
import pantz.mod.core.registry.PMBlocks;
import pantz.mod.core.registry.PMItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PMAdvancementProvider implements AdvancementGenerator {
    public static AdvancementProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        return new AdvancementProvider(output, provider, helper, List.of(new PMAdvancementProvider()));
    }

    @Override
    public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
        createAdvancement("look_at_scanner", "adventure", ResourceLocation.parse("adventure/root"),
                PMBlocks.ENDER_SCANNER.get(), AdvancementType.TASK, true, true, false)
                .addCriterion("look_at_scanner", PMCriteriaTriggers.lookAtScanner())
                .save(consumer, advancement("adventure/look_at_scanner"));

        createAdvancement("redirect_teleportation", "end", ResourceLocation.parse("end/root"),
                Items.ENDER_PEARL, AdvancementType.TASK, true, true, false)
                .addCriterion("redirect_teleportation", PMCriteriaTriggers.redirectTeleportation())
                .save(consumer, advancement("end/redirect_teleportation"));

        createItemAdvancement("obtain_moon_globe", "adventure", ResourceLocation.parse("adventure/trade"),
                PMBlocks.MOON_GLOBE.get(), AdvancementType.TASK, true, true, false)
                .save(consumer, advancement("adventure/obtain_moon_globe"));

        createItemAdvancement("obtain_saturn_globe", "nether", ResourceLocation.parse("nether/find_fortress"),
                PMBlocks.SATURN_GLOBE.get(), AdvancementType.TASK, true, true, false)
                .save(consumer, advancement("nether/obtain_saturn_globe"));

        createItemAdvancement("obtain_jupiter_globe", "nether", ResourceLocation.parse("nether/find_fortress"),
                PMBlocks.JUPITER_GLOBE.get(), AdvancementType.TASK, true, true, false)
                .save(consumer, advancement("nether/obtain_jupiter_globe"));

        createItemAdvancement("obtain_neptune_globe", "nether", ResourceLocation.parse("nether/loot_bastion"),
                PMBlocks.NEPTUNE_GLOBE.get(), AdvancementType.TASK, true, true, false)
                .save(consumer, advancement("nether/obtain_neptune_globe"));

        createItemAdvancement("obtain_iris_globe", "end", ResourceLocation.parse("end/find_end_city"),
                PMBlocks.IRIS_GLOBE.get(), AdvancementType.TASK, true, true, false)
                .save(consumer, advancement("end/obtain_iris_globe"));

        createAdvancement("use_dynamite", "adventure", ResourceLocation.parse("adventure/root"),
                PMItems.DYNAMITE.get(), AdvancementType.TASK, true, true, false)
                .addCriterion("dynamite", PMCriteriaTriggers.useDynamite())
                .save(consumer, advancement("adventure/use_dynamite"));
    }

    private static Advancement.Builder createAdvancement(String name, String category, AdvancementHolder parent, ItemLike icon, AdvancementType frame, boolean showToast, boolean announceToChat, boolean hidden) {
        return Advancement.Builder.advancement().parent(parent).display(icon,
                Component.translatable("advancements." + PantzMod.MOD_ID + "." + category + "." + name + ".title"),
                Component.translatable("advancements." + PantzMod.MOD_ID + "." + category + "." + name + ".description"),
                null, frame, showToast, announceToChat, hidden);
    }

    private static Advancement.Builder createItemAdvancement(String name, String category, AdvancementHolder parent, ItemLike item, AdvancementType frame, boolean showToast, boolean announceToChat, boolean hidden) {
        return Advancement.Builder.advancement().parent(parent).display(item,
                        Component.translatable("advancements." + PantzMod.MOD_ID + "." + category + "." + name + ".title"),
                        Component.translatable("advancements." + PantzMod.MOD_ID + "." + category + "." + name + ".description"),
                        null, frame, showToast, announceToChat, hidden)
                .addCriterion(BuiltInRegistries.ITEM.getKey(item.asItem()).getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(item));
    }

    private static Advancement.Builder createAdvancement(String name, String category, ResourceLocation parent, ItemLike icon, AdvancementType frame, boolean showToast, boolean announceToChat, boolean hidden) {
        return createAdvancement(name, category, Advancement.Builder.advancement().build(parent), icon, frame, showToast, announceToChat, hidden);
    }

    private static Advancement.Builder createItemAdvancement(String name, String category, ResourceLocation parent, ItemLike icon, AdvancementType frame, boolean showToast, boolean announceToChat, boolean hidden) {
        return createItemAdvancement(name, category, Advancement.Builder.advancement().build(parent), icon, frame, showToast, announceToChat, hidden);
    }

    public static String advancement(String path) {
        return PantzMod.MOD_ID + ":" + path;
    }

}
