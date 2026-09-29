package need.clarity.render;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL33C;
import org.lwjgl.system.MemoryStack;

final class OverlayGlState {
   int intA;
   int intB;
   int intC;
   int intD;
   int intE;
   int intF;
   int intG;
   int intH;
   int intI;
   int intJ;
   int intK;
   int intL;
   int intM;
   int intN;
   int intO;
   int intP;
   int intQ;
   int intR;
   boolean flagS;
   boolean flagT;
   boolean flagU;
   boolean flagV;
   boolean flagW;
   int intX;
   int intY;
   int intZ;
   int intA_A;
   int intB_B;
   int intC_C;
   int intD_D;
   int intE_E;
   int intF_F;
   int intG_G;
   int intH_H;
   int intI_I;
   int intJ_J;
   int intK_K;
   int intL_L;
   final byte[] byteArrayM;

   private OverlayGlState() {
      byte[] b = new byte[4];
      this.byteArrayM = b;
   }

   static OverlayGlState methodA() {
      OverlayGlState overlayGlState = new OverlayGlState();
      int i = GL33C.glGetInteger(35725);
      overlayGlState.intA = i;
      int j = GL33C.glGetInteger(34229);
      overlayGlState.intB = j;
      int k = GL33C.glGetInteger(34964);
      overlayGlState.intC = k;
      int l = GL33C.glGetInteger(35368);
      overlayGlState.intD = l;
      int m = GL33C.glGetInteger(34016);
      overlayGlState.intE = m;
      int n = GL33C.glGetInteger(32873);
      overlayGlState.intF = n;
      int o = GL33C.glGetInteger(36006);
      overlayGlState.intG = o;
      int p = GL33C.glGetInteger(35055);
      overlayGlState.intH = p;
      int q = GL33C.glGetInteger(35053);
      overlayGlState.intI = q;
      int r = GL33C.glGetInteger(3317);
      overlayGlState.intK = r;
      int s = GL33C.glGetInteger(3314);
      overlayGlState.intL = s;
      int t = GL33C.glGetInteger(3316);
      overlayGlState.intM = t;
      int u = GL33C.glGetInteger(3315);
      overlayGlState.intN = u;
      GL33C.glActiveTexture(33984);
      int v = GL33C.glGetInteger(35097);
      overlayGlState.intJ = v;
      GL33C.glActiveTexture(overlayGlState.intE);
      MemoryStack memoryStack = MemoryStack.stackPush();

      try {
         IntBuffer intBuffer = memoryStack.mallocInt(4);
         GL33C.glGetIntegerv(2978, intBuffer);
         int w = intBuffer.get(0);
         overlayGlState.intO = w;
         int x = intBuffer.get(1);
         overlayGlState.intP = x;
         int y = intBuffer.get(2);
         overlayGlState.intQ = y;
         int z = intBuffer.get(3);
         overlayGlState.intR = z;
      } catch (Throwable throwable3) {
         if (memoryStack != null) {
            try {
               memoryStack.close();
            } catch (Throwable throwable1) {
               throwable3.addSuppressed(throwable1);
            }
         }

         throw throwable3;
      }

      if (memoryStack != null) {
         memoryStack.close();
      }

      boolean bl = GL33C.glIsEnabled(3042);
      overlayGlState.flagS = bl;
      boolean bl1 = GL33C.glIsEnabled(2929);
      overlayGlState.flagT = bl1;
      boolean bl2 = GL33C.glIsEnabled(2884);
      overlayGlState.flagU = bl2;
      boolean bl3 = GL33C.glIsEnabled(2960);
      overlayGlState.flagV = bl3;
      boolean bl4 = GL33C.glIsEnabled(3089);
      overlayGlState.flagW = bl4;
      int ba = GL33C.glGetInteger(32969);
      overlayGlState.intX = ba;
      int bb = GL33C.glGetInteger(32968);
      overlayGlState.intY = bb;
      int bc = GL33C.glGetInteger(32971);
      overlayGlState.intZ = bc;
      int bd = GL33C.glGetInteger(32970);
      overlayGlState.intA_A = bd;
      int be = GL33C.glGetInteger(32777);
      overlayGlState.intB_B = be;
      int bf = GL33C.glGetInteger(34877);
      overlayGlState.intC_C = bf;
      int bg = GL33C.glGetInteger(2885);
      overlayGlState.intD_D = bg;
      int bh = GL33C.glGetInteger(2886);
      overlayGlState.intE_E = bh;
      int bi = GL33C.glGetInteger(2968);
      overlayGlState.intF_F = bi;
      int bj = GL33C.glGetInteger(2962);
      overlayGlState.intG_G = bj;
      int bk = GL33C.glGetInteger(2967);
      overlayGlState.intH_H = bk;
      int bm = GL33C.glGetInteger(2963);
      overlayGlState.intI_I = bm;
      int bn = GL33C.glGetInteger(2964);
      overlayGlState.intJ_J = bn;
      int bo = GL33C.glGetInteger(2965);
      overlayGlState.intK_K = bo;
      int bp = GL33C.glGetInteger(2966);
      overlayGlState.intL_L = bp;
      MemoryStack memoryStack1 = MemoryStack.stackPush();

      try {
         ByteBuffer byteBuffer = memoryStack1.malloc(4);
         GL33C.glGetBooleanv(3107, byteBuffer);
         byte[] b = overlayGlState.byteArrayM;
         byteBuffer.get(b);
      } catch (Throwable throwable2) {
         if (memoryStack1 != null) {
            try {
               memoryStack1.close();
            } catch (Throwable throwable) {
               throwable2.addSuppressed(throwable);
            }
         }

         throw throwable2;
      }

      if (memoryStack1 != null) {
         memoryStack1.close();
      }

      return overlayGlState;
   }

