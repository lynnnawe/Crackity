package need.clarity.util;

import java.lang.reflect.Field;
import need.clarity.mixin.InventoryAccessor;
import net.minecraft.world.entity.player.Inventory;

public final class InventoryAccess {
   private static Field fieldA = null;
   private static boolean flagB = false;

   public static int getSelectedSlot(Inventory inventory) {
      try {
         return ((InventoryAccessor)inventory).getSelected();
      } catch (ClassCastException classCastException) {
         return getSelectedSlotReflectively(inventory);
      }
   }

   public static void setSelectedSlot(Inventory inventory, int i) {
      try {
         ((InventoryAccessor)inventory).setSelected(i);
      } catch (ClassCastException classCastException) {
         setSelectedSlotReflectively(inventory, i);
      }
   }

   private static Field findSelectedSlotField(Inventory inventory) {
      if (fieldA != null) {
         return fieldA;
      }

      if (flagB) {
         return null;
      }

      String[] strings = new String[]{"selected", "f_35978_", "field_7545"};
      int j = strings.length;

      for (int i = 0; i < j; i++) {
         int m = i;
         String string = strings[m];

         try {
            Field field = Inventory.class.getDeclaredField(string);
            field.setAccessible(true);
            fieldA = field;
            return field;
         } catch (NoSuchFieldException noSuchFieldException) {
         }
      }

      Field[] fields = Inventory.class.getDeclaredFields();
      int k = fields.length;

      for (int o = 0; o < k; o++) {
         int n = o;
         Field field1 = fields[n];
         Class class_x = field1.getType();
         Class class_x = int.class;
         if (class_x == class_x) {
            field1.setAccessible(true);

            try {
               int l = (Integer)field1.get(inventory);
               if (l >= 0 && l <= 8) {
                  fieldA = field1;
                  return field1;
               }
            } catch (IllegalAccessException illegalAccessException) {
            }
         }
      }

      flagB = true;
      return null;
   }

   private static int getSelectedSlotReflectively(Inventory inventory) {
      Field field = findSelectedSlotField(inventory);
      if (field == null) {
         return 0;
      }

      try {
         return (Integer)field.get(inventory);
      } catch (IllegalAccessException illegalAccessException) {
         return 0;
      }
   }

   private static void setSelectedSlotReflectively(Inventory inventory, int i) {
      Field field = findSelectedSlotField(inventory);
      if (field != null) {
         try {
            Integer integer = i;
            field.set(inventory, integer);
         } catch (IllegalAccessException illegalAccessException) {
         }
      }
   }
}
