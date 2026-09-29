package need.clarity.mixin;

import need.clarity.modules.utility.ChestStealer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.ChestMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class SetScreenMixin {
   @Shadow
   public Screen screen;
   @Shadow
   public MouseHandler mouseHandler;

   @Inject(method = "setScreen", at = @At("RETURN"))
   private void cl$suppressChestScreenAfterOpen(Screen screen, CallbackInfo callbackInfo) {
      if (screenx instanceof AbstractContainerScreen abstractContainerScreen) {
         if (abstractContainerScreen.getMenu() instanceof ChestMenu) {
            if (ChestStealer.isCollectionModeSetting()) {
               this.screen = null;
               this.mouseHandler.grabMouse();
            }
         }
      }
   }
}
