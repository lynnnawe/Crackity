package need.clarity.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import need.clarity.modules.visual.ViewModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ViewModelMixin {
   @Unique
   private boolean clarity$pushed = false;

   @Inject(method = "renderArmWithItem", at = @At("HEAD"))
   private void clarity$vmHead(
      AbstractClientPlayer abstractClientPlayer,
      float g,
      float h,
      InteractionHand interactionHand,
      float i,
      ItemStack itemStack,
      float j,
      PoseStack poseStack,
      SubmitNodeCollector submitNodeCollector,
      int k,
      CallbackInfo callbackInfo
   ) {
      this.clarity$pushed = false;
      if (ViewModel.methodB()) {
         ViewModel viewModel = ViewModel.instance;
         poseStack.pushPose();
         poseStack.translate(viewModel.methodA(interactionHand), viewModel.methodB(interactionHand), viewModel.methodC(interactionHand));
         poseStack.mulPose(Axis.XP.rotationDegrees(viewModel.methodD(interactionHand)));
         poseStack.mulPose(Axis.YP.rotationDegrees(viewModel.methodE(interactionHand)));
         poseStack.mulPose(Axis.ZP.rotationDegrees(viewModel.methodF(interactionHand)));
         float f = viewModel.methodG(interactionHand);
         poseStack.scale(f, f, f);
         this.clarity$pushed = true;
      }
   }

   @Inject(method = "renderArmWithItem", at = @At("RETURN"))
   private void clarity$vmTail(
      AbstractClientPlayer abstractClientPlayer,
      float f,
      float g,
      InteractionHand interactionHand,
      float h,
      ItemStack itemStack,
      float i,
      PoseStack poseStack,
      SubmitNodeCollector submitNodeCollector,
      int j,
      CallbackInfo callbackInfo
   ) {
      if (this.clarity$pushed) {
         poseStack.popPose();
         this.clarity$pushed = false;
      }
   }
}
