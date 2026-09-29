package need.clarity.modules.visual;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;
import java.util.Base64.Decoder;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import need.clarity.Clarity;
import need.clarity.config.ClarityConfig;
import need.clarity.modules.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Capes extends ModuleBase {
   private static final Logger LOGGER = LoggerFactory.getLogger("Clarity/Capes");
   public static volatile Capes instance;
   private static final Map<String, Identifier> mapC;
   private volatile Identifier identifierD;
   private volatile String stringE = "";

   public Capes() {
      super("Capes", "Visual");
      instance = this;
   }

   @Override
   public void onEnable() {
      this.methodC();
   }

   @Override
   public void onDisable() {
      this.identifierD = null;
      this.stringE = "";
   }

   @Override
   public void onTick() {
      String string = this.getCapeSetting();
      String string1 = this.stringE;
      if (!string.equals(string1)) {
         this.methodC();
      }
   }

   public Identifier getIdentifierD() {
      return this.identifierD;
   }

   private String getCapeSetting() {
      Object object = this.getSetting("Cape", "none");
      String string;
      if (object != null) {
         string = object.toString();
      } else {
         string = "none";
      }

      return string;
   }

   private void methodC() {
      String string = this.getCapeSetting();
      this.stringE = string;
      this.identifierD = null;
      if (!"none".equals(string)) {
         if (!string.isEmpty()) {
            Identifier identifier = mapC.get(string);
            if (identifier != null) {
               this.identifierD = identifier;
            } else {
               Minecraft minecraft = Minecraft.getInstance();
               if (minecraft != null) {
                  if (string.startsWith("custom_")) {
                     this.methodB(minecraft, string);
                  } else {
                     this.methodA(minecraft, string);
                  }
               }
            }
         }
      }
   }

   private void methodA(Minecraft minecraft, String string) {
      String string1 = "capes/" + string + ".png";
      Identifier identifier = Clarity.methodA("capes/" + string);

      try {
         ResourceManager resourceManager = minecraft.getResourceManager();
         Identifier identifier1 = Clarity.methodA(string1);
         InputStream inputStream = resourceManager.open(identifier1);
         NativeImage nativeImage = NativeImage.read(inputStream);
         inputStream.close();
         Supplier supplier = () -> string + "_cape";
         DynamicTexture dynamicTexture = new DynamicTexture(supplier, nativeImage);
         minecraft.getTextureManager().register(identifier, dynamicTexture);
         mapC.put(string, identifier);
         this.identifierD = identifier;
      } catch (IOException iOException) {
         Logger logger = LOGGER;
         String string2 = iOException.getMessage();
         logger.warn("[Capes] Failed to load bundled cape '{}': {}", string, string2);
      }
   }

   private void methodB(Minecraft minecraft, String string) {
      String string2 = ClarityConfig.getCustomCapeUrl(string);
      if (string2 != null && !string2.isEmpty()) {
         try {
            String string3;
            if (string2.contains(",")) {
               int i = string2.indexOf(44) + 1;
               string3 = string2.substring(i);
            } else {
               string3 = string2;
            }

            String string1 = string3;
            Decoder decoder = Base64.getDecoder();
            String string4 = string1.trim();
            byte[] b = decoder.decode(string4);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(b);
            NativeImage nativeImage = NativeImage.read(byteArrayInputStream);
            Identifier identifier = Clarity.methodA("capes/" + string);
            Supplier supplier = () -> string + "_custom_cape";
            DynamicTexture dynamicTexture = new DynamicTexture(supplier, nativeImage);
            minecraft.getTextureManager().register(identifier, dynamicTexture);
            mapC.put(string, identifier);
            this.identifierD = identifier;
         } catch (Exception exception) {
            Logger logger = LOGGER;
            String string5 = exception.getMessage();
            logger.warn("[Capes] Failed to decode custom cape '{}': {}", string, string5);
         }
      } else {
         LOGGER.warn("[Capes] No data found for custom cape '{}'", string);
      }
   }

   static {
      ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
      mapC = concurrentHashMap;
   }
}
