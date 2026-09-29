package need.clarity.modules.utility;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import need.clarity.config.ClarityConfig;
import need.clarity.modules.ModuleBase;
import need.clarity.modules.movement.MoveFix;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.HashedStack;
import net.minecraft.network.HashedPatchMap.HashGenerator;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class InventoryCleaner extends ModuleBase {
   private InventoryCleaner$Stage stageA;
   private long longB;
   private final List<Integer> listC;
   private int intD;
   private long longE;
   private static final Random random;

   public InventoryCleaner() {
      super("Inventory Cleaner", "utility");
      InventoryCleaner$Stage stage = InventoryCleaner$Stage.IDLE;
      this.stageA = stage;
      this.longB = 0L;
      ArrayList arrayList = new ArrayList();
      this.listC = arrayList;
      this.intD = 0;
      this.longE = 0L;
   }

   @Override
   public void onDisable() {
      MoveFix.methodA(false);
      this.methodA();
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (this.isEnabled()) {
         if (minecraft.player != null) {
            if (minecraft.level != null) {
               Set set = this.methodB();
               if (!set.isEmpty()) {
                  Boolean boolean_ = false;
                  if (this.getSetting("Silent", boolean_)) {
                     this.methodB(minecraft, set);
                  } else {
                     this.methodA(minecraft, set);
                  }
               }
            }
         }
      }
   }

   private void methodA(Minecraft minecraft, Set<Item> set) {
      if (minecraft.screen instanceof InventoryScreen) {
         long l = System.currentTimeMillis();
         int k = this.methodA("Item Delay_lo", "Item Delay_hi");
         long o = this.longE;
         long m = l - o;
         long p = k;
         if (m >= p) {
            Inventory inventory = minecraft.player.getInventory();

            for (int i = 0; i < 2; i++) {
               byte d;
               if (i == 0) {
                  d = 9;
               } else {
                  d = 0;
               }

               byte b = d;
               byte e;
               if (i == 0) {
                  e = 36;
               } else {
                  e = 9;
               }

               byte c = e;

               for (int j = b; j < c; j++) {
                  int n = j;
                  ItemStack itemStack = inventory.getItem(n);
                  if (!itemStack.isEmpty()) {
                     Item item = itemStack.getItem();
                     if (set.contains(item)) {
                        int q = j;
                        this.methodA(minecraft, q);
                        this.longE = l;
                        return;
                     }
                  }
               }
            }
         }
      }
   }

   private void methodB(Minecraft minecraft, Set<Item> set) {
      if (minecraft.screen != null && !(minecraft.screen instanceof InventoryScreen)) {
         InventoryCleaner$Stage stage1 = this.stageA;
         InventoryCleaner$Stage stage = InventoryCleaner$Stage.IDLE;
         if (stage1 != stage) {
            this.methodC(minecraft);
         }

         MoveFix.methodA(false);
      } else {
         switch (this.stageA) {
            case IDLE:
               this.methodC(minecraft, set);
               break;
            case WAIT_OPEN:
               this.methodD(minecraft, set);
               break;
            case THROWING:
               this.methodE(minecraft, set);
               break;
            case WAIT_CLOSE:
               this.methodA(minecraft);
         }

         boolean bl;
         label27: {
            Boolean boolean1 = true;
            if (this.getSetting("Bypass", boolean1)) {
               InventoryCleaner$Stage stage2 = this.stageA;
               InventoryCleaner$Stage stage3 = InventoryCleaner$Stage.IDLE;
               if (stage2 != stage3) {
                  bl = true;
                  break label27;
               }
            }

            bl = false;
         }

         MoveFix.methodA(bl);
      }
   }

   private void methodC(Minecraft minecraft, Set<Item> set) {
      this.listC.clear();
      this.intD = 0;
      Inventory inventory = minecraft.player.getInventory();

      for (int i = 0; i < 2; i++) {
         byte d;
         if (i == 0) {
            d = 9;
         } else {
            d = 0;
         }

         byte b = d;
         byte e;
         if (i == 0) {
            e = 36;
         } else {
            e = 9;
         }

         byte c = e;

         for (int j = b; j < c; j++) {
            int k = j;
            ItemStack itemStack = inventory.getItem(k);
            if (!itemStack.isEmpty()) {
               Item item = itemStack.getItem();
               if (set.contains(item)) {
                  List list = this.listC;
                  Integer integer = j;
                  list.add(integer);
               }
            }
         }
      }

      if (!this.listC.isEmpty()) {
         long l = methodC();
         long n = this.methodA("Open Delay_lo", "Open Delay_hi");
         long m = l + n;
         this.longB = m;
         InventoryCleaner$Stage stage = InventoryCleaner$Stage.WAIT_OPEN;
         this.stageA = stage;
      }
   }

   private void methodD(Minecraft minecraft, Set<Item> set) {
      long l = methodC();
      long u = this.longB;
      if (l >= u) {
         this.methodF(minecraft, set);
         int i = this.intD;
         int k = this.listC.size();
         if (i >= k) {
            long s = methodC();
            long z = this.methodA("Close Delay_lo", "Close Delay_hi");
            long t = s + z;
            this.longB = t;
            InventoryCleaner$Stage stage2 = InventoryCleaner$Stage.WAIT_CLOSE;
            this.stageA = stage2;
         } else {
            List list = this.listC;
            int w = this.intD;
            int v = (Integer)list.get(w);
            this.methodA(minecraft, v);
            int m = this.intD + 1;
            this.intD = m;
            int j = this.intD;
            int n = this.listC.size();
            if (j < n) {
               long q = methodC();
               long y = this.methodA("Silent Item Delay_lo", "Silent Item Delay_hi");
               long r = q + y;
               this.longB = r;
               InventoryCleaner$Stage stage1 = InventoryCleaner$Stage.THROWING;
               this.stageA = stage1;
            } else {
               long o = methodC();
               long x = this.methodA("Close Delay_lo", "Close Delay_hi");
               long p = o + x;
               this.longB = p;
               InventoryCleaner$Stage stage = InventoryCleaner$Stage.WAIT_CLOSE;
               this.stageA = stage;
            }
         }
      }
   }

   private void methodE(Minecraft minecraft, Set<Item> set) {
      long l = methodC();
      long u = this.longB;
      if (l >= u) {
         this.methodF(minecraft, set);
         int i = this.intD;
         int k = this.listC.size();
         if (i >= k) {
            long m = methodC();
            long w = this.methodA("Close Delay_lo", "Close Delay_hi");
            long n = m + w;
            this.longB = n;
            InventoryCleaner$Stage stage = InventoryCleaner$Stage.WAIT_CLOSE;
            this.stageA = stage;
         } else {
            List list = this.listC;
            int x = this.intD;
            int v = (Integer)list.get(x);
            this.methodA(minecraft, v);
            int o = this.intD + 1;
            this.intD = o;
            int j = this.intD;
            int p = this.listC.size();
            if (j < p) {
               long q = methodC();
               long y = this.methodA("Silent Item Delay_lo", "Silent Item Delay_hi");
               long r = q + y;
               this.longB = r;
            } else {
               long s = methodC();
               long z = this.methodA("Close Delay_lo", "Close Delay_hi");
               long t = s + z;
               this.longB = t;
               InventoryCleaner$Stage stage1 = InventoryCleaner$Stage.WAIT_CLOSE;
               this.stageA = stage1;
            }
         }
      }
   }

   private void methodF(Minecraft minecraft, Set<Item> set) {
      while (true) {
         int i = this.intD;
         int k = this.listC.size();
         if (i >= k) {
            return;
         }

         List list = this.listC;
         int l = this.intD;
         int j = (Integer)list.get(l);
         ItemStack itemStack = minecraft.player.getInventory().getItem(j);
         if (!itemStack.isEmpty()) {
            Item item = itemStack.getItem();
            if (set.contains(item)) {
               return;
            }
         }

         int m = this.intD + 1;
         this.intD = m;
      }
   }

   private void methodA(Minecraft minecraft) {
      long l = methodC();
      long m = this.longB;
      if (l >= m) {
         this.methodB(minecraft);
         this.methodA();
      }
   }

   private void methodA(Minecraft minecraft, int i) {
      int j = methodA(i);
      if (!minecraft.player.getInventory().getItem(i).isEmpty()) {
         int k = minecraft.player.inventoryMenu.containerId;
         int l = minecraft.player.inventoryMenu.getStateId();
         HashGenerator hashGenerator = minecraft.player.connection.decoratedHashOpsGenenerator();
         Int2ObjectArrayMap int2ObjectArrayMap = new Int2ObjectArrayMap();
         HashedStack hashedStack1 = HashedStack.create(ItemStack.EMPTY, hashGenerator);
         int2ObjectArrayMap.put(j, hashedStack1);
         ClientPacketListener clientPacketListener = minecraft.player.connection;
         short s = (short)j;
         ContainerInput containerInput = ContainerInput.THROW;
         HashedStack hashedStack = HashedStack.EMPTY;
         ServerboundContainerClickPacket serverboundContainerClickPacket = new ServerboundContainerClickPacket(
            k, l, s, (byte)1, containerInput, int2ObjectArrayMap, hashedStack
         );
         clientPacketListener.send(serverboundContainerClickPacket);
         Inventory inventory = minecraft.player.getInventory();
         ItemStack itemStack = ItemStack.EMPTY;
         inventory.setItem(i, itemStack);
      }
   }

   private void methodB(Minecraft minecraft) {
      ClientPacketListener clientPacketListener = minecraft.player.connection;
      int i = minecraft.player.inventoryMenu.containerId;
      ServerboundContainerClosePacket serverboundContainerClosePacket = new ServerboundContainerClosePacket(i);
      clientPacketListener.send(serverboundContainerClosePacket);
   }

   private void methodC(Minecraft minecraft) {
      InventoryCleaner$Stage stage1 = this.stageA;
      InventoryCleaner$Stage stage = InventoryCleaner$Stage.IDLE;
      if (stage1 != stage) {
         this.methodB(minecraft);
      }

      MoveFix.methodA(false);
      this.methodA();
   }

   private void methodA() {
      InventoryCleaner$Stage stage = InventoryCleaner$Stage.IDLE;
      this.stageA = stage;
      this.longB = 0L;
      this.intD = 0;
      this.listC.clear();
   }

   private Set<Item> methodB() {
      try {
         JsonObject jsonObject = ClarityConfig.getCurrent().getAsJsonObject("Inventory Cleaner");
         if (jsonObject == null) {
            return Collections.emptySet();
         }

         JsonObject jsonObject1 = jsonObject.getAsJsonObject("values");
         if (jsonObject1 == null) {
            return Collections.emptySet();
         }

         JsonElement jsonElement = jsonObject1.get("Items");
         if (jsonElement != null && jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            if (jsonArray.size() == 0) {
               return Collections.emptySet();
            }

            HashSet hashSet = new HashSet();

            for (JsonElement jsonElement1 : jsonArray) {
               if (jsonElement1.isJsonPrimitive()) {
                  try {
                     String string = jsonElement1.getAsString();
                     if (!string.contains(":")) {
                        string = "minecraft:" + string;
                     }

                     DefaultedRegistry defaultedRegistry = BuiltInRegistries.ITEM;
                     Identifier identifier = Identifier.parse(string);
                     Item item = (Item)defaultedRegistry.getValue(identifier);
                     Item item1 = Items.AIR;
                     if (item != item1) {
                        hashSet.add(item);
                     }
                  } catch (Exception exception) {
                  }
               }
            }

            return hashSet;
         } else {
            return Collections.emptySet();
         }
      } catch (Exception exception1) {
         return Collections.emptySet();
      }
   }

   private static int methodA(int i) {
      int j;
      if (i < 9) {
         j = i + 36;
      } else {
         j = i;
      }

      return j;
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

   private static long methodC() {
      return System.currentTimeMillis();
   }

   static {
      Random randomx = new Random();
      random = randomx;
   }
}
