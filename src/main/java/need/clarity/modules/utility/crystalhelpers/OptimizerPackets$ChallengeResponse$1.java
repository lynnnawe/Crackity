package need.clarity.modules.utility.crystalhelpers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

class OptimizerPackets$ChallengeResponse$1 implements StreamCodec<FriendlyByteBuf, OptimizerPackets$ChallengeResponse> {
   public void methodA(FriendlyByteBuf friendlyByteBuf, OptimizerPackets$ChallengeResponse challengeResponse) {
      int i = challengeResponse.getIntC();
      friendlyByteBuf.writeInt(i);
   }

   @NotNull
   public OptimizerPackets.OptimizerPackets$ChallengeResponse methodA(FriendlyByteBuf friendlyByteBuf) {
      int i = friendlyByteBuf.readInt();
      return new OptimizerPackets$ChallengeResponse(i);
   }

   // $VF: synthetic method
   public void encode(Object object, Object object1) {
      FriendlyByteBuf friendlyByteBuf = (FriendlyByteBuf)object;
      OptimizerPackets$ChallengeResponse challengeResponse = (OptimizerPackets$ChallengeResponse)object1;
      this.methodA(friendlyByteBuf, challengeResponse);
   }

   // $VF: synthetic method
   @NotNull
   public Object decode(Object object) {
      FriendlyByteBuf friendlyByteBuf = (FriendlyByteBuf)object;
      return this.methodA(friendlyByteBuf);
   }
}
