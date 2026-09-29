package need.clarity.modules.utility;

enum MLG$State {
   IDLE,
   AIMING,
   PLACING,
   WAITING,
   DONE,
   PICKUP;

   // $VF: synthetic method
   private static MLG$State[] methodA() {
      MLG$State[] states = new MLG$State[6];
      MLG$State state = IDLE;
      states[0] = state;
      MLG$State state1 = AIMING;
      states[1] = state1;
      MLG$State state2 = PLACING;
      states[2] = state2;
      MLG$State state3 = WAITING;
      states[3] = state3;
      MLG$State state4 = DONE;
      states[4] = state4;
      MLG$State state5 = PICKUP;
      states[5] = state5;
      return states;
   }
}
