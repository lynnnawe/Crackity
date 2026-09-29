package need.clarity.modules.combat;

import java.util.Random;
import need.clarity.EventStartAttack;
import need.clarity.PerAttackEvents;
import need.clarity.modules.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;

public class Triggerbot extends ModuleBase {
   private final Minecraft minecraft;
   private final Random random;
   private boolean flagC;
   private long longD;

   public Triggerbot() {
      super("Triggerbot", "Combat");
      Minecraft minecraftx = Minecraft.getInstance();
      this.minecraft = minecraftx;
      Random randomx = new Random();
      this.random = randomx;
      this.flagC = false;
      this.longD = -1L;
   }

   @Override
   public void onDisable() {
      this.flagC = false;
      this.longD = -1L;
   }

   @Override
   public void onTick() {
      if (this.minecraft.player != null) {
         if (this.minecraft.level != null) {
            Boolean boolean_xxxx = true;
            if (!this.getSetting("Not in GUI", boolean_xxxx) || this.minecraft.screen == null) {
               Boolean boolean_x = true;
               if (!this.getSetting("Not While Using", boolean_x) || !this.minecraft.player.isUsingItem()) {
                  HitResult hitResult = this.minecraft.hitResult;
                  if (hitResult != null) {
                     Type type = hitResult.getType();
                     Type type2 = Type.ENTITY;
                     if (type == type2) {
                        if (((EntityHitResult)hitResult).getEntity() instanceof LivingEntity livingEntity) {
                           LocalPlayer localPlayer = this.minecraft.player;
                           if (livingEntity != localPlayer && livingEntity.isAlive()) {
                              Boolean boolean_xx = false;
                              if (this.getSetting("Only Weapon", boolean_xx)) {
                                 ItemStack itemStack = this.minecraft.player.getMainHandItem();
                                 if (!this.methodA(itemStack)) {
                                    this.methodB();
                                    return;
                                 }
                              }

                              Boolean boolean_xxx = true;
                              if (this.getSetting("Check Shielding", boolean_xxx) && livingEntity.isBlocking()) {
                                 this.methodB();
                                 return;
                              }

                              Float float1 = 0.9F;
                              float f = this.getSetting("Cooldown Threshold_lo", float1);
                              Float float2 = 1.0F;
                              float g = this.getSetting("Cooldown Threshold_hi", float2);
                              float p = this.random.nextFloat();
                              float w = g - f;
                              float t = Math.max(0.0F, w);
                              float q = p * t;
                              float h = f + q;
                              if (this.minecraft.player.getAttackStrengthScale(0.0F) < h) {
                                 this.methodB();
                                 return;
                              }

                              Boolean boolean_xxxx = true;
                              if (this.getSetting("Prioritize Criticals", boolean_xxxx) && !this.methodA()) {
                                 this.methodB();
                                 return;
                              }

                              Integer integer = 0;
                              int i = this.getSetting("Extra Delay_lo", integer);
                              Integer integer1 = 0;
                              int j = this.getSetting("Extra Delay_hi", integer1);
                              long m = System.currentTimeMillis();
                              if (!this.flagC) {
                                 long o;
                                 if (j > i) {
                                    float k = this.random.nextFloat();
                                    float r = j - i;
                                    long n = (long)(k * r);
                                    long u = i;
                                    o = n + u;
                                 } else {
                                    o = i;
                                 }

                                 long l = o;
                                 long s = m + l;
                                 this.longD = s;
                                 this.flagC = true;
                              }

                              long v = this.longD;
                              if (m < v) {
                                 return;
                              }

                              HitResult hitResult1 = this.minecraft.hitResult;
                              if (hitResult1 != null) {
                                 Type type1 = hitResult1.getType();
                                 Type type3 = Type.ENTITY;
                                 if (type1 == type3) {
                                    Entity entity = ((EntityHitResult)hitResult1).getEntity();
                                    if (entity instanceof LivingEntity && entity.isAlive()) {
                                       LocalPlayer localPlayer1 = this.minecraft.player;
                                       if (entity != localPlayer1) {
                                          this.methodA(entity);
                                       }
                                    }
                                 }
                              }

                              this.flagC = false;
                              this.longD = -1L;
                              return;
                           }

                           this.methodB();
                           return;
                        }

                        this.methodB();
                        return;
                     }
                  }

                  this.methodB();
               }
            }
         }
      }
   }

   private boolean methodA() {
      if (this.minecraft.player == null) {
         return false;
      }

      if (this.minecraft.player.onGround()) {
         return true;
      }

      if (this.minecraft.player.isInWater() || this.minecraft.player.isInLava()) {
         return false;
      }

      if (this.minecraft.player.isPassenger()) {
         return false;
      }

      boolean bl;
      if (this.minecraft.player.getDeltaMovement().y <= -0.1) {
         bl = true;
      } else {
         bl = false;
      }

      return bl;
   }

   private boolean methodA(ItemStack itemStack) {
      if (itemStack.isEmpty()) {
         return false;
      }

      Item item = itemStack.getItem();
      if (item instanceof TridentItem) {
         return true;
      }

      BuiltInRegistries.ITEM.getKey(item).toString();
      TagKey tagKey = ItemTags.SWORDS;
      if (itemStack.is(tagKey)) {
         return true;
      }

      TagKey tagKey1 = ItemTags.AXES;
      if (itemStack.is(tagKey1)) {
         return true;
      }

      TagKey tagKey2 = ItemTags.MACE_ENCHANTABLE;
      return itemStack.is(tagKey2);
   }

   private void methodA(Entity entity) {
      if (this.minecraft.gameMode != null) {
         if (this.minecraft.player != null) {
            LocalPlayer localPlayer2 = this.minecraft.player;
            EventStartAttack eventStartAttack = new EventStartAttack(localPlayer2, entity);
            PerAttackEvents.fire(eventStartAttack);
            MultiPlayerGameMode multiPlayerGameMode = this.minecraft.gameMode;
            LocalPlayer localPlayer1 = this.minecraft.player;
            multiPlayerGameMode.attack(localPlayer1, entity);
            LocalPlayer localPlayer = this.minecraft.player;
            InteractionHand interactionHand = InteractionHand.MAIN_HAND;
            localPlayer.swing(interactionHand);
         }
      }
   }

   private void methodB() {
      this.flagC = false;
      this.longD = -1L;
   }
}
