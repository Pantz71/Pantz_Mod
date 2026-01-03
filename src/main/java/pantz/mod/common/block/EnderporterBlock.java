package pantz.mod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import pantz.mod.core.registry.PMParticleTypes;
import pantz.mod.core.registry.PMSoundEvents;

@SuppressWarnings("deprecation")
public class EnderporterBlock extends Block {
    public static final IntegerProperty ENDERPORTER_CHARGE = IntegerProperty.create("enderporter_charge", 0, 4);

    public EnderporterBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(ENDERPORTER_CHARGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ENDERPORTER_CHARGE);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        if (hand == InteractionHand.MAIN_HAND) {
            if (stack.is(Items.END_STONE) && state.getValue(ENDERPORTER_CHARGE) < 4) {
                if (!level.isClientSide()) {
                    int currentCharge = state.getValue(ENDERPORTER_CHARGE);
                    level.setBlock(pos, state.setValue(ENDERPORTER_CHARGE, currentCharge + 1), 3);
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    level.playSound(null, pos, PMSoundEvents.ENDERPORTER_CHARGE.get(), SoundSource.BLOCKS);
                }
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(ENDERPORTER_CHARGE) > 0) {
            Direction direction = Direction.getRandom(random);
            if (direction != Direction.UP) {
                double x = pos.getX() + (direction.getStepX() == 0 ? random.nextDouble() : 0.5 + direction.getStepX() * 0.6d);
                double y = pos.getY() + (direction.getStepY() == 0 ? random.nextDouble() : 0.25 + direction.getStepY());
                double z = pos.getZ() + (direction.getStepZ() == 0 ? random.nextDouble() : 0.5 + direction.getStepZ() * 0.6d);
                level.addParticle(PMParticleTypes.END_SPARKLE.get(), x, y, z, random.nextGaussian() * 0.005D, random.nextGaussian() * 0.005D, random.nextGaussian() * 0.005D);
            }
        }
    }

}
