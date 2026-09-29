package need.clarity.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import need.clarity.modules.ModuleRegistry;
import net.fabricmc.loader.api.FabricLoader;

public class ClarityConfig {
   private static final Path configPath = FabricLoader.getInstance().getGameDir().resolve("clarity_config.json");
   private static final Gson gson;
   private static volatile JsonObject current;
   private static volatile String currentJson = "{}";
   private static final ConcurrentHashMap<String, Boolean> enabledCache;
   private static final ScheduledExecutorService saveExecutor = Executors.newSingleThreadScheduledExecutor(runnable -> {
      Thread thread = new Thread(runnable, "clarity-config-writer");
      thread.setDaemon(true);
      return thread;
   });
   private static volatile ScheduledFuture<?> pendingSave = null;

   public static void load() {
      Path path = configPath;
      LinkOption[] linkOptions = new LinkOption[0];
      if (Files.exists(path, linkOptions)) {
         try {
            Path path1 = configPath;
            Charset charset = StandardCharsets.UTF_8;

            try (BufferedReader bufferedReader = Files.newBufferedReader(path1, charset)) {
               JsonElement jsonElement = JsonParser.parseReader(bufferedReader);
               if (jsonElement.isJsonObject()) {
                  current = jsonElement.getAsJsonObject();
                  rebuildEnabledCache();
                  Gson gsonx = gson;
                  JsonObject jsonObject = current;
                  currentJson = gsonx.toJson(jsonObject);
               }
            }
         } catch (Exception exception) {
         }
      }
   }

   private static void rebuildEnabledCache() {
      enabledCache.clear();

      for (Entry entry : current.entrySet()) {
         String string = (String)entry.getKey();
         if (!string.startsWith("__")) {
            JsonElement jsonElement = (JsonElement)entry.getValue();
            if (jsonElement.isJsonObject()) {
               JsonElement jsonElement1 = jsonElement.getAsJsonObject().get("enabled");
               boolean bl1;
               if (jsonElement1 != null && jsonElement1.isJsonPrimitive() && jsonElement1.getAsBoolean()) {
                  bl1 = true;
               } else {
                  bl1 = false;
               }

               boolean bl = bl1;
               ConcurrentHashMap concurrentHashMap = enabledCache;
               Boolean boolean1 = bl;
               concurrentHashMap.put(string, boolean1);
            }
         }
      }
   }

   public static JsonObject getCurrent() {
      return current;
   }

   public static String getCurrentJson() {
      return currentJson;
   }

   public static void update(String string) {
      try {
         JsonElement jsonElement = JsonParser.parseString(string);
         if (jsonElement.isJsonObject()) {
            current = jsonElement.getAsJsonObject();
            rebuildEnabledCache();
            Gson gsonx = gson;
            JsonObject jsonObject = current;
            currentJson = gsonx.toJson(jsonObject);
            methodB();

            try {
               ModuleRegistry.syncFromConfig();
            } catch (Throwable throwable) {
            }
         }
      } catch (Exception exception) {
      }
   }

   public static boolean isEnabled(String string) {
      ConcurrentHashMap concurrentHashMap = enabledCache;
      Boolean boolean_ = false;
      return concurrentHashMap.getOrDefault(string, boolean_);
   }

   public static void setModuleEnabled(String string, boolean bl) {
      ConcurrentHashMap concurrentHashMap = enabledCache;
      Boolean boolean1 = bl;
      concurrentHashMap.put(string, boolean1);
      JsonElement jsonElement = current.get(string);
      JsonObject jsonObject5;
      if (jsonElement != null && jsonElement.isJsonObject()) {
         jsonObject5 = jsonElement.getAsJsonObject();
      } else {
         JsonObject jsonObject = new JsonObject();
         jsonObject5 = jsonObject;
         JsonObject jsonObject1 = current;
         JsonObject jsonObject4 = jsonObject5;
         jsonObject1.add(string, jsonObject4);
      }

      JsonObject jsonObject2 = jsonObject5;
      Boolean boolean2 = bl;
      jsonObject2.addProperty("enabled", boolean2);
      Gson gsonx = gson;
      JsonObject jsonObject3 = current;
      currentJson = gsonx.toJson(jsonObject3);
      methodB();
   }

