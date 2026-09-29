package need.clarity.mixin;

import need.clarity.modules.utility.CrystalOptimizer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundAttackPacket;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class CrystalOptimizerMixin {
   @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"))
   private void clarity$onPacketSend(Packet<?> packet, CallbackInfo callbackInfo) {
      if (packet instanceof ServerboundAttackPacket serverboundAttackPacket) {
         ServerboundAttackPacket serverboundAttackPacket1 = serverboundAttackPacket;

         try {
            k = serverboundAttackPacket1.entityId();
         } catch (Throwable throwable) {
            throw new MatchException(throwable.toString(), throwable);
         }

         int j = k;
         int i = j;
         if (CrystalOptimizer.methodA()) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
               if (minecraft.level.getEntity(i) instanceof EndCrystal endCrystal) {
                  LocalPlayer localPlayer = minecraft.player;
                  if (localPlayer != null) {
                     if (this.clarity$canDestroyCrystal(localPlayer)) {
                        this.clarity$destroyCrystal(endCrystal);
                        this.clarity$retargetCrosshair(minecraft, endCrystal);
                     }
                  }
               }
            }
         }
      }
   }

   @Unique
   private boolean clarity$canDestroyCrystal(LocalPlayer localPlayer) {
      double d = localPlayer.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
      d += this.clarity$getWeaponDamage(localPlayer.getMainHandItem());
      MobEffectInstance mobEffectInstance = localPlayer.getEffect(MobEffects.STRENGTH);
      if (mobEffectInstance != null) {
         d += 3.0 * (mobEffectInstance.getAmplifier() + 1);
      }

      MobEffectInstance mobEffectInstance1 = localPlayer.getEffect(MobEffects.WEAKNESS);
      if (mobEffectInstance1 != null) {
         d -= 4.0 * (mobEffectInstance1.getAmplifier() + 1);
      }

      return d > 0.0;
   }

   @Unique
   private double clarity$getWeaponDamage(ItemStack itemStack) {
      if (itemStack.isEmpty()) {
         return 0.0;
      }

      double[] d = new double[]{0.0};
      itemStack.forEachModifier(EquipmentSlot.MAINHAND, (holder, attributeModifier) -> {
         if (Attributes.ATTACK_DAMAGE.equals(holder)) {
            d[0] += attributeModifier.amount();
         }
      });
      return d[0];
   }

   @Unique
   private void clarity$destroyCrystal(EndCrystal endCrystal) {
      endCrystal.remove(RemovalReason.KILLED);
      endCrystal.gameEvent(GameEvent.ENTITY_DIE);
   }

   @Unique
   private void clarity$retargetCrosshair(Minecraft minecraft, EndCrystal endCrystal) {
      LocalPlayer localPlayer = minecraft.player;
      if (localPlayer != null && minecraft.hitResult != null && minecraft.crosshairPickEntity == endCrystal) {
         HitResult hitResult = localPlayer.pick(localPlayer.blockInteractionRange(), 1.0F, false);
         minecraft.crosshairPickEntity = null;
         minecraft.hitResult = hitResult;
      }
   }
}
