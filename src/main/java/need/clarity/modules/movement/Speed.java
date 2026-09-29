package need.clarity.modules.movement;

import need.clarity.PerMovementEvents;
import need.clarity.modules.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class Speed extends ModuleBase {
   public Speed() {
      super("Speed", "Movement");
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         if (minecraft.level != null) {
            this.methodA(minecraft);
         }
      }
   }

   private void methodA(Minecraft minecraft) {
      LocalPlayer localPlayer = minecraft.player;
      MoveFix$EventMovementInput eventMovementInput = PerMovementEvents.currentInput;
      boolean bl4;
      if (eventMovementInput != null) {
         bl4 = eventMovementInput.forward;
      } else {
         bl4 = minecraft.options.keyUp.isDown();
      }

      boolean bl = bl4;
      boolean bl7;
      if (eventMovementInput != null) {
         bl7 = eventMovementInput.backward;
      } else {
         bl7 = minecraft.options.keyDown.isDown();
      }

      boolean bl1 = bl7;
      boolean bl6;
      if (eventMovementInput != null) {
         bl6 = eventMovementInput.left;
      } else {
         bl6 = minecraft.options.keyLeft.isDown();
      }

      boolean bl2 = bl6;
      boolean bl5;
      if (eventMovementInput != null) {
         bl5 = eventMovementInput.right;
      } else {
         bl5 = minecraft.options.keyRight.isDown();
      }

      boolean bl3 = bl5;
      if (!bl && !bl1 && !bl2 && !bl3) {
         double h = localPlayer.getDeltaMovement().y;
         localPlayer.setDeltaMovement(0.0, h, 0.0);
      } else {
         Float float_ = 4.0F;
         double d = this.getSetting("Speed", float_).floatValue() / 20.0;
         double e = methodA(localPlayer, bl, bl1, bl2, bl3);
         double f = -Math.sin(e) * d;
         double g = Math.cos(e) * d;
         double i = localPlayer.getDeltaMovement().y;
         localPlayer.setDeltaMovement(f, i, g);
      }
   }

   private static double methodA(LocalPlayer localPlayer, boolean bl, boolean bl1, boolean bl2, boolean bl3) {
      float h = localPlayer.getYRot();
      float g = 0.0F;
      if (bl) {
         if (bl2 && !bl3) {
            g = -45.0F;
         } else if (bl3 && !bl2) {
            g = 45.0F;
         }
      } else if (bl1) {
         if (bl2 && !bl3) {
            g = -135.0F;
         } else if (bl3 && !bl2) {
            g = 135.0F;
         } else {
            g = 180.0F;
         }
      } else if (bl2 && !bl3) {
         g = -90.0F;
      } else if (bl3 && !bl2) {
         g = 90.0F;
      }

      float f = g;
      return Math.toRadians(h + f);
   }
}
