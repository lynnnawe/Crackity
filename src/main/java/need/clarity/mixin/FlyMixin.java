package need.clarity.mixin;

import need.clarity.modules.movement.Fly;
import net.minecraft.client.Minecraft;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class FlyMixin {
   @Inject(method = "travel", at = @At("HEAD"))
   private void cl$flyNoGravity(Vec3 vec3, CallbackInfo callbackInfo) {
      if (Fly.methodA()) {
         Minecraft minecraft = Minecraft.getInstance();
         if (minecraft.player != null && this == minecraft.player) {
            LivingEntity livingEntity = (LivingEntity)this;
            if (livingEntity.getDeltaMovement().y < 0.0 && !Fly.methodA(minecraft) && !Fly.methodB(minecraft)) {
               livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().x, 0.0, livingEntity.getDeltaMovement().z);
            }
         }
      }
   }

   @Inject(method = "travel", at = @At("RETURN"))
   private void cl$flyApplyVelocity(Vec3 vec31, CallbackInfo callbackInfo) {
      if (Fly.methodA() && Fly.flagD) {
         Minecraft minecraft = Minecraft.getInstance();
         if (minecraft.player != null && this == minecraft.player) {
            LivingEntity livingEntity = (LivingEntity)this;
            String string = Fly.getCreativeSetting();
            if ("Full".equals(string)) {
               livingEntity.setDeltaMovement(Fly.doubleA, Fly.doubleB, Fly.doubleC);
            } else {
               Vec3 vec3 = livingEntity.getDeltaMovement();
               livingEntity.setDeltaMovement(vec3.x * 0.91 + Fly.doubleA, Fly.doubleB, vec3.z * 0.91 + Fly.doubleC);
            }

            Fly.flagD = false;
         }
      }
   }

   @Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
   private void cl$flyNoFallDamage(double d, float f, DamageSource damageSource, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
      if (Fly.methodA()) {
         Minecraft minecraft = Minecraft.getInstance();
         if (minecraft.player != null && this == minecraft.player) {
            callbackInfoReturnable.setReturnValue(false);
         }
      }
   }
}
