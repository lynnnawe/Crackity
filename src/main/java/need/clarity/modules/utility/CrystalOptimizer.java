package need.clarity.modules.utility;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import need.clarity.modules.ModuleBase;
import need.clarity.modules.utility.crystalhelpers.OptOutCache;
import need.clarity.modules.utility.crystalhelpers.OptimizerPackets$Challenge;
import need.clarity.modules.utility.crystalhelpers.OptimizerPackets$ChallengeResponse;
import need.clarity.modules.utility.crystalhelpers.OptimizerPackets$OptOut;
import need.clarity.modules.utility.crystalhelpers.OptimizerPackets$OptOutAck;
import need.clarity.modules.utility.crystalhelpers.OptimizerPackets$Version;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Disconnect;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.Join;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayPayloadHandler;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.TracingExecutor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.HoverEvent.ShowText;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.util.Util;

public class CrystalOptimizer extends ModuleBase {
   private static CrystalOptimizer instance;
   private final OptOutCache optOutCacheB;
   private static boolean flagC = false;
   private static final Component componentD;

   public static boolean methodA() {
      boolean bl;
      if (instance != null && instance.isEnabled() && !instance.optOutCacheB.isFlagC()) {
         bl = true;
      } else {
         bl = false;
      }

      return bl;
   }

   public CrystalOptimizer() {
      super("Crystal Optimizer", "utility");
      OptOutCache optOutCache = new OptOutCache();
      this.optOutCacheB = optOutCache;
      instance = this;
      if (!flagC) {
         flagC = true;
         methodB();
      }

      this.methodC();
      Event event = ClientPlayConnectionEvents.JOIN;
      Join join = (clientPacketListener, packetSender, minecraft) -> {
         if (!minecraft.isLocalServer()) {
            OptimizerPackets$Version version = new OptimizerPackets$Version(1, 0, 0, false);
            packetSender.sendPacket(version);
            String string = methodA(minecraft);
            if (this.optOutCacheB.methodB(string)) {
               this.optOutCacheB.methodA(true);
            }
         }
      };
      event.register(join);
      Event event1 = ClientPlayConnectionEvents.DISCONNECT;
      Disconnect disconnect = (clientPacketListener, minecraft) -> this.optOutCacheB.methodB();
      event1.register(disconnect);
   }

   @Override
   public void onEnable() {
      this.optOutCacheB.methodB();
   }

   @Override
   public void onDisable() {
   }

   private static void methodB() {
      PayloadTypeRegistry payloadTypeRegistry = PayloadTypeRegistry.clientboundConfiguration();
      Type type = OptimizerPackets$OptOut.typeA;
      StreamCodec streamCodec = OptimizerPackets$OptOut.streamCodecC;
      payloadTypeRegistry.register(type, streamCodec);
      PayloadTypeRegistry payloadTypeRegistry1 = PayloadTypeRegistry.clientboundPlay();
      Type type1 = OptimizerPackets$OptOut.typeA;
      StreamCodec streamCodec1 = OptimizerPackets$OptOut.streamCodecC;
      payloadTypeRegistry1.register(type1, streamCodec1);
      PayloadTypeRegistry payloadTypeRegistry2 = PayloadTypeRegistry.clientboundPlay();
      Type type2 = OptimizerPackets$Challenge.typeA;
      StreamCodec streamCodec2 = OptimizerPackets$Challenge.streamCodecB;
      payloadTypeRegistry2.register(type2, streamCodec2);
      PayloadTypeRegistry payloadTypeRegistry3 = PayloadTypeRegistry.serverboundPlay();
      Type type3 = OptimizerPackets$OptOutAck.typeA;
      StreamCodec streamCodec3 = OptimizerPackets$OptOutAck.streamCodecC;
      payloadTypeRegistry3.register(type3, streamCodec3);
      PayloadTypeRegistry payloadTypeRegistry4 = PayloadTypeRegistry.serverboundPlay();
      Type type4 = OptimizerPackets$Version.typeA;
      StreamCodec streamCodec4 = OptimizerPackets$Version.streamCodecB;
      payloadTypeRegistry4.register(type4, streamCodec4);
      PayloadTypeRegistry payloadTypeRegistry5 = PayloadTypeRegistry.serverboundPlay();
      Type type5 = OptimizerPackets$ChallengeResponse.typeA;
      StreamCodec streamCodec5 = OptimizerPackets$ChallengeResponse.streamCodecB;
      payloadTypeRegistry5.register(type5, streamCodec5);
   }

