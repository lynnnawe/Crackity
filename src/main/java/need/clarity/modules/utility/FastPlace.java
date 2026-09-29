package need.clarity.modules.utility;

import java.lang.reflect.Field;
import need.clarity.modules.ModuleBase;
import net.minecraft.client.Minecraft;

public class FastPlace extends ModuleBase {
   private static final Field fieldA = methodA();

   private static Field methodA() {
      try {
         Field field = Minecraft.class.getDeclaredField("rightClickDelay");
         field.setAccessible(true);
         return field;
      } catch (NoSuchFieldException noSuchFieldException) {
         return null;
      }
   }

   public FastPlace() {
      super("Fast Place", "Utility");
   }

   @Override
   public void onTick() {
      if (fieldA != null) {
         Minecraft minecraft = Minecraft.getInstance();
         if (minecraft.player != null) {
            if (minecraft.level != null) {
               Integer integer = 0;
               int i = this.getSetting("Delay", integer);
               if (i < 0) {
                  i = 0;
               }

               if (i > 4) {
                  i = 4;
               }

               try {
                  int j = fieldA.getInt(minecraft);
                  int k = i;
                  if (j > k) {
                     Field field = fieldA;
                     int l = i;
                     field.setInt(minecraft, l);
                  }
               } catch (IllegalAccessException illegalAccessException) {
               }
            }
         }
      }
   }
}
