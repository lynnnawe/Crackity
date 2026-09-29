package need.clarity.modules.utility;

enum ChestStealer$Stage {
   IDLE,
   COLLECTING,
   WAIT_CLOSE;

   // $VF: synthetic method
   private static ChestStealer$Stage[] methodA() {
      ChestStealer$Stage[] stages = new ChestStealer$Stage[3];
      ChestStealer$Stage stage = IDLE;
      stages[0] = stage;
      ChestStealer$Stage stage1 = COLLECTING;
      stages[1] = stage1;
      ChestStealer$Stage stage2 = WAIT_CLOSE;
      stages[2] = stage2;
      return stages;
   }
}
