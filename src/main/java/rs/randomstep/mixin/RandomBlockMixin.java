package rs.randomstep.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Entity.class)
public class RandomBlockMixin {

    @Shadow
    private @Nullable BlockState inBlockState;
    private int LastPOS_X;
    private int LastPOS_Y;
    private int LastPOS_Z;

    @Inject(method = "move", at = @At("TAIL"))
    private void randomSteps(MoverType moverType, Vec3 movement, CallbackInfo ci){

        Entity entity = (Entity)(Object)this;

        int POS_X = Mth.floor(entity.getX());
        int POS_Y = Mth.floor(entity.getY());
        int POS_Z = Mth.floor(entity.getZ());

        if (entity instanceof Player player) // Check That You Are A Player And Not Other Entity e.g. Zombie
        {

            if (LastPOS_X != POS_X || LastPOS_Y != POS_Y || LastPOS_Z != POS_Z) { // Check That We Aren't Staying On The Same Block

                BlockPos blockPos = entity.blockPosition().below();

                if (!entity.level().getBlockState(blockPos).isAir()) {

                    Optional<Holder.Reference<Block>> randomBlock = BuiltInRegistries.BLOCK.getRandom(entity.getRandom());

                    BlockState randomBlockState = randomBlock.get().value().defaultBlockState();

                    if (!randomBlockState.isAir()) {

                        entity.level().setBlock(blockPos, randomBlockState, 3);

                        // Update LastPOS_X

                        if (LastPOS_X != POS_X){
                            LastPOS_X = POS_X;
                        }

                        // Update LastPOS_Y

                        if (LastPOS_Y != POS_Y){
                            LastPOS_Y = POS_Y;
                        }

                        // Update LastPOS_Y

                        if (LastPOS_Z != POS_Z){
                            LastPOS_Z = POS_Z;
                        }

                    }

                }

            }

        }

    }

}
