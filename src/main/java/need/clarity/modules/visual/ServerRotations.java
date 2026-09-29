package need.clarity.modules.visual;

import need.clarity.modules.ModuleBase;
import net.minecraft.util.Mth;

public class ServerRotations extends ModuleBase {
   private static ServerRotations instance;
   private static float floatB;
   private static float floatC;

   public ServerRotations() {
      super("Server Rotations", "Visual");
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

   public static void methodA(float f) {
      floatB = f;
   }

   public static void methodB(float f) {
      floatC = f;
   }

   public static float methodC(float f) {
      float g = Mth.clamp(f, 0.0F, 1.0F);
      float h = floatB;
      float i = floatC;
      return Mth.lerp(g, h, i);
   }
}
