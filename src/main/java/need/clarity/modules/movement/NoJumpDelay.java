package need.clarity.modules.movement;

import java.lang.reflect.Field;
import need.clarity.modules.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;

public class NoJumpDelay extends ModuleBase {
   private static final Field fieldA = methodA();

   private static Field methodA() {
      try {
         Field field = LivingEntity.class.getDeclaredField("noJumpDelay");
         field.setAccessible(true);
         return field;
      } catch (NoSuchFieldException noSuchFieldException) {
         return null;
      }
   }

   public NoJumpDelay() {
      super("No Jump Delay", "Utility");
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         if (minecraft.level != null) {
            if (fieldA != null) {
               try {
                  Field field = fieldA;
                  LocalPlayer localPlayer = minecraft.player;
                  field.setInt(localPlayer, 0);
               } catch (IllegalAccessException illegalAccessException) {
               }
            }
         }
      }
   }
}
