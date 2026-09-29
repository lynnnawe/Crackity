package need.clarity.modules.visual;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;

record Search$ChunkSearcher$Hit(AABB aABBA, Block blockB) {
   public AABB getAABBA() {
      return this.aABBA;
   }

   public Block getBlockB() {
      return this.blockB;
   }
}
