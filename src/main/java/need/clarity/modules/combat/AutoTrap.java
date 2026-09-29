package need.clarity.modules.combat;

import com.mojang.blaze3d.vertex.PoseStack;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import need.clarity.WorldRenderEvent;
import need.clarity.config.ThemeColors;
import need.clarity.modules.ModuleBase;
import need.clarity.modules.utility.Targets;
import need.clarity.render.BoxRenderer;
import need.clarity.util.InventoryAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public class AutoTrap extends ModuleBase {
   private static AutoTrap instance;
   private BlockPos blockPosB = null;
   private int intC = -1;
   private boolean flagD = false;
   private int intE = 0;
   private int intF = -1;
   private boolean flagG = false;
   private final List<AutoTrap$RenderEntry> listH;
   private static final Field fieldI;
   private int intJ;

   public AutoTrap() {
      super("Auto Trap", "Combat");
      CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
      this.listH = copyOnWriteArrayList;
      this.intJ = -1;
      instance = this;
      WorldRenderEvent.register(this::methodA);
   }

   @Override
   public void onDisable() {
      Minecraft minecraft = Minecraft.getInstance();
      this.methodC(minecraft);
      AimAssist.methodE();
      this.methodA();
   }

   private void methodA() {
      this.blockPosB = null;
      this.intC = -1;
      this.intE = 0;
      this.flagD = false;
      this.flagG = false;
      this.intJ = -1;
      this.listH.clear();
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         if (minecraft.level != null) {
            if (minecraft.screen == null) {
               int i = (int)minecraft.level.getGameTime();
               if (this.flagG) {
                  if (this.methodB(minecraft)) {
                     this.intF = -1;
                     this.flagG = false;
                     AimAssist.methodE();
                  } else {
                     int j = this.intE;
                     if (i >= j) {
                        this.methodC(minecraft);
                        AimAssist.methodE();
                        this.flagG = false;
                     }
                  }
               } else {
                  int k = this.intE;
                  if (i >= k) {
                     Player player = methodA(minecraft);
                     if (player == null) {
                        this.methodD(minecraft);
                     } else {
                        Boolean boolean1 = false;
                        boolean bl = this.getSetting("Block Whitelist", boolean1);
                        Set set1;
                        if (bl) {
                           set1 = this.getWhitelistBlocksSetting();
                        } else {
                           set1 = Collections.emptySet();
                        }

                        Set set = set1;
                        Boolean boolean2 = false;
                        boolean bl1 = this.getSetting("Piercing", boolean2);
                        List list = this.methodA(minecraft, player, bl, set, bl1);
                        if (list.isEmpty()) {
                           if (methodA(minecraft, player)) {
                              if (this.intF != -1 && !this.flagG) {
                                 this.methodA(minecraft, i);
                              }

                              this.blockPosB = null;
                              this.intC = -1;
                           }
                        } else {
                           String string = this.getSetting("Mode", "Instant");
                           if ("Legit".equals(string)) {
                              this.methodB(minecraft, list, i);
                           } else {
                              this.methodA(minecraft, list, i);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void methodA(Minecraft minecraft, List<AutoTrap$PlacementJob> list, int i) {
      for (AutoTrap$PlacementJob placementJob : list) {
         int j = placementJob.getIntC();
         this.methodB(minecraft, j);
         this.methodA(minecraft, placementJob);
      }

      this.methodA(minecraft, i);
      this.blockPosB = null;
      this.flagD = false;
   }

   private void methodB(Minecraft minecraft, List<AutoTrap$PlacementJob> list, int i) {
      if (this.intF != -1 && this.methodB(minecraft)) {
         this.intF = -1;
         this.flagD = false;
      }

      AutoTrap$PlacementJob placementJob = null;
      if (this.blockPosB != null) {
         for (AutoTrap$PlacementJob placementJob1 : list) {
            BlockPos blockPos = placementJob1.getBlockPosA();
            BlockPos blockPos1 = this.blockPosB;
            if (blockPos.equals(blockPos1)) {
               placementJob = placementJob1;
               break;
            }
         }

         if (placementJob != null) {
            if (this.intC >= 0) {
               int o = this.intC;
               if (i - o > 4) {
                  placementJob = null;
                  this.blockPosB = null;
                  this.intC = -1;
                  this.flagD = false;
                  if (!list.isEmpty()) {
                     Iterator iterator = list.iterator();
                     if (iterator.hasNext()) {
                        placementJob = (AutoTrap$PlacementJob)iterator.next();
                     }
                  }

                  if (placementJob == null) {
                     return;
                  }
               }
            }
         } else {
            this.flagD = false;
            this.intC = -1;
         }
      }

      if (placementJob == null) {
         placementJob = (AutoTrap$PlacementJob)list.get(0);
         this.flagD = false;
         this.intC = -1;
      }

      BlockPos blockPos2 = placementJob.getBlockPosA();
      this.blockPosB = blockPos2;
      Vec3 vec3 = minecraft.player.getEyePosition();
      Vec3 vec31 = placementJob.getBlockHitResultB().getLocation();
      float[] f = methodA(vec3, vec31);
      float g = f[0];
      float h = f[1];
      AimAssist.methodC(g, h);
      int p = placementJob.getIntC();
      this.methodB(minecraft, p);
      if (!this.flagD) {
         Integer integer = 0;
         int j = this.getSetting("Swap Delay_lo", integer);
         Integer integer1 = 0;
         int l = this.getSetting("Swap Delay_hi", integer1);
         int k = methodA(methodA(j, l));
         this.flagD = true;
         if (k > 0) {
            int m = i + k;
            this.intE = m;
            return;
         }
      }

      AutoTrap$PlacementJob placementJob2 = placementJob;
      this.methodA(minecraft, placementJob2);
      this.intC = i;
      int n = i + 1;
      this.intE = n;
      this.flagD = false;
   }

   private void methodA(Minecraft minecraft, int i) {
      if (this.intF != -1) {
         Boolean boolean_ = true;
         if (!this.getSetting("Swap Back", boolean_)) {
            this.intF = -1;
         } else {
            Integer integer = 50;
            int j = this.getSetting("Swap Back Delay_lo", integer);
            Integer integer1 = 150;
            int l = this.getSetting("Swap Back Delay_hi", integer1);
            int m = methodA(methodA(j, l));
            int k = Math.max(1, m);
            int n = i + k;
            this.intE = n;
            this.flagG = true;
         }
      }
   }

   private static boolean methodA(Minecraft minecraft, Player player) {
      for (BlockPos blockPos : methodA(player)) {
         if (minecraft.level.getBlockState(blockPos).canBeReplaced()) {
            return false;
         }
      }

      return true;
   }

   private List<AutoTrap$PlacementJob> methodA(Minecraft minecraft, Player player, boolean bl, Set<String> set, boolean bl1) {
      ArrayList arrayList = new ArrayList();
      HashSet hashSet = new HashSet();

      for (BlockPos blockPos : methodA(player)) {
         if (minecraft.level.getBlockState(blockPos).canBeReplaced()) {
            int i = methodA(minecraft, bl, set);
            if (i != -1) {
               BlockHitResult blockHitResult = methodA(minecraft, blockPos, hashSet, bl1);
               if (blockHitResult != null) {
                  AutoTrap$PlacementJob placementJob = new AutoTrap$PlacementJob(blockPos, blockHitResult, i);
                  arrayList.add(placementJob);
                  hashSet.add(blockPos);
               }
            }
         }
      }

      return arrayList;
   }

   private static List<BlockPos> methodA(Player player) {
      double d = player.getX();
      double e = player.getY();
      double f = player.getZ();
      BlockPos blockPos = BlockPos.containing(d, e, f);
      BlockPos[] blockPos1 = new BlockPos[11];
      BlockPos blockPos2 = blockPos.below();
      blockPos1[0] = blockPos2;
      BlockPos blockPos3 = blockPos.north();
      blockPos1[1] = blockPos3;
      BlockPos blockPos4 = blockPos.south();
      blockPos1[2] = blockPos4;
      BlockPos blockPos5 = blockPos.east();
      blockPos1[3] = blockPos5;
      BlockPos blockPos6 = blockPos.west();
      blockPos1[4] = blockPos6;
      BlockPos blockPos7 = blockPos.north().above();
      blockPos1[5] = blockPos7;
      BlockPos blockPos8 = blockPos.south().above();
      blockPos1[6] = blockPos8;
      BlockPos blockPos9 = blockPos.east().above();
      blockPos1[7] = blockPos9;
      BlockPos blockPos10 = blockPos.west().above();
      blockPos1[8] = blockPos10;
      BlockPos blockPos11 = blockPos.above(1);
      blockPos1[9] = blockPos11;
      BlockPos blockPos12 = blockPos.above(2);
      blockPos1[10] = blockPos12;
      return List.of(blockPos1);
   }

   private static BlockHitResult methodA(Minecraft minecraft, BlockPos blockPos, Set<BlockPos> set, boolean bl) {
      double d = minecraft.player.blockInteractionRange();
      double f = minecraft.player.blockInteractionRange();
      double e = d * f;
      Vec3 vec3 = minecraft.player.getEyePosition();
      Vec3 vec31 = Vec3.atCenterOf(blockPos);
      Direction[] directions = Direction.values();
      int j = directions.length;

      for (int i = 0; i < j; i++) {
         int k = i;
         Direction direction = directions[k];
         BlockPos blockPos1 = blockPos.relative(direction);
         boolean bl1 = minecraft.level.getBlockState(blockPos1).isSolid();
         boolean bl2 = set.contains(blockPos1);
         if (bl1 || bl2) {
            Direction direction1 = direction.getOpposite();
            double g = direction1.getStepX();
            double h = direction1.getStepY();
            double l = direction1.getStepZ();
            Vec3 vec32 = new Vec3(g, h, l);
            Vec3 vec33 = Vec3.atCenterOf(blockPos1);
            Vec3 vec35 = vec32.scale(0.5);
            Vec3 vec34 = vec33.add(vec35);
            if (!(vec3.distanceToSqr(vec34) > e)) {
               if (bl || bl2) {
                  return new BlockHitResult(vec34, direction1, blockPos1, false);
               }

               ClientLevel clientLevel = minecraft.level;
               Block block = Block.OUTLINE;
               Fluid fluid = Fluid.NONE;
               LocalPlayer localPlayer = minecraft.player;
               ClipContext clipContext = new ClipContext(vec3, vec31, block, fluid, localPlayer);
               BlockHitResult blockHitResult = clientLevel.clip(clipContext);
               Type type = blockHitResult.getType();
               Type type1 = Type.BLOCK;
               if (type == type1 && blockHitResult.getBlockPos().equals(blockPos1) && blockHitResult.getDirection() == direction1) {
                  Vec3 vec36 = blockHitResult.getLocation();
                  return new BlockHitResult(vec36, direction1, blockPos1, false);
               }
            }
         }
      }

      return null;
   }

   private static int methodA(Minecraft minecraft, boolean bl, Set<String> set) {
      Inventory inventory = minecraft.player.getInventory();

      for (int i = 0; i < 9; i++) {
         int j = i;
         ItemStack itemStack = inventory.getItem(j);
         if (!itemStack.isEmpty() && itemStack.getItem() instanceof BlockItem) {
            if (!bl || set.isEmpty()) {
               return i;
            }

            DefaultedRegistry defaultedRegistry = BuiltInRegistries.ITEM;
            Item item = itemStack.getItem();
            String string = defaultedRegistry.getKey(item).getPath();
            if (set.contains(string)) {
               return i;
            }
         }
      }

      return -1;
   }

   private Set<String> getWhitelistBlocksSetting() {
      ArrayList arrayList = new ArrayList();
      List list = this.getSetting("Whitelist Blocks", arrayList);
      HashSet hashSet = new HashSet();

      for (Object object : list) {
         if (object instanceof String string) {
            hashSet.add(string);
         } else if (object instanceof Map) {
            Object object1 = ((Map)object).get("id");
            if (object1 != null) {
               String string1 = object1.toString();
               hashSet.add(string1);
            }
         }
      }

      return hashSet;
   }

   private void methodA(Minecraft minecraft, AutoTrap$PlacementJob placementJob) {
      MultiPlayerGameMode multiPlayerGameMode = minecraft.gameMode;
      LocalPlayer localPlayer1 = minecraft.player;
      InteractionHand interactionHand = InteractionHand.MAIN_HAND;
      BlockHitResult blockHitResult = placementJob.getBlockHitResultB();
      if (multiPlayerGameMode.useItemOn(localPlayer1, interactionHand, blockHitResult).consumesAction()) {
         LocalPlayer localPlayer = minecraft.player;
         InteractionHand interactionHand1 = InteractionHand.MAIN_HAND;
         localPlayer.swing(interactionHand1);
         this.methodC(minecraft, 4);
         Boolean boolean_ = false;
         if (this.getSetting("Render", boolean_)) {
            List list = this.listH;
            BlockPos blockPos = placementJob.getBlockPosA();
            long l = System.currentTimeMillis();
            AutoTrap$RenderEntry renderEntry = new AutoTrap$RenderEntry(blockPos, l);
            list.add(renderEntry);
         }
      }
   }

   private static Player methodA(Minecraft minecraft) {
      Player player = null;
      double e = Double.MAX_VALUE;

      for (Player player1 : minecraft.level.players()) {
         LocalPlayer localPlayer = minecraft.player;
         if (Targets.methodA(player1, localPlayer)) {
            double f = minecraft.player.distanceToSqr(player1);
            double d = e;
            if (f < d) {
               e = f;
               player = player1;
            }
         }
      }

      return player;
   }

   private boolean methodB(Minecraft minecraft) {
      if (this.intJ != -1 && minecraft.player != null) {
         int i = InventoryAccess.getSelectedSlot(minecraft.player.getInventory());
         int j = this.intJ;
         boolean bl;
         if (i != j) {
            bl = true;
         } else {
            bl = false;
         }

         return bl;
      } else {
         return false;
      }
   }

   private void methodB(Minecraft minecraft, int i) {
      Boolean boolean_ = true;
      if (this.getSetting("Swap", boolean_)) {
         int j = InventoryAccess.getSelectedSlot(minecraft.player.getInventory());
         if (j == i) {
            this.intJ = i;
         } else {
            if (this.intF == -1) {
               this.intF = j;
            }

            InventoryAccess.setSelectedSlot(minecraft.player.getInventory(), i);
            this.intJ = i;
         }
      }
   }

   private void methodC(Minecraft minecraft) {
      if (this.intF >= 0 && minecraft.player != null) {
         Inventory inventory = minecraft.player.getInventory();
         int i = this.intF;
         InventoryAccess.setSelectedSlot(inventory, i);
      }

      this.intF = -1;
      this.intJ = -1;
   }

   private void methodD(Minecraft minecraft) {
      AimAssist.methodE();
      this.blockPosB = null;
      this.flagD = false;
      this.intE = 0;
   }

   private void methodA(PoseStack poseStack, float r) {
      if (this.isEnabled()) {
         Boolean boolean_ = false;
         if (this.getSetting("Render", boolean_)) {
            long l = System.currentTimeMillis();
            List list = this.listH;
            Predicate predicate = renderEntry1 -> {
               long s = renderEntry1.getLongB();
               boolean bl;
               if (l - s > 750L) {
                  bl = true;
               } else {
                  bl = false;
               }

               return bl;
            };
            list.removeIf(predicate);
            if (!this.listH.isEmpty()) {
               int i = methodA(this.getSetting("Render Color", "#3a7fcc"));

               for (AutoTrap$RenderEntry renderEntry : this.listH) {
                  long n = renderEntry.getLongB();
                  float g = (float)(l - n) / 750.0F;
                  float f = 1.0F - g;
                  if (!(f <= 0.0F)) {
                     int j = ((int)(f * 90.0F) & 0xFF) << 24;
                     int m = i & 16777215;
                     int k = j | m;
                     BlockPos blockPos = renderEntry.getBlockPosA();
                     double d = blockPos.getX();
                     double e = blockPos.getY();
                     double h = blockPos.getZ();
                     double o = blockPos.getX() + 1;
                     double p = blockPos.getY() + 1;
                     double q = blockPos.getZ() + 1;
                     AABB aABB = new AABB(d, e, h, o, p, q);
                     List list1 = List.of(aABB);
                     BoxRenderer.methodA(poseStack, list1, k, 0, true, false);
                  }
               }
            }
         }
      }
   }

   private static float[] methodA(Vec3 vec3, Vec3 vec31) {
      Vec3 vec32 = vec31.subtract(vec3);
      double d = vec32.x;
      double j = vec32.x;
      double e = d * j;
      double k = vec32.z;
      double n = vec32.z;
      double l = k * n;
      double f = Math.sqrt(e + l);
      double g = -vec32.x;
      double m = vec32.z;
      float h = (float)(Math.atan2(g, m) * 180.0 / Math.PI);
      float i = (float)(-Math.atan2(vec32.y, f) * 180.0 / Math.PI);
      return new float[]{h, i};
   }

   private void methodC(Minecraft minecraft, int i) {
      if (fieldI != null) {
         try {
            fieldI.setInt(minecraft, i);
         } catch (IllegalAccessException illegalAccessException) {
         }
      }
   }

   private static int methodA(int i, int j) {
      if (i >= j) {
         return i;
      }

      double d = Math.random();
      double e = j - i + 1;
      int k = (int)(d * e);
      return i + k;
   }

   private static int methodA(int i) {
      int j = (i + 49) / 50;
      return Math.max(0, j);
   }

   private static int methodA(String string) {
      return ThemeColors.resolveRgb(string, 3833804, false);
   }

   static {
      Field field = null;

      try {
         field = Minecraft.class.getDeclaredField("rightClickDelay");
         field.setAccessible(true);
      } catch (NoSuchFieldException noSuchFieldException) {
      }

      fieldI = field;
   }
}
