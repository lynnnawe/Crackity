package need.clarity.modules;

import need.clarity.config.ClarityConfig;

public abstract class ModuleBase {
   private final String name;
   private final String category;
   private boolean enabled;

   protected ModuleBase(String string, String string1) {
      this.name = string;
      this.category = string1;
      boolean bl = ClarityConfig.isEnabled(string);
      this.enabled = bl;
      ModuleRegistry.register(this);
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void onTick() {
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean bl) {
      if (this.enabled != bl) {
         this.enabled = bl;
         ClarityConfig.setModuleEnabled(this.name, bl);
         if (bl) {
            this.onEnable();
         } else {
            this.onDisable();
         }
      }
   }

   public void toggle() {
      boolean bl;
      if (!this.enabled) {
         bl = true;
      } else {
         bl = false;
      }

      this.setEnabled(bl);
   }

   public String getName() {
      return this.name;
   }

   public String getCategory() {
      return this.category;
   }

   protected <T> T getSetting(String string, T object) {
      Object object1 = ClarityConfig.getSetting(this.name, string);
      if (object1 == null) {
         return (T)object;
      }

      try {
         if (object instanceof Integer && object1 instanceof Double) {
            return (T)((Double)object1).intValue();
         } else if (object instanceof Long && object1 instanceof Double) {
            return (T)((Double)object1).longValue();
         } else {
            return (T)(object instanceof Float && object1 instanceof Double ? ((Double)object1).floatValue() : object1);
         }
      } catch (ClassCastException classCastException) {
         return (T)object;
      }
   }
}
