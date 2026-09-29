package need.clarity.mixin;

import need.clarity.modules.visual.CameraTweaks;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraTweaksMixin {
   @Shadow
   private @Nullable Entity entity;

   @Shadow
   protected abstract void move(float f, float g, float h);

   @Shadow
   protected abstract float getMaxZoom(float f);

   @Inject(method = "alignWithEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getMaxZoom(F)F"), cancellable = true)
   private void clarity$cameraTweaks(float j, CallbackInfo callbackInfo) {
      if (CameraTweaks.methodA()) {
         Minecraft minecraft = Minecraft.getInstance();
         if (minecraft != null && !minecraft.options.getCameraType().isFirstPerson()) {
            float f = 1.0F;
            if (this.entity instanceof LivingEntity livingEntity) {
               f = livingEntity.getScale() * (float)livingEntity.getAttributeValue(Attributes.CAMERA_DISTANCE) / 4.0F;
            }

            if (this.entity != null && this.entity.isPassenger() && this.entity.getVehicle() instanceof LivingEntity livingEntity1) {
               float h = livingEntity1.getScale() * (float)livingEntity1.getAttributeValue(Attributes.CAMERA_DISTANCE);
               if (h / 4.0F > f) {
                  f = h / 4.0F;
               }
            }

            float g = CameraTweaks.isCameraDistanceSetting() ? CameraTweaks.getDistanceSetting() : 4.0F;
            float i = g * f;
            boolean bl = CameraTweaks.isNoCameraClipSetting();
            this.move(bl ? -i : -this.getMaxZoom(i), 0.0F, 0.0F);
            callbackInfo.cancel();
         }
      }
   }
}