   void methodB() {
      int n = this.intG;
      GL33C.glBindFramebuffer(36160, n);
      int i = this.intO;
      int o = this.intP;
      int bd = this.intQ;
      int bh = this.intR;
      GL33C.glViewport(i, o, bd, bh);
      GL33C.glUseProgram(this.intA);
      GL33C.glBindVertexArray(this.intB);
      int p = this.intC;
      GL33C.glBindBuffer(34962, p);
      int q = this.intD;
      GL33C.glBindBuffer(35345, q);
      GL33C.glActiveTexture(this.intE);
      int r = this.intF;
      GL33C.glBindTexture(3553, r);
      int s = this.intH;
      GL33C.glBindBuffer(35052, s);
      int t = this.intI;
      GL33C.glBindBuffer(35051, t);
      int u = this.intK;
      GL33C.glPixelStorei(3317, u);
      int v = this.intL;
      GL33C.glPixelStorei(3314, v);
      int w = this.intM;
      GL33C.glPixelStorei(3316, w);
      int x = this.intN;
      GL33C.glPixelStorei(3315, x);
      int y = this.intJ;
      GL33C.glBindSampler(0, y);
      boolean bl1 = this.flagS;
      methodA(3042, bl1);
      boolean bl2 = this.flagT;
      methodA(2929, bl2);
      boolean bl3 = this.flagU;
      methodA(2884, bl3);
      boolean bl4 = this.flagV;
      methodA(2960, bl4);
      boolean bl5 = this.flagW;
      methodA(3089, bl5);
      int j = this.intX;
      int z = this.intY;
      int be = this.intZ;
      int bi = this.intA_A;
      GL33C.glBlendFuncSeparate(j, z, be, bi);
      int k = this.intB_B;
      int ba = this.intC_C;
      GL33C.glBlendEquationSeparate(k, ba);
      GL33C.glCullFace(this.intD_D);
      GL33C.glFrontFace(this.intE_E);
      GL33C.glStencilMask(this.intF_F);
      int l = this.intG_G;
      int bb = this.intH_H;
      int bf = this.intI_I;
      GL33C.glStencilFunc(l, bb, bf);
      int m = this.intJ_J;
      int bc = this.intK_K;
      int bg = this.intL_L;
      GL33C.glStencilOp(m, bc, bg);
      boolean bl;
      if (this.byteArrayM[0] != 0) {
         bl = true;
      } else {
         bl = false;
      }

      boolean bl6;
      if (this.byteArrayM[1] != 0) {
         bl6 = true;
      } else {
         bl6 = false;
      }

      boolean bl7;
      if (this.byteArrayM[2] != 0) {
         bl7 = true;
      } else {
         bl7 = false;
      }

      boolean bl8;
      if (this.byteArrayM[3] != 0) {
         bl8 = true;
      } else {
         bl8 = false;
      }

      GL33C.glColorMask(bl, bl6, bl7, bl8);
   }

   private static void methodA(int i, boolean bl) {
      if (bl) {
         GL33C.glEnable(i);
      } else {
         GL33C.glDisable(i);
      }
   }
}
