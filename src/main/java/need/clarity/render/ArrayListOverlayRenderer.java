package need.clarity.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.SamplerCache;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.util.List;
import need.clarity.modules.ModuleBase;
import need.clarity.modules.ModuleRegistry;
import need.clarity.modules.visual.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import org.lwjgl.nanovg.NVGLUFramebuffer;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL3;
import org.lwjgl.opengl.GL33C;

public final class ArrayListOverlayRenderer {
   private static final ArrayListOverlayRenderer instance;
   private static final Identifier identifierB = Identifier.fromNamespaceAndPath("clarity", "arraylist-overlay");
   private long longC;
   private boolean flagD;
   private NVGLUFramebuffer nVGLUFramebufferE;
   private int intF;
   private int intG;
   private GpuTextureView gpuTextureViewH;
   private boolean flagI;
   private int intJ;
   private boolean flagK;

   private ArrayListOverlayRenderer() {
   }

   public static void methodA(GuiGraphicsExtractor guiGraphicsExtractor, float f, float g, float h, float i) {
      if (!instance.flagD) {
         Minecraft minecraft = Minecraft.getInstance();
         if (minecraft != null) {
            if (!minecraft.options.hideGui) {
               ArrayList arrayList = ArrayList.instance;
               if (arrayList != null && arrayList.isEnabled()) {
                  try {
                     instance.methodC();
                     ArrayListOverlayRenderer arrayListOverlayRenderer = instance;
                     int k = (int)h;
                     int n = (int)i;
                     arrayListOverlayRenderer.methodA(k, n, minecraft);
                     boolean bl;
                     if (instance.flagI && instance.flagK && !instance.methodA(arrayList, f, g)) {
                        bl = false;
                     } else {
                        bl = true;
                     }

                     if (bl) {
                        float j = h / f;
                        OverlayGlState overlayGlState = OverlayGlState.methodA();

                        try {
                           long l = instance.longC;
                           NVGLUFramebuffer nVGLUFramebuffer = instance.nVGLUFramebufferE;
                           NanoVGGL3.nvgluBindFramebuffer(l, nVGLUFramebuffer);
                           int o = instance.intF;
                           int p = instance.intG;
                           GL33C.glViewport(0, 0, o, p);
                           GL33C.glDisable(3089);
                           GL33C.glColorMask(true, true, true, true);
                           GL33C.glStencilMask(255);
                           GL33C.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                           GL33C.glClearStencil(0);
                           GL33C.glClear(17408);
                           GL33C.glDisable(2929);
                           GL33C.glBindBuffer(35052, 0);
                           GL33C.glBindBuffer(35051, 0);
                           GL33C.glPixelStorei(3317, 1);
                           GL33C.glPixelStorei(3314, 0);
                           GL33C.glPixelStorei(3316, 0);
                           GL33C.glPixelStorei(3315, 0);
                           GL33C.glBindSampler(0, 0);
                           NanoVG.nvgBeginFrame(instance.longC, f, g, j);
                           ArrayList.methodA(instance.longC, f, g);
                           NanoVG.nvgEndFrame(instance.longC);
                        } catch (Throwable throwable) {
                           overlayGlState.methodB();
                           throw throwable;
                        }

                        overlayGlState.methodB();
                        instance.flagI = true;
                        ArrayListOverlayRenderer arrayListOverlayRenderer1 = instance;
                        boolean bl1 = instance.methodA(arrayList);
                        arrayListOverlayRenderer1.flagK = bl1;
                        ArrayListOverlayRenderer arrayListOverlayRenderer2 = instance;
                        int m = methodB();
                        arrayListOverlayRenderer2.intJ = m;
                     }

                     RenderPipeline renderPipeline = RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA;
                     Identifier identifier = identifierB;
                     float q = instance.intG;
                     int r = (int)f;
                     int s = (int)g;
                     int t = instance.intF;
                     int u = -instance.intG;
                     int v = instance.intF;
                     int w = instance.intG;
                     guiGraphicsExtractor.blit(renderPipeline, identifier, 0, 0, 0.0F, q, r, s, t, u, v, w);
                  } catch (Throwable throwable1) {
                     instance.flagD = true;
                  }
               } else {
                  instance.flagI = false;
               }
            }
         }
      }
   }

