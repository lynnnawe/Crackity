package need.clarity.mixin;

import need.clarity.LocalPlayerRotationState;
import need.clarity.modules.combat.AimAssist;
import need.clarity.modules.movement.MoveFix;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public class PlayerMixin {
   @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getLookAngle()Lnet/minecraft/world/phys/Vec3;"))
   private Vec3 cl$redirectSwimmingLookVector(Player player) {
      Minecraft minecraft = Minecraft.getInstance();
      return player == minecraft.player && LocalPlayerRotationState.silentActive && MoveFix.isModeSetting()
         ? AimAssist.methodB(LocalPlayerRotationState.tickServerPitch, LocalPlayerRotationState.tickServerYaw)
         : player.getLookAngle();
   }

   @Redirect(method = {"causeExtraKnockback", "doSweepAttack"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getYRot()F"))
   private float cl$redirectAttackYaw(Player player) {
      Minecraft minecraft = Minecraft.getInstance();
      return player == minecraft.player && LocalPlayerRotationState.silentActive ? player.getYRot() : player.getYRot();
   }
}
