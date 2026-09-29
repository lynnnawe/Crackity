package need.clarity.input;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;
import need.clarity.config.ClarityConfig;
import need.clarity.modules.ModuleBase;
import need.clarity.modules.ModuleRegistry;

public class KeybindHandler {
   private static final Set<Integer> setA;

   public static void onKey(int i, boolean bl) {
      for (Entry entry : ClarityConfig.getKeybinds().entrySet()) {
         String string = (String)entry.getKey();
         String string1 = (String)entry.getValue();
         if (parseMouseButton(string1) < 0) {
            int j = parseKeyCode(string1);
            if (j != -1 && j == i) {
               String string2 = getBindMode(string);
               handleBinding(string, string2, i, bl);
            }
         }
      }
   }

   public static void onMouseButton(int i, boolean bl) {
      for (Entry entry : ClarityConfig.getKeybinds().entrySet()) {
         String string = (String)entry.getKey();
         int j = parseMouseButton((String)entry.getValue());
         if (j >= 0 && j == i) {
            String string1 = getBindMode(string);
            int k = i + 10000;
            handleBinding(string, string1, k, bl);
         }
      }
   }

   private static void handleBinding(String string, String string1, int i, boolean bl) {
      if ("hold".equals(string1)) {
         if (bl) {
            Set set = setA;
            Integer integer = i;
            if (!set.contains(integer)) {
               Set set1 = setA;
               Integer integer1 = i;
               set1.add(integer1);
               setModuleEnabled(string, true);
               return;
            }
         }

         if (!bl) {
            Set set2 = setA;
            Integer integer2 = i;
            if (set2.contains(integer2)) {
               Set set3 = setA;
               Integer integer3 = i;
               set3.remove(integer3);
               setModuleEnabled(string, false);
            }
         }
      } else {
         if (bl) {
            Set set4 = setA;
            Integer integer4 = i;
            if (!set4.contains(integer4)) {
               Set set5 = setA;
               Integer integer5 = i;
               set5.add(integer5);
               boolean bl1;
               if (!ClarityConfig.isEnabled(string)) {
                  bl1 = true;
               } else {
                  bl1 = false;
               }

               setModuleEnabled(string, bl1);
               return;
            }
         }

         if (!bl) {
            Set set6 = setA;
            Integer integer6 = i;
            set6.remove(integer6);
         }
      }
   }

   private static void setModuleEnabled(String string, boolean bl) {
      ModuleBase moduleBase = ModuleRegistry.getModule(string);
      if (moduleBase != null) {
         moduleBase.setEnabled(bl);
      } else {
         ClarityConfig.setModuleEnabled(string, bl);
      }
   }

   private static String getBindMode(String string) {
      try {
         JsonObject jsonObject = ClarityConfig.getCurrent().getAsJsonObject(string);
         if (jsonObject == null) {
            return "toggle";
         }

         JsonElement jsonElement = jsonObject.get("bindMode");
         if (jsonElement != null && jsonElement.isJsonPrimitive()) {
            return jsonElement.getAsString();
         }
      } catch (Exception exception) {
      }

      return "toggle";
   }

   static int parseMouseButton(String string) {
      byte b = -1;
      int i = string.hashCode();
      switch (i) {
         case -1688861649:
            if (string.equals("Left Click")) {
               b = 0;
            }
            break;
         case -1510354115:
            if (string.equals("Middle Click")) {
               b = 2;
            }
            break;
         case -1387248807:
            if (string.equals("Mouse 4")) {
               b = 3;
            }
            break;
         case -1387248806:
            if (string.equals("Mouse 5")) {
               b = 4;
            }
            break;
         case -1387248805:
            if (string.equals("Mouse 6")) {
               b = 5;
            }
            break;
         case -1387248804:
            if (string.equals("Mouse 7")) {
               b = 6;
            }
            break;
         case -1387248803:
            if (string.equals("Mouse 8")) {
               b = 7;
            }
            break;
         case -248230332:
            if (string.equals("Right Click")) {
               b = 1;
            }
      }

      byte c = b;

      return switch (c) {
         case 0 -> 0;
         case 1 -> 1;
         case 2 -> 2;
         case 3 -> 3;
         case 4 -> 4;
         case 5 -> 5;
         case 6 -> 6;
         case 7 -> 7;
         default -> -1;
      };
   }

