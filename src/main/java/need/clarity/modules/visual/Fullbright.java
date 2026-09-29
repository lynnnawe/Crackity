package need.clarity.modules.visual;

import java.lang.reflect.Field;
import need.clarity.modules.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class Fullbright extends ModuleBase {
   private double doubleA = 1.0;
   private static Field fieldB;

   public Fullbright() {
      super("Fullbright", "Visual");
   }

   @Override
   public void onEnable() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         if (this.methodA(minecraft)) {
            if (minecraft.options != null) {
               double d = (Double)minecraft.options.gamma().get();
               this.doubleA = d;
               methodA(minecraft, 16.0);
            }
         } else {
            methodB(minecraft);
         }
      }
   }

   @Override
   public void onDisable() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         if (minecraft.options != null) {
            double d = this.doubleA;
            methodA(minecraft, d);
         }

         methodC(minecraft);
      }
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         if (this.methodA(minecraft)) {
            methodC(minecraft);
            if (minecraft.options != null) {
               methodA(minecraft, 16.0);
            }
         } else {
            if (minecraft.options != null && (Double)minecraft.options.gamma().get() > 1.0) {
               double d = this.doubleA;
               methodA(minecraft, d);
            }

            methodB(minecraft);
         }
      }
   }

   private boolean methodA(Minecraft minecraft) {
      Object object = this.getSetting("Mode", "Gamma");
      if (object != null) {
         String string = object.toString();
         if (!"Gamma".equals(string)) {
            return false;
         }
      }

      return true;
   }

   private static void methodB(Minecraft minecraft) {
      if (minecraft.player != null) {
         Holder holder = MobEffects.NIGHT_VISION;
         MobEffectInstance mobEffectInstance = minecraft.player.getEffect(holder);
         if (mobEffectInstance == null || mobEffectInstance.getDuration() < 40) {
            LocalPlayer localPlayer = minecraft.player;
            MobEffectInstance mobEffectInstance1 = new MobEffectInstance(holder, Integer.MAX_VALUE, 0, false, false, false);
            localPlayer.addEffect(mobEffectInstance1);
         }
      }
   }

   private static void methodC(Minecraft minecraft) {
      if (minecraft.player != null) {
         LocalPlayer localPlayer = minecraft.player;
         Holder holder = MobEffects.NIGHT_VISION;
         localPlayer.removeEffect(holder);
      }
   }

   private static void methodA(Minecraft minecraft, double d) {
      if (minecraft.options != null) {
         if (fieldB == null) {
            OptionInstance optionInstance = minecraft.options.gamma();
            Double double1 = Math.min(d, 1.0);
            optionInstance.set(double1);
         } else {
            try {
               Field field = fieldB;
               OptionInstance optionInstance1 = minecraft.options.gamma();
               Double double2 = d;
               field.set(optionInstance1, double2);
            } catch (IllegalAccessException illegalAccessException) {
            }
         }
      }
   }

   static {
      try {
         fieldB = OptionInstance.class.getDeclaredField("value");
         fieldB.setAccessible(true);
      } catch (NoSuchFieldException noSuchFieldException1) {
         try {
            fieldB = OptionInstance.class.getDeclaredField("f_231548_");
            fieldB.setAccessible(true);
         } catch (NoSuchFieldException noSuchFieldException) {
         }
      }
   }
}
