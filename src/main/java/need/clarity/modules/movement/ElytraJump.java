package need.clarity.modules.movement;

import need.clarity.modules.ModuleBase;
import need.clarity.util.ContainerReflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ElytraJump extends ModuleBase {
   private boolean flagA = false;

   public ElytraJump() {
      super("Elytra Jump", "Movement");
   }

   private int methodA(Item item) {
      Minecraft minecraft = Minecraft.getInstance();
      int i = -1;

      for (int j = 0; j < 36; j++) {
         Inventory inventory = minecraft.player.getInventory();
         int k = j;
         if (inventory.getItem(k).getItem() == item) {
            i = j;
            break;
         }
      }

      if (i < 9 && i != -1) {
         i += 36;
      }

      return i;
   }

   private int methodA() {
      Minecraft minecraft = Minecraft.getInstance();
      Item[] items = new Item[6];
      Item item = Items.NETHERITE_CHESTPLATE;
      items[0] = item;
      Item item2 = Items.DIAMOND_CHESTPLATE;
      items[1] = item2;
      Item item3 = Items.GOLDEN_CHESTPLATE;
      items[2] = item3;
      Item item4 = Items.IRON_CHESTPLATE;
      items[3] = item4;
      Item item5 = Items.LEATHER_CHESTPLATE;
      items[4] = item5;
      Item item6 = Items.CHAINMAIL_CHESTPLATE;
      items[5] = item6;
      int k = items.length;

      for (int i = 0; i < k; i++) {
         int l = i;
         Item item1 = items[l];

         for (int j = 0; j < 36; j++) {
            Inventory inventory = minecraft.player.getInventory();
            int m = j;
            if (inventory.getItem(m).getItem() == item1) {
               if (j < 9) {
                  j += 36;
               }

               return j;
            }
         }
      }

      return -1;
   }

   private void methodB() {
      Minecraft minecraft = Minecraft.getInstance();
      Item item = Items.ELYTRA;
      int k = this.methodA(item);
      if (k != -1) {
         if (minecraft.gameMode != null) {
            int m;
            if (k >= 36) {
               m = k - 36;
            } else {
               m = k;
            }

            int i = m;
            int l;
            if (i < 9) {
               l = i + 36;
            } else {
               l = i;
            }

            int j = l;
            MultiPlayerGameMode multiPlayerGameMode = minecraft.gameMode;
            int n = minecraft.player.inventoryMenu.containerId;
            LocalPlayer localPlayer = minecraft.player;
            ContainerReflection.methodB(multiPlayerGameMode, n, j, localPlayer);
         }
      }
   }

   private void methodC() {
      Minecraft minecraft = Minecraft.getInstance();
      int k = this.methodA();
      if (k != -1) {
         if (minecraft.gameMode != null) {
            int m;
            if (k >= 36) {
               m = k - 36;
            } else {
               m = k;
            }

            int i = m;
            int l;
            if (i < 9) {
               l = i + 36;
            } else {
               l = i;
            }

            int j = l;
            MultiPlayerGameMode multiPlayerGameMode = minecraft.gameMode;
            int n = minecraft.player.inventoryMenu.containerId;
            LocalPlayer localPlayer = minecraft.player;
            ContainerReflection.methodB(multiPlayerGameMode, n, j, localPlayer);
         }
      }
   }

   @Override
   public void onEnable() {
      this.flagA = false;
      Boolean boolean_ = false;
      if (this.getSetting("Auto Swap", boolean_)) {
         this.methodB();
      }

      super.onEnable();
   }

   @Override
   public void onDisable() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         Boolean boolean_ = false;
         if (this.getSetting("Auto Swap", boolean_)) {
            LocalPlayer localPlayer = minecraft.player;
            EquipmentSlot equipmentSlot = EquipmentSlot.CHEST;
            Item item = localPlayer.getItemBySlot(equipmentSlot).getItem();
            Item item1 = Items.ELYTRA;
            if (item == item1) {
               this.methodC();
            }
         }

         super.onDisable();
      }
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         if (minecraft.level != null) {
            if (!minecraft.player.getAbilities().flying && minecraft.player.onGround() && !minecraft.player.isInWater() && !minecraft.player.isInLava()) {
               LocalPlayer localPlayer = minecraft.player;
               EquipmentSlot equipmentSlot = EquipmentSlot.CHEST;
               Item item = localPlayer.getItemBySlot(equipmentSlot).getItem();
               Item item4 = Items.ELYTRA;
               if (item == item4 && !minecraft.options.keyJump.isDown()) {
                  minecraft.player.jumpFromGround();
               }
            }

            if (!minecraft.player.getAbilities().flying && !minecraft.player.onGround() && !minecraft.player.isInWater() && !minecraft.player.isFallFlying()) {
               LocalPlayer localPlayer1 = minecraft.player;
               EquipmentSlot equipmentSlot1 = EquipmentSlot.CHEST;
               Item item1 = localPlayer1.getItemBySlot(equipmentSlot1).getItem();
               Item item5 = Items.ELYTRA;
               if (item1 == item5) {
                  minecraft.player.startFallFlying();
                  ClientPacketListener clientPacketListener = minecraft.player.connection;
                  LocalPlayer localPlayer6 = minecraft.player;
                  Action action = Action.START_FALL_FLYING;
                  ServerboundPlayerCommandPacket serverboundPlayerCommandPacket = new ServerboundPlayerCommandPacket(localPlayer6, action);
                  clientPacketListener.send(serverboundPlayerCommandPacket);
               }
            }

            if (minecraft.player.onGround() || minecraft.player.isInWater() || minecraft.player.isInLava()) {
               this.flagA = false;
            }

            if (minecraft.player.hurtTime > 0) {
               Boolean boolean_x = false;
               if (this.getSetting("Auto Swap", boolean_x)) {
                  LocalPlayer localPlayer2 = minecraft.player;
                  EquipmentSlot equipmentSlot2 = EquipmentSlot.CHEST;
                  Item item2 = localPlayer2.getItemBySlot(equipmentSlot2).getItem();
                  Item item6 = Items.ELYTRA;
                  if (item2 == item6) {
                     this.methodC();
                     return;
                  }
               }
            }

            if (minecraft.player.getInventory() != null) {
               Item item7 = Items.ELYTRA;
               if (this.methodA(item7) == 38 && minecraft.options.keyJump.isDown() && !minecraft.player.isFallFlying()) {
                  minecraft.options.keyJump.setDown(false);
               }
            }

            LocalPlayer localPlayer3 = minecraft.player;
            EquipmentSlot equipmentSlot3 = EquipmentSlot.CHEST;
            Item item3 = localPlayer3.getItemBySlot(equipmentSlot3).getItem();
            Item item8 = Items.ELYTRA;
            if (item3 == item8) {
               LocalPlayer localPlayer4 = minecraft.player;
               double e = minecraft.player.getDeltaMovement().y;
               localPlayer4.setDeltaMovement(0.0, e, 0.0);
               minecraft.options.keyJump.setDown(true);
               if (minecraft.player.isFallFlying()) {
                  float g = (float)(Math.random() * 0.001F);
                  float f = 0.06F + g;
                  LocalPlayer localPlayer5 = minecraft.player;
                  double d = minecraft.player.getDeltaMovement().x;
                  double h = minecraft.player.getDeltaMovement().y;
                  double j = f;
                  double i = h + j;
                  double k = minecraft.player.getDeltaMovement().z;
                  localPlayer5.setDeltaMovement(d, i, k);
               }
            } else {
               Boolean boolean_ = false;
               if (this.getSetting("Auto Swap", boolean_)) {
                  this.methodB();
               }
            }
         }
      }
   }
}
