package need.clarity.modules.combat;

enum AutoHitCrystal$Stage {
   NONE,
   SWITCH_OBSIDIAN,
   PLACE_OBSIDIAN,
   SWITCH_CRYSTAL,
   DONE;

   // $VF: synthetic method
   private static AutoHitCrystal$Stage[] methodA() {
      AutoHitCrystal$Stage[] stages = new AutoHitCrystal$Stage[5];
      AutoHitCrystal$Stage stage = NONE;
      stages[0] = stage;
      AutoHitCrystal$Stage stage1 = SWITCH_OBSIDIAN;
      stages[1] = stage1;
      AutoHitCrystal$Stage stage2 = PLACE_OBSIDIAN;
      stages[2] = stage2;
      AutoHitCrystal$Stage stage3 = SWITCH_CRYSTAL;
      stages[3] = stage3;
      AutoHitCrystal$Stage stage4 = DONE;
      stages[4] = stage4;
      return stages;
   }
}
