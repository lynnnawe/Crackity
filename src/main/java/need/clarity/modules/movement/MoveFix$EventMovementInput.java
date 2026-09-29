package need.clarity.modules.movement;

public class MoveFix$EventMovementInput {
   public boolean forward;
   public boolean backward;
   public boolean left;
   public boolean right;
   public boolean flagE;
   public boolean flagF;
   public boolean flagG;
   public float forwardImpulse;
   public float leftImpulse;
   public final boolean flagJ;
   public final boolean flagK;
   public final boolean flagL;
   public final boolean flagM;
   public final boolean flagN;
   public final boolean flagO;
   public final boolean flagP;

   public MoveFix$EventMovementInput(boolean bl, boolean bl1, boolean bl2, boolean bl3, boolean bl4, boolean bl5, boolean bl6) {
      this.forward = bl;
      this.backward = bl1;
      this.left = bl2;
      this.right = bl3;
      this.flagE = bl4;
      this.flagF = bl5;
      this.flagG = bl6;
      this.flagJ = bl;
      this.flagK = bl1;
      this.flagL = bl2;
      this.flagM = bl3;
      this.flagN = bl4;
      this.flagO = bl5;
      this.flagP = bl6;
      this.updateImpulses();
   }

   public void updateImpulses() {
      float f;
      if (this.forward) {
         f = 1.0F;
      } else if (this.backward) {
         f = -1.0F;
      } else {
         f = 0.0F;
      }

      this.forwardImpulse = f;
      float g;
      if (this.left) {
         g = 1.0F;
      } else if (this.right) {
         g = -1.0F;
      } else {
         g = 0.0F;
      }

      this.leftImpulse = g;
   }

   public void updateDirectionalKeys() {
      boolean bl2;
      if (this.forwardImpulse > 0.0F) {
         bl2 = true;
      } else {
         bl2 = false;
      }

      this.forward = bl2;
      boolean bl;
      if (this.forwardImpulse < 0.0F) {
         bl = true;
      } else {
         bl = false;
      }

      this.backward = bl;
      boolean bl3;
      if (this.leftImpulse > 0.0F) {
         bl3 = true;
      } else {
         bl3 = false;
      }

      this.left = bl3;
      boolean bl1;
      if (this.leftImpulse < 0.0F) {
         bl1 = true;
      } else {
         bl1 = false;
      }

      this.right = bl1;
   }
}
