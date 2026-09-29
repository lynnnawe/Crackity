package need.clarity.modules.combat;

import net.minecraft.core.BlockPos;

record AutoTrap$RenderEntry(BlockPos blockPosA, long longB) {
   public BlockPos getBlockPosA() {
      return this.blockPosA;
   }

   public long getLongB() {
      return this.longB;
   }
}
