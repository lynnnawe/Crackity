package need.clarity.modules.combat;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Predicate;
import need.clarity.PerFrameEvents;
import need.clarity.mixin.KeyMappingAccessor;
import need.clarity.modules.ModuleBase;
import need.clarity.util.InventoryAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import org.lwjgl.glfw.GLFW;

public class AutoAnchor extends ModuleBase {
   private AutoAnchor$Stage stageA;
   private BlockPos blockPosB;
   private BlockPos blockPosC;
   private int intD;
   private int intE;
   private int intF;
   private float floatG;
   private float floatH;
   private float floatI;
   private float floatJ;
   private float floatK;
   private float floatL;
   private boolean flagM;
   private static final Field fieldN = methodA();

   private static Field methodA() {
      try {
         Field field = Minecraft.class.getDeclaredField("rightClickDelay");
         field.setAccessible(true);
         return field;
      } catch (NoSuchFieldException noSuchFieldException) {
         return null;
      }
   }

   public AutoAnchor() {
      super("Auto Anchor", "Combat");
      AutoAnchor$Stage stage = AutoAnchor$Stage.IDLE;
      this.stageA = stage;
      this.blockPosB = null;
      this.blockPosC = null;
      this.intD = -1;
      this.intE = 0;
      this.intF = -1;
      this.floatG = 0.0F;
      this.floatH = 0.0F;
      this.floatI = 0.0F;
      this.floatJ = 0.0F;
      this.floatK = 0.0F;
      this.floatL = 0.0F;
      this.flagM = false;
      PerFrameEvents.register(this::methodB);
   }