   public static Object getSetting(String string, String string1) {
      try {
         JsonElement jsonElement = current.get(string);
         if (jsonElement != null && jsonElement.isJsonObject()) {
            JsonElement jsonElement1 = jsonElement.getAsJsonObject().get("values");
            if (jsonElement1 != null && jsonElement1.isJsonObject()) {
               JsonElement jsonElement2 = jsonElement1.getAsJsonObject().get(string1);
               if (jsonElement2 != null && !jsonElement2.isJsonNull()) {
                  JsonPrimitive jsonPrimitive = jsonElement2.getAsJsonPrimitive();
                  if (jsonPrimitive.isBoolean()) {
                     return jsonPrimitive.getAsBoolean();
                  } else {
                     return jsonPrimitive.isNumber() ? jsonPrimitive.getAsDouble() : jsonPrimitive.getAsString();
                  }
               } else {
                  return null;
               }
            } else {
               return null;
            }
         } else {
            return null;
         }
      } catch (Exception exception) {
         return null;
      }
   }

   public static String getString(String string, String string1, String string2) {
      try {
         JsonElement jsonElement = current.getAsJsonObject(string).getAsJsonObject("values").get(string1);
         String string3;
         if (jsonElement != null && jsonElement.isJsonPrimitive()) {
            string3 = jsonElement.getAsString();
         } else {
            string3 = string2;
         }

         return string3;
      } catch (Exception exception) {
         return string2;
      }
   }

   public static void putSetting(String string, String string1, Object object) {
      try {
         JsonElement jsonElement = current.get(string);
         JsonObject jsonObject;
         if (jsonElement != null && jsonElement.isJsonObject()) {
            jsonObject = jsonElement.getAsJsonObject();
         } else {
            JsonObject jsonObject4 = new JsonObject();
            jsonObject = jsonObject4;
            JsonObject jsonObject5 = current;
            JsonObject jsonObject8 = jsonObject;
            jsonObject5.add(string, jsonObject8);
         }

         JsonElement jsonElement1 = jsonObject.get("values");
         JsonObject jsonObject9;
         if (jsonElement1 != null && jsonElement1.isJsonObject()) {
            jsonObject9 = jsonElement1.getAsJsonObject();
         } else {
            JsonObject jsonObject2 = new JsonObject();
            jsonObject9 = jsonObject2;
            JsonObject jsonObject3 = jsonObject;
            JsonObject jsonObject7 = jsonObject9;
            jsonObject3.add("values", jsonObject7);
         }

         if (object instanceof Boolean boolean_) {
            jsonObject9.addProperty(string1, boolean_);
         } else if (object instanceof Number number) {
            jsonObject9.addProperty(string1, number);
         } else {
            JsonObject jsonObject1 = jsonObject9;
            String string2 = object.toString();
            jsonObject1.addProperty(string1, string2);
         }

         Gson gsonx = gson;
         JsonObject jsonObject6 = current;
         currentJson = gsonx.toJson(jsonObject6);
         methodB();
      } catch (Exception exception) {
      }
   }

   public static void methodA(String string) {
      try {
         JsonObject jsonObject = current;
         String string1;
         if (string == null) {
            string1 = "";
         } else {
            string1 = string;
         }

         jsonObject.addProperty("__activeProfile", string1);
         Gson gsonx = gson;
         JsonObject jsonObject1 = current;
         currentJson = gsonx.toJson(jsonObject1);
         methodB();
      } catch (Exception exception) {
      }
   }

   public static void methodA(JsonObject jsonObject) {
      if (jsonObject != null) {
         try {
            for (Entry entry : jsonObject.entrySet()) {
               String string = (String)entry.getKey();
               if (string != null && !string.isEmpty() && !string.startsWith("__")) {
                  JsonElement jsonElement = (JsonElement)entry.getValue();
                  if (jsonElement != null && jsonElement.isJsonObject()) {
                     JsonObject jsonObject2 = jsonElement.getAsJsonObject();
                     JsonObject jsonObject3 = new JsonObject();

                     for (Entry entry1 : jsonObject2.entrySet()) {
                        JsonElement jsonElement1 = (JsonElement)entry1.getValue();
                        String string1 = (String)entry1.getKey();
                        Object object;
                        if (jsonElement1 == null) {
                           object = JsonNull.INSTANCE;
                        } else {
                           object = jsonElement1.deepCopy();
                        }

                        jsonObject3.add(string1, (JsonElement)object);
                     }

                     JsonElement jsonElement2 = current.get(string);
                     if (jsonElement2 != null && jsonElement2.isJsonObject()) {
                        for (Entry entry2 : jsonElement2.getAsJsonObject().entrySet()) {
                           String string2 = (String)entry2.getKey();
                           if (!jsonObject3.has(string2)) {
                              JsonElement jsonElement3 = (JsonElement)entry2.getValue();
                              String string3 = (String)entry2.getKey();
                              Object object1;
                              if (jsonElement3 == null) {
                                 object1 = JsonNull.INSTANCE;
                              } else {
                                 object1 = jsonElement3.deepCopy();
                              }

                              jsonObject3.add(string3, (JsonElement)object1);
                           }
                        }
                     }

                     JsonElement jsonElement4 = jsonObject2.get("enabled");
                     if (jsonElement4 != null && jsonElement4.isJsonPrimitive()) {
                        boolean bl;
                        try {
                           bl = jsonElement4.getAsBoolean();
                        } catch (Exception exception) {
                           bl = isEnabled(string);
                        }

                        ConcurrentHashMap concurrentHashMap = enabledCache;
                        Boolean boolean_xx = bl;
                        concurrentHashMap.put(string, boolean_xx);
                        Boolean boolean_x = bl;
                        jsonObject3.addProperty("enabled", boolean_x);
                     } else if (!jsonObject3.has("enabled")) {
                        Boolean boolean_ = isEnabled(string);
                        jsonObject3.addProperty("enabled", boolean_);
                     }

                     current.add(string, jsonObject3);
                  }
               }
            }

            Gson gsonx = gson;
            JsonObject jsonObject1 = current;
            currentJson = gsonx.toJson(jsonObject1);
            methodB();
         } catch (Exception exception1) {
         }
      }
   }

