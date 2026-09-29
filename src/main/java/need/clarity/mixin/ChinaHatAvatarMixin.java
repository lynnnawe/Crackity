package need.clarity.mixin;

import need.clarity.render.ChinaHatLayer;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class ChinaHatAvatarMixin implements LivingEntityRendererAccessor<Player, AvatarRenderState, PlayerModel> {
   @Inject(method = "<init>", at = @At("TAIL"))
   private void clarity$addChinaHatLayer(Context context, boolean bl, CallbackInfo callbackInfo) {
      this.clarity$invokeAddLayer(new ChinaHatLayer((RenderLayerParent<AvatarRenderState, PlayerModel>)this));
   }
}
