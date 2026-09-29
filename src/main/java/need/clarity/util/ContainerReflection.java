package need.clarity.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public final class ContainerReflection {
   private static final Field fieldA;
   private static final Method methodB;
   private static final Object objectC;
   private static final Object objectD;

   public static Slot methodA(AbstractContainerScreen<?> abstractContainerScreen) {
      if (fieldA != null && abstractContainerScreen != null) {
         try {
            return (Slot)fieldA.get(abstractContainerScreen);
         } catch (Throwable throwable) {
            return null;
         }
      } else {
         return null;
      }
   }

   public static void methodA(MultiPlayerGameMode multiPlayerGameMode, int i, int j, LocalPlayer localPlayer) {
      Object object = objectC;
      methodA(multiPlayerGameMode, i, j, 40, object, localPlayer);
   }

   public static void methodA(MultiPlayerGameMode multiPlayerGameMode, int i, int j, int k, LocalPlayer localPlayer) {
      Object object = objectC;
      methodA(multiPlayerGameMode, i, j, k, object, localPlayer);
   }

   public static void methodB(MultiPlayerGameMode multiPlayerGameMode, int i, int j, LocalPlayer localPlayer) {
      Object object = objectD;
      methodA(multiPlayerGameMode, i, j, 0, object, localPlayer);
   }

   private static void methodA(MultiPlayerGameMode multiPlayerGameMode, int i, int j, int k, Object object, LocalPlayer localPlayer) {
      if (methodB != null) {
         if (object != null) {
            if (multiPlayerGameMode != null) {
               try {
                  Method method = methodB;
                  Object[] objects = new Object[5];
                  Integer integer = i;
                  objects[0] = integer;
                  Integer integer1 = j;
                  objects[1] = integer1;
                  Integer integer2 = k;
                  objects[2] = integer2;
                  objects[3] = object;
                  objects[4] = localPlayer;
                  method.invoke(multiPlayerGameMode, objects);
               } catch (Throwable throwable) {
               }
            }
         }
      }
   }

   static {
      Field field = null;

      label61:
      for (Class<AbstractContainerScreen> class_xxxxx = AbstractContainerScreen.class; class_xxxxx != null; class_xxxxx = class_xxxxx.getSuperclass()) {
         try {
            Field field1 = class_xxxxx.getDeclaredField("hoveredSlot");
            field1.setAccessible(true);
            field = field1;
            break;
         } catch (NoSuchFieldException noSuchFieldException) {
            Field[] fields = class_xxxxx.getDeclaredFields();
            int k = fields.length;

            for (int n = 0; n < k; n++) {
               int m = n;
               Field field2 = fields[m];
               Class class_x = field2.getType();
               Class<Slot> class_xx = Slot.class;
               if (class_x == class_xx) {
                  field2.setAccessible(true);
                  field = field2;
                  break label61;
               }
            }
         }
      }

      fieldA = field;
      Method method1 = null;
      Object object = null;
      Object object1 = null;

      try {
         Method[] methods = MultiPlayerGameMode.class.getDeclaredMethods();
         int j = methods.length;

         for (int i = 0; i < j; i++) {
            int l = i;
            Method method = methods[l];
            Class[] class_xxxxxx = method.getParameterTypes();
            if (class_xxxxxx.length == 5) {
               Class class_x = class_xxxxxx[0];
               Class class_xx = int.class;
               if (class_x == class_xx) {
                  Class class_xxx = class_xxxxxx[1];
                  Class class_xxxx = int.class;
                  if (class_xxx == class_xxxx) {
                     Class class_xxxxx = class_xxxxxx[2];
                     Class class_xxxxxx = int.class;
                     if (class_xxxxx == class_xxxxxx && class_xxxxxx[3].isEnum()) {
                        Class class_xxxxxxx = class_xxxxxx[4];
                        Class<Player> class_xxxxxxxx = Player.class;
                        if (class_xxxxxxx == class_xxxxxxxx) {
                           Class class_xxxxxxxxx = method.getReturnType();
                           Class class_xxxxxxxxxx = void.class;
                           if (class_xxxxxxxxx == class_xxxxxxxxxx) {
                              method.setAccessible(true);
                              method1 = method;
                              Object[] objects = class_xxxxxx[3].getEnumConstants();
                              if (objects != null && objects.length > 2) {
                                 object1 = objects[1];
                                 object = objects[2];
                              }
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }
      } catch (Throwable throwable) {
      }

      methodB = method1;
      objectC = object;
      objectD = object1;
   }
}