   public static JsonObject getBlockColors(String string) {
      try {
         JsonElement jsonElement = current.get(string);
         if (jsonElement != null && jsonElement.isJsonObject()) {
            JsonElement jsonElement1 = jsonElement.getAsJsonObject().get("blockColors");
            return jsonElement1 != null && jsonElement1.isJsonObject() ? jsonElement1.getAsJsonObject() : new JsonObject();
         } else {
            return new JsonObject();
         }
      } catch (Exception exception) {
         return new JsonObject();
      }
   }

   public static Map<String, String> getKeybinds() {
      HashMap hashMap = new HashMap();

      for (Entry entry : current.entrySet()) {
         String string = (String)entry.getKey();
         if (!string.startsWith("__")) {
            JsonElement jsonElement = (JsonElement)entry.getValue();
            if (jsonElement.isJsonObject()) {
               JsonElement jsonElement1 = jsonElement.getAsJsonObject().get("bind");
               if (jsonElement1 != null && jsonElement1.isJsonPrimitive()) {
                  String string1 = jsonElement1.getAsString();
                  if (string1 != null && !string1.isEmpty() && !string1.equals("null")) {
                     hashMap.put(string, string1);
                  }
               }
            }
         }
      }

      return hashMap;
   }

   private static synchronized void methodB() {
      if (pendingSave != null) {
         pendingSave.cancel(false);
      }

      ScheduledExecutorService scheduledExecutorService = saveExecutor;
      Runnable runnable = ClarityConfig::methodC;
      TimeUnit timeUnit = TimeUnit.MILLISECONDS;
      pendingSave = scheduledExecutorService.schedule(runnable, 300L, timeUnit);
   }

   private static void methodC() {
      try {
         Gson gsonx = gson;
         JsonObject jsonObject = current;
         String string = gsonx.toJson(jsonObject);
         Path path = configPath;
         Charset charset = StandardCharsets.UTF_8;
         OpenOption[] openOptions = new OpenOption[0];
         Files.writeString(path, string, charset, openOptions);
      } catch (Exception exception) {
      }
   }

   public static void flushSync() {
      if (pendingSave != null) {
         pendingSave.cancel(false);
      }

      methodC();
   }

   public static String getCustomCapeUrl(String string) {
      try {
         JsonElement jsonElement = current.get("__customCapes");
         if (jsonElement == null || !jsonElement.isJsonArray()) {
            return null;
         }

         for (JsonElement jsonElement1 : jsonElement.getAsJsonArray()) {
            if (jsonElement1.isJsonObject()) {
               JsonObject jsonObject = jsonElement1.getAsJsonObject();
               JsonElement jsonElement2 = jsonObject.get("id");
               if (jsonElement2 != null) {
                  String string2 = jsonElement2.getAsString();
                  if (string.equals(string2)) {
                     JsonElement jsonElement3 = jsonObject.get("url");
                     String string1;
                     if (jsonElement3 != null) {
                        string1 = jsonElement3.getAsString();
                     } else {
                        string1 = null;
                     }

                     return string1;
                  }
               }
            }
         }
      } catch (Exception exception) {
      }

      return null;
   }

   static {
      GsonBuilder gsonBuilder = new GsonBuilder();
      gson = gsonBuilder.setPrettyPrinting().create();
      JsonObject jsonObject = new JsonObject();
      current = jsonObject;
      ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
      enabledCache = concurrentHashMap;
   }
}
