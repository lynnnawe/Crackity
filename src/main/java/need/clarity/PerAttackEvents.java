package need.clarity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class PerAttackEvents {
   private static final List<Consumer<EventStartAttack>> listeners;

   private PerAttackEvents() {
   }

   public static void register(Consumer<EventStartAttack> consumer) {
      listeners.add(consumer);
   }

   public static void fire(EventStartAttack eventStartAttack) {
      for (Consumer consumer : listeners) {
         try {
            consumer.accept(eventStartAttack);
         } catch (Exception exception) {
         }
      }
   }

   static {
      ArrayList arrayList = new ArrayList();
      listeners = arrayList;
   }
}
