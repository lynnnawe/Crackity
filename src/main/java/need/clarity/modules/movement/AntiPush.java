package need.clarity.modules.movement;

import need.clarity.modules.ModuleBase;

public class AntiPush extends ModuleBase {
   public static AntiPush instance;

   public AntiPush() {
      super("Anti Push", "Movement");
      instance = this;
   }

   public static boolean methodA() {
      boolean bl;
      if (instance != null && instance.isEnabled()) {
         bl = true;
      } else {
         bl = false;
      }

      return bl;
   }
}
