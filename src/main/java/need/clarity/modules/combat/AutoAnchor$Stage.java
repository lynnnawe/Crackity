package need.clarity.modules.combat;

enum AutoAnchor$Stage {
   IDLE,
   EXPLODE_WAIT,
   SAFE_ROTATE_TO_SIDE,
   SAFE_PLACE_GLOWSTONE,
   SAFE_ROTATE_TO_ANCHOR,
   SAFE_EXPLODE,
   SILENT_PLACE_ANCHOR,
   SILENT_CHARGE,
   SILENT_EXPLODE_WAIT,
   SILENT_SAFE_ROTATE_TO_SIDE,
   SILENT_SAFE_PLACE_GLOWSTONE,
   SILENT_SAFE_ROTATE_TO_ANCHOR,
   SILENT_SAFE_EXPLODE,
   DONE;

   // $VF: synthetic method
   private static AutoAnchor$Stage[] methodA() {
      AutoAnchor$Stage[] stages = new AutoAnchor$Stage[14];
      AutoAnchor$Stage stage = IDLE;
      stages[0] = stage;
      AutoAnchor$Stage stage1 = EXPLODE_WAIT;
      stages[1] = stage1;
      AutoAnchor$Stage stage2 = SAFE_ROTATE_TO_SIDE;
      stages[2] = stage2;
      AutoAnchor$Stage stage3 = SAFE_PLACE_GLOWSTONE;
      stages[3] = stage3;
      AutoAnchor$Stage stage4 = SAFE_ROTATE_TO_ANCHOR;
      stages[4] = stage4;
      AutoAnchor$Stage stage5 = SAFE_EXPLODE;
      stages[5] = stage5;
      AutoAnchor$Stage stage6 = SILENT_PLACE_ANCHOR;
      stages[6] = stage6;
      AutoAnchor$Stage stage7 = SILENT_CHARGE;
      stages[7] = stage7;
      AutoAnchor$Stage stage8 = SILENT_EXPLODE_WAIT;
      stages[8] = stage8;
      AutoAnchor$Stage stage9 = SILENT_SAFE_ROTATE_TO_SIDE;
      stages[9] = stage9;
      AutoAnchor$Stage stage10 = SILENT_SAFE_PLACE_GLOWSTONE;
      stages[10] = stage10;
      AutoAnchor$Stage stage11 = SILENT_SAFE_ROTATE_TO_ANCHOR;
      stages[11] = stage11;
      AutoAnchor$Stage stage12 = SILENT_SAFE_EXPLODE;
      stages[12] = stage12;
      AutoAnchor$Stage stage13 = DONE;
      stages[13] = stage13;
      return stages;
   }
}
