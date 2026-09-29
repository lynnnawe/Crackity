package need.clarity.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class ThemeColors {
   private static JsonObject getThemeConfig() {
      try {
         JsonElement jsonElement = ClarityConfig.getCurrent().get("__theme");
         if (jsonElement != null && jsonElement.isJsonObject()) {
            return jsonElement.getAsJsonObject();
         }
      } catch (Throwable throwable) {
      }

      return null;
   }

   public static boolean isThemeEnabled() {
      JsonObject jsonObject = getThemeConfig();
      if (jsonObject == null) {
         return true;
      }

      try {
         JsonElement jsonElement = jsonObject.get("enabled");
         return jsonElement != null && jsonElement.isJsonPrimitive() ? jsonElement.getAsBoolean() : true;
      } catch (Throwable throwable) {
         return true;
      }
   }

   private static int getThemeColor(String string, int i) {
      JsonObject jsonObject = getThemeConfig();
      if (jsonObject == null) {
         return i;
      }

      try {
         JsonElement jsonElement = jsonObject.get(string);
         if (jsonElement != null && jsonElement.isJsonPrimitive()) {
            return parseRgb(jsonElement.getAsString(), i);
         }
      } catch (Throwable throwable) {
      }

      return i;
   }

   public static int getAccentColor() {
      return getThemeColor("accent", 8037111);
   }

   public static int getSecondaryAccentColor() {
      return getThemeColor("accent2", 4020641);
   }

   public static int getOpaqueAccentColor() {
      int i = getAccentColor();
      return 0xFF000000 | i;
   }

   public static int getOpaqueSecondaryAccentColor() {
      int i = getSecondaryAccentColor();
      return 0xFF000000 | i;
   }

   public static int resolveRgb(Object object, int i, boolean bl) {
      int j = resolveThemeRgb(object, bl);
      return j >= 0 ? j : parseRgb(object, i);
   }

   public static int resolveArgb(Object object, int i, boolean bl) {
      if (isThemeToken(object)) {
         int k;
         if (object.toString().trim().equalsIgnoreCase("theme2")) {
            k = getOpaqueSecondaryAccentColor();
         } else {
            k = getOpaqueAccentColor();
         }

         return k;
      } else if (isThemeEnabled()) {
         int j;
         if (bl) {
            j = getOpaqueSecondaryAccentColor();
         } else {
            j = getOpaqueAccentColor();
         }

         return j;
      } else {
         return parseArgb(object, i);
      }
   }

   public static boolean isThemeToken(Object object) {
      if (object == null) {
         return false;
      }

      String string = object.toString().trim();
      boolean bl;
      if (!string.equalsIgnoreCase("theme") && !string.equalsIgnoreCase("theme2")) {
         bl = false;
      } else {
         bl = true;
      }

      return bl;
   }

   private static int resolveThemeRgb(Object object, boolean bl) {
      if (isThemeToken(object)) {
         int j;
         if (object.toString().trim().equalsIgnoreCase("theme2")) {
            j = getSecondaryAccentColor();
         } else {
            j = getAccentColor();
         }

         return j;
      } else if (isThemeEnabled()) {
         int i;
         if (bl) {
            i = getSecondaryAccentColor();
         } else {
            i = getAccentColor();
         }

         return i;
      } else {
         return -1;
      }
   }

   public static int parseRgb(Object object, int i) {
      if (object == null) {
         return i;
      }

      String string = object.toString().trim();
      if (string.startsWith("#")) {
         string = string.substring(1);
      }

      try {
         if (string.length() == 6) {
            return (int)Long.parseLong(string, 16) & 16777215;
         }

         if (string.length() == 8) {
            return (int)Long.parseLong(string, 16) & 16777215;
         }
      } catch (NumberFormatException numberFormatException) {
      }

      return i;
   }

   public static int parseArgb(Object object, int i) {
      if (object == null) {
         return i;
      }

      String string = object.toString().trim();
      if (string.startsWith("#")) {
         string = string.substring(1);
      }

      try {
         if (string.length() == 6) {
            long l = Long.parseLong(string, 16);
            return (int)(4278190080L | l);
         }

         if (string.length() == 8) {
            return (int)Long.parseLong(string, 16);
         }
      } catch (NumberFormatException numberFormatException) {
      }

      return i;
   }
}
