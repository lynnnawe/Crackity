package need.clarity.modules.utility;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import need.clarity.EventStartAttack;
import need.clarity.PerAttackEvents;
import need.clarity.PerPacketEvents;
import need.clarity.PerPacketSendEvents;
import need.clarity.WorldRenderEvent;
import need.clarity.WorldRenderEvent$Listener;
import need.clarity.config.ThemeColors;
import need.clarity.event.PacketReceiveEvent;
import need.clarity.event.PacketSendEvent;
import need.clarity.input.KeybindHandler;
import need.clarity.modules.ModuleBase;
import need.clarity.render.BoxRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundPingPacket;
import net.minecraft.network.protocol.common.ServerboundPongPacket;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.network.protocol.game.ClientboundHurtAnimationPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.WalkAnimationState;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

public class Overtick extends ModuleBase {
   private static final Map<Class<?>, Integer> mapA;
   private final List<Overtick$Entry> listB;
   private final Object objectC;
   private final Map<Integer, Overtick$GhostPos> mapD;
   private final Map<Integer, double[]> mapE;
   private final Map<Long, Object> mapF;
   private volatile Set<EntityRenderState> setG;
   private volatile boolean flagH;
   private volatile boolean flagI;
   private volatile boolean flagJ;
   private boolean flagK;
   private long longL;
   private long longM;
   private int intN;
   private boolean flagO;
   private int intP;
   private int intQ;
   private boolean flagR;
   private int intS;
   private boolean flagT;
   private int intU;
   private boolean flagV;
   private int intW;
   private boolean flagX;
   private boolean flagY;
   private final List<Packet<?>> listZ;
   private final List<Integer> listA;
   private boolean flagB;
   private String stringC;
   private long longD;
   private ClientPacketListener clientPacketListenerE;
   private ResourceKey<Level> resourceKeyF;
   private volatile int intG;
   private volatile int intH;
   private volatile String stringI;
   private volatile int intJ;
   private final WorldRenderEvent$Listener listenerK;

   private static Class<?> methodA(String string) {
      try {
         return Class.forName(string);
      } catch (Throwable throwable) {
         return null;
      }
   }

   private static void methodA(Class<?> class_, int i, int j) {
      if (class_ != null) {
         Map map = mapA;
         int k = i & 0xFF;
         int l = j << 8;
         Integer integer = k | l;
         map.put(class_, integer);
      }
   }

   public Overtick() {
      super("Overtick", "utility");
      ArrayList arrayList = new ArrayList();
      this.listB = arrayList;
      Object object = new Object();
      this.objectC = object;
      HashMap hashMap = new HashMap();
      this.mapD = hashMap;
      HashMap hashMap1 = new HashMap();
      this.mapE = hashMap1;
      HashMap hashMap2 = new HashMap();
      this.mapF = hashMap2;
      IdentityHashMap identityHashMap = new IdentityHashMap();
      Set set = Collections.newSetFromMap(identityHashMap);
      this.setG = set;
      this.flagH = false;
      this.flagI = false;
      this.flagJ = false;
      this.flagK = false;
      this.longL = 0L;
      this.longM = 0L;
      this.intN = 0;
      this.flagO = false;
      this.intP = -1;
      this.intQ = 0;
      this.flagR = false;
      this.intS = 0;
      this.flagT = false;
      this.intU = 0;
      this.flagV = false;
      this.intW = 0;
      this.flagX = false;
      this.flagY = false;
      ArrayList arrayList1 = new ArrayList();
      this.listZ = arrayList1;
      ArrayList arrayList2 = new ArrayList();
      this.listA = arrayList2;
      this.flagB = false;
      this.stringC = "";
      this.longD = 0L;
      this.clientPacketListenerE = null;
      this.resourceKeyF = null;
      this.intG = 0;
      this.intH = 0;
      this.stringI = "Flow";
      this.intJ = -1;
      WorldRenderEvent$Listener listener = this::methodA;
      this.listenerK = listener;
      PerPacketEvents.register(this::methodA);
      PerPacketSendEvents.register(this::methodA);
      PerAttackEvents.register(this::methodA);
   }

   private String getReleaseModeSetting() {
      return this.getSetting("Release Mode", "Flow");
   }

   private int getFlowSpeedSetting() {
      Integer integer = 3;
      return this.getSetting("Flow Speed", integer);
   }

   private int getReleaseSpeedSetting() {
      Integer integer = 2;
      return this.getSetting("Release Speed", integer);
   }

   private int getIntervalSetting() {
      Integer integer = 5;
      return this.getSetting("Interval", integer);
   }

   private int getDelaySetting() {
      Integer integer = 10;
      return this.getSetting("Delay", integer);
   }

   private boolean isDisableOnFlagSetting() {
      Boolean boolean_ = true;
      return this.getSetting("Disable On Flag", boolean_);
   }

   private boolean isDisableOnDeathSetting() {
      Boolean boolean_ = true;
      return this.getSetting("Disable On Death", boolean_);
   }

   private boolean isDisableOnConnectSetting() {
      Boolean boolean_ = true;
      return this.getSetting("Disable On Connect", boolean_);
   }

   private boolean isDisableOnDimensionChangeSetting() {
      Boolean boolean_ = true;
      return this.getSetting("Disable On Dimension Change", boolean_);
   }

   private int getQueueTimeoutSetting() {
      Integer integer = 29;
      return this.getSetting("Queue Timeout", integer);
   }

   private boolean isFlushAllOnTimeoutSetting() {
      Boolean boolean_ = false;
      return this.getSetting("Flush All On Timeout", boolean_);
   }

   private boolean isSafeReleaseOnTimeoutSetting() {
      Boolean boolean_ = true;
      return this.getSetting("Safe Release On Timeout", boolean_);
   }

   private boolean isGhostEntitiesSetting() {
      Boolean boolean_ = true;
      return this.getSetting("Ghost Entities", boolean_);
   }

   private boolean isGhostESPSetting() {
      Boolean boolean_ = true;
      return this.getSetting("Ghost ESP", boolean_);
   }

   private int getGhostAlphaSetting() {
      Integer integer = 60;
      return this.getSetting("Ghost Alpha", integer);
   }

   private float getGhostOpacitySetting() {
      Float float_ = 0.6F;
      return this.getSetting("Ghost Opacity", float_);
   }

   private String getGhostColorSetting() {
      return this.getSetting("Ghost Color", "#64b4ff");
   }

   private boolean isAdvancedPacketControlSetting() {
      Boolean boolean_ = false;
      return this.getSetting("Advanced Packet Control", boolean_);
   }

   private int getReleaseKeySetting() {
      String string = this.getSetting("Release Key", "NONE");
      return string != null && !string.isEmpty() && !string.equals("NONE") ? KeybindHandler.parseKeyCode(string) : -1;
   }

   private void methodV() {
      short t = 0;
      if (this.isAdvancedPacketControlSetting()) {
         Boolean boolean_xxxxxxx = true;
         if (this.getSetting("Queue Transactions", boolean_xxxxxxx)) {
            t = 1;
         }

         Boolean boolean_x = true;
         if (this.getSetting("Queue Entity Movement", boolean_x)) {
            t |= 2;
         }

         Boolean boolean_xx = true;
         if (this.getSetting("Queue Entity Lifecycle", boolean_xx)) {
            t |= 4;
         }

         Boolean boolean_xxx = true;
         if (this.getSetting("Queue Entity State", boolean_xxx)) {
            t |= 8;
         }

         Boolean boolean_xxxx = true;
         if (this.getSetting("Queue Velocity", boolean_xxxx)) {
            t |= 16;
         }

         Boolean boolean_xxxxx = true;
         if (this.getSetting("Queue Blocks", boolean_xxxxx)) {
            t |= 32;
         }

         Boolean boolean_xxxxxx = true;
         if (this.getSetting("Queue Game State", boolean_xxxxxx)) {
            t |= 64;
         }

         Boolean boolean_xxxxxxx = true;
         if (this.getSetting("Queue Inventory", boolean_xxxxxxx)) {
            t |= 128;
         }

         Boolean boolean_xxxxxxxx = true;
         if (this.getSetting("Queue Entity Events", boolean_xxxxxxxx)) {
            t |= 256;
         }

         Boolean boolean_xxxxxxxxx = true;
         if (this.getSetting("Queue Health", boolean_xxxxxxxxx)) {
            t |= 512;
         }

         Boolean boolean_xxxxxxxxxx = true;
         if (this.getSetting("Queue Experience", boolean_xxxxxxxxxx)) {
            t |= 1024;
         }

         Boolean boolean_xxxxxxxxxxx = true;
         if (this.getSetting("Queue Cooldowns", boolean_xxxxxxxxxxx)) {
            t |= 2048;
         }

         Boolean boolean_xxxxxxxxxxxx = true;
         if (this.getSetting("Queue Held Slot", boolean_xxxxxxxxxxxx)) {
            t |= 4096;
         }

         Boolean boolean_xxxxxxxxxxxxx = false;
         if (this.getSetting("Queue Camera", boolean_xxxxxxxxxxxxx)) {
            t |= 8192;
         }

         Boolean boolean_xxxxxxxxxxxxxx = false;
         if (this.getSetting("Queue Combat Events", boolean_xxxxxxxxxxxxxx)) {
            t |= 16384;
         }
      } else {
         t = 8191;
      }

      short s = t;
      this.intG = s;
      short u = 0;
      if (this.isAdvancedPacketControlSetting()) {
         Boolean boolean_xxxxxxxxxx = false;
         if (this.getSetting("Never Queue Inventory", boolean_xxxxxxxxxx)) {
            u = 128;
         }

         Boolean boolean_x = false;
         if (this.getSetting("Never Queue Velocity", boolean_x)) {
            u |= 16;
         }

         Boolean boolean_xx = false;
         if (this.getSetting("Never Queue Blocks", boolean_xx)) {
            u |= 32;
         }
      }

      short v = u;
      this.intH = v;
      String string = this.getReleaseModeSetting();
      this.stringI = string;

      try {
         int i;
         if (Minecraft.getInstance().player != null) {
            i = Minecraft.getInstance().player.getId();
         } else {
            i = -1;
         }

         this.intJ = i;
      } catch (Exception exception) {
         this.intJ = -1;
      }
   }

