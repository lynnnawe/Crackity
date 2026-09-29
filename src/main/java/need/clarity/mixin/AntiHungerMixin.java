package need.clarity.mixin;

import need.clarity.modules.movement.AntiHunger;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.Pos;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.PosRot;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.Rot;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.StatusOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientCommonPacketListenerImpl.class)
public class AntiHungerMixin {
   @Redirect(
      method = "send(Lnet/minecraft/network/protocol/Packet;)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;send(Lnet/minecraft/network/protocol/Packet;)V")
   )
   private void cl$antiHungerSend(Connection connection, Packet<?> object) {
      if (object instanceof ServerboundMovePlayerPacket serverboundMovePlayerPacket) {
         AntiHunger antiHunger = AntiHunger.instance;
         if (antiHunger != null && antiHunger.isEnabled() && antiHunger.methodA()) {
            object = modifyOnGround(serverboundMovePlayerPacket, false);
         }
      }

      connection.send((Packet)object);
   }

   private static ServerboundMovePlayerPacket modifyOnGround(ServerboundMovePlayerPacket serverboundMovePlayerPacket, boolean bl) {
      if (serverboundMovePlayerPacket instanceof PosRot posRot) {
         return new PosRot(posRot.getX(0.0), posRot.getY(0.0), posRot.getZ(0.0), posRot.getYRot(0.0F), posRot.getXRot(0.0F), bl, posRot.horizontalCollision());
      } else if (serverboundMovePlayerPacket instanceof Pos pos) {
         return new Pos(pos.getX(0.0), pos.getY(0.0), pos.getZ(0.0), bl, pos.horizontalCollision());
      } else {
         return (ServerboundMovePlayerPacket)(serverboundMovePlayerPacket instanceof Rot rot
            ? new Rot(rot.getYRot(0.0F), rot.getXRot(0.0F), bl, rot.horizontalCollision())
            : new StatusOnly(bl, serverboundMovePlayerPacket.horizontalCollision()));
      }
   }
}
