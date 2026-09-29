package need.clarity.modules.movement;

import com.mojang.blaze3d.platform.InputConstants.Key;
import com.mojang.blaze3d.platform.InputConstants.Type;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Predicate;
import java.util.stream.Stream;
import need.clarity.PerMovementEvents;
import need.clarity.PerPacketSendEvents;
import need.clarity.event.PacketSendEvent;
import need.clarity.mixin.CreativeModeInventoryScreenAccessor;
import need.clarity.mixin.KeyMappingAccessor;
import need.clarity.modules.ModuleBase;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import org.lwjgl.glfw.GLFW;

public class InventoryWalk extends ModuleBase {
   private final Queue<Packet<?>> queueA;
   private boolean flagB;
   private boolean flagC;
   private int intD;

   public InventoryWalk() {
      super("Inventory Walk", "Movement");
      ConcurrentLinkedDeque concurrentLinkedDeque = new ConcurrentLinkedDeque();
      this.queueA = concurrentLinkedDeque;
      this.flagB = false;
      this.flagC = false;
      this.intD = 0;
      PerMovementEvents.register(this::methodA);
      PerPacketSendEvents.register(this::methodA);
   }

   @Override
   public void onDisable() {
      this.methodA();
      this.flagC = false;
      this.intD = 0;
   }

   @Override
   public void onEnable() {
      this.queueA.clear();
      this.flagC = false;
      this.intD = 0;
   }

   @Override
   public void onTick() {
      if (this.isEnabled()) {
         if (this.isGrimSetting()) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null) {
               if (minecraft.level != null) {
                  if (!this.queueA.isEmpty()) {
                     if (!this.flagC) {
                        if (!(minecraft.screen instanceof AbstractContainerScreen)) {
                           this.flagC = true;
                           this.intD = 0;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void methodA(MoveFix$EventMovementInput eventMovementInput) {
      if (this.isEnabled()) {
         Minecraft minecraft = Minecraft.getInstance();
         if (minecraft.player != null) {
            if (minecraft.level != null) {
               if (this.isGrimSetting() && this.flagC) {
                  eventMovementInput.forward = false;
                  eventMovementInput.backward = false;
                  eventMovementInput.left = false;
                  eventMovementInput.right = false;
                  eventMovementInput.flagG = false;
               } else {
                  Screen screen = minecraft.screen;
                  if (screen != null) {
                     if (this.methodA(screen)) {
                        long l = minecraft.getWindow().handle();
                        KeyMapping keyMapping = minecraft.options.keyUp;
                        boolean bl = this.methodA(l, keyMapping);
                        eventMovementInput.forward = bl;
                        KeyMapping keyMapping1 = minecraft.options.keyDown;
                        boolean bl1 = this.methodA(l, keyMapping1);
                        eventMovementInput.backward = bl1;
                        KeyMapping keyMapping2 = minecraft.options.keyLeft;
                        boolean bl2 = this.methodA(l, keyMapping2);
                        eventMovementInput.left = bl2;
                        KeyMapping keyMapping3 = minecraft.options.keyRight;
                        boolean bl3 = this.methodA(l, keyMapping3);
                        eventMovementInput.right = bl3;
                        KeyMapping keyMapping4 = minecraft.options.keyJump;
                        boolean bl4 = this.methodA(l, keyMapping4);
                        eventMovementInput.flagE = bl4;
                        KeyMapping keyMapping5 = minecraft.options.keyShift;
                        boolean bl5 = this.methodA(l, keyMapping5);
                        eventMovementInput.flagF = bl5;
                        KeyMapping keyMapping6 = minecraft.options.keySprint;
                        boolean bl6 = this.methodA(l, keyMapping6);
                        eventMovementInput.flagG = bl6;
                     }
                  }
               }
            }
         }
      }
   }

   private void methodA(PacketSendEvent packetSendEvent) {
      if (this.isEnabled()) {
         if (this.isGrimSetting()) {
            if (!this.flagB) {
               Minecraft minecraft = Minecraft.getInstance();
               if (minecraft.player != null) {
                  if (minecraft.level != null) {
                     Packet packet = packetSendEvent.getPacket();
                     if (!(minecraft.screen instanceof AbstractContainerScreen)
                        || !(packet instanceof ServerboundContainerClickPacket)
                           && !(packet instanceof ServerboundContainerClosePacket)
                           && !(packet instanceof ServerboundChatPacket)
                           && !(packet instanceof ServerboundChatCommandPacket)) {
                        if (this.flagC) {
                           if (packet instanceof ServerboundMovePlayerPacket) {
                              int i = this.intD + 1;
                              this.intD = i;
                              if (this.intD >= 1) {
                                 this.methodA();
                                 this.flagC = false;
                                 this.intD = 0;
                              }
                           }
                        }
                     } else {
                        this.queueA.add(packet);
                        packetSendEvent.cancel();
                        this.flagC = true;
                        this.intD = 0;
                     }
                  }
               }
            }
         }
      }
   }

   private void methodA() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player == null) {
         this.queueA.clear();
      } else {
         ClientPacketListener clientPacketListener = minecraft.getConnection();
         if (clientPacketListener == null) {
            this.queueA.clear();
         } else {
            this.flagB = true;

            while (!this.queueA.isEmpty()) {
               Packet packet = this.queueA.poll();
               if (packet != null) {
                  clientPacketListener.send(packet);
               }
            }

            this.flagB = false;
         }
      }
   }

   private boolean methodA(Screen screen) {
      if (screen instanceof InventoryScreen) {
         return true;
      }

      if (screen instanceof CreativeModeInventoryScreen) {
         boolean bl;
         if (!this.methodB()) {
            bl = true;
         } else {
            bl = false;
         }

         return bl;
      } else if (screen instanceof AbstractContainerScreen) {
         boolean bl1;
         if (!this.methodB(screen)) {
            bl1 = true;
         } else {
            bl1 = false;
         }

         return bl1;
      } else {
         return false;
      }
   }

   private boolean methodB() {
      CreativeModeTab creativeModeTab = CreativeModeInventoryScreenAccessor.getSelectedTab();
      CreativeModeTab creativeModeTab1 = CreativeModeTabs.searchTab();
      boolean bl;
      if (creativeModeTab == creativeModeTab1) {
         bl = true;
      } else {
         bl = false;
      }

      return bl;
   }

   private boolean methodB(Screen screen) {
      Stream stream = screen.children().stream();
      Class<EditBox> class_ = EditBox.class;
      Predicate predicate = class_::isInstance;
      return stream.anyMatch(predicate);
   }

   private boolean methodA(long l, KeyMapping keyMapping) {
      Key key = ((KeyMappingAccessor)keyMapping).getBoundKey();
      int i = key.getValue();
      Type type1 = key.getType();
      Type type = Type.MOUSE;
      if (type1 == type) {
         boolean bl1;
         if (GLFW.glfwGetMouseButton(l, i) == 1) {
            bl1 = true;
         } else {
            bl1 = false;
         }

         return bl1;
      } else {
         boolean bl;
         if (GLFW.glfwGetKey(l, i) == 1) {
            bl = true;
         } else {
            bl = false;
         }

         return bl;
      }
   }

   private boolean isGrimSetting() {
      Boolean boolean_ = false;
      return this.getSetting("Grim", boolean_);
   }
}
