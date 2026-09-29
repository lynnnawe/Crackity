package need.clarity.modules.movement;

import need.clarity.modules.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.Pos;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.StatusOnly;

public class NoFall extends ModuleBase {
   private boolean flagA = false;

   public NoFall() {
      super("NoFall", "Movement");
   }

   @Override
   public void onEnable() {
      this.flagA = false;
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         if (minecraft.level != null) {
            if (!minecraft.player.getAbilities().invulnerable) {
               String string = this.getSetting("Mode", "Normal");
               if ("Grim".equals(string)) {
                  this.methodB(minecraft);
               } else {
                  this.methodA(minecraft);
               }

               boolean bl = minecraft.player.onGround();
               this.flagA = bl;
            }
         }
      }
   }

   private void methodA(Minecraft minecraft) {
      float f = (float)minecraft.player.fallDistance;
      if (!(f <= 0.0F)) {
         Boolean boolean_ = true;
         if (this.getSetting("Only Lethal", boolean_)) {
            float g = f - 3.0F;
            float h = minecraft.player.getHealth();
            if (g < h) {
               return;
            }
         }

         ClientPacketListener clientPacketListener = minecraft.player.connection;
         boolean bl = minecraft.player.horizontalCollision;
         StatusOnly statusOnly = new StatusOnly(true, bl);
         clientPacketListener.send(statusOnly);
      }
   }

   private void methodB(Minecraft minecraft) {
      boolean bl = minecraft.player.onGround();
      float f = (float)minecraft.player.fallDistance;
      if (!this.flagA) {
         if (bl) {
            if (f > 0.0F) {
               Boolean boolean1 = true;
               if (this.getSetting("Only Lethal", boolean1)) {
                  float g = f - 3.0F;
                  float i = minecraft.player.getHealth();
                  if (g < i) {
                     return;
                  }
               }

               double d = minecraft.player.getX();
               double e = minecraft.player.getY();
               double h = minecraft.player.getZ();
               ClientPacketListener clientPacketListener = minecraft.player.connection;
               double j = e + 1.0E-7;
               boolean bl1 = minecraft.player.horizontalCollision;
               Pos pos = new Pos(d, j, h, false, bl1);
               clientPacketListener.send(pos);
               minecraft.player.fallDistance = 0.0;
            }
         }
      }
   }
}
