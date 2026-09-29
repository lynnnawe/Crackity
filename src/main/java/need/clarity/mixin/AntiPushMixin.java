package need.clarity.mixin;

import need.clarity.modules.movement.AntiPush;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class AntiPushMixin {
   @Inject(method = "push(DDD)V", at = @At("HEAD"), cancellable = true)
   private void cl$antiPushEntity(double d, double e, double f, CallbackInfo callbackInfo) {
      if (AntiPush.methodA()) {
         Minecraft minecraft = Minecraft.getInstance();
         if (minecraft.player != null) {
            if (this == minecraft.player) {
               callbackInfo.cancel();
            }
         }
      }
   }
}
