package need.clarity.render;

import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public final class GlStateSnapshot implements AutoCloseable {
   private final int intA;
   private final int intB;
   private final int intC;
   private final int intD;
   private final int intE;
   private final int intF;
   private final int intG;
   private final int intH;
   private final int intI;
   private final int intJ;
   private final int intK;
   private final int intL;
   private final int intM;
   private final int intN;
   private final int[] intArrayO;
   private final boolean flagP;
   private final boolean flagQ;
   private final boolean flagR;
   private final boolean flagS;
   private final boolean flagT;
   private final boolean flagU;
   private final boolean flagV;
   private final boolean flagW;
   private final boolean flagX;
   private final int intY;
   private final int intZ;
   private final int intA_A;
   private final int intB_B;
   private final int intC_C;
   private final int intD_D;
   private final int intE_E;
   private final int intF_F;
   private final int intG_G;
   private final float floatH;
   private final float floatI;
   private final float floatJ;
   private final float floatK;
   private final GpuTextureView gpuTextureViewL;
   private final GpuTextureView gpuTextureViewM;
   private boolean flagN;

   private GlStateSnapshot() {
      int[] k = new int[3];
      this.intArrayO = k;
      RenderSystem.assertOnRenderThread();
      int l = GL11.glGetInteger(36006);
      this.intA = l;
      int m = GL11.glGetInteger(36010);
      this.intB = m;
      int n = GL11.glGetInteger(35725);
      this.intK = n;
      int o = GL11.glGetInteger(34229);
      this.intL = o;
      int p = GL11.glGetInteger(34964);
      this.intM = p;
      IntBuffer intBuffer = BufferUtils.createIntBuffer(4);
      GL11.glGetIntegerv(2978, intBuffer);
      int q = intBuffer.get(0);
      this.intC = q;
      int r = intBuffer.get(1);
      this.intD = r;
      int s = intBuffer.get(2);
      this.intE = s;
      int t = intBuffer.get(3);
      this.intF = t;
      intBuffer.clear();
      GL11.glGetIntegerv(3088, intBuffer);
      int u = intBuffer.get(0);
      this.intG = u;
      int v = intBuffer.get(1);
      this.intH = v;
      int w = intBuffer.get(2);
      this.intI = w;
      int x = intBuffer.get(3);
      this.intJ = x;
      int y = GL11.glGetInteger(34016);
      this.intN = y;

      for (int i = 0; i < 3; i++) {
         int bj = i;
         GlStateManager._activeTexture(33984 + bj);
         int[] j = this.intArrayO;
         int bk = i;
         int bm = GL11.glGetInteger(32873);
         j[bk] = bm;
      }

      GlStateManager._activeTexture(this.intN);
      boolean bl = GL11.glIsEnabled(3042);
      this.flagP = bl;
      boolean bl1 = GL11.glIsEnabled(2929);
      this.flagQ = bl1;
      boolean bl2 = GL11.glIsEnabled(2884);
      this.flagR = bl2;
      boolean bl3 = GL11.glIsEnabled(3089);
      this.flagS = bl3;
      boolean bl4 = GL11.glGetBoolean(2930);
      this.flagT = bl4;
      ByteBuffer byteBuffer = BufferUtils.createByteBuffer(4);
      GL11.glGetBooleanv(3107, byteBuffer);
      boolean bl8;
      if (byteBuffer.get(0) != 0) {
         bl8 = true;
      } else {
         bl8 = false;
      }

      this.flagU = bl8;
      boolean bl5;
      if (byteBuffer.get(1) != 0) {
         bl5 = true;
      } else {
         bl5 = false;
      }

      this.flagV = bl5;
      boolean bl6;
      if (byteBuffer.get(2) != 0) {
         bl6 = true;
      } else {
         bl6 = false;
      }

      this.flagW = bl6;
      boolean bl7;
      if (byteBuffer.get(3) != 0) {
         bl7 = true;
      } else {
         bl7 = false;
      }

      this.flagX = bl7;
      int z = GL11.glGetInteger(32969);
      this.intY = z;
      int ba = GL11.glGetInteger(32968);
      this.intZ = ba;
      int bb = GL11.glGetInteger(32971);
      this.intA_A = bb;
      int bc = GL11.glGetInteger(32970);
      this.intB_B = bc;
      int bd = GL11.glGetInteger(32777);
      this.intC_C = bd;
      int be = GL11.glGetInteger(34877);
      this.intD_D = be;
      int bf = GL11.glGetInteger(2932);
      this.intE_E = bf;
      int bg = GL11.glGetInteger(2885);
      this.intF_F = bg;
      int bh = GL11.glGetInteger(2886);
      this.intG_G = bh;
      FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(4);
      GL11.glGetFloatv(3106, floatBuffer);
      float f = floatBuffer.get(0);
      this.floatH = f;
      float g = floatBuffer.get(1);
      this.floatI = g;
      float h = floatBuffer.get(2);
      this.floatJ = h;
      float bi = floatBuffer.get(3);
      this.floatK = bi;
      GpuTextureView gpuTextureView = RenderSystem.outputColorTextureOverride;
      this.gpuTextureViewL = gpuTextureView;
      GpuTextureView gpuTextureView1 = RenderSystem.outputDepthTextureOverride;
      this.gpuTextureViewM = gpuTextureView1;
   }

   public static GlStateSnapshot capture() {
      return new GlStateSnapshot();
   }

   public void restore() {
      if (!this.flagN) {
         this.flagN = true;
         int o = this.intA;
         GlStateManager._glBindFramebuffer(36009, o);
         int p = this.intB;
         GlStateManager._glBindFramebuffer(36008, p);
         int j = this.intC;
         int q = this.intD;
         int x = this.intE;
         int ba = this.intF;
         GlStateManager._viewport(j, q, x, ba);
         int k = this.intG;
         int r = this.intH;
         int y = this.intI;
         int bb = this.intJ;
         GlStateManager._scissorBox(k, r, y, bb);

         for (int i = 0; i < 3; i++) {
            int v = i;
            GlStateManager._activeTexture(33984 + v);
            int[] n = this.intArrayO;
            int w = i;
            GlStateManager._bindTexture(n[w]);
         }

         GlStateManager._activeTexture(this.intN);
         int l = this.intY;
         int s = this.intZ;
         int z = this.intA_A;
         int bc = this.intB_B;
         GlStateManager._blendFuncSeparate(l, s, z, bc);
         int m = this.intC_C;
         int t = this.intD_D;
         GL20.glBlendEquationSeparate(m, t);
         GlStateManager._depthFunc(this.intE_E);
         GlStateManager._depthMask(this.flagT);
         boolean bl = this.flagU;
         boolean bl1 = this.flagV;
         boolean bl2 = this.flagW;
         boolean bl3 = this.flagX;
         GL11.glColorMask(bl, bl1, bl2, bl3);
         GL11.glCullFace(this.intF_F);
         GL11.glFrontFace(this.intG_G);
         float f = this.floatH;
         float g = this.floatI;
         float h = this.floatJ;
         float bd = this.floatK;
         GL11.glClearColor(f, g, h, bd);
         methodA(this.flagP);
         methodB(this.flagQ);
         methodC(this.flagR);
         methodD(this.flagS);
         GL20.glUseProgram(this.intK);
         GL30.glBindVertexArray(this.intL);
         int u = this.intM;
         GL15.glBindBuffer(34962, u);
         RenderSystem.outputColorTextureOverride = this.gpuTextureViewL;
         RenderSystem.outputDepthTextureOverride = this.gpuTextureViewM;
      }
   }

   private static void methodA(boolean bl) {
      if (bl) {
         GlStateManager._enableBlend();
      } else {
         GlStateManager._disableBlend();
      }
   }

   private static void methodB(boolean bl) {
      if (bl) {
         GlStateManager._enableDepthTest();
      } else {
         GlStateManager._disableDepthTest();
      }
   }

   private static void methodC(boolean bl) {
      if (bl) {
         GlStateManager._enableCull();
      } else {
         GlStateManager._disableCull();
      }
   }

   private static void methodD(boolean bl) {
      if (bl) {
         GlStateManager._enableScissorTest();
      } else {
         GlStateManager._disableScissorTest();
      }
   }
}
