package need.clarity.modules.combat;

enum ShieldBreaker$Phase {
   IDLE,
   WAITING_TO_ATTACK,
   WAITING_TO_RESTORE;

   // $VF: synthetic method
   private static ShieldBreaker$Phase[] methodA() {
      ShieldBreaker$Phase[] phases = new ShieldBreaker$Phase[3];
      ShieldBreaker$Phase phase = IDLE;
      phases[0] = phase;
      ShieldBreaker$Phase phase1 = WAITING_TO_ATTACK;
      phases[1] = phase1;
      ShieldBreaker$Phase phase2 = WAITING_TO_RESTORE;
      phases[2] = phase2;
      return phases;
   }
}
