package need.clarity.modules.utility.crystalhelpers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

class OptimizerPackets$OptOut$1 implements StreamCodec<FriendlyByteBuf, OptimizerPackets$OptOut> {
   public void methodA(FriendlyByteBuf friendlyByteBuf, OptimizerPackets$OptOut optOut) {
   }

   @NotNull
   public OptimizerPackets.OptimizerPackets$OptOut methodA(FriendlyByteBuf friendlyByteBuf) {
      return OptimizerPackets$OptOut.instance;
   }

   // $VF: synthetic method
   public void encode(Object object, Object object1) {
      FriendlyByteBuf friendlyByteBuf = (FriendlyByteBuf)object;
      OptimizerPackets$OptOut optOut = (OptimizerPackets$OptOut)object1;
      this.methodA(friendlyByteBuf, optOut);
   }

   // $VF: synthetic method
   @NotNull
   public Object decode(Object object) {
      FriendlyByteBuf friendlyByteBuf = (FriendlyByteBuf)object;
      return this.methodA(friendlyByteBuf);
   }
}
