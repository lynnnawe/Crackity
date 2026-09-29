package need.clarity.modules.visual;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import need.clarity.WorldRenderEvent;
import need.clarity.WorldRenderEvent$Listener;
import need.clarity.config.ClarityConfig;
import need.clarity.config.ThemeColors;
import need.clarity.modules.ModuleBase;
import need.clarity.render.BoxRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class Tracers extends ModuleBase {
   private static Tracers instance;
   private final WorldRenderEvent$Listener listenerB;

   public Tracers() {
      super("Tracers", "Visual");
      WorldRenderEvent$Listener listener = this::methodA;
      this.listenerB = listener;
      instance = this;
   }

   @Override
   public void onEnable() {
      WorldRenderEvent.register(this.listenerB);
   }

   @Override
   public void onDisable() {
      WorldRenderEvent.methodA(this.listenerB);
   }

   private void methodA(PoseStack poseStack, float f) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.level != null) {
         if (minecraft.player != null) {
            Integer integer = 200;
            int i = this.getSetting("Alpha", integer);
            int j = methodA(this.getSetting("Player Colour", "#ff4444"), i, false);
            int k = methodA(this.getSetting("Mob Colour", "#44ff44"), i, true);
            int l = methodA(this.getSetting("Tracer Colour", "#ffb52e"), i, false);
            Float float1 = 2.0F;
            float g = this.getSetting("Thickness", float1);
            boolean bl = this.methodA("Players");
            boolean bl1 = this.methodA("Mobs");
            boolean bl2 = this.methodA("Entities");
            java.util.ArrayList arrayList = new java.util.ArrayList();
            java.util.ArrayList arrayList1 = new java.util.ArrayList();
            java.util.ArrayList arrayList2 = new java.util.ArrayList();

            for (Entity entity : minecraft.level.entitiesForRendering()) {
               LocalPlayer localPlayer = minecraft.player;
               if (entity != localPlayer && entity.isAlive()) {
                  double d = entity.xOld;
                  double h = entity.getX();
                  double t = entity.xOld;
                  double o = h - t;
                  double u = f;
                  double p = o * u;
                  double e = d + p;
                  double q = entity.yOld;
                  double v = entity.getY();
                  double bb = entity.yOld;
                  double w = v - bb;
                  double bc = f;
                  double x = w * bc;
                  double r = q + x;
                  double y = entity.getBbHeight() * 0.5;
                  double s = r + y;
                  double z = entity.zOld;
                  double bd = entity.getZ();
                  double bg = entity.zOld;
                  double be = bd - bg;
                  double bh = f;
                  double bf = be * bh;
                  double ba = z + bf;
                  Vec3 vec3 = new Vec3(e, s, ba);
                  if (bl && entity instanceof Player) {
                     arrayList.add(vec3);
                  } else if (bl1 && entity instanceof Mob) {
                     arrayList1.add(vec3);
                  } else if (bl2 && !(entity instanceof LivingEntity)) {
                     arrayList2.add(vec3);
                  }
               }
            }

            if (!arrayList.isEmpty()) {
               BoxRenderer.methodA(poseStack, f, arrayList, j, g);
            }

            if (!arrayList1.isEmpty()) {
               BoxRenderer.methodA(poseStack, f, arrayList1, k, g);
            }

            if (!arrayList2.isEmpty()) {
               int n = i / 2;
               int m = methodA(l, n);
               BoxRenderer.methodA(poseStack, f, arrayList2, m, g);
            }
         }
      }
   }

   private boolean methodA(String string) {
      try {
         JsonObject jsonObject = ClarityConfig.getCurrent().getAsJsonObject("Tracers");
         if (jsonObject == null) {
            return true;
         }

         JsonObject jsonObject1 = jsonObject.getAsJsonObject("values");
         if (jsonObject1 == null) {
            return true;
         }

         JsonElement jsonElement = jsonObject1.get("Tracer Mode");
         if (jsonElement != null && jsonElement.isJsonArray()) {
            for (JsonElement jsonElement1 : jsonElement.getAsJsonArray()) {
               if (jsonElement1.isJsonPrimitive() && jsonElement1.getAsString().equals(string)) {
                  return true;
               }
            }

            return false;
         } else {
            return true;
         }
      } catch (Exception exception) {
         return true;
      }
   }

   private static int methodA(String string, int i, boolean bl) {
      int j = ThemeColors.resolveRgb(string, 16758062, bl);
      int k = (i & 0xFF) << 24;
      int l = j & 16777215;
      return k | l;
   }

   private static int methodA(int i, int j) {
      int k = i & 16777215;
      int l = (j & 0xFF) << 24;
      return k | l;
   }
}
