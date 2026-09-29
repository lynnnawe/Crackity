package need.clarity.modules.combat;

import java.util.Optional;
import java.util.Random;
import need.clarity.PerFrameEvents;
import need.clarity.mixin.KeyMappingAccessor;
import need.clarity.modules.ModuleBase;
import need.clarity.util.InventoryAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.lwjgl.glfw.GLFW;

public class AutoLunge extends ModuleBase {
   private static final ResourceKey<Enchantment> resourceKeyA;
   private AutoLunge$Phase phaseB;
   private long longC;
   private long longD;
   private int intE;
   private boolean flagF;
   private boolean flagG;
   private final Random random;

   public AutoLunge() {
      super("Auto Lunge", "combat");
      AutoLunge$Phase phase = AutoLunge$Phase.IDLE;
      this.phaseB = phase;
      this.longC = 0L;
      this.longD = 0L;
      this.intE = -1;
      this.flagF = false;
      this.flagG = false;
      Random randomx = new Random();
      this.random = randomx;
      PerFrameEvents.register(this::methodA);
   }

   @Override
   public void onEnable() {
      AutoLunge$Phase phase = AutoLunge$Phase.IDLE;
      this.phaseB = phase;
      this.longC = 0L;
      this.intE = -1;
      this.flagF = false;
      this.flagG = false;
   }

   @Override
   public void onDisable() {
      Minecraft minecraft = Minecraft.getInstance();
      if (minecraft.player != null && this.intE != -1) {
         Inventory inventory = minecraft.player.getInventory();
         int i = this.intE;
         InventoryAccess.setSelectedSlot(inventory, i);
      }

      AutoLunge$Phase phase = AutoLunge$Phase.IDLE;
      this.phaseB = phase;
      this.intE = -1;
      this.flagF = false;
      this.flagG = false;
   }

