package need.clarity.modules.visual;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import need.clarity.PerPacketEvents;
import need.clarity.WorldRenderEvent;
import need.clarity.WorldRenderEvent$Listener;
import need.clarity.config.ClarityConfig;
import need.clarity.config.ThemeColors;
import need.clarity.event.PacketReceiveEvent;
import need.clarity.modules.ModuleBase;
import need.clarity.render.BoxRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Search extends ModuleBase {
   private static Search instance;
   private final Map<ChunkPos, Search$ChunkSearcher> mapB;
   private final Set<ChunkPos> setC;
   private static final ExecutorService executorServiceD;
   private volatile Set<Block> setE;
   private volatile boolean flagF;
   private final Consumer<PacketReceiveEvent> consumerG;
   private final WorldRenderEvent$Listener listenerH;

   public Search() {
      super("Search", "Visual");
      HashMap hashMap = new HashMap();
      this.mapB = hashMap;
      HashSet hashSet = new HashSet();
      Set set = Collections.synchronizedSet(hashSet);
      this.setC = set;
      Set set1 = Collections.emptySet();
      this.setE = set1;
      this.flagF = false;
      Consumer consumer = this::methodA;
      this.consumerG = consumer;
      WorldRenderEvent$Listener listener = this::methodA;
      this.listenerH = listener;
      instance = this;
   }

   @Override
   public void onEnable() {
      PerPacketEvents.register(this.consumerG);
      WorldRenderEvent.register(this.listenerH);
      Set set = this.methodC();
      this.setE = set;
      this.methodB();
   }

   @Override
   public void onDisable() {
      PerPacketEvents.unregister(this.consumerG);
      WorldRenderEvent.methodA(this.listenerH);
      this.methodA();
      this.setC.clear();
      Set set = Collections.emptySet();
      this.setE = set;
   }

   @Override
   public void onTick() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.level != null) {
         if (minecraft.player != null) {
            boolean bl;
            label85: {
               Boolean boolean1 = false;
               bl = this.getSetting("Only Visible Blocks", boolean1);
               Set set = this.methodC();
               Set set3 = this.setE;
               if (set.equals(set3)) {
                  boolean bl1 = this.flagF;
                  if (bl == bl1) {
                     break label85;
                  }
               }

               this.setE = set;
               this.flagF = bl;
               this.methodA();
            }

            if (!this.setE.isEmpty()) {
               Set set1 = this.setC;
               HashSet hashSet;

               synchronized (set1) {
                  Set set4 = this.setC;
                  hashSet = new HashSet(set4);
                  this.setC.clear();

               }

               for (ChunkPos chunkPos2 : hashSet) {
                  Search$ChunkSearcher chunkSearcher1 = this.mapB.remove(chunkPos2);
                  if (chunkSearcher1 != null) {
                     chunkSearcher1.methodA();
                  }
               }

               int l = (Integer)minecraft.options.renderDistance().get();
               int m = l + 2;
               ChunkPos chunkPos = minecraft.player.chunkPosition();
               Set set2 = this.mapB.entrySet();
               Predicate predicate = entry -> {
                  ChunkPos chunkPos4;
                  boolean bl5;
                  label23: {
                     chunkPos4 = (ChunkPos)entry.getKey();
                     int bo = chunkPos4.x();
                     int bq = chunkPos.x();
                     if (Math.abs(bo - bq) <= m) {
                        int bp = chunkPos4.z();
                        int br = chunkPos.z();
                        if (Math.abs(bp - br) <= m) {
                           bl5 = false;
                           break label23;
                        }
                     }

                     bl5 = true;
                  }

                  boolean bl2 = bl5;
                  ClientLevel clientLevel10 = minecraft.level;
                  int bs = chunkPos4.x();
                  int bt = chunkPos4.z();
                  boolean bl4;
                  if (!clientLevel10.hasChunk(bs, bt)) {
                     bl4 = true;
                  } else {
                     bl4 = false;
                  }

                  boolean bl3 = bl4;
                  if (!bl2 && !bl3) {
                     return false;
                  }

                  ((Search$ChunkSearcher)entry.getValue()).methodA();
                  return true;
               };
               set2.removeIf(predicate);

               for (int j = -l; j <= l; j++) {
                  for (int k = -l; k <= l; k++) {
                     int x = chunkPos.x();
                     int bj = j;
                     int y = x + bj;
                     int bk = chunkPos.z();
                     int i = k;
                     int bm = bk + i;
                     ChunkPos chunkPos1 = new ChunkPos(y, bm);
                     if (!this.mapB.containsKey(chunkPos1)) {
                        ClientLevel clientLevel = minecraft.level;
                        int n = chunkPos1.x();
                        int z = chunkPos1.z();
                        if (clientLevel.hasChunk(n, z)) {
                           ClientLevel clientLevel1 = minecraft.level;
                           int o = chunkPos1.x();
                           int ba = chunkPos1.z();
                           LevelChunk levelChunk4 = clientLevel1.getChunk(o, ba);
                           ClientLevel clientLevel2 = minecraft.level;
                           int p = chunkPos1.x();
                           int bb = chunkPos1.z() - 1;
                           LevelChunk levelChunk8;
                           if (clientLevel2.hasChunk(p, bb)) {
                              ClientLevel clientLevel9 = minecraft.level;
                              int w = chunkPos1.x();
                              int bi = chunkPos1.z() - 1;
                              levelChunk8 = clientLevel9.getChunk(w, bi);
                           } else {
                              levelChunk8 = null;
                           }

                           LevelChunk levelChunk = levelChunk8;
                           ClientLevel clientLevel3 = minecraft.level;
                           int q = chunkPos1.x();
                           int bc = chunkPos1.z() + 1;
                           LevelChunk levelChunk5;
                           if (clientLevel3.hasChunk(q, bc)) {
                              ClientLevel clientLevel8 = minecraft.level;
                              int v = chunkPos1.x();
                              int bh = chunkPos1.z() + 1;
                              levelChunk5 = clientLevel8.getChunk(v, bh);
                           } else {
                              levelChunk5 = null;
                           }

                           LevelChunk levelChunk1 = levelChunk5;
                           ClientLevel clientLevel4 = minecraft.level;
                           int r = chunkPos1.x() - 1;
                           int bd = chunkPos1.z();
                           LevelChunk levelChunk7;
                           if (clientLevel4.hasChunk(r, bd)) {
                              ClientLevel clientLevel7 = minecraft.level;
                              int u = chunkPos1.x() - 1;
                              int bg = chunkPos1.z();
                              levelChunk7 = clientLevel7.getChunk(u, bg);
                           } else {
                              levelChunk7 = null;
                           }

                           LevelChunk levelChunk2 = levelChunk7;
                           ClientLevel clientLevel5 = minecraft.level;
                           int s = chunkPos1.x() + 1;
                           int be = chunkPos1.z();
                           LevelChunk levelChunk6;
                           if (clientLevel5.hasChunk(s, be)) {
                              ClientLevel clientLevel6 = minecraft.level;
                              int t = chunkPos1.x() + 1;
                              int bf = chunkPos1.z();
                              levelChunk6 = clientLevel6.getChunk(t, bf);
                           } else {
                              levelChunk6 = null;
                           }

                           LevelChunk levelChunk3 = levelChunk6;
                           Set set5 = this.setE;
                           Search$ChunkSearcher chunkSearcher = new Search$ChunkSearcher(
                              levelChunk4, levelChunk, levelChunk1, levelChunk2, levelChunk3, set5, bl
                           );
                           this.mapB.put(chunkPos1, chunkSearcher);
                           ExecutorService executorService = executorServiceD;
                           chunkSearcher.methodA(executorService);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void methodA(PacketReceiveEvent packetReceiveEvent) {
      ChunkPos chunkPos = methodA(packetReceiveEvent.getPacket());
      if (chunkPos != null) {
         this.setC.add(chunkPos);
      }
   }

   private static ChunkPos methodA(Packet<?> packet) {
      if (packet instanceof ClientboundLevelChunkWithLightPacket clientboundLevelChunkWithLightPacket) {
         int i = clientboundLevelChunkWithLightPacket.getX();
         int j = clientboundLevelChunkWithLightPacket.getZ();
         return new ChunkPos(i, j);
      } else if (packet instanceof ClientboundBlockUpdatePacket) {
         return ChunkPos.containing(((ClientboundBlockUpdatePacket)packet).getPos());
      } else if (packet instanceof ClientboundSectionBlocksUpdatePacket clientboundSectionBlocksUpdatePacket) {
         ChunkPos[] chunkPos = new ChunkPos[]{null};
         BiConsumer biConsumer = (blockPos, blockState) -> {
            if (chunkPos[0] == null) {
               ChunkPos chunkPos2 = ChunkPos.containing(blockPos);
               chunkPos[0] = chunkPos2;
            }
         };
         clientboundSectionBlocksUpdatePacket.runUpdates(biConsumer);
         return chunkPos[0];
      } else {
         return null;
      }
   }

   private void methodA(PoseStack poseStack, float f) {
      if (!this.mapB.isEmpty()) {
         Boolean boolean1 = true;
         boolean bl = this.getSetting("Show Fill", boolean1);
         Boolean boolean2 = true;
         boolean bl1 = this.getSetting("Show Outline", boolean2);
         Integer integer2 = 80;
         int i = this.getSetting("Fill Opacity", integer2);
         Integer integer3 = 200;
         int j = this.getSetting("Outline Opacity", integer3);
         Boolean boolean3 = false;
         boolean bl2 = this.getSetting("Show Tracers", boolean3);
         Float float1 = 2.0F;
         float g = this.getSetting("Tracer Thickness", float1);
         JsonObject jsonObject = ClarityConfig.getBlockColors("Search");
         HashMap hashMap1 = new HashMap();
         HashMap hashMap2;
         if (bl2) {
            hashMap2 = new HashMap();
         } else {
            hashMap2 = null;
         }

         HashMap hashMap = hashMap2;
         Iterator iterator = this.mapB.values().iterator();

         while (iterator.hasNext()) {
            for (Search$ChunkSearcher$Hit hit : ((Search$ChunkSearcher)iterator.next()).getListH()) {
               int m = methodA(hit.getBlockB(), jsonObject);
               int o = m >> 16 & 0xFF;
               int q = m >> 8 & 0xFF;
               int t = m & 0xFF;
               Integer integer = methodA(i, o, q, t);
               Function function = integer4 -> new java.util.ArrayList();
               List list = (List)hashMap1.computeIfAbsent(integer, function);
               AABB aABB1 = hit.getAABBA();
               list.add(aABB1);
               if (bl2 && hashMap != null) {
                  int p = m >> 16 & 0xFF;
                  int r = m >> 8 & 0xFF;
                  int u = m & 0xFF;
                  int n = methodA(j, p, r, u);
                  AABB aABB = hit.getAABBA();
                  Integer integer1 = n;
                  Function function1 = integer4 -> new java.util.ArrayList();
                  List list1 = (List)hashMap.computeIfAbsent(integer1, function1);
                  double d = aABB.minX + 0.5;
                  double e = aABB.minY + 0.5;
                  double h = aABB.minZ + 0.5;
                  Vec3 vec3 = new Vec3(d, e, h);
                  list1.add(vec3);
               }
            }
         }

         for (Entry entry : hashMap1.entrySet()) {
            int k = (Integer)entry.getKey();
            int l = methodA(k, j);
            List list2 = (List)entry.getValue();
            BoxRenderer.methodA(poseStack, list2, k, l, bl, bl1);
         }

         if (bl2) {
            if (hashMap != null) {
               for (Entry entry1 : hashMap.entrySet()) {
                  List list3 = (List)entry1.getValue();
                  int s = (Integer)entry1.getKey();
                  BoxRenderer.methodA(poseStack, f, list3, s, g);
               }
            }
         }
      }
   }

   private void methodA() {
      Collection collection = this.mapB.values();
      Consumer consumer = Search$ChunkSearcher::methodA;
      collection.forEach(consumer);
      this.mapB.clear();
   }

   private void methodB() {
      this.methodA();
   }

   private Set<Block> methodC() {
      try {
         JsonObject jsonObject = ClarityConfig.getCurrent().getAsJsonObject("Search");
         if (jsonObject == null) {
            return Collections.emptySet();
         }

         JsonObject jsonObject1 = jsonObject.getAsJsonObject("values");
         if (jsonObject1 == null) {
            return Collections.emptySet();
         }

         JsonElement jsonElement = jsonObject1.get("Blocks");
         if (jsonElement != null && jsonElement.isJsonArray()) {
            HashSet hashSet = new HashSet();

            for (JsonElement jsonElement1 : jsonElement.getAsJsonArray()) {
               if (jsonElement1.isJsonPrimitive()) {
                  try {
                     String string = jsonElement1.getAsString();
                     if (!string.contains(":")) {
                        string = "minecraft:" + string;
                     }

                     DefaultedRegistry defaultedRegistry = BuiltInRegistries.BLOCK;
                     Identifier identifier = Identifier.parse(string);
                     Block block = (Block)defaultedRegistry.getValue(identifier);
                     Block block1 = Blocks.AIR;
                     if (block != block1) {
                        hashSet.add(block);
                     }
                  } catch (Exception exception) {
                  }
               }
            }

            return hashSet;
         } else {
            return Collections.emptySet();
         }
      } catch (Exception exception1) {
         return Collections.emptySet();
      }
   }

   private static int methodA(Block block, JsonObject jsonObject) {
      String string2 = BuiltInRegistries.BLOCK.getKey(block).toString();
      String string3;
      if (string2.contains(":")) {
         string3 = string2.split(":", 2)[1];
      } else {
         string3 = string2;
      }

      String string = string3;
      String string1 = null;
      if (jsonObject.has(string)) {
         string1 = jsonObject.get(string).getAsString();
      } else if (jsonObject.has(string2)) {
         string1 = jsonObject.get(string2).getAsString();
      }

      if (string1 == null) {
         int i;
         if (ThemeColors.isThemeEnabled()) {
            int j = ThemeColors.getAccentColor();
            i = 0xFF000000 | j;
         } else {
            i = 16758062;
         }

         return i;
      } else {
         return ThemeColors.resolveArgb(string1, 16758062, false);
      }
   }

   private static int methodA(int i, int j, int k, int l) {
      int m = (i & 0xFF) << 24;
      int p = (j & 0xFF) << 16;
      int n = m | p;
      int q = (k & 0xFF) << 8;
      int o = n | q;
      int r = l & 0xFF;
      return o | r;
   }

   private static int methodA(int i, int j) {
      int k = i & 16777215;
      int l = (j & 0xFF) << 24;
      return k | l;
   }

   static {
      int j = Runtime.getRuntime().availableProcessors() - 1;
      int i = Math.max(1, j);
      ThreadFactory threadFactory = runnable -> {
         Thread thread = new Thread(runnable, "clarity-search-chunk");
         thread.setDaemon(true);
         thread.setPriority(1);
         return thread;
      };
      executorServiceD = Executors.newFixedThreadPool(i, threadFactory);
   }
}