   @Override
   public void onEnable() {
      List list = this.listB;
      synchronized (list) {
         this.listB.clear();

      }

      this.methodUpperI();
      this.flagI = false;
      this.flagH = false;
      this.flagK = false;
      this.longL = 0L;
      this.longM = 0L;
      this.intN = 0;
      this.flagO = false;
      this.intP = -1;
      this.flagX = false;
      this.flagT = false;
      this.flagR = false;
      this.intW = 0;
      this.intU = 0;
      this.intS = 0;
      this.flagV = false;
      this.flagY = false;
      this.stringC = "";
      this.longD = 0L;
      Minecraft minecraft = Minecraft.getInstance();

      try {
         ClientPacketListener clientPacketListener;
         if (minecraft != null) {
            clientPacketListener = minecraft.getConnection();
         } else {
            clientPacketListener = null;
         }

         this.clientPacketListenerE = clientPacketListener;
      } catch (Exception exception) {
         this.clientPacketListenerE = null;
      }

      try {
         ResourceKey resourceKey;
         if (minecraft != null && minecraft.level != null) {
            resourceKey = minecraft.level.dimension();
         } else {
            resourceKey = null;
         }

         this.resourceKeyF = resourceKey;
      } catch (Exception exception1) {
         this.resourceKeyF = null;
      }

      this.methodV();
      WorldRenderEvent.register(this.listenerK);
   }

