package need.clarity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import need.clarity.event.PacketSendEvent;

public final class PerPacketSendEvents {
   private static final List<Consumer<PacketSendEvent>> listeners;

   private PerPacketSendEvents() {
   }

   public static void register(Consumer<PacketSendEvent> consumer) {
      listeners.add(consumer);
   }

   public static PacketSendEvent fire(PacketSendEvent packetSendEvent) {
      for (Consumer consumer : listeners) {
         try {
            consumer.accept(packetSendEvent);
         } catch (Exception exception) {
         }
      }

      return packetSendEvent;
   }

   static {
      ArrayList arrayList = new ArrayList();
      listeners = arrayList;
   }
}
