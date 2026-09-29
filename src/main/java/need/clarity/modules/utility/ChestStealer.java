package need.clarity.modules.utility;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import need.clarity.config.ClarityConfig;
import need.clarity.modules.ModuleBase;
import need.clarity.modules.movement.MoveFix;
import need.clarity.util.ContainerReflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ChestStealer extends ModuleBase {
   private static ChestStealer instance;
   private ChestStealer$Stage stageB;
   private long longC;
   private boolean flagD;
   private AbstractContainerMenu abstractContainerMenuE;
   private final List<Integer> listF;
   private int intG;
   private long longH;
   private boolean flagI;
   private static final Random random;

   public ChestStealer() {
      super("Chest Stealer", "utility");
      ChestStealer$Stage stage = ChestStealer$Stage.IDLE;
      this.stageB = stage;
      this.longC = 0L;
      this.flagD = false;
      this.abstractContainerMenuE = null;
      ArrayList arrayList = new ArrayList();
      this.listF = arrayList;
      this.intG = 0;
      this.longH = 0L;
      this.flagI = false;
      instance = this;
   }

   public static boolean isCollectionModeSetting() {
      ChestStealer chestStealer = instance;
      if (chestStealer != null && chestStealer.isEnabled()) {
         Object object = chestStealer.getSetting("Collection Mode", "Normal");
         if (!"Silent".equals(object)) {
            return false;
         }

         ChestStealer$Stage stage = chestStealer.stageB;
         ChestStealer$Stage stage1 = ChestStealer$Stage.IDLE;
         if (stage != stage1) {
            return false;
         }

         chestStealer.flagD = true;
         return true;
      } else {
         return false;
      }
   }

   @Override
   public void onDisable() {
      this.methodB();
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (this.isEnabled()) {
         if (minecraft.player != null) {
            if (minecraft.level != null) {
               Object object = this.getSetting("Collection Mode", "Normal");
               if ("Silent".equals(object)) {
                  this.methodB(minecraft);
               } else {
                  this.methodA(minecraft);
               }
            }
         }
      }
   }

   private void methodA(Minecraft minecraft) {
      Screen screen = minecraft.screen;
      if (!(screen instanceof AbstractContainerScreen)) {
         this.methodB();
      } else {
         AbstractContainerMenu abstractContainerMenu = ((AbstractContainerScreen)screen).getMenu();
         if (!(abstractContainerMenu instanceof ChestMenu)) {
            this.methodB();
         } else {
            if (!this.flagI) {
               Set set = this.getItemModeSetting();
               this.methodA(abstractContainerMenu, set);
               this.flagI = true;
            }

            this.methodA(abstractContainerMenu);
            int i = this.intG;
            int o = this.listF.size();
            if (i >= o) {
               this.methodB();
            } else {
               long l = System.currentTimeMillis();
               int j = this.getItemDelayLoSetting();
               long t = this.longH;
               long m = l - t;
               long u = j;
               if (m >= u) {
                  int n;
                  int s;
                  do {
                     this.methodA(abstractContainerMenu);
                     int k = this.intG;
                     int p = this.listF.size();
                     if (k >= p) {
                        this.methodB();
                        return;
                     }

                     MultiPlayerGameMode multiPlayerGameMode = minecraft.gameMode;
                     int q = abstractContainerMenu.containerId;
                     List list = this.listF;
                     int w = this.intG;
                     int v = (Integer)list.get(w);
                     LocalPlayer localPlayer = minecraft.player;
                     ContainerReflection.methodB(multiPlayerGameMode, q, v, localPlayer);
                     int r = this.intG + 1;
                     this.intG = r;
                     if (j != 0) {
                        break;
                     }

                     n = this.intG;
                     s = this.listF.size();
                  } while (n < s);

                  this.longH = l;
               }
            }
         }
      }
   }

   private void methodB(Minecraft minecraft) {
      switch (this.stageB) {
         case IDLE:
            this.methodC(minecraft);
            break;
         case COLLECTING:
            this.methodD(minecraft);
            break;
         case WAIT_CLOSE:
            this.methodE(minecraft);
      }

      boolean bl;
      label18: {
         Boolean boolean1 = true;
         if (this.getSetting("Bypass", boolean1)) {
            ChestStealer$Stage stage = this.stageB;
            ChestStealer$Stage stage1 = ChestStealer$Stage.IDLE;
            if (stage != stage1) {
               bl = true;
               break label18;
            }
         }

         bl = false;
      }

      MoveFix.methodA(bl);
   }

   private void methodC(Minecraft minecraft) {
      if (this.flagD) {
         AbstractContainerMenu abstractContainerMenu = minecraft.player.containerMenu;
         if (!(abstractContainerMenu instanceof ChestMenu)) {
            this.flagD = false;
         } else {
            this.abstractContainerMenuE = abstractContainerMenu;
            Set set = this.getItemModeSetting();
            this.methodA(abstractContainerMenu, set);
            this.flagD = false;
            if (this.listF.isEmpty()) {
               this.methodF(minecraft);
               this.methodB();
            } else {
               long l = methodE();
               long n = this.getItemDelayLoSetting();
               long m = l + n;
               this.longC = m;
               ChestStealer$Stage stage = ChestStealer$Stage.COLLECTING;
               this.stageB = stage;
            }
         }
      }
   }

   private void methodD(Minecraft minecraft) {
      AbstractContainerMenu abstractContainerMenu = minecraft.player.containerMenu;
      InventoryMenu inventoryMenu = minecraft.player.inventoryMenu;
      if (abstractContainerMenu == inventoryMenu) {
         this.methodB();
      } else {
         long l = methodE();
         long s = this.longC;
         if (l >= s) {
            int i = this.getItemDelayLoSetting();
            int j = minecraft.player.containerMenu.containerId;

            do {
               AbstractContainerMenu abstractContainerMenu1 = this.abstractContainerMenuE;
               this.methodA(abstractContainerMenu1);
               int k = this.intG;
               int m = this.listF.size();
               if (k >= m) {
                  long n = methodE();
                  long u = i;
                  long o = n + u;
                  this.longC = o;
                  ChestStealer$Stage stage = ChestStealer$Stage.WAIT_CLOSE;
                  this.stageB = stage;
                  return;
               }

               MultiPlayerGameMode multiPlayerGameMode = minecraft.gameMode;
               List list = this.listF;
               int v = this.intG;
               int t = (Integer)list.get(v);
               LocalPlayer localPlayer = minecraft.player;
               ContainerReflection.methodB(multiPlayerGameMode, j, t, localPlayer);
               int p = this.intG + 1;
               this.intG = p;
            } while (i == 0);

            long q = methodE();
            long w = i;
            long r = q + w;
            this.longC = r;
         }
      }
   }

   private void methodE(Minecraft minecraft) {
      AbstractContainerMenu abstractContainerMenu = minecraft.player.containerMenu;
      InventoryMenu inventoryMenu = minecraft.player.inventoryMenu;
      if (abstractContainerMenu == inventoryMenu) {
         this.methodB();
      } else {
         long l = methodE();
         long m = this.longC;
         if (l >= m) {
            this.methodF(minecraft);
            this.methodB();
         }
      }
   }

   private void methodA(AbstractContainerMenu abstractContainerMenu, Set<Item> set) {
      this.listF.clear();
      this.intG = 0;
      Object object = this.getSetting("Item Mode", "All");
      boolean bl1;
      if ("Whitelist".equals(object) && !set.isEmpty()) {
         bl1 = true;
      } else {
         bl1 = false;
      }

      boolean bl = bl1;
      int j = abstractContainerMenu.slots.size() - 36;

      for (int i = 0; i < j; i++) {
         int k = i;
         ItemStack itemStack = abstractContainerMenu.getSlot(k).getItem();
         if (!itemStack.isEmpty()) {
            if (bl) {
               Item item = itemStack.getItem();
               if (!set.contains(item)) {
                  continue;
               }
            }

            List list = this.listF;
            Integer integer = i;
            list.add(integer);
         }
      }

      Object object1 = this.getSetting("Slot Order", "Normal");
      if ("Random".equals(object1)) {
         List list1 = this.listF;
         Random randomx = random;
         Collections.shuffle(list1, randomx);
      }
   }

   private void methodA(AbstractContainerMenu abstractContainerMenu) {
      if (abstractContainerMenu != null) {
         while (true) {
            int i = this.intG;
            int k = this.listF.size();
            if (i >= k) {
               return;
            }

            List list = this.listF;
            int l = this.intG;
            int j = (Integer)list.get(l);
            int m = abstractContainerMenu.slots.size();
            if (j < m && !abstractContainerMenu.getSlot(j).getItem().isEmpty()) {
               return;
            }

            int n = this.intG + 1;
            this.intG = n;
         }
      }
   }

   private void methodF(Minecraft minecraft) {
      if (minecraft.player != null) {
         if (this.abstractContainerMenuE != null) {
            ClientPacketListener clientPacketListener = minecraft.player.connection;
            int i = this.abstractContainerMenuE.containerId;
            ServerboundContainerClosePacket serverboundContainerClosePacket = new ServerboundContainerClosePacket(i);
            clientPacketListener.send(serverboundContainerClosePacket);
            LocalPlayer localPlayer = minecraft.player;
            InventoryMenu inventoryMenu = minecraft.player.inventoryMenu;
            localPlayer.containerMenu = inventoryMenu;
         }
      }
   }

   private void methodB() {
      MoveFix.methodA(false);
      ChestStealer$Stage stage = ChestStealer$Stage.IDLE;
      this.stageB = stage;
      this.longC = 0L;
      this.flagD = false;
      this.abstractContainerMenuE = null;
      this.listF.clear();
      this.intG = 0;
      this.flagI = false;
      this.longH = 0L;
   }

   private Set<Item> getItemModeSetting() {
      Object object = this.getSetting("Item Mode", "All");
      if (!"Whitelist".equals(object)) {
         return Collections.emptySet();
      }

      try {
         JsonObject jsonObject = ClarityConfig.getCurrent().getAsJsonObject("Chest Stealer");
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

   private int getItemDelayLoSetting() {
      Integer integer = 50;
      int i = this.getSetting("Item Delay_lo", integer);
      Integer integer1 = 100;
      int j = this.getSetting("Item Delay_hi", integer1);
      if (i >= j) {
         return Math.max(0, i);
      }

      Random randomx = random;
      int l = j - i + 1;
      int k = randomx.nextInt(l);
      return i + k;
   }

   private static long methodE() {
      return System.currentTimeMillis();
   }

   static {
      Random randomx = new Random();
      random = randomx;
   }
}
