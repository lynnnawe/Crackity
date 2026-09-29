package need.clarity.mixin;

import need.clarity.render.ArrayListOverlayRenderer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class ArrayListRenderMixin {
   @Inject(method = "extractRenderState", at = @At("TAIL"))
   private void clarity$arrayListOverlay(GuiGraphicsExtractor guiGraphicsExtractor, DeltaTracker deltaTracker, CallbackInfo callbackInfo) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft != null) {
         ArrayListOverlayRenderer.methodA(
            guiGraphicsExtractor,
            minecraft.getWindow().getGuiScaledWidth(),
            minecraft.getWindow().getGuiScaledHeight(),
            minecraft.getWindow().getWidth(),
            minecraft.getWindow().getHeight()
         );
      }
   }
}
