package need.clarity.modules.visual;

import need.clarity.modules.ModuleBase;

public class Animations extends ModuleBase {
   public static volatile Animations instance;

   public Animations() {
      super("Animations", "Visual");
      instance = this;
   }

   public String getModeSetting() {
      Object object = this.getSetting("Mode", "Simple");
      if (object == null) {
         return "Simple";
      }

      String string = object.toString();
      String string1;
      if ("Simple".equals(string)) {
         string1 = "Mode 1";
      } else {
         string1 = string;
      }

      return string1;
   }

   public int getStrengthSetting() {
      Integer integer = 20;
      Object object = this.getSetting("Strength", integer);
      int i;
      if (object instanceof Number) {
         i = ((Number)object).intValue();
      } else {
         i = 20;
      }

      return Math.clamp((byte)i, 20, 75);
   }

   public boolean isSwingSpeedSetting() {
      Boolean boolean1 = false;
      Object object = this.getSetting("Swing Speed", boolean1);
      boolean bl;
      if (object instanceof Boolean) {
         bl = (Boolean)object;
      } else {
         bl = false;
      }

      return bl;
   }

   public int getSwingSpeedValueSetting() {
      Integer integer = 6;
      Object object = this.getSetting("Swing Speed Value", integer);
      int i;
      if (object instanceof Number) {
         i = ((Number)object).intValue();
      } else {
         i = 6;
      }

      return Math.clamp((byte)i, 1, 20);
   }

   public static boolean methodE() {
      Animations animations = instance;
      boolean bl;
      if (animations != null && animations.isEnabled() && animations.isSwingSpeedSetting()) {
         bl = true;
      } else {
         bl = false;
      }

      return bl;
   }

   public static int methodF() {
      Animations animations = instance;
      int i;
      if (animations != null) {
         i = animations.getSwingSpeedValueSetting();
      } else {
         i = 6;
      }

      return i;
   }
}
