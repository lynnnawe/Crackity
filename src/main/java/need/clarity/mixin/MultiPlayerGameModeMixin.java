package need.clarity.mixin;

import need.clarity.modules.combat.AimAssist;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {
   @Unique
   private boolean mpgm$swapActive = false;
   @Unique
   private float mpgm$savedYRot = 0.0F;
   @Unique
   private float mpgm$savedXRot = 0.0F;

   @Inject(method = "useItem", at = @At("HEAD"))
   private void mpgm$useItemHead(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> callbackInfoReturnable) {
      if (!AimAssist.isFlagF()) {
         this.mpgm$swapActive = false;
      } else {
         float f = AimAssist.getFloatD();
         float g = AimAssist.getFloatE();
         float h = player.getYRot();
         float i = player.getXRot();
         float j = wrapDeg(f - h);
         float k = wrapDeg(g - i);
         if (Math.abs(j) < 0.05F && Math.abs(k) < 0.05F) {
            this.mpgm$swapActive = false;
         } else {
            this.mpgm$savedYRot = h;
            this.mpgm$savedXRot = i;
            this.mpgm$swapActive = true;
            AimAssist.methodA(f, g);
            player.setYRot(f);
            player.setXRot(g);
         }
      }
   }

   @Inject(method = "useItem", at = @At("RETURN"))
   private void mpgm$useItemReturn(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> callbackInfoReturnable) {
      if (this.mpgm$swapActive) {
         this.mpgm$swapActive = false;
         player.setYRot(this.mpgm$savedYRot);
         player.setXRot(this.mpgm$savedXRot);
      }
   }

   private static float wrapDeg(float f) {
      f %= 360.0F;
      if (f >= 180.0F) {
         f -= 360.0F;
      }

      if (f < -180.0F) {
         f += 360.0F;
      }

      return f;
   }
}
