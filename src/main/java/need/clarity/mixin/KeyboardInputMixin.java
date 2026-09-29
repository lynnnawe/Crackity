package need.clarity.mixin;

import need.clarity.PerMovementEvents;
import need.clarity.modules.movement.MoveFix;
import need.clarity.modules.movement.MoveFix$EventMovementInput;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import net.minecraft.client.ToggleKeyMapping;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Input;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {
   @Shadow
   @Final
   private Options options;
   @Unique
   private boolean cl$originalForward;
   @Unique
   private boolean cl$originalBack;
   @Unique
   private boolean cl$originalLeft;
   @Unique
   private boolean cl$originalRight;
   @Unique
   private boolean cl$originalJump;
   @Unique
   private boolean cl$originalSneak;
   @Unique
   private boolean cl$originalSprint;

   @Inject(method = "tick", at = @At("HEAD"))
   private void cl$onTickHead(CallbackInfo callbackInfo) {
      this.cl$originalForward = this.options.keyUp.isDown();
      this.cl$originalBack = this.options.keyDown.isDown();
      this.cl$originalLeft = this.options.keyLeft.isDown();
      this.cl$originalRight = this.options.keyRight.isDown();
      this.cl$originalJump = this.options.keyJump.isDown();
      this.cl$originalSneak = this.options.keyShift.isDown();
      this.cl$originalSprint = this.options.keySprint.isDown();
      MoveFix$EventMovementInput eventMovementInput = new MoveFix$EventMovementInput(
         this.cl$originalForward,
         this.cl$originalBack,
         this.cl$originalLeft,
         this.cl$originalRight,
         this.cl$originalJump,
         this.cl$originalSneak,
         this.cl$originalSprint
      );
      MoveFix.methodA(eventMovementInput);
      PerMovementEvents.fire(eventMovementInput);
      if (MoveFix.isFlagB()) {
         eventMovementInput.forward = false;
         eventMovementInput.backward = false;
         eventMovementInput.left = false;
         eventMovementInput.right = false;
         eventMovementInput.flagE = false;
         eventMovementInput.flagF = false;
         eventMovementInput.flagG = false;
      }

      this.options.keyUp.setDown(eventMovementInput.forward);
      this.options.keyDown.setDown(eventMovementInput.backward);
      this.options.keyLeft.setDown(eventMovementInput.left);
      this.options.keyRight.setDown(eventMovementInput.right);
      this.options.keyJump.setDown(eventMovementInput.flagE);
      this.cl$setKey(this.options.keyShift, eventMovementInput.flagF, (Boolean)this.options.toggleCrouch().get());
      this.cl$setKey(this.options.keySprint, eventMovementInput.flagG, (Boolean)this.options.toggleSprint().get());
   }

   @Inject(method = "tick", at = @At("RETURN"))
   private void cl$onTickReturn(CallbackInfo callbackInfo) {
      this.options.keyUp.setDown(this.cl$originalForward);
      this.options.keyDown.setDown(this.cl$originalBack);
      this.options.keyLeft.setDown(this.cl$originalLeft);
      this.options.keyRight.setDown(this.cl$originalRight);
      this.options.keyJump.setDown(this.cl$originalJump);
      this.cl$setKey(this.options.keyShift, this.cl$originalSneak, (Boolean)this.options.toggleCrouch().get());
      this.cl$setKey(this.options.keySprint, this.cl$originalSprint, (Boolean)this.options.toggleSprint().get());
      if (MoveFix.isFlagB()) {
         ((ClientInput)this).keyPresses = Input.EMPTY;
      }
   }

   @Unique
   private void cl$setKey(KeyMapping keyMapping, boolean bl, boolean bl1) {
      if (keyMapping instanceof ToggleKeyMapping && bl1) {
         if (keyMapping.isDown() != bl) {
            keyMapping.setDown(true);
         }
      } else {
         keyMapping.setDown(bl);
      }
   }
}
