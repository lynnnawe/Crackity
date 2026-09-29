package need.clarity.modules.visual;

import need.clarity.PerFrameEvents;
import need.clarity.modules.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public class Freecam extends ModuleBase {
   private static Freecam instance;
   public static volatile Vec3 vec3A = Vec3.ZERO;
   public static volatile float floatB = 0.0F;
   public static volatile float floatC = 0.0F;
   private static volatile Vec3 vec3E = Vec3.ZERO;
   private static volatile float floatF = 0.0F;
   private static volatile float floatG = 0.0F;
   private static volatile boolean flagH = false;
   private long longI = -1L;
   private ClientLevel clientLevelJ;
   private LocalPlayer localPlayerK;
   private final Runnable runnableL;

   public Freecam() {
      super("Freecam", "Visual");
      Runnable runnable = this::methodH;
      this.runnableL = runnable;
      instance = this;
   }

   public static boolean isFlagH() {
      return flagH;
   }

   public static Vec3 getVec3A() {
      return vec3A;
   }

   private static Freecam getInstance() {
      return instance;
   }

   public static boolean isFreezePlayerSetting() {
      Freecam freecam = getInstance();
      if (freecam != null) {
         Boolean boolean_ = true;
         if (!freecam.getSetting("Freeze Player", boolean_)) {
            return false;
         }
      }

      return true;
   }

   public static boolean isNoClipSetting() {
      Freecam freecam = getInstance();
      if (freecam != null) {
         Boolean boolean_ = true;
         if (!freecam.getSetting("No Clip", boolean_)) {
            return false;
         }
      }

      return true;
   }

   public static float getSpeedSetting() {
      Freecam freecam = getInstance();
      float f;
      if (freecam == null) {
         f = 5.0F;
      } else {
         Float float1 = 5.0F;
         f = freecam.getSetting("Speed", float1);
      }

      return f;
   }

   public static float getVerticalSpeedSetting() {
      Freecam freecam = getInstance();
      float f;
      if (freecam == null) {
         f = 5.0F;
      } else {
         Float float1 = 5.0F;
         f = freecam.getSetting("Vertical Speed", float1);
      }

      return f;
   }

   @Override
   public void onEnable() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         if (minecraft.level != null) {
            this.methodA(minecraft);
         }
      }
   }

   @Override
   public void onDisable() {
      flagH = false;
      this.longI = -1L;
      this.clientLevelJ = null;
      this.localPlayerK = null;
      PerFrameEvents.methodA(this.runnableL);
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         LocalPlayer localPlayer = minecraft.player;
         Vec3 vec3 = Vec3.ZERO;
         localPlayer.setDeltaMovement(vec3);
         minecraft.player.resetFallDistance();
         LocalPlayer localPlayer1 = minecraft.player;
         float f = floatF;
         localPlayer1.setYRot(f);
         LocalPlayer localPlayer2 = minecraft.player;
         float g = floatG;
         localPlayer2.setXRot(g);
         LocalPlayer localPlayer3 = minecraft.player;
         float h = floatF;
         localPlayer3.yRotO = h;
         LocalPlayer localPlayer4 = minecraft.player;
         float i = floatG;
         localPlayer4.xRotO = i;
      }
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null && minecraft.level != null) {
         if (!flagH) {
            this.methodA(minecraft);
         } else {
            LocalPlayer localPlayer = minecraft.player;
            LocalPlayer localPlayer7 = this.localPlayerK;
            if (localPlayer == localPlayer7) {
               ClientLevel clientLevel = minecraft.level;
               ClientLevel clientLevel1 = this.clientLevelJ;
               if (clientLevel == clientLevel1) {
                  if (!minecraft.player.isRemoved() && !minecraft.player.isDeadOrDying()) {
                     if (!isFreezePlayerSetting()) {
                        return;
                     }

                     LocalPlayer localPlayer1 = minecraft.player;
                     Vec3 vec3 = Vec3.ZERO;
                     localPlayer1.setDeltaMovement(vec3);
                     minecraft.player.resetFallDistance();
                     LocalPlayer localPlayer2 = minecraft.player;
                     float f = floatF;
                     localPlayer2.setYRot(f);
                     LocalPlayer localPlayer3 = minecraft.player;
                     float g = floatG;
                     localPlayer3.setXRot(g);
                     LocalPlayer localPlayer4 = minecraft.player;
                     float h = floatF;
                     localPlayer4.yRotO = h;
                     LocalPlayer localPlayer5 = minecraft.player;
                     float i = floatG;
                     localPlayer5.xRotO = i;
                     if (!minecraft.player.isPassenger()) {
                        LocalPlayer localPlayer6 = minecraft.player;
                        double d = vec3E.x;
                        double e = vec3E.y;
                        double j = vec3E.z;
                        localPlayer6.setPos(d, e, j);
                        return;
                     }

                     return;
                  }

                  this.setEnabled(false);
                  return;
               }
            }

            this.setEnabled(false);
         }
      } else if (flagH) {
         this.setEnabled(false);
      }
   }

   private void methodA(Minecraft minecraft) {
      if (!flagH) {
         vec3A = minecraft.player.getEyePosition(1.0F);
         vec3E = minecraft.player.position();
         floatF = minecraft.player.getYRot();
         floatG = minecraft.player.getXRot();
         floatB = floatF;
         floatC = floatG;
         flagH = true;
         long l = System.nanoTime();
         this.longI = l;
         ClientLevel clientLevel = minecraft.level;
         this.clientLevelJ = clientLevel;
         LocalPlayer localPlayer2 = minecraft.player;
         this.localPlayerK = localPlayer2;
         if (isFreezePlayerSetting()) {
            LocalPlayer localPlayer = minecraft.player;
            Vec3 vec3 = Vec3.ZERO;
            localPlayer.setDeltaMovement(vec3);
            minecraft.player.resetFallDistance();
            LocalPlayer localPlayer1 = minecraft.player;
            double d = vec3E.x;
            double e = vec3E.y;
            double f = vec3E.z;
            localPlayer1.setPos(d, e, f);
         }

         PerFrameEvents.register(this.runnableL);
      }
   }

   private void methodH() {
      if (flagH) {
         Minecraft minecraft = Minecraft.getInstance();
         if (minecraft.player != null) {
            if (minecraft.level != null) {
               long l = System.nanoTime();
               long ba = this.longI;
               double e = (l - ba) / 1.0E9;
               this.longI = l;
               if (!(e <= 0.0)) {
                  if (e > 0.1) {
                     e = 0.1;
                  }

                  if (minecraft.screen == null) {
                     if (!minecraft.isPaused()) {
                        boolean bl = minecraft.options.keyUp.isDown();
                        boolean bl1 = minecraft.options.keyDown.isDown();
                        boolean bl2 = minecraft.options.keyLeft.isDown();
                        boolean bl3 = minecraft.options.keyRight.isDown();
                        boolean bl4 = minecraft.options.keyJump.isDown();
                        boolean bl5 = minecraft.options.keyShift.isDown();
                        boolean bl6 = minecraft.options.keySprint.isDown();
                        if (bl || bl1 || bl2 || bl3 || bl4 || bl5) {
                           double j = getSpeedSetting() * 2.0;
                           double bb = e;
                           double f = j * bb;
                           double k = getVerticalSpeedSetting() * 2.0;
                           double bc = e;
                           double m = k * bc;
                           if (bl6) {
                              f *= 2.0;
                           }

                           double n = Math.toRadians(floatB);
                           double o = Math.sin(n);
                           double p = Math.cos(n);
                           double q = -o;
                           double r = -p;
                           double s = -o;
                           double g = 0.0;
                           double h = 0.0;
                           double i = 0.0;
                           if (bl) {
                              g = 0.0 + q;
                              i = 0.0 + p;
                           }

                           if (bl1) {
                              g -= q;
                              i -= p;
                           }

                           if (bl2) {
                              g -= r;
                              i -= s;
                           }

                           if (bl3) {
                              g += r;
                              i += s;
                           }

                           double t = g;
                           double bd = g;
                           double u = t * bd;
                           double be = i;
                           double d = i;
                           double bf = be * d;
                           double v = Math.sqrt(u + bf);
                           if (v > 1.0) {
                              g /= v;
                              i /= v;
                           }

                           double w = g;
                           double bg = f;
                           double x = w * bg;
                           double y = i;
                           double bh = f;
                           double z = y * bh;
                           if (bl4) {
                              h = 0.0 + m;
                           }

                           if (bl5) {
                              h -= m;
                           }

                           double bi = h;
                           Vec3 vec3 = new Vec3(x, bi, z);
                           Vec3 vec31;
                           if (isNoClipSetting()) {
                              vec31 = vec3A.add(vec3);
                           } else {
                              Vec3 vec32 = vec3A;
                              vec31 = methodA(minecraft, vec32, vec3);
                           }

                           vec3A = vec31;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private static Vec3 methodA(Minecraft minecraft, Vec3 vec3, Vec3 vec31) {
      double d = vec31.x;
      Vec3 vec32 = new Vec3(d, 0.0, 0.0);
      Vec3 vec33 = methodB(minecraft, vec3, vec32);
      double e = vec31.y;
      Vec3 vec35 = new Vec3(0.0, e, 0.0);
      Vec3 vec34 = methodB(minecraft, vec33, vec35);
      double f = vec31.z;
      Vec3 vec36 = new Vec3(0.0, 0.0, f);
      return methodB(minecraft, vec34, vec36);
   }

   private static Vec3 methodB(Minecraft minecraft, Vec3 vec3, Vec3 vec31) {
      if (vec31.lengthSqr() < 1.0E-8) {
         return vec3;
      }

      Vec3 vec32 = vec3.add(vec31);
      ClientLevel clientLevel = minecraft.level;
      Block block = Block.COLLIDER;
      Fluid fluid = Fluid.NONE;
      LocalPlayer localPlayer = minecraft.player;
      ClipContext clipContext = new ClipContext(vec3, vec32, block, fluid, localPlayer);
      BlockHitResult blockHitResult = clientLevel.clip(clipContext);
      Type type = blockHitResult.getType();
      Type type1 = Type.MISS;
      if (type == type1) {
         return vec32;
      }

      Vec3 vec33 = vec31.normalize();
      Vec3 vec34 = blockHitResult.getLocation();
      Vec3 vec35 = vec33.scale(0.01);
      return vec34.subtract(vec35);
   }
}
