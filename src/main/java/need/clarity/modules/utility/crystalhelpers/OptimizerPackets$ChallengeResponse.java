package need.clarity.modules.utility.crystalhelpers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public record OptimizerPackets$ChallengeResponse(int intC) implements CustomPacketPayload {
   public static final Type<@NotNull OptimizerPackets.OptimizerPackets$ChallengeResponse> typeA;
   public static final StreamCodec<@NotNull FriendlyByteBuf, @NotNull OptimizerPackets.OptimizerPackets$ChallengeResponse> streamCodecB;

   @NotNull
   public Type<OptimizerPackets.OptimizerPackets$ChallengeResponse> type() {
      return typeA;
   }

   public int getIntC() {
      return this.intC;
   }

   static {
      Identifier identifier = OptimizerPackets.methodA("challenge_response");
      Type type = new Type(identifier);
      typeA = type;
      OptimizerPackets$ChallengeResponse$1 var2/* $VF was: 11 */ = new OptimizerPackets$ChallengeResponse$1();
      streamCodecB = var2/* $VF was: 11 */;
   }
}
