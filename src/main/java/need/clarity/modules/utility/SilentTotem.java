package need.clarity.modules.utility;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import java.util.Random;
import need.clarity.modules.ModuleBase;
import need.clarity.modules.movement.MoveFix;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.HashedStack;
import net.minecraft.network.HashedPatchMap.HashGenerator;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SilentTotem extends ModuleBase {
   private SilentTotem$Stage stageA;
   private long longB;
   private static final Random random;

   public SilentTotem() {
      super("Silent Totem", "utility");
      SilentTotem$Stage stage = SilentTotem$Stage.IDLE;
      this.stageA = stage;
      this.longB = 0L;
   }

   @Override
   public void onDisable() {
      MoveFix.methodA(false);
      SilentTotem$Stage stage = SilentTotem$Stage.IDLE;
      this.stageA = stage;
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (this.isEnabled()) {
         if (minecraft.player != null) {
            if (minecraft.level != null) {
               if (minecraft.screen != null && !(minecraft.screen instanceof InventoryScreen)) {
                  SilentTotem$Stage stage1 = this.stageA;
                  SilentTotem$Stage stage3 = SilentTotem$Stage.IDLE;
                  if (stage1 != stage3) {
                     this.methodE(minecraft);
                  }
               } else {
                  switch (this.stageA) {
                     case IDLE:
                        this.methodA(minecraft);
                        break;
                     case WAIT_OPEN:
                        this.methodB(minecraft);
                        break;
                     case WAIT_CLOSE:
                        this.methodC(minecraft);
                  }

                  boolean bl;
                  label34: {
                     Boolean boolean1 = true;
                     if (this.getSetting("Bypass", boolean1)) {
                        SilentTotem$Stage stage = this.stageA;
                        SilentTotem$Stage stage2 = SilentTotem$Stage.IDLE;
                        if (stage != stage2) {
                           bl = true;
                           break label34;
                        }
                     }

                     bl = false;
                  }

                  MoveFix.methodA(bl);
               }
            }
         }
      }
   }

   private void methodA(Minecraft minecraft) {
      if (!this.methodF(minecraft)) {
         if (this.methodG(minecraft) != -1) {
            long l = methodA();
            long n = this.methodA("Open Delay_lo", "Open Delay_hi");
            long m = l + n;
            this.longB = m;
            SilentTotem$Stage stage = SilentTotem$Stage.WAIT_OPEN;
            this.stageA = stage;
         }
      }
   }

   private void methodB(Minecraft minecraft) {
      long l = methodA();
      long o = this.longB;
      if (l >= o) {
         if (this.methodF(minecraft)) {
            this.methodE(minecraft);
         } else {
            int j = this.methodG(minecraft);
            if (j == -1) {
               SilentTotem$Stage stage = SilentTotem$Stage.IDLE;
               this.stageA = stage;
            } else {
               int k;
               if (j < 9) {
                  k = j + 36;
               } else {
                  k = j;
               }

               int i = k;
               this.methodA(minecraft, i, j);
               long m = methodA();
               long p = this.methodA("Close Delay_lo", "Close Delay_hi");
               long n = m + p;
               this.longB = n;
               SilentTotem$Stage stage1 = SilentTotem$Stage.WAIT_CLOSE;
               this.stageA = stage1;
            }
         }
      }
   }

   private void methodC(Minecraft minecraft) {
      long l = methodA();
      long m = this.longB;
      if (l >= m) {
         this.methodD(minecraft);
         SilentTotem$Stage stage = SilentTotem$Stage.IDLE;
         this.stageA = stage;
      }
   }

   private void methodA(Minecraft minecraft, int i, int j) {
      int k = minecraft.player.inventoryMenu.containerId;
      int l = minecraft.player.inventoryMenu.getStateId();
      HashGenerator hashGenerator = minecraft.player.connection.decoratedHashOpsGenenerator();
      ItemStack itemStack = minecraft.player.getInventory().getItem(j);
      ItemStack itemStack1 = minecraft.player.getOffhandItem();
      Int2ObjectArrayMap int2ObjectArrayMap = new Int2ObjectArrayMap();
      ItemStack itemStack2;
      if (itemStack1.isEmpty()) {
         itemStack2 = ItemStack.EMPTY;
      } else {
         itemStack2 = itemStack1;
      }

      HashedStack hashedStack1 = HashedStack.create(itemStack2, hashGenerator);
      int2ObjectArrayMap.put(i, hashedStack1);
      HashedStack hashedStack2 = HashedStack.create(itemStack, hashGenerator);
      int2ObjectArrayMap.put(45, hashedStack2);
      ClientPacketListener clientPacketListener = minecraft.player.connection;
      short s = (short)i;
      ContainerInput containerInput = ContainerInput.SWAP;
      HashedStack hashedStack = HashedStack.EMPTY;
      ServerboundContainerClickPacket serverboundContainerClickPacket = new ServerboundContainerClickPacket(
         k, l, s, (byte)40, containerInput, int2ObjectArrayMap, hashedStack
      );
      clientPacketListener.send(serverboundContainerClickPacket);
      Inventory inventory = minecraft.player.getInventory();
      ItemStack itemStack4;
      if (itemStack1.isEmpty()) {
         itemStack4 = ItemStack.EMPTY;
      } else {
         itemStack4 = itemStack1.copy();
      }

      inventory.setItem(j, itemStack4);
      Inventory inventory1 = minecraft.player.getInventory();
      ItemStack itemStack3 = itemStack.copy();
      inventory1.setItem(40, itemStack3);
   }

   private void methodD(Minecraft minecraft) {
      ClientPacketListener clientPacketListener = minecraft.player.connection;
      int i = minecraft.player.inventoryMenu.containerId;
      ServerboundContainerClosePacket serverboundContainerClosePacket = new ServerboundContainerClosePacket(i);
      clientPacketListener.send(serverboundContainerClosePacket);
   }

   private void methodE(Minecraft minecraft) {
      SilentTotem$Stage stage1 = this.stageA;
      SilentTotem$Stage stage = SilentTotem$Stage.IDLE;
      if (stage1 != stage) {
         this.methodD(minecraft);
      }

      MoveFix.methodA(false);
      SilentTotem$Stage stage2 = SilentTotem$Stage.IDLE;
      this.stageA = stage2;
   }

   private boolean methodF(Minecraft minecraft) {
      ItemStack itemStack = minecraft.player.getOffhandItem();
      Item item = Items.TOTEM_OF_UNDYING;
      return itemStack.is(item);
   }

   private int methodG(Minecraft minecraft) {
      Inventory inventory = minecraft.player.getInventory();

      for (int i = 9; i < 36; i++) {
         int k = i;
         ItemStack itemStack1 = inventory.getItem(k);
         Item item1 = Items.TOTEM_OF_UNDYING;
         if (itemStack1.is(item1)) {
            return i;
         }
      }

      for (int l = 0; l < 9; l++) {
         int j = l;
         ItemStack itemStack = inventory.getItem(j);
         Item item = Items.TOTEM_OF_UNDYING;
         if (itemStack.is(item)) {
            return l;
         }
      }

      return -1;
   }

   private int methodA(String string, String string1) {
      Integer integer = 0;
      int i = this.getSetting(string, integer);
      Integer integer1 = 0;
      int j = this.getSetting(string1, integer1);
      if (i >= j) {
         return Math.max(0, i);
      }

      Random randomx = random;
      int l = j - i + 1;
      int k = randomx.nextInt(l);
      return i + k;
   }

   private static long methodA() {
      return System.currentTimeMillis();
   }

   static {
      Random randomx = new Random();
      random = randomx;
   }
}
