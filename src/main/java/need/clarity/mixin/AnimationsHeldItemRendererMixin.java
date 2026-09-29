package need.clarity.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import need.clarity.render.HeldItemAnimations;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemInHandRenderer.class)
public abstract class AnimationsHeldItemRendererMixin {
   @Redirect(
      method = "renderArmWithItem",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;applyItemArmTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V",
         ordinal = 4
      )
   )
   private void cl$skipDefaultEquipOffset(ItemInHandRenderer itemInHandRenderer, PoseStack poseStack, HumanoidArm humanoidArm, float f) {
      if (!HeldItemAnimations.methodA(humanoidArm)) {
         ((AnimationsHeldItemRendererInvoker)itemInHandRenderer).cl$applyItemArmTransform(poseStack, humanoidArm, f);
      }
   }

   @Redirect(
      method = "renderArmWithItem",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;swingArm(FLcom/mojang/blaze3d/vertex/PoseStack;ILnet/minecraft/world/entity/HumanoidArm;)V",
         ordinal = 2
      )
   )
   private void cl$replaceSwingArm(ItemInHandRenderer itemInHandRenderer, float f, PoseStack poseStack, int i, HumanoidArm humanoidArm) {
      if (!HeldItemAnimations.methodA(poseStack, humanoidArm, f)) {
         ((AnimationsHeldItemRendererInvoker)itemInHandRenderer).cl$swingArm(f, poseStack, i, humanoidArm);
      }
   }
}
