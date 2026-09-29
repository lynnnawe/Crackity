package need.clarity.modules.visual;

import need.clarity.config.ClarityConfig;
import need.clarity.config.ThemeColors;
import need.clarity.modules.ModuleBase;

public class Ambience extends ModuleBase {
   private static Ambience instance;

   public Ambience() {
      super("Ambience", "Visual");
      instance = this;
   }

   @Override
   public void onEnable() {
      String[] strings = new String[]{
         "Override Time", "Override Weather", "Override Fog Colour", "Override Fog Distance", "Override Lightmap", "Override Sky Colour"
      };
      int j = strings.length;

      for (int i = 0; i < j; i++) {
         int k = i;
         String string = strings[k];

         try {
            String string1 = this.getName();
            Boolean boolean_ = true;
            ClarityConfig.putSetting(string1, string, boolean_);
         } catch (Throwable throwable) {
         }
      }
   }

   @Override
   public void onDisable() {
   }

   public static boolean isOverrideTimeSetting() {
      Ambience ambience = instance;
      if (ambience != null && ambience.isEnabled()) {
         Boolean boolean_ = false;
         if (ambience.getSetting("Override Time", boolean_)) {
            return true;
         }
      }

      return false;
   }

   public static long getTimeOfDaySetting() {
      Ambience ambience = instance;
      int i;
      if (ambience != null) {
         Integer integer = 6000;
         i = ambience.getSetting("Time of Day", integer);
      } else {
         i = 6000;
      }

      return Math.clamp(i, 0, 23999);
   }

   public static boolean isOverrideWeatherSetting() {
      Ambience ambience = instance;
      if (ambience != null && ambience.isEnabled()) {
         Boolean boolean_ = false;
         if (ambience.getSetting("Override Weather", boolean_)) {
            return true;
         }
      }

      return false;
   }

   public static String getWeatherSetting() {
      Ambience ambience = instance;
      String string;
      if (ambience != null) {
         string = ambience.getSetting("Weather", "Clear");
      } else {
         string = "Clear";
      }

      return string;
   }

   public static boolean isOverrideFogColourSetting() {
      Ambience ambience = instance;
      if (ambience != null && ambience.isEnabled()) {
         Boolean boolean_ = false;
         if (ambience.getSetting("Override Fog Colour", boolean_)) {
            return true;
         }
      }

      return false;
   }

   public static float[] getFogColourSetting() {
      Ambience ambience = instance;
      String string;
      if (ambience != null) {
         string = ambience.getSetting("Fog Colour", "#c0d8ff");
      } else {
         string = "#c0d8ff";
      }

      return methodA(string, 12638463, false);
   }

   public static boolean isOverrideFogDistanceSetting() {
      Ambience ambience = instance;
      if (ambience != null && ambience.isEnabled()) {
         Boolean boolean_ = false;
         if (ambience.getSetting("Override Fog Distance", boolean_)) {
            return true;
         }
      }

      return false;
   }

   public static int getFogDistanceSetting() {
      Ambience ambience = instance;
      int i;
      if (ambience != null) {
         Integer integer = 16;
         i = ambience.getSetting("Fog Distance", integer);
      } else {
         i = 16;
      }

      byte b = (byte)i;
      return Math.max(0, b);
   }

   public static boolean isOverrideLightmapSetting() {
      Ambience ambience = instance;
      if (ambience != null && ambience.isEnabled()) {
         Boolean boolean_ = false;
         if (ambience.getSetting("Override Lightmap", boolean_)) {
            return true;
         }
      }

      return false;
   }

   public static float[] getLightmapColourSetting() {
      Ambience ambience = instance;
      String string;
      if (ambience != null) {
         string = ambience.getSetting("Lightmap Colour", "#ffffff");
      } else {
         string = "#ffffff";
      }

      int i = ThemeColors.parseRgb(string, 16777215);
      float[] f = new float[3];
      float g = (i >> 16 & 0xFF) / 255.0F;
      f[0] = g;
      float h = (i >> 8 & 0xFF) / 255.0F;
      f[1] = h;
      float j = (i & 0xFF) / 255.0F;
      f[2] = j;
      return f;
   }

   public static boolean isOverrideSkyColourSetting() {
      Ambience ambience = instance;
      if (ambience != null && ambience.isEnabled()) {
         Boolean boolean_ = false;
         if (ambience.getSetting("Override Sky Colour", boolean_)) {
            return true;
         }
      }

      return false;
   }

   public static float[] getSkyColourSetting() {
      Ambience ambience = instance;
      String string;
      if (ambience != null) {
         string = ambience.getSetting("Sky Colour", "#78a7ff");
      } else {
         string = "#78a7ff";
      }

      return methodA(string, 7907327, false);
   }

   public static boolean isSkyGradientSetting() {
      Ambience ambience = instance;
      if (ambience != null && ambience.isEnabled()) {
         Boolean boolean_ = false;
         if (ambience.getSetting("Sky Gradient", boolean_)) {
            return true;
         }
      }

      return false;
   }

   public static float[] getSkyGradientColour2Setting() {
      Ambience ambience = instance;
      String string;
      if (ambience != null) {
         string = ambience.getSetting("Sky Gradient Colour 2", "#1a2a6c");
      } else {
         string = "#1a2a6c";
      }

      return methodA(string, 1714796, true);
   }

   private static float[] methodA(String string, int i, boolean bl) {
      int j = ThemeColors.resolveRgb(string, i, bl);
      float[] f = new float[3];
      float g = (j >> 16 & 0xFF) / 255.0F;
      f[0] = g;
      float h = (j >> 8 & 0xFF) / 255.0F;
      f[1] = h;
      float k = (j & 0xFF) / 255.0F;
      f[2] = k;
      return f;
   }
}
