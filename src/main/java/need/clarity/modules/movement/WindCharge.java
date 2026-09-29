package need.clarity.modules.movement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import need.clarity.PerPacketEvents;
import need.clarity.PerPacketSendEvents;
import need.clarity.event.PacketReceiveEvent;
import need.clarity.event.PacketSendEvent;
import need.clarity.modules.ModuleBase;
import need.clarity.modules.combat.AimAssist;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundPingPacket;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.Pos;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.PosRot;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.Rot;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.StatusOnly;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class WindCharge extends ModuleBase {
   public static WindCharge instance;
   private static final Map<Class<?>, Integer> mapB;
   private final List<Packet<?>> listC;
   private volatile boolean flagD;
   private String stringE;
   private int intF;
   private int intG;
   private int intH;
   private boolean flagI;
   private float floatJ;
   private float floatK;
   private float floatL;
   private double doubleM;
   private double doubleN;
   private double doubleO;
   private int intP;
   private int intQ;

   private static void methodA(Class<?> class_) {
      if (class_ != null) {
         Map map = mapB;
         Integer integer = 0;
         map.put(class_, integer);
      }
   }

   public WindCharge() {
      super("WindCharge", "Movement");
      ArrayList arrayList = new ArrayList();
      this.listC = arrayList;
      this.flagD = false;
      this.stringE = "IDLE";
      this.intF = 0;
      this.intG = 0;
      this.intH = 0;
      this.flagI = false;
      this.intP = -1;
      this.intQ = -1;
      instance = this;
      PerPacketEvents.register(this::methodA);
      PerPacketSendEvents.register(this::methodA);
   }

   private String getModeSetting() {
      return this.getSetting("Mode", "Delay");
   }

   private boolean isSilentAimSetting() {
      Boolean boolean_ = true;
      return this.getSetting("Silent Aim", boolean_);
   }

   private float getTargetPitchSetting() {
      Float float_ = 90.0F;
      return this.getSetting("Target Pitch", float_);
   }

   private int getAimTicksSetting() {
      Integer integer = 1;
      int i = this.getSetting("Aim Ticks", integer);
      return Math.max(1, i);
   }

   private int getRestoreTicksSetting() {
      Integer integer = 1;
      int i = this.getSetting("Restore Ticks", integer);
      return Math.max(1, i);
   }

   private int getShotDelaySetting() {
      Integer integer = 10;
      int i = this.getSetting("Shot Delay", integer);
      return Math.max(0, i);
   }

   private boolean isComboSetting() {
      Boolean boolean_ = true;
      return this.getSetting("Combo", boolean_);
   }

   private int getComboCountSetting() {
      Integer integer = 3;
      int j = this.getSetting("Combo Count", integer);
      int i = Math.max(2, j);
      return Math.min(10, i);
   }

   private int getMultiUseSetting() {
      Integer integer = 1;
      int i = this.getSetting("Multi Use", integer);
      return Math.max(1, i);
   }

   private boolean isJumpSetting() {
      Boolean boolean_ = true;
      return this.getSetting("Jump", boolean_);
   }

   private int getJumpDelaySetting() {
      Integer integer = 3;
      int i = this.getSetting("Jump Delay", integer);
      return Math.max(0, i);
   }

   private boolean isElytraSetting() {
      Boolean boolean_ = false;
      return this.getSetting("Elytra", boolean_);
   }

   private int getElytraTicksSetting() {
      Integer integer = 40;
      int i = this.getSetting("Elytra Ticks", integer);
      return Math.max(1, i);
   }

   private int getUpRangeSetting() {
      Integer integer = 3;
      int i = this.getSetting("Up Range", integer);
      return Math.max(1, i);
   }

   private int getDelaySetting() {
      Integer integer = 10;
      int i = this.getSetting("Delay", integer);
      return Math.max(1, i);
   }

   @Override
   public void onEnable() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player == null) {
         this.setEnabled(false);
      } else {
         int i = this.methodQ();
         this.intP = i;
         if (this.intP < 0) {
            this.setEnabled(false);
         } else {
            float f = minecraft.player.getYRot();
            this.floatJ = f;
            float g = minecraft.player.getXRot();
            this.floatK = g;
            Vec3 vec3 = minecraft.player.position();
            double d = vec3.x;
            this.doubleM = d;
            double e = vec3.y;
            this.doubleN = e;
            double h = vec3.z;
            this.doubleO = h;
            boolean bl = this.methodP();
            this.flagI = bl;
            float j;
            if (this.flagI) {
               j = -90.0F;
            } else {
               float l = this.getTargetPitchSetting();
               float k = Math.min(90.0F, l);
               j = Math.max(-90.0F, k);
            }

            this.floatL = j;
            this.stringE = "AIM";
            this.intF = 0;
            this.intG = 0;
            this.intH = 0;
            this.listC.clear();
         }
      }
   }

   @Override
   public void onDisable() {
      Minecraft minecraft = Minecraft.getInstance();
      if (!this.flagD) {
         this.methodS();
      }

      if (minecraft.player != null) {
         int i = this.intQ;
         this.methodA(i);
         if (this.isSilentAimSetting()) {
            AimAssist.methodE();
         }
      }

      this.stringE = "IDLE";
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         if (minecraft.level != null) {
            String string = this.stringE;
            byte b = -1;
            int by = string.hashCode();
            switch (by) {
               case 64805:
                  if (string.equals("AIM")) {
                     b = 0;
                  }
                  break;
               case 2104194:
                  if (string.equals("DONE")) {
                     b = 7;
                  }
                  break;
               case 2158134:
                  if (string.equals("FIRE")) {
                     b = 1;
                  }
                  break;
               case 2288686:
                  if (string.equals("JUMP")) {
                     b = 2;
                  }
                  break;
               case 2656629:
                  if (string.equals("WAIT")) {
                     b = 5;
                  }
                  break;
               case 1808577511:
                  if (string.equals("RELEASE")) {
                     b = 4;
                  }
                  break;
               case 1815502446:
                  if (string.equals("RESTORE")) {
                     b = 6;
                  }
                  break;
               case 2048333745:
                  if (string.equals("ELYTRA")) {
                     b = 3;
                  }
            }

            byte c = b;
            switch (c) {
               case 0:
                  int be = this.intF + 1;
                  this.intF = be;
                  if (this.isSilentAimSetting()) {
                     AimAssist.getFloatE();
                  } else {
                     minecraft.player.getXRot();
                  }

                  if (!this.isSilentAimSetting()) {
                     minecraft.player.getXRot();
                  }

                  float f = this.floatK;
                  float bf = this.intF;
                  float bs = this.getAimTicksSetting();
                  float bg = bf / bs;
                  float g = Math.min(1.0F, bg);
                  float bh = (this.floatL - f) * g;
                  float h = f + bh;
                  float bi = this.floatJ;
                  float bt = this.methodA(h);
                  this.methodA(bi, bt);
                  int o = this.intF;
                  int bj = this.getAimTicksSetting();
                  if (o >= bj) {
                     this.intF = 0;
                     this.stringE = "FIRE";
                     return;
                  }

                  return;
               case 1:
                  if (this.intF == 0) {
                     int x = this.intP;
                     this.methodA(x);
                     int i = 0;

                     while (true) {
                        int l = i;
                        int y = this.getMultiUseSetting();
                        if (l >= y) {
                           int z = this.intQ;
                           this.methodA(z);
                           int ba = this.intG + 1;
                           this.intG = ba;
                           break;
                        }

                        this.methodA(minecraft);
                        i++;
                     }
                  }

                  int bb = this.intF + 1;
                  this.intF = bb;
                  int m = this.intF;
                  int bc = this.getShotDelaySetting() + 1;
                  if (m >= bc) {
                     this.intF = 0;
                     if (this.isComboSetting()) {
                        int n = this.intG;
                        int bd = this.getComboCountSetting();
                        if (n < bd) {
                           return;
                        }
                     }

                     String string1;
                     if (this.isJumpSetting()) {
                        string1 = "JUMP";
                     } else if (this.isElytraSetting()) {
                        string1 = "ELYTRA";
                     } else {
                        string1 = "RELEASE";
                     }

                     this.stringE = string1;
                     return;
                  } else {
                     return;
                  }
               case 2:
                  int v = this.intF + 1;
                  this.intF = v;
                  int k = this.intF;
                  int w = this.getJumpDelaySetting();
                  if (k >= w) {
                     minecraft.player.jumpFromGround();
                     this.intF = 0;
                     String string2;
                     if (this.isElytraSetting()) {
                        string2 = "ELYTRA";
                     } else {
                        string2 = "RELEASE";
                     }

                     this.stringE = string2;
                     return;
                  }

                  return;
               case 3:
                  int t = this.intF + 1;
                  this.intF = t;
                  int j = this.intF;
                  int u = this.getElytraTicksSetting();
                  if (j >= u) {
                     this.intF = 0;
                     this.stringE = "RELEASE";
                     return;
                  }

                  return;
               case 4:
                  if (this.intF == 0) {
                     this.methodR();
                     this.stringE = "WAIT";
                     return;
                  }

                  return;
               case 5:
                  return;
               case 6:
                  int bk = this.intF + 1;
                  this.intF = bk;
                  float bl = this.intF;
                  float bu = this.getRestoreTicksSetting();
                  float bm = bl / bu;
                  float p = Math.min(1.0F, bm);
                  float q = this.floatL;
                  float bn = this.floatK;
                  float bv = this.floatL;
                  float bo = (bn - bv) * p;
                  float r = q + bo;
                  float bp = this.floatJ;
                  float bw = this.methodA(r);
                  this.methodA(bp, bw);
                  int s = this.intF;
                  int bq = this.getRestoreTicksSetting();
                  if (s >= bq) {
                     float br = this.floatJ;
                     float bx = this.floatK;
                     this.methodA(br, bx);
                     if (this.isSilentAimSetting()) {
                        AimAssist.methodE();
                     }

                     this.stringE = "DONE";
                     return;
                  }

                  return;
               case 7:
                  this.setEnabled(false);
                  return;
            }
         }
      }
   }

   private void methodA(float f, float g) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         if (this.isSilentAimSetting()) {
            AimAssist.methodC(f, g);
         } else {
            minecraft.player.setYRot(f);
            minecraft.player.setXRot(g);
         }
      }
   }

   private float methodA(float f) {
      Minecraft minecraft = Minecraft.getInstance();

      float k;
      try {
         float g = (float)((Double)minecraft.options.sensitivity().get()).doubleValue() * 0.6F + 0.2F;
         k = g * g * g * 8.0F * 0.15F;
      } catch (Throwable throwable) {
         k = 0.15F;
      }

      if (k < 1.0E-4F) {
         return f;
      }

      float i = k;
      float h = Math.round(f / i);
      float j = k;
      return h * j;
   }

   private void methodA(Minecraft minecraft) {
      try {
         ClientPacketListener clientPacketListener = minecraft.player.connection;
         InteractionHand interactionHand = InteractionHand.MAIN_HAND;
         float f = minecraft.player.getYRot();
         float g = minecraft.player.getXRot();
         ServerboundUseItemPacket serverboundUseItemPacket = new ServerboundUseItemPacket(interactionHand, 0, f, g);
         clientPacketListener.send(serverboundUseItemPacket);
         LocalPlayer localPlayer = minecraft.player;
         InteractionHand interactionHand1 = InteractionHand.MAIN_HAND;
         localPlayer.swing(interactionHand1);
      } catch (Throwable throwable) {
      }
   }

   private boolean methodP() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null && minecraft.level != null) {
         BlockPos blockPos = minecraft.player.blockPosition();
         int j = 1;

         while (true) {
            int k = j;
            int l = this.getUpRangeSetting();
            if (k > l) {
               return false;
            }

            ClientLevel clientLevel = minecraft.level;
            int i = j;
            BlockPos blockPos1 = blockPos.above(i);
            BlockState blockState = clientLevel.getBlockState(blockPos1);
            if (blockState != null && !blockState.isAir()) {
               return true;
            }

            j++;
         }
      } else {
         return false;
      }
   }

   private int methodQ() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player == null) {
         return -1;
      }

      Inventory inventory = minecraft.player.getInventory();

      for (int i = 0; i < 9; i++) {
         int j = i;
         ItemStack itemStack = inventory.getItem(j);
         if (!itemStack.isEmpty() && String.valueOf(itemStack.getItem().getDescriptionId()).contains("wind_charge")) {
            return i;
         }
      }

      return -1;
   }

   private void methodA(int i) {
      if (i >= 0) {
         if (i <= 8) {
            Minecraft minecraft = Minecraft.getInstance();
            if (this.intQ == -1 && minecraft.player != null) {
               int j = minecraft.player.getInventory().getSelectedSlot();
               this.intQ = j;
            }

            try {
               ClientPacketListener clientPacketListener = minecraft.player.connection;
               ServerboundSetCarriedItemPacket serverboundSetCarriedItemPacket = new ServerboundSetCarriedItemPacket(i);
               clientPacketListener.send(serverboundSetCarriedItemPacket);
            } catch (Throwable throwable) {
            }
         }
      }
   }

   private void methodA(PacketReceiveEvent packetReceiveEvent) {
      if (this.isEnabled()) {
         if (!this.flagD) {
            try {
               Packet packet = packetReceiveEvent.getPacket();
               if (packet instanceof ClientboundPlayerPositionPacket) {
                  packetReceiveEvent.cancel();
                  this.methodS();
                  this.methodT();
               } else {
                  Map map = mapB;
                  Class class_ = packet.getClass();
                  if (map.containsKey(class_)) {
                     packetReceiveEvent.cancel();
                     List list = this.listC;
                     synchronized (list) {
                        this.listC.add(packet);

                     }

                     if (packet instanceof ClientboundPingPacket) {
                        String string = this.stringE;
                        if ("WAIT".equals(string)) {
                           int i = this.intH + 1;
                           this.intH = i;
                           int j = this.getDelaySetting();
                           if (i >= j) {
                              this.methodS();
                              this.methodT();
                              return;
                           }
                        }
                     }
                  }
               }
            } catch (Throwable throwable1) {
            }
         }
      }
   }

   private void methodA(PacketSendEvent packetSendEvent) {
      if (this.isEnabled()) {
         if (!this.flagD) {
            String string = this.getModeSetting();
            if ("Immediate".equalsIgnoreCase(string)) {
               try {
                  if (packetSendEvent.getPacket() instanceof ServerboundMovePlayerPacket) {
                     packetSendEvent.cancel();
                     Minecraft minecraft = Minecraft.getInstance();
                     if (minecraft.player != null) {
                        ClientPacketListener clientPacketListener = minecraft.player.connection;
                        StatusOnly statusOnly = new StatusOnly(true, false);
                        clientPacketListener.send(statusOnly);
                        return;
                     }
                  }
               } catch (Throwable throwable) {
               }
            }
         }
      }
   }

   private void methodR() {
      List list = this.listC;
      ArrayList arrayList;

      synchronized (list) {
         if (this.listC.isEmpty()) {
            this.stringE = "DONE";

            return;
         }

         int j = this.listC.size() / 2;
         int i = Math.max(1, j);
         List list1 = this.listC.subList(0, i);
         arrayList = new ArrayList(list1);
         this.listC.subList(0, i).clear();

      }

      this.flagD = true;

      for (Packet packet : arrayList) {
         this.methodA(packet);
      }

      this.flagD = false;
   }

   private void methodS() {
      List list = this.listC;
      ArrayList arrayList;

      synchronized (list) {
         List list1 = this.listC;
         arrayList = new ArrayList(list1);
         this.listC.clear();

      }

      this.flagD = true;

      for (Packet packet : arrayList) {
         this.methodA(packet);
      }

      this.flagD = false;
   }

   private void methodA(Packet<?> packet) {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null) {
         if (minecraft.player.connection != null) {
            try {
               methodA(packet, minecraft);
            } catch (Throwable throwable) {
            }
         }
      }
   }

   private static <T extends PacketListener> void methodA(Packet<?> packet, Minecraft minecraft) {
      ClientPacketListener clientPacketListener = minecraft.player.connection;
      packet.handle(clientPacketListener);
   }

   private void methodT() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.isSameThread()) {
         this.stringE = "RESTORE";
         this.intF = 0;
      } else {
         Runnable runnable = () -> {
            this.stringE = "RESTORE";
            this.intF = 0;
         };
         minecraft.execute(runnable);
      }
   }

   static {
      HashMap hashMap = new HashMap();
      mapB = hashMap;
      methodA(ClientboundPingPacket.class);
      methodA(ClientboundSetEntityMotionPacket.class);
      methodA(ClientboundExplodePacket.class);
      methodA(Pos.class);
      methodA(PosRot.class);
      methodA(Rot.class);
   }
}
