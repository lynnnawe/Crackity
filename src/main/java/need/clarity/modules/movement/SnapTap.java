package need.clarity.modules.movement;

import need.clarity.PerMovementEvents;
import need.clarity.modules.ModuleBase;
import net.minecraft.client.Minecraft;

public class SnapTap extends ModuleBase {
   private boolean flagA = false;
   private boolean flagB = false;
   private boolean flagC = false;
   private boolean flagD = false;
   private boolean flagE = false;
   private boolean flagF = false;

   public SnapTap() {
      super("Snap Tap", "Utility");
      PerMovementEvents.register(this::methodA);
   }

   @Override
   public void onDisable() {
      this.flagA = false;
      this.flagB = false;
      this.flagC = false;
      this.flagD = false;
   }

   private void methodA(MoveFix$EventMovementInput eventMovementInput) {
      if (this.isEnabled()) {
         if (Minecraft.getInstance().screen == null) {
            boolean bl6;
            if (eventMovementInput.left && !this.flagA) {
               bl6 = true;
            } else {
               bl6 = false;
            }

            boolean bl = bl6;
            boolean bl4;
            if (eventMovementInput.right && !this.flagB) {
               bl4 = true;
            } else {
               bl4 = false;
            }

            boolean bl1 = bl4;
            if (bl && !bl1) {
               this.flagE = false;
            }

            if (bl1 && !bl) {
               this.flagE = true;
            }

            if (eventMovementInput.left && eventMovementInput.right) {
               if (this.flagE) {
                  eventMovementInput.left = false;
               } else {
                  eventMovementInput.right = false;
               }
            }

            boolean bl5;
            if (eventMovementInput.forward && !this.flagC) {
               bl5 = true;
            } else {
               bl5 = false;
            }

            boolean bl2 = bl5;
            boolean bl7;
            if (eventMovementInput.backward && !this.flagD) {
               bl7 = true;
            } else {
               bl7 = false;
            }

            boolean bl3 = bl7;
            if (bl2 && !bl3) {
               this.flagF = true;
            }

            if (bl3 && !bl2) {
               this.flagF = false;
            }

            if (eventMovementInput.forward && eventMovementInput.backward) {
               if (this.flagF) {
                  eventMovementInput.backward = false;
               } else {
                  eventMovementInput.forward = false;
               }
            }

            boolean bl8 = eventMovementInput.flagL;
            this.flagA = bl8;
            boolean bl9 = eventMovementInput.flagM;
            this.flagB = bl9;
            boolean bl10 = eventMovementInput.flagJ;
            this.flagC = bl10;
            boolean bl11 = eventMovementInput.flagK;
            this.flagD = bl11;
         }
      }
   }
}
