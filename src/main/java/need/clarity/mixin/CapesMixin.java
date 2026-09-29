package need.clarity.mixin;

import com.mojang.authlib.GameProfile;
import need.clarity.modules.visual.Capes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.core.ClientAsset.ResourceTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInfo.class)
public abstract class CapesMixin {
   @Shadow
   @Final
   private GameProfile profile;

   @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
   private void clarity$overrideCape(CallbackInfoReturnable<PlayerSkin> callbackInfoReturnable) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft != null && minecraft.player != null) {
         if (this.profile.id().equals(minecraft.player.getUUID())) {
            Capes capes = Capes.instance;
            if (capes != null && capes.isEnabled()) {
               Identifier identifier = capes.getIdentifierD();
               if (identifier != null) {
                  PlayerSkin playerSkin = (PlayerSkin)callbackInfoReturnable.getReturnValue();
                  ResourceTexture resourceTexture = new ResourceTexture(identifier, identifier);
                  callbackInfoReturnable.setReturnValue(
                     new PlayerSkin(playerSkin.body(), resourceTexture, resourceTexture, playerSkin.model(), playerSkin.secure())
                  );
               }
            }
         }
      }
   }
}