   private void methodA() {
      Minecraft minecraft = Minecraft.getInstance();
      if (this.isEnabled()) {
         if (minecraft.player != null) {
            if (minecraft.level != null) {
               if (minecraft.screen == null) {
                  LocalPlayer localPlayer = minecraft.player;
                  long l = System.currentTimeMillis();
                  AutoLunge$Phase phase1 = this.phaseB;
                  AutoLunge$Phase phase = AutoLunge$Phase.SWAP_BACK_WAITING;
                  if (phase1 == phase) {
                     long p = this.longC;
                     long m = l - p;
                     long q = this.longD;
                     if (m >= q) {
                        if (this.intE != -1) {
                           Inventory inventory = localPlayer.getInventory();
                           int i = this.intE;
                           InventoryAccess.setSelectedSlot(inventory, i);
                           this.intE = -1;
                        }

                        AutoLunge$Phase phase4 = AutoLunge$Phase.IDLE;
                        this.phaseB = phase4;
                        this.flagG = true;
                        this.longC = l;
                     }
                  } else {
                     AutoLunge$Phase phase2 = this.phaseB;
                     AutoLunge$Phase phase5 = AutoLunge$Phase.HOLD_LOOP_WAITING;
                     if (phase2 == phase5) {
                        String string = this.getSetting("Lunge Bind", "NONE");
                        if (!methodA(minecraft, string)) {
                           AutoLunge$Phase phase7 = AutoLunge$Phase.IDLE;
                           this.phaseB = phase7;
                           this.flagG = false;
                        } else {
                           long r = this.longC;
                           long n = l - r;
                           long s = this.longD;
                           if (n >= s) {
                              float f = localPlayer.getAttackStrengthScale(0.0F);
                              float g = this.getCooldownSetting();
                              if (f >= g) {
                                 this.methodA(minecraft, localPlayer);
                              } else {
                                 AutoLunge$Phase phase6 = AutoLunge$Phase.IDLE;
                                 this.phaseB = phase6;
                                 this.flagG = false;
                              }
                           }
                        }
                     } else {
                        AutoLunge$Phase phase3 = this.phaseB;
                        AutoLunge$Phase phase8 = AutoLunge$Phase.SWAP_WAITING;
                        if (phase3 == phase8) {
                           long t = this.longC;
                           long o = l - t;
                           long u = this.longD;
                           if (o >= u) {
                              this.methodA(minecraft, localPlayer);
                           }
                        } else {
                           String string1 = this.getSetting("Lunge Bind", "NONE");
                           boolean bl1 = methodA(minecraft, string1);
                           Boolean boolean1 = false;
                           if (this.getSetting("Bind Hold Loop", boolean1)) {
                              if (!bl1) {
                                 this.flagF = false;
                                 this.flagG = false;
                              } else {
                                 this.flagG = true;
                                 this.methodA(minecraft, localPlayer, l);
                              }
                           } else {
                              boolean bl2;
                              if (bl1 && !this.flagF) {
                                 bl2 = true;
                              } else {
                                 bl2 = false;
                              }

                              boolean bl = bl2;
                              this.flagF = bl1;
                              if (bl) {
                                 this.methodA(minecraft, localPlayer, l);
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

   private float getCooldownSetting() {
      Integer integer = 85;
      return Math.clamp(this.getSetting("Cooldown %", integer).intValue(), 0, 100) / 100.0F;
   }

   private void methodA(Minecraft minecraft, LocalPlayer localPlayer, long l) {
      if (this.methodA(localPlayer) >= 0) {
         Boolean boolean_ = false;
         if (this.getSetting("Bind Hold Loop", boolean_) && this.flagG) {
            float f = localPlayer.getAttackStrengthScale(0.0F);
            float h = this.getCooldownSetting();
            if (f >= h) {
               AutoLunge$Phase phase = this.phaseB;
               AutoLunge$Phase phase1 = AutoLunge$Phase.IDLE;
               if (phase == phase1) {
                  int i = InventoryAccess.getSelectedSlot(localPlayer.getInventory());
                  this.intE = i;
                  AutoLunge$Phase phase2 = AutoLunge$Phase.HOLD_LOOP_WAITING;
                  this.phaseB = phase2;
                  this.longC = l;
                  Integer integer1 = 0;
                  int p = this.getSetting("Hold Loop Delay_lo", integer1);
                  Integer integer3 = 100;
                  int r = this.getSetting("Hold Loop Delay_hi", integer3);
                  long n = this.methodA(p, r);
                  this.longD = n;
               }
            }
         } else {
            float g = localPlayer.getAttackStrengthScale(0.0F);
            float j = this.getCooldownSetting();
            if (!(g < j)) {
               int k = InventoryAccess.getSelectedSlot(localPlayer.getInventory());
               this.intE = k;
               Integer integer = 0;
               int o = this.getSetting("Swap Delay_lo", integer);
               Integer integer2 = 100;
               int q = this.getSetting("Swap Delay_hi", integer2);
               long m = this.methodA(o, q);
               if (m == 0L) {
                  this.methodA(minecraft, localPlayer);
               } else {
                  AutoLunge$Phase phase3 = AutoLunge$Phase.SWAP_WAITING;
                  this.phaseB = phase3;
                  this.longC = l;
                  this.longD = m;
               }
            }
         }
      }
   }

   private void methodA(Minecraft minecraft, LocalPlayer localPlayer) {
      Inventory inventory = localPlayer.getInventory();
      Boolean boolean1 = false;
      boolean bl = this.getSetting("Swap Back", boolean1);
      int i = this.methodA(localPlayer);
      if (i < 0) {
         if (this.intE != -1) {
            int j = this.intE;
            InventoryAccess.setSelectedSlot(inventory, j);
            this.intE = -1;
         }

         AutoLunge$Phase phase = AutoLunge$Phase.IDLE;
         this.phaseB = phase;
         this.flagG = false;
      } else {
         InventoryAccess.setSelectedSlot(inventory, i);
         KeyMappingAccessor keyMappingAccessor = (KeyMappingAccessor)minecraft.options.keyAttack;
         int k = ((KeyMappingAccessor)minecraft.options.keyAttack).getClickCount() + 1;
         keyMappingAccessor.setClickCount(k);
         if (bl && this.intE != -1 && this.intE != i) {
            AutoLunge$Phase phase1 = AutoLunge$Phase.SWAP_BACK_WAITING;
            this.phaseB = phase1;
            long l = System.currentTimeMillis();
            this.longC = l;
            Integer integer = 0;
            int o = this.getSetting("Swap Back Delay_lo", integer);
            Integer integer1 = 100;
            int p = this.getSetting("Swap Back Delay_hi", integer1);
            long m = this.methodA(o, p);
            this.longD = m;
         } else {
            this.intE = -1;
            AutoLunge$Phase phase2 = AutoLunge$Phase.IDLE;
            this.phaseB = phase2;
            this.flagG = true;
            long n = System.currentTimeMillis();
            this.longC = n;
         }
      }
   }

   private int methodA(LocalPlayer localPlayer) {
      Inventory inventory = localPlayer.getInventory();
      int i = -1;
      int j = 0;

      for (int k = 0; k < 9; k++) {
         int m = k;
         ItemStack itemStack = inventory.getItem(m);
         if (!itemStack.isEmpty()) {
            TagKey tagKey = ItemTags.SPEARS;
            if (itemStack.is(tagKey)) {
               int l = this.methodA(itemStack, localPlayer);
               int n = j;
               if (l > n) {
                  j = l;
                  i = k;
               }
            }
         }
      }

      return i;
   }

   private int methodA(ItemStack itemStack, LocalPlayer localPlayer) {
      try {
         RegistryAccess registryAccess = localPlayer.level().registryAccess();
         ResourceKey resourceKey = Registries.ENCHANTMENT;
         Registry registry = registryAccess.lookupOrThrow(resourceKey);
         ResourceKey resourceKey1 = resourceKeyA;
         Optional optional = registry.get(resourceKey1);
         if (optional.isEmpty()) {
            return 0;
         }

         Holder holder = (Holder)optional.get();
         DataComponentType dataComponentType = DataComponents.ENCHANTMENTS;
         ItemEnchantments itemEnchantments = (ItemEnchantments)itemStack.get(dataComponentType);
         return itemEnchantments == null ? 0 : itemEnchantments.getLevel(holder);
      } catch (Exception exception) {
         return 0;
      }
   }

   private static boolean methodA(Minecraft minecraft, String string) {
      if (string != null && !string.isEmpty() && !"NONE".equalsIgnoreCase(string) && !"None".equalsIgnoreCase(string)) {
         long l = minecraft.getWindow().handle();
         int i = methodA(string);
         if (i == -1) {
            return false;
         }

         boolean bl;
         if (GLFW.glfwGetKey(l, i) == 1) {
            bl = true;
         } else {
            bl = false;
         }

         return bl;
      } else {
         return false;
      }
   }

   private static int methodA(String string) {
      byte b = -1;
      int k = string.hashCode();
      switch (k) {
         case -2099925287:
            if (string.equals("Insert")) {
               b = 15;
            }
            break;
         case -1911885398:
            if (string.equals("PageUp")) {
               b = 18;
            }
            break;
         case -1678770883:
            if (string.equals("Control")) {
               b = 1;
            }
            break;
         case -937491361:
            if (string.equals("Backspace")) {
               b = 13;
            }
            break;
         case 32:
            if (string.equals(" ")) {
               b = 9;
            }
            break;
         case 2219:
            if (string.equals("F1")) {
               b = 21;
            }
            break;
         case 2220:
            if (string.equals("F2")) {
               b = 22;
            }
            break;
         case 2221:
            if (string.equals("F3")) {
               b = 23;
            }
            break;
         case 2222:
            if (string.equals("F4")) {
               b = 24;
            }
            break;
         case 2223:
            if (string.equals("F5")) {
               b = 25;
            }
            break;
         case 2224:
            if (string.equals("F6")) {
               b = 26;
            }
            break;
         case 2225:
            if (string.equals("F7")) {
               b = 27;
            }
            break;
         case 2226:
            if (string.equals("F8")) {
               b = 28;
            }
            break;
         case 2227:
            if (string.equals("F9")) {
               b = 29;
            }
            break;
         case 65929:
            if (string.equals("Alt")) {
               b = 2;
            }
            break;
         case 68837:
            if (string.equals("F10")) {
               b = 30;
            }
            break;
         case 68838:
            if (string.equals("F11")) {
               b = 31;
            }
            break;
         case 68839:
            if (string.equals("F12")) {
               b = 32;
            }
            break;
         case 69819:
            if (string.equals("End")) {
               b = 17;
            }
            break;
         case 83829:
            if (string.equals("Tab")) {
               b = 12;
            }
            break;
         case 2255103:
            if (string.equals("Home")) {
               b = 16;
            }
            break;
         case 2394661:
            if (string.equals("Meta")) {
               b = 3;
            }
            break;
         case 12763084:
            if (string.equals("CapsLock")) {
               b = 20;
            }
            break;
         case 67114680:
            if (string.equals("Enter")) {
               b = 11;
            }
            break;
         case 79854690:
            if (string.equals("Shift")) {
               b = 0;
            }
            break;
         case 80085222:
            if (string.equals("Space")) {
               b = 10;
            }
            break;
         case 251549619:
            if (string.equals("ArrowRight")) {
               b = 7;
            }
            break;
         case 923631601:
            if (string.equals("PageDown")) {
               b = 19;
            }
            break;
         case 930625636:
            if (string.equals("ArrowUp")) {
               b = 4;
            }
            break;
         case 977535019:
            if (string.equals("ArrowDown")) {
               b = 5;
            }
            break;
         case 977763216:
            if (string.equals("ArrowLeft")) {
               b = 6;
            }
            break;
         case 2043376075:
            if (string.equals("Delete")) {
               b = 14;
            }
            break;
         case 2084662433:
            if (string.equals("Escape")) {
               b = 8;
            }
      }

      byte d = b;

      return switch (d) {
         case 0 -> 340;
         case 1 -> 341;
         case 2 -> 342;
         case 3 -> 343;
         case 4 -> 265;
         case 5 -> 264;
         case 6 -> 263;
         case 7 -> 262;
         case 8 -> 256;
         case 9 -> 32;
         case 10 -> 32;
         case 11 -> 257;
         case 12 -> 258;
         case 13 -> 259;
         case 14 -> 261;
         case 15 -> 260;
         case 16 -> 268;
         case 17 -> 269;
         case 18 -> 266;
         case 19 -> 267;
         case 20 -> 280;
         case 21 -> 290;
         case 22 -> 291;
         case 23 -> 292;
         case 24 -> 293;
         case 25 -> 294;
         case 26 -> 295;
         case 27 -> 296;
         case 28 -> 297;
         case 29 -> 298;
         case 30 -> 299;
         case 31 -> 300;
         case 32 -> 301;
         default -> {
            label159: {
               if (string.length() == 1) {
                  char c = string.charAt(0);
                  if (c >= 'A' && c <= 'Z') {
                     int i = c - 'A';
                     yield 65 + i;
                     break label159;
                  }

                  if (c >= '0' && c <= '9') {
                     if (c == '0') {
                        yield 48;
                     } else {
                        int j = c - '1';
                        yield 49 + j;
                     }
                     break label159;
                  }
               }

               yield -1;
            }
         }
      };
   }

   private int methodA(int i, int j) {
      if (j <= i) {
         return i;
      }

      Random randomx = this.random;
      int l = j - i + 1;
      int k = randomx.nextInt(l);
      return i + k;
   }

   static {
      ResourceKey resourceKey = Registries.ENCHANTMENT;
      Identifier identifier = Identifier.withDefaultNamespace("lunge");
      resourceKeyA = ResourceKey.create(resourceKey, identifier);
   }
}