   private void methodC() {
      Type type = OptimizerPackets$OptOut.typeA;
      PlayPayloadHandler playPayloadHandler = (optOut, context) -> {
         Minecraft minecraft = context.client();
         String string = methodA(minecraft);
         if (string != null) {
            this.optOutCacheB.methodA(string);
         } else {
            this.optOutCacheB.methodA(true);
         }

         if (!this.optOutCacheB.methodC(string)) {
            TimeUnit timeUnit = TimeUnit.SECONDS;
            TracingExecutor tracingExecutor = Util.backgroundExecutor();
            Executor executor = CompletableFuture.delayedExecutor(2L, timeUnit, tracingExecutor);
            Runnable runnable = () -> {
               Runnable runnable1 = () -> {
                  if (minecraft.player != null) {
                     if (!this.optOutCacheB.methodC(string)) {
                        this.optOutCacheB.methodD(string);
                        LocalPlayer localPlayer = minecraft.player;
                        Component component = methodD();
                        localPlayer.sendSystemMessage(component);
                     }
                  }
               };
               minecraft.execute(runnable1);
            };
            executor.execute(runnable);
         }

         ClientPlayNetworking.send(OptimizerPackets$OptOutAck.instance);
      };
      ClientPlayNetworking.registerGlobalReceiver(type, playPayloadHandler);
      Type type1 = OptimizerPackets$Challenge.typeA;
      PlayPayloadHandler playPayloadHandler1 = (challenge, context) -> {
         int i = challenge.getIntC();
         OptimizerPackets$ChallengeResponse challengeResponse = new OptimizerPackets$ChallengeResponse(i);
         ClientPlayNetworking.send(challengeResponse);
      };
      ClientPlayNetworking.registerGlobalReceiver(type1, playPayloadHandler1);
   }

   private static String methodA(Minecraft minecraft) {
      String string;
      if (minecraft.getCurrentServer() != null) {
         string = minecraft.getCurrentServer().ip;
      } else {
         string = null;
      }

      return string;
   }

   private static Component methodD() {
      MutableComponent mutableComponent = Component.empty();
      MutableComponent mutableComponent7 = Component.literal("Why is this disabled?\n");
      ChatFormatting chatFormatting = ChatFormatting.AQUA;
      MutableComponent mutableComponent8 = mutableComponent7.withStyle(chatFormatting);
      MutableComponent mutableComponent1 = mutableComponent.append(mutableComponent8);
      MutableComponent mutableComponent9 = Component.literal("• This server has requested Crystal Optimizer to be disabled.\n");
      ChatFormatting chatFormatting1 = ChatFormatting.GRAY;
      MutableComponent mutableComponent10 = mutableComponent9.withStyle(chatFormatting1);
      MutableComponent mutableComponent2 = mutableComponent1.append(mutableComponent10);
      MutableComponent mutableComponent11 = Component.literal("• This may be to enforce server rules or avoid compatibility issues.\n");
      ChatFormatting chatFormatting2 = ChatFormatting.GRAY;
      MutableComponent mutableComponent12 = mutableComponent11.withStyle(chatFormatting2);
      MutableComponent mutableComponent3 = mutableComponent2.append(mutableComponent12);
      MutableComponent mutableComponent13 = Component.literal("\nThis only applies while you are connected to this server.");
      ChatFormatting chatFormatting3 = ChatFormatting.DARK_GRAY;
      MutableComponent mutableComponent14 = mutableComponent13.withStyle(chatFormatting3);
      MutableComponent mutableComponent4 = mutableComponent3.append(mutableComponent14);
      Style style = Style.EMPTY;
      ShowText showText = new ShowText(mutableComponent4);
      Style style1 = style.withHoverEvent(showText);
      MutableComponent mutableComponent5 = Component.literal("Crystal Optimizer disabled on this server.");
      ChatFormatting chatFormatting4 = ChatFormatting.RED;
      Style style2 = style1.withColor(chatFormatting4);
      MutableComponent mutableComponent6 = mutableComponent5.withStyle(style2);
      return componentD.copy().withStyle(style1).append(mutableComponent6);
   }

   static {
      MutableComponent mutableComponent = Component.literal("[");
      ChatFormatting chatFormatting = ChatFormatting.GRAY;
      MutableComponent mutableComponent1 = mutableComponent.withStyle(chatFormatting);
      MutableComponent mutableComponent3 = Component.literal("Crystal Optimizer");
      ChatFormatting chatFormatting1 = ChatFormatting.AQUA;
      MutableComponent mutableComponent4 = mutableComponent3.withStyle(chatFormatting1);
      MutableComponent mutableComponent2 = mutableComponent1.append(mutableComponent4);
      MutableComponent mutableComponent5 = Component.literal("] ");
      ChatFormatting chatFormatting2 = ChatFormatting.GRAY;
      MutableComponent mutableComponent6 = mutableComponent5.withStyle(chatFormatting2);
      componentD = mutableComponent2.append(mutableComponent6);
   }
}
