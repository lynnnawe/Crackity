package need.clarity.modules.utility;

import java.util.Optional;
import need.clarity.modules.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GhostHand extends ModuleBase {
   private static GhostHand instance;

   public GhostHand() {
      super("Ghost Hand", "Utility");
      instance = this;
   }

   public static GhostHand getInstance() {
      return instance;
   }

   public void methodA(Minecraft minecraft) {
      if (minecraft.player != null) {
         if (minecraft.level != null) {
            if (minecraft.screen == null) {
               if (!this.methodB(minecraft)) {
                  double d = minecraft.player.blockInteractionRange();
                  Vec3 vec3 = minecraft.player.getEyePosition();
                  Vec3 vec32 = minecraft.player.getLookAngle().scale(d);
                  Vec3 vec31 = vec3.add(vec32);
                  BlockHitResult blockHitResult = this.methodA(minecraft, vec3, vec31);
                  if (blockHitResult != null) {
                     minecraft.hitResult = blockHitResult;
                  }
               }
            }
         }
      }
   }

   private BlockHitResult methodA(Minecraft minecraft, Vec3 vec3, Vec3 vec31) {
      double d = vec31.x;
      double s = vec3.x;
      double e = d - s;
      double f = vec31.y;
      double t = vec3.y;
      double g = f - t;
      double h = vec31.z;
      double u = vec3.z;
      double j = h - u;
      double k = e * e;
      double v = g * g;
      double l = k + v;
      double w = j * j;
      double m = Math.sqrt(l + w);
      if (m < 0.001) {
         return null;
      }

      int n = (int)Math.ceil(m / 0.1);
      BlockPos blockPos = null;

      for (int i = 1; i <= n; i++) {
         double o = i;
         double x = n;
         double p = o / x;
         double q = vec3.x;
         double y = e * p;
         double r = q + y;
         double z = vec3.y;
         double bb = g * p;
         double ba = z + bb;
         double bc = vec3.z;
         double be = j * p;
         double bd = bc + be;
         BlockPos blockPos1 = BlockPos.containing(r, ba, bd);
         BlockPos blockPos2 = blockPos;
         if (!blockPos1.equals(blockPos2)) {
            blockPos = blockPos1;
            BlockState blockState = minecraft.level.getBlockState(blockPos1);
            if (!blockState.isAir() && minecraft.level.getBlockEntity(blockPos1) instanceof MenuProvider) {
               Vec3 vec32 = this.methodA(minecraft, blockPos1, blockState, vec3, vec31);
               Direction direction = this.methodA(vec32, blockPos1);
               return new BlockHitResult(vec32, direction, blockPos1, false);
            }
         }
      }

      return null;
   }

   private boolean methodB(Minecraft minecraft) {
      if (minecraft.hitResult instanceof BlockHitResult blockHitResult) {
         Type type1 = blockHitResult.getType();
         Type type = Type.MISS;
         if (type1 == type) {
            return false;
         }

         ClientLevel clientLevel = minecraft.level;
         BlockPos blockPos = blockHitResult.getBlockPos();
         return clientLevel.getBlockEntity(blockPos) instanceof MenuProvider;
      } else {
         return false;
      }
   }

   private Vec3 methodA(Minecraft minecraft, BlockPos blockPos, BlockState blockState, Vec3 vec3, Vec3 vec31) {
      ClientLevel clientLevel = minecraft.level;
      VoxelShape voxelShape = blockState.getShape(clientLevel, blockPos);
      if (!voxelShape.isEmpty()) {
         Optional optional = voxelShape.bounds().move(blockPos).clip(vec3, vec31);
         if (optional.isPresent()) {
            return (Vec3)optional.get();
         }
      }

      double d = blockPos.getX() + 0.5;
      double e = blockPos.getY() + 0.5;
      double f = blockPos.getZ() + 0.5;
      return new Vec3(d, e, f);
   }

   private Direction methodA(Vec3 vec3, BlockPos blockPos) {
      double d = vec3.x;
      double m = blockPos.getX() + 0.5;
      double e = d - m;
      double f = vec3.y;
      double n = blockPos.getY() + 0.5;
      double g = f - n;
      double h = vec3.z;
      double o = blockPos.getZ() + 0.5;
      double i = h - o;
      double j = Math.abs(e);
      double k = Math.abs(g);
      double l = Math.abs(i);
      if (j >= k && j >= l) {
         Direction direction;
         if (e > 0.0) {
            direction = Direction.EAST;
         } else {
            direction = Direction.WEST;
         }

         return direction;
      } else if (k >= j && k >= l) {
         Direction direction1;
         if (g > 0.0) {
            direction1 = Direction.UP;
         } else {
            direction1 = Direction.DOWN;
         }

         return direction1;
      } else {
         Direction direction2;
         if (i > 0.0) {
            direction2 = Direction.SOUTH;
         } else {
            direction2 = Direction.NORTH;
         }

         return direction2;
      }
   }
}
