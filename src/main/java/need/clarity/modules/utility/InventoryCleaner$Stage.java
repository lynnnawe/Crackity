package need.clarity.modules.utility;

enum InventoryCleaner$Stage {
   IDLE,
   WAIT_OPEN,
   THROWING,
   WAIT_CLOSE;

   // $VF: synthetic method
   private static InventoryCleaner$Stage[] methodA() {
      InventoryCleaner$Stage[] stages = new InventoryCleaner$Stage[4];
      InventoryCleaner$Stage stage = IDLE;
      stages[0] = stage;
      InventoryCleaner$Stage stage1 = WAIT_OPEN;
      stages[1] = stage1;
      InventoryCleaner$Stage stage2 = THROWING;
      stages[2] = stage2;
      InventoryCleaner$Stage stage3 = WAIT_CLOSE;
      stages[3] = stage3;
      return stages;
   }
}
