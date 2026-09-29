package need.clarity.modules.combat;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

record Piercing$Trace(Player playerA, Vec3 vec3B, Vec3 vec3C) {
   public Vec3 getVec3B() {
      return this.vec3B;
   }

   public Vec3 getVec3C() {
      return this.vec3C;
   }
}
