package need.clarity.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import need.clarity.modules.visual.EnchantGlint;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class EnchantGlintMixin {
   @Inject(method = "renderItem", at = @At("HEAD"))
   private void clarity$glintHead(
      LivingEntity livingEntity,
      ItemStack itemStack,
      ItemDisplayContext itemDisplayContext,
      PoseStack poseStack,
      SubmitNodeCollector submitNodeCollector,
      int i,
      CallbackInfo callbackInfo
   ) {
      if (EnchantGlint.methodA()) {
         float[] f = EnchantGlint.methodB();
         setColorModulator(f[0], f[1], f[2], f[3]);
      }
   }

   @Inject(method = "renderItem", at = @At("RETURN"))
   private void clarity$glintReturn(
      LivingEntity livingEntity,
      ItemStack itemStack,
      ItemDisplayContext itemDisplayContext,
      PoseStack poseStack,
      SubmitNodeCollector submitNodeCollector,
      int i,
      CallbackInfo callbackInfo
   ) {
      if (EnchantGlint.methodA()) {
         setColorModulator(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   private static void setColorModulator(float f, float g, float h, float i) {
      int j = GL11.glGetInteger(35725);
      if (j != 0) {
         int k = GL20.glGetUniformLocation(j, "ColorModulator");
         if (k >= 0) {
            GL20.glUniform4f(k, f, g, h, i);
         }
      }
   }
}
