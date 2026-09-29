package need.clarity.mixin;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import need.clarity.modules.utility.NameProtect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityRenderer.class, priority = 900)
public abstract class NameProtectNametagMixin {
   private static final Map<Class<?>, Field> NP_NAME_TAG_FIELD = new ConcurrentHashMap<>();

   private static Field cl$npFindNameTagField(EntityRenderState entityRenderState) {
      return NP_NAME_TAG_FIELD.computeIfAbsent(entityRenderState.getClass(), class_ -> {
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
   private void cl$nameProtectNametag(Entity entity, EntityRenderState entityRenderState, float f, CallbackInfo callbackInfo) {
      NameProtect nameProtect = NameProtect.getInstance();
      if (nameProtect != null && nameProtect.isEnabled()) {
         Minecraft minecraft = Minecraft.getInstance();
         if (entity == minecraft.player) {
            Field field = cl$npFindNameTagField(entityRenderState);
            if (field != null) {
               try {
                  if (!(field.get(entityRenderState) instanceof Component component)) {
                     return;
                  }

                  Component component1 = NameProtect.methodA(component);
                  if (component1 != component) {
                     field.set(entityRenderState, component1);
                     return;
                  }
               } catch (Exception exception) {
               }
            }
         }
      }
   }
}
