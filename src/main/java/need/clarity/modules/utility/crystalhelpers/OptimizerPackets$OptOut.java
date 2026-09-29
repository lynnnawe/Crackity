package need.clarity.modules.utility.crystalhelpers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public final class OptimizerPackets$OptOut implements CustomPacketPayload {
   public static final Type<@NotNull OptimizerPackets.OptimizerPackets$OptOut> typeA;
   public static final OptimizerPackets$OptOut instance;
   public static final StreamCodec<@NotNull FriendlyByteBuf, @NotNull OptimizerPackets.OptimizerPackets$OptOut> streamCodecC;

   private OptimizerPackets$OptOut() {
   }

   @NotNull
   public Type<OptimizerPackets.OptimizerPackets$OptOut> type() {
      return typeA;
   }

   static {
      Identifier identifier = OptimizerPackets.methodA("opt_out");
      Type type = new Type(identifier);
      typeA = type;
      OptimizerPackets$OptOut optOut = new OptimizerPackets$OptOut();
      instance = optOut;
      OptimizerPackets$OptOut$1 var3/* $VF was: 11 */ = new OptimizerPackets$OptOut$1();
      streamCodecC = var3/* $VF was: 11 */;
   }
}
