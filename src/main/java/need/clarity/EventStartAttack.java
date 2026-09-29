package need.clarity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class EventStartAttack {
   private final Player player;
   private final Entity target;

   public EventStartAttack(Player player, Entity entity) {
      this.player = playerx;
      this.target = entity;
   }

   public Player getPlayer() {
      return this.player;
   }

   public Entity getTarget() {
      return this.target;
   }
}
