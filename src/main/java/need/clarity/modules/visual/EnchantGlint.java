package need.clarity.modules.visual;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.InputStream;
import java.util.function.Supplier;
import need.clarity.PerFrameEvents;
import need.clarity.config.ThemeColors;
import need.clarity.modules.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;

public class EnchantGlint extends ModuleBase {
   private static final Identifier identifierB = Identifier.withDefaultNamespace("textures/misc/enchanted_glint_item.png");
   private static final Identifier identifierC = Identifier.withDefaultNamespace("textures/misc/enchanted_glint_armor.png");
   public static volatile EnchantGlint instance;
   private int intD = Integer.MIN_VALUE;
   private int intE = Integer.MIN_VALUE;

   public EnchantGlint() {
      super("Enchant Glint", "Visual");
      instance = this;

      try {
         PerFrameEvents.register(this::methodC);
      } catch (Throwable throwable) {
      }
   }

   private void methodC() {
      if (!this.isEnabled()) {
         if (this.intD != Integer.MIN_VALUE) {
            this.methodF();
         }
      }
   }

   @Override
   public void onEnable() {
      this.intD = Integer.MIN_VALUE;
      this.intE = Integer.MIN_VALUE;
      this.methodE();
   }

   @Override
   public void onDisable() {
      this.methodF();
   }

   @Override
   public void onTick() {
      if (this.isEnabled()) {
         int i = this.getGlintColorSetting();
         int j = this.intD;
         if (i != j) {
            this.methodE();
         }
      }
   }

   private int getGlintColorSetting() {
      String string = this.getSetting("Glint Color", "#8040FF");
      return ThemeColors.isThemeToken(string) ? ThemeColors.resolveRgb(string, 8405247, false) : ThemeColors.parseRgb(string, 8405247);
   }

   private void methodE() {
      int i = this.getGlintColorSetting();
      this.intD = i;
      Identifier identifier = identifierB;
      this.methodA(identifier, i);
      Identifier identifier1 = identifierC;
      this.methodA(identifier1, i);
   }

   private void methodF() {
      Identifier identifier = identifierB;
      this.methodA(identifier);
      Identifier identifier1 = identifierC;
      this.methodA(identifier1);
      this.intD = Integer.MIN_VALUE;
      this.intE = Integer.MIN_VALUE;
   }

   private void methodA(Identifier identifier, int i) {
      NativeImage nativeImage = this.methodB(identifier);
      if (nativeImage != null) {
         try {
            int l = i >> 16 & 0xFF;
            int m = i >> 8 & 0xFF;
            int n = i & 0xFF;
            int j = 0;

            while (true) {
               int o = j;
               int y = nativeImage.getHeight();
               if (o >= y) {
                  TextureManager textureManager = Minecraft.getInstance().getTextureManager();
                  Supplier supplier = () -> "clarity_glint";
                  DynamicTexture dynamicTexture = new DynamicTexture(supplier, nativeImage);
                  textureManager.register(identifier, dynamicTexture);
                  return;
               }

               int k = 0;

               while (true) {
                  int p = k;
                  int z = nativeImage.getWidth();
                  if (p >= z) {
                     j++;
                     break;
                  }

                  int ba = k;
                  int bc = j;
                  int q = nativeImage.getPixel(ba, bc);
                  int r = q >>> 24 & 0xFF;
                  int s = q >>> 16 & 0xFF;
                  int t = q >>> 8 & 0xFF;
                  int u = q & 0xFF;
                  int v = s * l / 255;
                  int w = t * m / 255;
                  int x = u * n / 255;
                  int bb = k;
                  int bd = j;
                  int be = r << 24;
                  int bh = v << 16;
                  int bf = be | bh;
                  int bi = w << 8;
                  int bg = bf | bi | x;
                  nativeImage.setPixel(bb, bd, bg);
                  k++;
               }
            }
         } catch (Throwable throwable1) {
            try {
               nativeImage.close();
            } catch (Throwable throwable) {
            }
         }
      }
   }

   private void methodA(Identifier identifier) {
      NativeImage nativeImage = this.methodB(identifier);
      if (nativeImage != null) {
         try {
            TextureManager textureManager = Minecraft.getInstance().getTextureManager();
            Supplier supplier = () -> "clarity_glint";
            DynamicTexture dynamicTexture = new DynamicTexture(supplier, nativeImage);
            textureManager.register(identifier, dynamicTexture);
         } catch (Throwable throwable1) {
            try {
               nativeImage.close();
            } catch (Throwable throwable) {
            }
         }
      }
   }

   private NativeImage methodB(Identifier identifier) {
      try (InputStream inputStream = ((Resource)Minecraft.getInstance().getResourceManager().getResource(identifier).orElseThrow()).open()) {
         return NativeImage.read(inputStream);
      } catch (Throwable throwable) {
         return null;
      }
   }

   public static boolean methodA() {
      EnchantGlint enchantGlint = instance;
      boolean bl;
      if (enchantGlint != null && enchantGlint.isEnabled()) {
         bl = true;
      } else {
         bl = false;
      }

      return bl;
   }

   public static float[] methodB() {
      return new float[]{0.502F, 0.251F, 1.0F, 0.502F};
   }
}
