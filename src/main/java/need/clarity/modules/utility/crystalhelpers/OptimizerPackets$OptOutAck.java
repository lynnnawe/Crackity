package need.clarity.modules.utility.crystalhelpers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public final class OptimizerPackets$OptOutAck implements CustomPacketPayload {
   public static final Type<@NotNull OptimizerPackets.OptimizerPackets$OptOutAck> typeA;
   public static final OptimizerPackets$OptOutAck instance;
   public static final StreamCodec<@NotNull FriendlyByteBuf, @NotNull OptimizerPackets.OptimizerPackets$OptOutAck> streamCodecC;

   private OptimizerPackets$OptOutAck() {
   }

   @NotNull
   public Type<OptimizerPackets.OptimizerPackets$OptOutAck> type() {
      return typeA;
   }

   static {
      Identifier identifier = OptimizerPackets.methodA("opt_out_ack");
      Type type = new Type(identifier);
      typeA = type;
      OptimizerPackets$OptOutAck optOutAck = new OptimizerPackets$OptOutAck();
      instance = optOutAck;
      OptimizerPackets$OptOutAck$1 var3/* $VF was: 11 */ = new OptimizerPackets$OptOutAck$1();
      streamCodecC = var3/* $VF was: 11 */;
   }
}
