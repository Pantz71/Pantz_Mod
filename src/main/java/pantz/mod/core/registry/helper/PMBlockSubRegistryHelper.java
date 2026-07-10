package pantz.mod.core.registry.helper;

import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.function.Supplier;

public class PMBlockSubRegistryHelper extends BlockSubRegistryHelper {
    public PMBlockSubRegistryHelper(RegistryHelper parent) {
        super(parent);
    }

    public <B extends Block> DeferredBlock<B> createCustomRarityBlock(String name, Rarity rarity, Supplier<? extends B> supplier) {
        DeferredBlock<B> block = this.deferredRegister.register(name, supplier);
        this.itemRegister.register(name, () -> new BlockItem(block.get(), new Item.Properties().fireResistant().rarity(rarity)));
        return block;
    }

    public <B extends Block> DeferredBlock<B> createPlacedItem(String name, Supplier<? extends B> supplier) {
        DeferredBlock<B> block = this.deferredRegister.register(name, supplier);
        this.itemRegister.register(name + "_placed", () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }
}
