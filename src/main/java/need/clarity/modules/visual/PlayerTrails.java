package need.clarity.modules.visual;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import need.clarity.WorldRenderEvent;
import need.clarity.WorldRenderEvent$Listener;
import need.clarity.config.ThemeColors;
import need.clarity.modules.ModuleBase;
import need.clarity.render.BoxRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.phys.Vec3;

public class PlayerTrails extends ModuleBase {
   private final Map<Integer, Deque<Vec3>> mapA;
   private final WorldRenderEvent$Listener listenerB;

   public PlayerTrails() {
      super("Player Trails", "Visual");
      HashMap hashMap = new HashMap();
      this.mapA = hashMap;
      WorldRenderEvent$Listener listener = this::methodA;
      this.listenerB = listener;
   }

   @Override
   public void onEnable() {
      WorldRenderEvent.register(this.listenerB);
   }

   @Override
   public void onDisable() {
      WorldRenderEvent.methodA(this.listenerB);
      this.mapA.clear();
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.level != null) {
         if (minecraft.player != null) {
            Integer integer2 = 20;
            int i = this.getSetting("Length", integer2);
            HashSet hashSet = new HashSet();

            for (AbstractClientPlayer abstractClientPlayer : minecraft.level.players()) {
               if (abstractClientPlayer.isAlive() && !abstractClientPlayer.isSpectator()) {
                  int j = abstractClientPlayer.getId();
                  Integer integer = j;
                  hashSet.add(integer);
                  Map map = this.mapA;
                  Integer integer1 = j;
                  Function function = integer3 -> new ArrayDeque();
                  Deque deque = (Deque)map.computeIfAbsent(integer1, function);
                  double d = abstractClientPlayer.getX();
                  double e = abstractClientPlayer.getY();
                  double f = abstractClientPlayer.getZ();
                  Vec3 vec3 = new Vec3(d, e, f);
                  deque.addFirst(vec3);

                  while (deque.size() > i) {
                     deque.removeLast();
                  }
               }
            }

            Set set = this.mapA.keySet();
            Predicate predicate = integer3 -> {
               boolean bl;
               if (!hashSet.contains(integer3)) {
                  bl = true;
               } else {
                  bl = false;
               }

               return bl;
            };
            set.removeIf(predicate);
         }
      }
   }

   private void methodA(PoseStack poseStack, float t) {
      if (this.isEnabled()) {
         if (!this.mapA.isEmpty()) {
            int k = ThemeColors.resolveRgb(this.getSetting("Colour", "#00AAFF"), 43775, false);
            Integer integer = 180;
            int l = this.getSetting("Alpha", integer);
            Float float1 = 2.0F;
            float f = this.getSetting("Width", float1);
            Boolean boolean1 = true;
            boolean bl = this.getSetting("Fade", boolean1);
            Boolean boolean2 = true;
            boolean bl1 = this.getSetting("Through Walls", boolean2);

            for (Deque deque : this.mapA.values()) {
               if (deque.size() >= 2) {
                  int m = deque.size() - 1;
                  int i = 0;
                  Vec3 vec3 = null;

                  for (Vec3 vec31 : deque) {
                     if (vec3 != null) {
                        int j = l;
                        if (bl) {
                           float g = l;
                           float q = i - 1;
                           float s = m;
                           float r = q / s;
                           float h = 1.0F - r;
                           j = Math.round(g * h);
                           if (j < 0) {
                              j = 0;
                           }
                        }

                        int n = (j & 0xFF) << 24;
                        int p = k & 16777215;
                        int o = n | p;
                        Vec3 vec32 = vec3;
                        BoxRenderer.methodA(poseStack, vec32, vec31, o, f, bl1);
                     }

                     vec3 = vec31;
                     i++;
                  }
               }
            }
         }
      }
   }
}
