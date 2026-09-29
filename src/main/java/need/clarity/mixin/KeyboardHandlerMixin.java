package need.clarity.mixin;

import need.clarity.input.KeybindHandler;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {
   @Inject(method = "keyPress", at = @At("HEAD"))
   private void onKeyPress(long l, int i, KeyEvent keyEvent, CallbackInfo callbackInfo) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         if (minecraft.screen == null) {
            int j = keyEvent.key();
            if (i == 1) {
               KeybindHandler.onKey(j, true);
            } else {
               if (i == 0) {
                  KeybindHandler.onKey(j, false);
               }
            }
         }
      }
   }
}
