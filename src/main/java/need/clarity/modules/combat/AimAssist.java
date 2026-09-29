package need.clarity.modules.combat;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import need.clarity.PerFrameEvents;
import need.clarity.modules.ModuleBase;
import need.clarity.modules.utility.Targets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class AimAssist extends ModuleBase {
   private long longC = -1L;
   private static float floatD = 0.0F;
   private static float floatE = 0.0F;
   private static boolean flagF = false;
   private static boolean flagG = false;
   private static boolean flagH = false;
   private static boolean flagI = false;
   private static volatile LivingEntity livingEntityJ = null;
   public static float floatA = 0.0F;
   public static float floatB = 0.0F;
   private static boolean flagK = false;
   private static float floatL = 0.0F;
   private static float floatM = 0.0F;

   public static void methodA(float f, float g) {
      flagK = true;
      floatL = f;
      floatM = g;
   }

   public static boolean isFlagF() {
      return flagF;
   }

   public static boolean isFlagI() {
      return flagI;
   }

   public static float getFloatD() {
      return floatD;
   }

   public static float getFloatE() {
      return floatE;
   }

   public static Vec3 methodB(float f, float g) {
      float h = f * (float) (Math.PI / 180.0);
      float i = g * (float) (-Math.PI / 180.0);
      float j = Mth.cos(h);
      float k = Mth.sin(h);
      float l = Mth.cos(i);
      float m = Mth.sin(i);
      double d = m * j;
      double e = k * -1.0F;
      double n = l * j;
      return new Vec3(d, e, n);
   }

   public AimAssist() {
      super("Aim Assist", "Combat");
      PerFrameEvents.register(this::methodF);
   }

   @Override
   public void onEnable() {
      this.longC = -1L;
      flagH = false;
   }

   @Override
   public void onDisable() {
      if (!flagF && !flagG) {
         flagF = false;
         flagG = false;
         flagH = false;
         flagI = false;
      } else {
         flagI = false;
      }
   }

   private void methodF() {
      flagG = flagF;
      flagF = false;
      flagI = false;
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null && minecraft.level != null) {
         long m = System.currentTimeMillis();
         long bm;
         if (this.longC < 0L) {
            bm = 1L;
         } else {
            long cc = this.longC;
            bm = Math.min(m - cc, 100L);
         }

         long l;
         boolean bl;
         boolean bl3;
         label138: {
            l = bm;
            this.longC = m;
            Boolean boolean1 = false;
            bl = this.getSetting("Silent Rotations", boolean1);
            if (this.isEnabled()) {
               Boolean boolean2 = true;
               if (!this.getSetting("Not in GUI", boolean2) || minecraft.screen == null) {
                  Boolean boolean3 = false;
                  if (!this.getSetting("Not While Using", boolean3) || !minecraft.player.isUsingItem()) {
                     Boolean boolean4 = false;
                     if (!this.getSetting("Only Weapon", boolean4) || methodA(minecraft.player)) {
                        bl3 = true;
                        break label138;
                     }
                  }
               }
            }

            bl3 = false;
         }

         if (bl3) {
            Integer integer = 5;
            int j = this.getSetting("Range", integer);
            double d = j;
            double cd = j;
            double e = d * cd;
            Stream stream = StreamSupport.stream(minecraft.level.entitiesForRendering().spliterator(), false);
            Predicate predicate = entity -> {
               if (entity instanceof Player) {
                  LocalPlayer localPlayer3 = minecraft.player;
                  if (entity != localPlayer3) {
                     LocalPlayer localPlayer4 = minecraft.player;
                     if (entity.distanceToSqr(localPlayer4) <= e && !AntiBot.methodA((LivingEntity)entity)) {
                        return true;
                     }
                  }
               }

               return false;
            };
            Stream stream1 = stream.filter(predicate);
            Function function = entity -> (LivingEntity)entity;
            Stream stream2 = stream1.map(function);
            Collector collector = Collectors.toList();
            List list = stream2.collect(collector);
            LivingEntity livingEntity1;
            if (list.isEmpty()) {
               livingEntity1 = null;
            } else {
               LocalPlayer localPlayer2 = minecraft.player;
               livingEntity1 = Targets.methodA(list, localPlayer2);
            }

            LivingEntity livingEntity = livingEntity1;
            if (livingEntity == null) {
               livingEntityJ = null;
               if (bl && flagG) {
                  this.methodA(minecraft, l);
               } else {
                  flagH = false;
               }
            } else {
               livingEntityJ = livingEntity;
               flagH = false;
               Vec3 vec3 = minecraft.player.getEyePosition();
               String string = this.getSetting("Aim At", "Closest");
               Vec3 vec34;
               if ("Center".equals(string)) {
                  AABB aABB1 = livingEntity.getBoundingBox();
                  double cm = aABB1.minX;
                  double cu = aABB1.maxX;
                  double cn = (cm + cu) / 2.0;
                  double cv = aABB1.minY;
                  double dc = aABB1.maxY;
                  double cw = (cv + dc) / 2.0;
                  double dd = aABB1.minZ;
                  double di = aABB1.maxZ;
                  double de = (dd + di) / 2.0;
                  Vec3 vec33 = new Vec3(cn, cw, de);
                  vec34 = vec33;
               } else {
                  AABB aABB = livingEntity.getBoundingBox();
                  double ce = aABB.minX;
                  double cp = vec3.x;
                  double cx = aABB.maxX;
                  double cq = Math.min(cp, cx);
                  double cf = Math.max(ce, cq);
                  double cr = aABB.minY;
                  double cy = vec3.y;
                  double df = aABB.maxY;
                  double cz = Math.min(cy, df);
                  double cs = Math.max(cr, cz);
                  double da = aABB.minZ;
                  double dg = vec3.z;
                  double dj = aABB.maxZ;
                  double dh = Math.min(dg, dj);
                  double db = Math.max(da, dh);
                  Vec3 vec31 = new Vec3(cf, cs, db);
                  vec34 = vec31;
               }

               Vec3 vec32 = vec34.subtract(vec3);
               if (!(vec32.lengthSqr() < 1.0E-6)) {
                  double k = vec32.x;
                  double cg = vec32.x;
                  double n = k * cg;
                  double ch = vec32.z;
                  double ct = vec32.z;
                  double ci = ch * ct;
                  double o = Math.sqrt(n + ci);
                  double p = vec32.x * -1.0;
                  double cj = vec32.z;
                  float q = (float)(Math.atan2(p, cj) * 180.0 / Math.PI);
                  float r = (float)(Math.atan2(vec32.y, o) * -1.0 * 180.0 / Math.PI);
                  if (bl && !flagG) {
                     floatD = minecraft.player.getYRot();
                     floatE = minecraft.player.getXRot();
                  }

                  float bp;
                  if (bl && flagG) {
                     bp = floatD;
                  } else {
                     bp = minecraft.player.getYRot();
                  }

                  float f = bp;
                  float bn;
                  if (bl && flagG) {
                     bn = floatE;
                  } else {
                     bn = minecraft.player.getXRot();
                  }

                  float g = bn;
                  float s = methodA(q - f);
                  float t = methodA(r - g);
                  Integer integer1 = 90;
                  int u = this.getSetting("FOV", integer1);
                  float v = minecraft.player.getYRot();
                  float w = minecraft.player.getXRot();
                  float x = methodA(q - v);
                  float y = methodA(r - w);
                  float z = x * x;
                  float bq = y * y;
                  float ba = (float)Math.sqrt(z + bq);
                  float br = u / 2.0F;
                  if (!(ba > br)) {
                     Integer integer2 = 10;
                     int bb = this.getSetting("Horizontal Speed", integer2);
                     Integer integer3 = 10;
                     int bc = this.getSetting("Vertical Speed", integer3);
                     Boolean boolean5 = true;
                     boolean bl1 = this.getSetting("Aim Horizontally", boolean5);
                     Boolean boolean6 = true;
                     boolean bl2 = this.getSetting("Aim Vertically", boolean6);
                     float bs = bb / 20.0F;
                     float bd = s * bs;
                     float bt = (float)l / 50.0F;
                     float be = bd * bt;
                     float bu = bc / 20.0F;
                     float bf = t * bu;
                     float bv = (float)l / 50.0F;
                     float bg = bf * bv;
                     float bw = Math.abs(s);
                     float bh = methodD(be, bw);
                     float bx = Math.abs(t);
                     float bi = methodD(bg, bx);
                     if (bl) {
                        float bk;
                        if (bl1) {
                           bk = f + bh;
                        } else {
                           bk = f;
                        }

                        float h = bk;
                        float bo;
                        if (bl2) {
                           bo = g + bi;
                        } else {
                           bo = g;
                        }

                        float i = bo;
                        float bj = floatD;
                        float cl = floatD;
                        float ca = methodA(h - cl);
                        floatD = bj + ca;
                        float cb = Math.min(90.0F, i);
                        floatE = Math.max(-90.0F, cb);
                        flagF = true;
                        flagI = true;
                     } else {
                        if (bl1) {
                           LocalPlayer localPlayer = minecraft.player;
                           float by = f + bh;
                           localPlayer.setYRot(by);
                        }

                        if (bl2) {
                           LocalPlayer localPlayer1 = minecraft.player;
                           float co = g + bi;
                           float ck = Math.min(90.0F, co);
                           float bz = Math.max(-90.0F, ck);
                           localPlayer1.setXRot(bz);
                        }
                     }
                  } else if (bl) {
                     if (flagG) {
                        this.methodA(minecraft, l);
                     }
                  }
               }
            }
         } else if (bl && flagG) {
            this.methodA(minecraft, l);
         } else {
            flagH = false;
         }
      } else {
         flagH = false;
         flagG = false;
      }
   }

   private void methodA(Minecraft minecraft, long l) {
      flagH = true;
      float g = minecraft.player.getYRot();
      float h = minecraft.player.getXRot();
      float f = floatD;
      float i = methodA(g - f);
      float t = floatE;
      float j = methodA(h - t);
      if (Math.abs(i) < 0.5F && Math.abs(j) < 0.5F) {
         flagF = false;
         flagG = false;
         flagH = false;
         flagI = false;
         floatD = g;
         floatE = h;
         floatA = g;
         floatB = h;
      } else {
         Integer integer = 10;
         int k = this.getSetting("Horizontal Speed", integer);
         Integer integer1 = 10;
         int m = this.getSetting("Vertical Speed", integer1);
         float u = k / 20.0F;
         float n = i * u;
         float v = (float)l / 50.0F;
         float o = n * v;
         float w = Math.abs(i);
         float p = methodD(o, w);
         float x = m / 20.0F;
         float q = j * x;
         float y = (float)l / 50.0F;
         float r = q * y;
         float z = Math.abs(j);
         float s = methodD(r, z);
         floatD += p;
         float bb = floatE + s;
         float ba = Math.min(90.0F, bb);
         floatE = Math.max(-90.0F, ba);
         flagF = true;
      }
   }

   public static void methodC(float f, float g) {
      floatD = f;
      float h = Math.min(90.0F, g);
      floatE = Math.max(-90.0F, h);
      flagF = true;
      flagI = true;
   }

   public static void methodE() {
      flagI = false;
   }

   private static boolean methodA(LocalPlayer localPlayer) {
      ItemStack itemStack = localPlayer.getMainHandItem();
      Item item = itemStack.getItem();
      TagKey tagKey = ItemTags.SWORDS;
      if (!itemStack.is(tagKey)) {
         TagKey tagKey1 = ItemTags.AXES;
         if (!itemStack.is(tagKey1)) {
            TagKey tagKey2 = ItemTags.MACE_ENCHANTABLE;
            if (!itemStack.is(tagKey2) && !(item instanceof TridentItem)) {
               return false;
            }
         }
      }

      return true;
   }

   private static float methodA(float f) {
      float g = f % 360.0F;
      if (g >= 180.0F) {
         g -= 360.0F;
      }

      if (g < -180.0F) {
         g += 360.0F;
      }

      return g;
   }

   private static float methodD(float f, float g) {
      float h = g * -1.0F;
      if (f > g) {
         return g;
      } else {
         return f < h ? h : f;
      }
   }
}
