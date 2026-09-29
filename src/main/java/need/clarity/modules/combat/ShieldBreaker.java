package need.clarity.modules.combat;

import java.util.Random;
import java.util.WeakHashMap;
import need.clarity.modules.ModuleBase;
import need.clarity.util.InventoryAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class ShieldBreaker extends ModuleBase {
   private static final WeakHashMap<LivingEntity, Long> weakHashMapA;
   private ShieldBreaker$Phase phaseB;
   private long longC;
   private int intD;
   private final Random random;

   public ShieldBreaker() {
      super("Shield Breaker", "Combat");
      ShieldBreaker$Phase phase = ShieldBreaker$Phase.IDLE;
      this.phaseB = phase;
      this.longC = 0L;
      this.intD = -1;
      Random randomx = new Random();
      this.random = randomx;
   }

   @Override
   public void onEnable() {
      Minecraft minecraft = Minecraft.getInstance();
      this.methodA(minecraft);
   }

   @Override
   public void onDisable() {
      Minecraft minecraft = Minecraft.getInstance();
      if (this.intD != -1 && minecraft.player != null) {
         Inventory inventory = minecraft.player.getInventory();
         int i = this.intD;
         methodA(inventory, i);
      }

      this.methodA(minecraft);
   }

   private void methodA(Minecraft minecraft) {
      if (this.intD != -1 && minecraft.player != null) {
         Inventory inventory = minecraft.player.getInventory();
         int i = this.intD;
         methodA(inventory, i);
      }

      ShieldBreaker$Phase phase = ShieldBreaker$Phase.IDLE;
      this.phaseB = phase;
      this.longC = 0L;
      this.intD = -1;
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (this.isEnabled()) {
         LocalPlayer localPlayer = minecraft.player;
         if (localPlayer != null) {
            if (minecraft.level != null) {
               Boolean boolean1 = true;
               if (!this.getSetting("Not in GUI", boolean1) || minecraft.screen == null) {
                  Boolean boolean2 = true;
                  if (this.getSetting("Not While Using", boolean2) && localPlayer.isUsingItem()) {
                     ShieldBreaker$Phase phase = this.phaseB;
                     ShieldBreaker$Phase phase3 = ShieldBreaker$Phase.IDLE;
                     if (phase != phase3) {
                        if (this.intD != -1) {
                           Inventory inventory = localPlayer.getInventory();
                           int q = this.intD;
                           methodA(inventory, q);
                           this.intD = -1;
                        }

                        ShieldBreaker$Phase phase4 = ShieldBreaker$Phase.IDLE;
                        this.phaseB = phase4;
                     }
                  } else {
                     String string = this.getSetting("Mode", "Normal");
                     Boolean boolean3 = false;
                     boolean bl = this.getSetting("Swap Back", boolean3);
                     Integer integer = 50;
                     int i = this.getSetting("Delay_lo", integer);
                     Integer integer1 = 75;
                     int j = this.getSetting("Delay_hi", integer1);
                     Integer integer2 = 50;
                     int k = this.getSetting("Swap Back Delay_lo", integer2);
                     Integer integer3 = 75;
                     int l = this.getSetting("Swap Back Delay_hi", integer3);
                     long m = System.currentTimeMillis();
                     ShieldBreaker$Phase phase1 = this.phaseB;
                     ShieldBreaker$Phase phase5 = ShieldBreaker$Phase.WAITING_TO_RESTORE;
                     if (phase1 == phase5) {
                        long x = this.longC;
                        long p = m - x;
                        long y = this.methodA(k, l);
                        if (p >= y) {
                           if (this.intD != -1) {
                              Inventory inventory2 = localPlayer.getInventory();
                              int u = this.intD;
                              methodA(inventory2, u);
                              this.intD = -1;
                           }

                           ShieldBreaker$Phase phase9 = ShieldBreaker$Phase.IDLE;
                           this.phaseB = phase9;
                        }
                     } else {
                        ShieldBreaker$Phase phase2 = this.phaseB;
                        ShieldBreaker$Phase phase6 = ShieldBreaker$Phase.WAITING_TO_ATTACK;
                        if (phase2 == phase6) {
                           long v = this.longC;
                           long n = m - v;
                           long w = this.methodA(i, j);
                           if (n >= w) {
                              LivingEntity livingEntity = methodB(minecraft);
                              if (livingEntity != null && methodA(livingEntity)) {
                                 this.methodA(minecraft, localPlayer, livingEntity, string, bl);
                              } else {
                                 if (this.intD != -1) {
                                    Inventory inventory1 = localPlayer.getInventory();
                                    int r = this.intD;
                                    methodA(inventory1, r);
                                    this.intD = -1;
                                 }

                                 ShieldBreaker$Phase phase7 = ShieldBreaker$Phase.IDLE;
                                 this.phaseB = phase7;
                              }
                           }
                        } else {
                           LivingEntity livingEntity1 = methodB(minecraft);
                           if (livingEntity1 != null) {
                              if (methodA(livingEntity1)) {
                                 int o = methodA(localPlayer.getInventory());
                                 if (o >= 0) {
                                    int s = methodB(localPlayer.getInventory());
                                    this.intD = s;
                                    int t = this.intD;
                                    if (o == t) {
                                       this.intD = -1;
                                       this.methodA(minecraft, localPlayer, livingEntity1, string, bl);
                                    } else {
                                       methodA(localPlayer.getInventory(), o);
                                       ShieldBreaker$Phase phase8 = ShieldBreaker$Phase.WAITING_TO_ATTACK;
                                       this.phaseB = phase8;
                                       this.longC = m;
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
   }

   private void methodA(Minecraft minecraft, LocalPlayer localPlayer, LivingEntity livingEntity, String string, boolean bl) {
      if (methodA(localPlayer.getInventory()) < 0) {
         if (this.intD != -1) {
            Inventory inventory = localPlayer.getInventory();
            int i = this.intD;
            methodA(inventory, i);
            this.intD = -1;
         }

         ShieldBreaker$Phase phase = ShieldBreaker$Phase.IDLE;
         this.phaseB = phase;
      } else {
         minecraft.gameMode.attack(localPlayer, livingEntity);
         InteractionHand interactionHand = InteractionHand.MAIN_HAND;
         localPlayer.swing(interactionHand);
         localPlayer.resetAttackStrengthTicker();
         if ("Stun".equals(string)) {
            minecraft.gameMode.attack(localPlayer, livingEntity);
            InteractionHand interactionHand1 = InteractionHand.MAIN_HAND;
            localPlayer.swing(interactionHand1);
            localPlayer.resetAttackStrengthTicker();
         }

         methodB(livingEntity);
         if (bl && this.intD != -1) {
            ShieldBreaker$Phase phase1 = ShieldBreaker$Phase.WAITING_TO_RESTORE;
            this.phaseB = phase1;
            long l = System.currentTimeMillis();
            this.longC = l;
         } else {
            this.intD = -1;
            ShieldBreaker$Phase phase2 = ShieldBreaker$Phase.IDLE;
            this.phaseB = phase2;
         }
      }
   }

   public static boolean methodA(LivingEntity livingEntity) {
      if (livingEntity == null) {
         return false;
      }

      Long long_ = weakHashMapA.get(livingEntity);
      if (long_ != null) {
         long l = System.currentTimeMillis();
         long m = long_;
         if (l - m < 600L) {
            if (livingEntity.isUsingItem()) {
               return false;
            }

            weakHashMapA.remove(livingEntity);
         } else {
            weakHashMapA.remove(livingEntity);
         }
      }

      return livingEntity.isBlocking();
   }

   public static void methodB(LivingEntity livingEntity) {
      WeakHashMap weakHashMap = weakHashMapA;
      Long long_ = System.currentTimeMillis();
      weakHashMap.put(livingEntity, long_);
   }

   private static LivingEntity methodB(Minecraft minecraft) {
      Entity entity = minecraft.crosshairPickEntity;
      return entity instanceof LivingEntity ? (LivingEntity)entity : null;
   }

   private static int methodA(Inventory inventory) {
      for (int i = 0; i < 9; i++) {
         int j = i;
         ItemStack itemStack = inventory.getItem(j);
         if (!itemStack.isEmpty()) {
            TagKey tagKey = ItemTags.AXES;
            if (itemStack.is(tagKey)) {
               return i;
            }
         }
      }

      return -1;
   }

   private static int methodB(Inventory inventory) {
      return InventoryAccess.getSelectedSlot(inventory);
   }

   private static void methodA(Inventory inventory, int i) {
      InventoryAccess.setSelectedSlot(inventory, i);
   }

   private int methodA(int i, int j) {
      if (j <= i) {
         return i;
      }

      Random randomx = this.random;
      int l = j - i + 1;
      int k = randomx.nextInt(l);
      return i + k;
   }

   static {
      WeakHashMap weakHashMap = new WeakHashMap();
      weakHashMapA = weakHashMap;
   }
}
