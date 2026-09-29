package need.clarity.modules.combat;

enum HitSwap$Phase {
   IDLE,
   WAIT_SWAP,
   WAIT_RESTORE,
   SLAM_WAIT_TO_FIRE,
   SLAM_WAIT_RESTORE;

   // $VF: synthetic method
   private static HitSwap$Phase[] methodA() {
      HitSwap$Phase[] phases = new HitSwap$Phase[5];
      HitSwap$Phase phase = IDLE;
      phases[0] = phase;
      HitSwap$Phase phase1 = WAIT_SWAP;
      phases[1] = phase1;
      HitSwap$Phase phase2 = WAIT_RESTORE;
      phases[2] = phase2;
      HitSwap$Phase phase3 = SLAM_WAIT_TO_FIRE;
      phases[3] = phase3;
      HitSwap$Phase phase4 = SLAM_WAIT_RESTORE;
      phases[4] = phase4;
      return phases;
   }
}
