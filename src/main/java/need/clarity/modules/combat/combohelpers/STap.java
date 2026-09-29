package need.clarity.modules.combat.combohelpers;

import need.clarity.EventStartAttack;
import need.clarity.modules.combat.SprintReset;
import need.clarity.modules.movement.MoveFix$EventMovementInput;

public class STap extends ComboMode {
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
      int m;
      if (ComboHelper.sprintResetA != null) {
         SprintReset sprintReset = ComboHelper.sprintResetA;
         Integer integer = 3;
         m = sprintReset.getSetting("S-Tap Duration", integer);
      } else {
         m = 3;
      }

      byte b = (byte)m;
      int i;
      if (ComboHelper.sprintResetA != null) {
         SprintReset sprintReset1 = ComboHelper.sprintResetA;
         Integer integer1 = 0;
         i = sprintReset1.getSetting("S-Tap Reaction", integer1);
      } else {
         i = 0;
      }

      byte c;
      boolean bl1;
      label39: {
         c = (byte)i;
         if (ComboHelper.sprintResetA != null) {
            SprintReset sprintReset2 = ComboHelper.sprintResetA;
            Boolean boolean1 = false;
            if (sprintReset2.getSetting("S-Tap Ground Only", boolean1)) {
               bl1 = true;
               break label39;
            }
         }

         bl1 = false;
      }

      boolean bl = bl1;
      int j = minecraft.player.tickCount;
      int n = this.intB;
      int k = j - n;
      int l = c + b;
      if (k > l) {
         this.intB = -1;
      } else if (k > c) {
         if (!bl || minecraft.player.onGround()) {
            eventMovementInput.forward = false;
            eventMovementInput.flagG = false;
            eventMovementInput.backward = true;
            if (minecraft.player.isSprinting()) {
               minecraft.player.setSprinting(false);
            }
         }
      }
   }
}
