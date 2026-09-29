package need.clarity.modules.visual;

import com.mojang.blaze3d.vertex.PoseStack;
import need.clarity.WorldRenderEvent;
import need.clarity.WorldRenderEvent$Listener;
import need.clarity.config.ThemeColors;
import need.clarity.modules.ModuleBase;
import need.clarity.render.BoxRenderer;
import need.clarity.render.ColoredBox;
import need.clarity.render.PlayerEspRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;

public class PlayerESP extends ModuleBase {
   public static PlayerESP instance;
   private final WorldRenderEvent$Listener listenerB;
   private boolean flagC;

   public PlayerESP() {
      super("Player ESP", "Visual");
      WorldRenderEvent$Listener listener = this::methodA;
      this.listenerB = listener;
      this.flagC = false;
      instance = this;
      if (this.isEnabled()) {
         this.methodU();
      }
   }

   @Override
   public void onEnable() {
      this.methodU();
   }

   @Override
   public void onDisable() {
      this.methodV();
   }

   private void methodU() {
      if (!this.flagC) {
         this.flagC = true;
         WorldRenderEvent.register(this.listenerB);
      }
   }

   private void methodV() {
      if (this.flagC) {
         this.flagC = false;
         WorldRenderEvent.methodA(this.listenerB);
      }
   }

   private void methodA(PoseStack poseStack, float f) {
      if (this.isEnabled()) {
         Minecraft minecraft = Minecraft.getInstance();
         if (minecraft != null) {
            if (minecraft.level != null) {
               if (minecraft.player != null) {
                  boolean bl = isHideSelfSetting();
                  boolean bl1 = isHideInvisibleSetting();
                  boolean bl2 = isHideTeammatesSetting();
                  boolean bl3 = isSmoothSetting();
                  boolean bl4 = isHealthColorSetting();
                  boolean bl5 = isChromaSetting();
                  float i = getChromaSpeedSetting();
                  float j = getMaxDistanceSetting();
                  boolean bl6 = isDistanceFadeSetting();
                  float t;
                  if (j > 0.5F) {
                     t = j;
                  } else {
                     t = 32.0F;
                  }

                  float g = t;
                  int k = getOutlineColorSetting();
                  int l = getHealthLowSetting();
                  int m = getHealthHighSetting();
                  double d = System.nanoTime() / 1.0E9;
                  Vec3 vec31 = BoxRenderer.methodA();
                  LocalPlayer localPlayer = minecraft.player;
                  java.util.ArrayList arrayList = new java.util.ArrayList();

                  for (Player player : minecraft.level.players()) {
                     if ((player != localPlayer || !bl)
                        && player.isAlive()
                        && !player.isSpectator()
                        && (!bl1 || !player.isInvisible())
                        && (!bl2 || !methodA(player, localPlayer))) {
                        Vec3 vec32;
                        if (bl3) {
                           vec32 = player.getPosition(f);
                        } else {
                           vec32 = player.position();
                        }

                        Vec3 vec3 = vec32;
                        AABB aABB = player.getBoundingBox();
                        double u = vec3.x;
                        double bh = player.getX();
                        double v = u - bh;
                        double bi = vec3.y;
                        double bm = player.getY();
                        double bj = bi - bm;
                        double bn = vec3.z;
                        double bp = player.getZ();
                        double bo = bn - bp;
                        AABB aABB1 = aABB.move(v, bj, bo);
                        double e = vec31.distanceTo(vec3);
                        if (j > 0.5F) {
                           double z = j;
                           if (e > z) {
                              continue;
                           }
                        }

                        float h = 1.0F;
                        if (bl6) {
                           double bk = g;
                           double ba = e / bk;
                           h = (float)(1.0 - ba);
                           if (h < 0.0F) {
                              h = 0.0F;
                           }

                           if (h > 1.0F) {
                              h = 1.0F;
                           }
                        }

                        int bq;
                        if (bl5) {
                           double bb = i;
                           double n = d * bb;
                           double bc = vec3.x * 0.03;
                           double o = n + bc;
                           double bd = vec3.y * 0.02;
                           double p = o + bd;
                           double be = vec3.z * 0.03;
                           bq = methodA((float)(p + be), 1.0F, 1.0F);
                        } else if (bl4) {
                           float s = player.getHealth();
                           float bf = player.getMaxHealth();
                           float y = Math.max(1.0F, bf);
                           float bg = methodA(s / y);
                           bq = methodA(l, m, bg);
                        } else {
                           bq = k & 16777215;
                        }

                        float w = h;
                        int q = Math.round(255.0F * w) << 24;
                        int x = bq & 16777215;
                        int r = q | x;
                        ColoredBox coloredBox = new ColoredBox(aABB1, r);
                        arrayList.add(coloredBox);
                     }
                  }

                  if (!arrayList.isEmpty()) {
                     PlayerEspRenderer.methodA(poseStack, arrayList, f);
                  }
               }
            }
         }
      }
   }

   private static boolean methodA(Player player, Player player1) {
      PlayerTeam playerTeam = player.getTeam();
      PlayerTeam playerTeam1 = player1.getTeam();
      if (playerTeam != null && playerTeam1 != null) {
         String string = playerTeam.getName();
         String string1 = playerTeam1.getName();
         boolean bl;
         if (string != null && string.equals(string1)) {
            bl = true;
         } else {
            bl = false;
         }

         return bl;
      } else {
         return false;
      }
   }

   private static float methodA(float f) {
      float g;
      if (f < 0.0F) {
         g = 0.0F;
      } else if (f > 1.0F) {
         g = 1.0F;
      } else {
         g = f;
      }

      return g;
   }

