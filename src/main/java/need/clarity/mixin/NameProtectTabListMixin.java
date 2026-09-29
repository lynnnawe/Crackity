package need.clarity.mixin;

import need.clarity.modules.utility.NameProtect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInfo.class)
public abstract class NameProtectTabListMixin {
   @Inject(method = "getTabListDisplayName", at = @At("RETURN"), cancellable = true)
   private void cl$nameProtectTabList(CallbackInfoReturnable<Component> callbackInfoReturnable) {
      NameProtect nameProtect = NameProtect.getInstance();
      if (nameProtect != null && nameProtect.isEnabled()) {
         Minecraft minecraft = Minecraft.getInstance();
         if (minecraft.player != null) {
            String string = ((PlayerInfo)this).getProfile().name();
            if (string.equals(minecraft.player.getGameProfile().name())) {
               Component component = (Component)callbackInfoReturnable.getReturnValue();
               String string1 = nameProtect.getSpoofNameSetting();
               if (component != null) {
                  String string2 = component.getString();
                  if (string2.contains(string)) {
                     callbackInfoReturnable.setReturnValue(Component.literal(string2.replace(string, string1)).withStyle(component.getStyle()));
                     return;
                  }
               } else {
                  callbackInfoReturnable.setReturnValue(Component.literal(string1));
               }
            }
         }
      }
   }
}
