package need.clarity.modules.visual;

import need.clarity.modules.ModuleBase;

public class NoRender extends ModuleBase {
   public static volatile NoRender instance;

   public NoRender() {
      super("NoRender", "Visual");
      instance = this;
   }

   public boolean isFireOverlaySetting() {
      if (!this.isEnabled()) {
         return false;
      }

      Boolean boolean_ = true;
      return this.getSetting("Fire Overlay", boolean_);
   }

   public boolean isNauseaOverlaySetting() {
      if (!this.isEnabled()) {
         return false;
      }

      Boolean boolean_ = true;
      return this.getSetting("Nausea Overlay", boolean_);
   }

   public boolean isPortalOverlaySetting() {
      if (!this.isEnabled()) {
         return false;
      }

      Boolean boolean_ = true;
      return this.getSetting("Portal Overlay", boolean_);
   }

   public boolean isSpyglassOverlaySetting() {
      if (!this.isEnabled()) {
         return false;
      }

      Boolean boolean_ = true;
      return this.getSetting("Spyglass Overlay", boolean_);
   }

   public boolean isScoreboardSetting() {
      if (!this.isEnabled()) {
         return false;
      }

      Boolean boolean_ = false;
      return this.getSetting("Scoreboard", boolean_);
   }

   public boolean isVignetteSetting() {
      if (!this.isEnabled()) {
         return false;
      }

      Boolean boolean_ = false;
      return this.getSetting("Vignette", boolean_);
   }

   public boolean isTotemAnimationSetting() {
      if (!this.isEnabled()) {
         return false;
      }

      Boolean boolean_ = false;
      return this.getSetting("Totem Animation", boolean_);
   }

   public boolean isExplosionParticlesSetting() {
      if (!this.isEnabled()) {
         return false;
      }

      Boolean boolean_ = false;
      return this.getSetting("Explosion Particles", boolean_);
   }
}