   private static int methodA(int i, int j, float f) {
      float g = (i >> 16 & 0xFF) / 255.0F;
      float h = (i >> 8 & 0xFF) / 255.0F;
      float k = (i & 0xFF) / 255.0F;
      float l = (j >> 16 & 0xFF) / 255.0F;
      float m = (j >> 8 & 0xFF) / 255.0F;
      float n = (j & 0xFF) / 255.0F;
      float s = (l - g) * f;
      int o = Math.round((g + s) * 255.0F);
      float t = (m - h) * f;
      int p = Math.round((h + t) * 255.0F);
      float u = (n - k) * f;
      int q = Math.round((k + u) * 255.0F);
      int r = o << 16;
      int v = p << 8;
      return r | v | q;
   }

   private static int methodA(float f, float g, float h) {
      float s = (float)Math.floor(f);
      float i = (f - s) * 6.0F;
      int j = (int)i;
      float t = j;
      float k = i - t;
      float u = 1.0F - g;
      float l = h * u;
      float y = g * k;
      float v = 1.0F - y;
      float m = h * v;
      float ba = 1.0F - k;
      float z = g * ba;
      float w = 1.0F - z;
      float n = h * w;
      float bb;
      float bc;
      float bd;
      switch (j % 6) {
         case 0:
            bb = h;
            bc = n;
            bd = l;
            break;
         case 1:
            bb = m;
            bc = h;
            bd = l;
            break;
         case 2:
            bb = l;
            bc = h;
            bd = n;
            break;
         case 3:
            bb = l;
            bc = m;
            bd = h;
            break;
         case 4:
            bb = n;
            bc = l;
            bd = h;
            break;
         default:
            bb = h;
            bc = l;
            bd = m;
      }

      int o = Math.round(bb * 255.0F);
      int p = Math.round(bc * 255.0F);
      int q = Math.round(bd * 255.0F);
      int r = o << 16;
      int x = p << 8;
      return r | x | q;
   }

   public static String getModeSetting() {
      String string1;
      if (instance != null) {
         string1 = instance.getSetting("Mode", "Outline");
      } else {
         string1 = "Outline";
      }

      String string = string1;
      return !"Box".equals(string) && !"3D".equals(string) && !"Outline".equals(string) ? "Outline" : string;
   }

   public static boolean isOutlineSetting() {
      PlayerESP playerESP = instance;
      Boolean boolean_ = true;
      return playerESP.getSetting("Outline", boolean_);
   }

   public static int getOutlineColorSetting() {
      return methodA(instance.getSetting("Outline Color", ""), -16733441, false);
   }

   public static float getOutlineThicknessSetting() {
      PlayerESP playerESP = instance;
      Float float_ = 2.0F;
      return playerESP.getSetting("Outline Thickness", float_);
   }

   public static boolean isFillSetting() {
      PlayerESP playerESP = instance;
      Boolean boolean_ = false;
      return playerESP.getSetting("Fill", boolean_);
   }

   public static int getFillColorSetting() {
      return methodA(instance.getSetting("Fill Color", ""), -16733441, true);
   }

   public static float getFillOpacitySetting() {
      PlayerESP playerESP = instance;
      Float float_ = 0.25F;
      return playerESP.getSetting("Fill Opacity", float_);
   }

   public static String getBoxStyleSetting() {
      return instance.getSetting("Box Style", "Full");
   }

   public static boolean isChromaSetting() {
      PlayerESP playerESP = instance;
      Boolean boolean_ = false;
      return playerESP.getSetting("Chroma", boolean_);
   }

   public static float getChromaSpeedSetting() {
      PlayerESP playerESP = instance;
      Float float_ = 0.3F;
      return playerESP.getSetting("Chroma Speed", float_);
   }

   public static boolean isHealthColorSetting() {
      PlayerESP playerESP = instance;
      Boolean boolean_ = false;
      return playerESP.getSetting("Health Color", boolean_);
   }

   public static int getHealthLowSetting() {
      return methodA(instance.getSetting("Health Low", ""), -50373, true);
   }

   public static int getHealthHighSetting() {
      return methodA(instance.getSetting("Health High", ""), -12845218, false);
   }

   public static float getMaxDistanceSetting() {
      PlayerESP playerESP = instance;
      Float float_ = 0.0F;
      return playerESP.getSetting("Max Distance", float_);
   }

   public static boolean isDistanceFadeSetting() {
      PlayerESP playerESP = instance;
      Boolean boolean_ = false;
      return playerESP.getSetting("Distance Fade", boolean_);
   }

   public static boolean isThroughWallsSetting() {
      PlayerESP playerESP = instance;
      Boolean boolean_ = true;
      return playerESP.getSetting("Through Walls", boolean_);
   }

   public static boolean isHideInvisibleSetting() {
      PlayerESP playerESP = instance;
      Boolean boolean_ = true;
      return playerESP.getSetting("Hide Invisible", boolean_);
   }

   public static boolean isHideTeammatesSetting() {
      PlayerESP playerESP = instance;
      Boolean boolean_ = false;
      return playerESP.getSetting("Hide Teammates", boolean_);
   }

   public static boolean isHideSelfSetting() {
      PlayerESP playerESP = instance;
      Boolean boolean_ = true;
      return playerESP.getSetting("Hide Self", boolean_);
   }

   public static boolean isSmoothSetting() {
      PlayerESP playerESP = instance;
      Boolean boolean_ = true;
      return playerESP.getSetting("Smooth", boolean_);
   }

   private static int methodA(String string, int i, boolean bl) {
      return ThemeColors.resolveArgb(string, i, bl);
   }
}
