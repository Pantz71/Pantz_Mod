package pantz.mod.common.utils;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.fml.ModList;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class CompatHelper {
    private static <I extends Item> Supplier<? extends I> item(List<String> modIds, Function<Item.Properties, ? extends I> compat, Function<Item.Properties, ? extends I> fallBack, Supplier<Item.Properties> properties) {
        for (String modId : modIds) {
            if (ModList.get().isLoaded(modId)) {
                return () -> compat.apply(properties.get());
            }
        }
        return () -> fallBack.apply(properties.get());
    }

    public static Supplier<? extends Item> item(List<String> modIds, Function<Item.Properties, Item> compat, Supplier<Item.Properties> properties) {
        return item(modIds, compat, Item::new, properties);
    }

    private static <B extends Block> Supplier<? extends B> block(List<String> modIds, Function<BlockBehaviour.Properties, ? extends B> compat, Function<BlockBehaviour.Properties, ? extends B> fallBack, Supplier<BlockBehaviour.Properties> properties) {
        for (String modId : modIds) {
            if (ModList.get().isLoaded(modId)) {
                return () -> compat.apply(properties.get());
            }
        }
        return () -> fallBack.apply(properties.get());
    }

    public static Supplier<? extends Block> block(List<String> modIds, Function<BlockBehaviour.Properties, Block> compat, Supplier<BlockBehaviour.Properties> properties) {
        return block(modIds, compat, Block::new, properties);
    }
}
