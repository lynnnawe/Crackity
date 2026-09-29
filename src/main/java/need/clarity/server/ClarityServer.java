package need.clarity.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import need.clarity.config.ClarityConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;

public class ClarityServer {
   private static HttpServer httpServerA;

   public static void start() {
      try {
         InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 7420);
         httpServerA = HttpServer.create(inetSocketAddress, 0);
         HttpServer httpServer = httpServerA;
         HttpHandler httpHandler = ClarityServer::methodA;
         httpServer.createContext("/", httpHandler);
         HttpServer httpServer1 = httpServerA;
         ExecutorService executorService = Executors.newCachedThreadPool();
         httpServer1.setExecutor(executorService);
         httpServerA.start();
      } catch (IOException iOException) {
      }
   }

   public static void stop() {
      if (httpServerA != null) {
         httpServerA.stop(0);
      }
   }

   // $VF: Handled exception range with multiple entry points by splitting it
   // $VF: Duplicated exception handlers to handle obfuscated exceptions
   private static void methodA(HttpExchange httpExchange) {
      httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
      httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
      httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, *");
      String string11 = httpExchange.getRequestMethod();
      if ("OPTIONS".equalsIgnoreCase(string11)) {
         httpExchange.sendResponseHeaders(204, -1L);
         httpExchange.close();
      } else {
         String string1 = httpExchange.getRequestURI().getPath();
         String string2 = httpExchange.getRequestMethod().toUpperCase();
         if ((string1.equals("/") || string1.equals("/index.html")) && string2.equals("GET")) {
            methodA(httpExchange, "assets/clarity/Clarity.html", "text/html; charset=utf-8");
         } else if ((string1.equals("/favicon.ico") || string1.equals("/icon.png")) && string2.equals("GET")) {
            methodA(httpExchange, "assets/clarity/icon.png", "image/png");
         } else if (string1.startsWith("/capes/") && string2.equals("GET")) {
            int i = "/capes/".length();
            String string3 = string1.substring(i);
            if (!string3.contains("..") && !string3.contains("/") && !string3.contains("\\")) {
               String string12 = "assets/clarity/capes/" + string3;
               methodA(httpExchange, string12, "image/png");
            } else {
               methodA(httpExchange, 400, "text/plain", "Bad request");
            }
         } else if (string1.startsWith("/backgrounds/") && string2.equals("GET")) {
            int j = "/backgrounds/".length();
            String string4 = string1.substring(j);
            if (!string4.contains("..") && !string4.contains("/") && !string4.contains("\\")) {
               String string5;
               if (string4.endsWith(".png")) {
                  string5 = "image/png";
               } else {
                  string5 = "image/jpeg";
               }

               String string = string5;
               String string13 = "assets/clarity/backgrounds/" + string4;
               methodA(httpExchange, string13, string);
            } else {
               methodA(httpExchange, 400, "text/plain", "Bad request");
            }
         } else if (string1.equals("/currentserver") && string2.equals("GET")) {
            String string16 = "";

            try {
               ServerData serverData = Minecraft.getInstance().getCurrentServer();
               if (serverData != null && serverData.ip != null && !serverData.ip.isEmpty()) {
                  string16 = serverData.ip;
               } else {
                  ClientPacketListener clientPacketListener = Minecraft.getInstance().getConnection();
                  if (clientPacketListener != null
                     && clientPacketListener.getConnection() != null
                     && clientPacketListener.getConnection().getRemoteAddress() instanceof InetSocketAddress inetSocketAddress) {
                     String string7 = inetSocketAddress.getHostString();
                     int k = inetSocketAddress.getPort();
                     string16 = string7 + ":" + k;
                  }
               }
            } catch (Throwable throwable3) {
            }

            String string6 = "{\"ip\":\"" + methodA(string16) + "\"}";
            Charset charset = StandardCharsets.UTF_8;
            byte[] b = string6.getBytes(charset);
            httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
            httpExchange.getResponseHeaders().add("Cache-Control", "no-store");
            long m = b.length;
            httpExchange.sendResponseHeaders(200, m);

            try (OutputStream outputStream = httpExchange.getResponseBody()) {
               outputStream.write(b);
            }
         } else if (string1.equals("/servers") && string2.equals("GET")) {
            StringBuilder stringBuilder = new StringBuilder("[");

            label182: {
               ServerList serverList;
               byte h;
               try {
                  Minecraft minecraft = Minecraft.getInstance();
                  serverList = new ServerList(minecraft);
                  serverList.load();
                  h = 0;
               } catch (Throwable throwable4) {
                  break label182;
               }

               while (true) {
                  try {
                     byte c = h;
                     int l = serverList.size();
                     if (c >= l) {
                        break;
                     }

                     byte f = h;
                     ServerData serverData1 = serverList.get(f);
                     if (serverData1 != null) {
                        if (stringBuilder.length() > 1) {
                           stringBuilder.append(',');
                        }

                        StringBuilder stringBuilder1 = stringBuilder.append("{\"name\":\"");
                        String string14 = methodA(serverData1.name);
                        StringBuilder stringBuilder2 = stringBuilder1.append(string14).append("\",\"ip\":\"");
                        String string15 = methodA(serverData1.ip);
                        stringBuilder2.append(string15).append("\"}");
                     }

                     h += 1;
                  } catch (Throwable throwable5) {
                     break;
                  }
               }
            }

            stringBuilder.append(']');
            String string8 = stringBuilder.toString();
            Charset charset1 = StandardCharsets.UTF_8;
            byte[] d = string8.getBytes(charset1);
            httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
            httpExchange.getResponseHeaders().add("Cache-Control", "no-store");
            long n = d.length;
            httpExchange.sendResponseHeaders(200, n);

            try (OutputStream outputStream1 = httpExchange.getResponseBody()) {
               outputStream1.write(d);
            }
         } else if (string1.equals("/config") && string2.equals("GET")) {
            String string9 = ClarityConfig.getCurrentJson();
            Charset charset2 = StandardCharsets.UTF_8;
            byte[] e = string9.getBytes(charset2);
            httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
            httpExchange.getResponseHeaders().add("Cache-Control", "no-store");
            long o = e.length;
            httpExchange.sendResponseHeaders(200, o);

            try (OutputStream outputStream2 = httpExchange.getResponseBody()) {
               outputStream2.write(e);
            }
         } else if (string1.equals("/config") && string2.equals("POST")) {
            byte[] g = httpExchange.getRequestBody().readAllBytes();
            Charset charset3 = StandardCharsets.UTF_8;
            String string10 = new String(g, charset3);
            ClarityConfig.update(string10);
            methodA(httpExchange, 204, "text/plain", "");
         } else {
            methodB(httpExchange);
         }
      }
   }

   private static String methodA(String string) {
      return string == null ? "" : string.replace("\\", "\\\\").replace("\"", "\\\"");
   }

   private static void methodA(HttpExchange httpExchange, String string, String string1) {
      label63: {
         try (InputStream inputStream = ClarityServer.class.getClassLoader().getResourceAsStream(string)) {
            if (inputStream == null) {
               methodB(httpExchange);
               break label63;
            }

            byte[] b = inputStream.readAllBytes();
            httpExchange.getResponseHeaders().add("Content-Type", string1);
            httpExchange.getResponseHeaders().add("Cache-Control", "no-cache");
            long l = b.length;
            httpExchange.sendResponseHeaders(200, l);

            try (OutputStream outputStream = httpExchange.getResponseBody()) {
               outputStream.write(b);
            }
         }

         return;
      }
   }

   private static void methodA(HttpExchange httpExchange, int i, String string, String string1) {
      Charset charset = StandardCharsets.UTF_8;
      byte[] b = string1.getBytes(charset);
      httpExchange.getResponseHeaders().add("Content-Type", string);
      long l;
      if (b.length > 0) {
         l = b.length;
      } else {
         l = -1L;
      }

      httpExchange.sendResponseHeaders(i, l);
      if (b.length > 0) {
         try (OutputStream outputStream = httpExchange.getResponseBody()) {
            outputStream.write(b);
         }
      } else {
         httpExchange.close();
      }
   }

   private static void methodB(HttpExchange httpExchange) {
      methodA(httpExchange, 404, "text/plain", "Not found");
   }
}
