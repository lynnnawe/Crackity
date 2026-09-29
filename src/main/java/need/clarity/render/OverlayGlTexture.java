package need.clarity.render;

import com.mojang.blaze3d.opengl.GlTexture;
import com.mojang.blaze3d.textures.TextureFormat;

final class OverlayGlTexture extends GlTexture {
   OverlayGlTexture(int i, int j, int k) {
      TextureFormat textureFormat = TextureFormat.RGBA8;
      super(12, "clarity-arraylist-overlay", textureFormat, j, k, 1, 1, i);
   }

   public void close() {
   }
}
