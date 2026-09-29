package need.clarity.modules.visual;

import need.clarity.modules.ModuleBase;

public class CameraTweaks extends ModuleBase {
   private static CameraTweaks instance;

   public CameraTweaks() {
      super("Camera Tweaks", "Visual");
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

   public static boolean isNoCameraClipSetting() {
      if (instance == null) {
         return false;
      }

      CameraTweaks cameraTweaks = instance;
      Boolean boolean_ = true;
      return cameraTweaks.getSetting("No Camera Clip", boolean_);
   }

   public static boolean isCameraDistanceSetting() {
      if (instance == null) {
         return false;
      }

      CameraTweaks cameraTweaks = instance;
      Boolean boolean_ = false;
      return cameraTweaks.getSetting("Camera Distance", boolean_);
   }

   public static float getDistanceSetting() {
      if (instance == null) {
         return 4.0F;
      }

      CameraTweaks cameraTweaks = instance;
      Float float_ = 4.0F;
      return cameraTweaks.getSetting("Distance", float_);
   }
}
