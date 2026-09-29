package need.clarity.mixin;

import need.clarity.LocalPlayerRotationState;
import need.clarity.modules.combat.AimAssist;
import need.clarity.modules.visual.ServerRotations;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {
   @Shadow
   public float yBob;
   @Shadow
   public float yBobO;
   @Shadow
   public float xBob;
   @Shadow
   public float xBobO;
   @Unique
   private boolean cl$silentApplied;
   @Unique
   private float cl$tickServerYaw;
   @Unique
   private float cl$tickServerPitch;
   @Unique
   private float cl$sYaw;
   @Unique
   private float cl$sPitch;
   @Unique
   private float cl$sRenderYaw;
   @Unique
   private float cl$sRenderPitch;
   @Unique
   private float cl$sHeadYaw;
   @Unique
   private float cl$sBodyYaw;
   @Unique
   private static final float cl$BODY_FOLLOW = 0.22F;
   @Unique
   private static final float cl$MAX_HEAD_YAW = 50.0F;

   @Inject(method = "tick", at = @At("HEAD"))
   private void cl$silentRotationHead(CallbackInfo callbackInfo) {
      this.cl$silentApplied = false;
      LocalPlayerRotationState.silentActive = false;
      if (AimAssist.isFlagF()) {
         LocalPlayer localPlayer = (LocalPlayer)this;
         this.cl$tickServerYaw = AimAssist.getFloatD();
         this.cl$tickServerPitch = Mth.clamp(AimAssist.getFloatE(), -90.0F, 90.0F);
         this.cl$sYaw = localPlayer.getYRot();
         this.cl$sPitch = localPlayer.getXRot();
         this.cl$sRenderYaw = this.yBob;
         this.cl$sRenderPitch = this.xBob;
         this.cl$sHeadYaw = localPlayer.yHeadRot;
         this.cl$sBodyYaw = localPlayer.yBodyRot;
         LocalPlayerRotationState.tickServerYaw = this.cl$tickServerYaw;
         LocalPlayerRotationState.tickServerPitch = this.cl$tickServerPitch;
         LocalPlayerRotationState.realYaw = this.cl$sYaw;
         LocalPlayerRotationState.silentActive = true;
         ServerRotations.methodA(ServerRotations.methodC(1.0F));
         localPlayer.setYRot(this.cl$tickServerYaw);
         localPlayer.setXRot(this.cl$tickServerPitch);
         ServerRotations.methodB(this.cl$tickServerPitch);
         this.cl$silentApplied = true;
      }
   }

   @Inject(method = "tick", at = @At("RETURN"))
   private void cl$silentRotationReturn(CallbackInfo callbackInfo) {
      LocalPlayerRotationState.silentActive = false;
      if (this.cl$silentApplied) {
         this.cl$silentApplied = false;
         LocalPlayer localPlayer = (LocalPlayer)this;
         localPlayer.setYRot(this.cl$sYaw);
         localPlayer.setXRot(this.cl$sPitch);
         localPlayer.yRotO = this.cl$sYaw;
         localPlayer.xRotO = this.cl$sPitch;
         float f = ServerRotations.methodA() ? this.cl$tickServerYaw : this.cl$sYaw;
         localPlayer.yHeadRotO = this.cl$sHeadYaw;
         localPlayer.yHeadRot = f;
         float g = this.cl$sBodyYaw + Mth.wrapDegrees(f - this.cl$sBodyYaw) * 0.22F;
         float h = Mth.wrapDegrees(f - g);
         if (h > 50.0F) {
            g = f - 50.0F;
         } else if (h < -50.0F) {
            g = f + 50.0F;
         }

         localPlayer.yBodyRotO = this.cl$sBodyYaw;
         localPlayer.yBodyRot = g;
         this.yBobO = this.cl$sRenderYaw;
         this.xBobO = this.cl$sRenderPitch;
         this.yBob = this.cl$sRenderYaw + (this.cl$sYaw - this.cl$sRenderYaw) * 0.5F;
         this.xBob = this.cl$sRenderPitch + (this.cl$sPitch - this.cl$sRenderPitch) * 0.5F;
      }
   }
}
