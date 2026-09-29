package need.clarity.mixin;

import need.clarity.modules.ModuleBase;
import need.clarity.modules.ModuleRegistry;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public class XCarryMixin {
   @Redirect(
      method = "closeContainer",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V")
   )
   private void xcarry$redirectSend(ClientPacketListener clientPacketListener, Packet<?> packet) {
      LocalPlayer localPlayer = (LocalPlayer)this;
      ModuleBase moduleBase = ModuleRegistry.getModule("XCarry");
      if (moduleBase == null || !moduleBase.isEnabled() || localPlayer.containerMenu.containerId != 0) {
         clientPacketListener.send(packet);
      }
   }
}
