package need.clarity;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class WorldRenderEvent {
   private static final List<WorldRenderEvent$Listener> listeners;

   private WorldRenderEvent() {
   }

   public static void register(WorldRenderEvent$Listener listener) {
      if (!listeners.contains(listener)) {
         listeners.add(listener);
      }
   }

   public static void methodA(WorldRenderEvent$Listener listener) {
      listeners.remove(listener);
   }

   public static void fire(PoseStack poseStack, float f) {
      for (WorldRenderEvent$Listener listener : listeners) {
         try {
            listener.onWorldRender(poseStack, f);
         } catch (Exception exception) {
         }
      }
   }

   static {
      CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
      listeners = copyOnWriteArrayList;
   }
}
