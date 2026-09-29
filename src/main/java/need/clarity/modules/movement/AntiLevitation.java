package need.clarity.modules.movement;

import need.clarity.modules.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffects;

public class AntiLevitation extends ModuleBase {
   public AntiLevitation() {
      super("Anti Levitation", "Movement");
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         LocalPlayer localPlayer = minecraft.player;
         Holder holder = MobEffects.LEVITATION;
         localPlayer.removeEffect(holder);
      }
   }
}
