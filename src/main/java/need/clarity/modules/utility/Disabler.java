package need.clarity.modules.utility;

import need.clarity.PerPacketEvents;
import need.clarity.event.PacketReceiveEvent;
import need.clarity.modules.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
import net.minecraft.world.InteractionHand;

public class Disabler extends ModuleBase {
   private int intA = 0;

   public Disabler() {
      super("Disabler", "utility");
      PerPacketEvents.register(this::methodA);
   }

   @Override
   public void onEnable() {
      this.intA = 0;
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         if (minecraft.level != null) {
            if (minecraft.player.connection != null) {
               String string = this.getSetting("Mode", "Spartan Trident");
               if ("Spartan Trident".equals(string)) {
                  this.methodA(minecraft);
               }
            }
         }
      }
   }

   private void methodA(Minecraft minecraft) {
      MultiPlayerGameMode multiPlayerGameMode = minecraft.gameMode;
      LocalPlayer localPlayer = minecraft.player;
      InteractionHand interactionHand = InteractionHand.MAIN_HAND;
      multiPlayerGameMode.useItem(localPlayer, interactionHand);
      int i = this.intA + 1;
      this.intA = i;
   }

   private void methodA(PacketReceiveEvent packetReceiveEvent) {
      if (this.isEnabled()) {
         Object object = this.getSetting("Mode", "Spartan Trident");
         if ("Grim NoFlag".equals(object)) {
            if (packetReceiveEvent.getPacket() instanceof ClientboundPlayerPositionPacket clientboundPlayerPositionPacket) {
               packetReceiveEvent.cancel();
               int i = clientboundPlayerPositionPacket.id();
               Minecraft minecraft = Minecraft.getInstance();
               Runnable runnable = () -> {
                  if (minecraft.player != null) {
                     if (minecraft.player.connection != null) {
                        ClientPacketListener clientPacketListener = minecraft.player.connection;
                        ServerboundAcceptTeleportationPacket serverboundAcceptTeleportationPacket = new ServerboundAcceptTeleportationPacket(i);
                        clientPacketListener.send(serverboundAcceptTeleportationPacket);
                     }
                  }
               };
               minecraft.execute(runnable);
            }
         }
      }
   }
}
