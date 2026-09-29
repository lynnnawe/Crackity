package need.clarity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import need.clarity.modules.movement.MoveFix$EventMovementInput;

public final class PerMovementEvents {
   private static final List<Consumer<MoveFix$EventMovementInput>> listB;
   public static MoveFix$EventMovementInput currentInput = null;

   private PerMovementEvents() {
   }

   public static void register(Consumer<MoveFix$EventMovementInput> consumer) {
      listB.add(consumer);
   }

   public static void fire(MoveFix$EventMovementInput eventMovementInput) {
      currentInput = eventMovementInput;

      for (Consumer consumer : listB) {
         try {
            consumer.accept(eventMovementInput);
         } catch (Exception exception) {
         }
      }
   }

   static {
      ArrayList arrayList = new ArrayList();
      listB = arrayList;
   }
}
