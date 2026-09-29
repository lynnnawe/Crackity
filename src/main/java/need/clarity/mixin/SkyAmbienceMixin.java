package need.clarity.mixin;

import need.clarity.modules.visual.Ambience;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkyRenderer.class)
public abstract class SkyAmbienceMixin {
   @Inject(
      method = "extractRenderState(Lnet/minecraft/client/multiplayer/ClientLevel;FLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/state/level/SkyRenderState;)V",
      at = @At("TAIL")
   )
   private void ambience$overrideSkyColor(ClientLevel clientLevel, float n, Camera camera, SkyRenderState skyRenderState, CallbackInfo callbackInfo) {
      if (Ambience.isOverrideTimeSetting()) {
         long l = Ambience.getTimeOfDaySetting();
         float g = (float)l / 24000.0F;
         float h = g * 360.0F;
         float i = h * (float) (Math.PI / 180.0);
         skyRenderState.sunAngle = i;
         skyRenderState.moonAngle = i + (float) Math.PI;
         skyRenderState.starAngle = i;
      }

      if (Ambience.isOverrideSkyColourSetting()) {
         float[] j = Ambience.getSkyColourSetting();
         if (!Ambience.isSkyGradientSetting()) {
            skyRenderState.skyColor = toArgb(j[0], j[1], j[2]);
         } else {
            float[] f = Ambience.getSkyGradientColour2Setting();
            float k = camera.xRot();
            float m = (float)Math.max(0.0, Math.min(1.0, -k / 90.0));
            skyRenderState.skyColor = toArgb(lerp(j[0], f[0], m), lerp(j[1], f[1], m), lerp(j[2], f[2], m));
         }
      }
   }

   private static int toArgb(float f, float g, float h) {
      int i = Math.max(0, Math.min(255, (int)(f * 255.0F)));
      int j = Math.max(0, Math.min(255, (int)(g * 255.0F)));
      int k = Math.max(0, Math.min(255, (int)(h * 255.0F)));
      return 0xFF000000 | i << 16 | j << 8 | k;
   }

   private static float lerp(float f, float g, float h) {
      return f + (g - f) * h;
   }
}
