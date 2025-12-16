package pantz.mod.core;

import com.mojang.logging.LogUtils;
import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.*;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import pantz.mod.core.data.client.PMBlockStateProvider;
import pantz.mod.core.data.client.PMItemModelProvider;
import pantz.mod.core.data.client.PMSpriteSourceProvider;
import pantz.mod.core.data.server.PMDatapackBuiltinEntriesProvider;
import pantz.mod.core.data.server.PMLootTableProvider;
import pantz.mod.core.data.server.PMRecipeProvider;
import pantz.mod.core.data.server.modifiers.PMLootModifierProvider;
import pantz.mod.core.data.server.tags.PMBlockTagsProvider;
import pantz.mod.core.data.server.tags.PMItemTagsProvider;
import pantz.mod.core.data.server.tags.PMTrimMaterialTagsProvider;
import pantz.mod.core.other.PMClientCompat;
import pantz.mod.core.other.PMCompat;
import pantz.mod.core.registry.PMBlocks;
import pantz.mod.core.registry.PMItems;

import java.util.concurrent.CompletableFuture;

@Mod(PantzMod.MOD_ID)
public class PantzMod {
    public static final String MOD_ID = "pantz_mod";
    public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);
    private static final Logger LOGGER = LogUtils.getLogger();

    public PantzMod() {
        ModLoadingContext context = ModLoadingContext.get();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        PMBlocks.BLOCKS.register(bus);
        PMItems.ITEMS.register(bus);

        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        bus.addListener(this::dataSetup);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            PMBlocks.setupTabs();
            PMItems.setupTabs();
        });

        context.registerConfig(Type.COMMON, PMConfig.Common.COMMON_SPEC);
        DataUtil.registerConfigCondition(MOD_ID, PMConfig.Common.COMMON);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(PMCompat::registerCompat);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(PMClientCompat::registerClientCompat);
    }

    private void dataSetup(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();

        boolean client = event.includeClient();
        gen.addProvider(client, new PMBlockStateProvider(output, helper));
        gen.addProvider(client, new PMItemModelProvider(output, helper));
        gen.addProvider(client, new PMSpriteSourceProvider(output, helper));

        boolean server = event.includeServer();
        PMDatapackBuiltinEntriesProvider datapackEntries = new PMDatapackBuiltinEntriesProvider(output, provider);
        gen.addProvider(server, datapackEntries);
        provider = datapackEntries.getRegistryProvider();

        gen.addProvider(server, new PMRecipeProvider(output));
        gen.addProvider(server, new PMLootTableProvider(output));

        PMBlockTagsProvider blockTags = new PMBlockTagsProvider(output, provider, helper);
        gen.addProvider(server, blockTags);
        gen.addProvider(server, new PMItemTagsProvider(output, provider, blockTags.contentsGetter(), helper));
        gen.addProvider(server, new PMTrimMaterialTagsProvider(output, provider, helper));

        gen.addProvider(server, new PMLootModifierProvider(output, provider));

    }

    public static ResourceLocation location(String loc) {
        return new ResourceLocation(MOD_ID, loc);
    }
}
