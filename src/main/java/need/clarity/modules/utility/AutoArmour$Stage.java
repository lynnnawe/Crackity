package need.clarity.modules.utility;

enum AutoArmour$Stage {
   IDLE,
   WAIT_OPEN,
   WAIT_CLOSE;

   // $VF: synthetic method
   private static AutoArmour$Stage[] methodA() {
      AutoArmour$Stage[] stages = new AutoArmour$Stage[3];
      AutoArmour$Stage stage = IDLE;
      stages[0] = stage;
      AutoArmour$Stage stage1 = WAIT_OPEN;
      stages[1] = stage1;
      AutoArmour$Stage stage2 = WAIT_CLOSE;
      stages[2] = stage2;
      return stages;
   }
}
