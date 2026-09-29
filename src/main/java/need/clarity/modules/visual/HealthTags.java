package need.clarity.modules.visual;

import need.clarity.modules.ModuleBase;
import net.minecraft.world.entity.LivingEntity;

public class HealthTags extends ModuleBase {
   private static HealthTags instance;

   public HealthTags() {
      super("Health Tags", "Visual");
      instance = this;
   }

   public static HealthTags getInstance() {
      return instance;
   }

   public static int methodA(float f, float g) {
      if (g <= 0.0F) {
         return -1;
      } else {
         float h = f / g;
         if (h >= 0.8F) {
            return -11141291;
         } else if (h >= 0.5F) {
            return -171;
         } else {
            return h >= 0.25F ? -22016 : -43691;
         }
      }
   }

   public static int methodA(LivingEntity livingEntity) {
      int i = Math.round(livingEntity.getHealth());
      return Math.max(0, i);
   }
}
