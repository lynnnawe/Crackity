package need.clarity.modules.utility;

enum SilentTotem$Stage {
   IDLE,
   WAIT_OPEN,
   WAIT_CLOSE;

   // $VF: synthetic method
   private static SilentTotem$Stage[] methodA() {
      SilentTotem$Stage[] stages = new SilentTotem$Stage[3];
      SilentTotem$Stage stage = IDLE;
      stages[0] = stage;
      SilentTotem$Stage stage1 = WAIT_OPEN;
      stages[1] = stage1;
      SilentTotem$Stage stage2 = WAIT_CLOSE;
      stages[2] = stage2;
      return stages;
   }
}
