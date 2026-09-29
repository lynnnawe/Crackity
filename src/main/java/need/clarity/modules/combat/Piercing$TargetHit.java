package need.clarity.modules.combat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

record Piercing$TargetHit(LivingEntity livingEntityA, Vec3 vec3B) {
   public LivingEntity getLivingEntityA() {
      return this.livingEntityA;
   }

   public Vec3 getVec3B() {
      return this.vec3B;
   }
}
