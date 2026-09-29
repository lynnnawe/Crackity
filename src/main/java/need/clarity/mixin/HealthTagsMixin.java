package need.clarity.mixin;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import need.clarity.modules.visual.HealthTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class HealthTagsMixin {
   private static final Map<Class<?>, Field> NAME_TAG_FIELD = new ConcurrentHashMap<>();

   private static Field findNameTagField(EntityRenderState entityRenderState) {
      return NAME_TAG_FIELD.computeIfAbsent(entityRenderState.getClass(), class_ -> {
         for (Class class_x = class_; class_x != null && class_x != Object.class; class_x = class_x.getSuperclass()) {
            for (Field field : class_x.getDeclaredFields()) {
               if (Component.class.isAssignableFrom(field.getType())) {
                  field.setAccessible(true);
                  return field;
               }
            }
         }

         return null;
      });
   }

   @Inject(method = "extractRenderState", at = @At("RETURN"))
   private void clarity$appendHealthTag(Entity entity, EntityRenderState entityRenderState, float f, CallbackInfo callbackInfo) {
      HealthTags healthTags = HealthTags.getInstance();
      if (healthTags != null && healthTags.isEnabled()) {
         if (entity instanceof LivingEntity livingEntity) {
            Minecraft minecraft = Minecraft.getInstance();
            if (entity != minecraft.player) {
               Field field = findNameTagField(entityRenderState);
               if (field != null) {
                  try {
                     if (field.get(entityRenderState) instanceof Component component) {
                        int i = HealthTags.methodA(livingEntity);
                        int j = HealthTags.methodA(livingEntity.getHealth(), livingEntity.getMaxHealth()) & 16777215;
                        MutableComponent mutableComponent = Component.literal("  " + i).setStyle(Style.EMPTY.withColor(j));
                        field.set(entityRenderState, component.copy().append(mutableComponent));
                     }
                  } catch (Exception exception) {
                  }
               }
            }
         }
      }
   }
}
