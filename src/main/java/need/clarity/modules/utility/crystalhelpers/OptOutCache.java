package need.clarity.modules.utility.crystalhelpers;

import org.jetbrains.annotations.Nullable;

public final class OptOutCache {
   private final OptOutCache$EvictingList<String> evictingListA;
   private final OptOutCache$EvictingList<String> evictingListB;
   private volatile boolean flagC;

   public OptOutCache() {
      OptOutCache$EvictingList evictingList = new OptOutCache$EvictingList(10);
      this.evictingListA = evictingList;
      OptOutCache$EvictingList evictingList1 = new OptOutCache$EvictingList(10);
      this.evictingListB = evictingList1;
   }

   public boolean isFlagC() {
      return this.flagC;
   }

   public void methodA(boolean bl) {
      this.flagC = bl;
   }

   public void methodA(@Nullable String string) {
      if (string != null) {
         OptOutCache$EvictingList evictingList = this.evictingListA;
         synchronized (evictingList) {
            if (!this.evictingListA.contains(string)) {
               this.evictingListA.methodA(string);
            }

         }

         this.flagC = true;
      }
   }

   public boolean methodB(@Nullable String string) {
      if (string == null) {
         return false;
      }

      OptOutCache$EvictingList evictingList = this.evictingListA;
      synchronized (evictingList) {
         boolean bl = this.evictingListA.contains(string);

         return bl;
      }
   }

   public boolean methodC(@Nullable String string) {
      if (string == null) {
         return false;
      }

      OptOutCache$EvictingList evictingList = this.evictingListB;
      synchronized (evictingList) {
         boolean bl = this.evictingListB.contains(string);

         return bl;
      }
   }

   public void methodD(@Nullable String string) {
      if (string != null) {
         OptOutCache$EvictingList evictingList = this.evictingListB;
         synchronized (evictingList) {
            if (!this.evictingListB.contains(string)) {
               this.evictingListB.methodA(string);
            }

         }
      }
   }

   public void methodB() {
      this.flagC = false;
   }
}
