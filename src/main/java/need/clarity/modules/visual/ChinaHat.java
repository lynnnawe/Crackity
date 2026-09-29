package need.clarity.modules.visual;

import need.clarity.config.ThemeColors;
import need.clarity.modules.ModuleBase;

public class ChinaHat extends ModuleBase {
   public static ChinaHat instance;

   public ChinaHat() {
      super("China Hat", "Visual");
      instance = this;
   }

   public boolean isColorModeSetting() {
      Object object = this.getSetting("Color Mode", "Sync");
      return "Custom".equals(object);
   }

   public int[] getColorCountSetting() {
      if (!this.isColorModeSetting()) {
         return null;
      }

      String string = this.getSetting("Color Count", "Quartet");
      byte b = -1;
      int t = string.hashCode();
      switch (t) {
         case -1781598082:
            if (string.equals("Triple")) {
               b = 2;
            }
            break;
         case 69086:
            if (string.equals("Duo")) {
               b = 1;
            }
            break;
         case 2582783:
            if (string.equals("Solo")) {
               b = 0;
            }
      }

      byte c = b;
      int[] i;
      switch (c) {
         case 0:
            i = new int[1];
            int o = methodA(this.getSetting("Color 1", "#FFE96B"), false);
            i[0] = o;
            break;
         case 1:
            i = new int[2];
            int j = methodA(this.getSetting("Color 1", "#FFE96B"), false);
            i[0] = j;
            int k = methodA(this.getSetting("Color 2", "#3B5B30"), true);
            i[1] = k;
            break;
         case 2:
            i = new int[3];
            int l = methodA(this.getSetting("Color 1", "#FFE96B"), false);
            i[0] = l;
            int m = methodA(this.getSetting("Color 2", "#3B5B30"), true);
            i[1] = m;
            int n = methodA(this.getSetting("Color 3", "#9A4676"), false);
            i[2] = n;
            break;
         default:
            i = new int[4];
            int p = methodA(this.getSetting("Color 1", "#FFE96B"), false);
            i[0] = p;
            int q = methodA(this.getSetting("Color 2", "#3B5B30"), true);
            i[1] = q;
            int r = methodA(this.getSetting("Color 3", "#9A4676"), false);
            i[2] = r;
            int s = methodA(this.getSetting("Color 4", "#F9B9EB"), true);
            i[3] = s;
      }

      return i;
   }

   public float getCenterAlphaSetting() {
      Float float_ = 1.0F;
      return this.getSetting("Center Alpha", float_);
   }

   public float getEdgeAlphaSetting() {
      Float float_ = 0.4F;
      return this.getSetting("Edge Alpha", float_);
   }

   public float getRotationSpeedSetting() {
      Float float_ = -0.2F;
      return this.getSetting("Rotation Speed", float_);
   }

   private static int methodA(String string, boolean bl) {
      return ThemeColors.resolveArgb(string, -16714844, bl);
   }
}
