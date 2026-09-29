package need.clarity.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.blaze3d.vertex.MeshData.DrawState;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.blaze3d.vertex.VertexFormatElement.Type;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public final class MeshRenderer {
   private static int intA = -1;
   private static int intB = -1;
   private static int intC = -1;
   private static int intD = 0;

   public static void methodA(MeshData meshData) {
      RenderSystem.assertOnRenderThread();
      methodA();
      ByteBuffer byteBuffer = meshData.vertexBuffer();
      DrawState drawState = meshData.drawState();
      VertexFormat vertexFormat = drawState.format();
      Mode mode = drawState.mode();
      int k = drawState.vertexCount();
      GL30.glBindVertexArray(intA);
      int i = intB;
      GL15.glBindBuffer(34962, i);
      GL15.glBufferData(34962, byteBuffer, 35040);
      methodA(vertexFormat);
      methodA(mode, k);
      List list = vertexFormat.getElements();
      int j = 0;

      while (true) {
         int l = j;
         int m = list.size();
         if (l >= m) {
            GL30.glBindVertexArray(0);
            GL15.glBindBuffer(34962, 0);
            meshData.close();
            return;
         }

         GL20.glDisableVertexAttribArray(j);
         j++;
      }
   }

   private static void methodA() {
      if (intA == -1) {
         intA = GL30.glGenVertexArrays();
         intB = GL15.glGenBuffers();
         intC = GL15.glGenBuffers();
      }
   }

   private static void methodA(VertexFormat vertexFormat) {
      List list = vertexFormat.getElements();
      List list1 = vertexFormat.getElementAttributeNames();
      int k = vertexFormat.getVertexSize();
      int[] l = vertexFormat.getOffsetsByElement();
      int j = 0;

      while (true) {
         int m = j;
         int o = list.size();
         if (m >= o) {
            return;
         }

         int p = j;
         VertexFormatElement vertexFormatElement = (VertexFormatElement)list.get(p);
         int q = j;
         String string = (String)list1.get(q);
         int n = methodA(vertexFormatElement, string);
         if (n >= 0) {
            GL20.glEnableVertexAttribArray(n);
            int r = vertexFormatElement.count();
            int s = methodA(vertexFormatElement.type());
            boolean bl = methodA(vertexFormatElement);
            int i = j;
            long t = l[i];
            GL20.glVertexAttribPointer(n, r, s, bl, k, t);
         }

         j++;
      }
   }

   private static int methodA(VertexFormatElement vertexFormatElement, String string) {
      VertexFormatElement vertexFormatElement1 = VertexFormatElement.POSITION;
      if (vertexFormatElement == vertexFormatElement1) {
         return 0;
      }

      VertexFormatElement vertexFormatElement2 = VertexFormatElement.UV0;
      if (vertexFormatElement == vertexFormatElement2) {
         return 1;
      }

      VertexFormatElement vertexFormatElement3 = VertexFormatElement.COLOR;
      if (vertexFormatElement == vertexFormatElement3) {
         return 2;
      }

      VertexFormatElement vertexFormatElement4 = VertexFormatElement.NORMAL;
      return vertexFormatElement == vertexFormatElement4 ? 3 : GL20.glGetAttribLocation(GL11.glGetInteger(35725), string);
   }

   private static void methodA(Mode mode, int i) {
      Mode mode1 = Mode.QUADS;
      if (mode == mode1) {
         int k = i / 4;
         methodA(k);
         int m = intC;
         GL15.glBindBuffer(34963, m);
         int n = k * 6;
         GL11.glDrawElements(4, n, 5125, 0L);
         GL15.glBindBuffer(34963, 0);
      } else {
         int[] j = MeshEnumSwitches.intArrayA;
         int l = mode.ordinal();

         GL11.glDrawArrays(switch (j[l]) {
            case 1 -> 4;
            case 2 -> 5;
            case 3 -> 6;
            case 4 -> 1;
            default -> 4;
         }, 0, i);
      }
   }

   private static void methodA(int i) {
      int j = intD;
      if (i > j) {
         int n = intD * 2;
         int l = Math.max(i, n);
         IntBuffer intBuffer = MemoryUtil.memAllocInt(l * 6);

         for (int k = 0; k < l; k++) {
            int m = k * 4;
            intBuffer.put(m);
            int p = m + 1;
            intBuffer.put(p);
            int q = m + 2;
            intBuffer.put(q);
            int r = m + 2;
            intBuffer.put(r);
            int s = m + 3;
            intBuffer.put(s);
            intBuffer.put(m);
         }

         intBuffer.flip();
         int o = intC;
         GL15.glBindBuffer(34963, o);
         GL15.glBufferData(34963, intBuffer, 35044);
         GL15.glBindBuffer(34963, 0);
         MemoryUtil.memFree(intBuffer);
         intD = l;
      }
   }

   private static int methodA(Type type) {
      int[] i = MeshEnumSwitches.intArrayB;
      int j = type.ordinal();

      return switch (i[j]) {
         case 1 -> 5126;
         case 2, 3 -> 5121;
         case 4, 5 -> 5123;
         case 6, 7 -> 5125;
         default -> {
            MatchException matchException = new MatchException(null, null);
            throw matchException;
         }
      };
   }

   private static boolean methodA(VertexFormatElement vertexFormatElement) {
      VertexFormatElement vertexFormatElement1 = VertexFormatElement.COLOR;
      if (vertexFormatElement != vertexFormatElement1) {
         VertexFormatElement vertexFormatElement2 = VertexFormatElement.NORMAL;
         if (vertexFormatElement != vertexFormatElement2) {
            return false;
         }
      }

      return true;
   }
}
