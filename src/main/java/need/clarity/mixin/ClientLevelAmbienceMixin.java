package need.clarity.mixin;

import need.clarity.modules.visual.Ambience;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public abstract class ClientLevelAmbienceMixin {
   @Inject(method = "getOverworldClockTime()J", at = @At("RETURN"), cancellable = true)
   private void ambience$overrideOverworldClockTime(CallbackInfoReturnable<Long> callbackInfoReturnable) {
      if (this instanceof ClientLevel && Ambience.isOverrideTimeSetting()) {
         callbackInfoReturnable.setReturnValue(Ambience.getTimeOfDaySetting());
      }
   }

   @Inject(method = "getDefaultClockTime()J", at = @At("RETURN"), cancellable = true)
   private void ambience$overrideDefaultClockTime(CallbackInfoReturnable<Long> callbackInfoReturnable) {
      if (this instanceof ClientLevel && Ambience.isOverrideTimeSetting()) {
         callbackInfoReturnable.setReturnValue(Ambience.getTimeOfDaySetting());
      }
   }

   @Inject(method = "getRainLevel(F)F", at = @At("RETURN"), cancellable = true)
   private void ambience$overrideRainLevel(float f, CallbackInfoReturnable<Float> callbackInfoReturnable) {
      if (this instanceof ClientLevel) {
         if (Ambience.isOverrideWeatherSetting()) {
            callbackInfoReturnable.setReturnValue("Clear".equals(Ambience.getWeatherSetting()) ? 0.0F : 1.0F);
         }
      }
   }

   @Inject(method = "getThunderLevel(F)F", at = @At("RETURN"), cancellable = true)
   private void ambience$overrideThunderLevel(float f, CallbackInfoReturnable<Float> callbackInfoReturnable) {
      if (this instanceof ClientLevel) {
         if (Ambience.isOverrideWeatherSetting()) {
            callbackInfoReturnable.setReturnValue("Thunder".equals(Ambience.getWeatherSetting()) ? 1.0F : 0.0F);
         }
      }
   }
}