   @Override
   public void onDisable() {
      if (!this.flagI) {
         this.flagI = true;
         WorldRenderEvent.methodA(this.listenerK);

         try {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null) {
               minecraft.player.noPhysics = false;
            }
         } catch (Exception exception) {
         }

         this.methodUpperD();
         this.methodUpperI();
         this.flagH = false;
         this.flagK = false;
         this.flagX = false;
         this.intN = 0;
         this.flagO = false;
         this.flagI = false;
      }
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null && minecraft.level != null && minecraft.player.connection != null) {
         this.methodA();

         try {
            this.methodV();
            if (this.isDisableOnDeathSetting()) {
               try {
                  if (minecraft.player.isDeadOrDying()) {
                     this.setEnabled(false);
                     return;
                  }
               } catch (Exception exception2) {
               }
            }

            try {
               ClientPacketListener clientPacketListener = minecraft.getConnection();
               ClientPacketListener clientPacketListener1 = this.clientPacketListenerE;
               if (clientPacketListener != clientPacketListener1) {
                  if (this.isDisableOnConnectSetting() && this.clientPacketListenerE != null) {
                     this.setEnabled(false);
                     return;
                  }

                  this.clientPacketListenerE = clientPacketListener;
               }
            } catch (Exception exception1) {
            }

            try {
               ResourceKey resourceKey = minecraft.level.dimension();
               ResourceKey resourceKey1 = this.resourceKeyF;
               if (!Objects.equals(resourceKey, resourceKey1)) {
                  if (this.isDisableOnDimensionChangeSetting() && this.resourceKeyF != null && resourceKey != null) {
                     this.setEnabled(false);
                     return;
                  }

                  this.resourceKeyF = resourceKey;
               }
            } catch (Exception exception) {
            }

            this.methodA(minecraft);
            List list = this.listB;
            synchronized (list) {

            }

            List list1 = this.listB;
            boolean bl4;

            synchronized (list1) {
               boolean bl1;
               if (!this.listB.isEmpty()) {
                  bl1 = true;
               } else {
                  bl1 = false;
               }

               bl4 = bl1;

            }

            boolean bl3;
            if (bl4 && this.isGhostEntitiesSetting()) {
               bl3 = true;
            } else {
               bl3 = false;
            }

            this.flagJ = bl3;
            if (this.flagJ) {
               this.methodUpperG();
            }

            String string = this.stringI;
            if ("Jetpack".equals(string) && this.getReleaseKeySetting() != -1) {
               boolean bl2 = this.flagY;
               this.flagV = bl2;
            } else {
               this.flagV = false;
            }

            if (this.flagX) {
               String string1 = this.stringI;
               if ("Steady".equals(string1)) {
                  int s = this.getReleaseSpeedSetting();
                  int j = Math.max(1, s);
                  ArrayList arrayList = new ArrayList();
                  List list2 = this.listB;
                  synchronized (list2) {
                     for (int i = 0; i < j && !this.listB.isEmpty(); i++) {
                        Overtick$Entry entry3 = this.listB.remove(0);
                        arrayList.add(entry3);
                     }

                  }

                  if (!arrayList.isEmpty()) {
                     this.flagH = true;
                     Iterator iterator = arrayList.iterator();

                     while (iterator.hasNext()) {
                        Packet packet = ((Overtick$Entry)iterator.next()).packetA;
                        this.methodC(packet);
                     }

                     this.flagH = false;
                     List list3 = this.listB;
                     synchronized (list3) {
                        if (this.listB.isEmpty()) {
                           this.flagX = false;
                           this.methodUpperH();
                        }

                     }
                  }
               }
            }

            if (this.flagK) {
               List list4 = this.listB;
               boolean bl;

               synchronized (list4) {
                  bl = this.listB.isEmpty();

               }

               if (bl) {
                  this.flagK = false;
                  this.intN = 0;
                  this.methodUpperH();
               } else {
                  int r;
                  if (this.intN > 0) {
                     r = this.intN;
                  } else {
                     int t = this.getFlowSpeedSetting();
                     r = Math.max(1, t);
                  }

                  int z = r;
                  long n = System.currentTimeMillis();
                  long w = this.longL;
                  long o = n - w;
                  long x = z;
                  long p = o * x;
                  ArrayList arrayList2 = new ArrayList();
                  List list8 = this.listB;
                  synchronized (list8) {
                     while (!this.listB.isEmpty()) {
                        long q = this.listB.get(0).longB;
                        long y = this.longM;
                        if (q - y > p) {
                           break;
                        }

                        Overtick$Entry entry2 = this.listB.remove(0);
                        arrayList2.add(entry2);
                     }

                  }

                  if (!arrayList2.isEmpty()) {
                     this.flagH = true;
                     Iterator iterator2 = arrayList2.iterator();

                     while (iterator2.hasNext()) {
                        Packet packet2 = ((Overtick$Entry)iterator2.next()).packetA;
                        this.methodC(packet2);
                     }

                     this.flagH = false;
                     List list9 = this.listB;
                     synchronized (list9) {
                        if (this.listB.isEmpty()) {
                           this.flagK = false;
                           this.intN = 0;
                           this.methodUpperH();
                        }

                     }
                  }
               }
            }

            int k = this.getQueueTimeoutSetting();
            if (k > 0 && bl4) {
               long l = System.currentTimeMillis();
               long m = k * 1000L;
               List list5 = this.listB;
               Overtick$Entry entry4;

               synchronized (list5) {
                  Overtick$Entry entry;
                  if (this.listB.isEmpty()) {
                     entry = null;
                  } else {
                     entry = this.listB.get(0);
                  }

                  entry4 = entry;

               }

               if (entry4 != null) {
                  long u = entry4.longB;
                  if (l - u >= m) {
                     if (this.isSafeReleaseOnTimeoutSetting()) {
                        if (!this.flagK) {
                           this.methodA(1);
                           return;
                        }
                     } else {
                        if (this.isFlushAllOnTimeoutSetting()) {
                           this.methodUpperC();
                           return;
                        }

                        ArrayList arrayList1 = new ArrayList();
                        List list6 = this.listB;
                        synchronized (list6) {
                           while (!this.listB.isEmpty()) {
                              long v = this.listB.get(0).longB;
                              if (l - v < m) {
                                 break;
                              }

                              Overtick$Entry entry1 = this.listB.remove(0);
                              arrayList1.add(entry1);
                           }

                        }

                        if (!arrayList1.isEmpty()) {
                           this.flagH = true;
                           Iterator iterator1 = arrayList1.iterator();

                           while (iterator1.hasNext()) {
                              Packet packet1 = ((Overtick$Entry)iterator1.next()).packetA;
                              this.methodC(packet1);
                           }

                           this.flagH = false;
                           List list7 = this.listB;
                           synchronized (list7) {
                              if (this.listB.isEmpty()) {
                                 this.methodUpperH();
                              }

                              return;
                           }
                        }
                     }
                  }
               }
            }
         } catch (Exception exception3) {
            this.flagK = false;
            this.intN = 0;
         }
      } else if (this.isEnabled()) {
         this.setEnabled(false);
      }
   }

   private void methodA(Minecraft minecraft) {
      int i = this.getReleaseKeySetting();
      if (i > 0 && i != -1) {
         boolean bl7;
         try {
            boolean bl4;
            if (GLFW.glfwGetKey(minecraft.getWindow().handle(), i) == 1) {
               bl4 = true;
            } else {
               bl4 = false;
            }

            bl7 = bl4;
         } catch (Exception exception) {
            bl7 = false;
         }

         boolean bl2;
         if (bl7 && !this.flagY) {
            bl2 = true;
         } else {
            bl2 = false;
         }

         boolean bl = bl2;
         boolean bl3;
         if (!bl7 && this.flagY) {
            bl3 = true;
         } else {
            bl3 = false;
         }

         boolean bl1 = bl3;
         boolean bl5 = bl7;
         this.flagY = bl5;
         String string = this.stringI;
         String string1 = this.stringC;
         if (!string.equals(string1)) {
            this.methodY();
            this.stringC = string;
         }

         if (bl) {
            byte b = -1;
            int j = string.hashCode();
            switch (j) {
               case -1808513998:
                  if (string.equals("Steady")) {
                     b = 1;
                  }
                  break;
               case -42098670:
                  if (string.equals("Jetpack")) {
                     b = 2;
                  }
                  break;
               case 2192718:
                  if (string.equals("Flow")) {
                     b = 0;
                  }
                  break;
               case 2606525:
                  if (string.equals("Tick")) {
                     b = 3;
                  }
                  break;
               case 1380408214:
                  if (string.equals("Up/Down")) {
                     b = 5;
                  }
                  break;
               case 1587773975:
                  if (string.equals("One by One")) {
                     b = 4;
                  }
            }

            byte c = b;
            switch (c) {
               case 0:
                  this.methodX();
                  break;
               case 1:
                  this.flagX = true;
                  break;
               case 2:
                  this.methodUpperC();
                  break;
               case 3:
                  boolean bl6;
                  if (!this.flagR) {
                     bl6 = true;
                  } else {
                     bl6 = false;
                  }

                  this.flagR = bl6;
                  this.intS = 0;
                  break;
               case 4:
                  if (this.methodW()) {
                     if (!this.methodUpperB()) {
                        this.methodUpperC();
                     } else {
                        this.methodUpperE();
                        if (!this.methodUpperB()) {
                           this.methodUpperC();
                        }
                     }
                  }
                  break;
               case 5:
                  if (this.methodW() && !this.flagT) {
                     this.methodUpperF();
                     if (this.methodUpperB()) {
                        this.flagT = true;
                        this.intU = 0;
                     } else {
                        this.methodUpperC();
                     }
                  }
                  break;
               default:
                  if (this.methodW()) {
                     this.methodUpperC();
                  }
            }
         }

         if (bl1) {
            if ("Flow".equals(string)) {
               this.flagK = false;
               this.intN = 0;
               this.flagO = false;
            }

            if ("Steady".equals(string)) {
               this.flagX = false;
            }
         }
      } else {
         this.flagY = false;
      }
   }

   private boolean methodW() {
      long l = System.currentTimeMillis();
      long m = this.longD;
      if (l - m < 250L) {
         return false;
      }

      this.longD = l;
      return true;
   }

   private void methodX() {
      this.methodA(0);
   }

   private void methodA(int i) {
      List list = this.listB;
      Overtick$Entry entry;

      synchronized (list) {
         Overtick$Entry entry1;
         if (this.listB.isEmpty()) {
            entry1 = null;
         } else {
            entry1 = this.listB.get(0);
         }

         entry = entry1;

      }

      if (entry == null) {
         this.flagK = false;
         this.intN = 0;
      } else {
         this.flagK = true;
         int j = Math.max(0, i);
         this.intN = j;
         long l = System.currentTimeMillis();
         this.longL = l;
         long m = entry.longB;
         this.longM = m;
      }
   }

   private void methodY() {
      this.flagR = false;
      this.intS = 0;
      this.intU = 0;
      this.intW = 0;
      this.flagT = false;
   }

   private void methodA(PacketReceiveEvent packetReceiveEvent) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         if (minecraft.level != null) {
            if (this.isEnabled()) {
               if (!this.flagH) {
                  if (!this.flagI) {
                     try {
                        Packet packet = packetReceiveEvent.getPacket();
                        if (packet instanceof ClientboundBundlePacket clientboundBundlePacket) {
                           boolean bl = false;
                           boolean bl1 = false;

                           for (Packet packet1 : clientboundBundlePacket.subPackets()) {
                              Map map = mapA;
                              Class class_x = packet1.getClass();
                              Integer integer = (Integer)map.get(class_x);
                              if (integer != null) {
                                 int i = integer & 0xFF;
                                 int j = integer >>> 8;
                                 if (i == 7) {
                                    bl = true;
                                 } else if (i == 0 && j != 0) {
                                    int o = this.intH;
                                    if ((j & o) == 0) {
                                       int p = this.intG;
                                       if ((j & p) != 0) {
                                          bl1 = true;
                                       }
                                    }
                                 }
                              }
                           }

                           if (bl) {
                              this.methodZ();
                           } else if (bl1) {
                              packetReceiveEvent.cancel();
                              this.methodA(packet);
                           }
                        } else {
                           Map map1 = mapA;
                           Class class_ = packet.getClass();
                           Integer integer1 = (Integer)map1.get(class_);
                           if (integer1 != null) {
                              int k = integer1 & 0xFF;
                              if (k != 0) {
                                 switch (k) {
                                    case 1:
                                       return;
                                    case 2:
                                       return;
                                    case 3:
                                       return;
                                    case 4:
                                       return;
                                    case 5:
                                       return;
                                    case 6:
                                       this.methodUpperC();
                                       return;
                                    case 7:
                                       this.methodZ();
                                       return;
                                    case 8:
                                       if (!this.isGhostEntitiesSetting() && !this.isBlockDiffSetting()) {
                                          return;
                                       }

                                       this.methodD(packet);
                                       return;
                                 }
                              } else {
                                 int l = integer1 >>> 8;
                                 if (l != 0) {
                                    int q = this.intH;
                                    if ((l & q) == 0) {
                                       int r = this.intG;
                                       if ((l & r) != 0) {
                                          if (packet instanceof ClientboundSetEntityDataPacket clientboundSetEntityDataPacket) {
                                             int m = clientboundSetEntityDataPacket.id();
                                             int s = this.intJ;
                                             if (m == s) {
                                                boolean bl2 = false;

                                                try {
                                                   for (DataValue dataValue : clientboundSetEntityDataPacket.packedItems()) {
                                                      int n = dataValue.id();
                                                      if (n == 0 || n == 6 || n == 8) {
                                                         bl2 = true;
                                                      }

                                                      if (n == 15 && minecraft.player != null && dataValue.value() instanceof Number number) {
                                                         LocalPlayer localPlayer = minecraft.player;
                                                         float f = number.floatValue();
                                                         localPlayer.setAbsorptionAmount(f);
                                                      }
                                                   }
                                                } catch (Exception exception1) {
                                                   bl2 = true;
                                                }

                                                if (!bl2) {
                                                   this.methodC(packet);
                                                   return;
                                                }
                                             }
                                          }

                                          if (packet instanceof ClientboundEntityEventPacket clientboundEntityEventPacket && minecraft.level != null) {
                                             ClientLevel clientLevel = minecraft.level;
                                             Entity entity = clientboundEntityEventPacket.getEntity(clientLevel);
                                             LocalPlayer localPlayer1 = minecraft.player;
                                             if (entity == localPlayer1 && clientboundEntityEventPacket.getEventId() != 9) {
                                                return;
                                             }
                                          }

                                          packetReceiveEvent.cancel();
                                          this.methodA(packet);

                                          try {
                                             if (this.isGhostEntitiesSetting() || this.isBlockDiffSetting()) {
                                                this.methodD(packet);
                                             }
                                          } catch (Exception exception) {
                                          }

                                          if (packet instanceof ClientboundPingPacket) {
                                             this.methodUpperA();
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     } catch (Exception exception2) {
                     }
                  }
               }
            }
         }
      }
   }

   private void methodZ() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft != null) {
         if (minecraft.isSameThread()) {
            if (this.isDisableOnFlagSetting()) {
               this.setEnabled(false);
            } else {
               this.methodUpperC();
            }
         } else {
            Runnable runnable = () -> {
               if (this.isDisableOnFlagSetting()) {
                  this.setEnabled(false);
               } else {
                  this.methodUpperC();
               }
            };
            minecraft.execute(runnable);
         }
      }
   }

   private void methodA(Packet<?> packet) {
      List list = this.listB;
      synchronized (list) {
         List list1 = this.listB;
         Overtick$Entry entry = new Overtick$Entry(packet);
         list1.add(entry);

      }
   }

   private void methodUpperA() {
      String string = this.stringI;
      String string1 = this.stringC;
      if (!string.equals(string1)) {
         this.methodY();
         this.stringC = string;
      }

      if ("Tick".equals(string) && this.flagR) {
         int i = this.intS + 1;
         this.intS = i;
         int o = this.getIntervalSetting();
         int j = Math.max(1, o);
         if (i >= j) {
            this.intS = 0;
            if (!this.methodUpperE()) {
               this.flagR = false;
               this.methodUpperC();
            }
         }
      } else if ("Jetpack".equals(string)) {
         if (this.flagV) {
            int m = this.intW + 1;
            this.intW = m;
            int q = this.getIntervalSetting();
            int n = Math.max(1, q);
            if (m >= n) {
               this.intW = 0;
               if (!this.methodUpperE()) {
                  this.methodUpperC();
               }
            }
         } else {
            this.intW = 0;
         }
      } else if ("Up/Down".equals(string)) {
         if (this.flagT) {
            int k = this.intU + 1;
            this.intU = k;
            int p = this.getDelaySetting();
            int l = Math.max(1, p);
            if (k >= l) {
               this.flagT = false;
               this.intU = 0;
               this.methodUpperC();
               Minecraft minecraft = Minecraft.getInstance();
               if (minecraft != null) {
                  if (minecraft.isSameThread()) {
                     this.setEnabled(false);
                  } else {
                     Runnable runnable = () -> this.setEnabled(false);
                     minecraft.execute(runnable);
                  }
               }
            }
         }
      }
   }

   private void methodA(PacketSendEvent packetSendEvent) {
      if (this.isEnabled()) {
         ;
      }
   }

   public void methodA() {
      if (!this.listZ.isEmpty() || !this.listA.isEmpty()) {
         try {
            ClientPacketListener clientPacketListener = Minecraft.getInstance().getConnection();
            Connection connection1;
            if (clientPacketListener != null) {
               connection1 = clientPacketListener.getConnection();
            } else {
               connection1 = null;
            }

            Connection connection = connection1;
            if (connection != null) {
               for (Packet packet : this.listZ) {
                  connection.send(packet);
               }

               for (Integer integer : this.listA) {
                  int i = integer;
                  ServerboundPongPacket serverboundPongPacket = new ServerboundPongPacket(i);
                  connection.send(serverboundPongPacket);
               }
            }
         } catch (Throwable throwable) {
         }

         this.listZ.clear();
         this.listA.clear();
      }
   }

   private void methodA(EventStartAttack eventStartAttack) {
      if (eventStartAttack != null) {
         try {
            Entity entity = eventStartAttack.getTarget();
            if (entity != null) {
               int i = entity.getId();
               this.intP = i;
            }
         } catch (Exception exception) {
         }
      }
   }

   private boolean methodUpperB() {
      List list = this.listB;
      synchronized (list) {
         Iterator iterator = this.listB.iterator();

         while (iterator.hasNext()) {
            if (((Overtick$Entry)iterator.next()).packetA instanceof ClientboundExplodePacket) {

               return true;
            }
         }

         return false;
      }
   }

   private boolean methodB(Packet<?> packet) {
      return packet instanceof ClientboundPingPacket;
   }

   private void methodC(Packet<?> packet) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         if (minecraft.player.connection != null) {
            try {
               methodA(packet, minecraft);
            } catch (Exception exception) {
            }
         }
      }
   }

   private static <T extends PacketListener> void methodA(Packet<?> packet, Minecraft minecraft) {
      ClientPacketListener clientPacketListener = minecraft.player.connection;
      packet.handle(clientPacketListener);
   }

   private void methodUpperC() {
      List list = this.listB;
      ArrayList arrayList;

      synchronized (list) {
         if (this.listB.isEmpty()) {
            this.methodUpperH();

            return;
         }

         List list1 = this.listB;
         arrayList = new ArrayList(list1);
         this.listB.clear();

      }

      this.methodUpperH();
      this.flagT = false;
      this.flagK = false;
      this.intN = 0;
      this.flagO = false;
      this.flagX = false;
      this.flagH = true;
      Iterator iterator = arrayList.iterator();

      while (iterator.hasNext()) {
         Packet packet = ((Overtick$Entry)iterator.next()).packetA;
         this.methodC(packet);
      }

      this.flagH = false;
   }

   private void methodUpperD() {
      List list = this.listB;
      ArrayList arrayList;

      synchronized (list) {
         List list1 = this.listB;
         arrayList = new ArrayList(list1);
         this.listB.clear();

      }

      this.flagH = true;
      Iterator iterator = arrayList.iterator();

      while (iterator.hasNext()) {
         Packet packet = ((Overtick$Entry)iterator.next()).packetA;
         this.methodC(packet);
      }

      this.flagH = false;
   }

   private boolean methodUpperE() {
      ArrayList arrayList = new ArrayList();
      List list = this.listB;
      synchronized (list) {
         int j = -1;
         int k = 0;

         while (true) {
            int m = k;
            int o = this.listB.size();
            if (m >= o) {
               break;
            }

            List list1 = this.listB;
            int p = k;
            if (((Overtick$Entry)list1.get(p)).packetA instanceof ClientboundExplodePacket) {
               j = k;
               break;
            }

            k++;
         }

         if (j < 0) {

            return false;
         }

         int t = -1;
         int l = j + 1;

         while (true) {
            int n = l;
            int q = this.listB.size();
            if (n >= q) {
               break;
            }

            List list4 = this.listB;
            int i = l;
            Packet packet = ((Overtick$Entry)list4.get(i)).packetA;
            if (this.methodB(packet)) {
               t = l;
               break;
            }

            l++;
         }

         if (t < 0) {

            return false;
         }

         List list5 = this.listB;
         int s = t + 1;
         List list6 = list5.subList(0, s);
         arrayList.addAll(list6);
         List list2 = this.listB;
         int r = t + 1;
         list2.subList(0, r).clear();

      }

      this.flagH = true;
      Iterator iterator = arrayList.iterator();

      while (iterator.hasNext()) {
         Packet packet1 = ((Overtick$Entry)iterator.next()).packetA;
         this.methodC(packet1);
      }

      this.flagH = false;
      List list3 = this.listB;
      synchronized (list3) {
         if (this.listB.isEmpty()) {
            this.methodUpperH();
         }

         return true;
      }
   }

   private void methodUpperF() {
      int i = 0;

      while (true) {
         ArrayList arrayList = new ArrayList();
         List list = this.listB;
         synchronized (list) {
            int j;
            label92: {
               j = -1;
               int k = 0;

               while (true) {
                  int m = k;
                  int p = this.listB.size();
                  if (m >= p) {
                     break;
                  }

                  List list1 = this.listB;
                  int q = k;
                  if (((Overtick$Entry)list1.get(q)).packetA instanceof ClientboundExplodePacket) {
                     j = k;
                     break;
                  }

                  k++;
               }

               if (j < 0) {

                  break;
               }

               try {
                  List list2 = this.listB;
                  int r = j;
                  Optional optional = ((ClientboundExplodePacket)((Overtick$Entry)list2.get(r)).packetA).playerKnockback();
                  if (!optional.isEmpty() && !(((Vec3)optional.get()).y <= 0.0)) {
                     break label92;
                  }
               } catch (Exception exception) {

                  break;
               }

               break;
            }

            int x = -1;
            int l = j + 1;

            while (true) {
               int n = l;
               int s = this.listB.size();
               if (n >= s) {
                  break;
               }

               List list4 = this.listB;
               int u = l;
               Packet packet = ((Overtick$Entry)list4.get(u)).packetA;
               if (this.methodB(packet)) {
                  x = l;
                  break;
               }

               l++;
            }

            if (x < 0) {

               break;
            }

            List list5 = this.listB;
            int w = x + 1;
            List list6 = list5.subList(0, w);
            arrayList.addAll(list6);
            List list3 = this.listB;
            int v = x + 1;
            list3.subList(0, v).clear();

         }

         this.flagH = true;
         Iterator iterator = arrayList.iterator();

         while (iterator.hasNext()) {
            Packet packet1 = ((Overtick$Entry)iterator.next()).packetA;
            this.methodC(packet1);
         }

         this.flagH = false;
         int o = i;
         int t = arrayList.size();
         i = o + t;
      }

      if (i > 0) {
         this.methodUpperH();
      }
   }

   private void methodD(Packet<?> packet) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.level != null) {
         try {
            if (packet instanceof ClientboundBlockUpdatePacket clientboundBlockUpdatePacket) {
               Object object = this.objectC;
               synchronized (object) {
                  Map map = this.mapF;
                  Long long_ = clientboundBlockUpdatePacket.getPos().asLong();
                  BlockState blockState = clientboundBlockUpdatePacket.getBlockState();
                  map.put(long_, blockState);
                  if (this.mapF.size() > 20000) {
                     this.mapF.clear();
                  }

               }
            } else if (packet instanceof ClientboundSectionBlocksUpdatePacket clientboundSectionBlocksUpdatePacket) {
               Object object10 = this.objectC;
               synchronized (object10) {
                  BiConsumer biConsumer = (blockPos, blockState1) -> {
                     Map map5 = this.mapF;
                     Long long_x = blockPos.asLong();
                     map5.put(long_x, blockState1);
                  };
                  clientboundSectionBlocksUpdatePacket.runUpdates(biConsumer);
                  if (this.mapF.size() > 20000) {
                     this.mapF.clear();
                  }

               }
            } else if (packet instanceof ClientboundRemoveEntitiesPacket clientboundRemoveEntitiesPacket) {
               Object object1 = this.objectC;
               synchronized (object1) {
                  IntListIterator intListIterator = clientboundRemoveEntitiesPacket.getEntityIds().iterator();

                  while (intListIterator.hasNext()) {
                     int i = (Integer)intListIterator.next();
                     Map map1 = this.mapD;
                     Integer integer = i;
                     map1.remove(integer);
                  }

               }
            } else if (packet instanceof ClientboundMoveEntityPacket clientboundMoveEntityPacket) {
               ClientLevel clientLevel = minecraft.level;
               Entity entity = clientboundMoveEntityPacket.getEntity(clientLevel);
               if (entity != null) {
                  LocalPlayer localPlayer = minecraft.player;
                  if (entity != localPlayer) {
                     int j = entity.getId();
                     Object object2 = this.objectC;
                     synchronized (object2) {
                        Overtick$GhostPos ghostPos = this.methodA(j, entity);
                        if (ghostPos == null) {

                        } else {
                           if (clientboundMoveEntityPacket.hasPosition()) {
                              double d = ghostPos.doubleM;
                              double bk = clientboundMoveEntityPacket.getXa() / 4096.0;
                              double e = d + bk;
                              ghostPos.doubleM = e;
                              double f = ghostPos.doubleN;
                              double bm = clientboundMoveEntityPacket.getYa() / 4096.0;
                              double g = f + bm;
                              ghostPos.doubleN = g;
                              double h = ghostPos.doubleO;
                              double bn = clientboundMoveEntityPacket.getZa() / 4096.0;
                              double p = h + bn;
                              ghostPos.doubleO = p;
                           }

                           if (clientboundMoveEntityPacket.hasRotation()) {
                              float q = clientboundMoveEntityPacket.getYRot();
                              ghostPos.floatP = q;
                              float r = clientboundMoveEntityPacket.getXRot();
                              ghostPos.floatQ = r;
                              float s = clientboundMoveEntityPacket.getYRot();
                              ghostPos.floatR = s;
                           }

                        }
                     }
                  }
               }
            } else if (packet instanceof ClientboundEntityPositionSyncPacket clientboundEntityPositionSyncPacket) {
               int o = clientboundEntityPositionSyncPacket.id();
               int bd = this.intJ;
               if (o != bd) {
                  Object object9 = this.objectC;
                  synchronized (object9) {
                     Entity entity3 = minecraft.level.getEntity(o);
                     Overtick$GhostPos ghostPos6 = this.methodA(o, entity3);
                     if (ghostPos6 == null) {

                     } else {
                        PositionMoveRotation positionMoveRotation2 = clientboundEntityPositionSyncPacket.values();
                        Vec3 vec31 = positionMoveRotation2.position();
                        double be = vec31.x;
                        ghostPos6.doubleM = be;
                        double bf = vec31.y;
                        ghostPos6.doubleN = bf;
                        double bg = vec31.z;
                        ghostPos6.doubleO = bg;
                        float bh = positionMoveRotation2.yRot();
                        ghostPos6.floatP = bh;
                        float bi = positionMoveRotation2.xRot();
                        ghostPos6.floatQ = bi;
                        float bj = positionMoveRotation2.yRot();
                        ghostPos6.floatR = bj;

                     }
                  }
               }
            } else if (packet instanceof ClientboundTeleportEntityPacket clientboundTeleportEntityPacket) {
               int n = clientboundTeleportEntityPacket.id();
               int w = this.intJ;
               if (n != w) {
                  Object object8 = this.objectC;
                  synchronized (object8) {
                     Entity entity2 = minecraft.level.getEntity(n);
                     Overtick$GhostPos ghostPos5 = this.methodA(n, entity2);
                     if (ghostPos5 == null) {

                     } else {
                        double bo = ghostPos5.doubleM;
                        double br = ghostPos5.doubleN;
                        double bs = ghostPos5.doubleO;
                        Vec3 vec32 = new Vec3(bo, br, bs);
                        Vec3 vec33 = Vec3.ZERO;
                        float bp = ghostPos5.floatP;
                        float bq = ghostPos5.floatQ;
                        PositionMoveRotation positionMoveRotation = new PositionMoveRotation(vec32, vec33, bp, bq);
                        PositionMoveRotation positionMoveRotation3 = clientboundTeleportEntityPacket.change();
                        Set set = clientboundTeleportEntityPacket.relatives();
                        PositionMoveRotation positionMoveRotation1 = PositionMoveRotation.calculateAbsolute(positionMoveRotation, positionMoveRotation3, set);
                        Vec3 vec3 = positionMoveRotation1.position();
                        double x = vec3.x;
                        ghostPos5.doubleM = x;
                        double y = vec3.y;
                        ghostPos5.doubleN = y;
                        double z = vec3.z;
                        ghostPos5.doubleO = z;
                        float ba = positionMoveRotation1.yRot();
                        ghostPos5.floatP = ba;
                        float bb = positionMoveRotation1.xRot();
                        ghostPos5.floatQ = bb;
                        float bc = positionMoveRotation1.yRot();
                        ghostPos5.floatR = bc;

                     }
                  }
               }
            } else if (packet instanceof ClientboundRotateHeadPacket clientboundRotateHeadPacket) {
               ClientLevel clientLevel1 = minecraft.level;
               Entity entity1 = clientboundRotateHeadPacket.getEntity(clientLevel1);
               if (entity1 != null) {
                  LocalPlayer localPlayer1 = minecraft.player;
                  if (entity1 != localPlayer1) {
                     Object object7 = this.objectC;
                     synchronized (object7) {
                        int u = entity1.getId();
                        Overtick$GhostPos ghostPos4 = this.methodA(u, entity1);
                        if (ghostPos4 != null) {
                           float v = clientboundRotateHeadPacket.getYHeadRot();
                           ghostPos4.floatR = v;
                        }

                     }
                  }
               }
            } else if (packet instanceof ClientboundAnimatePacket clientboundAnimatePacket) {
               Object object3 = this.objectC;
               synchronized (object3) {
                  Map map2 = this.mapD;
                  Integer integer1 = clientboundAnimatePacket.getId();
                  Overtick$GhostPos ghostPos1 = (Overtick$GhostPos)map2.get(integer1);
                  if (ghostPos1 == null) {

                  } else {
                     int k = clientboundAnimatePacket.getAction();
                     if (k == 0) {
                        this.methodA(ghostPos1, true);
                     } else if (k == 3) {
                        this.methodA(ghostPos1, false);
                     } else if (k == 1) {
                        ghostPos1.intY = 10;
                     }

                  }
               }
            } else if (packet instanceof ClientboundHurtAnimationPacket clientboundHurtAnimationPacket) {
               Object object4 = this.objectC;
               synchronized (object4) {
                  Map map3 = this.mapD;
                  Integer integer2 = clientboundHurtAnimationPacket.id();
                  Overtick$GhostPos ghostPos2 = (Overtick$GhostPos)map3.get(integer2);
                  if (ghostPos2 != null) {
                     ghostPos2.intY = 10;
                  }

               }
            } else if (packet instanceof ClientboundSetEntityDataPacket clientboundSetEntityDataPacket) {
               int l = clientboundSetEntityDataPacket.id();
               int t = this.intJ;
               if (l != t) {
                  Object object5 = this.objectC;
                  synchronized (object5) {
                     Map map4 = this.mapD;
                     Integer integer3 = clientboundSetEntityDataPacket.id();
                     Overtick$GhostPos ghostPos3 = (Overtick$GhostPos)map4.get(integer3);
                     if (ghostPos3 == null) {

                     } else {
                        try {
                           for (DataValue dataValue : clientboundSetEntityDataPacket.packedItems()) {
                              int m = dataValue.id();
                              if (m == 0) {
                                 Object object6 = dataValue.value();
                                 if (object6 instanceof Byte) {
                                    byte b = (Byte)object6;
                                    boolean bl2;
                                    if ((b & 2) != 0) {
                                       bl2 = true;
                                    } else {
                                       bl2 = false;
                                    }

                                    ghostPos3.flagZ = bl2;
                                    boolean bl3;
                                    if ((b & 8) != 0) {
                                       bl3 = true;
                                    } else {
                                       bl3 = false;
                                    }

                                    ghostPos3.flagA = bl3;
                                    boolean bl1;
                                    if ((b & 16) != 0) {
                                       bl1 = true;
                                    } else {
                                       bl1 = false;
                                    }

                                    ghostPos3.flagB = bl1;
                                    boolean bl;
                                    if ((b & 128) != 0) {
                                       bl = true;
                                    } else {
                                       bl = false;
                                    }

                                    ghostPos3.flagC = bl;
                                    continue;
                                 }
                              }

                              if (m == 6 && dataValue.value() instanceof Pose pose) {
                                 ghostPos3.poseD = pose;
                              }
                           }
                        } catch (Exception exception) {
                        }

                     }
                  }
               }
            }
         } catch (Exception exception1) {
         }
      }
   }

   private void methodA(Overtick$GhostPos ghostPos, boolean bl) {
      if (!ghostPos.flagT || ghostPos.intU >= 3 || ghostPos.intU < 0) {
         ghostPos.intU = -1;
         ghostPos.flagT = true;
         HumanoidArm humanoidArm;
         if (bl) {
            humanoidArm = HumanoidArm.RIGHT;
         } else {
            humanoidArm = HumanoidArm.LEFT;
         }

         ghostPos.humanoidArmX = humanoidArm;
      }
   }

   private void methodUpperG() {
      Object object = this.objectC;
      synchronized (object) {
         for (Entry entry : this.mapD.entrySet()) {
            Overtick$GhostPos ghostPos;
            label62: {
               ghostPos = (Overtick$GhostPos)entry.getValue();
               float t = ghostPos.floatD;
               ghostPos.floatG = t;
               float u = ghostPos.floatF;
               ghostPos.floatI = u;
               float v = ghostPos.floatE;
               ghostPos.floatH = v;
               double w = ghostPos.doubleA;
               ghostPos.doubleJ = w;
               double x = ghostPos.doubleB;
               ghostPos.doubleK = x;
               double y = ghostPos.doubleC;
               ghostPos.doubleL = y;
               float z = ghostPos.floatP_P;
               ghostPos.floatQ_Q = z;
               double d = ghostPos.doubleM;
               double cn = ghostPos.doubleH;
               if (d == cn) {
                  double e = ghostPos.doubleN;
                  double co = ghostPos.doubleI;
                  if (e == co) {
                     double f = ghostPos.doubleO;
                     double cp = ghostPos.doubleJ_J;
                     if (f == cp) {
                        break label62;
                     }
                  }
               }

               double ba = ghostPos.doubleM;
               ghostPos.doubleH = ba;
               double bb = ghostPos.doubleN;
               ghostPos.doubleI = bb;
               double bc = ghostPos.doubleO;
               ghostPos.doubleJ_J = bc;
               ghostPos.intK = 3;
            }

            if (ghostPos.intK > 0) {
               double bd = ghostPos.doubleA;
               double dh = ghostPos.doubleH;
               double do_xxxxx = ghostPos.doubleA;
               double di = dh - do_xxxxx;
               double do_x = ghostPos.intK;
               double dj = di / do_x;
               double be = bd + dj;
               ghostPos.doubleA = be;
               double bf = ghostPos.doubleB;
               double dk = ghostPos.doubleI;
               double do_xx = ghostPos.doubleB;
               double dl = dk - do_xx;
               double do_xxx = ghostPos.intK;
               double dm = dl / do_xxx;
               double bg = bf + dm;
               ghostPos.doubleB = bg;
               double bh = ghostPos.doubleC;
               double dn = ghostPos.doubleJ_J;
               double do_xxxx = ghostPos.doubleC;
               double do_xxxxx = dn - do_xxxx;
               double do_xxxxxx = ghostPos.intK;
               double do_xxxxxxx = do_xxxxx / do_xxxxxx;
               double bi = bh + do_xxxxxxx;
               ghostPos.doubleC = bi;
               int bj = ghostPos.intK - 1;
               ghostPos.intK = bj;
            }

            label55: {
               float g = ghostPos.floatP;
               float bk = ghostPos.floatL;
               if (g == bk) {
                  float h = ghostPos.floatQ;
                  float bl = ghostPos.floatM;
                  if (h == bl) {
                     float i = ghostPos.floatR;
                     float bm = ghostPos.floatN;
                     if (i == bm) {
                        break label55;
                     }
                  }
               }

               float bn = ghostPos.floatP;
               ghostPos.floatL = bn;
               float bo = ghostPos.floatQ;
               ghostPos.floatM = bo;
               float bp = ghostPos.floatR;
               ghostPos.floatN = bp;
               ghostPos.intO = 3;
            }

            if (ghostPos.intO > 0) {
               float bq = ghostPos.floatD;
               float cq = ghostPos.floatL;
               float do_ = ghostPos.floatD;
               float cr = Mth.wrapDegrees(cq - do_);
               float do_x = ghostPos.intO;
               float cs = cr / do_x;
               float br = bq + cs;
               ghostPos.floatD = br;
               float bs = ghostPos.floatE;
               float ct = ghostPos.floatM;
               float do_xx = ghostPos.floatE;
               float cu = ct - do_xx;
               float do_xxx = ghostPos.intO;
               float cv = cu / do_xxx;
               float bt = bs + cv;
               ghostPos.floatE = bt;
               float bu = ghostPos.floatF;
               float cw = ghostPos.floatN;
               float do_xxxx = ghostPos.floatF;
               float cx = Mth.wrapDegrees(cw - do_xxxx);
               float do_xxxxx = ghostPos.intO;
               float cy = cx / do_xxxxx;
               float bv = bu + cy;
               ghostPos.floatF = bv;
               int bw = ghostPos.intO - 1;
               ghostPos.intO = bw;
            }

            float j = ghostPos.floatF;
            float bx = ghostPos.floatP_P;
            float k = Mth.wrapDegrees(j - bx);
            float by = ghostPos.floatP_P;
            float cz = k * 0.3F;
            float bz = by + cz;
            ghostPos.floatP_P = bz;
            float l = ghostPos.floatF;
            float ca = ghostPos.floatP_P;
            float m = Mth.wrapDegrees(l - ca);
            if (Math.abs(m) > 50.0F) {
               float cb = ghostPos.floatP_P;
               float do_x = Math.signum(m) * 50.0F;
               float da = m - do_x;
               float cc = cb + da;
               ghostPos.floatP_P = cc;
            }

            float cd = Mth.wrapDegrees(ghostPos.floatP_P);
            ghostPos.floatP_P = cd;
            long ce = System.nanoTime();
            ghostPos.longG = ce;
            Map map = this.mapE;
            Integer integer = (Integer)entry.getKey();
            double[] db = new double[3];
            double do_xxxxxxxxxxxxxxxx = ghostPos.doubleA;
            db[0] = do_xxxxxxxxxxxxxxxx;
            double do_x = ghostPos.doubleB;
            db[1] = do_x;
            double do_xx = ghostPos.doubleC;
            db[2] = do_xx;
            map.put(integer, db);
            double n = ghostPos.doubleA;
            double dc = ghostPos.doubleJ;
            double o = n - dc;
            double p = ghostPos.doubleC;
            double dd = ghostPos.doubleL;
            double q = p - dd;
            double r = o * o;
            double de = q * q;
            float s = (float)Math.sqrt(r + de);
            WalkAnimationState walkAnimationState = ghostPos.walkAnimationStateS;
            float cf = Math.min(s * 4.0F, 1.0F);
            walkAnimationState.update(cf, 0.4F, 1.0F);
            float cg = ghostPos.floatV;
            ghostPos.floatW = cg;
            if (ghostPos.flagT) {
               int cm = ghostPos.intU + 1;
               ghostPos.intU = cm;
               if (ghostPos.intU >= 6) {
                  ghostPos.intU = 0;
                  ghostPos.flagT = false;
               }
            } else {
               ghostPos.intU = 0;
            }

            float ch = ghostPos.intU / 6.0F;
            ghostPos.floatV = ch;
            if (ghostPos.intY > 0) {
               int ci = ghostPos.intY - 1;
               ghostPos.intY = ci;
            }

            float cj = ghostPos.floatE_E;
            float df;
            if (ghostPos.flagB) {
               df = 1.0F;
            } else {
               df = 0.0F;
            }

            float do_xxx = ghostPos.floatE_E;
            float dg = (df - do_xxx) * 0.1F;
            float ck = cj + dg;
            ghostPos.floatE_E = ck;
            int cl = ghostPos.intF + 1;
            ghostPos.intF = cl;
         }

      }
   }

   private Overtick$GhostPos methodA(int i, Entity entity) {
      Map map = this.mapD;
      Integer integer = i;
      Overtick$GhostPos ghostPos = (Overtick$GhostPos)map.get(integer);
      if (ghostPos == null) {
         if (entity == null) {
            return null;
         }

         Map map1 = this.mapE;
         Integer integer1 = i;
         double[] d = (double[])map1.get(integer1);
         if (d != null) {
            double e = d[0];
            double g = d[1];
            double j = d[2];
            float l = entity.getYRot();
            float n = entity.getXRot();
            float p = entity.getYHeadRot();
            Overtick$GhostPos ghostPos1 = new Overtick$GhostPos(e, g, j, l, n, p);
            ghostPos = ghostPos1;
         } else {
            double f = entity.getX();
            double h = entity.getY();
            double k = entity.getZ();
            float m = entity.getYRot();
            float o = entity.getXRot();
            float q = entity.getYHeadRot();
            Overtick$GhostPos ghostPos2 = new Overtick$GhostPos(f, h, k, m, o, q);
            ghostPos = ghostPos2;
         }

         Map map2 = this.mapD;
         Integer integer2 = i;
         Overtick$GhostPos ghostPos3 = ghostPos;
         map2.put(integer2, ghostPos3);
      }

      return ghostPos;
   }

   private void methodUpperH() {
   }

   private void methodUpperI() {
      Object object = this.objectC;
      synchronized (object) {
         this.mapD.clear();
         this.mapE.clear();
         this.mapF.clear();

      }

      IdentityHashMap identityHashMap = new IdentityHashMap();
      Set set = Collections.newSetFromMap(identityHashMap);
      this.setG = set;
   }

   public boolean methodA(Object object) {
      boolean bl;
      if (object != null && this.setG.contains(object)) {
         bl = true;
      } else {
         bl = false;
      }

      return bl;
   }

   public int methodB() {
      float f = this.getGhostOpacitySetting();
      if (f < 0.0F) {
         f = 0.0F;
      }

      if (f > 1.0F) {
         f = 1.0F;
      }

      return (int)(f * 255.0F + 0.5F);
   }

   public void methodA(LevelRenderState levelRenderState, float f) {
      if (this.isEnabled()) {
         if (this.isGhostEntitiesSetting()) {
            if (this.flagJ) {
               if (levelRenderState != null) {
                  Minecraft minecraft = Minecraft.getInstance();
                  if (minecraft != null) {
                     if (minecraft.level != null) {
                        if (minecraft.player != null) {
                           Object object = this.objectC;
                           ArrayList arrayList;

                           synchronized (object) {
                              if (this.mapD.isEmpty()) {
                                 IdentityHashMap identityHashMap1 = new IdentityHashMap();
                                 Set set1 = Collections.newSetFromMap(identityHashMap1);
                                 this.setG = set1;

                                 return;
                              }

                              Set set2 = this.mapD.entrySet();
                              arrayList = new ArrayList(set2);

                           }

                           EntityRenderDispatcher entityRenderDispatcher = minecraft.getEntityRenderDispatcher();
                           double d = 0.0;
                           double e = 0.0;
                           double g = 0.0;

                           try {
                              if (levelRenderState.cameraRenderState != null && levelRenderState.cameraRenderState.pos != null) {
                                 Vec3 vec3 = levelRenderState.cameraRenderState.pos;
                                 d = vec3.x;
                                 e = vec3.y;
                                 g = vec3.z;
                              }
                           } catch (Exception exception2) {
                           }

                           IdentityHashMap identityHashMap = new IdentityHashMap();
                           Set set = Collections.newSetFromMap(identityHashMap);

                           for (Entry entry : arrayList) {
                              ClientLevel clientLevel = minecraft.level;
                              int q = (Integer)entry.getKey();
                              Entity entity = clientLevel.getEntity(q);
                              if (entity != null) {
                                 LocalPlayer localPlayer = minecraft.player;
                                 if (entity != localPlayer) {
                                    Overtick$GhostPos ghostPos = (Overtick$GhostPos)entry.getValue();

                                    try {
                                       EntityRenderState entityRenderState = entityRenderDispatcher.extractEntity(entity, f);
                                       if (entityRenderState != null) {
                                          double h = f;
                                          double bg = ghostPos.doubleJ;
                                          double bu = ghostPos.doubleA;
                                          double i = Mth.lerp(h, bg, bu);
                                          double j = f;
                                          double bh = ghostPos.doubleK;
                                          double bv = ghostPos.doubleB;
                                          double k = Mth.lerp(j, bh, bv);
                                          double l = f;
                                          double bi = ghostPos.doubleL;
                                          double bw = ghostPos.doubleC;
                                          double m = Mth.lerp(l, bi, bw);
                                          entityRenderState.x = i;
                                          entityRenderState.y = k;
                                          entityRenderState.z = m;
                                          entityRenderState.nameTag = null;
                                          entityRenderState.scoreText = null;
                                          entityRenderState.shadowRadius = 0.0F;

                                          try {
                                             entityRenderState.shadowPieces.clear();
                                          } catch (Exception exception) {
                                          }

                                          double r = d - i;
                                          double bo = d - i;
                                          double s = r * bo;
                                          double bp = e - k;
                                          double bx = e - k;
                                          double bq = bp * bx;
                                          double t = s + bq;
                                          double br = g - m;
                                          double by = g - m;
                                          double bs = br * by;
                                          double u = t + bs;
                                          entityRenderState.distanceToCameraSq = u;
                                          if (entityRenderState instanceof LivingEntityRenderState livingEntityRenderState) {
                                             livingEntityRenderState.isInvisible = false;
                                             livingEntityRenderState.isInvisibleToPlayer = false;
                                             float v = ghostPos.floatQ_Q;
                                             float bj = ghostPos.floatP_P;
                                             float n = Mth.rotLerp(f, v, bj);
                                             float w = ghostPos.floatI;
                                             float bk = ghostPos.floatF;
                                             float o = Mth.rotLerp(f, w, bk);
                                             livingEntityRenderState.bodyRot = n;
                                             float x = Mth.wrapDegrees(o - n);
                                             livingEntityRenderState.yRot = x;
                                             float bm = ghostPos.floatH;
                                             float bt = ghostPos.floatE;
                                             float y = Mth.lerp(f, bm, bt);
                                             livingEntityRenderState.xRot = y;
                                             float z = ghostPos.walkAnimationStateS.position(f);
                                             livingEntityRenderState.walkAnimationPos = z;
                                             float ba = ghostPos.walkAnimationStateS.speed(f);
                                             livingEntityRenderState.walkAnimationSpeed = ba;
                                             float bb = ghostPos.intF + f;
                                             livingEntityRenderState.ageInTicks = bb;
                                             boolean bl3;
                                             if (ghostPos.intY > 0) {
                                                bl3 = true;
                                             } else {
                                                bl3 = false;
                                             }

                                             livingEntityRenderState.hasRedOverlay = bl3;
                                          }

                                          if (entityRenderState instanceof HumanoidRenderState humanoidRenderState) {
                                             boolean bl = ghostPos.flagZ;
                                             humanoidRenderState.isCrouching = bl;
                                             boolean bl1 = ghostPos.flagC;
                                             humanoidRenderState.isFallFlying = bl1;
                                             boolean bl2 = ghostPos.flagB;
                                             humanoidRenderState.isVisuallySwimming = bl2;
                                             float bc = ghostPos.floatE_E;
                                             humanoidRenderState.swimAmount = bc;
                                             Pose pose = ghostPos.poseD;
                                             humanoidRenderState.pose = pose;
                                          }

                                          if (entityRenderState instanceof ArmedEntityRenderState armedEntityRenderState) {
                                             float p = ghostPos.floatV;
                                             float bd = ghostPos.floatW;
                                             float bz = p - bd;
                                             if (bz < 0.0F) {
                                                bz += 1.0F;
                                             }

                                             float be = ghostPos.floatW;
                                             float bn = bz * f;
                                             float bf = be + bn;
                                             armedEntityRenderState.attackTime = bf;
                                             HumanoidArm humanoidArm = ghostPos.humanoidArmX;
                                             armedEntityRenderState.attackArm = humanoidArm;
                                          }

                                          set.add(entityRenderState);
                                          levelRenderState.entityRenderStates.add(entityRenderState);
                                       }
                                    } catch (Exception exception1) {
                                    }
                                 }
                              }
                           }

                           this.setG = set;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private boolean isBlockDiffSetting() {
      Boolean boolean_ = true;
      return this.getSetting("Block Diff", boolean_);
   }

   private String getBlockDiffColorSetting() {
      return this.getSetting("Block Diff Color", "#FF4D4D");
   }

   private void methodA(PoseStack poseStack, float f) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft != null) {
         if (minecraft.level != null) {
            if (this.isBlockDiffSetting()) {
               try {
                  Object object = this.objectC;
                  ArrayList arrayList;

                  synchronized (object) {
                     Set set = this.mapF.keySet();
                     arrayList = new ArrayList(set);

                  }

                  ArrayList arrayList3 = new ArrayList();

                  for (Long long_ : arrayList) {
                     Object object2 = this.objectC;
                     Object object3;

                     synchronized (object2) {
                        object3 = this.mapF.get(long_);

                     }

                     if (object3 instanceof BlockState blockState) {
                        BlockPos blockPos = BlockPos.of(long_);
                        if (minecraft.level.getBlockState(blockPos) != blockState) {
                           double bb = blockPos.getX();
                           double bj = blockPos.getY();
                           double bo = blockPos.getZ();
                           double bq = blockPos.getX() + 1;
                           double br = blockPos.getY() + 1;
                           double bs = blockPos.getZ() + 1;
                           AABB aABB2 = new AABB(bb, bj, bo, bq, br, bs);
                           arrayList3.add(aABB2);
                        }
                     }
                  }

                  if (!arrayList3.isEmpty()) {
                     int p = methodB(this.getBlockDiffColorSetting());
                     int z = 1845493760 | p;
                     int bc = 0xFF000000 | p;
                     BoxRenderer.methodA(poseStack, arrayList3, z, bc);
                  }
               } catch (Exception exception1) {
               }
            }

            if (this.isGhostEntitiesSetting()) {
               if (this.isGhostESPSetting()) {
                  if (this.flagJ) {
                     Object object1 = this.objectC;
                     ArrayList arrayList1;

                     synchronized (object1) {
                        if (this.mapD.isEmpty()) {

                           return;
                        }

                        Set set1 = this.mapD.entrySet();
                        arrayList1 = new ArrayList(set1);

                     }

                     try {
                        int i = methodB(this.getGhostColorSetting());
                        int t = this.getGhostAlphaSetting();
                        int q = Math.min(255, t);
                        int j = Math.max(0, q);
                        int k = Math.min(120, j) << 24 | i;
                        int r = j + 140;
                        int l = Math.min(255, r) << 24 | i;
                        int u = arrayList1.size();
                        ArrayList arrayList2 = new ArrayList(u);

                        for (Entry entry : arrayList1) {
                           ClientLevel clientLevel = minecraft.level;
                           int s = (Integer)entry.getKey();
                           Entity entity = clientLevel.getEntity(s);
                           if (entity != null) {
                              Overtick$GhostPos ghostPos = (Overtick$GhostPos)entry.getValue();
                              double d = f;
                              double v = ghostPos.doubleJ;
                              double bd = ghostPos.doubleA;
                              double e = Mth.lerp(d, v, bd);
                              double g = f;
                              double w = ghostPos.doubleK;
                              double be = ghostPos.doubleB;
                              double h = Mth.lerp(g, w, be);
                              double m = f;
                              double x = ghostPos.doubleL;
                              double bf = ghostPos.doubleC;
                              double n = Mth.lerp(m, x, bf);
                              AABB aABB = entity.getBoundingBox();
                              double bg = entity.getX();
                              double y = e - bg;
                              double bk = entity.getY();
                              double bh = h - bk;
                              double bp = entity.getZ();
                              double bl = n - bp;
                              AABB aABB1 = aABB.move(y, bh, bl);
                              arrayList2.add(aABB1);
                              double o = entity.getBbHeight() * 0.5;
                              double ba = entity.getX();
                              double bi = entity.getY() + o;
                              double bn = entity.getZ();
                              Vec3 vec3 = new Vec3(ba, bi, bn);
                              double bm = h + o;
                              Vec3 vec31 = new Vec3(e, bm, n);
                              BoxRenderer.methodA(poseStack, vec3, vec31, l, 2.0F);
                           }
                        }

                        if (!arrayList2.isEmpty()) {
                           BoxRenderer.methodA(poseStack, arrayList2, k, l);
                        }
                     } catch (Exception exception) {
                     }
                  }
               }
            }
         }
      }
   }

   private static int methodB(String string) {
      return ThemeColors.resolveRgb(string, 6599935, false);
   }

   static {
      HashMap hashMap = new HashMap();
      mapA = hashMap;
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundSystemChatPacket"), 1, 0);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundPlayerChatPacket"), 2, 0);
      methodA(methodA("net.minecraft.network.protocol.common.ClientboundDisconnectPacket"), 3, 0);
      methodA(methodA("net.minecraft.network.protocol.common.ClientboundKeepAlivePacket"), 5, 0);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundRespawnPacket"), 6, 0);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket"), 7, 0);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundAnimatePacket"), 8, 0);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundSoundPacket"), 4, 0);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundSoundEntityPacket"), 4, 0);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundStopSoundPacket"), 4, 0);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket"), 4, 0);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundLevelEventPacket"), 4, 0);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundBossEventPacket"), 4, 0);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundTabListPacket"), 4, 0);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket"), 4, 0);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket"), 4, 0);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket"), 4, 0);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundClearTitlesPacket"), 4, 0);
      methodA(methodA("net.minecraft.network.protocol.common.ClientboundPingPacket"), 0, 1);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundMoveEntityPacket$Pos"), 0, 2);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundMoveEntityPacket$PosRot"), 0, 2);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundMoveEntityPacket$Rot"), 0, 2);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket"), 0, 2);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket"), 0, 2);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundRotateHeadPacket"), 0, 2);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundAddEntityPacket"), 0, 4);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket"), 0, 4);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundSetPassengersPacket"), 0, 4);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket"), 0, 4);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket"), 0, 8);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket"), 0, 8);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket"), 0, 8);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket"), 0, 8);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket"), 0, 8);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket"), 0, 16);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundExplodePacket"), 0, 16);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket"), 0, 32);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket"), 0, 32);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket"), 0, 32);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundBlockEventPacket"), 0, 32);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundBlockChangedAckPacket"), 0, 32);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundGameEventPacket"), 0, 64);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket"), 0, 64);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundSetCameraPacket"), 0, 8192);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket"), 0, 128);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket"), 0, 128);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket"), 0, 4096);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundCooldownPacket"), 0, 2048);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundEntityEventPacket"), 0, 256);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundHurtAnimationPacket"), 0, 256);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundSetHealthPacket"), 0, 512);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundSetExperiencePacket"), 0, 1024);
      methodA(methodA("net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket"), 0, 16384);
   }
}
