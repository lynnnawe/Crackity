from pathlib import Path
import sys
p=Path(sys.argv[1]) / 'need/clarity/render/NanoVGFontManager.java';s=p.read_text()
a=s.index('   // $VF: Could not properly define all variable types!')
b=s.index('\n   static {',a)
s=s[:a]+'''   // Reconstructed from the verified bytecode: preserve allocation, put, flip and resource closure.
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
''' + s[b:]
p.write_text(s)
