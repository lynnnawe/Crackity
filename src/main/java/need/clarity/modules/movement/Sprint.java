package need.clarity.modules.movement;

import need.clarity.PerMovementEvents;
import need.clarity.modules.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class Sprint extends ModuleBase {
   public Sprint() {
      super("Sprint", "Movement");
   }

   @Override
   public void onDisable() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         minecraft.player.setSprinting(false);
      }
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         if (minecraft.level != null) {
            LocalPlayer localPlayer = minecraft.player;
            if (this.canSprint(minecraft, localPlayer)) {
               localPlayer.setSprinting(true);
            }
         }
      }
   }

   private boolean canSprint(Minecraft minecraft, Player player) {
      MoveFix$EventMovementInput eventMovementInput = PerMovementEvents.currentInput;
      boolean bl;
      if (eventMovementInput != null) {
         bl = eventMovementInput.forward;
      } else {
         bl = minecraft.options.keyUp.isDown();
      }

      if (!bl) {
         return false;
      }

      if (player.getFoodData().getFoodLevel() <= 6) {
         return false;
      }

      if (player.isShiftKeyDown()) {
         return false;
      }

      if (player.isBlocking()) {
         return false;
      }

      if (player.isUsingItem()) {
         return false;
      }

      Holder holder = MobEffects.BLINDNESS;
      return player.hasEffect(holder) ? false : player.onGround() || player.isSprinting();
   }
}
