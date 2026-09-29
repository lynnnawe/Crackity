package need.clarity.modules.utility.crystalhelpers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

class OptimizerPackets$OptOutAck$1 implements StreamCodec<FriendlyByteBuf, OptimizerPackets$OptOutAck> {
   public void methodA(FriendlyByteBuf friendlyByteBuf, OptimizerPackets$OptOutAck optOutAck) {
   }

   @NotNull
   public OptimizerPackets.OptimizerPackets$OptOutAck methodA(FriendlyByteBuf friendlyByteBuf) {
      return OptimizerPackets$OptOutAck.instance;
   }

   // $VF: synthetic method
   public void encode(Object object, Object object1) {
      FriendlyByteBuf friendlyByteBuf = (FriendlyByteBuf)object;
      OptimizerPackets$OptOutAck optOutAck = (OptimizerPackets$OptOutAck)object1;
      this.methodA(friendlyByteBuf, optOutAck);
   }

   // $VF: synthetic method
   @NotNull
   public Object decode(Object object) {
      FriendlyByteBuf friendlyByteBuf = (FriendlyByteBuf)object;
      return this.methodA(friendlyByteBuf);
   }
}
