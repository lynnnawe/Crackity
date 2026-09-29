package need.clarity.mixin;

import need.clarity.LocalPlayerRotationState;
import need.clarity.modules.combat.AimAssist;
import need.clarity.modules.movement.MoveFix;
import need.clarity.modules.visual.Animations;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
   @ModifyArg(
      method = "jumpFromGround",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V")
   )
   private Vec3 cl$adjustSprintJumpVelocity(Vec3 vec3) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player == null || this != minecraft.player) {
         return vec3;
      }

      if (LocalPlayerRotationState.silentActive && MoveFix.isModeSetting()) {
         double d = Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z);
         if (d < 1.0E-6) {
            return vec3;
         }

         float f = LocalPlayerRotationState.tickServerYaw * (float) (Math.PI / 180.0);
         double e = -Mth.sin(f) * d;
         double g = Mth.cos(f) * d;
         return new Vec3(e, vec3.y, g);
      } else {
         return vec3;
      }
   }

   @Redirect(method = "updateFallFlyingMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getXRot()F"))
   private float cl$redirectElytraPitch(LivingEntity livingEntity) {
      Minecraft minecraft = Minecraft.getInstance();
      return livingEntity == minecraft.player && LocalPlayerRotationState.silentActive && MoveFix.isModeSetting()
         ? LocalPlayerRotationState.tickServerPitch
         : livingEntity.getXRot();
   }

   @Redirect(
      method = "updateFallFlyingMovement",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getLookAngle()Lnet/minecraft/world/phys/Vec3;")
   )
   private Vec3 cl$redirectElytraLookVector(LivingEntity livingEntity) {
      Minecraft minecraft = Minecraft.getInstance();
      return livingEntity == minecraft.player && LocalPlayerRotationState.silentActive && MoveFix.isModeSetting()
         ? AimAssist.methodB(LocalPlayerRotationState.tickServerPitch, LocalPlayerRotationState.tickServerYaw)
         : livingEntity.getLookAngle();
   }

   @Inject(method = "getCurrentSwingDuration", at = @At("HEAD"), cancellable = true)
   private void cl$overrideSwingDuration(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null && this == minecraft.player) {
         if (Animations.methodE()) {
            callbackInfoReturnable.setReturnValue(Animations.methodF());
         }
      }
   }
}
