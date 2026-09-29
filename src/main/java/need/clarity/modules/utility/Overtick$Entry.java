package need.clarity.modules.utility;

import net.minecraft.network.protocol.Packet;

final class Overtick$Entry {
   final Packet<?> packetA;
   final long longB;

   Overtick$Entry(Packet<?> packet) {
      this.packetA = packet;
      long l = System.currentTimeMillis();
      this.longB = l;
   }
}
