package need.clarity.modules.combat;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import need.clarity.EventStartAttack;
import need.clarity.PerAttackEvents;
import need.clarity.PerMovementEvents;
import need.clarity.modules.ModuleBase;
import need.clarity.modules.combat.combohelpers.ComboHelper;
import need.clarity.modules.combat.combohelpers.ComboMode;
import need.clarity.modules.combat.combohelpers.STap;
import need.clarity.modules.combat.combohelpers.WTap;
import need.clarity.modules.movement.MoveFix$EventMovementInput;

public class SprintReset extends ModuleBase {
   private final Map<String, ComboMode> mapA;

   public SprintReset() {
      super("Sprint Reset", "Combat");
      LinkedHashMap linkedHashMap = new LinkedHashMap();
      this.mapA = linkedHashMap;
      ComboHelper.sprintResetA = this;
      Map map = this.mapA;
      WTap wTap = new WTap();
      map.put("W-Tap", wTap);
      Map map1 = this.mapA;
      STap sTap = new STap();
      map1.put("S-Tap", sTap);
      PerAttackEvents.register(this::methodA);
      PerMovementEvents.register(this::methodA);
   }

   @Override
   public void onEnable() {
      Collection collection = this.mapA.values();
      Consumer consumer = ComboMode::onEnable;
      collection.forEach(consumer);
   }

   @Override
   public void onDisable() {
      Collection collection = this.mapA.values();
      Consumer consumer = ComboMode::onDisable;
      collection.forEach(consumer);
   }

   private void methodA(EventStartAttack eventStartAttack) {
      if (this.isEnabled()) {
         ComboMode comboMode = this.getModeSetting();
         if (comboMode != null) {
            comboMode.methodA(eventStartAttack);
         }
      }
   }

   private void methodA(MoveFix$EventMovementInput eventMovementInput) {
      if (this.isEnabled()) {
         ComboMode comboMode = this.getModeSetting();
         if (comboMode != null) {
            comboMode.methodA(eventMovementInput);
         }
      }
   }

   private ComboMode getModeSetting() {
      String string = this.getSetting("Mode", "W-Tap");
      return this.mapA.get(string);
   }

   @Override
   public <T> T getSetting(String string, T object) {
      return super.getSetting(string, (T)object);
   }
}
