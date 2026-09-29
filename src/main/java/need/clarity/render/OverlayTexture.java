package need.clarity.render;

import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.renderer.texture.AbstractTexture;

final class OverlayTexture extends AbstractTexture {
   OverlayTexture(GpuTexture gpuTexture, GpuTextureView gpuTextureView, GpuSampler gpuSampler) {
      this.texture = gpuTexture;
      this.textureView = gpuTextureView;
      this.sampler = gpuSampler;
   }

   public void close() {
   }
}
