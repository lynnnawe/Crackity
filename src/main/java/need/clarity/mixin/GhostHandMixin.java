package need.clarity.mixin;

import need.clarity.modules.utility.GhostHand;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class GhostHandMixin {
   @Inject(method = "handleKeybinds", at = @At("HEAD"))
   private void cl$ghostHandHandleKeybinds(CallbackInfo callbackInfo) {
      Minecraft minecraft = (Minecraft)this;
      if (minecraft.player != null && minecraft.level != null) {
         GhostHand ghostHand = GhostHand.getInstance();
         if (ghostHand != null && ghostHand.isEnabled()) {
            ghostHand.methodA(minecraft);
         }
      }
   }
}
