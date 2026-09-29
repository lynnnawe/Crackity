package need.clarity.modules.combat.combohelpers;

import need.clarity.EventStartAttack;
import need.clarity.modules.combat.SprintReset;
import need.clarity.modules.movement.MoveFix$EventMovementInput;

public class WTap extends ComboMode {
   @Override
   protected boolean methodB(EventStartAttack eventStartAttack) {
      boolean bl;
      if (minecraft.player != null && minecraft.player.zza > 0.0F) {
         bl = true;
      } else {
         bl = false;
      }

      return bl;
   }

   @Override
   protected void methodB(MoveFix$EventMovementInput eventMovementInput) {
      int j;
      if (ComboHelper.sprintResetA != null) {
         SprintReset sprintReset2 = ComboHelper.sprintResetA;
         Integer integer1 = 3;
         j = sprintReset2.getSetting("W-Tap Duration", integer1);
      } else {
         j = 3;
      }

      int i = j;
      int n;
      if (ComboHelper.sprintResetA != null) {
         SprintReset sprintReset = ComboHelper.sprintResetA;
         Integer integer = 0;
         n = sprintReset.getSetting("W-Tap Reaction", integer);
      } else {
         n = 0;
      }

      byte b;
      boolean bl1;
      label39: {
         b = (byte)n;
         if (ComboHelper.sprintResetA != null) {
            SprintReset sprintReset1 = ComboHelper.sprintResetA;
            Boolean boolean1 = false;
            if (sprintReset1.getSetting("W-Tap Ground Only", boolean1)) {
               bl1 = true;
               break label39;
            }
         }

         bl1 = false;
      }

      boolean bl = bl1;
      int k = minecraft.player.tickCount;
      int o = this.intB;
      int l = k - o;
      int m = b + i;
      if (l > m) {
         this.intB = -1;
      } else if (l > b) {
         if (!bl || minecraft.player.onGround()) {
            eventMovementInput.forward = false;
            eventMovementInput.flagG = false;
            if (minecraft.player.isSprinting()) {
               minecraft.player.setSprinting(false);
            }
         }
      }
   }
}
