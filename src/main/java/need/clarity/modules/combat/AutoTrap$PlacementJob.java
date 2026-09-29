package need.clarity.modules.combat;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;

record AutoTrap$PlacementJob(BlockPos blockPosA, BlockHitResult blockHitResultB, int intC) {
   public BlockPos getBlockPosA() {
      return this.blockPosA;
   }

   public BlockHitResult getBlockHitResultB() {
      return this.blockHitResultB;
   }

   public int getIntC() {
      return this.intC;
   }
}
