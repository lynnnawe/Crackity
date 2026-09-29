package need.clarity.mixin;

import need.clarity.EventStartAttack;
import need.clarity.PerAttackEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftStartAttackMixin {
   @Shadow
   public LocalPlayer player;
   @Shadow
   public HitResult hitResult;

   @Inject(method = "startAttack", at = @At("HEAD"))
   private void cl$onStartAttack(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
      if (this.player != null) {
         Entity entity = null;
         if (this.hitResult != null && this.hitResult.getType() == Type.ENTITY) {
            entity = ((EntityHitResult)this.hitResult).getEntity();
         }

         PerAttackEvents.fire(new EventStartAttack(this.player, entity));
      }
   }
}
