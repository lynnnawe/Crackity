package need.clarity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import need.clarity.modules.ModuleRegistry;
import need.clarity.modules.visual.Animations;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import org.joml.Quaternionf;

public final class HeldItemAnimations {
   public static boolean methodA(HumanoidArm humanoidArm) {
      Animations animations = methodA();
      if (animations != null && animations.isEnabled()) {
         Minecraft minecraft = Minecraft.getInstance();
         boolean bl;
         if (minecraft.player != null && minecraft.player.getMainArm() == humanoidArm) {
            bl = true;
         } else {
            bl = false;
         }

         return bl;
      } else {
         return false;
      }
   }

   public static boolean methodA(PoseStack poseStack, HumanoidArm humanoidArm, float f) {
      if (!methodA(humanoidArm)) {
         return false;
      }

      Animations animations = methodA();
      if (animations == null) {
         return false;
      }

      String string = animations.getModeSetting();
      int i = animations.getStrengthSetting();
      float h = Mth.sin(Mth.sqrt(f) * (float) Math.PI);
      float j = (float)Math.sin(f * Math.PI);
      HumanoidArm humanoidArm1 = HumanoidArm.LEFT;
      float l;
      if (humanoidArm == humanoidArm1) {
         l = -1.0F;
      } else {
         l = 1.0F;
      }

      float g = l;
      float k = Mth.clamp(i, 20, 75);
      byte b = -1;
      int bh = string.hashCode();
      switch (bh) {
         case -1984934636:
            if (string.equals("Mode 1")) {
               b = 0;
            }
            break;
         case -1984934635:
            if (string.equals("Mode 2")) {
               b = 1;
            }
            break;
         case -1984934634:
            if (string.equals("Mode 3")) {
               b = 2;
            }
            break;
         case -1984934633:
            if (string.equals("Mode 4")) {
               b = 3;
            }
            break;
         case -1984934632:
            if (string.equals("Mode 5")) {
               b = 4;
            }
      }

      byte c = b;
      switch (c) {
         case 0:
            methodA(poseStack, humanoidArm);
            Axis axis8 = Axis.YP;
            float bg = j * -20.0F;
            float bc = 45.0F + bg;
            float u = g * bc;
            Quaternionf quaternionf10 = axis8.rotationDegrees(u);
            poseStack.mulPose(quaternionf10);
            Axis axis9 = Axis.ZP;
            float v = g * h * -20.0F;
            Quaternionf quaternionf11 = axis9.rotationDegrees(v);
            poseStack.mulPose(quaternionf11);
            Axis axis10 = Axis.XP;
            float w = h * -80.0F;
            Quaternionf quaternionf12 = axis10.rotationDegrees(w);
            poseStack.mulPose(quaternionf12);
            Axis axis11 = Axis.YP;
            float x = g * -45.0F;
            Quaternionf quaternionf13 = axis11.rotationDegrees(x);
            poseStack.mulPose(quaternionf13);
            break;
         case 1:
            methodA(poseStack, humanoidArm);
            Quaternionf quaternionf1 = Axis.XP.rotationDegrees(50.0F);
            poseStack.mulPose(quaternionf1);
            Axis axis1 = Axis.YP;
            float n = g * -60.0F;
            Quaternionf quaternionf2 = axis1.rotationDegrees(n);
            poseStack.mulPose(quaternionf2);
            Axis axis2 = Axis.ZP;
            float bd = k * h;
            float y = 110.0F + bd;
            float o = g * y;
            Quaternionf quaternionf3 = axis2.rotationDegrees(o);
            poseStack.mulPose(quaternionf3);
            break;
         case 2:
            methodA(poseStack, humanoidArm);
            Quaternionf quaternionf4 = Axis.XP.rotationDegrees(50.0F);
            poseStack.mulPose(quaternionf4);
            Axis axis3 = Axis.YP;
            float be = 1.0F - h;
            float z = -30.0F * be - 30.0F;
            float bf = (k - 20.0F) * h;
            float ba = z + bf;
            float p = g * ba;
            Quaternionf quaternionf5 = axis3.rotationDegrees(p);
            poseStack.mulPose(quaternionf5);
            Axis axis4 = Axis.ZP;
            float q = g * 110.0F;
            Quaternionf quaternionf6 = axis4.rotationDegrees(q);
            poseStack.mulPose(quaternionf6);
            break;
         case 3:
            methodA(poseStack, humanoidArm);
            Axis axis5 = Axis.YP;
            float r = g * 90.0F;
            Quaternionf quaternionf7 = axis5.rotationDegrees(r);
            poseStack.mulPose(quaternionf7);
            Axis axis6 = Axis.ZP;
            float s = g * -30.0F;
            Quaternionf quaternionf8 = axis6.rotationDegrees(s);
            poseStack.mulPose(quaternionf8);
            Axis axis7 = Axis.XP;
            float bb = k * j;
            float t = -90.0F - bb + 10.0F;
            Quaternionf quaternionf9 = axis7.rotationDegrees(t);
            poseStack.mulPose(quaternionf9);
            break;
         case 4:
            methodA(poseStack, humanoidArm);
            Axis axis = Axis.XP;
            float m = f * -360.0F;
            Quaternionf quaternionf = axis.rotationDegrees(m);
            poseStack.mulPose(quaternionf);
            break;
         default:
            return false;
      }

      return true;
   }

   private static void methodA(PoseStack poseStack, HumanoidArm humanoidArm) {
      HumanoidArm humanoidArm1 = HumanoidArm.RIGHT;
      float f;
      if (humanoidArm == humanoidArm1) {
         f = 1.0F;
      } else {
         f = -1.0F;
      }

      float g = f * 0.56F;
      poseStack.translate(g, -0.52F, -0.72F);
   }

   private static Animations methodA() {
      if (!(ModuleRegistry.getModule("Animations") instanceof Animations animations)) {
         animations = null;
      }

      return animations;
   }
}
