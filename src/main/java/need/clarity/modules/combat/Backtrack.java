package need.clarity.modules.combat;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import need.clarity.EventStartAttack;
import need.clarity.PerAttackEvents;
import need.clarity.PerPacketEvents;
import need.clarity.WorldRenderEvent;
import need.clarity.config.ThemeColors;
import need.clarity.event.PacketReceiveEvent;
import need.clarity.modules.ModuleBase;
import need.clarity.render.BoxRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.network.protocol.game.VecDeltaCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Backtrack extends ModuleBase {
   private final Queue<Backtrack$QueuedPacket> queueA;
   private final Random random;
   private Entity entityC;
   private Vec3 vec3D;
   private static volatile LivingEntity livingEntityE = null;
   private VecDeltaCodec vecDeltaCodecF;
   private float floatG;

   public Backtrack() {
      super("Backtrack", "Combat");
      ConcurrentLinkedDeque concurrentLinkedDeque = new ConcurrentLinkedDeque();
      this.queueA = concurrentLinkedDeque;
      Random randomx = new Random();
      this.random = randomx;
      PerAttackEvents.register(this::methodA);
      PerPacketEvents.register(this::methodA);
      WorldRenderEvent.register(this::methodA);
   }

   @Override
   public void onEnable() {
      this.queueA.clear();
      this.methodC();
   }

   @Override
   public void onDisable() {
      this.methodA(false);
      this.methodC();
   }

   private void methodA(EventStartAttack eventStartAttack) {
      if (this.isEnabled()) {
         Entity entity = eventStartAttack.getTarget();
         this.entityC = entity;
         if (!(this.entityC instanceof LivingEntity livingEntity)) {
            livingEntity = null;
         }

         livingEntityE = livingEntity;
         this.methodC();
      }
   }

   private void methodA(PacketReceiveEvent packetReceiveEvent) {
      if (this.isEnabled()) {
         Minecraft minecraft = Minecraft.getInstance();
         if (minecraft.player != null) {
            if (minecraft.level != null) {
               if (!minecraft.isSingleplayer()) {
                  Packet packet = packetReceiveEvent.getPacket();
                  if (packet != null) {
                     if (!this.methodA(minecraft)) {
                        this.methodA(false);
                        this.methodC();
                     } else if (!(packet instanceof ServerboundChatPacket)) {
                        if (!(packet instanceof ClientboundSystemChatPacket)) {
                           if (!(packet instanceof ServerboundChatCommandPacket)) {
                              if (!(packet instanceof ClientboundPlayerPositionPacket) && !(packet instanceof ClientboundDisconnectPacket)) {
                                 if (packet instanceof ClientboundSetHealthPacket && ((ClientboundSetHealthPacket)packet).getHealth() <= 0.0F) {
                                    this.methodA(false);
                                    this.methodC();
                                 } else {
                                    if (this.floatG <= 0.0F) {
                                       Float float1 = 150.0F;
                                       float f = this.getSetting("Delay_lo", float1);
                                       Float float2 = 250.0F;
                                       float g = this.getSetting("Delay_hi", float2);
                                       float i;
                                       if (g > f) {
                                          float j = this.random.nextFloat();
                                          float n = g - f;
                                          float k = j * n;
                                          i = f + k;
                                       } else {
                                          i = f;
                                       }

                                       this.floatG = i;
                                    }

                                    float h = this.floatG;
                                    if (h <= 0.0F) {
                                       this.methodA(false);
                                    } else {
                                       this.methodA(true);
                                       if (this.methodA(packet, minecraft)) {
                                          this.methodA(false);
                                          this.methodC();
                                       } else {
                                          this.methodB(packet, minecraft);
                                          Queue queue = this.queueA;
                                          long o = System.currentTimeMillis();
                                          Backtrack$QueuedPacket queuedPacket1 = new Backtrack$QueuedPacket(packet, o);
                                          queue.add(queuedPacket1);
                                          packetReceiveEvent.cancel();
                                          Backtrack$QueuedPacket queuedPacket = this.queueA.peek();
                                          if (queuedPacket != null) {
                                             long l = System.currentTimeMillis();
                                             long m = queuedPacket.longB;
                                             if ((float)(l - m) > h) {
                                                this.methodA(false);
                                             }
                                          }
                                       }
                                    }
                                 }
                              } else {
                                 this.methodA(false);
                                 this.methodC();
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void methodA(boolean bl) {
      ClientPacketListener clientPacketListener = Minecraft.getInstance().getConnection();
      if (clientPacketListener == null) {
         this.queueA.clear();
         this.methodC();
      } else {
         long l = System.currentTimeMillis();

         while (true) {
            Backtrack$QueuedPacket queuedPacket = this.queueA.peek();
            if (queuedPacket == null) {
               break;
            }

            if (bl) {
               long m = queuedPacket.longB;
               float f = (float)(l - m);
               float g = this.floatG;
               if (f < g) {
                  break;
               }
            }

            this.queueA.poll();

            try {
               queuedPacket.packetA.handle(clientPacketListener);
            } catch (Throwable throwable) {
            }
         }

         if (!bl) {
            this.queueA.clear();
            this.methodC();
         } else if (this.queueA.isEmpty()) {
            this.methodC();
         }
      }
   }

   private void methodA(PoseStack poseStack, float f) {
      if (this.isEnabled()) {
         Boolean boolean1 = true;
         if (this.getSetting("Render", boolean1)) {
            Minecraft minecraft = Minecraft.getInstance();
            if (!this.methodA(minecraft)) {
               this.methodC();
            } else {
               AABB aABB = this.methodB();
               if (aABB != null) {
                  Boolean boolean2 = true;
                  boolean bl = this.getSetting("Fill", boolean2);
                  Boolean boolean3 = true;
                  boolean bl1 = this.getSetting("Outline", boolean3);
                  if (bl || bl1) {
                     int j;
                     if (bl) {
                        j = methodA(this.getSetting("Fill Color", "#3a7fcc"), 80, false);
                     } else {
                        j = 0;
                     }

                     byte b = (byte)j;
                     int i;
                     if (bl1) {
                        i = methodA(this.getSetting("Outline Color", "#ffffff"), 255, true);
                     } else {
                        i = 0;
                     }

                     byte c = (byte)i;
                     List list = List.of(aABB);
                     BoxRenderer.methodA(poseStack, list, b, c, bl, bl1);
                  }
               }
            }
         }
      }
   }

   private boolean methodA(Packet<?> packet, Minecraft minecraft) {
      if (minecraft.player != null && minecraft.level != null && this.entityC != null) {
         Vec3 vec3 = null;
         if (packet instanceof ClientboundMoveEntityPacket clientboundMoveEntityPacket) {
            if (!this.methodA(clientboundMoveEntityPacket, minecraft)) {
               return false;
            }

            if (!clientboundMoveEntityPacket.hasPosition()) {
               return false;
            }

            this.methodA();
            if (this.vecDeltaCodecF == null) {
               return false;
            }

            VecDeltaCodec vecDeltaCodec = this.vecDeltaCodecF;
            long r = clientboundMoveEntityPacket.getXa();
            long v = clientboundMoveEntityPacket.getYa();
            long w = clientboundMoveEntityPacket.getZa();
            vec3 = vecDeltaCodec.decode(r, v, w);
         } else if (packet instanceof ClientboundEntityPositionSyncPacket clientboundEntityPositionSyncPacket) {
            int o = clientboundEntityPositionSyncPacket.id();
            int q = this.entityC.getId();
            if (o != q) {
               return false;
            }

            vec3 = clientboundEntityPositionSyncPacket.values().position();
         } else if (packet instanceof ClientboundTeleportEntityPacket clientboundTeleportEntityPacket) {
            int i = clientboundTeleportEntityPacket.id();
            int p = this.entityC.getId();
            if (i != p) {
               return false;
            }

            Vec3 vec35;
            if (this.vec3D != null) {
               vec35 = this.vec3D;
            } else {
               vec35 = this.entityC.position();
            }

            Vec3 vec31 = vec35;
            Vec3 vec32 = clientboundTeleportEntityPacket.change().position();
            Set set = clientboundTeleportEntityPacket.relatives();
            Relative relative = Relative.X;
            double n;
            if (set.contains(relative)) {
               double m = vec31.x;
               double u = vec32.x;
               n = m + u;
            } else {
               n = vec32.x;
            }

            double d = n;
            Set set1 = clientboundTeleportEntityPacket.relatives();
            Relative relative1 = Relative.Y;
            double g;
            if (set1.contains(relative1)) {
               double l = vec31.y;
               double t = vec32.y;
               g = l + t;
            } else {
               g = vec32.y;
            }

            double e = g;
            Set set2 = clientboundTeleportEntityPacket.relatives();
            Relative relative2 = Relative.Z;
            double h;
            if (set2.contains(relative2)) {
               double k = vec31.z;
               double s = vec32.z;
               h = k + s;
            } else {
               h = vec32.z;
            }

            double f = h;
            Vec3 vec33 = new Vec3(d, e, f);
            vec3 = vec33;
         }

         if (vec3 == null) {
            return false;
         }

         Vec3 vec34 = minecraft.player.position();
         Vec3 vec36 = this.entityC.position();
         double j = vec34.distanceToSqr(vec36);
         Vec3 vec37 = vec3;
         boolean bl;
         if (vec34.distanceToSqr(vec37) > j) {
            bl = true;
         } else {
            bl = false;
         }

         return bl;
      } else {
         return false;
      }
   }

   private void methodB(Packet<?> packet, Minecraft minecraft) {
      if (this.methodA(minecraft)) {
         if (minecraft.level != null) {
            if (packet instanceof ClientboundMoveEntityPacket clientboundMoveEntityPacket) {
               if (this.methodA(clientboundMoveEntityPacket, minecraft)) {
                  this.methodA(clientboundMoveEntityPacket);
               }
            } else if (packet instanceof ClientboundEntityPositionSyncPacket clientboundEntityPositionSyncPacket) {
               int j = clientboundEntityPositionSyncPacket.id();
               int l = this.entityC.getId();
               if (j == l) {
                  Vec3 vec3 = clientboundEntityPositionSyncPacket.values().position();
                  this.methodA(vec3);
               }
            } else if (packet instanceof ClientboundTeleportEntityPacket clientboundTeleportEntityPacket) {
               int i = clientboundTeleportEntityPacket.id();
               int k = this.entityC.getId();
               if (i == k) {
                  this.methodA(clientboundTeleportEntityPacket);
               }
            }
         }
      }
   }

   private void methodA(ClientboundMoveEntityPacket clientboundMoveEntityPacket) {
      this.methodA();
      if (this.vecDeltaCodecF != null) {
         if (clientboundMoveEntityPacket.hasPosition()) {
            VecDeltaCodec vecDeltaCodec = this.vecDeltaCodecF;
            long l = clientboundMoveEntityPacket.getXa();
            long m = clientboundMoveEntityPacket.getYa();
            long n = clientboundMoveEntityPacket.getZa();
            Vec3 vec3 = vecDeltaCodec.decode(l, m, n);
            this.vec3D = vec3;
            this.vecDeltaCodecF.setBase(vec3);
         }
      }
   }

   private void methodA(ClientboundTeleportEntityPacket clientboundTeleportEntityPacket) {
      this.methodA();
      if (this.vec3D != null) {
         Vec3 vec3 = clientboundTeleportEntityPacket.change().position();
         Set set = clientboundTeleportEntityPacket.relatives();
         Relative relative = Relative.X;
         double h;
         if (set.contains(relative)) {
            double g = this.vec3D.x;
            double m = vec3.x;
            h = g + m;
         } else {
            h = vec3.x;
         }

         double d = h;
         Set set1 = clientboundTeleportEntityPacket.relatives();
         Relative relative1 = Relative.Y;
         double l;
         if (set1.contains(relative1)) {
            double i = this.vec3D.y;
            double n = vec3.y;
            l = i + n;
         } else {
            l = vec3.y;
         }

         double e = l;
         Set set2 = clientboundTeleportEntityPacket.relatives();
         Relative relative2 = Relative.Z;
         double k;
         if (set2.contains(relative2)) {
            double j = this.vec3D.z;
            double o = vec3.z;
            k = j + o;
         } else {
            k = vec3.z;
         }

         double f = k;
         Vec3 vec31 = new Vec3(d, e, f);
         this.methodA(vec31);
      }
   }

   private void methodA() {
      if (this.vec3D == null || this.vecDeltaCodecF == null) {
         if (this.entityC != null) {
            Vec3 vec3 = this.entityC.getPositionCodec().getBase();
            this.methodA(vec3);
         }
      }
   }

   private void methodA(Vec3 vec3) {
      this.vec3D = vec3;
      VecDeltaCodec vecDeltaCodec = new VecDeltaCodec();
      this.vecDeltaCodecF = vecDeltaCodec;
      this.vecDeltaCodecF.setBase(vec3);
   }

   private AABB methodB() {
      if (this.vec3D != null && this.entityC != null) {
         Vec3 vec3 = this.vec3D;
         Vec3 vec32 = this.entityC.position();
         Vec3 vec31 = vec3.subtract(vec32);
         return this.entityC.getBoundingBox().move(vec31);
      } else {
         return null;
      }
   }

   private boolean methodA(ClientboundMoveEntityPacket clientboundMoveEntityPacket, Minecraft minecraft) {
      if (minecraft.level == null) {
         return false;
      }

      ClientLevel clientLevel = minecraft.level;
      Entity entity = clientboundMoveEntityPacket.getEntity(clientLevel);
      if (entity != null) {
         int i = entity.getId();
         int j = this.entityC.getId();
         if (i == j) {
            return true;
         }
      }

      return false;
   }

   private void methodC() {
      this.vec3D = null;
      this.vecDeltaCodecF = null;
      this.floatG = 0.0F;
   }

   private boolean methodA(Minecraft minecraft) {
      if (this.entityC == null || minecraft.player == null) {
         return false;
      }

      if (!this.entityC.isAlive()) {
         return false;
      }

      Integer integer = 6;
      int i = this.getSetting("Range", integer);
      LocalPlayer localPlayer = minecraft.player;
      Entity entity = this.entityC;
      double d = localPlayer.distanceToSqr(entity);
      double e = i * i;
      boolean bl;
      if (d <= e) {
         bl = true;
      } else {
         bl = false;
      }

      return bl;
   }

   private static int methodA(String string, int i, boolean bl) {
      int j = ThemeColors.resolveRgb(string, 3833804, bl);
      int k = (i & 0xFF) << 24;
      int l = j & 16777215;
      return k | l;
   }
}
