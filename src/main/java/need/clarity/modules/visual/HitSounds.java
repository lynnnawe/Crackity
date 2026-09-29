package need.clarity.modules.visual;

import java.util.Random;
import need.clarity.EventStartAttack;
import need.clarity.PerAttackEvents;
import need.clarity.modules.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class HitSounds extends ModuleBase {
   private final Random random;

   public HitSounds() {
      super("Hit Sounds", "Visual");
      Random randomx = new Random();
      this.random = randomx;
      PerAttackEvents.register(this::methodA);
   }

   private void methodA(EventStartAttack eventStartAttack) {
      if (this.isEnabled()) {
         Entity entity = eventStartAttack.getTarget();
         if (entity != null) {
            Boolean boolean_x = false;
            if (!this.getSetting("Only Players", boolean_x) || entity instanceof Player) {
               Boolean boolean_x = false;
               if (!this.getSetting("Only Crit", boolean_x) || this.methodA(entity)) {
                  Minecraft minecraft = Minecraft.getInstance();
                  if (minecraft.player != null) {
                     SoundEvent soundEvent = methodA(this.getSetting("Sound", "Attack Crit"));
                     if (soundEvent != null) {
                        Float float1 = 1.0F;
                        float h = this.getSetting("Volume", float1);
                        Float float2 = 1.0F;
                        float g = this.getSetting("Pitch", float2);
                        Float float3 = 0.0F;
                        float i = this.getSetting("Pitch Random", float3);
                        if (i > 0.0F) {
                           float j = g;
                           float k = (this.random.nextFloat() - 0.5F) * 2.0F * i;
                           g = j + k;
                           if (g < 0.5F) {
                              g = 0.5F;
                           }

                           if (g > 2.0F) {
                              g = 2.0F;
                           }
                        }

                        LocalPlayer localPlayer = minecraft.player;
                        float f = g;
                        localPlayer.playSound(soundEvent, h, f);
                     }
                  }
               }
            }
         }
      }
   }

   private boolean methodA(Entity entity) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player == null) {
         return false;
      }

      if (!(entity instanceof LivingEntity)) {
         return false;
      }

      boolean bl;
      if (minecraft.player.fallDistance > 0.0 && !minecraft.player.onGround() && !minecraft.player.isInWater() && !minecraft.player.isPassenger()) {
         bl = true;
      } else {
         bl = false;
      }

      return bl;
   }

   private static SoundEvent methodA(String string) {
      if (string == null) {
         return SoundEvents.PLAYER_ATTACK_CRIT;
      }

      byte b = -1;
      int i = string.hashCode();
      switch (i) {
         case -2067043081:
            if (string.equals("Level Up")) {
               b = 5;
            }
            break;
         case -828063980:
            if (string.equals("Attack Sweep")) {
               b = 2;
            }
            break;
         case 2066723:
            if (string.equals("Bell")) {
               b = 8;
            }
            break;
         case 97446767:
            if (string.equals("Attack Strong")) {
               b = 0;
            }
            break;
         case 175685887:
            if (string.equals("Anvil Land")) {
               b = 7;
            }
            break;
         case 632633315:
            if (string.equals("Attack Knockback")) {
               b = 3;
            }
            break;
         case 942638258:
            if (string.equals("Attack Crit")) {
               b = 9;
            }
            break;
         case 943221328:
            if (string.equals("Attack Weak")) {
               b = 1;
            }
            break;
         case 1486349085:
            if (string.equals("Orb Pickup")) {
               b = 4;
            }
            break;
         case 1540557928:
            if (string.equals("Totem Use")) {
               b = 6;
            }
      }

      byte c = b;
      switch (c) {
         case 0:
            return SoundEvents.PLAYER_ATTACK_STRONG;
         case 1:
            return SoundEvents.PLAYER_ATTACK_WEAK;
         case 2:
            return SoundEvents.PLAYER_ATTACK_SWEEP;
         case 3:
            return SoundEvents.PLAYER_ATTACK_KNOCKBACK;
         case 4:
            return SoundEvents.EXPERIENCE_ORB_PICKUP;
         case 5:
            return SoundEvents.PLAYER_LEVELUP;
         case 6:
            return SoundEvents.TOTEM_USE;
         case 7:
            return SoundEvents.ANVIL_LAND;
         case 8:
            return SoundEvents.BELL_BLOCK;
         default:
            return SoundEvents.PLAYER_ATTACK_CRIT;
      }
   }
}
