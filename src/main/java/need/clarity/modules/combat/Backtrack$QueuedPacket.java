package need.clarity.modules.combat;

import net.minecraft.network.protocol.Packet;

final class Backtrack$QueuedPacket {
   final Packet<?> packetA;
   final long longB;

   Backtrack$QueuedPacket(Packet<?> packet, long l) {
      this.packetA = packet;
      this.longB = l;
   }
}
