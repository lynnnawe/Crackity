package need.clarity.mixin;

import need.clarity.modules.visual.NoRender;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level.ExplosionInteraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class ExplosionParticleMixin {
   @Inject(method = "explode", at = @At("HEAD"), cancellable = true)
   private void clarity$noExplosionParticles(
      Entity entity,
      DamageSource damageSource,
      ExplosionDamageCalculator explosionDamageCalculator,
      double d,
      double e,
      double f,
      float g,
      boolean bl,
      ExplosionInteraction explosionInteraction,
      ParticleOptions particleOptions,
      ParticleOptions particleOptions1,
      WeightedList<?> weightedList,
      Holder<?> holder,
      CallbackInfo callbackInfo
   ) {
      NoRender noRender = NoRender.instance;
      if (noRender != null && noRender.isExplosionParticlesSetting()) {
         callbackInfo.cancel();
      }
   }
}
