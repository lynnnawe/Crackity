package need.clarity.mixin;

import need.clarity.modules.combat.Reach;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public class ReachMixin {
   @ModifyArg(
      method = "pick",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/level/BlockGetter;clip(Lnet/minecraft/world/level/ClipContext;)Lnet/minecraft/world/phys/BlockHitResult;"
      ),
      index = 0,
      require = 0
   )
   private ClipContext reach$modifyClipContext(ClipContext clipContext) {
      return clipContext;
   }

   @ModifyArg(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;pick(F)V"), index = 0, require = 0)
   private float reach$extendBlockReach(float f) {
      float g = Reach.getReachSetting();
      return g > 0.0F ? g : f;
   }

   @Redirect(
      method = "startAttack",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;distanceTo(Lnet/minecraft/world/entity/Entity;)F"),
      require = 0
   )
   private float reach$redirectEntityDistance(Entity entity, Entity entity1) {
      float f = entity.distanceTo(entity1);
      float g = Reach.getReachSetting2();
      if (g <= 0.0F) {
         return f;
      } else {
         return f <= g ? Math.min(f, 3.0F) : f;
      }
   }
}
