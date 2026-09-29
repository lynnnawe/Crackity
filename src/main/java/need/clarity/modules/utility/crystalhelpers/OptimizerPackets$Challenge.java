package need.clarity.modules.utility.crystalhelpers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public record OptimizerPackets$Challenge(int intC) implements CustomPacketPayload {
   public static final Type<@NotNull OptimizerPackets.OptimizerPackets$Challenge> typeA;
   public static final StreamCodec<@NotNull FriendlyByteBuf, @NotNull OptimizerPackets.OptimizerPackets$Challenge> streamCodecB;

   @NotNull
   public Type<OptimizerPackets.OptimizerPackets$Challenge> type() {
      return typeA;
   }

   public int getIntC() {
      return this.intC;
   }

   static {
      Identifier identifier = OptimizerPackets.methodA("challenge");
      Type type = new Type(identifier);
      typeA = type;
      OptimizerPackets$Challenge$1 var2/* $VF was: 11 */ = new OptimizerPackets$Challenge$1();
      streamCodecB = var2/* $VF was: 11 */;
   }
}
