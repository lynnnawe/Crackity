package need.clarity.modules.visual;

import need.clarity.modules.ModuleBase;
import net.minecraft.world.InteractionHand;

public class ViewModel extends ModuleBase {
   public static volatile ViewModel instance;

   public ViewModel() {
      super("ViewModel", "Visual");
      instance = this;
   }

   public boolean isMirrorSetting() {
      Boolean boolean_ = false;
      return this.getSetting("Mirror", boolean_);
   }

   public float methodA(InteractionHand interactionHand) {
      if (this.isMirrorSetting()) {
         Float float3 = 0.0F;
         float g = this.getSetting("PosX", float3);
         InteractionHand interactionHand2 = InteractionHand.MAIN_HAND;
         float h;
         if (interactionHand == interactionHand2) {
            h = g;
         } else {
            h = g * -1.0F;
         }

         return h;
      } else {
         InteractionHand interactionHand1 = InteractionHand.MAIN_HAND;
         float f;
         if (interactionHand == interactionHand1) {
            Float float1 = 0.0F;
            f = this.getSetting("Main PosX", float1);
         } else {
            Float float2 = 0.0F;
            f = this.getSetting("Off PosX", float2);
         }

         return f;
      }
   }

   public float methodB(InteractionHand interactionHand) {
      if (this.isMirrorSetting()) {
         Float float3 = 0.0F;
         return this.getSetting("PosY", float3);
      }

      InteractionHand interactionHand1 = InteractionHand.MAIN_HAND;
      float f;
      if (interactionHand == interactionHand1) {
         Float float2 = 0.0F;
         f = this.getSetting("Main PosY", float2);
      } else {
         Float float1 = 0.0F;
         f = this.getSetting("Off PosY", float1);
      }

      return f;
   }

   public float methodC(InteractionHand interactionHand) {
      if (this.isMirrorSetting()) {
         Float float1 = 0.0F;
         return this.getSetting("PosZ", float1);
      }

      InteractionHand interactionHand1 = InteractionHand.MAIN_HAND;
      float f;
      if (interactionHand == interactionHand1) {
         Float float2 = 0.0F;
         f = this.getSetting("Main PosZ", float2);
      } else {
         Float float3 = 0.0F;
         f = this.getSetting("Off PosZ", float3);
      }

      return f;
   }

   public float methodD(InteractionHand interactionHand) {
      if (this.isMirrorSetting()) {
         Float float1 = 0.0F;
         return this.getSetting("RotX", float1);
      }

      InteractionHand interactionHand1 = InteractionHand.MAIN_HAND;
      float f;
      if (interactionHand == interactionHand1) {
         Float float3 = 0.0F;
         f = this.getSetting("Main RotX", float3);
      } else {
         Float float2 = 0.0F;
         f = this.getSetting("Off RotX", float2);
      }

      return f;
   }

   public float methodE(InteractionHand interactionHand) {
      if (this.isMirrorSetting()) {
         Float float3 = 0.0F;
         return this.getSetting("RotY", float3);
      }

      InteractionHand interactionHand1 = InteractionHand.MAIN_HAND;
      float f;
      if (interactionHand == interactionHand1) {
         Float float1 = 0.0F;
         f = this.getSetting("Main RotY", float1);
      } else {
         Float float2 = 0.0F;
         f = this.getSetting("Off RotY", float2);
      }

      return f;
   }

   public float methodF(InteractionHand interactionHand) {
      if (this.isMirrorSetting()) {
         Float float1 = 0.0F;
         return this.getSetting("RotZ", float1);
      }

      InteractionHand interactionHand1 = InteractionHand.MAIN_HAND;
      float f;
      if (interactionHand == interactionHand1) {
         Float float2 = 0.0F;
         f = this.getSetting("Main RotZ", float2);
      } else {
         Float float3 = 0.0F;
         f = this.getSetting("Off RotZ", float3);
      }

      return f;
   }

   public float methodG(InteractionHand interactionHand) {
      if (this.isMirrorSetting()) {
         Float float3 = 1.0F;
         return this.getSetting("Scale", float3);
      }

      InteractionHand interactionHand1 = InteractionHand.MAIN_HAND;
      float f;
      if (interactionHand == interactionHand1) {
         Float float2 = 1.0F;
         f = this.getSetting("Main Scale", float2);
      } else {
         Float float1 = 1.0F;
         f = this.getSetting("Off Scale", float1);
      }

      return f;
   }

   public static boolean methodB() {
      ViewModel viewModel = instance;
      boolean bl;
      if (viewModel != null && viewModel.isEnabled()) {
         bl = true;
      } else {
         bl = false;
      }

      return bl;
   }
}
