package need.clarity.mixin;

import need.clarity.modules.visual.Ambience;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import net.minecraft.client.renderer.state.LightmapRenderState;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightmapRenderStateExtractor.class)
public abstract class LightmapAmbienceMixin {
   @Inject(method = "extract(Lnet/minecraft/client/renderer/state/LightmapRenderState;F)V", at = @At("TAIL"))
   private void ambience$tintLightmap(LightmapRenderState lightmapRenderState, float j, CallbackInfo callbackInfo) {
      if (Ambience.isOverrideLightmapSetting()) {
         float[] f = Ambience.getLightmapColourSetting();
         float g = f[0];
         float h = f[1];
         float i = f[2];
         if (!(g >= 1.0F) || !(h >= 1.0F) || !(i >= 1.0F)) {
            tintVec(lightmapRenderState.blockLightTint, g, h, i);
            tintVec(lightmapRenderState.skyLightColor, g, h, i);
            tintVec(lightmapRenderState.ambientColor, g, h, i);
         }
      }
   }

   private static void tintVec(Vector3fc vector3fc, float f, float g, float h) {
      if (vector3fc instanceof Vector3f) {
         ((Vector3f)vector3fc).mul(f, g, h);
      }
   }
}
