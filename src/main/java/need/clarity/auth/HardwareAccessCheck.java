package need.clarity.auth;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import need.clarity.Clarity;

public class HardwareAccessCheck {
   public static void checkAccess() {
      String string = HardwareId.getHardwareId();

      try {
         if (!fetchAllowedHardwareIds().contains(string)) {
            denyAccess(string);
         }
      } catch (Exception exception) {
         denyAccess(string);
      }
   }

   private static Set<String> fetchAllowedHardwareIds() {
      HashSet hashSet = new HashSet();
      URL uRL = new URL("https://gist.githubusercontent.com/clarity-mg/c107da069dc5ef07d5c7443f3b76aeb9/raw/gistfile1.txt");
      HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
      httpURLConnection.setRequestMethod("GET");
      httpURLConnection.setConnectTimeout(7000);
      httpURLConnection.setReadTimeout(7000);
      httpURLConnection.setRequestProperty("Cache-Control", "no-cache");
      httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
      int i = httpURLConnection.getResponseCode();
      if (i != 200) {
         String string2 = "Gist returned HTTP " + i;
         RuntimeException runtimeException = new RuntimeException(string2);
         throw runtimeException;
      }

      InputStream inputStream = httpURLConnection.getInputStream();
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

      try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
         while (true) {
            String string = bufferedReader.readLine();
            if (string == null) {
               return hashSet;
            }

            String string1 = string.trim();
            if (!string1.isEmpty()) {
               hashSet.add(string1);
            }
         }
      }
   }

   private static void denyAccess(String string) {
      String string1 = "\n==============================\n  Clarity, Access Denied\n  HWID: " + string + "\n==============================\n";

      try {
         Clarity.LOGGER.error(string1);
      } catch (Throwable throwable) {
      }

      System.err.println(string1);
      System.out.println(string1);
      Runtime.getRuntime().halt(1);
   }
}
