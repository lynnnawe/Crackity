package need.clarity.render;

import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.blaze3d.vertex.VertexFormatElement.Type;

// $VF: synthetic class
class MeshEnumSwitches {
   // $VF: synthetic field
   static final int[] intArrayA;
   // $VF: synthetic field
   static final int[] intArrayB = new int[Type.values().length];

   static {
      try {
         int[] i = intArrayB;
         int t = Type.FLOAT.ordinal();
         i[t] = 1;
      } catch (NoSuchFieldError noSuchFieldError10) {
      }

      try {
         int[] j = intArrayB;
         int u = Type.UBYTE.ordinal();
         j[u] = 2;
      } catch (NoSuchFieldError noSuchFieldError9) {
      }

      try {
         int[] k = intArrayB;
         int v = Type.BYTE.ordinal();
         k[v] = 3;
      } catch (NoSuchFieldError noSuchFieldError8) {
      }

      try {
         int[] l = intArrayB;
         int w = Type.USHORT.ordinal();
         l[w] = 4;
      } catch (NoSuchFieldError noSuchFieldError7) {
      }

      try {
         int[] m = intArrayB;
         int x = Type.SHORT.ordinal();
         m[x] = 5;
      } catch (NoSuchFieldError noSuchFieldError6) {
      }

      try {
         int[] n = intArrayB;
         int y = Type.UINT.ordinal();
         n[y] = 6;
      } catch (NoSuchFieldError noSuchFieldError5) {
      }

      try {
         int[] o = intArrayB;
         int z = Type.INT.ordinal();
         o[z] = 7;
      } catch (NoSuchFieldError noSuchFieldError4) {
      }

      intArrayA = new int[Mode.values().length];

      try {
         int[] p = intArrayA;
         int ba = Mode.TRIANGLES.ordinal();
         p[ba] = 1;
      } catch (NoSuchFieldError noSuchFieldError3) {
      }

      try {
         int[] q = intArrayA;
         int bb = Mode.TRIANGLE_STRIP.ordinal();
         q[bb] = 2;
      } catch (NoSuchFieldError noSuchFieldError2) {
      }

      try {
         int[] r = intArrayA;
         int bc = Mode.TRIANGLE_FAN.ordinal();
         r[bc] = 3;
      } catch (NoSuchFieldError noSuchFieldError1) {
      }

      try {
         int[] s = intArrayA;
         int bd = Mode.LINES.ordinal();
         s[bd] = 4;
      } catch (NoSuchFieldError noSuchFieldError) {
      }
   }
}
