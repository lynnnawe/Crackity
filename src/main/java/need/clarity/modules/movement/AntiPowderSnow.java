package need.clarity.modules.movement;

import need.clarity.modules.ModuleBase;

public class AntiPowderSnow extends ModuleBase {
   public static AntiPowderSnow instance;

   public AntiPowderSnow() {
      super("Anti Powder Snow", "Movement");
      instance = this;
   }
}
