package need.clarity.auth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class HardwareId {
   public static String getHardwareId() {
      try {
         String string = System.getProperty("os.name", "").toLowerCase();
         String string1;
         if (string.contains("win")) {
            string1 = readWindowsId();
         } else if (!string.contains("mac") && !string.contains("darwin")) {
            string1 = readLinuxId();
         } else {
            string1 = readMacId();
         }

         if (string1 == null || string1.trim().isEmpty()) {
            string1 = buildFallbackId();
         }

         return hashIdentifier(string1.trim());
      } catch (Exception exception) {
         return "UNKNOWN";
      }
   }

   private static String readWindowsId() {
      String[] strings = new String[]{"wmic", "csproduct", "get", "UUID"};
      String string = runCommand(strings);
      if (string != null) {
         String[] strings1 = string.split("\n");
         int j = strings1.length;

         for (int i = 0; i < j; i++) {
            int k = i;
            String string1 = strings1[k].trim();
            if (!string1.isEmpty() && !string1.equalsIgnoreCase("UUID")) {
               return "WIN-" + string1;
            }
         }
      }

      return buildFallbackId();
   }

   private static String readMacId() {
      String[] strings = new String[]{"ioreg", "-d2", "-c", "IOPlatformExpertDevice"};
      String string = runCommand(strings);
      if (string != null) {
         String[] strings1 = string.split("\n");
         int j = strings1.length;

         for (int i = 0; i < j; i++) {
            int m = i;
            String string1 = strings1[m];
            if (string1.contains("IOPlatformUUID")) {
               int n = string1.indexOf(61) + 1;
               int k = string1.indexOf(34, n) + 1;
               int l = string1.lastIndexOf(34);
               if (k > 0 && l > k) {
                  return "MAC-" + string1.substring(k, l);
               }
            }
         }
      }

      return buildFallbackId();
   }

   private static String readLinuxId() {
      String[] strings = new String[]{"/etc/machine-id", "/var/lib/dbus/machine-id"};
      int j = strings.length;

      for (int i = 0; i < j; i++) {
         int k = i;
         String string = strings[k];
         File file = new File(string);
         if (file.exists() && file.canRead()) {
            try {
               FileReader fileReader = new FileReader(file);

               try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                  String string1 = bufferedReader.readLine();
                  if (string1 != null && !string1.trim().isEmpty()) {
                     return "LNX-" + string1.trim();
                  }
               }
            } catch (Exception exception) {
            }
         }
      }

      return buildFallbackId();
   }

   private static String buildFallbackId() {
      StringBuilder stringBuilder = new StringBuilder();

      try {
         Iterator iterator = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();

         while (iterator.hasNext()) {
            byte[] b = ((NetworkInterface)iterator.next()).getHardwareAddress();
            if (b != null) {
               int j = b.length;

               for (int i = 0; i < j; i++) {
                  int k = i;
                  byte c = b[k];
                  Object[] objects = new Object[1];
                  Byte byte1 = c;
                  objects[0] = byte1;
                  String string = String.format("%02X", objects);
                  stringBuilder.append(string);
               }
            }
         }
      } catch (Exception exception) {
      }

      return "MAC-" + stringBuilder;
   }

   private static String runCommand(String... strings) {
      Process process = null;

      String string;
      label61: {
         String string2;
         try {
            ProcessBuilder processBuilder = new ProcessBuilder(strings);
            process = processBuilder.redirectErrorStream(true).start();
            Process process1 = process;
            StringBuilder stringBuilder = new StringBuilder();
            Runnable runnable = () -> {
               try {
                  InputStream inputStream = process1.getInputStream();
                  InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                  try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                     while (true) {
                        String string3 = bufferedReader.readLine();
                        if (string3 == null) {
                           return;
                        }

                        stringBuilder.append(string3).append("\n");
                     }
                  }
               } catch (Exception exception1) {
               }
            };
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            thread.start();
            Process process2 = process;
            TimeUnit timeUnit = TimeUnit.SECONDS;
            if (!process2.waitFor(4L, timeUnit)) {
               process.destroyForcibly();
               thread.join(500L);
               String string1;
               if (stringBuilder.length() > 0) {
                  string1 = stringBuilder.toString();
               } else {
                  string1 = null;
               }

               string = string1;
               break label61;
            }

            thread.join(500L);
            string2 = stringBuilder.toString();
         } catch (Exception exception) {
            if (process != null && process.isAlive()) {
               process.destroyForcibly();
            }

            return null;
         } catch (Throwable throwable) {
            if (process != null && process.isAlive()) {
               process.destroyForcibly();
            }

            throw throwable;
         }

         if (process != null && process.isAlive()) {
            process.destroyForcibly();
         }

         return string2;
      }

      if (process != null && process.isAlive()) {
         process.destroyForcibly();
      }

      return string;
   }

   private static String hashIdentifier(String string) {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      byte[] d = string.getBytes("UTF-8");
      byte[] b = messageDigest.digest(d);
      StringBuilder stringBuilder = new StringBuilder();
      int j = b.length;

      for (int i = 0; i < j; i++) {
         int k = i;
         byte c = b[k];
         Object[] objects = new Object[1];
         Byte byte1 = c;
         objects[0] = byte1;
         String string1 = String.format("%02x", objects);
         stringBuilder.append(string1);
      }

      return stringBuilder.toString();
   }
}
