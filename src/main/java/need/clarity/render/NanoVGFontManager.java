package need.clarity.render;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.system.MemoryUtil;

public final class NanoVGFontManager {
   private static final List<ByteBuffer> fontBuffers;
   private static boolean fontsInitialized = false;

   private NanoVGFontManager() {
   }

   public static void initializeFonts(long l) {
      if (!fontsInitialized) {
         registerFont(l, "inter", "Inter-Regular");
         registerFont(l, "inter-med", "Inter-Medium");
         registerFont(l, "inter-sb", "Inter-SemiBold");
         fontsInitialized = true;
      }
   }

   public static void releaseFonts() {
      Iterator iterator = fontBuffers.iterator();

      while (iterator.hasNext()) {
         MemoryUtil.memFree((ByteBuffer)iterator.next());
      }

      fontBuffers.clear();
      fontsInitialized = false;
   }

   private static void registerFont(long l, String string, String string1) {
      ByteBuffer byteBuffer2 = readFontResource("/fonts/" + string1 + ".ttf");
      if (byteBuffer2 == null) {
         byteBuffer2 = readFontResource("/assets/clarity/fonts/" + string1 + ".ttf");
      }

      if (byteBuffer2 != null) {
         List list = fontBuffers;
         ByteBuffer byteBuffer = byteBuffer2;
         list.add(byteBuffer);
         ByteBuffer byteBuffer1 = byteBuffer2;
         NanoVG.nvgCreateFontMem(l, string, byteBuffer1, false);
      }
   }

   // Reconstructed from the verified bytecode: preserve allocation, put, flip and resource closure.
   private static ByteBuffer readFontResource(String resourcePath) {
      try (InputStream input = NanoVGFontManager.class.getResourceAsStream(resourcePath)) {
         if (input == null) {
            return null;
         }
         byte[] bytes = input.readAllBytes();
         ByteBuffer buffer = MemoryUtil.memAlloc(bytes.length);
         buffer.put(bytes).flip();
         return buffer;
      } catch (IOException exception) {
         return null;
      }
   }

   static {
      ArrayList arrayList = new ArrayList();
      fontBuffers = arrayList;
   }
}
