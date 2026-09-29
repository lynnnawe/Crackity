package need.clarity.render;

import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.textures.GpuTexture;
import org.lwjgl.opengl.GL11;

public final class RenderState {
   private static boolean flagA = false;

   public static boolean isColorMasked() {
      return flagA;
   }

   public static void setColorMasked(boolean bl) {
      flagA = bl;
      boolean bl1;
      if (!bl) {
         bl1 = true;
      } else {
         bl1 = false;
      }

      boolean bl2;
      if (!bl) {
         bl2 = true;
      } else {
         bl2 = false;
      }

      boolean bl3;
      if (!bl) {
         bl3 = true;
      } else {
         bl3 = false;
      }

      boolean bl4;
      if (!bl) {
         bl4 = true;
      } else {
         bl4 = false;
      }

      GL11.glColorMask(bl1, bl2, bl3, bl4);
   }

   public static void enableBlend() {
      GlStateManager._enableBlend();
   }

   public static void disableBlend() {
      GlStateManager._disableBlend();
   }

   public static void defaultBlendFunction() {
      blendFunctionSeparate(770, 771, 1, 0);
   }

   public static void blendFunctionSeparate(int i, int j, int k, int l) {
      GlStateManager._blendFuncSeparate(i, j, k, l);
   }

   public static void enableDepthTest() {
      GlStateManager._enableDepthTest();
   }

   public static void disableDepthTest() {
      GlStateManager._disableDepthTest();
   }

   public static void setActiveTexture(int i) {
      GlStateManager._activeTexture(i);
   }

   public static void bindTexture(int i) {
      GlStateManager._bindTexture(i);
   }

   public static void bindTexture(GpuTexture gpuTexture) {
      bindTexture(FramebufferUtils.methodA(gpuTexture));
   }

   public static void clearColor(float f, float g, float h, float i) {
      GL11.glClearColor(f, g, h, i);
   }

   public static void clear(int i) {
      GL11.glClear(i);
   }
}
