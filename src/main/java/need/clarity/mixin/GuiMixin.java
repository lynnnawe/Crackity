package need.clarity.mixin;

import need.clarity.modules.visual.NoRender;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
   @Inject(method = "extractConfusionOverlay", at = @At("HEAD"), cancellable = true)
   private void clarity$noNausea(GuiGraphicsExtractor guiGraphicsExtractor, float f, CallbackInfo callbackInfo) {
      NoRender noRender = NoRender.instance;
      if (noRender != null && noRender.isNauseaOverlaySetting()) {
         callbackInfo.cancel();
      }
   }

   @Inject(method = "extractPortalOverlay", at = @At("HEAD"), cancellable = true)
   private void clarity$noPortal(GuiGraphicsExtractor guiGraphicsExtractor, float f, CallbackInfo callbackInfo) {
      NoRender noRender = NoRender.instance;
      if (noRender != null && noRender.isPortalOverlaySetting()) {
         callbackInfo.cancel();
      }
   }

   @Inject(method = "extractSpyglassOverlay", at = @At("HEAD"), cancellable = true)
   private void clarity$noSpyglass(GuiGraphicsExtractor guiGraphicsExtractor, float f, CallbackInfo callbackInfo) {
      NoRender noRender = NoRender.instance;
      if (noRender != null && noRender.isSpyglassOverlaySetting()) {
         callbackInfo.cancel();
      }
   }

   @Inject(method = "extractScoreboardSidebar", at = @At("HEAD"), cancellable = true)
   private void clarity$noScoreboard(GuiGraphicsExtractor guiGraphicsExtractor, DeltaTracker deltaTracker, CallbackInfo callbackInfo) {
      NoRender noRender = NoRender.instance;
      if (noRender != null && noRender.isScoreboardSetting()) {
         callbackInfo.cancel();
      }
   }

   @Inject(method = "extractVignette", at = @At("HEAD"), cancellable = true)
   private void clarity$noVignette(GuiGraphicsExtractor guiGraphicsExtractor, Entity entity, CallbackInfo callbackInfo) {
      NoRender noRender = NoRender.instance;
      if (noRender != null && noRender.isVignetteSetting()) {
         callbackInfo.cancel();
      }
   }
}
