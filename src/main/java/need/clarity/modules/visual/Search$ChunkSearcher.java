package need.clarity.modules.visual;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.AABB;

final class Search$ChunkSearcher {
   private final ChunkAccess chunkAccessA;
   private final ChunkAccess chunkAccessB;
   private final ChunkAccess chunkAccessC;
   private final ChunkAccess chunkAccessD;
   private final ChunkAccess chunkAccessE;
   private final Set<Block> setF;
   private final boolean flagG;
   private volatile List<Search$ChunkSearcher$Hit> listH;
   private CompletableFuture<Void> completableFutureI;
   private volatile boolean flagJ;

   Search$ChunkSearcher(
      ChunkAccess chunkAccess,
      ChunkAccess chunkAccess1,
      ChunkAccess chunkAccess2,
      ChunkAccess chunkAccess3,
      ChunkAccess chunkAccess4,
      Set<Block> set,
      boolean bl
   ) {
      List list = Collections.emptyList();
      this.listH = list;
      this.completableFutureI = null;
      this.flagJ = false;
      this.chunkAccessA = chunkAccess;
      this.chunkAccessB = chunkAccess1;
      this.chunkAccessC = chunkAccess2;
      this.chunkAccessD = chunkAccess3;
      this.chunkAccessE = chunkAccess4;
      this.setF = set;
      this.flagG = bl;
   }

   void methodA(ExecutorService executorService) {
      CompletableFuture completableFuture = CompletableFuture.runAsync(this::methodC, executorService);
      this.completableFutureI = completableFuture;
   }

   void methodA() {
      this.flagJ = true;
      if (this.completableFutureI != null) {
         this.completableFutureI.cancel(false);
      }
   }

   List<Search$ChunkSearcher$Hit> getListH() {
      return this.listH;
   }

   private void methodC() {
      java.util.ArrayList arrayList = new java.util.ArrayList();
      MutableBlockPos mutableBlockPos = new MutableBlockPos();
      int m = this.chunkAccessA.getPos().getMinBlockX();
      int n = this.chunkAccessA.getPos().getMinBlockZ();
      int o = this.chunkAccessA.getMinY();
      int p = this.chunkAccessA.getMaxY();
      int j = m;

      while (true) {
         int q = j;
         int s = m + 16;
         if (q >= s || this.flagJ) {
            if (!this.flagJ) {
               this.listH = arrayList;
               return;
            } else {
               return;
            }
         }

         int k = n;

         while (true) {
            int r = k;
            int t = n + 16;
            if (r >= t || this.flagJ) {
               j++;
               break;
            }

            for (int l = o; l < p && !this.flagJ; l++) {
               int u = j;
               int w = l;
               int i = k;
               mutableBlockPos.set(u, w, i);
               BlockState blockState = this.chunkAccessA.getBlockState(mutableBlockPos);
               Set set = this.setF;
               Block block = blockState.getBlock();
               if (set.contains(block)) {
                  if (this.flagG) {
                     int v = j;
                     int x = l;
                     int y = k;
                     if (!this.methodA(v, x, y, m, n, o, p)) {
                        continue;
                     }
                  }

                  double d = j;
                  double e = l;
                  double f = k;
                  double g = j + 1;
                  double h = l + 1;
                  double z = k + 1;
                  AABB aABB = new AABB(d, e, f, g, h, z);
                  Block block1 = blockState.getBlock();
                  Search$ChunkSearcher$Hit hit = new Search$ChunkSearcher$Hit(aABB, block1);
                  arrayList.add(hit);
               }
            }

            k++;
         }
      }
   }

   private boolean methodA(int i, int j, int k, int l, int m, int n, int o) {
      if (j + 1 < o) {
         ChunkAccess chunkAccess = this.chunkAccessA;
         int x = j + 1;
         if (methodA(chunkAccess, i, x, k)) {
            return true;
         }
      }

      if (j - 1 >= n) {
         ChunkAccess chunkAccess1 = this.chunkAccessA;
         int y = j - 1;
         if (methodA(chunkAccess1, i, y, k)) {
            return true;
         }
      }

      if (j + 1 < o && j - 1 >= n) {
         if (k - 1 >= m) {
            ChunkAccess chunkAccess2 = this.chunkAccessA;
            int z = k - 1;
            if (methodA(chunkAccess2, i, j, z)) {
               return true;
            }
         } else {
            if (this.chunkAccessB == null) {
               return true;
            }

            ChunkAccess chunkAccess9 = this.chunkAccessB;
            int bc = k - 1;
            if (methodA(chunkAccess9, i, j, bc)) {
               return true;
            }
         }

         int p = k + 1;
         int r = m + 16;
         if (p < r) {
            ChunkAccess chunkAccess3 = this.chunkAccessA;
            int ba = k + 1;
            if (methodA(chunkAccess3, i, j, ba)) {
               return true;
            }
         } else {
            if (this.chunkAccessC == null) {
               return true;
            }

            ChunkAccess chunkAccess8 = this.chunkAccessC;
            int bb = k + 1;
            if (methodA(chunkAccess8, i, j, bb)) {
               return true;
            }
         }

         if (i - 1 >= l) {
            ChunkAccess chunkAccess4 = this.chunkAccessA;
            int s = i - 1;
            if (methodA(chunkAccess4, s, j, k)) {
               return true;
            }
         } else {
            if (this.chunkAccessD == null) {
               return true;
            }

            ChunkAccess chunkAccess7 = this.chunkAccessD;
            int w = i - 1;
            if (methodA(chunkAccess7, w, j, k)) {
               return true;
            }
         }

         int q = i + 1;
         int t = l + 16;
         if (q < t) {
            ChunkAccess chunkAccess6 = this.chunkAccessA;
            int v = i + 1;
            if (methodA(chunkAccess6, v, j, k)) {
               return true;
            }
         } else {
            if (this.chunkAccessE == null) {
               return true;
            }

            ChunkAccess chunkAccess5 = this.chunkAccessE;
            int u = i + 1;
            if (methodA(chunkAccess5, u, j, k)) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private static boolean methodA(ChunkAccess chunkAccess, int i, int j, int k) {
      try {
         BlockPos blockPos = new BlockPos(i, j, k);
         return chunkAccess.getBlockState(blockPos).isAir();
      } catch (Exception exception) {
         return true;
      }
   }
}
