package need.clarity.modules;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import need.clarity.config.ClarityConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.StartTick;
import net.fabricmc.fabric.api.event.Event;

public final class ModuleRegistry {
   private static final Map<String, ModuleBase> modulesByName;
   private static volatile boolean tickEventRegistered = false;

   private ModuleRegistry() {
   }

   public static void register(ModuleBase moduleBase) {
      Map map = modulesByName;
      String string = moduleBase.getName();
      map.put(string, moduleBase);
   }

   public static ModuleBase getModule(String string) {
      return modulesByName.get(string);
   }

   public static List<ModuleBase> getModules() {
      return List.copyOf(modulesByName.values());
   }

   public static void registerTickEvent() {
      if (!tickEventRegistered) {
         tickEventRegistered = true;
         Event event = ClientTickEvents.START_CLIENT_TICK;
         StartTick startTick = minecraft -> {
            for (ModuleBase moduleBase : getModules()) {
               if (moduleBase.isEnabled()) {
                  try {
                     moduleBase.onTick();
                  } catch (Throwable throwable) {
                  }
               }
            }
         };
         event.register(startTick);
      }
   }

   public static void syncFromConfig() {
      for (ModuleBase moduleBase : getModules()) {
         boolean bl = ClarityConfig.isEnabled(moduleBase.getName());
         moduleBase.setEnabled(bl);
      }
   }

   static {
      LinkedHashMap linkedHashMap = new LinkedHashMap();
      modulesByName = linkedHashMap;
   }
}
