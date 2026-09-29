package need.clarity.modules.combat;

enum AutoCrystal$Stage {
   IDLE,
   SWITCH_CRYSTAL,
   NORMAL;

   // $VF: synthetic method
   private static AutoCrystal$Stage[] methodA() {
      AutoCrystal$Stage[] stages = new AutoCrystal$Stage[3];
      AutoCrystal$Stage stage = IDLE;
      stages[0] = stage;
      AutoCrystal$Stage stage1 = SWITCH_CRYSTAL;
      stages[1] = stage1;
      AutoCrystal$Stage stage2 = NORMAL;
      stages[2] = stage2;
      return stages;
   }
}
