package need.clarity.modules.combat;

import java.lang.reflect.Field;
import java.util.Random;
import need.clarity.mixin.KeyMappingAccessor;
import need.clarity.modules.ModuleBase;
import need.clarity.modules.ModuleRegistry;
import need.clarity.util.InventoryAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class AutoHitCrystal extends ModuleBase {
   private AutoHitCrystal$Stage stageA;
   private long longB;
   private long longC;
   private long longD;
   private boolean flagE;
   private BlockPos blockPosF;
   private BlockPos blockPosG;
   private int intH;
   private int intI;
   private final Random random;
   private static final Field fieldK = methodA();

   private static Field methodA() {
      try {
         Field field = Minecraft.class.getDeclaredField("rightClickDelay");
         field.setAccessible(true);
         return field;
      } catch (NoSuchFieldException noSuchFieldException) {
         return null;
      }
   }

   public AutoHitCrystal() {
      super("Auto Hit Crystal", "Combat");
      AutoHitCrystal$Stage stage = AutoHitCrystal$Stage.NONE;
      this.stageA = stage;
      this.longB = 0L;
      this.longC = 0L;
      this.longD = 0L;
      this.flagE = false;
      this.blockPosF = null;
      this.blockPosG = null;
      this.intH = -1;
      this.intI = -1;
      Random randomx = new Random();
      this.random = randomx;
   }

   @Override
   public void onDisable() {
      Minecraft minecraft = Minecraft.getInstance();
      this.methodB(minecraft);
      this.methodC();
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (this.isEnabled()) {
         if (minecraft.player != null) {
            if (minecraft.level != null) {
               if (minecraft.gameMode != null) {
                  if (minecraft.screen == null) {
                     if (!minecraft.player.isUsingItem()) {
                        long l = System.currentTimeMillis();
                        boolean bl = minecraft.options.keyUse.isDown();
                        if (!bl) {
                           AutoHitCrystal$Stage stage1 = this.stageA;
                           AutoHitCrystal$Stage stage = AutoHitCrystal$Stage.NONE;
                           if (stage1 != stage) {
                              this.methodB(minecraft);
                              this.methodC();
                              return;
                           }
                        }

                        if (bl) {
                           int i = 0;

                           do {
                              AutoHitCrystal$Stage stage2 = this.stageA;
                              switch (this.stageA) {
                                 case NONE:
                                    this.methodA(minecraft, l);
                                    break;
                                 case SWITCH_OBSIDIAN:
                                    this.methodC(minecraft, l);
                                    break;
                                 case PLACE_OBSIDIAN:
                                    this.methodD(minecraft, l);
                                    break;
                                 case SWITCH_CRYSTAL:
                                    this.methodE(minecraft, l);
                                    break;
                                 case DONE:
                                    this.methodF(minecraft, l);
                              }

                              if (this.stageA == stage2) {
                                 return;
                              }

                              AutoHitCrystal$Stage stage3 = this.stageA;
                              AutoHitCrystal$Stage stage5 = AutoHitCrystal$Stage.NONE;
                              if (stage3 == stage5) {
                                 return;
                              }

                              AutoHitCrystal$Stage stage4 = this.stageA;
                              AutoHitCrystal$Stage stage6 = AutoHitCrystal$Stage.DONE;
                              if (stage4 == stage6) {
                                 return;
                              }

                              long m = this.longB;
                              if (l < m) {
                                 return;
                              }
                           } while (++i < 10);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void methodA(Minecraft minecraft, long l) {
      if (this.methodE(minecraft)) {
         if (this.methodA(minecraft)) {
            this.methodB(minecraft, l);
         }
      }
   }

   private boolean methodA(Minecraft minecraft) {
      HitResult hitResult = minecraft.hitResult;
      if (hitResult instanceof EntityHitResult && ((EntityHitResult)hitResult).getEntity() instanceof EndCrystal) {
         return this.methodF(minecraft);
      }

      if (minecraft.hitResult instanceof BlockHitResult blockHitResult) {
         BlockPos blockPos1 = blockHitResult.getBlockPos();
         BlockState blockState = minecraft.level.getBlockState(blockPos1);
         if (blockState.isAir()) {
            return false;
         }

         Block block = Blocks.OBSIDIAN;
         if (!blockState.is(block)) {
            Block block1 = Blocks.BEDROCK;
            if (!blockState.is(block1)) {
               if (this.methodG(minecraft) && this.methodF(minecraft)) {
                  Direction direction = blockHitResult.getDirection();
                  BlockPos blockPos2;
                  if (blockState.canBeReplaced()) {
                     blockPos2 = blockPos1;
                  } else {
                     blockPos2 = blockPos1.relative(direction);
                  }

                  BlockPos blockPos = blockPos2;
                  BlockState blockState1 = minecraft.level.getBlockState(blockPos);
                  boolean bl;
                  if (!blockState1.isAir() && !blockState1.canBeReplaced()) {
                     bl = false;
                  } else {
                     bl = true;
                  }

                  return bl;
               }

               return false;
            }
         }

         return this.methodF(minecraft);
      } else {
         return false;
      }
   }

   private void methodB(Minecraft minecraft, long l) {
      HitResult hitResult = minecraft.hitResult;
      if (hitResult instanceof EntityHitResult && ((EntityHitResult)hitResult).getEntity() instanceof EndCrystal) {
         this.longB = l;
         AutoHitCrystal$Stage stage = AutoHitCrystal$Stage.SWITCH_CRYSTAL;
         this.stageA = stage;
      } else if (minecraft.hitResult instanceof BlockHitResult blockHitResult) {
         BlockPos blockPos1 = blockHitResult.getBlockPos();
         BlockState blockState = minecraft.level.getBlockState(blockPos1);
         Block block = Blocks.OBSIDIAN;
         if (!blockState.is(block)) {
            Block block1 = Blocks.BEDROCK;
            if (!blockState.is(block1)) {
               if (blockState.isAir()) {
                  return;
               }

               Direction direction = blockHitResult.getDirection();
               BlockPos blockPos2;
               if (blockState.canBeReplaced()) {
                  blockPos2 = blockPos1;
               } else {
                  blockPos2 = blockPos1.relative(direction);
               }

               BlockPos blockPos = blockPos2;
               BlockState blockState1 = minecraft.level.getBlockState(blockPos);
               if (!blockState1.isAir() && !blockState1.canBeReplaced()) {
                  return;
               }

               this.blockPosF = blockPos;
               BlockPos blockPos3 = blockPos.below();
               this.blockPosG = blockPos3;
               this.flagE = false;
               this.longB = l;
               AutoHitCrystal$Stage stage1 = AutoHitCrystal$Stage.SWITCH_OBSIDIAN;
               this.stageA = stage1;
               return;
            }
         }

         this.longB = l;
         AutoHitCrystal$Stage stage2 = AutoHitCrystal$Stage.SWITCH_CRYSTAL;
         this.stageA = stage2;
      }
   }

   private void methodC(Minecraft minecraft, long l) {
      long n = this.longB;
      if (l >= n) {
         Item item = Items.OBSIDIAN;
         int i = this.methodA(minecraft, item);
         if (i == -1) {
            Item item1 = Items.BEDROCK;
            i = this.methodA(minecraft, item1);
         }

         if (i == -1) {
            this.methodC();
         } else {
            int j = i;
            this.methodC(minecraft, j);
            ((KeyMappingAccessor)minecraft.options.keyUse).setClickCount(0);
            this.methodB(minecraft, 4);
            this.flagE = false;
            long o = this.methodD();
            long m = l + o;
            this.longB = m;
            AutoHitCrystal$Stage stage = AutoHitCrystal$Stage.PLACE_OBSIDIAN;
            this.stageA = stage;
         }
      }
   }

   private void methodD(Minecraft minecraft, long l) {
      long m = this.longB;
      if (l >= m) {
         if (this.flagE && this.blockPosF != null) {
            ClientLevel clientLevel = minecraft.level;
            BlockPos blockPos = this.blockPosF;
            BlockState blockState = clientLevel.getBlockState(blockPos);
            Block block = Blocks.OBSIDIAN;
            if (!blockState.is(block)) {
               Block block1 = Blocks.BEDROCK;
               if (!blockState.is(block1)) {
                  return;
               }
            }

            this.flagE = false;
            this.longB = l;
            AutoHitCrystal$Stage stage = AutoHitCrystal$Stage.SWITCH_CRYSTAL;
            this.stageA = stage;
         } else {
            if (this.blockPosF == null) {
               if (!(minecraft.hitResult instanceof BlockHitResult blockHitResult)) {
                  this.methodC();
                  return;
               }

               BlockPos blockPos1 = this.methodA(minecraft, blockHitResult);
               this.blockPosF = blockPos1;
               if (this.blockPosF == null) {
                  this.methodC();
                  return;
               }

               BlockPos blockPos2 = this.blockPosF.below();
               this.blockPosG = blockPos2;
            }

            ClientLevel clientLevel1 = minecraft.level;
            BlockPos blockPos3 = this.blockPosF;
            BlockState blockState1 = clientLevel1.getBlockState(blockPos3);
            Block block2 = Blocks.OBSIDIAN;
            if (!blockState1.is(block2)) {
               Block block3 = Blocks.BEDROCK;
               if (!blockState1.is(block3)) {
                  if (!blockState1.isAir() && !blockState1.canBeReplaced()) {
                     this.methodC();
                     return;
                  }

                  double d = this.blockPosF.getX() + 0.5;
                  double e = this.blockPosF.getY();
                  double f = this.blockPosF.getZ() + 0.5;
                  Vec3 vec3 = new Vec3(d, e, f);
                  Direction direction = Direction.UP;
                  BlockPos blockPos4 = this.blockPosG;
                  BlockHitResult blockHitResult1 = new BlockHitResult(vec3, direction, blockPos4, false);
                  MultiPlayerGameMode multiPlayerGameMode = minecraft.gameMode;
                  LocalPlayer localPlayer1 = minecraft.player;
                  InteractionHand interactionHand1 = InteractionHand.MAIN_HAND;
                  if (multiPlayerGameMode.useItemOn(localPlayer1, interactionHand1, blockHitResult1).consumesAction()) {
                     LocalPlayer localPlayer = minecraft.player;
                     InteractionHand interactionHand = InteractionHand.MAIN_HAND;
                     localPlayer.swing(interactionHand);
                     this.methodB(minecraft, 4);
                  }

                  this.flagE = true;
                  return;
               }
            }

            this.flagE = false;
            this.blockPosF = null;
            this.blockPosG = null;
            this.longB = l;
            AutoHitCrystal$Stage stage1 = AutoHitCrystal$Stage.SWITCH_CRYSTAL;
            this.stageA = stage1;
         }
      }
   }

   private void methodE(Minecraft minecraft, long l) {
      long n = this.longB;
      if (l >= n) {
         Item item = Items.END_CRYSTAL;
         int i = this.methodA(minecraft, item);
         if (i == -1) {
            this.methodC();
         } else {
            this.methodC(minecraft, i);
            this.longC = 0L;
            long o = this.methodF();
            long m = l + o;
            this.longD = m;
            this.longB = l;
            AutoHitCrystal$Stage stage = AutoHitCrystal$Stage.DONE;
            this.stageA = stage;
         }
      }
   }

   private void methodF(Minecraft minecraft, long l) {
      ItemStack itemStack = minecraft.player.getMainHandItem();
      Item item = Items.END_CRYSTAL;
      if (!itemStack.is(item)) {
         Item item1 = Items.END_CRYSTAL;
         int i = this.methodA(minecraft, item1);
         if (i == -1) {
            this.methodC();
         } else {
            this.methodC(minecraft, i);
         }
      } else {
         HitResult hitResult = minecraft.hitResult;
         if (hitResult instanceof BlockHitResult) {
            BlockPos blockPos;
            boolean bl;
            label55: {
               blockPos = ((BlockHitResult)hitResult).getBlockPos();
               BlockState blockState = minecraft.level.getBlockState(blockPos);
               Block block = Blocks.OBSIDIAN;
               if (!blockState.is(block)) {
                  Block block1 = Blocks.BEDROCK;
                  if (!blockState.is(block1)) {
                     bl = false;
                     break label55;
                  }
               }

               bl = true;
            }

            if (bl) {
               long o = this.longC;
               if (l >= o) {
                  if (this.intI != -1) {
                     int k = this.intI;
                     if (this.methodA(minecraft, k)) {
                        return;
                     }

                     this.intI = -1;
                  }

                  if (!this.methodB(minecraft, blockPos)) {
                     this.methodC(minecraft);
                     long q = this.methodE();
                     long m = l + q;
                     this.longC = m;
                  }
               }
            }
         }

         HitResult hitResult1 = minecraft.hitResult;
         if (hitResult1 instanceof EntityHitResult) {
            if (((EntityHitResult)hitResult1).getEntity() instanceof EndCrystal endCrystal) {
               if (!endCrystal.isRemoved()) {
                  if (endCrystal.isAlive()) {
                     long p = this.longD;
                     if (l >= p) {
                        this.methodD(minecraft);
                        int j = endCrystal.getId();
                        this.intI = j;
                        long r = this.methodF();
                        long n = l + r;
                        this.longD = n;
                        this.methodB();
                        this.methodC();
                     }
                  }
               }
            }
         }
      }
   }

   private void methodB() {
      AutoCrystal autoCrystal = (AutoCrystal)ModuleRegistry.getModule("Auto Crystal");
      if (autoCrystal != null && autoCrystal.isEnabled()) {
         int i = this.intH;
         autoCrystal.methodA(i);
         this.intH = -1;
      } else {
         Minecraft minecraft = Minecraft.getInstance();
         this.methodB(minecraft);
      }
   }

   private void methodC() {
      AutoHitCrystal$Stage stage = AutoHitCrystal$Stage.NONE;
      this.stageA = stage;
      this.longB = 0L;
      this.longC = 0L;
      this.longD = 0L;
      this.flagE = false;
      this.blockPosF = null;
      this.blockPosG = null;
      this.intI = -1;
   }

   private void methodB(Minecraft minecraft) {
      if (this.intH >= 0 && minecraft.player != null) {
         Inventory inventory = minecraft.player.getInventory();
         int i = this.intH;
         methodA(inventory, i);
      }

      this.intH = -1;
   }

   private void methodC(Minecraft minecraft) {
      KeyMappingAccessor keyMappingAccessor = (KeyMappingAccessor)minecraft.options.keyUse;
      int i = ((KeyMappingAccessor)minecraft.options.keyUse).getClickCount() + 1;
      keyMappingAccessor.setClickCount(i);
   }

   private void methodD(Minecraft minecraft) {
      if (minecraft.hitResult instanceof EntityHitResult entityHitResult) {
         MultiPlayerGameMode multiPlayerGameMode = minecraft.gameMode;
         LocalPlayer localPlayer1 = minecraft.player;
         Entity entity = entityHitResult.getEntity();
         multiPlayerGameMode.attack(localPlayer1, entity);
         LocalPlayer localPlayer = minecraft.player;
         InteractionHand interactionHand = InteractionHand.MAIN_HAND;
         localPlayer.swing(interactionHand);
      }
   }

   private BlockPos methodA(Minecraft minecraft, BlockHitResult blockHitResult) {
      try {
         BlockPos blockPos = blockHitResult.getBlockPos();
         BlockState blockState = minecraft.level.getBlockState(blockPos);
         if (blockState.isAir()) {
            return null;
         }

         Direction direction = blockHitResult.getDirection();
         BlockPos blockPos1;
         if (blockState.canBeReplaced()) {
            blockPos1 = blockPos;
         } else {
            blockPos1 = blockPos.relative(direction);
         }

         return blockPos1;
      } catch (Exception exception) {
         return null;
      }
   }

   private EndCrystal methodA(Minecraft minecraft, BlockPos blockPos) {
      for (Entity entity : minecraft.level.entitiesForRendering()) {
         if (entity instanceof EndCrystal endCrystal && !endCrystal.isRemoved() && BlockPos.containing(endCrystal.position()).below().equals(blockPos)) {
            return endCrystal;
         }
      }

      return null;
   }

   private boolean methodB(Minecraft minecraft, BlockPos blockPos) {
      boolean bl;
      if (this.methodA(minecraft, blockPos) != null) {
         bl = true;
      } else {
         bl = false;
      }

      return bl;
   }

   private boolean methodA(Minecraft minecraft, int i) {
      for (Entity entity : minecraft.level.entitiesForRendering()) {
         if (entity.getId() == i && entity instanceof EndCrystal endCrystal) {
            boolean bl;
            if (!endCrystal.isRemoved() && endCrystal.isAlive()) {
               bl = true;
            } else {
               bl = false;
            }

            return bl;
         }
      }

      return false;
   }

   private void methodB(Minecraft minecraft, int i) {
      if (fieldK != null) {
         try {
            fieldK.setInt(minecraft, i);
         } catch (IllegalAccessException illegalAccessException) {
         }
      }
   }

   private boolean methodE(Minecraft minecraft) {
      Boolean boolean1 = true;
      boolean bl = this.getSetting("Work with Crystal", boolean1);
      Boolean boolean2 = true;
      boolean bl1 = this.getSetting("Work with Obsidian", boolean2);
      Boolean boolean3 = true;
      boolean bl2 = this.getSetting("Work with Sword", boolean3);
      Boolean boolean4 = true;
      boolean bl3 = this.getSetting("Work with Totem", boolean4);
      Boolean boolean5 = true;
      boolean bl4 = this.getSetting("Work with Fist", boolean5);
      if (!bl && !bl1 && !bl2 && !bl3 && !bl4) {
         return true;
      }

      ItemStack itemStack = minecraft.player.getMainHandItem();
      if (bl) {
         Item item = Items.END_CRYSTAL;
         if (itemStack.is(item)) {
            return true;
         }
      }

      if (bl1) {
         Item item1 = Items.OBSIDIAN;
         if (itemStack.is(item1)) {
            return true;
         }
      }

      if (bl2) {
         TagKey tagKey = ItemTags.SWORDS;
         if (itemStack.is(tagKey)) {
            return true;
         }
      }

      if (bl3) {
         Item item2 = Items.TOTEM_OF_UNDYING;
         if (itemStack.is(item2)) {
            return true;
         }
      }

      return bl4 && itemStack.isEmpty();
   }

   private boolean methodF(Minecraft minecraft) {
      Item item = Items.END_CRYSTAL;
      boolean bl;
      if (this.methodA(minecraft, item) != -1) {
         bl = true;
      } else {
         bl = false;
      }

      return bl;
   }

   private boolean methodG(Minecraft minecraft) {
      Item item = Items.OBSIDIAN;
      if (this.methodA(minecraft, item) == -1) {
         Item item1 = Items.BEDROCK;
         if (this.methodA(minecraft, item1) == -1) {
            return false;
         }
      }

      return true;
   }

   private int methodA(Minecraft minecraft, Item item) {
      for (int i = 0; i < 9; i++) {
         Inventory inventory = minecraft.player.getInventory();
         int j = i;
         if (inventory.getItem(j).is(item)) {
            return i;
         }
      }

      return -1;
   }

   private void methodC(Minecraft minecraft, int i) {
      if (this.intH == -1) {
         int j = methodA(minecraft.player.getInventory());
         this.intH = j;
      }

      methodA(minecraft.player.getInventory(), i);
   }

   private static int methodA(Inventory inventory) {
      return InventoryAccess.getSelectedSlot(inventory);
   }

   private static void methodA(Inventory inventory, int i) {
      InventoryAccess.setSelectedSlot(inventory, i);
   }

   private long methodD() {
      return this.methodA("Place Obsidian Delay Low", 0L, "Place Obsidian Delay High", 25L);
   }

   private long methodE() {
      return this.methodA("Place Delay Low", 0L, "Place Delay High", 25L);
   }

   private long methodF() {
      return this.methodA("Break Delay Low", 0L, "Break Delay High", 25L);
   }

   private long methodA(String string, long l, String string1, long m) {
      Long long1 = l;
      long o = methodA(this.getSetting(string, long1));
      Long long2 = m;
      long n = methodA(this.getSetting(string1, long2));
      if (n < o) {
         n = o;
      }

      long p = n;
      if (o == p) {
         return o;
      }

      double d = this.random.nextDouble();
      double e = n - o + 1L;
      long q = (long)(d * e);
      return o + q;
   }

   private static long methodA(Object object) {
      if (object == null) {
         return 0L;
      }

      if (object instanceof Number) {
         return ((Number)object).longValue();
      }

      try {
         return Long.parseLong(object.toString());
      } catch (NumberFormatException numberFormatException) {
         return 0L;
      }
   }
}
