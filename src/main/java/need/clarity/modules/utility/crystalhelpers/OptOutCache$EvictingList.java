package need.clarity.modules.utility.crystalhelpers;

import java.util.LinkedList;

final class OptOutCache$EvictingList<T> extends LinkedList<T> {
   private final int intA;

   OptOutCache$EvictingList(int i) {
      this.intA = i;
   }

   public boolean methodA(T object) {
      int i = this.size();
      int j = this.intA;
      if (i >= j && this.intA > 0) {
         this.removeFirst();
      }

      return super.add(object);
   }
}
