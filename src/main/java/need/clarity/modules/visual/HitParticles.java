package need.clarity.modules.visual;

import java.util.Random;
import need.clarity.EventStartAttack;
import need.clarity.PerAttackEvents;
import need.clarity.config.ThemeColors;
import need.clarity.modules.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;

public class HitParticles extends ModuleBase {
   private final Random random;

   public HitParticles() {
      super("Hit Particles", "Visual");
      Random randomx = new Random();
      this.random = randomx;
      PerAttackEvents.register(this::methodA);
   }

   private void methodA(EventStartAttack eventStartAttack) {
      if (this.isEnabled()) {
         Entity entity = eventStartAttack.getTarget();
         if (entity != null) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
               String string = this.getSetting("Particle", "Crit");
               Integer integer = 10;
               int j = this.getSetting("Count", integer);
               Float float1 = 0.3F;
               float f = this.getSetting("Spread", float1);
               int k = ThemeColors.resolveRgb(this.getSetting("Colour", "#FF0000"), 16711680, false);
               ParticleOptions particleOptions = methodA(string, k);
               if (particleOptions != null) {
                  double d = entity.getX();
                  double e = entity.getY();
                  double v = entity.getBbHeight() * 0.5;
                  double g = e + v;
                  double h = entity.getZ();
                  double l = f + 0.2;

                  for (int i = 0; i < j; i++) {
                     double m = this.random.nextDouble() - 0.5;
                     double w = f;
                     double n = m * w * 2.0;
                     double o = this.random.nextDouble() - 0.5;
                     double x = f;
                     double p = o * x * 2.0;
                     double q = this.random.nextDouble() - 0.5;
                     double y = f;
                     double r = q * y * 2.0;
                     double s = (this.random.nextDouble() - 0.5) * 0.3 * l;
                     double t = this.random.nextDouble() * 0.2 * l;
                     double u = (this.random.nextDouble() - 0.5) * 0.3 * l;
                     ClientLevel clientLevel = minecraft.level;
                     double z = d + n;
                     double ba = g + p;
                     double bb = h + r;
                     clientLevel.addParticle(particleOptions, z, ba, bb, s, t, u);
                  }
               }
            }
         }
      }
   }

   private static ParticleOptions methodA(String string, int i) {
      if (string == null) {
         return ParticleTypes.CRIT;
      }

      byte b = -1;
      int k = string.hashCode();
      switch (k) {
         case -2094112058:
            if (string.equals("Soul Flame")) {
               b = 3;
            }
            break;
         case -1342130166:
            if (string.equals("Large Smoke")) {
               b = 6;
            }
            break;
         case -985741672:
            if (string.equals("Enchanted")) {
               b = 0;
            }
            break;
         case 2108922:
            if (string.equals("Crit")) {
               b = 14;
            }
            break;
         case 2141906:
            if (string.equals("Dust")) {
               b = 13;
            }
            break;
         case 2361088:
            if (string.equals("Lava")) {
               b = 9;
            }
            break;
         case 2434066:
            if (string.equals("Note")) {
               b = 12;
            }
            break;
         case 47520061:
            if (string.equals("Electric")) {
               b = 8;
            }
            break;
         case 57074745:
            if (string.equals("Enchant")) {
               b = 1;
            }
            break;
         case 65203733:
            if (string.equals("Cloud")) {
               b = 7;
            }
            break;
         case 67960595:
            if (string.equals("Flame")) {
               b = 2;
            }
            break;
         case 69599270:
            if (string.equals("Heart")) {
               b = 4;
            }
            break;
         case 80009551:
            if (string.equals("Smoke")) {
               b = 5;
            }
            break;
         case 80997281:
            if (string.equals("Totem")) {
               b = 11;
            }
            break;
         case 1973786418:
            if (string.equals("Snowflake")) {
               b = 10;
            }
      }

      byte c = b;
      switch (c) {
         case 0:
            return ParticleTypes.ENCHANTED_HIT;
         case 1:
            return ParticleTypes.ENCHANT;
         case 2:
            return ParticleTypes.FLAME;
         case 3:
            return ParticleTypes.SOUL_FIRE_FLAME;
         case 4:
            return ParticleTypes.HEART;
         case 5:
            return ParticleTypes.SMOKE;
         case 6:
            return ParticleTypes.LARGE_SMOKE;
         case 7:
            return ParticleTypes.CLOUD;
         case 8:
            return ParticleTypes.ELECTRIC_SPARK;
         case 9:
            return ParticleTypes.LAVA;
         case 10:
            return ParticleTypes.SNOWFLAKE;
         case 11:
            return ParticleTypes.TOTEM_OF_UNDYING;
         case 12:
            return ParticleTypes.NOTE;
         case 13:
            int j = i & 16777215;
            return new DustParticleOptions(j, 1.0F);
         default:
            return ParticleTypes.CRIT;
      }
   }
}
