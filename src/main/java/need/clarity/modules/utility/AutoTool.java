package need.clarity.modules.utility;

import java.util.Optional;
import java.util.function.Function;
import need.clarity.modules.ModuleBase;
import need.clarity.util.InventoryAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;

public class AutoTool extends ModuleBase {
   private final Minecraft minecraft;
   private boolean flagB;
   private int intC;
   private long longD;
   private long longE;
   private boolean flagF;

   public AutoTool() {
      super("Auto Tool", "Utility");
      Minecraft minecraftx = Minecraft.getInstance();
      this.minecraft = minecraftx;
      this.flagB = false;
      this.intC = -1;
      this.longD = -1L;
      this.longE = -1L;
      this.flagF = false;
   }

   @Override
   public void onDisable() {
      this.flagB = false;
      this.intC = -1;
      this.longD = -1L;
      this.longE = -1L;
      this.flagF = false;
   }

   @Override
   public void onTick() {
      if (this.minecraft.player != null) {
         if (this.minecraft.level != null) {
            if (this.minecraft.gameMode != null) {
               boolean bl1;
               label64: {
                  if (this.minecraft.screen == null && this.minecraft.options.keyAttack.isDown() && this.minecraft.hitResult != null) {
                     Type type1 = this.minecraft.hitResult.getType();
                     Type type = Type.BLOCK;
                     if (type1 == type) {
                        bl1 = true;
                        break label64;
                     }
                  }

                  bl1 = false;
               }

               boolean bl = bl1;
               long l = System.currentTimeMillis();
               if (bl && !this.flagB) {
                  int i = InventoryAccess.getSelectedSlot(this.minecraft.player.getInventory());
                  this.intC = i;
                  this.longE = -1L;
                  this.flagF = false;
                  Integer integer = 0;
                  long q = this.getSetting("Swap Delay", integer).intValue();
                  long m = l + q;
                  this.longD = m;
               }

               if (bl) {
                  if (this.longD != -1L) {
                     long o = this.longD;
                     if (l >= o) {
                        this.longD = -1L;
                        this.methodA();
                     }
                  }
               } else if (this.flagB) {
                  label53: {
                     this.longD = -1L;
                     if (this.flagF) {
                        Boolean boolean1 = false;
                        if (this.getSetting("Swap Back", boolean1) && this.intC != -1) {
                           Integer integer1 = 0;
                           long r = this.getSetting("Swap Back Delay", integer1).intValue();
                           long n = l + r;
                           this.longE = n;
                           break label53;
                        }
                     }

                     this.intC = -1;
                  }

                  this.flagF = false;
               }

               if (!bl && this.longE != -1L) {
                  long p = this.longE;
                  if (l >= p) {
                     if (this.intC != -1) {
                        Inventory inventory = this.minecraft.player.getInventory();
                        int j = this.intC;
                        InventoryAccess.setSelectedSlot(inventory, j);
                     }

                     this.intC = -1;
                     this.longE = -1L;
                  }
               }

               this.flagB = bl;
            }
         }
      }
   }

   private void methodA() {
      if (this.minecraft.player != null) {
         if (this.minecraft.level != null) {
            if (this.minecraft.hitResult != null) {
               Type type1 = this.minecraft.hitResult.getType();
               Type type = Type.BLOCK;
               if (type1 == type) {
                  BlockPos blockPos = ((BlockHitResult)this.minecraft.hitResult).getBlockPos();
                  BlockState blockState = this.minecraft.level.getBlockState(blockPos);
                  Boolean boolean1 = false;
                  boolean bl = this.getSetting("Only Pickaxe", boolean1);
                  Inventory inventory = this.minecraft.player.getInventory();
                  int k = InventoryAccess.getSelectedSlot(inventory);
                  ItemStack itemStack1 = inventory.getItem(k);
                  float f = this.methodA(itemStack1, blockState);
                  int i = -1;

                  for (int j = 0; j < 9; j++) {
                     if (j != k) {
                        int m = j;
                        ItemStack itemStack = inventory.getItem(m);
                        if (!itemStack.isEmpty()) {
                           if (bl) {
                              TagKey tagKey = ItemTags.PICKAXES;
                              if (!itemStack.is(tagKey)) {
                                 continue;
                              }
                           }

                           float g = this.methodA(itemStack, blockState);
                           float h = f;
                           if (g > h) {
                              f = g;
                              i = j;
                           }
                        }
                     }
                  }

                  if (i != -1) {
                     int l = i;
                     InventoryAccess.setSelectedSlot(inventory, l);
                     this.flagF = true;
                  }
               }
            }
         }
      }
   }

   private float methodA(ItemStack itemStack, BlockState blockState) {
      if (itemStack.isEmpty()) {
         return 1.0F;
      }

      float f = itemStack.getDestroySpeed(blockState);
      if (f > 1.0F && this.minecraft.level != null) {
         try {
            RegistryAccess registryAccess = this.minecraft.level.registryAccess();
            ResourceKey resourceKey = Registries.ENCHANTMENT;
            Registry registry = registryAccess.lookupOrThrow(resourceKey);
            ResourceKey resourceKey1 = Enchantments.EFFICIENCY;
            Optional optional = registry.get(resourceKey1);
            Function function = reference -> EnchantmentHelper.getItemEnchantmentLevel(reference, itemStack);
            Optional optional1 = optional.map(function);
            Integer integer = 0;
            int i = optional1.orElse(integer);
            if (i > 0) {
               float g = f;
               float h = i * i + 1;
               f = g + h;
            }
         } catch (Exception exception) {
         }
      }

      return f;
   }
}
