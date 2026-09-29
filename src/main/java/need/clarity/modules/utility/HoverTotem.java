package need.clarity.modules.utility;

import java.util.List;
import java.util.Random;
import need.clarity.modules.ModuleBase;
import need.clarity.util.ContainerReflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class HoverTotem extends ModuleBase {
   private boolean flagA = false;
   private long longB = -1L;
   private boolean flagC = false;
   private long longD = -1L;
   private int intE = -1;
   private final Random random;

   public HoverTotem() {
      super("Hover Totem", "Utility");
      Random randomx = new Random();
      this.random = randomx;
   }

   @Override
   public void onDisable() {
      this.methodA();
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         if (minecraft.gameMode != null) {
            if (minecraft.screen instanceof AbstractContainerScreen abstractContainerScreen) {
               this.methodA(minecraft, abstractContainerScreen);
            } else {
               this.methodA();
            }
         }
      }
   }

   private void methodA(Minecraft minecraft, AbstractContainerScreen<?> abstractContainerScreen) {
      Slot slot;
      boolean bl;
      label54: {
         slot = ContainerReflection.methodA(abstractContainerScreen);
         if (slot != null && slot.hasItem()) {
            ItemStack itemStack = slot.getItem();
            Item item = Items.TOTEM_OF_UNDYING;
            if (itemStack.is(item)) {
               bl = true;
               break label54;
            }
         }

         bl = false;
      }

      if (bl) {
         int i = methodA(abstractContainerScreen, slot);
         if (slot.container instanceof Inventory && slot.getContainerSlot() == 40) {
            this.methodA();
         } else {
            if (this.intE != -1) {
               int j = this.intE;
               if (i != j) {
                  this.methodA();
               }
            }

            long l = System.currentTimeMillis();
            ItemStack itemStack1 = minecraft.player.getOffhandItem();
            Item item1 = Items.TOTEM_OF_UNDYING;
            if (!itemStack1.is(item1) && !this.flagA) {
               this.flagA = true;
               long o = this.getHoverDelaySetting();
               long m = l + o;
               this.longB = m;
               this.intE = i;
            }

            if (this.flagA) {
               long n = this.longB;
               if (l >= n) {
                  ItemStack itemStack2 = minecraft.player.getOffhandItem();
                  Item item2 = Items.TOTEM_OF_UNDYING;
                  if (!itemStack2.is(item2)) {
                     MultiPlayerGameMode multiPlayerGameMode = minecraft.gameMode;
                     int k = abstractContainerScreen.getMenu().containerId;
                     LocalPlayer localPlayer = minecraft.player;
                     ContainerReflection.methodA(multiPlayerGameMode, k, i, localPlayer);
                  }

                  this.flagA = false;
                  this.longB = -1L;
               }
            }

            Boolean boolean1 = false;
            if (!this.getSetting("Fill Hotbar", boolean1)) {
               this.flagC = false;
               this.longD = -1L;
            } else {
               Object object = this.getSetting("Fill Hotbar Mode", "Slot");
               if ("Empty".equals(object)) {
                  this.methodB(minecraft, abstractContainerScreen, i, l);
               } else {
                  this.methodA(minecraft, abstractContainerScreen, i, l);
               }
            }
         }
      } else {
         this.methodA();
      }
   }

   private void methodA(Minecraft minecraft, AbstractContainerScreen<?> abstractContainerScreen, int i, long l) {
      ItemStack itemStack = minecraft.player.getOffhandItem();
      Item item = Items.TOTEM_OF_UNDYING;
      if (!itemStack.is(item)) {
         this.flagC = false;
         this.longD = -1L;
      } else {
         Integer integer = 1;
         int j = this.getSetting("Slot", integer) - 1;
         ItemStack itemStack1 = minecraft.player.getInventory().getItem(j);
         Item item1 = Items.TOTEM_OF_UNDYING;
         if (itemStack1.is(item1)) {
            this.flagC = false;
            this.longD = -1L;
         } else {
            if (!this.flagC) {
               this.flagC = true;
               long o = this.getHoverDelaySetting();
               long m = l + o;
               this.longD = m;
            }

            long n = this.longD;
            if (l >= n) {
               MultiPlayerGameMode multiPlayerGameMode = minecraft.gameMode;
               int k = abstractContainerScreen.getMenu().containerId;
               LocalPlayer localPlayer = minecraft.player;
               ContainerReflection.methodA(multiPlayerGameMode, k, i, j, localPlayer);
               this.flagC = false;
               this.longD = -1L;
            }
         }
      }
   }

   private void methodB(Minecraft minecraft, AbstractContainerScreen<?> abstractContainerScreen, int i, long l) {
      if (!this.flagC) {
         this.flagC = true;
         long o = this.getHoverDelaySetting();
         long m = l + o;
         this.longD = m;
      }

      long n = this.longD;
      if (l >= n) {
         MultiPlayerGameMode multiPlayerGameMode = minecraft.gameMode;
         int j = abstractContainerScreen.getMenu().containerId;
         LocalPlayer localPlayer = minecraft.player;
         ContainerReflection.methodB(multiPlayerGameMode, j, i, localPlayer);
         this.flagC = false;
         this.longD = -1L;
      }
   }

   private static int methodA(AbstractContainerScreen<?> abstractContainerScreen, Slot slot) {
      return abstractContainerScreen.getMenu().slots.indexOf(slot);
   }

   private void methodA() {
      this.flagA = false;
      this.longB = -1L;
      this.flagC = false;
      this.longD = -1L;
      this.intE = -1;
   }

   private long getHoverDelaySetting() {
      Object object = this.getSetting("Hover Delay", null);
      long u;
      long v;
      if (object instanceof List list && list.size() >= 2) {
         u = methodA(list.get(0));
         v = methodA(list.get(1));
      } else {
         u = methodA(object);
         v = u;
      }

      long n = v;
      long r = u;
      if (n < r) {
         v = u;
      }

      long o = u;
      long s = v;
      long q;
      if (o == s) {
         q = u;
      } else {
         long p = u;
         double d = this.random.nextDouble();
         long l = v;
         long m = u;
         double e = l - m + 1L;
         long t = (long)(d * e);
         q = p + t;
      }

      return q;
   }

   private static long methodA(Object object) {
      if (object == null) {
         return 0L;
      }

      if (object instanceof Number) {
         return ((Number)object).longValue();
      }

      try {
         return Long.parseLong(object.toString());
      } catch (NumberFormatException numberFormatException) {
         return 0L;
      }
   }
}
