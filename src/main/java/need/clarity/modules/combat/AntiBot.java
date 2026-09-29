package need.clarity.modules.combat;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import need.clarity.modules.ModuleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerSkin;

public class AntiBot extends ModuleBase {
   private static AntiBot instance;
   private static final Set<UUID> setB = ConcurrentHashMap.newKeySet();

   public AntiBot() {
      super("Anti Bot", "Combat");
      instance = this;
   }

   public static boolean methodA(LivingEntity livingEntity) {
      AntiBot antiBot = instance;
      if (antiBot == null || !antiBot.isEnabled()) {
         return false;
      } else if (!(livingEntity instanceof Player player)) {
         return false;
      } else {
         LocalPlayer localPlayer = Minecraft.getInstance().player;
         if (livingEntity == localPlayer) {
            return false;
         }

         Set set = setB;
         UUID uUID = livingEntity.getUUID();
         if (set.contains(uUID)) {
            return true;
         }

         Minecraft minecraft = Minecraft.getInstance();
         if (minecraft.level != null && minecraft.getConnection() != null) {
            ClientPacketListener clientPacketListener = minecraft.getConnection();
            UUID uUID1 = player.getUUID();
            PlayerInfo playerInfo = clientPacketListener.getPlayerInfo(uUID1);
            Boolean boolean_xxx = true;
            if (antiBot.getSetting("Not in Tab List", boolean_xxx) && playerInfo == null) {
               return true;
            }

            if (playerInfo != null) {
               Boolean boolean_x = true;
               if (antiBot.getSetting("Invalid Ping", boolean_x) && playerInfo.getLatency() == 0) {
                  return true;
               }

               Boolean boolean_xx = false;
               if (antiBot.getSetting("No Skin", boolean_xx)) {
                  try {
                     PlayerSkin playerSkin = playerInfo.getSkin();
                     if (playerSkin == null || playerSkin.body() == null) {
                        return true;
                     }
                  } catch (Exception exception) {
                     return true;
                  }
               }
            }

            Boolean boolean_x = true;
            return antiBot.getSetting("Invalid UUID", boolean_x) && (int)(livingEntity.getUUID().getMostSignificantBits() >> 12 & 15L) != 4;
         } else {
            return false;
         }
      }
   }

   @Override
   public void onDisable() {
   }
}
