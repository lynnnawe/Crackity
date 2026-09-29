package need.clarity.modules.utility;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Stream;
import need.clarity.modules.ModuleBase;
import need.clarity.modules.combat.AntiBot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

public class Targets extends ModuleBase {
   private static Targets instance;
   private static final Map<LivingEntity, Long> mapB;

   public Targets() {
      super("Targets", "Utility");
      instance = this;
   }

   @Override
   public boolean isEnabled() {
      return true;
   }

   public static boolean methodA(LivingEntity livingEntity, Player player) {
      if (livingEntity == player) {
         return false;
      } else if (livingEntity.isRemoved()) {
         return false;
      } else if (AntiBot.methodA(livingEntity)) {
         return false;
      } else {
         return methodA("Don't Target Dead", true) && livingEntity.getHealth() <= 0.0F ? false : methodA(livingEntity);
      }
   }

   public static boolean methodA(LivingEntity livingEntity) {
      return livingEntity instanceof Player;
   }

   public static boolean methodA(LivingEntity livingEntity, int i) {
      if (livingEntity instanceof Player && (i & 1) != 0) {
         return true;
      } else {
         return livingEntity instanceof Monster && (i & 2) != 0
            ? true
            : !(livingEntity instanceof Player)
               && !(livingEntity instanceof Monster)
               && (livingEntity instanceof Animal || livingEntity instanceof AmbientCreature || livingEntity instanceof Mob)
               && (i & 4) != 0;
      }
   }

   public static LivingEntity methodA(List<? extends LivingEntity> list, Player player) {
      if (list != null && !list.isEmpty()) {
         boolean bl = methodA("Don't Target Dead", true);
         ArrayList arrayList = new ArrayList();

         for (LivingEntity livingEntity : list) {
            if (!bl || !(livingEntity.getHealth() <= 0.0F)) {
               arrayList.add(livingEntity);
            }
         }

         if (arrayList.isEmpty()) {
            return null;
         }

         String string = methodA("Mode", "Nearest");
         byte b = -1;
         int i = string.hashCode();
         switch (i) {
            case -1396350199:
               if (string.equals("Last Hit")) {
                  b = 1;
               }
               break;
            case 1490826774:
               if (string.equals("Lowest HP")) {
                  b = 0;
               }
         }

         byte c = b;
         LivingEntity livingEntity1;
         switch (c) {
            case 0:
               Stream stream2 = arrayList.stream();
               Comparator comparator2 = Comparator.comparingDouble(LivingEntity::getHealth);
               livingEntity1 = (LivingEntity)stream2.min(comparator2).orElse(null);
               break;
            case 1:
               Stream stream1 = arrayList.stream();
               Comparator comparator1 = Comparator.comparingLong(livingEntity2 -> {
                  Map map = mapB;
                  Long long_ = 0L;
                  return map.getOrDefault(livingEntity2, long_);
               });
               livingEntity1 = (LivingEntity)stream1.max(comparator1).orElse(null);
               break;
            default:
               Stream stream = arrayList.stream();
               Comparator comparator = Comparator.comparingDouble(livingEntity2 -> livingEntity2.distanceToSqr(player));
               livingEntity1 = (LivingEntity)stream.min(comparator).orElse(null);
         }

         return livingEntity1;
      } else {
         return null;
      }
   }

   private static String methodA(String string, String string1) {
      return instance == null ? string1 : instance.getSetting(string, string1);
   }

   private static boolean methodA(String string, boolean bl) {
      if (instance == null) {
         return bl;
      }

      Targets targets = instance;
      Boolean boolean1 = bl;
      return targets.getSetting(string, boolean1);
   }

   static {
      WeakHashMap weakHashMap = new WeakHashMap();
      mapB = weakHashMap;
   }
}
