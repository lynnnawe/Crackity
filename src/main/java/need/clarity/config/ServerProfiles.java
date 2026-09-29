package need.clarity.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import need.clarity.modules.ModuleRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Join;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.Connection;

public final class ServerProfiles {
   private static volatile boolean flagA = false;

   public static void methodA() {
      if (!flagA) {
         flagA = true;

         try {
            Event event = ClientPlayConnectionEvents.JOIN;
            Join join = (clientPacketListener, packetSender, minecraft) -> {
               try {
                  methodA(clientPacketListener);
               } catch (Throwable throwable1) {
               }
            };
            event.register(join);
         } catch (Throwable throwable) {
         }
      }
   }

   private static void methodA(ClientPacketListener clientPacketListener) {
      if (clientPacketListener != null) {
         List list = methodA(methodB(clientPacketListener));
         if (!list.isEmpty()) {
            JsonObject jsonObject = ClarityConfig.getCurrent();
            if (jsonObject != null) {
               JsonObject jsonObject1 = methodA(jsonObject.get("__serverProfiles"));
               if (jsonObject1 != null) {
                  if (jsonObject1.size() != 0) {
                     String string = methodA(jsonObject1, list);
                     if (string != null) {
                        if (!string.isEmpty()) {
                           JsonObject jsonObject2 = methodA(jsonObject.get("__profiles"));
                           if (jsonObject2 != null) {
                              JsonObject jsonObject3 = methodA(jsonObject2.get(string));
                              if (jsonObject3 != null) {
                                 ClarityConfig.methodA(jsonObject3);

                                 try {
                                    ClarityConfig.methodA(string);
                                 } catch (Throwable throwable) {
                                 }

                                 ModuleRegistry.syncFromConfig();
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private static String methodB(ClientPacketListener clientPacketListener) {
      try {
         ServerData serverData = clientPacketListener.getServerData();
         if (serverData != null && serverData.ip != null && !serverData.ip.isBlank()) {
            return serverData.ip;
         }
      } catch (Throwable throwable1) {
      }

      try {
         Connection connection = clientPacketListener.getConnection();
         if (connection != null) {
            SocketAddress socketAddress = connection.getRemoteAddress();
            if (socketAddress != null) {
               return socketAddress.toString();
            }
         }
      } catch (Throwable throwable) {
      }

      return null;
   }

   private static List<String> methodA(String string) {
      ArrayList arrayList = new ArrayList(3);
      String string1 = methodB(string);
      if (string1 == null) {
         return arrayList;
      }

      methodA(arrayList, string1);
      String string2 = methodC(string1);
      if (string2 != null && !string2.isEmpty()) {
         methodA(arrayList, string2);
         if (!methodD(string1)) {
            String string3 = string2 + ":25565";
            methodA(arrayList, string3);
         }
      }

      return arrayList;
   }

   private static void methodA(List<String> list, String string) {
      if (string != null) {
         if (!string.isEmpty()) {
            if (!list.contains(string)) {
               list.add(string);
            }
         }
      }
   }

   private static String methodB(String string) {
      if (string == null) {
         return null;
      }

      String string1 = string.trim();
      if (string1.isEmpty()) {
         return null;
      }

      int i = string1.lastIndexOf(47);
      if (i >= 0) {
         int j = string1.length() - 1;
         if (i < j) {
            String string2 = string1;
            int k = i + 1;
            string1 = string2.substring(k);
         }
      }

      String string3 = string1;
      Locale locale = Locale.ROOT;
      return string3.toLowerCase(locale);
   }

   private static String methodC(String string) {
      if (string == null) {
         return null;
      }

      if (string.startsWith("[")) {
         int i = string.indexOf(93);
         if (i > 0) {
            int k = i + 1;
            return string.substring(0, k);
         }
      }

      int j = string.indexOf(58);
      String string1;
      if (j > 0) {
         string1 = string.substring(0, j);
      } else {
         string1 = string;
      }

      return string1;
   }

   private static boolean methodD(String string) {
      if (string == null) {
         return false;
      }

      if (!string.startsWith("[")) {
         boolean bl;
         if (string.indexOf(58) >= 0) {
            bl = true;
         } else {
            bl = false;
         }

         return bl;
      } else {
         int i = string.indexOf(93);
         if (i >= 0) {
            int j = i + 1;
            int k = string.length();
            if (j < k) {
               int l = i + 1;
               if (string.charAt(l) == ':') {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private static String methodA(JsonObject jsonObject, List<String> list) {
      for (String string : list) {
         for (Entry entry : jsonObject.entrySet()) {
            String string1 = methodB((String)entry.getKey());
            if (string1 != null && string1.equals(string)) {
               JsonElement jsonElement = (JsonElement)entry.getValue();
               if (jsonElement != null && jsonElement.isJsonPrimitive()) {
                  return jsonElement.getAsString();
               }
            }
         }
      }

      return null;
   }

   private static JsonObject methodA(JsonElement jsonElement) {
      JsonObject jsonObject;
      if (jsonElement != null && jsonElement.isJsonObject()) {
         jsonObject = jsonElement.getAsJsonObject();
      } else {
         jsonObject = null;
      }

      return jsonObject;
   }
}
