package need.clarity.mixin;

import need.clarity.modules.visual.Ambience;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public abstract class FogAmbienceMixin {
   @Inject(
      method = "setupFog(Lnet/minecraft/client/Camera;ILnet/minecraft/client/DeltaTracker;FLnet/minecraft/client/multiplayer/ClientLevel;)Lnet/minecraft/client/renderer/fog/FogData;",
      at = @At("RETURN"),
      cancellable = true
   )
   private void ambience$overrideFog(
      Camera camera, int i, DeltaTracker deltaTracker, float h, ClientLevel clientLevel, CallbackInfoReturnable<FogData> callbackInfoReturnable
   ) {
      FogData fogData = (FogData)callbackInfoReturnable.getReturnValue();
      if (fogData != null) {
         if (Ambience.isOverrideFogColourSetting()) {
            float[] f = Ambience.getFogColourSetting();
            fogData.color.set(f[0], f[1], f[2], 1.0F);
         }

         if (Ambience.isOverrideFogDistanceSetting()) {
            float g = Ambience.getFogDistanceSetting() * 16.0F;
            fogData.environmentalStart = 0.0F;
            fogData.environmentalEnd = g;
            fogData.renderDistanceStart = 0.0F;
            fogData.renderDistanceEnd = g;
            fogData.skyEnd = g;
            fogData.cloudEnd = g;
         }

         callbackInfoReturnable.setReturnValue(fogData);
      }
   }
}
