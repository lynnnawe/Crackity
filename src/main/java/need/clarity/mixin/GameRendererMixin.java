package need.clarity.mixin;

import need.clarity.PerFrameEvents;
import need.clarity.modules.visual.HandShaders;
import need.clarity.render.HandShaderRenderer;
import need.clarity.render.RenderOptions;
import need.clarity.render.RenderState;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GameRenderer.class, priority = 1001)
public class GameRendererMixin {
   @Inject(method = "renderLevel", at = @At("HEAD"))
   public void clarity$firePerFrame(DeltaTracker deltaTracker, CallbackInfo callbackInfo) {
      PerFrameEvents.fire();
   }

   @Inject(
      method = "renderLevel",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/GameRenderer;renderItemInHand(Lnet/minecraft/client/renderer/state/level/CameraRenderState;FLorg/joml/Matrix4fc;)V"
      )
   )
   public void beforeRenderHand(DeltaTracker deltaTracker, CallbackInfo callbackInfo) {
      if (!HandShaderRenderer.isInitialized()) {
         HandShaderRenderer.initializeShaders();
      }

      HandShaders handShaders = HandShaders.instance;
      if (handShaders != null && handShaders.isEnabled()) {
         boolean bl = "Glow".equals(HandShaders.getShaderSetting());
         RenderOptions.flagA = bl;
         RenderOptions.flagB = bl && HandShaders.isApplyToArmSetting();
         RenderOptions.flagC = bl && HandShaders.isApplyToItemsSetting();
         RenderOptions.intD = HandShaders.methodO();
         RenderOptions.doubleE = HandShaders.getOpacitySetting();
         RenderOptions.flagF = HandShaders.isTexturedSetting();
         RenderOptions.flagG = HandShaders.isFullbrightSetting();
         RenderOptions.flagH = HandShaders.isOutlineSetting();
         RenderOptions.intI = HandShaders.methodT();
         RenderOptions.doubleJ = HandShaders.getOutlineWidthSetting();
         RenderOptions.flagK = HandShaders.isBloomSetting();
         RenderOptions.doubleL = HandShaders.getGlowSpreadSetting();
         RenderOptions.flagM = HandShaders.isAntialiasingSetting();
         if ("Invert".equals(HandShaders.getShaderSetting()) && HandShaders.isHideHandSetting()) {
            RenderState.setColorMasked(true);
            return;
         }
      } else {
         RenderOptions.flagA = false;
      }
   }

   @Inject(
      method = "renderLevel",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/GameRenderer;renderItemInHand(Lnet/minecraft/client/renderer/state/level/CameraRenderState;FLorg/joml/Matrix4fc;)V",
         shift = Shift.AFTER
      )
   )
   public void afterRenderHand(DeltaTracker deltaTracker, CallbackInfo callbackInfo) {
      HandShaders handShaders = HandShaders.instance;
      if (handShaders != null && handShaders.isEnabled()) {
         if (RenderState.isColorMasked()) {
            RenderState.setColorMasked(false);
         }

         HandShaderRenderer.resizeFramebuffers();
         handShaders.methodA();
      }
   }
}
