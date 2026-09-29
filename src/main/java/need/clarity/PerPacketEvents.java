package need.clarity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import need.clarity.event.PacketReceiveEvent;

public final class PerPacketEvents {
   private static final List<Consumer<PacketReceiveEvent>> listeners;

   private PerPacketEvents() {
   }

   public static void register(Consumer<PacketReceiveEvent> consumer) {
      listeners.add(consumer);
   }

   public static void unregister(Consumer<PacketReceiveEvent> consumer) {
      listeners.remove(consumer);
   }

   public static PacketReceiveEvent fire(PacketReceiveEvent packetReceiveEvent) {
      for (Consumer consumer : listeners) {
         try {
            consumer.accept(packetReceiveEvent);
         } catch (Exception exception) {
         }
      }

      return packetReceiveEvent;
   }

   static {
      ArrayList arrayList = new ArrayList();
      listeners = arrayList;
   }
}
