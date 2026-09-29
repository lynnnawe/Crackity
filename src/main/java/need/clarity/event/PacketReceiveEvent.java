package need.clarity.event;

import net.minecraft.network.protocol.Packet;

public final class PacketReceiveEvent {
   private final Packet<?> packet;
   private boolean cancelled;

   public PacketReceiveEvent(Packet<?> packet) {
      this.packet = packetx;
   }

   public Packet<?> getPacket() {
      return this.packet;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void cancel() {
      this.cancelled = true;
   }
}
