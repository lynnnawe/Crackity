package need.clarity.modules.utility.crystalhelpers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public record OptimizerPackets$Version(int intC, int intD, int intE, boolean flagF) implements CustomPacketPayload {
   public static final Type<OptimizerPackets$Version> typeA;
   public static final StreamCodec<FriendlyByteBuf, OptimizerPackets$Version> streamCodecB;

   @NotNull
   public Type<OptimizerPackets.OptimizerPackets$Version> type() {
      return typeA;
   }

   public int getIntC() {
      return this.intC;
   }

   public int getIntD() {
      return this.intD;
   }

   public int getIntE() {
      return this.intE;
   }

   public boolean isFlagF() {
      return this.flagF;
   }

   static {
      Identifier identifier = OptimizerPackets.methodA("version");
      Type type = new Type(identifier);
      typeA = type;
      OptimizerPackets$Version$1 var2/* $VF was: 11 */ = new OptimizerPackets$Version$1();
      streamCodecB = var2/* $VF was: 11 */;
   }
}
