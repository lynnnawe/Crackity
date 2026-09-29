package need.clarity.modules.utility.crystalhelpers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

class OptimizerPackets$Challenge$1 implements StreamCodec<FriendlyByteBuf, OptimizerPackets$Challenge> {
   public void methodA(FriendlyByteBuf friendlyByteBuf, OptimizerPackets$Challenge challenge) {
      int i = challenge.getIntC();
      friendlyByteBuf.writeInt(i);
   }

   @NotNull
   public OptimizerPackets.OptimizerPackets$Challenge methodA(FriendlyByteBuf friendlyByteBuf) {
      int i = friendlyByteBuf.readInt();
      return new OptimizerPackets$Challenge(i);
   }

   // $VF: synthetic method
   public void encode(Object object, Object object1) {
      FriendlyByteBuf friendlyByteBuf = (FriendlyByteBuf)object;
      OptimizerPackets$Challenge challenge = (OptimizerPackets$Challenge)object1;
      this.methodA(friendlyByteBuf, challenge);
   }

   // $VF: synthetic method
   @NotNull
   public Object decode(Object object) {
      FriendlyByteBuf friendlyByteBuf = (FriendlyByteBuf)object;
      return this.methodA(friendlyByteBuf);
   }
}
