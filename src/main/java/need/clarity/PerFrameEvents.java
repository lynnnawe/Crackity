package need.clarity;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class PerFrameEvents {
   private static final List<Runnable> listeners;

   private PerFrameEvents() {
   }

   public static void register(Runnable runnable) {
      if (!listeners.contains(runnable)) {
         listeners.add(runnable);
      }
   }

   public static void methodA(Runnable runnable) {
      listeners.remove(runnable);
   }

   public static void fire() {
      for (Runnable runnable : listeners) {
         try {
            runnable.run();
         } catch (Exception exception) {
         }
      }
   }

   static {
      CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
      listeners = copyOnWriteArrayList;
   }
}
