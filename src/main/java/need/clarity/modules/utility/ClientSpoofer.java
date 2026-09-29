package need.clarity.modules.utility;

import need.clarity.modules.ModuleBase;

public class ClientSpoofer extends ModuleBase {
   private static ClientSpoofer instance;

   public ClientSpoofer() {
      super("Client Spoofer", "Utility");
      instance = this;
   }

   public static ClientSpoofer getInstance() {
      return instance;
   }

   public String getClientSetting() {
      String string = this.getSetting("Client", "Vanilla");
      byte b = -1;
      int i = string.hashCode();
      switch (i) {
         case -1813153191:
            if (string.equals("Sodium")) {
               b = 8;
            }
            break;
         case -767248267:
            if (string.equals("Lunar Client")) {
               b = 2;
            }
            break;
         case 68066119:
            if (string.equals("Forge")) {
               b = 3;
            }
            break;
         case 78395117:
            if (string.equals("Quilt")) {
               b = 9;
            }
            break;
         case 685432963:
            if (string.equals("Feather")) {
               b = 6;
            }
            break;
         case 1189713103:
            if (string.equals("NeoForge")) {
               b = 4;
            }
            break;
         case 1318402433:
            if (string.equals("Badlion")) {
               b = 5;
            }
            break;
         case 1602017974:
            if (string.equals("LabyMod")) {
               b = 7;
            }
            break;
         case 1897755483:
            if (string.equals("Vanilla")) {
               b = 0;
            }
            break;
         case 2096654533:
            if (string.equals("Fabric")) {
               b = 1;
            }
      }

      byte c = b;

      return switch (c) {
         case 0 -> "vanilla";
         case 1 -> "fabric";
         case 2 -> "lunarclient:v2.21.5-2540";
         case 3 -> "forge";
         case 4 -> "neoforge";
         case 5 -> "badlion";
         case 6 -> "feather";
         case 7 -> "labymod";
         case 8 -> "sodium";
         case 9 -> "quilt";
         default -> "vanilla";
      };
   }
}
