package need.clarity.modules.combat;

import java.lang.reflect.Field;
import java.util.Random;
import need.clarity.mixin.KeyMappingAccessor;
import need.clarity.modules.ModuleBase;
import need.clarity.util.InventoryAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
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

public class AutoCrystal extends ModuleBase {
   private AutoCrystal$Stage stageA;
   private long longB;
   private long longC;
   private long longD;
   private long longE;
   private int intF;
   private int intG;
   private final Random random;
   private static final Field fieldI = methodA();

   private static Field methodA() {
      try {
         Field field = Minecraft.class.getDeclaredField("rightClickDelay");
         field.setAccessible(true);
         return field;
      } catch (NoSuchFieldException noSuchFieldException) {
         return null;
      }
   }

   public AutoCrystal() {
      super("Auto Crystal", "Combat");
      AutoCrystal$Stage stage = AutoCrystal$Stage.IDLE;
      this.stageA = stage;
      this.longB = 0L;
      this.longC = 0L;
      this.longD = 0L;
      this.longE = 0L;
      this.intF = -1;
      this.intG = -1;
      Random randomx = new Random();
      this.random = randomx;
   }

   @Override
   public void onDisable() {
      AimAssist.methodE();
      this.longE = 0L;
      Minecraft minecraft = Minecraft.getInstance();
      this.methodB(minecraft);
      this.methodB();
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
                        if (this.longE > 0L) {
                           long m = this.longE;
                           if (l >= m) {
                              this.methodB(minecraft);
                              this.longE = 0L;
                           }
                        }

                        boolean bl = minecraft.options.keyUse.isDown();
                        if (!bl) {
                           AutoCrystal$Stage stage = this.stageA;
                           AutoCrystal$Stage stage1 = AutoCrystal$Stage.IDLE;
                           if (stage != stage1) {
                              AimAssist.methodE();
                              this.methodE(minecraft, l);
                              this.methodB();
                              return;
                           }
                        }

                        if (bl) {
                           switch (this.stageA) {
                              case IDLE:
                                 this.methodA(minecraft, l);
                                 return;
                              case SWITCH_CRYSTAL:
                                 this.methodC(minecraft, l);
                                 return;
                              case NORMAL:
                                 this.methodD(minecraft, l);
                                 return;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void methodA(Minecraft minecraft, long l) {
      if (this.methodA(minecraft)) {
         if (this.methodE(minecraft)) {
            this.methodB(minecraft, l);
         }
      }
   }

   private boolean methodA(Minecraft minecraft) {
      HitResult hitResult = minecraft.hitResult;
      if (hitResult instanceof EntityHitResult && ((EntityHitResult)hitResult).getEntity() instanceof EndCrystal) {
         return this.methodF(minecraft);
      }

      HitResult hitResult1 = minecraft.hitResult;
      if (hitResult1 instanceof BlockHitResult) {
         BlockPos blockPos = ((BlockHitResult)hitResult1).getBlockPos();
         BlockState blockState = minecraft.level.getBlockState(blockPos);
         if (blockState.isAir()) {
            return false;
         }

         Block block = Blocks.OBSIDIAN;
         if (!blockState.is(block)) {
            Block block1 = Blocks.BEDROCK;
            if (!blockState.is(block1)) {
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
         long o = this.methodC();
         long m = l + o;
         this.longB = m;
         AutoCrystal$Stage stage = AutoCrystal$Stage.SWITCH_CRYSTAL;
         this.stageA = stage;
      } else {
         HitResult hitResult1 = minecraft.hitResult;
         if (hitResult1 instanceof BlockHitResult) {
            BlockPos blockPos = ((BlockHitResult)hitResult1).getBlockPos();
            BlockState blockState = minecraft.level.getBlockState(blockPos);
            Block block = Blocks.OBSIDIAN;
            if (!blockState.is(block)) {
               Block block1 = Blocks.BEDROCK;
               if (!blockState.is(block1)) {
                  return;
               }
            }

            long p = this.methodC();
            long n = l + p;
            this.longB = n;
            AutoCrystal$Stage stage1 = AutoCrystal$Stage.SWITCH_CRYSTAL;
            this.stageA = stage1;
         }
      }
   }

   private void methodC(Minecraft minecraft, long l) {
      long n = this.longB;
      if (l >= n) {
         Item item = Items.END_CRYSTAL;
         int i = this.methodA(minecraft, item);
         if (i == -1) {
            this.methodB();
         } else {
            this.methodB(minecraft, i);
            this.longC = 0L;
            long o = this.methodE();
            long m = l + o;
            this.longD = m;
            AutoCrystal$Stage stage = AutoCrystal$Stage.NORMAL;
            this.stageA = stage;
         }
      }
   }

   private void methodD(Minecraft minecraft, long l) {
      ItemStack itemStack = minecraft.player.getMainHandItem();
      Item item = Items.END_CRYSTAL;
      if (!itemStack.is(item)) {
         Item item1 = Items.END_CRYSTAL;
         int i = this.methodA(minecraft, item1);
         if (i == -1) {
            this.methodB();
         } else {
            this.methodB(minecraft, i);
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
                  if (this.intG != -1) {
                     int k = this.intG;
                     if (this.methodA(minecraft, k)) {
                        return;
                     }

                     this.intG = -1;
                  }

                  if (!this.methodB(minecraft, blockPos)) {
                     this.methodC(minecraft);
                     long q = this.methodD();
                     long m = l + q;
                     this.longC = m;
                  }
               }
            }
         }

         if (hitResult instanceof EntityHitResult) {
            if (((EntityHitResult)hitResult).getEntity() instanceof EndCrystal endCrystal) {
               if (!endCrystal.isRemoved()) {
                  if (endCrystal.isAlive()) {
                     long p = this.longD;
                     if (l >= p) {
                        this.methodD(minecraft);
                        int j = endCrystal.getId();
                        this.intG = j;
                        long r = this.methodE();
                        long n = l + r;
                        this.longD = n;
                     }
                  }
               }
            }
         }
      }
   }

   public void methodA(int i) {
      if (this.isEnabled()) {
         AutoCrystal$Stage stage1 = this.stageA;
         AutoCrystal$Stage stage = AutoCrystal$Stage.IDLE;
         if (stage1 == stage) {
            long l = System.currentTimeMillis();
            this.intF = i;
            this.intG = -1;
            this.longC = 0L;
            long n = this.methodE();
            long m = l + n;
            this.longD = m;
            AutoCrystal$Stage stage2 = AutoCrystal$Stage.NORMAL;
            this.stageA = stage2;
         }
      }
   }

   private void methodB() {
      AutoCrystal$Stage stage = AutoCrystal$Stage.IDLE;
      this.stageA = stage;
      this.longB = 0L;
      this.intG = -1;
      this.longC = 0L;
      this.longD = 0L;
   }

   private void methodE(Minecraft minecraft, long l) {
      if (this.intF >= 0) {
         if (minecraft.player != null) {
            Boolean boolean_ = true;
            if (!this.getSetting("Swap Back", boolean_)) {
               this.intF = -1;
            } else {
               long m = this.methodF();
               if (m <= 0L) {
                  this.methodB(minecraft);
               } else {
                  long n = l + m;
                  this.longE = n;
               }
            }
         }
      }
   }

   private void methodB(Minecraft minecraft) {
      if (this.intF >= 0 && minecraft.player != null) {
         Inventory inventory = minecraft.player.getInventory();
         int i = this.intF;
         methodA(inventory, i);
      }

      this.intF = -1;
      this.longE = 0L;
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

   private boolean methodE(Minecraft minecraft) {
      Boolean boolean1 = true;
      boolean bl = this.getSetting("Work with Crystal", boolean1);
      Boolean boolean2 = true;
      boolean bl1 = this.getSetting("Work with Sword", boolean2);
      Boolean boolean3 = true;
      boolean bl2 = this.getSetting("Work with Totem", boolean3);
      Boolean boolean4 = true;
      boolean bl3 = this.getSetting("Work with Fist", boolean4);
      if (!bl && !bl1 && !bl2 && !bl3) {
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
         TagKey tagKey = ItemTags.SWORDS;
         if (itemStack.is(tagKey)) {
            return true;
         }
      }

      if (bl2) {
         Item item1 = Items.TOTEM_OF_UNDYING;
         if (itemStack.is(item1)) {
            return true;
         }
      }

      return bl3 && itemStack.isEmpty();
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

   private void methodB(Minecraft minecraft, int i) {
      if (this.intF == -1) {
         int j = methodA(minecraft.player.getInventory());
         this.intF = j;
      }

      methodA(minecraft.player.getInventory(), i);
   }

   private static int methodA(Inventory inventory) {
      return InventoryAccess.getSelectedSlot(inventory);
   }

   private static void methodA(Inventory inventory, int i) {
      InventoryAccess.setSelectedSlot(inventory, i);
   }

   private long methodC() {
      return this.methodA("Switch Delay Low", 1L, "Switch Delay High", 50L);
   }

   private long methodD() {
      return this.methodA("Place Delay Low", 50L, "Place Delay High", 100L);
   }

   private long methodE() {
      return this.methodA("Break Delay Low", 50L, "Break Delay High", 100L);
   }

   private long methodF() {
      return this.methodA("Swap Back Delay Low", 50L, "Swap Back Delay High", 100L);
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
