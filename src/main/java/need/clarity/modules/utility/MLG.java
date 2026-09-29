package need.clarity.modules.utility;

import need.clarity.PerFrameEvents;
import need.clarity.mixin.KeyMappingAccessor;
import need.clarity.modules.ModuleBase;
import need.clarity.modules.combat.AimAssist;
import need.clarity.util.InventoryAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public class MLG extends ModuleBase {
   private MLG$State stateA;
   private int intB;
   private int intC;
   private boolean flagD;
   private long longE;
   private boolean flagF;
   private int intG;
   private int intH;
   private boolean flagI;
   private float floatJ;
   private float floatK;
   private boolean flagL;
   private float floatM;

   public MLG() {
      super("MLG", "utility");
      MLG$State state = MLG$State.IDLE;
      this.stateA = state;
      this.intB = -1;
      this.intC = 0;
      this.flagD = false;
      this.longE = 0L;
      this.flagF = false;
      this.intG = -1;
      this.intH = 0;
      this.flagI = false;
      this.floatJ = 0.0F;
      this.floatK = 0.0F;
      this.flagL = false;
      this.floatM = 512.0F;
      PerFrameEvents.register(this::methodA);
   }

   @Override
   public void onEnable() {
      this.methodC();
   }

   @Override
   public void onDisable() {
      Minecraft minecraft = Minecraft.getInstance();
      if (this.intB >= 0 && minecraft.player != null) {
         Inventory inventory = minecraft.player.getInventory();
         int i = this.intB;
         InventoryAccess.setSelectedSlot(inventory, i);
      }

      this.methodB();
      this.methodD();
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         if (minecraft.level != null) {
            if (this.intC > 0) {
               int p = this.intC - 1;
               this.intC = p;
               if (this.intC == 0 && this.intB >= 0) {
                  Boolean boolean1 = false;
                  if (this.getSetting("Swap Back", boolean1)) {
                     this.flagD = true;
                     long q = System.currentTimeMillis();
                     this.longE = q;
                  } else {
                     Inventory inventory1 = minecraft.player.getInventory();
                     int w = this.intB;
                     InventoryAccess.setSelectedSlot(inventory1, w);
                     this.intB = -1;
                  }
               }
            }

            if (this.flagD && this.intB >= 0) {
               Integer integer = 0;
               int i = this.getSetting("Swap Back Delay", integer);
               long l = System.currentTimeMillis();
               long x = this.longE;
               long m = l - x;
               long y = i;
               if (m >= y) {
                  Inventory inventory = minecraft.player.getInventory();
                  int r = this.intB;
                  InventoryAccess.setSelectedSlot(inventory, r);
                  this.intB = -1;
                  this.flagD = false;
               }
            }

            MLG$State state = this.stateA;
            MLG$State state11 = MLG$State.PICKUP;
            if (state == state11) {
               this.methodA(minecraft);
            } else if (!minecraft.player.onGround() && !minecraft.player.isInWater() && !minecraft.player.isInLava()) {
               double d = minecraft.player.getDeltaMovement().y;
               if (d >= 0.0) {
                  MLG$State state5 = this.stateA;
                  MLG$State state17 = MLG$State.DONE;
                  if (state5 == state17) {
                     this.flagI = false;
                  }

                  MLG$State state6 = this.stateA;
                  MLG$State state18 = MLG$State.IDLE;
                  if (state6 == state18) {
                     return;
                  }
               }

               float t = this.methodB(minecraft);
               this.floatM = t;
               Float float1 = 5.0F;
               float g = this.getSetting("Fall Threshold", float1);
               float h = (float)minecraft.player.fallDistance;
               float u = this.floatM;
               if (h + u < g) {
                  MLG$State state7 = this.stateA;
                  MLG$State state19 = MLG$State.DONE;
                  if (state7 == state19) {
                     this.flagI = false;
                  }

                  MLG$State state8 = this.stateA;
                  MLG$State state20 = MLG$State.IDLE;
                  if (state8 == state20) {
                     return;
                  }
               }

               Boolean boolean4 = false;
               if (this.getSetting("Only Lethal Fall", boolean4) && !this.methodA(minecraft, h)) {
                  MLG$State state9 = this.stateA;
                  MLG$State state21 = MLG$State.IDLE;
                  if (state9 == state21) {
                     return;
                  }

                  MLG$State state10 = this.stateA;
                  MLG$State state22 = MLG$State.AIMING;
                  if (state10 == state22) {
                     this.methodC();
                     return;
                  }
               }

               int j = this.methodD(minecraft);
               if (j < 0) {
                  this.flagI = false;
               } else {
                  float z = this.floatM;
                  float k = this.methodA(d, z);
                  switch (this.stateA) {
                     case IDLE:
                        MLG$State state23 = MLG$State.AIMING;
                        this.stateA = state23;
                        this.flagL = false;
                        this.flagI = true;
                        this.flagF = false;
                        this.intG = -1;
                        return;
                     case AIMING:
                        this.flagI = true;
                        float v = this.floatK;
                        float n = Math.abs(90.0F - v);
                        float o;
                        if (k <= 1.0F) {
                           o = 90.0F;
                        } else if (k <= 3.0F) {
                           o = 30.0F;
                        } else {
                           o = 5.0F;
                        }

                        float f = o;
                        boolean bl4;
                        if (n <= f) {
                           bl4 = true;
                        } else {
                           bl4 = false;
                        }

                        boolean bl = bl4;
                        boolean bl5;
                        if (k <= 5.0F) {
                           bl5 = true;
                        } else {
                           bl5 = false;
                        }

                        boolean bl1 = bl5;
                        if ((!bl || !bl1) && !(k <= 2.0F)) {
                           return;
                        }

                        MLG$State state26 = MLG$State.PLACING;
                        this.stateA = state26;
                        return;
                     case PLACING:
                        this.flagI = true;
                        this.methodA(minecraft, j);
                        MLG$State state25 = MLG$State.WAITING;
                        this.stateA = state25;
                        return;
                     case WAITING:
                        this.flagI = true;
                        if (this.intC <= 0) {
                           MLG$State state24 = MLG$State.DONE;
                           this.stateA = state24;
                           return;
                        }

                        return;
                     case DONE:
                        this.flagI = true;
                        return;
                  }
               }
            } else {
               label132: {
                  MLG$State state1 = this.stateA;
                  MLG$State state12 = MLG$State.DONE;
                  if (state1 != state12) {
                     MLG$State state2 = this.stateA;
                     MLG$State state13 = MLG$State.WAITING;
                     if (state2 != state13) {
                        MLG$State state3 = this.stateA;
                        MLG$State state14 = MLG$State.AIMING;
                        if (state3 != state14) {
                           break label132;
                        }
                     }
                  }

                  Boolean boolean2 = false;
                  boolean bl2 = this.getSetting("Pick Up Water", boolean2);
                  Boolean boolean3 = true;
                  boolean bl3 = this.getSetting("Water Bucket", boolean3);
                  if (bl2 && bl3 && this.flagF && minecraft.player.isInWater()) {
                     this.methodB();
                     this.flagI = false;
                     MLG$State state15 = MLG$State.PICKUP;
                     this.stateA = state15;
                     Integer integer1 = 0;
                     int s = this.getSetting("Pick Up Delay", integer1) / 50;
                     this.intH = s;
                     return;
                  }
               }

               MLG$State state4 = this.stateA;
               MLG$State state16 = MLG$State.IDLE;
               if (state4 != state16) {
                  this.methodB();
                  this.methodC();
               }
            }
         }
      }
   }

   private void methodA(Minecraft minecraft) {
      if (this.intH > 0) {
         int p = this.intH - 1;
         this.intH = p;
      } else if (!minecraft.player.isInWater()) {
         MLG$State state = MLG$State.IDLE;
         this.stateA = state;
         this.flagF = false;
      } else {
         int i = -1;
         if (this.intG >= 0 && this.intG < 9) {
            Inventory inventory = minecraft.player.getInventory();
            int l = this.intG;
            ItemStack itemStack = inventory.getItem(l);
            Item item = Items.BUCKET;
            if (itemStack.is(item)) {
               i = this.intG;
            }
         }

         if (i < 0) {
            for (int j = 0; j < 9; j++) {
               Inventory inventory1 = minecraft.player.getInventory();
               int m = j;
               ItemStack itemStack1 = inventory1.getItem(m);
               Item item1 = Items.BUCKET;
               if (itemStack1.is(item1)) {
                  i = j;
                  break;
               }
            }
         }

         if (i < 0) {
            MLG$State state1 = MLG$State.IDLE;
            this.stateA = state1;
            this.flagF = false;
         } else {
            int k = InventoryAccess.getSelectedSlot(minecraft.player.getInventory());
            if (this.intB < 0) {
               this.intB = k;
            }

            Inventory inventory2 = minecraft.player.getInventory();
            int n = i;
            InventoryAccess.setSelectedSlot(inventory2, n);
            Boolean boolean_ = true;
            if (this.getSetting("Silent Rotations", boolean_)) {
               AimAssist.methodC(minecraft.player.getYRot(), 89.0F);
            }

            KeyMappingAccessor keyMappingAccessor = (KeyMappingAccessor)minecraft.options.keyUse;
            int o = keyMappingAccessor.getClickCount() + 1;
            keyMappingAccessor.setClickCount(o);
            this.intC = 2;
            MLG$State state2 = MLG$State.IDLE;
            this.stateA = state2;
            this.flagF = false;
            this.intG = -1;
         }
      }
   }

   private void methodA() {
      if (this.isEnabled()) {
         if (this.flagI) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null) {
               Boolean boolean1 = true;
               boolean bl = this.getSetting("Silent Rotations", boolean1);
               Integer integer = 10;
               float f = this.getSetting("Aim Speed", integer).intValue();
               if (!this.flagL) {
                  this.flagL = true;
                  if (bl && AimAssist.isFlagF()) {
                     float m = AimAssist.getFloatD();
                     this.floatJ = m;
                     float n = AimAssist.getFloatE();
                     this.floatK = n;
                  } else {
                     float u = minecraft.player.getYRot();
                     this.floatJ = u;
                     float v = minecraft.player.getXRot();
                     this.floatK = v;
                  }
               }

               float g = this.floatJ;
               float h = f / 3.0F;
               float o = this.floatK;
               float i = 90.0F - o;
               float j = Math.min(Math.abs(i), h);
               float p = Math.signum(i);
               float k = j * p;
               float x = this.floatK + k;
               float w = Math.min(90.0F, x);
               float q = Math.max(-90.0F, w);
               this.floatK = q;
               this.floatJ = g;
               if (bl) {
                  float l = this.floatJ;
                  float r = this.floatK;
                  AimAssist.methodC(l, r);
               } else {
                  LocalPlayer localPlayer = minecraft.player;
                  float s = this.floatJ;
                  localPlayer.setYRot(s);
                  LocalPlayer localPlayer1 = minecraft.player;
                  float t = this.floatK;
                  localPlayer1.setXRot(t);
               }
            }
         }
      }
   }

   private void methodA(Minecraft minecraft, int i) {
      int j = InventoryAccess.getSelectedSlot(minecraft.player.getInventory());
      if (j != i) {
         this.intB = j;
         InventoryAccess.setSelectedSlot(minecraft.player.getInventory(), i);
      } else {
         this.intB = -1;
      }

      ItemStack itemStack = minecraft.player.getInventory().getItem(i);
      Item item = Items.WATER_BUCKET;
      boolean bl = itemStack.is(item);
      this.flagF = bl;
      this.intG = i;
      if (this.floatM <= 4.5F) {
         BlockHitResult blockHitResult = this.methodC(minecraft);
         if (blockHitResult != null) {
            minecraft.hitResult = blockHitResult;
         }
      }

      KeyMappingAccessor keyMappingAccessor = (KeyMappingAccessor)minecraft.options.keyUse;
      int k = keyMappingAccessor.getClickCount() + 1;
      keyMappingAccessor.setClickCount(k);
      this.intC = 2;
   }

   private float methodA(double d, float f) {
      if (f <= 0.0F) {
         return 0.0F;
      }

      double e = 0.0;
      double g = d;
      int i = 0;

      while (i < 2000) {
         g = (g - 0.08) * 0.98;
         double h = e;
         double k = g;
         e = h + k;
         i++;
         double j = e;
         double l = f * -1.0;
         if (j <= l) {
            return i;
         }
      }

      return 2000.0F;
   }

   private float methodB(Minecraft minecraft) {
      Vec3 vec3 = minecraft.player.position();
      Vec3 vec31 = vec3.add(0.0, -512.0, 0.0);
      ClientLevel clientLevel = minecraft.level;
      Block block = Block.COLLIDER;
      Fluid fluid = Fluid.NONE;
      LocalPlayer localPlayer = minecraft.player;
      ClipContext clipContext = new ClipContext(vec3, vec31, block, fluid, localPlayer);
      BlockHitResult blockHitResult = clientLevel.clip(clipContext);
      Type type = blockHitResult.getType();
      Type type1 = Type.MISS;
      if (type == type1) {
         return 512.0F;
      }

      double d = vec3.y;
      double e = blockHitResult.getLocation().y;
      return (float)(d - e);
   }

   private BlockHitResult methodC(Minecraft minecraft) {
      Vec3 vec3 = minecraft.player.position();
      Vec3 vec31 = vec3.add(0.0, -512.0, 0.0);
      ClientLevel clientLevel = minecraft.level;
      Block block = Block.COLLIDER;
      Fluid fluid = Fluid.NONE;
      LocalPlayer localPlayer = minecraft.player;
      ClipContext clipContext = new ClipContext(vec3, vec31, block, fluid, localPlayer);
      BlockHitResult blockHitResult = clientLevel.clip(clipContext);
      Type type = blockHitResult.getType();
      Type type1 = Type.MISS;
      if (type == type1) {
         return null;
      }

      Vec3 vec32 = blockHitResult.getLocation();
      Direction direction = Direction.UP;
      BlockPos blockPos = blockHitResult.getBlockPos();
      return new BlockHitResult(vec32, direction, blockPos, false);
   }

   private boolean methodA(Minecraft minecraft, float f) {
      float h = this.floatM;
      float i = f + h - 3.0F;
      float g = Math.max(0.0F, i);
      float j = minecraft.player.getHealth();
      boolean bl;
      if (g >= j) {
         bl = true;
      } else {
         bl = false;
      }

      return bl;
   }

   private int methodD(Minecraft minecraft) {
      Boolean boolean_ = true;
      if (!this.getSetting("Water Bucket", boolean_)) {
         return -1;
      }

      for (int i = 0; i < 9; i++) {
         Inventory inventory = minecraft.player.getInventory();
         int j = i;
         ItemStack itemStack = inventory.getItem(j);
         Item item = Items.WATER_BUCKET;
         if (itemStack.is(item)) {
            return i;
         }
      }

      return -1;
   }

   private void methodB() {
      Boolean boolean_ = true;
      if (this.getSetting("Silent Rotations", boolean_)) {
         AimAssist.methodE();
      }
   }

   private void methodC() {
      MLG$State state = MLG$State.IDLE;
      this.stateA = state;
      this.flagI = false;
      this.flagL = false;
      this.flagF = false;
      this.intG = -1;
      this.intH = 0;
      this.floatM = 512.0F;
   }

   private void methodD() {
      this.methodC();
      this.intB = -1;
      this.intC = 0;
      this.flagD = false;
      this.longE = 0L;
   }
}
