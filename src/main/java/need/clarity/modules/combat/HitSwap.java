package need.clarity.modules.combat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Optional;
import java.util.Random;
import need.clarity.EventStartAttack;
import need.clarity.PerAttackEvents;
import need.clarity.config.ClarityConfig;
import need.clarity.modules.ModuleBase;
import need.clarity.util.InventoryAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public class HitSwap extends ModuleBase {
   private HitSwap$Phase phaseA;
   private long longB;
   private int intC;
   private int intD;
   private LivingEntity livingEntityE;
   private final Random random;

   public HitSwap() {
      super("Hit Swap", "Combat");
      HitSwap$Phase phase = HitSwap$Phase.IDLE;
      this.phaseA = phase;
      this.longB = 0L;
      this.intC = -1;
      this.intD = -1;
      this.livingEntityE = null;
      Random randomx = new Random();
      this.random = randomx;
      PerAttackEvents.register(this::methodA);
   }

   @Override
   public void onEnable() {
      Minecraft minecraft = Minecraft.getInstance();
      this.methodA(minecraft);
   }

   @Override
   public void onDisable() {
      Minecraft minecraft = Minecraft.getInstance();
      this.methodA(minecraft);
   }

   private void methodA(Minecraft minecraft) {
      if (minecraft.player != null && this.intC >= 0) {
         Inventory inventory = minecraft.player.getInventory();
         int i = this.intC;
         InventoryAccess.setSelectedSlot(inventory, i);
      }

      HitSwap$Phase phase = HitSwap$Phase.IDLE;
      this.phaseA = phase;
      this.longB = 0L;
      this.intC = -1;
      this.intD = -1;
      this.livingEntityE = null;
   }

   private void methodA(EventStartAttack eventStartAttack) {
      if (this.isEnabled()) {
         HitSwap$Phase phase1 = this.phaseA;
         HitSwap$Phase phase = HitSwap$Phase.IDLE;
         if (phase1 == phase) {
            Entity entity = eventStartAttack.getTarget();
            if (entity != null) {
               if (this.methodA(entity)) {
                  if (entity instanceof LivingEntity livingEntity) {
                     Minecraft minecraft = Minecraft.getInstance();
                     if (minecraft.player != null) {
                        Boolean boolean1 = false;
                        if (!this.getSetting("Only Weapon", boolean1) || methodB(minecraft.player)) {
                           Boolean boolean2 = false;
                           boolean bl3;
                           if (this.getSetting("Stun Slam", boolean2) && methodA(livingEntity)) {
                              bl3 = true;
                           } else {
                              bl3 = false;
                           }

                           boolean bl;
                           boolean bl2;
                           label65: {
                              bl = bl3;
                              if (!bl) {
                                 Boolean boolean3 = false;
                                 if (this.getSetting("Check Shield", boolean3)) {
                                    bl2 = true;
                                    break label65;
                                 }
                              }

                              bl2 = false;
                           }

                           if (!bl2 || !methodA(livingEntity)) {
                              int l = InventoryAccess.getSelectedSlot(minecraft.player.getInventory());
                              this.intC = l;
                              LocalPlayer localPlayer = minecraft.player;
                              boolean bl1 = this.methodA(localPlayer);
                              Inventory inventory1 = minecraft.player.getInventory();
                              int m = this.methodA(inventory1, bl1);
                              this.intD = m;
                              if (this.intD >= 0) {
                                 if (bl) {
                                    int i = methodA(minecraft.player.getInventory());
                                    if (i >= 0) {
                                       this.livingEntityE = livingEntity;
                                       int n = this.intC;
                                       if (i != n) {
                                          InventoryAccess.setSelectedSlot(minecraft.player.getInventory(), i);
                                       }

                                       HitSwap$Phase phase2 = HitSwap$Phase.SLAM_WAIT_TO_FIRE;
                                       this.methodA(phase2);
                                    }
                                 } else {
                                    Inventory inventory = minecraft.player.getInventory();
                                    int o = this.intD;
                                    InventoryAccess.setSelectedSlot(inventory, o);
                                    Integer integer = 0;
                                    int j = this.getSetting("Swap Delay_lo", integer);
                                    Integer integer1 = 0;
                                    int k = this.getSetting("Swap Delay_hi", integer1);
                                    if (j == 0 && k == 0) {
                                       this.methodA();
                                    } else {
                                       HitSwap$Phase phase3 = HitSwap$Phase.WAIT_SWAP;
                                       this.methodA(phase3);
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

   @Override
   public void onTick() {
      if (this.isEnabled()) {
         Minecraft minecraft = Minecraft.getInstance();
         if (minecraft.player != null) {
            long l = System.currentTimeMillis();
            Integer integer = 50;
            int i = this.getSetting("Step Delay", integer);
            switch (this.phaseA) {
               case WAIT_SWAP:
                  Integer integer5 = 0;
                  int q = this.getSetting("Swap Delay_lo", integer5);
                  Integer integer6 = 0;
                  int r = this.getSetting("Swap Delay_hi", integer6);
                  long z = this.longB;
                  long s = l - z;
                  long ba = this.methodA(q, r);
                  if (s >= ba) {
                     this.methodA();
                     return;
                  }

                  return;
               case WAIT_RESTORE:
                  Integer integer3 = 100;
                  int n = this.getSetting("Swap Back Delay_lo", integer3);
                  Integer integer4 = 150;
                  int o = this.getSetting("Swap Back Delay_hi", integer4);
                  long x = this.longB;
                  long p = l - x;
                  long y = this.methodA(n, o);
                  if (p >= y) {
                     this.methodB(minecraft);
                     return;
                  }

                  return;
               case SLAM_WAIT_TO_FIRE:
                  long bb = this.longB;
                  long t = l - bb;
                  long bc = i;
                  if (t >= bc) {
                     if (this.intD >= 0) {
                        Inventory inventory = minecraft.player.getInventory();
                        int u = this.intD;
                        InventoryAccess.setSelectedSlot(inventory, u);
                     }

                     if (this.livingEntityE != null && this.livingEntityE.isAlive()) {
                        LivingEntity livingEntity = this.livingEntityE;
                        methodA(minecraft, livingEntity);
                     }

                     this.livingEntityE = null;
                     Boolean boolean_ = true;
                     if (this.getSetting("Swap Back", boolean_) && this.intC >= 0) {
                        HitSwap$Phase phase = HitSwap$Phase.SLAM_WAIT_RESTORE;
                        this.methodA(phase);
                        return;
                     }

                     this.intC = -1;
                     HitSwap$Phase phase1 = HitSwap$Phase.IDLE;
                     this.phaseA = phase1;
                     return;
                  }

                  return;
               case SLAM_WAIT_RESTORE:
                  Integer integer1 = 100;
                  int j = this.getSetting("Swap Back Delay_lo", integer1);
                  Integer integer2 = 150;
                  int k = this.getSetting("Swap Back Delay_hi", integer2);
                  long v = this.longB;
                  long m = l - v;
                  long w = this.methodA(j, k);
                  if (m >= w) {
                     this.methodB(minecraft);
                     return;
                  }

                  return;
            }
         }
      }
   }

   private void methodA(HitSwap$Phase phase) {
      this.phaseA = phase;
      long l = System.currentTimeMillis();
      this.longB = l;
   }

   private void methodA() {
      Boolean boolean_ = true;
      if (this.getSetting("Swap Back", boolean_) && this.intC >= 0) {
         HitSwap$Phase phase = HitSwap$Phase.WAIT_RESTORE;
         this.methodA(phase);
      } else {
         this.intC = -1;
         HitSwap$Phase phase1 = HitSwap$Phase.IDLE;
         this.phaseA = phase1;
      }
   }

   private void methodB(Minecraft minecraft) {
      if (this.intC >= 0 && minecraft.player != null) {
         Inventory inventory = minecraft.player.getInventory();
         int i = this.intC;
         InventoryAccess.setSelectedSlot(inventory, i);
         this.intC = -1;
      }

      HitSwap$Phase phase = HitSwap$Phase.IDLE;
      this.phaseA = phase;
   }

   private static void methodA(Minecraft minecraft, LivingEntity livingEntity) {
      if (minecraft.player != null) {
         MultiPlayerGameMode multiPlayerGameMode = minecraft.gameMode;
         LocalPlayer localPlayer1 = minecraft.player;
         multiPlayerGameMode.attack(localPlayer1, livingEntity);
         LocalPlayer localPlayer = minecraft.player;
         InteractionHand interactionHand = InteractionHand.MAIN_HAND;
         localPlayer.swing(interactionHand);
         minecraft.player.resetAttackStrengthTicker();
      }
   }

   private static boolean methodA(LivingEntity livingEntity) {
      if (!livingEntity.isUsingItem()) {
         return false;
      }

      ItemStack itemStack = livingEntity.getUseItem();
      if (!itemStack.isEmpty()) {
         Item item = Items.SHIELD;
         if (itemStack.is(item)) {
            boolean bl;
            if (livingEntity.getTicksUsingItem() >= 5) {
               bl = true;
            } else {
               bl = false;
            }

            return bl;
         }
      }

      return false;
   }

   private boolean methodA(LocalPlayer localPlayer) {
      String string = this.getSetting("Mode", "Breach");
      byte b = -1;
      int i = string.hashCode();
      switch (i) {
         case -1077596984:
            if (string.equals("Density")) {
               b = 0;
            }
            break;
         case -505546721:
            if (string.equals("Dynamic")) {
               b = 1;
            }
      }

      byte c = b;

      return switch (c) {
         case 0, 1 -> {
            double d = localPlayer.getDeltaMovement().y;
            Double double1 = -0.25;
            double e = this.getSetting("Dynamic Threshold", double1);
            if (d < e) {
               yield true;
            } else {
               yield false;
            }
         }
         default -> false;
      };
   }

   private int methodA(Inventory inventory, boolean bl) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.level == null) {
         return -1;
      }

      String string = this.getSetting("Mode", "Breach");
      if ("Any".equals(string)) {
         for (int i = 0; i < 9; i++) {
            int l = i;
            ItemStack itemStack = inventory.getItem(l);
            if (!itemStack.isEmpty()) {
               Item item = Items.MACE;
               if (itemStack.is(item)) {
                  return i;
               }
            }
         }

         return -1;
      } else {
         Registry registry;
         try {
            RegistryAccess registryAccess = minecraft.level.registryAccess();
            ResourceKey resourceKey = Registries.ENCHANTMENT;
            registry = registryAccess.lookupOrThrow(resourceKey);
         } catch (Exception exception) {
            return -1;
         }

         Holder holder = methodA(registry, bl);
         int j = -1;

         for (int k = 0; k < 9; k++) {
            int m = k;
            ItemStack itemStack1 = inventory.getItem(m);
            if (!itemStack1.isEmpty()) {
               Item item1 = Items.MACE;
               if (itemStack1.is(item1)) {
                  if (holder != null && EnchantmentHelper.getItemEnchantmentLevel(holder, itemStack1) > 0) {
                     return k;
                  }

                  if (j < 0) {
                     j = k;
                  }
               }
            }
         }

         return j;
      }
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

   private static Holder<Enchantment> methodA(Registry<Enchantment> registry, boolean bl) {
      try {
         Optional optional;
         if (bl) {
            ResourceKey resourceKey = Enchantments.DENSITY;
            optional = registry.get(resourceKey);
         } else {
            ResourceKey resourceKey1 = Enchantments.BREACH;
            optional = registry.get(resourceKey1);
         }

         return (Holder<Enchantment>)optional.orElse(null);
      } catch (Exception exception) {
         return null;
      }
   }

   private static boolean methodB(LocalPlayer localPlayer) {
      ItemStack itemStack = localPlayer.getMainHandItem();
      if (itemStack.isEmpty()) {
         return false;
      }

      TagKey tagKey = ItemTags.SWORDS;
      if (!itemStack.is(tagKey)) {
         TagKey tagKey1 = ItemTags.AXES;
         if (!itemStack.is(tagKey1)) {
            TagKey tagKey2 = ItemTags.MACE_ENCHANTABLE;
            if (!itemStack.is(tagKey2) && !(itemStack.getItem() instanceof TridentItem)) {
               return false;
            }
         }
      }

      return true;
   }

   private boolean methodA(Entity entity) {
      boolean bl = this.methodA("Players");
      boolean bl1 = this.methodA("Mobs");
      boolean bl2 = this.methodA("Entities");
      if (entity instanceof Player) {
         return bl;
      } else if (entity instanceof Mob) {
         return bl1;
      } else {
         return entity instanceof LivingEntity ? bl2 : bl2;
      }
   }

   private boolean methodA(String string) {
      try {
         JsonObject jsonObject = ClarityConfig.getCurrent().getAsJsonObject("Hit Swap");
         if (jsonObject == null) {
            return true;
         }

         JsonObject jsonObject1 = jsonObject.getAsJsonObject("values");
         if (jsonObject1 == null) {
            return true;
         }

         JsonElement jsonElement = jsonObject1.get("Targets");
         if (jsonElement != null && jsonElement.isJsonArray()) {
            for (JsonElement jsonElement1 : jsonElement.getAsJsonArray()) {
               if (jsonElement1.isJsonPrimitive() && jsonElement1.getAsString().equals(string)) {
                  return true;
               }
            }

            return false;
         } else {
            return true;
         }
      } catch (Exception exception) {
         return true;
      }
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
}
