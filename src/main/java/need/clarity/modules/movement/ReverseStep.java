package need.clarity.modules.movement;

import need.clarity.modules.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

public class ReverseStep extends ModuleBase {
   public static ReverseStep instance;

   public ReverseStep() {
      super("Reverse Step", "Movement");
      instance = this;
   }

   public static float getStepDownHeightSetting() {
      float f;
      if (instance == null) {
         f = 1.0F;
      } else {
         ReverseStep reverseStep = instance;
         Float float1 = 1.0F;
         f = reverseStep.getSetting("Step Down Height", float1);
      }

      return f;
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         if (minecraft.level != null) {
            LocalPlayer localPlayer = minecraft.player;
            if (localPlayer.onGround()) {
               double d = localPlayer.getDeltaMovement().x;
               double n = localPlayer.getDeltaMovement().x;
               double e = d * n;
               double o = localPlayer.getDeltaMovement().z;
               double u = localPlayer.getDeltaMovement().z;
               double p = o * u;
               if (!(e + p < 1.0E-6)) {
                  float q = getStepDownHeightSetting();
                  float h = Math.min(3.0F, q);
                  float f = Math.max(1.0F, h);
                  ClientLevel clientLevel = minecraft.level;
                  AABB aABB = localPlayer.getBoundingBox();
                  int i = 1;

                  while (true) {
                     int j = i;
                     int k = (int)f;
                     if (j > k) {
                        return;
                     }

                     double t = -i;
                     AABB aABB1 = aABB.move(0.0, t, 0.0);
                     if (!clientLevel.noBlockCollision(localPlayer, aABB1)) {
                        if (i == 1) {
                           return;
                        }

                        int m = i - 1;
                        methodA(localPlayer, m);
                        return;
                     }

                     double g = localPlayer.getX();
                     double r = localPlayer.getY();
                     double v = i;
                     double s = r - v - 0.1;
                     double w = localPlayer.getZ();
                     BlockPos blockPos = BlockPos.containing(g, s, w);
                     if (!clientLevel.getBlockState(blockPos).isAir()) {
                        int l = i;
                        methodA(localPlayer, l);
                        return;
                     }

                     i++;
                  }
               }
            }
         }
      }
   }

   private static void methodA(LocalPlayer localPlayer, int i) {
      double d = Math.floor(localPlayer.getY());
      double h = i;
      double e = d - h;
      double f = localPlayer.getX();
      double j = localPlayer.getZ();
      localPlayer.setPos(f, e, j);
      double g = localPlayer.getDeltaMovement().x;
      double k = localPlayer.getDeltaMovement().z;
      localPlayer.setDeltaMovement(g, 0.0, k);
      localPlayer.resetFallDistance();
   }
}
