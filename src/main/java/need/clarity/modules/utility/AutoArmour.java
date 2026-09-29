package need.clarity.modules.utility;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import need.clarity.modules.ModuleBase;
import need.clarity.modules.movement.MoveFix;
import need.clarity.util.ContainerReflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.HashedStack;
import net.minecraft.network.HashedPatchMap.HashGenerator;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemAttributeModifiers.Entry;

public class AutoArmour extends ModuleBase {
   private static final int[] intArrayA;
   private static final EquipmentSlot[] equipmentSlotArrayB;
   private AutoArmour$Stage stageC;
   private long longD;
   private final List<AutoArmour$SwapJob> listE;
   private long longF;
   private static final Random random;

   public AutoArmour() {
      super("Auto Armour", "utility");
      AutoArmour$Stage stage = AutoArmour$Stage.IDLE;
      this.stageC = stage;
      this.longD = 0L;
      ArrayList arrayList = new ArrayList();
      this.listE = arrayList;
      this.longF = 0L;
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
               Boolean boolean_ = false;
               if (this.getSetting("Silent", boolean_)) {
                  this.methodB(minecraft);
               } else {
                  this.methodA(minecraft);
               }
            }
         }
      }
   }

   private void methodA(Minecraft minecraft) {
      if (minecraft.screen instanceof InventoryScreen) {
         long l = System.currentTimeMillis();
         int j = this.methodA("Delay_lo", "Delay_hi");
         long q = this.longF;
         long m = l - q;
         long r = j;
         if (m >= r) {
            int i = 0;

            while (true) {
               int k = i;
               int o = intArrayA.length;
               if (k >= o) {
                  return;
               }

               int s = i;
               int n = this.methodA(minecraft, s);
               if (n != -1 && minecraft.gameMode != null) {
                  MultiPlayerGameMode multiPlayerGameMode = minecraft.gameMode;
                  int p = minecraft.player.inventoryMenu.containerId;
                  int t = this.methodA(n);
                  LocalPlayer localPlayer = minecraft.player;
                  ContainerReflection.methodB(multiPlayerGameMode, p, t, localPlayer);
                  this.longF = l;
                  return;
               }

               i++;
            }
         }
      }
   }

   private void methodB(Minecraft minecraft) {
      if (minecraft.screen != null && !(minecraft.screen instanceof InventoryScreen)) {
         AutoArmour$Stage stage1 = this.stageC;
         AutoArmour$Stage stage = AutoArmour$Stage.IDLE;
         if (stage1 != stage) {
            this.methodG(minecraft);
         }

         MoveFix.methodA(false);
      } else {
         switch (this.stageC) {
            case IDLE:
               this.methodC(minecraft);
               break;
            case WAIT_OPEN:
               this.methodD(minecraft);
               break;
            case WAIT_CLOSE:
               this.methodE(minecraft);
         }

         boolean bl;
         label26: {
            Boolean boolean1 = true;
            if (this.getSetting("Bypass", boolean1)) {
               AutoArmour$Stage stage2 = this.stageC;
               AutoArmour$Stage stage3 = AutoArmour$Stage.IDLE;
               if (stage2 != stage3) {
                  bl = true;
                  break label26;
               }
            }

            bl = false;
         }

         MoveFix.methodA(bl);
      }
   }

   private void methodC(Minecraft minecraft) {
      this.listE.clear();
      int k = 0;

      while (true) {
         int l = k;
         int n = intArrayA.length;
         if (l >= n) {
            if (this.listE.isEmpty()) {
               return;
            }

            long o = methodB();
            long q = this.methodA("Open Delay_lo", "Open Delay_hi");
            long p = o + q;
            this.longD = p;
            AutoArmour$Stage stage = AutoArmour$Stage.WAIT_OPEN;
            this.stageC = stage;
            return;
         }

         int i = k;
         int m = this.methodA(minecraft, i);
         if (m != -1) {
            List list = this.listE;
            int j = k;
            AutoArmour$SwapJob swapJob = new AutoArmour$SwapJob(j, m);
            list.add(swapJob);
         }

         k++;
      }
   }

   private void methodD(Minecraft minecraft) {
      long l = methodB();
      long o = this.longD;
      if (l >= o) {
         for (AutoArmour$SwapJob swapJob : this.listE) {
            Inventory inventory = minecraft.player.getInventory();
            int i = swapJob.intB;
            ItemStack itemStack = inventory.getItem(i);
            if (!itemStack.isEmpty()) {
               EquipmentSlot[] equipmentSlots1 = equipmentSlotArrayB;
               int r = swapJob.intA;
               EquipmentSlot equipmentSlot1 = equipmentSlots1[r];
               if (this.methodA(itemStack, equipmentSlot1)) {
                  EquipmentSlot[] equipmentSlots = equipmentSlotArrayB;
                  int j = swapJob.intA;
                  EquipmentSlot equipmentSlot = equipmentSlots[j];
                  if (!minecraft.player.getItemBySlot(equipmentSlot).isEmpty()) {
                     int k = swapJob.intA;
                     this.methodB(minecraft, k);
                  }

                  int p = swapJob.intB;
                  int s = swapJob.intA;
                  this.methodA(minecraft, p, s);
               }
            }
         }

         this.listE.clear();
         long m = methodB();
         long q = this.methodA("Close Delay_lo", "Close Delay_hi");
         long n = m + q;
         this.longD = n;
         AutoArmour$Stage stage = AutoArmour$Stage.WAIT_CLOSE;
         this.stageC = stage;
      }
   }

   private void methodE(Minecraft minecraft) {
      long l = methodB();
      long m = this.longD;
      if (l >= m) {
         this.methodF(minecraft);
         this.methodA();
      }
   }

   private int methodA(Minecraft minecraft, int i) {
      EquipmentSlot equipmentSlot = equipmentSlotArrayB[i];
      ItemStack itemStack = minecraft.player.getItemBySlot(equipmentSlot);
      int j = -1;
      int k = this.methodA(itemStack);
      Inventory inventory = minecraft.player.getInventory();

      for (int l = 0; l < 2; l++) {
         byte e;
         if (l == 0) {
            e = 9;
         } else {
            e = 0;
         }

         byte b = e;
         byte d;
         if (l == 0) {
            d = 36;
         } else {
            d = 9;
         }

         byte c = d;

         for (int m = b; m < c; m++) {
            int o = m;
            ItemStack itemStack1 = inventory.getItem(o);
            if (!itemStack1.isEmpty() && this.methodA(itemStack1, equipmentSlot)) {
               int n = this.methodB(itemStack1, equipmentSlot);
               int p = k;
               if (n > p) {
                  k = n;
                  j = m;
               }
            }
         }
      }

      return j;
   }

   private boolean methodA(ItemStack itemStack, EquipmentSlot equipmentSlot) {
      if (itemStack.isEmpty()) {
         return false;
      }

      int[] i = AutoArmour$1.intArrayA;
      int j = equipmentSlot.ordinal();

      return switch (i[j]) {
         case 1 -> {
            TagKey tagKey1 = ItemTags.HEAD_ARMOR;
            yield itemStack.is(tagKey1);
         }
         case 2 -> {
            TagKey tagKey = ItemTags.CHEST_ARMOR;
            yield itemStack.is(tagKey);
         }
         case 3 -> {
            TagKey tagKey2 = ItemTags.LEG_ARMOR;
            yield itemStack.is(tagKey2);
         }
         case 4 -> {
            TagKey tagKey3 = ItemTags.FOOT_ARMOR;
            yield itemStack.is(tagKey3);
         }
         default -> false;
      };
   }

   private int methodA(ItemStack itemStack) {
      if (itemStack.isEmpty()) {
         return 0;
      }

      double d = 0.0;
      double e = 0.0;
      double f = 0.0;

      try {
         DataComponentType dataComponentType = DataComponents.ATTRIBUTE_MODIFIERS;
         ItemAttributeModifiers itemAttributeModifiers = (ItemAttributeModifiers)itemStack.get(dataComponentType);
         if (itemAttributeModifiers == null) {
            return 0;
         }

         for (Entry entry : itemAttributeModifiers.modifiers()) {
            Holder holder = entry.attribute();
            double i = entry.modifier().amount();
            Holder holder1 = Attributes.ARMOR;
            if (holder.is(holder1)) {
               d += i;
            } else {
               Holder holder2 = Attributes.ARMOR_TOUGHNESS;
               if (holder.is(holder2)) {
                  e += i;
               } else {
                  Holder holder3 = Attributes.KNOCKBACK_RESISTANCE;
                  if (holder.is(holder3)) {
                     f += i;
                  }
               }
            }
         }
      } catch (Throwable throwable) {
         return 0;
      }

      double g = d * 1000.0;
      double j = e * 100.0;
      double h = g + j;
      double k = f * 10.0;
      return (int)(h + k);
   }

   private int methodB(ItemStack itemStack, EquipmentSlot equipmentSlot) {
      if (itemStack.isEmpty()) {
         return 0;
      }

      double d = 0.0;
      double e = 0.0;
      double f = 0.0;

      try {
         DataComponentType dataComponentType = DataComponents.ATTRIBUTE_MODIFIERS;
         ItemAttributeModifiers itemAttributeModifiers = (ItemAttributeModifiers)itemStack.get(dataComponentType);
         if (itemAttributeModifiers == null) {
            return 0;
         }

         for (Entry entry : itemAttributeModifiers.modifiers()) {
            if (entry.slot().test(equipmentSlot)) {
               Holder holder = entry.attribute();
               double i = entry.modifier().amount();
               Holder holder1 = Attributes.ARMOR;
               if (holder.is(holder1)) {
                  d += i;
               } else {
                  Holder holder2 = Attributes.ARMOR_TOUGHNESS;
                  if (holder.is(holder2)) {
                     e += i;
                  } else {
                     Holder holder3 = Attributes.KNOCKBACK_RESISTANCE;
                     if (holder.is(holder3)) {
                        f += i;
                     }
                  }
               }
            }
         }
      } catch (Throwable throwable) {
         return 0;
      }

      double g = d * 1000.0;
      double j = e * 100.0;
      double h = g + j;
      double k = f * 10.0;
      return (int)(h + k);
   }

   private void methodB(Minecraft minecraft, int i) {
      EquipmentSlot equipmentSlot = equipmentSlotArrayB[i];
      int j = intArrayA[i];
      int k = minecraft.player.inventoryMenu.containerId;
      int l = minecraft.player.inventoryMenu.getStateId();
      HashGenerator hashGenerator = minecraft.player.connection.decoratedHashOpsGenenerator();
      Int2ObjectArrayMap int2ObjectArrayMap = new Int2ObjectArrayMap();
      HashedStack hashedStack1 = HashedStack.create(ItemStack.EMPTY, hashGenerator);
      int2ObjectArrayMap.put(j, hashedStack1);
      ClientPacketListener clientPacketListener = minecraft.player.connection;
      short s = (short)j;
      ContainerInput containerInput = ContainerInput.QUICK_MOVE;
      HashedStack hashedStack = HashedStack.EMPTY;
      ServerboundContainerClickPacket serverboundContainerClickPacket = new ServerboundContainerClickPacket(
         k, l, s, (byte)0, containerInput, int2ObjectArrayMap, hashedStack
      );
      clientPacketListener.send(serverboundContainerClickPacket);
      LocalPlayer localPlayer = minecraft.player;
      ItemStack itemStack = ItemStack.EMPTY;
      localPlayer.setItemSlot(equipmentSlot, itemStack);
   }

   private void methodA(Minecraft minecraft, int i, int j) {
      EquipmentSlot equipmentSlot = equipmentSlotArrayB[j];
      int k = intArrayA[j];
      int l = this.methodA(i);
      ItemStack itemStack = minecraft.player.getInventory().getItem(i);
      int m = minecraft.player.inventoryMenu.containerId;
      int n = minecraft.player.inventoryMenu.getStateId();
      HashGenerator hashGenerator = minecraft.player.connection.decoratedHashOpsGenenerator();
      Int2ObjectArrayMap int2ObjectArrayMap = new Int2ObjectArrayMap();
      HashedStack hashedStack1 = HashedStack.create(ItemStack.EMPTY, hashGenerator);
      int2ObjectArrayMap.put(l, hashedStack1);
      HashedStack hashedStack2 = HashedStack.create(itemStack, hashGenerator);
      int2ObjectArrayMap.put(k, hashedStack2);
      ClientPacketListener clientPacketListener = minecraft.player.connection;
      short s = (short)l;
      ContainerInput containerInput = ContainerInput.QUICK_MOVE;
      HashedStack hashedStack = HashedStack.EMPTY;
      ServerboundContainerClickPacket serverboundContainerClickPacket = new ServerboundContainerClickPacket(
         m, n, s, (byte)0, containerInput, int2ObjectArrayMap, hashedStack
      );
      clientPacketListener.send(serverboundContainerClickPacket);
      Inventory inventory = minecraft.player.getInventory();
      ItemStack itemStack1 = ItemStack.EMPTY;
      inventory.setItem(i, itemStack1);
      LocalPlayer localPlayer = minecraft.player;
      ItemStack itemStack2 = itemStack.copy();
      localPlayer.setItemSlot(equipmentSlot, itemStack2);
   }

   private void methodF(Minecraft minecraft) {
      ClientPacketListener clientPacketListener = minecraft.player.connection;
      int i = minecraft.player.inventoryMenu.containerId;
      ServerboundContainerClosePacket serverboundContainerClosePacket = new ServerboundContainerClosePacket(i);
      clientPacketListener.send(serverboundContainerClosePacket);
   }

   private void methodG(Minecraft minecraft) {
      AutoArmour$Stage stage1 = this.stageC;
      AutoArmour$Stage stage = AutoArmour$Stage.IDLE;
      if (stage1 != stage) {
         this.methodF(minecraft);
      }

      MoveFix.methodA(false);
      this.methodA();
   }

   private void methodA() {
      AutoArmour$Stage stage = AutoArmour$Stage.IDLE;
      this.stageC = stage;
      this.longD = 0L;
      this.listE.clear();
   }

   private int methodA(int i) {
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

   private static long methodB() {
      return System.currentTimeMillis();
   }

   static {
      int[] i = new int[]{5, 6, 7, 8};
      intArrayA = i;
      EquipmentSlot[] equipmentSlots = new EquipmentSlot[4];
      EquipmentSlot equipmentSlot = EquipmentSlot.HEAD;
      equipmentSlots[0] = equipmentSlot;
      EquipmentSlot equipmentSlot1 = EquipmentSlot.CHEST;
      equipmentSlots[1] = equipmentSlot1;
      EquipmentSlot equipmentSlot2 = EquipmentSlot.LEGS;
      equipmentSlots[2] = equipmentSlot2;
      EquipmentSlot equipmentSlot3 = EquipmentSlot.FEET;
      equipmentSlots[3] = equipmentSlot3;
      equipmentSlotArrayB = equipmentSlots;
      Random randomx = new Random();
      random = randomx;
   }
}
