package need.clarity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import need.clarity.modules.visual.PlayerESP;

public final class PlayerEspRenderer {
   private static boolean flagA = false;
   private static boolean flagB = false;

   public static void methodA(PoseStack poseStack, List<ColoredBox> list, float g) {
      if (!list.isEmpty()) {
         boolean bl1 = PlayerESP.isThroughWallsSetting();
         String string = PlayerESP.getBoxStyleSetting();
         boolean bl2 = "Corners".equals(string);
         String string1 = PlayerESP.getModeSetting();
         boolean bl4;
         if (!"3D".equals(string1) && !PlayerESP.isFillSetting()) {
            bl4 = false;
         } else {
            bl4 = true;
         }

         boolean bl = bl4;
         boolean bl3 = PlayerESP.isOutlineSetting();
         int i = PlayerESP.getFillColorSetting();
         int j = Math.round(methodA(PlayerESP.getFillOpacitySetting()) * 255.0F) << 24;
         int l = i & 16777215;
         int k = j | l;
         float f = PlayerESP.getOutlineThicknessSetting();
         BoxRenderer.methodA(poseStack, list, bl, k, bl3, bl1, bl2, f);
      }
   }

   private static float methodA(float f) {
      float g;
      if (f < 0.0F) {
         g = 0.0F;
      } else if (f > 1.0F) {
         g = 1.0F;
      } else {
         g = f;
      }

      return g;
   }
}
