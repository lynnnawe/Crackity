package need.clarity.mixin;

import need.clarity.modules.utility.ClientSpoofer;
import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientBrandRetriever.class)
public class ClientBrandRetrieverMixin {
   @Inject(method = "getClientModName", at = @At("RETURN"), cancellable = true, remap = false)
   private static void cl$spoofBrand(CallbackInfoReturnable<String> callbackInfoReturnable) {
      ClientSpoofer clientSpoofer = ClientSpoofer.getInstance();
      if (clientSpoofer != null && clientSpoofer.isEnabled()) {
         callbackInfoReturnable.setReturnValue(clientSpoofer.getClientSetting());
      }
   }
}
