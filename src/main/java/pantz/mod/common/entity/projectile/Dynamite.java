package pantz.mod.common.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import pantz.mod.common.item.DynamiteItem;
import pantz.mod.common.utils.DynamiteType;
import pantz.mod.core.registry.PMEntityTypes;
import pantz.mod.core.registry.PMItems;

public class Dynamite extends ThrowableItemProjectile {

    public Dynamite(Level pLevel, LivingEntity pShooter) {
        super(PMEntityTypes.DYNAMITE.get(), pShooter, pLevel);
    }

    public Dynamite(Level level, double x, double y, double z) {
        super(PMEntityTypes.DYNAMITE.get(), x, y, z, level);
    }

    public Dynamite(EntityType<? extends ThrowableItemProjectile> entity, Level level) {
        super(entity, level);
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
        ItemStack stack = this.getItem();
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
