package need.clarity.render;

import net.minecraft.world.phys.AABB;

public record ColoredBox(AABB bounds, int color) {
   public AABB getBounds() {
      return this.bounds;
   }

   public int getColor() {
      return this.color;
   }
}
