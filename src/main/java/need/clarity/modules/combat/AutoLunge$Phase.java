package need.clarity.modules.combat;

enum AutoLunge$Phase {
   IDLE,
   HOLD_LOOP_WAITING,
   SWAP_WAITING,
   SWAP_BACK_WAITING;

   // $VF: synthetic method
   private static AutoLunge$Phase[] methodA() {
      AutoLunge$Phase[] phases = new AutoLunge$Phase[4];
      AutoLunge$Phase phase = IDLE;
      phases[0] = phase;
      AutoLunge$Phase phase1 = HOLD_LOOP_WAITING;
      phases[1] = phase1;
      AutoLunge$Phase phase2 = SWAP_WAITING;
      phases[2] = phase2;
      AutoLunge$Phase phase3 = SWAP_BACK_WAITING;
      phases[3] = phase3;
      return phases;
   }
}
