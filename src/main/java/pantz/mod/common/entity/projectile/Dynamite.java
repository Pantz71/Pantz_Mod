package pantz.mod.common.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.PlayMessages;
import pantz.mod.common.item.DynamiteItem;
import pantz.mod.common.utils.DynamiteType;
import pantz.mod.core.registry.PMEntityTypes;
import pantz.mod.core.registry.PMItems;

public class Dynamite extends ThrowableItemProjectile {

    public Dynamite(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public Dynamite(Level pLevel, LivingEntity pShooter) {
        super(PMEntityTypes.DYNAMITE.get(), pShooter, pLevel);
    }

    public Dynamite(PlayMessages.SpawnEntity entity, Level level) {
        this(PMEntityTypes.DYNAMITE.get(), level);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!level().isClientSide()) {
            switch (getDynamiteType()) {
                case COMBAT -> level().explode(this, getX(), getY(), getZ(), 3.0F, Level.ExplosionInteraction.NONE);
                case FIERY -> level().explode(this, getX(), getY(), getZ(), 3.0F, true, Level.ExplosionInteraction.BLOCK);
                case GENERIC -> level().explode(this, getX(), getY(), getZ(), 3.0F, Level.ExplosionInteraction.BLOCK);
            }
            discard();
        }
    }

    public DynamiteType getDynamiteType() {
        ItemStack stack = this.getItemRaw();
        if (stack.getItem() instanceof DynamiteItem dynamiteItem) {
            return dynamiteItem.getType();
        }
        return DynamiteType.GENERIC;
    }

    @Override
    protected Item getDefaultItem() {
        return switch (getDynamiteType()) {
            case GENERIC -> PMItems.DYNAMITE.get();
            case COMBAT -> PMItems.COMBAT_DYNAMITE.get();
            case FIERY -> PMItems.FIERY_DYNAMITE.get();
        };
    }
}