   private void methodB() {
      if (this.isEnabled()) {
         if (this.isSafeAimModeSetting()) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null) {
               switch (this.stageA) {
                  case SAFE_ROTATE_TO_SIDE:
                  case SAFE_PLACE_GLOWSTONE:
                  case SILENT_SAFE_ROTATE_TO_SIDE:
                  case SILENT_SAFE_PLACE_GLOWSTONE:
                     float f = this.floatG;
                     float i = this.floatH;
                     AimAssist.methodC(f, i);
                     return;
                  case SAFE_ROTATE_TO_ANCHOR:
                  case SAFE_EXPLODE:
                  case SILENT_EXPLODE_WAIT:
                  case SILENT_SAFE_ROTATE_TO_ANCHOR:
                  case SILENT_SAFE_EXPLODE:
                     if (this.blockPosB != null) {
                        BlockPos blockPos = this.blockPosB;
                        Direction direction = this.methodB(minecraft, blockPos);
                        Vec3 vec3 = minecraft.player.getEyePosition();
                        Vec3 vec31 = methodB(this.blockPosB, direction);
                        float[] g = methodA(vec3, vec31);
                        float j = g[0];
                        this.floatI = j;
                        float k = g[1];
                        this.floatJ = k;
                     }

                     float h = this.floatI;
                     float l = this.floatJ;
                     AimAssist.methodC(h, l);
                     return;
               }
            }
         }
      }
   }

   @Override
   public void onDisable() {
      Minecraft minecraft = Minecraft.getInstance();
      this.methodV(minecraft);
      this.methodD();
      this.methodE();
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (this.isEnabled()) {
         if (minecraft.player != null) {
            if (minecraft.level != null) {
               if (minecraft.gameMode != null) {
                  if (minecraft.screen == null) {
                     String string = this.getSetting("Aim Mode", "None");
                     if ("Silent".equals(string)) {
                        this.methodH(minecraft);
                     } else {
                        this.methodA(minecraft);
                     }
                  }
               }
            }
         }
      }
   }

   private void methodA(Minecraft minecraft) {
      boolean bl = minecraft.options.keyUse.isDown();
      AutoAnchor$Stage stage1 = this.stageA;
      AutoAnchor$Stage stage = AutoAnchor$Stage.DONE;
      if (stage1 == stage) {
         this.methodT(minecraft);
         if (!bl) {
            this.methodD();
            this.methodE();
         }
      } else {
         if (!bl) {
            AutoAnchor$Stage stage2 = this.stageA;
            AutoAnchor$Stage stage3 = AutoAnchor$Stage.IDLE;
            if (stage2 != stage3) {
               this.methodV(minecraft);
               this.methodD();
               this.methodE();
               return;
            }
         }

         if (bl) {
            switch (this.stageA) {
               case IDLE:
                  this.methodB(minecraft);
                  return;
               case EXPLODE_WAIT:
                  this.methodC(minecraft);
                  return;
               case SAFE_ROTATE_TO_SIDE:
                  this.methodD(minecraft);
                  return;
               case SAFE_PLACE_GLOWSTONE:
                  this.methodE(minecraft);
                  return;
               case SAFE_ROTATE_TO_ANCHOR:
                  this.methodF(minecraft);
                  return;
               case SAFE_EXPLODE:
                  this.methodG(minecraft);
                  return;
            }
         }
      }
   }

   private void methodB(Minecraft minecraft) {
      String string = this.getSetting("Anchor Mode", "Normal");
      BlockHitResult blockHitResult = this.methodU(minecraft);
      if (blockHitResult != null) {
         BlockPos blockPos = blockHitResult.getBlockPos();
         if (this.methodD(minecraft, blockPos)) {
            if (this.methodE(minecraft, blockPos) == 0) {
               int i = this.methodW(minecraft);
               if (i != -1) {
                  this.methodT(minecraft);
                  this.blockPosB = blockPos;
                  this.methodB(minecraft, i);
                  ItemStack itemStack = minecraft.player.getMainHandItem();
                  Item item = Items.GLOWSTONE;
                  if (itemStack.is(item)) {
                     if (!this.methodA(minecraft, blockHitResult).consumesAction()) {
                        this.methodV(minecraft);
                        this.methodE();
                     } else {
                        Integer integer = 0;
                        int j = methodA(this.getSetting("Explode Delay", integer));
                        int k = minecraft.player.tickCount;
                        int m = Math.max(0, j);
                        int l = k + m;
                        this.intE = l;
                        if ("Safe".equals(string)) {
                           if (!this.methodC(minecraft, blockPos) && this.methodA(minecraft, blockPos)) {
                              AutoAnchor$Stage stage1 = AutoAnchor$Stage.SAFE_ROTATE_TO_SIDE;
                              this.stageA = stage1;
                           } else {
                              AutoAnchor$Stage stage = AutoAnchor$Stage.EXPLODE_WAIT;
                              this.stageA = stage;
                           }
                        } else {
                           AutoAnchor$Stage stage2 = AutoAnchor$Stage.EXPLODE_WAIT;
                           this.stageA = stage2;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void methodC(Minecraft minecraft) {
      this.methodT(minecraft);
      int i = minecraft.player.tickCount;
      int j = this.intE;
      if (i >= j) {
         if (this.blockPosB == null) {
            this.methodV(minecraft);
            this.methodE();
         } else {
            BlockPos blockPos2 = this.blockPosB;
            if (!this.methodD(minecraft, blockPos2)) {
               this.methodV(minecraft);
               this.methodE();
            } else {
               BlockHitResult blockHitResult = this.methodU(minecraft);
               if (blockHitResult != null) {
                  BlockPos blockPos = blockHitResult.getBlockPos();
                  BlockPos blockPos1 = this.blockPosB;
                  if (blockPos.equals(blockPos1)) {
                     this.methodV(minecraft);
                     ItemStack itemStack = minecraft.player.getMainHandItem();
                     Item item = Items.GLOWSTONE;
                     if (!itemStack.is(item)) {
                        if (this.methodA(minecraft, blockHitResult).consumesAction()) {
                           AutoAnchor$Stage stage = AutoAnchor$Stage.DONE;
                           this.stageA = stage;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void methodD(Minecraft minecraft) {
      this.methodT(minecraft);
      int i = minecraft.player.tickCount;
      int n = this.intE;
      if (i >= n) {
         boolean bl1 = this.isSafeAimModeSetting();
         Integer integer = 10;
         float h = methodB(this.getSetting("Safe Rotation Speed", integer));
         float j;
         if (bl1) {
            j = AimAssist.getFloatD();
         } else {
            j = minecraft.player.getYRot();
         }

         float f = j;
         float k;
         if (bl1) {
            k = AimAssist.getFloatE();
         } else {
            k = minecraft.player.getXRot();
         }

         boolean bl2;
         boolean bl3;
         label36: {
            label35: {
               float g = k;
               float p = this.floatG;
               float l = this.methodA(f, p, h);
               float q = this.floatH;
               float m = this.methodA(g, q, h);
               this.floatK = l;
               this.floatL = m;
               this.methodA(minecraft, l, m, bl1);
               float o = this.floatG;
               float r = this.floatH;
               bl2 = this.methodA(o, r, l, m);
               BlockHitResult blockHitResult = this.methodU(minecraft);
               if (blockHitResult != null) {
                  BlockPos blockPos = blockHitResult.getBlockPos();
                  Direction direction = blockHitResult.getDirection();
                  BlockPos blockPos1 = blockPos.relative(direction);
                  BlockPos blockPos3 = this.blockPosC;
                  if (blockPos1.equals(blockPos3)) {
                     break label35;
                  }

                  BlockPos blockPos2 = blockHitResult.getBlockPos();
                  BlockPos blockPos4 = this.blockPosC;
                  if (blockPos2.equals(blockPos4)) {
                     break label35;
                  }
               }

               bl3 = false;
               break label36;
            }

            bl3 = true;
         }

         boolean bl = bl3;
         if (bl2) {
            if (bl) {
               AutoAnchor$Stage stage = AutoAnchor$Stage.SAFE_PLACE_GLOWSTONE;
               this.stageA = stage;
            }
         }
      }
   }

   private void methodE(Minecraft minecraft) {
      this.methodT(minecraft);
      boolean bl = this.isSafeAimModeSetting();
      float f = this.floatG;
      float g = this.floatH;
      this.methodA(minecraft, f, g, bl);
      BlockHitResult blockHitResult = this.methodU(minecraft);
      label25:
      if (blockHitResult != null) {
         BlockPos blockPos = blockHitResult.getBlockPos();
         Direction direction = blockHitResult.getDirection();
         BlockPos blockPos1 = blockPos.relative(direction);
         BlockPos blockPos3 = this.blockPosC;
         if (!blockPos1.equals(blockPos3)) {
            BlockPos blockPos2 = blockHitResult.getBlockPos();
            BlockPos blockPos4 = this.blockPosC;
            if (!blockPos2.equals(blockPos4)) {
               break label25;
            }
         }

         ItemStack itemStack = minecraft.player.getMainHandItem();
         Item item = Items.GLOWSTONE;
         if (!itemStack.is(item)) {
            int i = this.methodW(minecraft);
            if (i == -1) {
               this.methodV(minecraft);
               this.methodD();
               this.methodE();
               return;
            }

            this.methodB(minecraft, i);
            return;
         }

         if (!this.methodA(minecraft, blockHitResult).consumesAction()) {
            return;
         }

         AutoAnchor$Stage stage = AutoAnchor$Stage.SAFE_ROTATE_TO_ANCHOR;
         this.stageA = stage;
         return;
      }

      AutoAnchor$Stage stage1 = AutoAnchor$Stage.SAFE_ROTATE_TO_SIDE;
      this.stageA = stage1;
   }

   private void methodF(Minecraft minecraft) {
      this.methodT(minecraft);
      if (this.blockPosB != null) {
         BlockPos blockPos2 = this.blockPosB;
         Direction direction = this.methodB(minecraft, blockPos2);
         Vec3 vec3 = minecraft.player.getEyePosition();
         Vec3 vec31 = methodB(this.blockPosB, direction);
         float[] g = methodA(vec3, vec31);
         float m = g[0];
         this.floatI = m;
         float n = g[1];
         this.floatJ = n;
      }

      boolean bl1 = this.isSafeAimModeSetting();
      Integer integer = 10;
      float h = methodB(this.getSetting("Safe Rotation Speed", integer));
      float i;
      if (bl1) {
         i = AimAssist.getFloatD();
      } else {
         i = minecraft.player.getYRot();
      }

      float s = i;
      float j;
      if (bl1) {
         j = AimAssist.getFloatE();
      } else {
         j = minecraft.player.getXRot();
      }

      boolean bl2;
      boolean bl3;
      label31: {
         float f = j;
         float p = this.floatI;
         float k = this.methodA(s, p, h);
         float q = this.floatJ;
         float l = this.methodA(f, q, h);
         this.floatK = k;
         this.floatL = l;
         this.methodA(minecraft, k, l, bl1);
         float o = this.floatI;
         float r = this.floatJ;
         bl2 = this.methodA(o, r, k, l);
         BlockHitResult blockHitResult = this.methodU(minecraft);
         if (blockHitResult != null) {
            BlockPos blockPos = blockHitResult.getBlockPos();
            BlockPos blockPos1 = this.blockPosB;
            if (blockPos.equals(blockPos1)) {
               bl3 = true;
               break label31;
            }
         }

         bl3 = false;
      }

      boolean bl = bl3;
      if (bl2) {
         if (bl) {
            AutoAnchor$Stage stage = AutoAnchor$Stage.SAFE_EXPLODE;
            this.stageA = stage;
         }
      }
   }

   private void methodG(Minecraft minecraft) {
      this.methodT(minecraft);
      if (this.blockPosB != null) {
         BlockPos blockPos2 = this.blockPosB;
         Direction direction = this.methodB(minecraft, blockPos2);
         Vec3 vec3 = minecraft.player.getEyePosition();
         Vec3 vec31 = methodB(this.blockPosB, direction);
         float[] f = methodA(vec3, vec31);
         float g = f[0];
         this.floatI = g;
         float h = f[1];
         this.floatJ = h;
      }

      boolean bl = this.isSafeAimModeSetting();
      float i = this.floatI;
      float j = this.floatJ;
      this.methodA(minecraft, i, j, bl);
      if (this.blockPosB == null) {
         this.methodV(minecraft);
         this.methodD();
         this.methodE();
      } else {
         BlockPos blockPos3 = this.blockPosB;
         if (!this.methodD(minecraft, blockPos3)) {
            this.methodV(minecraft);
            this.methodD();
            this.methodE();
         } else {
            BlockHitResult blockHitResult = this.methodU(minecraft);
            if (blockHitResult != null) {
               BlockPos blockPos = blockHitResult.getBlockPos();
               BlockPos blockPos1 = this.blockPosB;
               if (blockPos.equals(blockPos1)) {
                  this.methodV(minecraft);
                  ItemStack itemStack = minecraft.player.getMainHandItem();
                  Item item = Items.GLOWSTONE;
                  if (itemStack.is(item)) {
                     return;
                  }

                  if (!this.methodA(minecraft, blockHitResult).consumesAction()) {
                     return;
                  }

                  this.methodD();
                  AutoAnchor$Stage stage = AutoAnchor$Stage.DONE;
                  this.stageA = stage;
                  return;
               }
            }

            AutoAnchor$Stage stage1 = AutoAnchor$Stage.SAFE_ROTATE_TO_ANCHOR;
            this.stageA = stage1;
         }
      }
   }

   private void methodH(Minecraft minecraft) {
      String string = this.getSetting("Silent Bind", "None");
      boolean bl1 = methodA(minecraft, string);
      boolean bl2;
      if (bl1 && !this.flagM) {
         bl2 = true;
      } else {
         bl2 = false;
      }

      boolean bl = bl2;
      this.flagM = bl1;
      AutoAnchor$Stage stage = this.stageA;
      AutoAnchor$Stage stage1 = AutoAnchor$Stage.IDLE;
      if (stage == stage1 && bl && !this.methodI(minecraft)) {
         this.flagM = false;
      } else {
         switch (this.stageA) {
            case SILENT_PLACE_ANCHOR:
               this.methodJ(minecraft);
               return;
            case SILENT_CHARGE:
               this.methodL(minecraft);
               return;
            case SILENT_EXPLODE_WAIT:
               this.methodM(minecraft);
               return;
            case SILENT_SAFE_ROTATE_TO_SIDE:
               this.methodN(minecraft);
               return;
            case SILENT_SAFE_PLACE_GLOWSTONE:
               this.methodO(minecraft);
               return;
            case SILENT_SAFE_ROTATE_TO_ANCHOR:
               this.methodP(minecraft);
               return;
            case SILENT_SAFE_EXPLODE:
               this.methodQ(minecraft);
               return;
            case DONE:
               this.methodD();
               this.methodE();
               if (bl1) {
                  this.flagM = false;
                  return;
               }

               return;
         }
      }
   }

   private boolean methodI(Minecraft minecraft) {
      Player player = this.methodR(minecraft);
      if (player == null) {
         return false;
      }

      BlockPos blockPos = this.methodA(minecraft, player);
      if (blockPos == null) {
         return false;
      }

      this.blockPosB = blockPos;
      Vec3 vec3 = minecraft.player.getEyePosition();
      Direction direction = this.methodB(minecraft, blockPos);
      Vec3 vec31 = methodB(blockPos, direction);
      float[] f = methodA(vec3, vec31);
      float h = f[0];
      this.floatI = h;
      float j = f[1];
      this.floatJ = j;
      float g = this.floatI;
      float k = this.floatJ;
      AimAssist.methodC(g, k);
      Integer integer = 0;
      int i = methodA(this.getSetting("Explode Delay", integer));
      int l = minecraft.player.tickCount;
      int n = Math.max(0, i);
      int m = l + n;
      this.intE = m;
      AutoAnchor$Stage stage = AutoAnchor$Stage.SILENT_PLACE_ANCHOR;
      this.stageA = stage;
      return true;
   }

   private void methodJ(Minecraft minecraft) {
      if (this.blockPosB == null) {
         this.methodV(minecraft);
         this.methodE();
      } else {
         BlockPos blockPos3 = this.blockPosB;
         if (this.methodD(minecraft, blockPos3)) {
            AutoAnchor$Stage stage = AutoAnchor$Stage.SILENT_CHARGE;
            this.stageA = stage;
         } else {
            int j = this.methodK(minecraft);
            if (j == -1) {
               this.methodV(minecraft);
               this.methodE();
            } else {
               this.methodB(minecraft, j);
               ItemStack itemStack = minecraft.player.getMainHandItem();
               Item item = Items.RESPAWN_ANCHOR;
               if (itemStack.is(item)) {
                  Vec3 vec3 = minecraft.player.getEyePosition();
                  Direction direction1 = null;
                  Direction[] directions = new Direction[6];
                  Direction direction = Direction.DOWN;
                  directions[0] = direction;
                  Direction direction7 = Direction.NORTH;
                  directions[1] = direction7;
                  Direction direction8 = Direction.SOUTH;
                  directions[2] = direction8;
                  Direction direction9 = Direction.WEST;
                  directions[3] = direction9;
                  Direction direction10 = Direction.EAST;
                  directions[4] = direction10;
                  Direction direction11 = Direction.UP;
                  directions[5] = direction11;
                  int k = directions.length;

                  for (int i = 0; i < k; i++) {
                     int n = i;
                     Direction direction2 = directions[n];
                     BlockPos blockPos = this.blockPosB.relative(direction2);
                     BlockState blockState = minecraft.level.getBlockState(blockPos);
                     if (blockState.isSolid()) {
                        Block block = Blocks.BEDROCK;
                        if (!blockState.is(block)) {
                           Direction direction3 = direction2.getOpposite();
                           double d = blockPos.getX() + 0.5;
                           double v = direction3.getStepX() * 0.5;
                           double e = d + v;
                           double w = blockPos.getY() + 0.5;
                           double bb = direction3.getStepY() * 0.5;
                           double x = w + bb;
                           double bc = blockPos.getZ() + 0.5;
                           double bh = direction3.getStepZ() * 0.5;
                           double bd = bc + bh;
                           Vec3 vec31 = new Vec3(e, x, bd);
                           if (vec31.distanceToSqr(vec3) <= 20.25) {
                              direction1 = direction2;
                              break;
                           }
                        }
                     }
                  }

                  if (direction1 == null) {
                     this.methodV(minecraft);
                     this.methodE();
                  } else {
                     BlockPos blockPos1 = this.blockPosB;
                     Direction direction6 = direction1;
                     BlockPos blockPos2 = blockPos1.relative(direction6);
                     Direction direction4 = direction1.getOpposite();
                     double t = blockPos2.getX() + 0.5;
                     double y = direction4.getStepX() * 0.5;
                     double u = t + y;
                     double z = blockPos2.getY() + 0.5;
                     double be = direction4.getStepY() * 0.5;
                     double ba = z + be;
                     double bf = blockPos2.getZ() + 0.5;
                     double bi = direction4.getStepZ() * 0.5;
                     double bg = bf + bi;
                     Vec3 vec32 = new Vec3(u, ba, bg);
                     BlockHitResult blockHitResult = new BlockHitResult(vec32, direction4, blockPos2, false);
                     float f = minecraft.player.getYRot();
                     float g = minecraft.player.getXRot();
                     float[] h = methodA(minecraft.player.getEyePosition(), vec32);
                     LocalPlayer localPlayer = minecraft.player;
                     float o = h[0];
                     localPlayer.setYRot(o);
                     LocalPlayer localPlayer1 = minecraft.player;
                     float p = h[1];
                     localPlayer1.setXRot(p);
                     MultiPlayerGameMode multiPlayerGameMode = minecraft.gameMode;
                     LocalPlayer localPlayer3 = minecraft.player;
                     InteractionHand interactionHand1 = InteractionHand.MAIN_HAND;
                     InteractionResult interactionResult = multiPlayerGameMode.useItemOn(localPlayer3, interactionHand1, blockHitResult);
                     minecraft.player.setYRot(f);
                     minecraft.player.setXRot(g);
                     if (interactionResult.consumesAction()) {
                        LocalPlayer localPlayer2 = minecraft.player;
                        InteractionHand interactionHand = InteractionHand.MAIN_HAND;
                        localPlayer2.swing(interactionHand);
                        Vec3 vec33 = minecraft.player.getEyePosition();
                        BlockPos blockPos4 = this.blockPosB;
                        Direction direction5 = this.methodB(minecraft, blockPos4);
                        Vec3 vec34 = methodB(this.blockPosB, direction5);
                        float[] l = methodA(vec33, vec34);
                        float q = l[0];
                        this.floatI = q;
                        float r = l[1];
                        this.floatJ = r;
                        float m = this.floatI;
                        float s = this.floatJ;
                        AimAssist.methodC(m, s);
                        AutoAnchor$Stage stage1 = AutoAnchor$Stage.SILENT_CHARGE;
                        this.stageA = stage1;
                     }
                  }
               }
            }
         }
      }
   }

   private int methodK(Minecraft minecraft) {
      for (int i = 0; i < 9; i++) {
         Inventory inventory = minecraft.player.getInventory();
         int j = i;
         ItemStack itemStack = inventory.getItem(j);
         Item item = Items.RESPAWN_ANCHOR;
         if (itemStack.is(item)) {
            return i;
         }
      }

      return -1;
   }

   private void methodL(Minecraft minecraft) {
      if (this.blockPosB == null) {
         this.methodV(minecraft);
         this.methodE();
      } else {
         BlockPos blockPos = this.blockPosB;
         if (!this.methodD(minecraft, blockPos)) {
            this.methodV(minecraft);
            this.methodE();
         } else {
            BlockPos blockPos1 = this.blockPosB;
            if (this.methodE(minecraft, blockPos1) != 0) {
               String string = this.getSetting("Anchor Mode", "Normal");
               if ("Safe".equals(string)) {
                  BlockPos blockPos2 = this.blockPosB;
                  if (!this.methodC(minecraft, blockPos2)) {
                     BlockPos blockPos3 = this.blockPosB;
                     if (this.methodA(minecraft, blockPos3)) {
                        AutoAnchor$Stage stage = AutoAnchor$Stage.SILENT_SAFE_ROTATE_TO_SIDE;
                        this.stageA = stage;
                        return;
                     }
                  }

                  AutoAnchor$Stage stage1 = AutoAnchor$Stage.SILENT_EXPLODE_WAIT;
                  this.stageA = stage1;
               } else {
                  AutoAnchor$Stage stage2 = AutoAnchor$Stage.SILENT_EXPLODE_WAIT;
                  this.stageA = stage2;
               }
            } else {
               int i = this.methodW(minecraft);
               if (i == -1) {
                  this.methodV(minecraft);
                  this.methodE();
               } else {
                  this.methodB(minecraft, i);
                  ItemStack itemStack = minecraft.player.getMainHandItem();
                  Item item = Items.GLOWSTONE;
                  if (itemStack.is(item)) {
                     BlockPos blockPos4 = this.blockPosB;
                     Direction direction = this.methodB(minecraft, blockPos4);
                     BlockHitResult blockHitResult = methodA(this.blockPosB, direction);
                     if (!this.methodA(minecraft, blockHitResult).consumesAction()) {
                        this.methodV(minecraft);
                        this.methodE();
                     } else {
                        String string1 = this.getSetting("Anchor Mode", "Normal");
                        if ("Safe".equals(string1)) {
                           BlockPos blockPos5 = this.blockPosB;
                           if (!this.methodC(minecraft, blockPos5)) {
                              BlockPos blockPos6 = this.blockPosB;
                              if (this.methodA(minecraft, blockPos6)) {
                                 AutoAnchor$Stage stage3 = AutoAnchor$Stage.SILENT_SAFE_ROTATE_TO_SIDE;
                                 this.stageA = stage3;
                                 return;
                              }
                           }

                           AutoAnchor$Stage stage4 = AutoAnchor$Stage.SILENT_EXPLODE_WAIT;
                           this.stageA = stage4;
                        } else {
                           AutoAnchor$Stage stage5 = AutoAnchor$Stage.SILENT_EXPLODE_WAIT;
                           this.stageA = stage5;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void methodM(Minecraft minecraft) {
      if (this.blockPosB != null) {
         BlockPos blockPos = this.blockPosB;
         Direction direction = this.methodB(minecraft, blockPos);
         Vec3 vec3 = minecraft.player.getEyePosition();
         Vec3 vec31 = methodB(this.blockPosB, direction);
         float[] f = methodA(vec3, vec31);
         float h = f[0];
         this.floatI = h;
         float k = f[1];
         this.floatJ = k;
         float g = this.floatI;
         float l = this.floatJ;
         AimAssist.methodC(g, l);
      }

      int i = minecraft.player.tickCount;
      int m = this.intE;
      if (i >= m) {
         if (this.blockPosB == null) {
            this.methodV(minecraft);
            this.methodE();
         } else {
            BlockPos blockPos1 = this.blockPosB;
            if (!this.methodD(minecraft, blockPos1)) {
               this.methodV(minecraft);
               this.methodE();
            } else {
               if (this.intF < 0) {
                  int n = minecraft.player.tickCount + 10;
                  this.intF = n;
               }

               int j = minecraft.player.tickCount;
               int o = this.intF;
               if (j > o) {
                  this.methodV(minecraft);
                  this.methodD();
                  this.methodE();
               } else {
                  this.methodV(minecraft);
                  ItemStack itemStack = minecraft.player.getMainHandItem();
                  Item item = Items.GLOWSTONE;
                  if (!itemStack.is(item)) {
                     BlockPos blockPos2 = this.blockPosB;
                     Direction direction1 = this.methodB(minecraft, blockPos2);
                     BlockHitResult blockHitResult = methodA(this.blockPosB, direction1);
                     if (this.methodA(minecraft, blockHitResult).consumesAction()) {
                        AutoAnchor$Stage stage = AutoAnchor$Stage.DONE;
                        this.stageA = stage;
                     }
                  }
               }
            }
         }
      }
   }

   private void methodN(Minecraft minecraft) {
      int i = minecraft.player.tickCount;
      int l = this.intE;
      if (i >= l) {
         Integer integer = 10;
         float f = methodB(this.getSetting("Safe Rotation Speed", integer));
         float g = AimAssist.getFloatD();
         float h = AimAssist.getFloatE();
         float n = this.floatG;
         float j = this.methodA(g, n, f);
         float o = this.floatH;
         float k = this.methodA(h, o, f);
         AimAssist.methodC(j, k);
         float m = this.floatG;
         float p = this.floatH;
         if (this.methodA(m, p, j, k)) {
            AutoAnchor$Stage stage = AutoAnchor$Stage.SILENT_SAFE_PLACE_GLOWSTONE;
            this.stageA = stage;
         }
      }
   }

   private void methodO(Minecraft minecraft) {
      float f = this.floatG;
      float g = this.floatH;
      AimAssist.methodC(f, g);
      ItemStack itemStack = minecraft.player.getMainHandItem();
      Item item = Items.GLOWSTONE;
      if (!itemStack.is(item)) {
         int i = this.methodW(minecraft);
         if (i == -1) {
            this.methodV(minecraft);
            this.methodD();
            this.methodE();
         } else {
            this.methodB(minecraft, i);
         }
      } else {
         BlockHitResult blockHitResult = this.methodS(minecraft);
         if (blockHitResult != null) {
            if (this.methodA(minecraft, blockHitResult).consumesAction()) {
               AutoAnchor$Stage stage = AutoAnchor$Stage.SILENT_SAFE_ROTATE_TO_ANCHOR;
               this.stageA = stage;
            }
         }
      }
   }

   private void methodP(Minecraft minecraft) {
      if (this.blockPosB != null) {
         BlockPos blockPos = this.blockPosB;
         Direction direction = this.methodB(minecraft, blockPos);
         Vec3 vec3 = minecraft.player.getEyePosition();
         Vec3 vec31 = methodB(this.blockPosB, direction);
         float[] f = methodA(vec3, vec31);
         float l = f[0];
         this.floatI = l;
         float m = f[1];
         this.floatJ = m;
      }

      Integer integer = 10;
      float g = methodB(this.getSetting("Safe Rotation Speed", integer));
      float h = AimAssist.getFloatD();
      float i = AimAssist.getFloatE();
      float o = this.floatI;
      float j = this.methodA(h, o, g);
      float p = this.floatJ;
      float k = this.methodA(i, p, g);
      AimAssist.methodC(j, k);
      float n = this.floatI;
      float q = this.floatJ;
      if (this.methodA(n, q, j, k)) {
         AutoAnchor$Stage stage = AutoAnchor$Stage.SILENT_SAFE_EXPLODE;
         this.stageA = stage;
      }
   }

   private void methodQ(Minecraft minecraft) {
      if (this.blockPosB != null) {
         BlockPos blockPos = this.blockPosB;
         Direction direction = this.methodB(minecraft, blockPos);
         Vec3 vec3 = minecraft.player.getEyePosition();
         Vec3 vec31 = methodB(this.blockPosB, direction);
         float[] f = methodA(vec3, vec31);
         float h = f[0];
         this.floatI = h;
         float j = f[1];
         this.floatJ = j;
      }

      float g = this.floatI;
      float k = this.floatJ;
      AimAssist.methodC(g, k);
      if (this.blockPosB == null) {
         this.methodV(minecraft);
         this.methodD();
         this.methodE();
      } else {
         BlockPos blockPos1 = this.blockPosB;
         if (!this.methodD(minecraft, blockPos1)) {
            this.methodV(minecraft);
            this.methodD();
            this.methodE();
         } else {
            if (this.intF < 0) {
               int l = minecraft.player.tickCount + 10;
               this.intF = l;
            }

            int i = minecraft.player.tickCount;
            int m = this.intF;
            if (i > m) {
               this.methodV(minecraft);
               this.methodD();
               this.methodE();
            } else {
               this.methodV(minecraft);
               ItemStack itemStack = minecraft.player.getMainHandItem();
               Item item = Items.GLOWSTONE;
               if (!itemStack.is(item)) {
                  BlockPos blockPos2 = this.blockPosB;
                  Direction direction1 = this.methodB(minecraft, blockPos2);
                  BlockHitResult blockHitResult = methodA(this.blockPosB, direction1);
                  if (this.methodA(minecraft, blockHitResult).consumesAction()) {
                     this.methodD();
                     AutoAnchor$Stage stage = AutoAnchor$Stage.DONE;
                     this.stageA = stage;
                  }
               }
            }
         }
      }
   }

   private Player methodR(Minecraft minecraft) {
      List list = minecraft.level.players();
      Player player = null;
      double e = Double.MAX_VALUE;

      for (Player player1 : list) {
         LocalPlayer localPlayer = minecraft.player;
         if (player1 != localPlayer) {
            double f = minecraft.player.distanceToSqr(player1);
            double d = e;
            if (f < d && f < 64.0) {
               e = f;
               player = player1;
            }
         }
      }

      return player;
   }

   private BlockPos methodA(Minecraft minecraft, LivingEntity livingEntity) {
      Vec3 vec3 = minecraft.player.getEyePosition();
      BlockPos blockPos1 = minecraft.player.blockPosition();
      BlockPos blockPos = null;
      double d = -1.0;

      for (int k = -5; k <= 5; k++) {
         for (int l = -5; l <= 5; l++) {
            for (int m = -5; m <= 5; m++) {
               int q = k;
               int i = l;
               int j = m;
               BlockPos blockPos2 = blockPos1.offset(q, i, j);
               if (minecraft.level.getBlockState(blockPos2).isAir() || minecraft.level.getBlockState(blockPos2).canBeReplaced()) {
                  AABB aABB = new AABB(blockPos2);
                  ClientLevel clientLevel = minecraft.level;
                  Class<LivingEntity> class_ = LivingEntity.class;
                  Predicate predicate = livingEntity1 -> true;
                  if (clientLevel.getEntitiesOfClass(class_, aABB, predicate).isEmpty()) {
                     Vec3 vec31 = Vec3.atCenterOf(blockPos2);
                     if (!(vec31.distanceToSqr(vec3) > 20.25)) {
                        boolean bl = false;
                        Direction[] directions = new Direction[6];
                        Direction direction2 = Direction.DOWN;
                        directions[0] = direction2;
                        Direction direction3 = Direction.NORTH;
                        directions[1] = direction3;
                        Direction direction4 = Direction.SOUTH;
                        directions[2] = direction4;
                        Direction direction5 = Direction.WEST;
                        directions[3] = direction5;
                        Direction direction6 = Direction.EAST;
                        directions[4] = direction6;
                        Direction direction7 = Direction.UP;
                        directions[5] = direction7;
                        int o = directions.length;

                        for (int n = 0; n < o; n++) {
                           int r = n;
                           Direction direction = directions[r];
                           BlockPos blockPos3 = blockPos2.relative(direction);
                           BlockState blockState = minecraft.level.getBlockState(blockPos3);
                           if (blockState.isSolid()) {
                              Block block = Blocks.BEDROCK;
                              if (!blockState.is(block)) {
                                 Direction direction1 = direction.getOpposite();
                                 double s = blockPos3.getX() + 0.5;
                                 double y = direction1.getStepX() * 0.5;
                                 double t = s + y;
                                 double z = blockPos3.getY() + 0.5;
                                 double be = direction1.getStepY() * 0.5;
                                 double ba = z + be;
                                 double bf = blockPos3.getZ() + 0.5;
                                 double bj = direction1.getStepZ() * 0.5;
                                 double bg = bf + bj;
                                 Vec3 vec32 = new Vec3(t, ba, bg);
                                 if (vec32.distanceToSqr(vec3) <= 20.25) {
                                    bl = true;
                                    break;
                                 }
                              }
                           }
                        }

                        if (bl) {
                           Vec3 vec33 = livingEntity.getEyePosition();
                           double e = vec31.distanceTo(vec33);
                           if (!(e >= 10.0)) {
                              double x = livingEntity.getX();
                              double bc = livingEntity.getY();
                              double bh = livingEntity.getBbHeight() * 0.5;
                              double bd = bc + bh;
                              double bi = livingEntity.getZ();
                              Vec3 vec34 = new Vec3(x, bd, bi);
                              double f = vec31.distanceTo(vec34);
                              if (!(f > 3.0)) {
                                 double g = this.methodA(minecraft, vec31, livingEntity);
                                 if (!(g <= 0.0)) {
                                    double bb = e / 10.0;
                                    double u = 1.0 - bb;
                                    double h = 5.0 * u * g;
                                    double v = Math.max(0.5, f);
                                    double p = h / v;
                                    double w = d;
                                    if (p > w) {
                                       d = p;
                                       blockPos = blockPos2;
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return blockPos;
   }

   private double methodA(Minecraft minecraft, Vec3 vec3, LivingEntity livingEntity) {
      AABB aABB = livingEntity.getBoundingBox();
      int i = 0;
      int j = 0;
      double[] d = new double[2];
      double x = aABB.minX;
      d[0] = x;
      double y = aABB.maxX;
      d[1] = y;
      int n = d.length;

      for (int k = 0; k < n; k++) {
         int t = k;
         double g = d[t];
         double[] h = new double[2];
         double z = aABB.minY;
         h[0] = z;
         double ba = aABB.maxY;
         h[1] = ba;
         int o = h.length;

         for (int l = 0; l < o; l++) {
            int u = l;
            double p = h[u];
            double[] q = new double[2];
            double bb = aABB.minZ;
            q[0] = bb;
            double bc = aABB.maxZ;
            q[1] = bc;
            int r = q.length;

            for (int m = 0; m < r; m++) {
               int v = m;
               double s = q[v];
               Vec3 vec31 = new Vec3(g, p, s);
               ClientLevel clientLevel = minecraft.level;
               net.minecraft.world.level.ClipContext.Block block = net.minecraft.world.level.ClipContext.Block.COLLIDER;
               Fluid fluid = Fluid.NONE;
               ClipContext clipContext = new ClipContext(vec3, vec31, block, fluid, livingEntity);
               Type type = clientLevel.clip(clipContext).getType();
               Type type1 = Type.MISS;
               if (type == type1) {
                  i++;
               }

               j++;
            }
         }
      }

      double f;
      if (j == 0) {
         f = 0.0;
      } else {
         double e = i;
         double w = j;
         f = e / w;
      }

      return f;
   }

   private BlockHitResult methodS(Minecraft minecraft) {
      if (this.blockPosC == null) {
         return null;
      }

      if (minecraft.hitResult instanceof BlockHitResult blockHitResult) {
         BlockPos blockPos = blockHitResult.getBlockPos();
         Direction direction1 = blockHitResult.getDirection();
         BlockPos blockPos1 = blockPos.relative(direction1);
         BlockPos blockPos4 = this.blockPosC;
         if (blockPos1.equals(blockPos4)) {
            return blockHitResult;
         }

         BlockPos blockPos2 = blockHitResult.getBlockPos();
         BlockPos blockPos5 = this.blockPosC;
         if (blockPos2.equals(blockPos5)) {
            return blockHitResult;
         }
      }

      Direction[] directions = Direction.values();
      int j = directions.length;

      for (int i = 0; i < j; i++) {
         int k = i;
         Direction direction = directions[k];
         BlockPos blockPos3 = this.blockPosC.relative(direction);
         BlockPos blockPos6 = this.blockPosB;
         if (!blockPos3.equals(blockPos6) && minecraft.level.getBlockState(blockPos3).isSolid()) {
            Vec3 vec3 = Vec3.atCenterOf(blockPos3);
            double d = direction.getOpposite().getStepX() * 0.5;
            double e = direction.getOpposite().getStepY() * 0.5;
            double f = direction.getOpposite().getStepZ() * 0.5;
            Vec3 vec31 = vec3.add(d, e, f);
            Direction direction2 = direction.getOpposite();
            return new BlockHitResult(vec31, direction2, blockPos3, false);
         }
      }

      return null;
   }

   private static BlockHitResult methodA(BlockPos blockPos, Direction direction) {
      Vec3 vec3 = Vec3.atCenterOf(blockPos);
      double d = direction.getStepX() * 0.5;
      double e = direction.getStepY() * 0.5;
      double f = direction.getStepZ() * 0.5;
      Vec3 vec31 = vec3.add(d, e, f);
      return new BlockHitResult(vec31, direction, blockPos, false);
   }

   private boolean methodA(Minecraft minecraft, BlockPos blockPos) {
      Vec3 vec31 = minecraft.player.getEyePosition();
      Vec3 vec32 = Vec3.atCenterOf(blockPos);
      Direction direction = this.methodB(minecraft, blockPos);
      Vec3 vec33 = methodB(blockPos, direction);
      float[] h = methodA(vec31, vec33);
      float ba = h[0];
      this.floatI = ba;
      float bb = h[1];
      this.floatJ = bb;
      float n = minecraft.player.getYRot();
      float o = minecraft.player.getXRot();
      AABB aABB = minecraft.player.getBoundingBox();
      BlockPos blockPos1 = null;
      float f = Float.MAX_VALUE;

      for (int j = -1; j <= 1; j++) {
         for (int k = -1; k <= 1; k++) {
            for (int l = -1; l <= 1; l++) {
               if (j != 0 || k != 0 || l != 0) {
                  int bc = j;
                  int bk = k;
                  int i = l;
                  BlockPos blockPos2 = blockPos.offset(bc, bk, i);
                  if (minecraft.level.getBlockState(blockPos2).isAir() || minecraft.level.getBlockState(blockPos2).canBeReplaced()) {
                     AABB aABB1 = new AABB(blockPos2);
                     if (!aABB.intersects(aABB1) && methodA(vec32, vec31, blockPos2)) {
                        Vec3 vec3 = null;
                        float g = Float.MAX_VALUE;
                        Direction[] directions = Direction.values();
                        int p = directions.length;

                        for (int m = 0; m < p; m++) {
                           int bd = m;
                           Direction direction1 = directions[bd];
                           BlockPos blockPos3 = blockPos2.relative(direction1);
                           if (!blockPos3.equals(blockPos) && minecraft.level.getBlockState(blockPos3).isSolid()) {
                              double d = blockPos2.getX() + 0.5;
                              double bl = direction1.getStepX() * 0.5;
                              double e = d + bl;
                              double q = blockPos2.getY() + 0.5;
                              double bm = direction1.getStepY() * 0.5;
                              double r = q + bm;
                              double s = blockPos2.getZ() + 0.5;
                              double bn = direction1.getStepZ() * 0.5;
                              double t = s + bn;
                              Vec3 vec34 = new Vec3(e, r, t);
                              float[] u = methodA(vec31, vec34);
                              float v = Math.abs(methodA(u[0] - n));
                              float be = Math.abs(methodA(u[1] - o));
                              float w = v + be;
                              float bf = g;
                              if (w < bf) {
                                 g = w;
                                 vec3 = vec34;
                              }
                           }
                        }

                        if (vec3 != null) {
                           Vec3 vec35 = vec3;
                           float[] x = methodA(vec31, vec35);
                           float y = Math.abs(methodA(x[0] - n));
                           float bg = Math.abs(methodA(x[1] - o));
                           float z = y + bg;
                           float bh = f;
                           if (z < bh) {
                              f = z;
                              blockPos1 = blockPos2;
                              float bi = x[0];
                              this.floatG = bi;
                              float bj = x[1];
                              this.floatH = bj;
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      if (blockPos1 == null) {
         return false;
      }

      BlockPos blockPos4 = blockPos1;
      this.blockPosC = blockPos4;
      return true;
   }

   private static boolean methodA(Vec3 vec3, Vec3 vec31, BlockPos blockPos) {
      double h = blockPos.getX();
      double i = h + 1.0;
      double j = blockPos.getY();
      double k = j + 1.0;
      double l = blockPos.getZ();
      double m = l + 1.0;
      double n = vec31.x;
      double bn = vec3.x;
      double o = n - bn;
      double p = vec31.y;
      double bo = vec3.y;
      double q = p - bo;
      double r = vec31.z;
      double bp = vec3.z;
      double s = r - bp;
      double d = 0.0;
      double e = 1.0;
      if (Math.abs(o) < 1.0E-10) {
         double bl = vec3.x;
         double cl = h + 1.0E-4;
         if (bl <= cl) {
            return false;
         }

         double bm = vec3.x;
         double cm = i - 1.0E-4;
         if (bm >= cm) {
            return false;
         }
      } else {
         double bq = vec3.x;
         double f = (h - bq) / o;
         double br = vec3.x;
         double g = (i - br) / o;
         double t = f;
         double bs = g;
         if (t > bs) {
            double u = f;
            f = g;
            g = u;
         }

         double bt = f + 1.0E-4;
         d = Math.max(0.0, bt);
         double bu = g - 1.0E-4;
         e = Math.min(1.0, bu);
         double v = d;
         double bv = e;
         if (v > bv) {
            return false;
         }
      }

      if (Math.abs(q) < 1.0E-10) {
         double bj = vec3.y;
         double cj = j + 1.0E-4;
         if (bj <= cj) {
            return false;
         }

         double bk = vec3.y;
         double ck = k - 1.0E-4;
         if (bk >= ck) {
            return false;
         }
      } else {
         double bw = vec3.y;
         double cn = (j - bw) / q;
         double bx = vec3.y;
         double cp = (k - bx) / q;
         double w = cn;
         double by = cp;
         if (w > by) {
            double x = cn;
            cn = cp;
            cp = x;
         }

         double y = d;
         double bz = cn + 1.0E-4;
         d = Math.max(y, bz);
         double z = e;
         double ca = cp - 1.0E-4;
         e = Math.min(z, ca);
         double ba = d;
         double cb = e;
         if (ba > cb) {
            return false;
         }
      }

      if (Math.abs(s) < 1.0E-10) {
         double bh = vec3.z;
         double ch = l + 1.0E-4;
         if (bh <= ch) {
            return false;
         }

         double bi = vec3.z;
         double ci = m - 1.0E-4;
         if (bi >= ci) {
            return false;
         }
      } else {
         double cc = vec3.z;
         double co = (l - cc) / s;
         double cd = vec3.z;
         double cq = (m - cd) / s;
         double bb = co;
         double ce = cq;
         if (bb > ce) {
            double bc = co;
            co = cq;
            cq = bc;
         }

         double bd = d;
         double cf = co + 1.0E-4;
         double be = Math.max(bd, cf);
         double bf = e;
         double cg = cq - 1.0E-4;
         double bg = Math.min(bf, cg);
         if (be > bg) {
            return false;
         }
      }

      return true;
   }

   private Direction methodB(Minecraft minecraft, BlockPos blockPos) {
      float g = minecraft.player.getYRot();
      float h = minecraft.player.getXRot();
      Vec3 vec3 = minecraft.player.getEyePosition();
      Direction direction = null;
      float f = Float.MAX_VALUE;
      Direction[] directions = Direction.values();
      int j = directions.length;

      for (int i = 0; i < j; i++) {
         int n = i;
         Direction direction1 = directions[n];
         ClientLevel clientLevel = minecraft.level;
         BlockPos blockPos1 = blockPos.relative(direction1);
         if (!clientLevel.getBlockState(blockPos1).isSolid()) {
            Vec3 vec31 = methodB(blockPos, direction1);
            ClientLevel clientLevel1 = minecraft.level;
            net.minecraft.world.level.ClipContext.Block block = net.minecraft.world.level.ClipContext.Block.COLLIDER;
            Fluid fluid = Fluid.NONE;
            LocalPlayer localPlayer = minecraft.player;
            ClipContext clipContext = new ClipContext(vec3, vec31, block, fluid, localPlayer);
            BlockHitResult blockHitResult = clientLevel1.clip(clipContext);
            Type type = blockHitResult.getType();
            Type type1 = Type.MISS;
            boolean bl;
            if (type != type1 && (!(blockHitResult instanceof BlockHitResult) || !blockHitResult.getBlockPos().equals(blockPos))) {
               bl = false;
            } else {
               bl = true;
            }

            if (bl) {
               float[] k = methodA(vec3, vec31);
               float l = Math.abs(methodA(k[0] - g));
               float o = Math.abs(methodA(k[1] - h));
               float m = l + o;
               float p = f;
               if (m < p) {
                  f = m;
                  direction = direction1;
               }
            }
         }
      }

      Direction direction2;
      if (direction != null) {
         direction2 = direction;
      } else {
         direction2 = Direction.UP;
      }

      return direction2;
   }

   private static Vec3 methodB(BlockPos blockPos, Direction direction) {
      Vec3 vec3 = Vec3.atCenterOf(blockPos);
      double d = direction.getStepX() * 0.5;
      double e = direction.getStepY() * 0.5;
      double f = direction.getStepZ() * 0.5;
      return vec3.add(d, e, f);
   }

   private boolean methodC(Minecraft minecraft, BlockPos blockPos) {
      if (minecraft.level != null && minecraft.player != null) {
         Vec3 vec3 = minecraft.player.getEyePosition();
         Vec3 vec31 = minecraft.player.position().add(0.0, 0.1, 0.0);
         Vec3 vec32 = vec3.add(vec31).scale(0.5);
         Vec3[] vec3s = new Vec3[]{vec3, vec32, vec31};
         Vec3[] vec3s1 = new Vec3[6];
         Direction[] directions = Direction.values();

         for (int i = 0; i < 6; i++) {
            int p = i;
            int q = i;
            Direction direction = directions[q];
            Vec3 vec35 = methodB(blockPos, direction);
            vec3s1[p] = vec35;
         }

         int l = vec3s.length;

         for (int j = 0; j < l; j++) {
            int n = j;
            Vec3 vec33 = vec3s[n];
            int m = vec3s1.length;

            for (int k = 0; k < m; k++) {
               int o = k;
               Vec3 vec34 = vec3s1[o];
               ClientLevel clientLevel = minecraft.level;
               net.minecraft.world.level.ClipContext.Block block = net.minecraft.world.level.ClipContext.Block.COLLIDER;
               Fluid fluid = Fluid.NONE;
               LocalPlayer localPlayer = minecraft.player;
               ClipContext clipContext = new ClipContext(vec33, vec34, block, fluid, localPlayer);
               BlockHitResult blockHitResult = clientLevel.clip(clipContext);
               Type type = blockHitResult.getType();
               Type type1 = Type.MISS;
               boolean bl;
               if (type != type1 && (!(blockHitResult instanceof BlockHitResult) || !blockHitResult.getBlockPos().equals(blockPos))) {
                  bl = false;
               } else {
                  bl = true;
               }

               if (bl) {
                  return false;
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private static boolean methodA(Minecraft minecraft, String string) {
      if (string != null && !string.isEmpty() && !"None".equalsIgnoreCase(string)) {
         long l = minecraft.getWindow().handle();
         byte b = -1;
         int j = string.hashCode();
         switch (j) {
            case -1387248810:
               if (string.equals("Mouse 1")) {
                  b = 0;
               }
               break;
            case -1387248809:
               if (string.equals("Mouse 2")) {
                  b = 1;
               }
               break;
            case -1387248808:
               if (string.equals("Mouse 3")) {
                  b = 2;
               }
               break;
            case -1387248807:
               if (string.equals("Mouse 4")) {
                  b = 3;
               }
               break;
            case -1387248806:
               if (string.equals("Mouse 5")) {
                  b = 4;
               }
         }

         byte c = b;

         return switch (c) {
            case 0 -> {
               if (GLFW.glfwGetMouseButton(l, 0) == 1) {
                  yield true;
               } else {
                  yield false;
               }
            }
            case 1 -> {
               if (GLFW.glfwGetMouseButton(l, 1) == 1) {
                  yield true;
               } else {
                  yield false;
               }
            }
            case 2 -> {
               if (GLFW.glfwGetMouseButton(l, 2) == 1) {
                  yield true;
               } else {
                  yield false;
               }
            }
            case 3 -> {
               if (GLFW.glfwGetMouseButton(l, 3) == 1) {
                  yield true;
               } else {
                  yield false;
               }
            }
            case 4 -> {
               if (GLFW.glfwGetMouseButton(l, 4) == 1) {
                  yield true;
               } else {
                  yield false;
               }
            }
            default -> {
               int i = methodA(string);
               if (i != -1 && GLFW.glfwGetKey(l, i) == 1) {
                  yield true;
               } else {
                  yield false;
               }
            }
         };
      } else {
         return false;
      }
   }

   private static int methodA(String string) {
      byte b = -1;
      int k = string.hashCode();
      switch (k) {
         case -2099925287:
            if (string.equals("Insert")) {
               b = 14;
            }
            break;
         case -1466726509:
            if (string.equals("Page Down")) {
               b = 18;
            }
            break;
         case -937491361:
            if (string.equals("Backspace")) {
               b = 10;
            }
            break;
         case 2219:
            if (string.equals("F1")) {
               b = 19;
            }
            break;
         case 2220:
            if (string.equals("F2")) {
               b = 20;
            }
            break;
         case 2221:
            if (string.equals("F3")) {
               b = 21;
            }
            break;
         case 2222:
            if (string.equals("F4")) {
               b = 22;
            }
            break;
         case 2223:
            if (string.equals("F5")) {
               b = 23;
            }
            break;
         case 2224:
            if (string.equals("F6")) {
               b = 24;
            }
            break;
         case 2225:
            if (string.equals("F7")) {
               b = 25;
            }
            break;
         case 2226:
            if (string.equals("F8")) {
               b = 26;
            }
            break;
         case 2227:
            if (string.equals("F9")) {
               b = 27;
            }
            break;
         case 2747:
            if (string.equals("Up")) {
               b = 5;
            }
            break;
         case 65929:
            if (string.equals("Alt")) {
               b = 4;
            }
            break;
         case 68837:
            if (string.equals("F10")) {
               b = 28;
            }
            break;
         case 68838:
            if (string.equals("F11")) {
               b = 29;
            }
            break;
         case 68839:
            if (string.equals("F12")) {
               b = 30;
            }
            break;
         case 69819:
            if (string.equals("End")) {
               b = 16;
            }
            break;
         case 69973:
            if (string.equals("Esc")) {
               b = 1;
            }
            break;
         case 83829:
            if (string.equals("Tab")) {
               b = 12;
            }
            break;
         case 2111115:
            if (string.equals("Ctrl")) {
               b = 2;
            }
            break;
         case 2136258:
            if (string.equals("Down")) {
               b = 6;
            }
            break;
         case 2255103:
            if (string.equals("Home")) {
               b = 15;
            }
            break;
         case 2364455:
            if (string.equals("Left")) {
               b = 7;
            }
            break;
         case 67114680:
            if (string.equals("Enter")) {
               b = 11;
            }
            break;
         case 78959100:
            if (string.equals("Right")) {
               b = 8;
            }
            break;
         case 79854690:
            if (string.equals("Shift")) {
               b = 3;
            }
            break;
         case 80085222:
            if (string.equals("Space")) {
               b = 0;
            }
            break;
         case 353989386:
            if (string.equals("Caps Lock")) {
               b = 9;
            }
            break;
         case 861043148:
            if (string.equals("Page Up")) {
               b = 17;
            }
            break;
         case 2043376075:
            if (string.equals("Delete")) {
               b = 13;
            }
      }

      byte d = b;

      return switch (d) {
         case 0 -> 32;
         case 1 -> 256;
         case 2 -> 341;
         case 3 -> 340;
         case 4 -> 342;
         case 5 -> 265;
         case 6 -> 264;
         case 7 -> 263;
         case 8 -> 262;
         case 9 -> 280;
         case 10 -> 259;
         case 11 -> 257;
         case 12 -> 258;
         case 13 -> 261;
         case 14 -> 260;
         case 15 -> 268;
         case 16 -> 269;
         case 17 -> 266;
         case 18 -> 267;
         case 19 -> 290;
         case 20 -> 291;
         case 21 -> 292;
         case 22 -> 293;
         case 23 -> 294;
         case 24 -> 295;
         case 25 -> 296;
         case 26 -> 297;
         case 27 -> 298;
         case 28 -> 299;
         case 29 -> 300;
         case 30 -> 301;
         default -> {
            label151: {
               if (string.length() == 1) {
                  char c = string.charAt(0);
                  if (c >= 'A' && c <= 'Z') {
                     int i = c - 'A';
                     yield 65 + i;
                     break label151;
                  }

                  if (c >= '0' && c <= '9') {
                     if (c == '0') {
                        yield 48;
                     } else {
                        int j = c - '1';
                        yield 49 + j;
                     }
                     break label151;
                  }
               }

               yield -1;
            }
         }
      };
   }

   private boolean isSafeAimModeSetting() {
      Object object = this.getSetting("Safe Aim Mode", "Silent");
      return "Silent".equals(object);
   }

   private boolean methodA(float f, float g, float h, float i) {
      boolean bl;
      if (Math.abs(methodA(f - h)) < 2.0F && Math.abs(methodA(g - i)) < 2.0F) {
         bl = true;
      } else {
         bl = false;
      }

      return bl;
   }

   private float methodA(float f, float g, float h) {
      float i = methodA(g - f);
      float j = Math.min(Math.abs(i), h);
      float l = Math.signum(i);
      float k = j * l;
      return f + k;
   }

   private void methodA(Minecraft minecraft, float f, float g, boolean bl) {
      float i = Math.min(90.0F, g);
      float h = Math.max(-90.0F, i);
      if (bl) {
         AimAssist.methodC(f, h);
      } else {
         minecraft.player.setYRot(f);
         minecraft.player.setXRot(h);
      }
   }

   private void methodD() {
      AimAssist.methodE();
      this.blockPosC = null;
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

   private InteractionResult methodA(Minecraft minecraft, BlockHitResult blockHitResult) {
      MultiPlayerGameMode multiPlayerGameMode = minecraft.gameMode;
      LocalPlayer localPlayer1 = minecraft.player;
      InteractionHand interactionHand = InteractionHand.MAIN_HAND;
      InteractionResult interactionResult = multiPlayerGameMode.useItemOn(localPlayer1, interactionHand, blockHitResult);
      if (interactionResult.consumesAction()) {
         LocalPlayer localPlayer = minecraft.player;
         InteractionHand interactionHand1 = InteractionHand.MAIN_HAND;
         localPlayer.swing(interactionHand1);
         this.methodA(minecraft, 4);
      }

      return interactionResult;
   }

   private void methodT(Minecraft minecraft) {
      ((KeyMappingAccessor)minecraft.options.keyUse).setClickCount(0);
      this.methodA(minecraft, 4);
   }

   private void methodA(Minecraft minecraft, int i) {
      if (fieldN != null) {
         try {
            fieldN.setInt(minecraft, i);
         } catch (IllegalAccessException illegalAccessException) {
         }
      }
   }

   private BlockHitResult methodU(Minecraft minecraft) {
      return minecraft.hitResult instanceof BlockHitResult blockHitResult ? blockHitResult : null;
   }

   private boolean methodD(Minecraft minecraft, BlockPos blockPos) {
      if (blockPos != null && minecraft.level != null) {
         BlockState blockState = minecraft.level.getBlockState(blockPos);
         Block block = Blocks.RESPAWN_ANCHOR;
         return blockState.is(block);
      } else {
         return false;
      }
   }

   private int methodE(Minecraft minecraft, BlockPos blockPos) {
      if (!this.methodD(minecraft, blockPos)) {
         return 0;
      }

      BlockState blockState = minecraft.level.getBlockState(blockPos);
      IntegerProperty integerProperty = RespawnAnchorBlock.CHARGE;
      return (Integer)blockState.getValue(integerProperty);
   }

   private void methodE() {
      AutoAnchor$Stage stage = AutoAnchor$Stage.IDLE;
      this.stageA = stage;
      this.blockPosB = null;
      this.blockPosC = null;
      this.intE = 0;
      this.intF = -1;
      this.floatG = 0.0F;
      this.floatH = 0.0F;
      this.floatI = 0.0F;
      this.floatJ = 0.0F;
      this.floatK = 0.0F;
      this.floatL = 0.0F;
   }

   private void methodV(Minecraft minecraft) {
      if (this.intD >= 0 && minecraft.player != null) {
         Inventory inventory = minecraft.player.getInventory();
         int i = this.intD;
         methodA(inventory, i);
      }

      this.intD = -1;
   }

   private int methodW(Minecraft minecraft) {
      for (int i = 0; i < 9; i++) {
         Inventory inventory = minecraft.player.getInventory();
         int j = i;
         ItemStack itemStack = inventory.getItem(j);
         Item item = Items.GLOWSTONE;
         if (itemStack.is(item)) {
            return i;
         }
      }

      return -1;
   }

   private void methodB(Minecraft minecraft, int i) {
      if (this.intD == -1) {
         int j = methodA(minecraft.player.getInventory());
         this.intD = j;
      }

      methodA(minecraft.player.getInventory(), i);
   }

   private static int methodA(Inventory inventory) {
      return InventoryAccess.getSelectedSlot(inventory);
   }

   private static void methodA(Inventory inventory, int i) {
      InventoryAccess.setSelectedSlot(inventory, i);
   }

   private static float methodA(float f) {
      float g = f % 360.0F;
      if (g >= 180.0F) {
         g -= 360.0F;
      }

      if (g < -180.0F) {
         g += 360.0F;
      }

      return g;
   }

   private static int methodA(Object object) {
      if (object == null) {
         return 0;
      }

      if (object instanceof Number) {
         return ((Number)object).intValue();
      }

      try {
         return Integer.parseInt(object.toString());
      } catch (NumberFormatException numberFormatException) {
         return 0;
      }
   }

   private static float methodB(Object object) {
      if (object == null) {
         return 0.0F;
      }

      if (object instanceof Number) {
         return ((Number)object).floatValue();
      }

      try {
         return Float.parseFloat(object.toString());
      } catch (NumberFormatException numberFormatException) {
         return 0.0F;
      }
   }
}
