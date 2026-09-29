package need.clarity.render;

import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import java.lang.reflect.Method;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public final class FramebufferUtils {
   private static int intA = 0;

   public static void methodA(RenderTarget renderTarget, RenderTarget renderTarget1, boolean bl) {
      RenderSystem.assertOnRenderThread();
      if (methodC(renderTarget)) {
         if (methodC(renderTarget1)) {
            CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
            int i = renderTarget.width;
            int m = renderTarget1.width;
            int j = Math.min(i, m);
            int k = renderTarget.height;
            int n = renderTarget1.height;
            int l = Math.min(k, n);
            GpuTexture gpuTexture = renderTarget.getColorTexture();
            GpuTexture gpuTexture1 = renderTarget1.getColorTexture();
            if (gpuTexture != null && gpuTexture1 != null) {
               commandEncoder.copyTextureToTexture(gpuTexture, gpuTexture1, 0, 0, 0, 0, 0, j, l);
            }

            if (bl) {
               GpuTexture gpuTexture2 = renderTarget.getDepthTexture();
               GpuTexture gpuTexture3 = renderTarget1.getDepthTexture();
               if (gpuTexture2 != null) {
                  if (gpuTexture3 != null) {
                     commandEncoder.copyTextureToTexture(gpuTexture2, gpuTexture3, 0, 0, 0, 0, 0, j, l);
                  }
               }
            }
         }
      }
   }

   public static void methodA(RenderTarget renderTarget) {
      RenderSystem.assertOnRenderThread();
      if (methodC(renderTarget)) {
         CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
         GpuTexture gpuTexture = renderTarget.getColorTexture();
         if (gpuTexture != null) {
            if (!gpuTexture.isClosed()) {
               commandEncoder.clearColorTexture(gpuTexture, 0);
            }
         }
      }
   }

   public static void methodB(RenderTarget renderTarget) {
      RenderSystem.assertOnRenderThread();
      if (methodC(renderTarget)) {
         methodA();
         int i = intA;
         GL30.glBindFramebuffer(36160, i);
         int j = methodA(renderTarget.getColorTexture());
         GL30.glFramebufferTexture2D(36160, 36064, 3553, j, 0);
         GpuTexture gpuTexture = renderTarget.getDepthTexture();
         int k;
         if (gpuTexture != null && !gpuTexture.isClosed()) {
            k = methodA(gpuTexture);
         } else {
            k = 0;
         }

         byte b = (byte)k;
         GL30.glFramebufferTexture2D(36160, 36096, 3553, b, 0);
         GL11.glDrawBuffer(36064);
         int l = renderTarget.width;
         int m = renderTarget.height;
         GL11.glViewport(0, 0, l, m);
      }
   }

   private static boolean methodC(RenderTarget renderTarget) {
      boolean bl;
      if (renderTarget != null && renderTarget.width > 0 && renderTarget.height > 0 && renderTarget.getColorTexture() != null) {
         bl = true;
      } else {
         bl = false;
      }

      return bl;
   }

   private static void methodA() {
      if (intA == 0) {
         intA = GL30.glGenFramebuffers();
      }
   }

   static int methodA(GpuTexture gpuTexture) {
      if (gpuTexture == null || gpuTexture.isClosed()) {
         return 0;
      }

      if (gpuTexture instanceof GlTexture) {
         return ((GlTexture)gpuTexture).glId();
      }

      try {
         Class class_x = gpuTexture.getClass();
         Class[] class_x = new Class[0];
         Method method = class_x.getMethod("glId", class_x);
         Object[] objects = new Object[0];
         return (Integer)method.invoke(gpuTexture, objects);
      } catch (Exception exception) {
         return 0;
      }
   }
}
