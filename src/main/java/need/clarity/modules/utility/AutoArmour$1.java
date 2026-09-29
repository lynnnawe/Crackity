package need.clarity.modules.utility;

import net.minecraft.world.entity.EquipmentSlot;

// $VF: synthetic class
class AutoArmour$1 {
   // $VF: synthetic field
   static final int[] intArrayA = new int[EquipmentSlot.values().length];

   static {
      try {
         int[] i = intArrayA;
         int m = EquipmentSlot.HEAD.ordinal();
         i[m] = 1;
      } catch (NoSuchFieldError noSuchFieldError3) {
      }

      try {
         int[] j = intArrayA;
         int n = EquipmentSlot.CHEST.ordinal();
         j[n] = 2;
      } catch (NoSuchFieldError noSuchFieldError2) {
      }

      try {
         int[] k = intArrayA;
         int o = EquipmentSlot.LEGS.ordinal();
         k[o] = 3;
      } catch (NoSuchFieldError noSuchFieldError1) {
      }

      try {
         int[] l = intArrayA;
         int p = EquipmentSlot.FEET.ordinal();
         l[p] = 4;
      } catch (NoSuchFieldError noSuchFieldError) {
      }
   }
}