   public static int parseKeyCode(String string) {
      if (string != null && !string.isEmpty()) {
         if (string.length() == 1) {
            char c = Character.toUpperCase(string.charAt(0));
            if (c >= 'A' && c <= 'Z') {
               return c;
            }

            if (c >= '0' && c <= '9') {
               return c;
            }

            char e = c;

            return switch (e) {
               case ' ' -> 32;
               case '\'' -> 39;
               case ',' -> 44;
               case '-' -> 45;
               case '.' -> 46;
               case '/' -> 47;
               case ';' -> 59;
               case '=' -> 61;
               case '[' -> 91;
               case '\\' -> 92;
               case ']' -> 93;
               case '`' -> 96;
               default -> -1;
            };
         } else {
            byte b = -1;
            int i = string.hashCode();
            switch (i) {
               case -2099925287:
                  if (string.equals("Insert")) {
                     b = 5;
                  }
                  break;
               case -1911885398:
                  if (string.equals("PageUp")) {
                     b = 8;
                  }
                  break;
               case -1678770883:
                  if (string.equals("Control")) {
                     b = 15;
                  }
                  break;
               case -937491361:
                  if (string.equals("Backspace")) {
                     b = 3;
                  }
                  break;
               case -336508495:
                  if (string.equals("NumLock")) {
                     b = 18;
                  }
                  break;
               case 2219:
                  if (string.equals("F1")) {
                     b = 23;
                  }
                  break;
               case 2220:
                  if (string.equals("F2")) {
                     b = 24;
                  }
                  break;
               case 2221:
                  if (string.equals("F3")) {
                     b = 25;
                  }
                  break;
               case 2222:
                  if (string.equals("F4")) {
                     b = 26;
                  }
                  break;
               case 2223:
                  if (string.equals("F5")) {
                     b = 27;
                  }
                  break;
               case 2224:
                  if (string.equals("F6")) {
                     b = 28;
                  }
                  break;
               case 2225:
                  if (string.equals("F7")) {
                     b = 29;
                  }
                  break;
               case 2226:
                  if (string.equals("F8")) {
                     b = 30;
                  }
                  break;
               case 2227:
                  if (string.equals("F9")) {
                     b = 31;
                  }
                  break;
               case 65929:
                  if (string.equals("Alt")) {
                     b = 16;
                  }
                  break;
               case 68837:
                  if (string.equals("F10")) {
                     b = 32;
                  }
                  break;
               case 68838:
                  if (string.equals("F11")) {
                     b = 33;
                  }
                  break;
               case 68839:
                  if (string.equals("F12")) {
                     b = 34;
                  }
                  break;
               case 69819:
                  if (string.equals("End")) {
                     b = 7;
                  }
                  break;
               case 83829:
                  if (string.equals("Tab")) {
                     b = 2;
                  }
                  break;
               case 2255103:
                  if (string.equals("Home")) {
                     b = 6;
                  }
                  break;
               case 12763084:
                  if (string.equals("CapsLock")) {
                     b = 17;
                  }
                  break;
               case 67114680:
                  if (string.equals("Enter")) {
                     b = 1;
                  }
                  break;
               case 76887510:
                  if (string.equals("Pause")) {
                     b = 21;
                  }
                  break;
               case 79854690:
                  if (string.equals("Shift")) {
                     b = 14;
                  }
                  break;
               case 80085222:
                  if (string.equals("Space")) {
                     b = 22;
                  }
                  break;
               case 251549619:
                  if (string.equals("ArrowRight")) {
                     b = 13;
                  }
                  break;
               case 923631601:
                  if (string.equals("PageDown")) {
                     b = 9;
                  }
                  break;
               case 930625636:
                  if (string.equals("ArrowUp")) {
                     b = 10;
                  }
                  break;
               case 977535019:
                  if (string.equals("ArrowDown")) {
                     b = 11;
                  }
                  break;
               case 977763216:
                  if (string.equals("ArrowLeft")) {
                     b = 12;
                  }
                  break;
               case 1318883673:
                  if (string.equals("PrintScreen")) {
                     b = 20;
                  }
                  break;
               case 2043376075:
                  if (string.equals("Delete")) {
                     b = 4;
                  }
                  break;
               case 2059521464:
                  if (string.equals("ScrollLock")) {
                     b = 19;
                  }
                  break;
               case 2084662433:
                  if (string.equals("Escape")) {
                     b = 0;
                  }
            }

            byte d = b;

            return switch (d) {
               case 0 -> 256;
               case 1 -> 257;
               case 2 -> 258;
               case 3 -> 259;
               case 4 -> 261;
               case 5 -> 260;
               case 6 -> 268;
               case 7 -> 269;
               case 8 -> 266;
               case 9 -> 267;
               case 10 -> 265;
               case 11 -> 264;
               case 12 -> 263;
               case 13 -> 262;
               case 14 -> 340;
               case 15 -> 341;
               case 16 -> 342;
               case 17 -> 280;
               case 18 -> 282;
               case 19 -> 281;
               case 20 -> 283;
               case 21 -> 284;
               case 22 -> 32;
               case 23 -> 290;
               case 24 -> 291;
               case 25 -> 292;
               case 26 -> 293;
               case 27 -> 294;
               case 28 -> 295;
               case 29 -> 296;
               case 30 -> 297;
               case 31 -> 298;
               case 32 -> 299;
               case 33 -> 300;
               case 34 -> 301;
               default -> -1;
            };
         }
      } else {
         return -1;
      }
   }

   static {
      HashSet hashSet = new HashSet();
      setA = hashSet;
   }
}
