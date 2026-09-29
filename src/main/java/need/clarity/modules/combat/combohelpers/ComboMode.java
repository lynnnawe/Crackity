package need.clarity.modules.combat.combohelpers;

import need.clarity.EventStartAttack;
import need.clarity.modules.combat.SprintReset;
import need.clarity.modules.movement.MoveFix$EventMovementInput;
import net.minecraft.client.Minecraft;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public abstract class ComboMode {
   protected static final Minecraft minecraft = Minecraft.getInstance();
   protected int intB = -1;

   public void onEnable() {
      this.intB = -1;
   }

   public void onDisable() {
      this.intB = -1;
   }

   public void methodA(EventStartAttack eventStartAttack) {
      if (minecraft.player != null) {
         if (minecraft.level != null) {
            if (minecraft.screen == null) {
               if (this.methodB(eventStartAttack)) {
                  if (ComboHelper.sprintResetA != null) {
                     SprintReset sprintReset = ComboHelper.sprintResetA;
                     Boolean boolean_ = false;
                     if (sprintReset.getSetting("Only Sword", boolean_) && !this.methodA()) {
                        return;
                     }
                  }

                  if (minecraft.hitResult != null) {
                     HitResult hitResult = minecraft.hitResult;
                     if (hitResult instanceof EntityHitResult) {
                        if (((EntityHitResult)hitResult).getEntity() instanceof LivingEntity livingEntity) {
                           if (this.methodA(livingEntity)) {
                              int i = minecraft.player.tickCount;
                              this.intB = i;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void methodA(MoveFix$EventMovementInput eventMovementInput) {
      if (minecraft.player != null) {
         if (minecraft.level != null) {
            if (minecraft.screen == null) {
               if (!minecraft.player.isDeadOrDying()) {
                  if (this.intB != -1) {
                     this.methodB(eventMovementInput);
                  }
               }
            }
         }
      }
   }

   protected boolean methodB(EventStartAttack eventStartAttack) {
      return true;
   }

   protected abstract void methodB(MoveFix$EventMovementInput eventMovementInput);

   protected boolean methodA(LivingEntity livingEntity) {
      if (livingEntity == null) {
         return false;
      }

      boolean bl;
      if (livingEntity.isAlive() && !livingEntity.isRemoved() && !livingEntity.isDeadOrDying()) {
         bl = true;
      } else {
         bl = false;
      }

      return bl;
   }

   private boolean methodA() {
      if (minecraft.player == null) {
         return false;
      }

      ItemStack itemStack = minecraft.player.getMainHandItem();
      if (itemStack.isEmpty()) {
         return false;
      }

      if (itemStack.getItem() instanceof TridentItem) {
         return true;
      }

      TagKey tagKey = ItemTags.SWORDS;
      return itemStack.is(tagKey);
   }
}
