package need.clarity.modules.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import need.clarity.modules.ModuleBase;
import need.clarity.modules.utility.Targets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public class Piercing extends ModuleBase {
   private static Piercing instance;

   public Piercing() {
      super("Piercing", "Combat");
      instance = this;
   }

   public void methodA(Minecraft minecraft) {
      if (minecraft.player != null) {
         if (minecraft.level != null) {
            if (minecraft.hitResult != null) {
               Piercing$Trace trace = this.methodB(minecraft);
               if (minecraft.options.keyAttack.isDown()) {
                  Piercing$TargetHit targetHit = this.methodB(minecraft, trace);
                  if (targetHit != null) {
                     this.methodA(minecraft, targetHit);
                  }
               } else {
                  Boolean boolean_ = false;
                  if (this.getSetting("Interact", boolean_) && minecraft.options.keyUse.isDown() && this.methodE(minecraft) != null) {
                     BlockHitResult blockHitResult = this.methodD(minecraft);
                     Type type = blockHitResult.getType();
                     Type type1 = Type.BLOCK;
                     if (type == type1) {
                        minecraft.hitResult = blockHitResult;
                        return;
                     }
                  }

                  this.methodD(minecraft, trace);
               }
            }
         }
      }
   }

   private Piercing$Trace methodB(Minecraft minecraft) {
      LocalPlayer localPlayer = minecraft.player;
      float f = minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
      Vec3 vec3 = localPlayer.getEyePosition(f);
      double d = this.methodC(minecraft);
      Vec3 vec32 = localPlayer.getViewVector(f).scale(d);
      Vec3 vec31 = vec3.add(vec32);
      return new Piercing$Trace(localPlayer, vec3, vec31);
   }

   private double methodC(Minecraft minecraft) {
      LocalPlayer localPlayer = minecraft.player;
      double d = this.methodA(localPlayer);
      double e = localPlayer.blockInteractionRange();
      return Math.max(d, e);
   }

   private double methodA(Player player) {
      try {
         Holder holder = Attributes.ENTITY_INTERACTION_RANGE;
         AttributeInstance attributeInstance = player.getAttributes().getInstance(holder);
         if (attributeInstance != null) {
            return attributeInstance.getValue();
         }
      } catch (Throwable throwable) {
      }

      return 3.0;
   }

   private double methodA(Minecraft minecraft, Piercing$Trace trace) {
      LocalPlayer localPlayer = minecraft.player;
      double d = this.methodA(localPlayer);
      double e = d * d;
      Boolean boolean_ = true;
      if (this.getSetting("Blocks", boolean_)) {
         return e;
      }

      BlockHitResult blockHitResult = this.methodC(minecraft, trace);
      Type type = blockHitResult.getType();
      Type type1 = Type.MISS;
      if (type == type1) {
         return e;
      }

      Vec3 vec3 = trace.getVec3B();
      Vec3 vec31 = blockHitResult.getLocation();
      return Math.min(vec3.distanceToSqr(vec31), e);
   }

   private Piercing$TargetHit methodB(Minecraft minecraft, Piercing$Trace trace) {
      double d = this.methodA(minecraft, trace);
      List list = this.methodA(minecraft, trace, d);
      return list.isEmpty() ? null : this.methodA(list);
   }

   private List<Piercing$TargetHit> methodA(Minecraft minecraft, Piercing$Trace trace, double d) {
      ArrayList arrayList = new ArrayList();
      int i = this.getPlayersSetting();
      AABB aABB = minecraft.player.getBoundingBox();
      Vec3 vec31 = trace.getVec3C();
      Vec3 vec34 = trace.getVec3B();
      Vec3 vec32 = vec31.subtract(vec34);
      AABB aABB1 = aABB.expandTowards(vec32).inflate(1.0, 1.0, 1.0);
      ClientLevel clientLevel = minecraft.level;
      LocalPlayer localPlayer = minecraft.player;
      Predicate predicate = EntitySelector.CAN_BE_PICKED;

      for (Entity entity : clientLevel.getEntities(localPlayer, aABB1, predicate)) {
         if (entity instanceof LivingEntity livingEntity && Targets.methodA(livingEntity, i)) {
            LocalPlayer localPlayer1 = minecraft.player;
            if (Targets.methodA(livingEntity, localPlayer1)) {
               AABB aABB2 = entity.getBoundingBox();
               double e = entity.getPickRadius();
               AABB aABB3 = aABB2.inflate(e);
               Vec3 vec33 = trace.getVec3B();
               Vec3 vec35 = trace.getVec3C();
               Optional optional = aABB3.clip(vec33, vec35);
               if (!optional.isEmpty()) {
                  Vec3 vec3 = (Vec3)optional.get();
                  if (!(trace.getVec3B().distanceToSqr(vec3) > d)) {
                     Piercing$TargetHit targetHit = new Piercing$TargetHit(livingEntity, vec3);
                     arrayList.add(targetHit);
                  }
               }
            }
         }
      }

      return arrayList;
   }

   private Piercing$TargetHit methodA(List<Piercing$TargetHit> list) {
      if (list.size() == 1) {
         return (Piercing$TargetHit)list.get(0);
      }

      String string = this.getSetting("Sort Targets By", "Hurt Time");
      if ("Health".equals(string)) {
         Comparator comparator1 = Comparator.comparingDouble(targetHit -> targetHit.getLivingEntityA().getHealth());
         list.sort(comparator1);
      } else {
         Comparator comparator = Comparator.comparingInt(targetHit -> targetHit.getLivingEntityA().hurtTime);
         list.sort(comparator);
      }

      return (Piercing$TargetHit)list.get(0);
   }

   private int getPlayersSetting() {
      byte b = 0;
      Boolean boolean_ = true;
      if (this.getSetting("Players", boolean_)) {
         b = 1;
      }

      Boolean boolean_x = false;
      if (this.getSetting("Hostile Mobs", boolean_x)) {
         b |= 2;
      }

      Boolean boolean_xx = false;
      if (this.getSetting("Passive Mobs", boolean_xx)) {
         b |= 4;
      }

      if (b == 0) {
         b = 1;
      }

      return b;
   }

   private BlockHitResult methodC(Minecraft minecraft, Piercing$Trace trace) {
      ClientLevel clientLevel = minecraft.level;
      Vec3 vec3 = trace.getVec3B();
      Vec3 vec31 = trace.getVec3C();
      Block block = Block.OUTLINE;
      Fluid fluid = Fluid.NONE;
      LocalPlayer localPlayer = minecraft.player;
      ClipContext clipContext = new ClipContext(vec3, vec31, block, fluid, localPlayer);
      return clientLevel.clip(clipContext);
   }

   private BlockHitResult methodD(Minecraft minecraft) {
      Vec3 vec3 = minecraft.player.getEyePosition();
      double d = Math.toRadians(minecraft.player.getYRot() * -1.0);
      double e = Math.toRadians(minecraft.player.getXRot() * -1.0);
      double g = Math.sin(d);
      double i = Math.cos(e);
      double h = g * i;
      double j = Math.sin(e);
      double k = Math.cos(d);
      double m = Math.cos(e);
      double l = k * m;
      Vec3 vec31 = new Vec3(h, j, l);
      double f = minecraft.player.blockInteractionRange();
      Vec3 vec33 = vec31.scale(f);
      Vec3 vec32 = vec3.add(vec33);
      LocalPlayer localPlayer = minecraft.player;
      Piercing$Trace trace = new Piercing$Trace(localPlayer, vec3, vec32);
      return this.methodC(minecraft, trace);
   }

   private void methodA(Minecraft minecraft, Piercing$TargetHit targetHit) {
      LivingEntity livingEntity1 = targetHit.getLivingEntityA();
      Vec3 vec3 = targetHit.getVec3B();
      EntityHitResult entityHitResult = new EntityHitResult(livingEntity1, vec3);
      minecraft.hitResult = entityHitResult;
      LivingEntity livingEntity = targetHit.getLivingEntityA();
      minecraft.crosshairPickEntity = livingEntity;
   }

   private void methodD(Minecraft minecraft, Piercing$Trace trace) {
      if (minecraft.hitResult instanceof EntityHitResult entityHitResult) {
         if (entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
            LocalPlayer localPlayer = minecraft.player;
            if (Targets.methodA(livingEntity, localPlayer)) {
               return;
            }
         }

         BlockHitResult blockHitResult = this.methodC(minecraft, trace);
         double d = minecraft.player.blockInteractionRange();
         double f = minecraft.player.blockInteractionRange();
         double e = d * f;
         Type type = blockHitResult.getType();
         Type type1 = Type.MISS;
         if (type != type1) {
            Vec3 vec3 = blockHitResult.getLocation();
            Vec3 vec31 = trace.getVec3B();
            if (vec3.distanceToSqr(vec31) <= e) {
               minecraft.hitResult = blockHitResult;
            }
         }

         minecraft.crosshairPickEntity = null;
      }
   }

   private InteractionHand methodE(Minecraft minecraft) {
      if (minecraft.player.getMainHandItem().getItem() instanceof BlockItem) {
         return InteractionHand.MAIN_HAND;
      } else {
         return minecraft.player.getOffhandItem().getItem() instanceof BlockItem ? InteractionHand.OFF_HAND : null;
      }
   }
}
