package need.clarity.modules.visual;

import need.clarity.modules.ModuleBase;
import need.clarity.render.HandShaderRenderer;

public class HandShaders extends ModuleBase {
   public static final String[] stringArrayA;
   public static HandShaders instance;

   public HandShaders() {
      super("Hand Shaders", "Visual");
      instance = this;
   }

   public void methodA() {
      HandShaderRenderer.renderHandEffect();
   }

   public static String getShaderSetting() {
      String string;
      if (instance != null) {
         string = instance.getSetting("Shader", "Chroma");
      } else {
         string = "None";
      }

      return string;
   }

   private static int methodA(String string, int i) {
      if (string != null && !string.isEmpty()) {
         String string2;
         if (string.startsWith("#")) {
            string2 = string.substring(1);
         } else {
            string2 = string;
         }

         String string1 = string2;

         try {
            if (string1.length() == 6) {
               long l = Long.parseLong(string1, 16);
               return (int)(4278190080L | l);
            }

            if (string1.length() == 8) {
               return (int)Long.parseLong(string1, 16);
            }
         } catch (NumberFormatException numberFormatException) {
         }

         return i;
      } else {
         return i;
      }
   }

   private static int methodB(String string, int i) {
      String string1 = instance.getSetting(string, "");
      return !string1.isEmpty() ? methodA(string1, i) : i;
   }

   public static float getChromaSpeedSetting() {
      HandShaders handShaders = instance;
      Float float_ = 0.3F;
      return handShaders.getSetting("Chroma Speed", float_);
   }

   public static float getChromaSaturationSetting() {
      HandShaders handShaders = instance;
      Float float_ = 1.0F;
      return handShaders.getSetting("Chroma Saturation", float_);
   }

   public static float getChromaBrightnessSetting() {
      HandShaders handShaders = instance;
      Float float_ = 1.0F;
      return handShaders.getSetting("Chroma Brightness", float_);
   }

   public static boolean isHideHandSetting() {
      HandShaders handShaders = instance;
      Boolean boolean_ = true;
      return handShaders.getSetting("Hide Hand", boolean_);
   }

   public static float getInvertIntensitySetting() {
      HandShaders handShaders = instance;
      Float float_ = 1.0F;
      return handShaders.getSetting("Invert Intensity", float_);
   }

   public static boolean isInvertHueSetting() {
      HandShaders handShaders = instance;
      Boolean boolean_ = false;
      return handShaders.getSetting("Invert Hue", boolean_);
   }

   public static boolean isInvertSaturationSetting() {
      HandShaders handShaders = instance;
      Boolean boolean_ = false;
      return handShaders.getSetting("Invert Saturation", boolean_);
   }

   public static boolean isInvertBrightnessSetting() {
      HandShaders handShaders = instance;
      Boolean boolean_ = false;
      return handShaders.getSetting("Invert Brightness", boolean_);
   }

   public static float getEdgeGlowSetting() {
      HandShaders handShaders = instance;
      Float float_ = 0.5F;
      return handShaders.getSetting("Edge Glow", float_);
   }

   public static int methodL() {
      return methodB("Glow Color", -16776961);
   }

   public static boolean isApplyToArmSetting() {
      HandShaders handShaders = instance;
      Boolean boolean_ = true;
      return handShaders.getSetting("Apply To Arm", boolean_);
   }

   public static boolean isApplyToItemsSetting() {
      HandShaders handShaders = instance;
      Boolean boolean_ = true;
      return handShaders.getSetting("Apply To Items", boolean_);
   }

   public static int methodO() {
      return methodB("Glow Color", -16720641);
   }

   public static double getOpacitySetting() {
      HandShaders handShaders = instance;
      Float float_ = 0.6F;
      return handShaders.getSetting("Opacity", float_).floatValue();
   }

   public static boolean isTexturedSetting() {
      HandShaders handShaders = instance;
      Boolean boolean_ = true;
      return handShaders.getSetting("Textured", boolean_);
   }

   public static boolean isFullbrightSetting() {
      HandShaders handShaders = instance;
      Boolean boolean_ = false;
      return handShaders.getSetting("Fullbright", boolean_);
   }

   public static boolean isOutlineSetting() {
      HandShaders handShaders = instance;
      Boolean boolean_ = true;
      return handShaders.getSetting("Outline", boolean_);
   }

   public static int methodT() {
      return methodB("Glow Outline Color", -16720641);
   }

   public static double getOutlineWidthSetting() {
      HandShaders handShaders = instance;
      Float float_ = 1.0F;
      return handShaders.getSetting("Outline Width", float_).floatValue();
   }

   public static boolean isBloomSetting() {
      HandShaders handShaders = instance;
      Boolean boolean_ = true;
      return handShaders.getSetting("Bloom", boolean_);
   }

   public static double getGlowSpreadSetting() {
      HandShaders handShaders = instance;
      Float float_ = 1.0F;
      return handShaders.getSetting("Glow Spread", float_).floatValue();
   }

   public static boolean isAntialiasingSetting() {
      HandShaders handShaders = instance;
      Boolean boolean_ = true;
      return handShaders.getSetting("Antialiasing", boolean_);
   }

   public static int methodY() {
      return methodB("Outline Color", -1);
   }

   public static float getOutlineThicknessSetting() {
      HandShaders handShaders = instance;
      Float float_ = 1.5F;
      return handShaders.getSetting("Outline Thickness", float_);
   }

   public static boolean isTintSetting() {
      HandShaders handShaders = instance;
      Boolean boolean_ = false;
      return handShaders.getSetting("Tint", boolean_);
   }

   public static int methodUpperB() {
      return methodB("Tint Color", -65536);
   }

   public static float getTintAmountSetting() {
      HandShaders handShaders = instance;
      Float float_ = 0.4F;
      return handShaders.getSetting("Tint Amount", float_);
   }

   static {
      String[] strings = new String[]{"None", "Chroma", "Invert", "Outline"};
      stringArrayA = strings;
   }
}
