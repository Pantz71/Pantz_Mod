package pantz.mod.core;

import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.*;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.Nullable;
import pantz.mod.core.data.client.PMBlockStateProvider;
import pantz.mod.core.data.client.PMItemModelProvider;
import pantz.mod.core.data.client.PMSpriteSourceProvider;
import pantz.mod.core.data.server.*;
import pantz.mod.core.data.server.tags.*;
import pantz.mod.core.other.*;
import pantz.mod.core.registry.*;
import pantz.mod.core.registry.helper.PMBlockSubRegistryHelper;

import java.util.concurrent.CompletableFuture;

@Mod(PantzMod.MOD_ID)
public class PantzMod {
    public static final String MOD_ID = "pantz_mod";
    public static final RegistryHelper REGISTRY_HELPER = RegistryHelper.create(MOD_ID, helper -> helper.putSubHelper(Registries.BLOCK, new PMBlockSubRegistryHelper(helper)));

    public PantzMod(IEventBus bus, ModContainer container) {
        PMBlocks.BLOCKS.register(bus);
        PMItems.ITEMS.register(bus);
        PMBlockEntityTypes.BLOCK_ENTITY_TYPES.register(bus);
        PMEntityTypes.ENTITY_TYPES.register(bus);
        PMAttributes.ATTRIBUTES.register(bus);
        PMDataComponents.DATA_COMPONENTS.register(bus);
        PMMobEffects.register(bus);
        PMMenuTypes.MENUS.register(bus);
        PMParticleTypes.PARTICLE_TYPES.register(bus);
        PMSoundEvents.SOUND_EVENTS.register(bus);
        PMCriteriaTriggers.TRIGGERS.register(bus);
        PMConditionSerializers.CONDITION_SERIALIZERS.register(bus);
        PMLootConditions.LOOT_CONDITION_TYPES.register(bus);
        PMLootContextParamSets.register();
        PMLootFunctions.LOOT_FUNCTIONS.register(bus);

        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        bus.addListener(this::dataSetup);

        container.registerConfig(Type.COMMON, PMConfig.Common.COMMON_SPEC);

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
        gen.addProvider(client, new PMSpriteSourceProvider(output, provider, helper));

        boolean server = event.includeServer();
        PMDatapackBuiltinEntriesProvider datapackEntries = new PMDatapackBuiltinEntriesProvider(output, provider);
        gen.addProvider(server, datapackEntries);
        provider = datapackEntries.getRegistryProvider();

        gen.addProvider(server, new PMRecipeProvider(output, provider));
        gen.addProvider(server, new PMLootTableProvider(output, provider));

        BlockTagsProvider blockTags = new PMBlockTagsProvider(output, provider, helper);

        gen.addProvider(server, blockTags);
        gen.addProvider(server, new PMItemTagsProvider(output, provider, blockTags.contentsGetter(), helper));
        gen.addProvider(server, new PMEntityTypeTagsProvider(output, provider, helper));
        gen.addProvider(server, new PMBiomeTagsProvider(output, provider, helper));
        gen.addProvider(server, new PMTrimMaterialTagsProvider(output, provider, helper));

        gen.addProvider(server, PMAdvancementProvider.create(output, provider, helper));

        gen.addProvider(server, new PMDataRemolderProvider(output, provider));

    }

    public static ResourceLocation location(String loc) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, loc);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> pServerType, BlockEntityType<E> pClientType, BlockEntityTicker<? super E> pTicker) {
        return pClientType == pServerType ? (BlockEntityTicker<A>)pTicker : null;
    }
}
