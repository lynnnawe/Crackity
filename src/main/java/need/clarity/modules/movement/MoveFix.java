package need.clarity.modules.movement;

import need.clarity.LocalPlayerRotationState;
import need.clarity.modules.ModuleBase;
import need.clarity.modules.combat.AimAssist;
import net.minecraft.util.Mth;

public class MoveFix extends ModuleBase {
   private static MoveFix instance;
   private static volatile boolean flagB = false;

   public MoveFix() {
      super("MoveFix", "Movement");
      instance = this;
   }

   public static void methodA(boolean bl) {
      flagB = bl;
   }

   public static boolean isFlagB() {
      return flagB;
   }

   public static boolean isModeSetting() {
      if (instance == null || !instance.isEnabled()) {
         return false;
      }

      if (!AimAssist.isFlagF()) {
         return false;
      }

      String string = instance.getSetting("Mode", "Silent");
      boolean bl;
      if (!"None".equals(string)) {
         bl = true;
      } else {
         bl = false;
      }

      return bl;
   }

   public static boolean isModeSetting2() {
      if (!isModeSetting()) {
         return false;
      }

      if (instance == null) {
         return false;
      }

      Object object = instance.getSetting("Mode", "Silent");
      return "Silent".equals(object);
   }

   public static void methodA(MoveFix$EventMovementInput eventMovementInput) {
      if (isModeSetting2()) {
         if (LocalPlayerRotationState.silentActive) {
            eventMovementInput.updateImpulses();
            float k = eventMovementInput.forwardImpulse;
            float l = eventMovementInput.leftImpulse;
            if (k != 0.0F || l != 0.0F) {
               float m = LocalPlayerRotationState.realYaw;
               float n = LocalPlayerRotationState.tickServerYaw;
               float o = (float)Math.toDegrees(methodA(m, k, l));
               float f = 0.0F;
               float g = 0.0F;
               float h = Float.MAX_VALUE;

               for (float i = -1.0F; i <= 1.0F; i++) {
                  for (float j = -1.0F; j <= 1.0F; j++) {
                     if (i != 0.0F || j != 0.0F) {
                        float t = i;
                        float v = j;
                        float p = (float)Math.toDegrees(methodA(n, t, v));
                        float q = Math.abs(Mth.wrapDegrees(o - p));
                        float u = h;
                        if (!(q >= u)) {
                           h = q;
                           f = i;
                           g = j;
                        }
                     }
                  }
               }

               float r = f;
               eventMovementInput.forwardImpulse = r;
               float s = g;
               eventMovementInput.leftImpulse = s;
               eventMovementInput.updateDirectionalKeys();
            }
         }
      }
   }

   private static double methodA(float f, float g, float h) {
      if (g < 0.0F) {
         f += 180.0F;
      }

      float j = 1.0F;
      if (g < 0.0F) {
         j = -0.5F;
      } else if (g > 0.0F) {
         j = 0.5F;
      }

      if (h > 0.0F) {
         float k = f;
         float i = j;
         float m = 90.0F * i;
         f = k - m;
      }

      if (h < 0.0F) {
         float l = f;
         float o = j;
         float n = 90.0F * o;
         f = l + n;
      }

      return Math.toRadians(f);
   }
}
