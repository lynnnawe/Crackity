package need.clarity.mixin;

import need.clarity.modules.movement.AntiPowderSnow;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {
   @Inject(method = "canEntityWalkOnPowderSnow", at = @At("HEAD"), cancellable = true)
   private static void cl$antiPowderSnow(Entity entity, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
      AntiPowderSnow antiPowderSnow = AntiPowderSnow.instance;
      if (antiPowderSnow != null && antiPowderSnow.isEnabled()) {
         if (entity == Minecraft.getInstance().player) {
            callbackInfoReturnable.setReturnValue(true);
         }
      }
   }
}
