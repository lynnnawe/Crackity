package need.clarity.modules.utility.crystalhelpers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

class OptimizerPackets$Version$1 implements StreamCodec<FriendlyByteBuf, OptimizerPackets$Version> {
   public void methodA(FriendlyByteBuf friendlyByteBuf, OptimizerPackets$Version version) {
      int i = version.getIntC();
      friendlyByteBuf.writeVarInt(i);
      int j = version.getIntD();
      friendlyByteBuf.writeVarInt(j);
      int k = version.getIntE();
      friendlyByteBuf.writeVarInt(k);
      boolean bl = version.isFlagF();
      friendlyByteBuf.writeBoolean(bl);
   }

   public OptimizerPackets$Version methodA(FriendlyByteBuf friendlyByteBuf) {
      int i = friendlyByteBuf.readVarInt();
      int j = friendlyByteBuf.readVarInt();
      int k = friendlyByteBuf.readVarInt();
      boolean bl = friendlyByteBuf.readBoolean();
      return new OptimizerPackets$Version(i, j, k, bl);
   }

   // $VF: synthetic method
   public void encode(Object object, Object object1) {
      FriendlyByteBuf friendlyByteBuf = (FriendlyByteBuf)object;
      OptimizerPackets$Version version = (OptimizerPackets$Version)object1;
      this.methodA(friendlyByteBuf, version);
   }

   // $VF: synthetic method
   public Object decode(Object object) {
      FriendlyByteBuf friendlyByteBuf = (FriendlyByteBuf)object;
      return this.methodA(friendlyByteBuf);
   }
}
