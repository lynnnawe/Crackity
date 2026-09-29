package need.clarity.mixin;

import need.clarity.modules.visual.Freecam;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = KeyboardInput.class, priority = 900)
public class FreecamInputMixin {
   @Inject(method = "tick", at = @At("RETURN"))
   private void clarity$freecamFreezeInput(CallbackInfo callbackInfo) {
      if (Freecam.isFlagH() && Freecam.isFreezePlayerSetting()) {
         ClientInput clientInput = (ClientInput)this;
         clientInput.keyPresses = Input.EMPTY;
         ((ClientInputAccessor)clientInput).setMoveVector(Vec2.ZERO);
      }
   }
}
