package need.clarity.modules.movement;

import need.clarity.modules.ModuleBase;
import net.minecraft.client.Minecraft;

public class AntiHunger extends ModuleBase {
   public static AntiHunger instance;

   public AntiHunger() {
      super("Anti Hunger", "Movement");
      instance = this;
   }

   public boolean methodA() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player == null) {
         return false;
      } else if (!minecraft.player.onGround()) {
         return false;
      } else {
         return minecraft.player.fallDistance > 0.5 ? false : minecraft.gameMode == null || !minecraft.gameMode.isDestroying();
      }
   }
}
