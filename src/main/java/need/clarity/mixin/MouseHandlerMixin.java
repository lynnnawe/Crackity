package need.clarity.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import need.clarity.input.KeybindHandler;
import need.clarity.modules.visual.Freecam;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
   @WrapOperation(method = "turnPlayer(D)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"))
   private void clarity$freecamRedirectTurn(LocalPlayer localPlayer, double d, double e, Operation<Void> operation) {
      if (!Freecam.isFlagH()) {
         operation.call(new Object[]{localPlayer, d, e});
      } else {
         Freecam.floatB = Mth.wrapDegrees(Freecam.floatB + (float)(d * 0.15));
         Freecam.floatC = Mth.clamp(Freecam.floatC + (float)(e * 0.15), -90.0F, 90.0F);
      }
   }

   @Inject(method = "onButton", at = @At("HEAD"))
   private void onMouseButton(long l, MouseButtonInfo mouseButtonInfo, int i, CallbackInfo callbackInfo) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         if (minecraft.screen == null) {
            int j = mouseButtonInfo.button();
            if (i == 1) {
               KeybindHandler.onMouseButton(j, true);
            } else {
               if (i == 0) {
                  KeybindHandler.onMouseButton(j, false);
               }
            }
         }
      }
   }
}
