package need.clarity.modules.movement;

import need.clarity.PerMovementEvents;
import need.clarity.modules.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.lwjgl.glfw.GLFW;

public class Fly extends ModuleBase {
   private static Fly instance;
   public static volatile double doubleA = 0.0;
   public static volatile double doubleB = 0.0;
   public static volatile double doubleC = 0.0;
   public static volatile boolean flagD = false;

   public Fly() {
      super("Fly", "Movement");
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

   public static String getCreativeSetting() {
      String string;
      if (instance == null) {
         string = "Creative";
      } else {
         string = instance.getSetting("Mode", "Creative");
      }

      return string;
   }

   public static float getSpeedSetting() {
      float f;
      if (instance == null) {
         f = 5.0F;
      } else {
         Fly fly = instance;
         Float float1 = 5.0F;
         f = fly.getSetting("Speed", float1);
      }

      return f;
   }

   @Override
   public void onEnable() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         LocalPlayer localPlayer = minecraft.player;
         double d = minecraft.player.getDeltaMovement().x;
         double e = minecraft.player.getDeltaMovement().z;
         localPlayer.setDeltaMovement(d, 0.0, e);
         flagD = false;
      }
   }

   @Override
   public void onDisable() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         minecraft.player.setDeltaMovement(0.0, 0.0, 0.0);
         flagD = false;
      }
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         LocalPlayer localPlayer = minecraft.player;
         float f = getSpeedSetting();
         MoveFix$EventMovementInput eventMovementInput = PerMovementEvents.currentInput;
         boolean bl8;
         if (eventMovementInput != null) {
            bl8 = eventMovementInput.forward;
         } else {
            bl8 = minecraft.options.keyUp.isDown();
         }

         boolean bl = bl8;
         boolean bl11;
         if (eventMovementInput != null) {
            bl11 = eventMovementInput.backward;
         } else {
            bl11 = minecraft.options.keyDown.isDown();
         }

         boolean bl1 = bl11;
         boolean bl6;
         if (eventMovementInput != null) {
            bl6 = eventMovementInput.left;
         } else {
            bl6 = minecraft.options.keyLeft.isDown();
         }

         boolean bl2 = bl6;
         boolean bl7;
         if (eventMovementInput != null) {
            bl7 = eventMovementInput.right;
         } else {
            bl7 = minecraft.options.keyRight.isDown();
         }

         boolean bl3 = bl7;
         boolean bl9;
         if (eventMovementInput != null) {
            bl9 = eventMovementInput.flagE;
         } else {
            bl9 = minecraft.options.keyJump.isDown();
         }

         boolean bl4 = bl9;
         boolean bl10;
         if (eventMovementInput != null) {
            bl10 = eventMovementInput.flagF;
         } else {
            bl10 = minecraft.options.keyShift.isDown();
         }

         boolean bl5 = bl10;
         String string = getCreativeSetting();
         if ("Full".equals(string)) {
            methodA(localPlayer, f, bl, bl1, bl2, bl3, bl4, bl5);
         } else {
            methodB(localPlayer, f, bl, bl1, bl2, bl3, bl4, bl5);
         }

         flagD = true;
      }
   }

   private static void methodA(LocalPlayer localPlayer, float f, boolean bl, boolean bl1, boolean bl2, boolean bl3, boolean bl4, boolean bl5) {
      double y = f;
      double g = 0.05 * y;
      float u;
      if (bl) {
         u = 1.0F;
      } else {
         u = 0.0F;
      }

      float x;
      if (bl1) {
         x = 1.0F;
      } else {
         x = 0.0F;
      }

      float h = u - x;
      float t;
      if (bl2) {
         t = 1.0F;
      } else {
         t = 0.0F;
      }

      float w;
      if (bl3) {
         w = 1.0F;
      } else {
         w = 0.0F;
      }

      float i = t - w;
      double d = 0.0;
      double e = 0.0;
      if (h != 0.0F || i != 0.0F) {
         float j = h * h;
         float v = i * i;
         double k = Math.sqrt(j + v);
         float l = (float)(h / k);
         float m = (float)(i / k);
         double n = Math.toRadians(localPlayer.getYRot());
         double o = Math.sin(n);
         double p = Math.cos(n);
         double q = l;
         double z = -o;
         double r = q * z;
         double ba = m * p;
         d = (r + ba) * g;
         double s = l * p;
         double bb = m * o;
         e = (s + bb) * g;
      }

      double bc = 0.0;
      if (bl4) {
         bc = g;
      }

      if (bl5) {
         bc = -g;
      }

      doubleA = d;
      doubleB = bc;
      doubleC = e;
   }

   private static void methodB(LocalPlayer localPlayer, float f, boolean bl, boolean bl1, boolean bl2, boolean bl3, boolean bl4, boolean bl5) {
      double y = f;
      double g = 0.05 * y;
      float u;
      if (bl) {
         u = 1.0F;
      } else {
         u = 0.0F;
      }

      float w;
      if (bl1) {
         w = 1.0F;
      } else {
         w = 0.0F;
      }

      float h = u - w;
      float t;
      if (bl2) {
         t = 1.0F;
      } else {
         t = 0.0F;
      }

      float x;
      if (bl3) {
         x = 1.0F;
      } else {
         x = 0.0F;
      }

      float i = t - x;
      double d = 0.0;
      double e = 0.0;
      if (h != 0.0F || i != 0.0F) {
         float j = h * h;
         float v = i * i;
         double k = Math.sqrt(j + v);
         float l = (float)(h / k);
         float m = (float)(i / k);
         double n = Math.toRadians(localPlayer.getYRot());
         double o = Math.sin(n);
         double p = Math.cos(n);
         double q = l;
         double z = -o;
         double r = q * z;
         double ba = m * p;
         d = (r + ba) * g;
         double s = l * p;
         double bb = m * o;
         e = (s + bb) * g;
      }

      double bc = 0.0;
      if (bl4) {
         bc = g;
      }

      if (bl5) {
         bc = -g;
      }

      doubleA = d;
      doubleB = bc;
      doubleC = e;
   }

   public static boolean methodA(Minecraft minecraft) {
      boolean bl;
      if (GLFW.glfwGetKey(minecraft.getWindow().handle(), 32) == 1) {
         bl = true;
      } else {
         bl = false;
      }

      return bl;
   }

   public static boolean methodB(Minecraft minecraft) {
      MoveFix$EventMovementInput eventMovementInput = PerMovementEvents.currentInput;
      return eventMovementInput != null ? eventMovementInput.flagF : minecraft.options.keyShift.isDown();
   }
}
