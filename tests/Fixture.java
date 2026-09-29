import java.nio.ByteBuffer;
import java.util.Random;
public class Fixture {
 public static void main(String[] args){long sum=0;for(int x=-300;x<=300;x++){sum=31*sum+loop(x);sum=31*sum+buffer(x);sum=31*sum+random(x);sum=31*sum+exception(x);sum=31*sum+alias(x);sum=31*sum+maskedSwitch(x);for(Phase phase:Phase.values())sum=31*sum+phaseSwitch(phase);if(nullable(x)!=null)sum+=nullable(x).length();}for(int i=0;i<65536;i++)sum=31*sum+charSwitch((char)i);for(String text:new String[]{"Alpha","Beta","Gamma","FB","Ea","","other"})sum=31*sum+textSwitch(text);Random texts=new Random(42);for(int i=0;i<20000;i++)sum=31*sum+textSwitch(Long.toHexString(texts.nextLong()));System.out.println(sum);}
 static int loop(int x){int s=0;for(int i=0;i<30;i++)s+=i*x;return s;}
 static int buffer(int x){ByteBuffer b=ByteBuffer.allocate(8);b.putInt(x);b.putInt(~x);b.flip();return b.getInt()^b.getInt();}
 static int random(int x){Random r=new Random(123456L);int a=r.nextInt();int b=r.nextInt();return a^b^x;}
 static int exception(int x){int a=x;try{if(x%2==0)throw new IllegalAccessException();a+=3;}catch(IllegalAccessException e){a+=11;}try{return a+10/(x%7);}catch(ArithmeticException e){return a;}}
 static int alias(int x){int old=x;x=7;return old+x;}
 static String nullable(int x){return x<0?null:"ok";}

 enum Phase { IDLE, ACTIVE, DONE }
 static int phaseSwitch(Phase p){switch(bijwajihqirrnqvj.itjkfrmknyehiltk.qfctmcaaxjcwxdcq(p.ordinal()^91823751)){case 86084071:return 21;case 86084069:return 47;default:return -9;}}
 static int maskedSwitch(int x){int v=x&255;switch(bijwajihqirrnqvj.itjkfrmknyehiltk.qfctmcaaxjcwxdcq(v^91823751)){case 86084065:return 70;case 86084027:return 420;default:return -1;}}
 static int charSwitch(char c){char u=Character.toUpperCase(c);switch(bijwajihqirrnqvj.itjkfrmknyehiltk.qfctmcaaxjcwxdcq(u^91823751)){case 86084000:return 7;case 86083982:return 9;case 86132249:return 12;default:return -1;}}
 static int textSwitch(String text){int idx=-1;switch(bijwajihqirrnqvj.itjkfrmknyehiltk.qfctmcaaxjcwxdcq(text.hashCode()^91823751)){
 case 106161922:if(text.equals("Alpha"))idx=0;break;
 case 84946475:if(text.equals("Beta"))idx=1;break;
 case 22570591:if(text.equals("Gamma"))idx=2;break;
 }switch(bijwajihqirrnqvj.itjkfrmknyehiltk.qfctmcaaxjcwxdcq(idx^91823751)){case 86084071:return 73;case 86084068:return 44;case 86084069:return 83;default:return -99;}}
}
