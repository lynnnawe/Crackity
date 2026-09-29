package need.clarity.modules.utility;

import java.util.Random;
import need.clarity.mixin.KeyMappingAccessor;
import need.clarity.modules.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class RightClicker extends ModuleBase {
   private final Random random;
   private long longB;
   private long longC;
   private long longD;
   private boolean flagE;

   public RightClicker() {
      super("Right Clicker", "utility");
      Random randomx = new Random();
      this.random = randomx;
      this.longB = 0L;
      this.longC = 0L;
      this.longD = 0L;
      this.flagE = false;
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         if (minecraft.level != null) {
            if (!minecraft.options.keyUse.isDown()) {
               this.flagE = false;
               this.longC = 0L;
               this.longB = 0L;
            } else if (!this.methodB(minecraft)) {
               this.flagE = false;
               this.longC = 0L;
               this.longB = 0L;
            } else {
               long n = System.nanoTime();
               if (!this.flagE) {
                  this.flagE = true;
                  this.longB = n;
                  this.longC = 0L;
                  long u = this.getCPSLowSetting();
                  this.longD = u;
               } else {
                  long v = this.longB;
                  long m = n - v;
                  this.longB = n;
                  if (m > 200000000L) {
                     m = 50000000L;
                  }

                  long p = this.longC;
                  long l = m;
                  long q = p + l;
                  this.longC = q;

                  while (true) {
                     long o = this.longC;
                     long w = this.longD;
                     if (o < w) {
                        return;
                     }

                     this.methodA(minecraft);
                     long r = this.longC;
                     long x = this.longD;
                     long s = r - x;
                     this.longC = s;
                     long t = this.getCPSLowSetting();
                     this.longD = t;
                  }
               }
            }
         }
      }
   }

   private void methodA(Minecraft minecraft) {
      KeyMappingAccessor keyMappingAccessor = (KeyMappingAccessor)minecraft.options.keyUse;
      int i = keyMappingAccessor.getClickCount() + 1;
      keyMappingAccessor.setClickCount(i);
   }

   private boolean methodB(Minecraft minecraft) {
      Boolean boolean1 = false;
      boolean bl = this.getSetting("All", boolean1);
      Boolean boolean2 = true;
      boolean bl1 = this.getSetting("Blocks", boolean2);
      Boolean boolean3 = true;
      boolean bl2 = this.getSetting("Experience Bottles", boolean3);
      if (bl) {
         return true;
      }

      ItemStack itemStack = minecraft.player.getMainHandItem();
      if (itemStack.isEmpty()) {
         return false;
      }

      if (bl1 && itemStack.getItem() instanceof BlockItem) {
         return true;
      }

      if (bl2) {
         Item item = Items.EXPERIENCE_BOTTLE;
         if (itemStack.is(item)) {
            return true;
         }
      }

      return false;
   }

   private long getCPSLowSetting() {
      Integer integer = 15;
      int j = this.getSetting("CPS Low", integer);
      Integer integer1 = 18;
      int k = this.getSetting("CPS High", integer1);
      if (j <= 0) {
         j = 1;
      }

      if (k <= 0) {
         k = 1;
      }

      int l = k;
      int n = j;
      if (l < n) {
         k = j;
      }

      int m = j;
      int o = k;
      int q = j;
      int p;
      if (o > q) {
         Random randomx = this.random;
         int s = k;
         int i = j;
         int t = s - i + 1;
         p = randomx.nextInt(t);
      } else {
         p = 0;
      }

      long r = m + p;
      return 1000000000L / r;
   }
}
