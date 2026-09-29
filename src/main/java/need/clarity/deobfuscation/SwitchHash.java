package need.clarity.deobfuscation;

public class SwitchHash {
   public static int hashSwitchKey(int i) {
      return i != 0 ? (i * 31 >>> 4) % i ^ i >>> 16 : 0;
   }

   public static int mixControlState(int i) {
      return (i & -536870912) >> 29 | i << 3;
   }

   public static int mixControlStateAlias(int i) {
      return (i & -536870912) >> 29 | i << 3;
   }
}