   public static void methodA() {
      if (instance.longC != 0L) {
         if (instance.nVGLUFramebufferE != null) {
            long l = instance.longC;
            NVGLUFramebuffer nVGLUFramebuffer = instance.nVGLUFramebufferE;
            NanoVGGL3.nvgluDeleteFramebuffer(l, nVGLUFramebuffer);
            instance.nVGLUFramebufferE = null;
         }

         NanoVGFontManager.releaseFonts();
         NanoVGGL3.nvgDelete(instance.longC);
         instance.longC = 0L;
      }

      instance.flagI = false;
   }

   private boolean methodA(ArrayList arrayList, float f, float g) {
      int i = (int)f;
      int l = this.intF;
      if (i == l) {
         int j = (int)g;
         int m = this.intG;
         if (j == m) {
            int k = methodB();
            int n = this.intJ;
            if (k != n) {
               return true;
            }

            return false;
         }
      }

      return true;
   }

   private static int methodB() {
      List list = ModuleRegistry.getModules();
      int i = 0;
      int j = 0;
      int k = 0;

      while (true) {
         int l = k;
         int n = list.size();
         if (l >= n) {
            return i;
         }

         int o = k;
         ModuleBase moduleBase = (ModuleBase)list.get(o);
         if (moduleBase.isEnabled() && !(moduleBase instanceof ArrayList)) {
            int m = i;
            int p = moduleBase.getName().hashCode() * 31;
            int r = j;
            int q = p + r;
            i = m ^ q;
            j++;
         }

         k++;
      }
   }

   private boolean methodA(ArrayList arrayList) {
      return ArrayList.methodA();
   }

   private void methodC() {
      if (this.longC == 0L) {
         long l = NanoVGGL3.nvgCreate(3);
         this.longC = l;
         if (this.longC == 0L) {
            IllegalStateException illegalStateException = new IllegalStateException("[clarity/ArrayList] nvgCreate failed");
            throw illegalStateException;
         }

         NanoVGFontManager.initializeFonts(this.longC);
      }
   }

   private void methodA(int i, int j, Minecraft minecraft) {
      if (this.nVGLUFramebufferE == null || this.intF != i || this.intG != j) {
         if (this.nVGLUFramebufferE != null) {
            long l = this.longC;
            NVGLUFramebuffer nVGLUFramebuffer1 = this.nVGLUFramebufferE;
            NanoVGGL3.nvgluDeleteFramebuffer(l, nVGLUFramebuffer1);
            this.nVGLUFramebufferE = null;
         }

         NVGLUFramebuffer nVGLUFramebuffer = NanoVGGL3.nvgluCreateFramebuffer(this.longC, i, j, 0);
         this.nVGLUFramebufferE = nVGLUFramebuffer;
         if (this.nVGLUFramebufferE == null) {
            String string = "[clarity/ArrayList] nvgluCreateFramebuffer failed at " + i + "×" + j;
            IllegalStateException illegalStateException = new IllegalStateException(string);
            throw illegalStateException;
         }

         this.intF = i;
         this.intG = j;
         this.flagI = false;
         int k = GL33C.glGetInteger(36006);
         long m = this.longC;
         NVGLUFramebuffer nVGLUFramebuffer2 = this.nVGLUFramebufferE;
         NanoVGGL3.nvgluBindFramebuffer(m, nVGLUFramebuffer2);
         GL33C.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
         GL33C.glClearStencil(0);
         GL33C.glClear(17408);
         GL33C.glBindFramebuffer(36160, k);
         this.methodA(minecraft);
      }
   }

   private void methodA(Minecraft minecraft) {
      int i = this.nVGLUFramebufferE.texture();
      int j = this.intF;
      int k = this.intG;
      OverlayGlTexture overlayGlTexture = new OverlayGlTexture(i, j, k);
      GpuTextureView gpuTextureView = RenderSystem.getDevice().createTextureView(overlayGlTexture);
      this.gpuTextureViewH = gpuTextureView;
      SamplerCache samplerCache = RenderSystem.getSamplerCache();
      FilterMode filterMode = FilterMode.LINEAR;
      GpuSampler gpuSampler = samplerCache.getClampToEdge(filterMode);
      GpuTextureView gpuTextureView1 = this.gpuTextureViewH;
      OverlayTexture overlayTexture = new OverlayTexture(overlayGlTexture, gpuTextureView1, gpuSampler);
      TextureManager textureManager = minecraft.getTextureManager();
      Identifier identifier = identifierB;
      textureManager.register(identifier, overlayTexture);
   }

   static {
      ArrayListOverlayRenderer arrayListOverlayRenderer = new ArrayListOverlayRenderer();
      instance = arrayListOverlayRenderer;
   }
}
