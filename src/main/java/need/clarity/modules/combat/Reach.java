package need.clarity.modules.combat;

import need.clarity.modules.ModuleBase;
import need.clarity.modules.ModuleRegistry;

public class Reach extends ModuleBase {
   public Reach() {
      super("Reach", "Combat");
   }

   public static float getReachSetting() {
      Reach reach = (Reach)ModuleRegistry.getModule("Reach");
      if (reach != null && reach.isEnabled()) {
         Float float_ = 4.5F;
         return reach.getSetting("Block Reach", float_);
      } else {
         return -1.0F;
      }
   }

   public static float getReachSetting2() {
      Reach reach = (Reach)ModuleRegistry.getModule("Reach");
      if (reach != null && reach.isEnabled()) {
         Float float_ = 3.0F;
         return reach.getSetting("Entity Reach", float_);
      } else {
         return -1.0F;
      }
   }
}
