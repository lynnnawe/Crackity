import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.*;
import java.util.*;
import java.util.jar.*;
import java.nio.file.*;
import java.io.*;

public class Clean implements Opcodes {
 static Map<String,ClassNode> classes=new LinkedHashMap<>();
 static Map<String,Object> fields=new HashMap<>();
 static Set<String> xorMethods=new HashSet<>(),decryptMethods=new HashSet<>();
 static Set<String> noiseFields=new HashSet<>();
 static Map<String,byte[]> keys=new HashMap<>(), arrayMethods=new HashMap<>(), stringBlobs=new HashMap<>();
 static int folded,branches,dead,throwsFixed,strings;
 static String key(String o,String n,String d){return o+"."+n+d;}
 static Object NULL_VALUE=new Object();
 static class Obj {String type; Object value; Obj(String t){type=t;}}
 static class Bytes {byte[] data; BitSet set=new BitSet(); Bytes(int n){data=new byte[n];}public boolean equals(Object o){return o instanceof Bytes b&&Arrays.equals(data,b.data)&&set.equals(b.set);}public int hashCode(){return Arrays.hashCode(data)^set.hashCode();}}
 static class V extends BasicValue {Object c; V(Type t,Object c){super(t);this.c=c;} public boolean equals(Object o){return o instanceof V v&&Objects.equals(getType(),v.getType())&&Objects.equals(c,v.c);}public int hashCode(){return Objects.hash(getType(),c);}}
 static Object c(BasicValue v){return v instanceof V ? ((V)v).c:null;}
 static BasicValue val(Object x){return new V(x instanceof Integer?Type.INT_TYPE:x instanceof Long?Type.LONG_TYPE:x instanceof Float?Type.FLOAT_TYPE:x instanceof Double?Type.DOUBLE_TYPE:Type.getType(Object.class),x);}
 static AbstractInsnNode prev(AbstractInsnNode n){do{n=n.getPrevious();}while(n!=null&&n.getOpcode()<0);return n;}
 static AbstractInsnNode next(AbstractInsnNode n){do{n=n.getNext();}while(n!=null&&n.getOpcode()<0);return n;}
 static Integer icon(AbstractInsnNode n){if(n==null)return null;int o=n.getOpcode();if(o>=ICONST_M1&&o<=ICONST_5)return o-ICONST_0;if(n instanceof IntInsnNode i&&(o==BIPUSH||o==SIPUSH))return i.operand;if(n instanceof LdcInsnNode l&&l.cst instanceof Integer i)return i;return null;}
 static class Eval extends BasicInterpreter {
  Eval(){super(ASM9);}
  public BasicValue newValue(Type t){BasicValue b=super.newValue(t);return b==null?null:new V(b.getType(),null);}
  public BasicValue newOperation(AbstractInsnNode n)throws AnalyzerException{
   Integer i=icon(n);if(i!=null)return val(i); if(n instanceof LdcInsnNode l&&!(l.cst instanceof Type)&&!(l.cst instanceof Handle)&&!(l.cst instanceof ConstantDynamic))return val(l.cst);
   if(n.getOpcode()==ACONST_NULL)return val(NULL_VALUE);
   if(n.getOpcode()>=LCONST_0&&n.getOpcode()<=LCONST_1)return val((long)(n.getOpcode()-LCONST_0));
   if(n.getOpcode()>=FCONST_0&&n.getOpcode()<=FCONST_2)return val((float)(n.getOpcode()-FCONST_0));
   if(n.getOpcode()>=DCONST_0&&n.getOpcode()<=DCONST_1)return val((double)(n.getOpcode()-DCONST_0));
   if(n.getOpcode()==GETSTATIC&&n instanceof FieldInsnNode f){Object x=fields.get(key(f.owner,f.name,f.desc));if(x!=null)return val(x);}
   if(n.getOpcode()==NEW)return val(new Obj(((TypeInsnNode)n).desc));
   return super.newOperation(n);
  }
  public BasicValue copyOperation(AbstractInsnNode n,BasicValue v){return v;}
  public BasicValue unaryOperation(AbstractInsnNode n,BasicValue v)throws AnalyzerException{
   Object a=c(v);int o=n.getOpcode();
   if(o==GETFIELD&&n instanceof FieldInsnNode f){Object x=fields.get(key(f.owner,f.name,f.desc));if(x!=null)return val(x);}
   if(a instanceof Integer x){switch(o){case INEG:return val(-x);case IINC:return val(x+((IincInsnNode)n).incr);case I2B:return val((int)(byte)(int)x);case I2C:return val((int)(char)(int)x);case I2S:return val((int)(short)(int)x);case I2L:return val((long)x);case I2F:return val((float)x);case I2D:return val((double)x);case NEWARRAY:if(((IntInsnNode)n).operand==T_BYTE&&x>=0&&x<1000000)return val(new Bytes(x));}}
   if(a instanceof Long x){switch(o){case LNEG:return val(-x);case L2I:return val((int)(long)x);case L2D:return val((double)x);case L2F:return val((float)x);}}
   if(a instanceof Float x){switch(o){case FNEG:return val(-x);case F2I:return val((int)(float)x);case F2L:return val((long)(float)x);case F2D:return val((double)x);}}
   if(a instanceof Double x){switch(o){case DNEG:return val(-x);case D2I:return val((int)(double)x);case D2L:return val((long)(double)x);case D2F:return val((float)(double)x);}}
   if(o==CHECKCAST)return v;
   if(o==ARRAYLENGTH&&a instanceof Bytes b)return val(b.data.length);
   return super.unaryOperation(n,v);
  }
  public BasicValue binaryOperation(AbstractInsnNode n,BasicValue v,BasicValue w)throws AnalyzerException{
   Object a=c(v),b=c(w);int o=n.getOpcode();
   if(a instanceof Integer x&&b instanceof Integer y){switch(o){case IADD:return val(x+y);case ISUB:return val(x-y);case IMUL:return val(x*y);case IDIV:if(y!=0)return val(x/y);break;case IREM:if(y!=0)return val(x%y);break;case IXOR:return val(x^y);case IAND:return val(x&y);case IOR:return val(x|y);case ISHL:return val(x<<y);case ISHR:return val(x>>y);case IUSHR:return val(x>>>y);}}
   if(a instanceof Long x&&b instanceof Long y){switch(o){case LADD:return val(x+y);case LSUB:return val(x-y);case LMUL:return val(x*y);case LDIV:if(y!=0)return val(x/y);break;case LREM:if(y!=0)return val(x%y);break;case LXOR:return val(x^y);case LAND:return val(x&y);case LOR:return val(x|y);case LCMP:return val(Long.compare(x,y));}}
   if(a instanceof Long x&&b instanceof Integer y){switch(o){case LSHL:return val(x<<y);case LSHR:return val(x>>y);case LUSHR:return val(x>>>y);}}
   if(a instanceof Bytes ar&&b instanceof Integer idx&&o==BALOAD&&idx>=0&&idx<ar.data.length&&ar.set.get(idx))return val((int)ar.data[idx]);
   return super.binaryOperation(n,v,w);
  }
  public BasicValue ternaryOperation(AbstractInsnNode n,BasicValue a,BasicValue b,BasicValue d)throws AnalyzerException{
   if(n.getOpcode()==BASTORE&&c(a) instanceof Bytes ar&&c(b) instanceof Integer ix&&c(d) instanceof Integer x&&ix>=0&&ix<ar.data.length){ar.data[ix]=(byte)(int)x;ar.set.set(ix);}
   return super.ternaryOperation(n,a,b,d);
  }
  public BasicValue naryOperation(AbstractInsnNode n,List<? extends BasicValue> vs)throws AnalyzerException{
   if(n instanceof MethodInsnNode m){List<Object> a=vs.stream().map(Clean::c).toList();String k=key(m.owner,m.name,m.desc);
    try{
     if(m.owner.equals("java/lang/Integer")&&m.name.equals("parseInt")&&a.size()==1&&a.get(0) instanceof String s)return val(Integer.parseInt(s));
     if(xorMethods.contains(k)&&a.get(0) instanceof Integer x&&a.get(1) instanceof Integer y)return val(x^y);
     if(m.owner.equals("bijwajihqirrnqvj/itjkfrmknyehiltk")&&a.get(0) instanceof Integer x){if(m.name.equals("qfctmcaaxjcwxdcq"))return val(x==0?0:((x*31)>>>4)%x^(x>>>16));if(m.name.equals("hyqmlgqtcqiakcqo")||m.name.equals("gualsbgxduatglmp"))return val(((x&(7<<29))>>29)|(x<<3));}
     if(m.owner.equals("java/util/Random")&&m.name.equals("<init>")&&a.get(0) instanceof Obj ob&&a.get(1) instanceof Long seed)ob.value=new Random(seed).nextInt();
     if(m.owner.equals("java/util/Random")&&m.name.equals("nextInt")&&m.desc.equals("()I")&&a.get(0) instanceof Obj ob&&ob.value instanceof Integer&&prev(n) instanceof MethodInsnNode ctor&&ctor.owner.equals("java/util/Random")&&ctor.name.equals("<init>")&&ctor.desc.equals("(J)V")&&prev(ctor) instanceof LdcInsnNode seed&&seed.cst instanceof Long&&prev(seed).getOpcode()==DUP&&prev(prev(seed)) instanceof TypeInsnNode nw&&nw.getOpcode()==NEW&&nw.desc.equals("java/util/Random"))return val(ob.value);
     if(m.owner.equals("java/lang/String")&&m.name.equals("getBytes")&&a.size()==1&&a.get(0) instanceof String s){Bytes b=new Bytes(s.length());b.data=s.getBytes(java.nio.charset.StandardCharsets.UTF_8);b.set.set(0,b.data.length);return val(b);}
     if(arrayMethods.containsKey(k)){Bytes ar=new Bytes(arrayMethods.get(k).length);ar.data=arrayMethods.get(k).clone();ar.set.set(0,ar.data.length);return val(ar);}
     if(decryptMethods.contains(k)&&a.get(0) instanceof Bytes ar&&a.get(a.size()-1) instanceof Integer seed&&ar.set.cardinality()==ar.data.length&&stringBlobs.containsKey(m.owner)&&ar.data.length==8){
      java.nio.ByteBuffer bb=java.nio.ByteBuffer.wrap(ar.data);int len=bb.getInt(),off=bb.getInt();byte[] blob=stringBlobs.get(m.owner);if(len>=0&&off>=0&&(long)(off+len)*2<=blob.length){byte[] out=Arrays.copyOfRange(blob,off*2,(off+len)*2),sk=seed.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);for(int i=0;i<out.length;i++)out[i]^=sk[i%sk.length];return val(new String(out,java.nio.charset.StandardCharsets.UTF_16BE));}
     }
     if(decryptMethods.contains(k)&&a.get(0) instanceof Bytes ar&&a.get(a.size()-1) instanceof Integer seed&&ar.set.cardinality()==ar.data.length&&(a.size()==3&&a.get(1) instanceof Bytes||keys.containsKey(m.owner))){
      byte[] out=ar.data.clone(),sk=seed.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8),ck=a.size()==3?((Bytes)a.get(1)).data:keys.get(m.owner);for(int i=0;i<out.length;i++)out[i]^=sk[i%sk.length]^ck[i%ck.length];return val(new String(out,java.nio.charset.StandardCharsets.UTF_16));
     }
    }catch(RuntimeException ignored){}
   }
   return super.naryOperation(n,vs);
  }
  public BasicValue merge(BasicValue a,BasicValue b){if(Objects.equals(a.getType(),b.getType())&&Objects.equals(c(a),c(b)))return a;BasicValue aa=new BasicValue(a.getType()),bb=new BasicValue(b.getType());BasicValue r=super.merge(aa,bb);return new V(r.getType(),null);}
 }
 static Boolean branch(int o,Frame<BasicValue> f){Object a=f.getStackSize()>0?c(f.getStack(f.getStackSize()-1)):null,b=f.getStackSize()>1?c(f.getStack(f.getStackSize()-2)):null;
  if(o>=IFEQ&&o<=IFLE&&a instanceof Integer x)return switch(o){case IFEQ->x==0;case IFNE->x!=0;case IFLT->x<0;case IFGE->x>=0;case IFGT->x>0;default->x<=0;};
  if(o>=IF_ICMPEQ&&o<=IF_ICMPLE&&a instanceof Integer y&&b instanceof Integer x)return switch(o){case IF_ICMPEQ->x.intValue()==y.intValue();case IF_ICMPNE->x.intValue()!=y.intValue();case IF_ICMPLT->x<y;case IF_ICMPGE->x>=y;case IF_ICMPGT->x>y;default->x<=y;};
  if((o==IFNULL||o==IFNONNULL)&&a!=null)return o==IFNULL?a==NULL_VALUE:a!=NULL_VALUE;
  return null;
 }
 static boolean canThrow(AbstractInsnNode n){int o=n.getOpcode();if(n instanceof MethodInsnNode m){if(xorMethods.contains(key(m.owner,m.name,m.desc))||m.owner.equals("bijwajihqirrnqvj/itjkfrmknyehiltk"))return false;if(m.name.equals("<init>")&&(m.owner.equals("java/io/IOException")||m.owner.equals("java/lang/RuntimeException")||m.owner.equals("java/lang/IllegalAccessException")))return false;}
  return o==ATHROW||o==GETFIELD||o==PUTFIELD||o==GETSTATIC||o==PUTSTATIC||o==CHECKCAST||o==ARRAYLENGTH||o==IDIV||o==IREM||o==LDIV||o==LREM||(o>=IALOAD&&o<=SALOAD)||(o>=IASTORE&&o<=SASTORE)||o==MONITORENTER||o==MONITOREXIT||(o>=INVOKEVIRTUAL&&o<=INVOKEDYNAMIC);
 }
 static Frame<BasicValue>[] analyze(ClassNode cn,MethodNode m)throws Exception{
  int n=m.instructions.size();Frame<BasicValue>[] fs=new Frame[n];if(n==0)return fs;Eval e=new Eval();Frame<BasicValue> start=new Frame<>(m.maxLocals,Math.max(64,m.maxStack+8));
  for(int i=0;i<m.maxLocals;i++)start.setLocal(i,BasicValue.UNINITIALIZED_VALUE);int li=0;if((m.access&ACC_STATIC)==0)start.setLocal(li++,BasicValue.REFERENCE_VALUE);for(Type t:Type.getArgumentTypes(m.desc)){start.setLocal(li++,e.newValue(t));if(t.getSize()==2)li++;}start.setReturn(e.newValue(Type.getReturnType(m.desc)));
  ArrayDeque<Integer> q=new ArrayDeque<>();boolean[] queued=new boolean[n];fs[0]=start;q.add(0);queued[0]=true;int steps=0;
  while(!q.isEmpty()){if(++steps>2000000)throw new Exception("analysis limit");int i=q.removeFirst();queued[i]=false;AbstractInsnNode ins=m.instructions.get(i);int o=ins.getOpcode();Frame<BasicValue> before=fs[i],after=new Frame<>(before);if(o>=0)after.execute(ins,e);
   List<Integer> edges=new ArrayList<>();
   if(ins instanceof JumpInsnNode j){Boolean b=o==GOTO?Boolean.TRUE:branch(o,before);if(b==null||b)edges.add(m.instructions.indexOf(j.label));if(o!=GOTO&&(b==null||!b))edges.add(i+1);}
   else if(ins instanceof LookupSwitchInsnNode s){Object x=c(before.getStack(before.getStackSize()-1));if(x instanceof Integer v){int ix=s.keys.indexOf(v);edges.add(m.instructions.indexOf(ix<0?s.dflt:s.labels.get(ix)));}else{edges.add(m.instructions.indexOf(s.dflt));for(LabelNode l:s.labels)edges.add(m.instructions.indexOf(l));}}
   else if(ins instanceof TableSwitchInsnNode s){Object x=c(before.getStack(before.getStackSize()-1));if(x instanceof Integer v)edges.add(m.instructions.indexOf(v>=s.min&&v<=s.max?s.labels.get(v-s.min):s.dflt));else{edges.add(m.instructions.indexOf(s.dflt));for(LabelNode l:s.labels)edges.add(m.instructions.indexOf(l));}}
   else if(o!=ATHROW&&!(o>=IRETURN&&o<=RETURN))edges.add(i+1);
   for(int j:edges)merge(j,after,fs,q,queued,e);
   if(canThrow(ins))for(TryCatchBlockNode t:m.tryCatchBlocks)if(i>=m.instructions.indexOf(t.start)&&i<m.instructions.indexOf(t.end)){Frame<BasicValue> ex=new Frame<>(before);ex.clearStack();ex.push(BasicValue.REFERENCE_VALUE);merge(m.instructions.indexOf(t.handler),ex,fs,q,queued,e);}
  }return fs;
 }
 static List<Frame<BasicValue>>[] pathFrames;
 record State(int index,Integer value){}
 static Frame<BasicValue>[] analyzePaths(ClassNode cn,MethodNode m)throws Exception{
  int stateVar=-1;Map<Integer,Integer> uses=new HashMap<>();for(AbstractInsnNode n:m.instructions)if(n instanceof MethodInsnNode call&&call.owner.equals("bijwajihqirrnqvj/itjkfrmknyehiltk")&&prev(n) instanceof VarInsnNode v&&v.getOpcode()==ILOAD)uses.merge(v.var,1,Integer::sum);
  if(!uses.isEmpty())stateVar=uses.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
  if(stateVar<0){pathFrames=null;return analyze(cn,m);}
  int n=m.instructions.size();Eval e=new Eval();Frame<BasicValue> start=new Frame<>(m.maxLocals,Math.max(64,m.maxStack+8));for(int i=0;i<m.maxLocals;i++)start.setLocal(i,e.newValue(null));int li=0;if((m.access&ACC_STATIC)==0)start.setLocal(li++,e.newValue(Type.getType(Object.class)));for(Type t:Type.getArgumentTypes(m.desc)){start.setLocal(li++,e.newValue(t));if(t.getSize()==2)li++;}start.setReturn(e.newValue(Type.getReturnType(m.desc)));
  Map<State,Frame<BasicValue>> all=new LinkedHashMap<>();ArrayDeque<State> q=new ArrayDeque<>();Set<State> queued=new HashSet<>();State initial=new State(0,null);all.put(initial,start);q.add(initial);queued.add(initial);int steps=0;
  while(!q.isEmpty()){if(++steps>1500000)throw new Exception("path analysis limit");State s0=q.removeFirst();queued.remove(s0);int i=s0.index;AbstractInsnNode ins=m.instructions.get(i);int o=ins.getOpcode();Frame<BasicValue> before=all.get(s0),after=new Frame<>(before);if(o>=0)after.execute(ins,e);List<Integer> edges=new ArrayList<>();
   if(ins instanceof JumpInsnNode j){Boolean b=o==GOTO?Boolean.TRUE:branch(o,before);if(b==null||b)edges.add(m.instructions.indexOf(j.label));if(o!=GOTO&&(b==null||!b))edges.add(i+1);}
   else if(ins instanceof LookupSwitchInsnNode s){Object x=c(before.getStack(before.getStackSize()-1));if(x instanceof Integer v){int ix=s.keys.indexOf(v);edges.add(m.instructions.indexOf(ix<0?s.dflt:s.labels.get(ix)));}else{edges.add(m.instructions.indexOf(s.dflt));for(LabelNode l:s.labels)edges.add(m.instructions.indexOf(l));}}
   else if(ins instanceof TableSwitchInsnNode s){Object x=c(before.getStack(before.getStackSize()-1));if(x instanceof Integer v)edges.add(m.instructions.indexOf(v>=s.min&&v<=s.max?s.labels.get(v-s.min):s.dflt));else{edges.add(m.instructions.indexOf(s.dflt));for(LabelNode l:s.labels)edges.add(m.instructions.indexOf(l));}}
   else if(o!=ATHROW&&!(o>=IRETURN&&o<=RETURN))edges.add(i+1);
   for(int j:edges)pathMerge(j,after,stateVar,n,all,q,queued,e);
   if(canThrow(ins))for(TryCatchBlockNode t:m.tryCatchBlocks)if(i>=m.instructions.indexOf(t.start)&&i<m.instructions.indexOf(t.end)){Frame<BasicValue> ex=new Frame<>(before);ex.clearStack();ex.push(e.newValue(Type.getType(Object.class)));pathMerge(m.instructions.indexOf(t.handler),ex,stateVar,n,all,q,queued,e);}
  }
  Frame<BasicValue>[] fs=new Frame[n];pathFrames=new List[n];for(var en:all.entrySet()){int i=en.getKey().index;if(pathFrames[i]==null)pathFrames[i]=new ArrayList<>();pathFrames[i].add(en.getValue());if(fs[i]==null)fs[i]=new Frame<>(en.getValue());else fs[i].merge(en.getValue(),e);}return fs;
 }
 static void pathMerge(int j,Frame<BasicValue> f,int var,int n,Map<State,Frame<BasicValue>> all,ArrayDeque<State> q,Set<State> queued,Eval e)throws Exception{if(j<0||j>=n)return;Object v=c(f.getLocal(var));State s=new State(j,v instanceof Integer x?x:null);boolean change;if(!all.containsKey(s)){all.put(s,new Frame<>(f));change=true;}else change=all.get(s).merge(f,e);if(change&&queued.add(s))q.add(s);}
 static Boolean uniformBranch(int o,int i,Frame<BasicValue> f){if(pathFrames==null)return branch(o,f);Boolean found=null;for(Frame<BasicValue> p:pathFrames[i]){Boolean b=branch(o,p);if(b==null||found!=null&&!found.equals(b))return null;found=b;}return found;}
 static void merge(int j,Frame<BasicValue> f,Frame<BasicValue>[] fs,ArrayDeque<Integer> q,boolean[] queued,Eval e)throws Exception{if(j<0||j>=fs.length)return;boolean change;if(fs[j]==null){fs[j]=new Frame<>(f);change=true;}else change=fs[j].merge(f,e);if(change&&!queued[j]){q.add(j);queued[j]=true;}}
 static void directThrows(MethodNode m){for(AbstractInsnNode n:m.instructions.toArray())if(n.getOpcode()==ATHROW){AbstractInsnNode p=prev(n);if(!(p instanceof MethodInsnNode call)||!(call.owner.equals("java/io/IOException")||call.owner.equals("java/lang/RuntimeException")||call.owner.equals("java/lang/IllegalAccessException"))||!call.name.equals("<init>"))continue;int i=m.instructions.indexOf(n);for(TryCatchBlockNode t:m.tryCatchBlocks){if(i>=m.instructions.indexOf(t.start)&&i<m.instructions.indexOf(t.end)&&(t.type==null||t.type.equals(call.owner)||t.type.equals("java/lang/Exception")||t.type.equals("java/lang/Throwable"))){m.instructions.set(n,new JumpInsnNode(GOTO,t.handler));throwsFixed++;break;}}}}
 static void replace(MethodNode m,AbstractInsnNode n,Object value,int... sizes){InsnList l=new InsnList();for(int s:sizes)l.add(new InsnNode(s==2?POP2:POP));if(value==NULL_VALUE)l.add(new InsnNode(ACONST_NULL));else if(value instanceof Integer v&&v>=-1&&v<=5)l.add(new InsnNode(ICONST_0+v));else l.add(new LdcInsnNode(value));m.instructions.insertBefore(n,l);m.instructions.remove(n);folded++;}
 static boolean clean(ClassNode cn,MethodNode m)throws Exception{
  Frame<BasicValue>[] fs=analyzePaths(cn,m);AbstractInsnNode[] ns=m.instructions.toArray();Eval e=new Eval();boolean change=false;
  m.tryCatchBlocks.removeIf(t->fs[m.instructions.indexOf(t.handler)]==null);
  for(int i=0;i<ns.length;i++){AbstractInsnNode n=ns[i];int o=n.getOpcode();Frame<BasicValue> f=fs[i];if(f==null&&o>=0){m.instructions.remove(n);dead++;change=true;continue;}if(f==null||o<0)continue;int sz=f.getStackSize();
   if(n instanceof JumpInsnNode j&&o!=GOTO){Boolean b=uniformBranch(o,i,f);if(b!=null){InsnList l=new InsnList();l.add(new InsnNode(POP));if(o>=IF_ICMPEQ&&o<=IF_ACMPNE)l.add(new InsnNode(POP));if(b)l.add(new JumpInsnNode(GOTO,j.label));m.instructions.insertBefore(n,l);m.instructions.remove(n);branches++;change=true;continue;}}
   if(n instanceof LookupSwitchInsnNode s&&c(f.getStack(sz-1)) instanceof Integer x){int ix=s.keys.indexOf(x);m.instructions.insertBefore(n,new InsnNode(POP));m.instructions.set(n,new JumpInsnNode(GOTO,ix<0?s.dflt:s.labels.get(ix)));branches++;change=true;continue;}
   if(n instanceof TableSwitchInsnNode s&&c(f.getStack(sz-1)) instanceof Integer x){m.instructions.insertBefore(n,new InsnNode(POP));m.instructions.set(n,new JumpInsnNode(GOTO,x>=s.min&&x<=s.max?s.labels.get(x-s.min):s.dflt));branches++;change=true;continue;}
   if(pathFrames!=null&&(n instanceof LookupSwitchInsnNode||n instanceof TableSwitchInsnNode)){
    Set<Integer> values=new HashSet<>();boolean known=true;for(Frame<BasicValue> p:pathFrames[i]){Object v=c(p.getStack(p.getStackSize()-1));if(v instanceof Integer x)values.add(x);else{known=false;break;}}
    if(known&&!values.isEmpty()){List<Integer> ks=new ArrayList<>(values);Collections.sort(ks);List<LabelNode> ls=new ArrayList<>();for(int v:ks){LabelNode l;if(n instanceof LookupSwitchInsnNode s){int ix=s.keys.indexOf(v);l=ix<0?s.dflt:s.labels.get(ix);}else{TableSwitchInsnNode s=(TableSwitchInsnNode)n;l=v>=s.min&&v<=s.max?s.labels.get(v-s.min):s.dflt;}ls.add(l);}LookupSwitchInsnNode replacement=new LookupSwitchInsnNode(ls.get(0),ks.stream().mapToInt(Integer::intValue).toArray(),ls.toArray(LabelNode[]::new));m.instructions.set(n,replacement);change=true;continue;}
   }
   BasicValue result=null;int[] sizes=null;
   if(n instanceof VarInsnNode v&&o>=ILOAD&&o<=ALOAD){result=f.getLocal(v.var);sizes=new int[0];}
   else if(o==GETSTATIC){result=e.newOperation(n);sizes=new int[0];}
   else if(o==GETFIELD||o==INEG||o==LNEG||o>=I2L&&o<=I2S){result=e.unaryOperation(n,f.getStack(sz-1));sizes=new int[]{f.getStack(sz-1).getSize()};}
   else if((o>=IADD&&o<=DREM||o>=ISHL&&o<=LXOR)){result=e.binaryOperation(n,f.getStack(sz-2),f.getStack(sz-1));sizes=new int[]{f.getStack(sz-1).getSize(),f.getStack(sz-2).getSize()};}
   else if(n instanceof MethodInsnNode call&&Type.getReturnType(call.desc).getSort()!=Type.VOID){int count=Type.getArgumentTypes(call.desc).length+(o==INVOKESTATIC?0:1);List<BasicValue> vs=new ArrayList<>();sizes=new int[count];for(int j=0;j<count;j++){vs.add(f.getStack(sz-count+j));sizes[j]=f.getStack(sz-1-j).getSize();}result=e.naryOperation(n,vs);}
   if(result!=null&&c(result)==null&&pathFrames!=null&&pathFrames[i].size()>1&&((o>=IADD&&o<=DREM||o>=ISHL&&o<=LXOR)||n instanceof MethodInsnNode)){
    Object common=null;boolean same=true;for(Frame<BasicValue> p:pathFrames[i]){BasicValue r=null;int ps=p.getStackSize();if(n instanceof MethodInsnNode call){int count=Type.getArgumentTypes(call.desc).length+(o==INVOKESTATIC?0:1);List<BasicValue> vs=new ArrayList<>();for(int j=ps-count;j<ps;j++)vs.add(p.getStack(j));r=e.naryOperation(n,vs);}else r=e.binaryOperation(n,p.getStack(ps-2),p.getStack(ps-1));Object v=c(r);if(v==null||common!=null&&!Objects.equals(common,v)){same=false;break;}common=v;}if(same&&common!=null)result=val(common);
   }
   Object x=c(result);if(x instanceof Integer||x instanceof Long||x instanceof Float||x instanceof Double||x instanceof String||x==NULL_VALUE){replace(m,n,x,sizes);change=true;if(n instanceof MethodInsnNode call&&decryptMethods.contains(key(call.owner,call.name,call.desc)))strings++;}
  }
  m.tryCatchBlocks.removeIf(t->{int a=m.instructions.indexOf(t.start),b=m.instructions.indexOf(t.end);if(a<0||b<=a)return true;for(int j=a;j<b;j++)if(canThrow(m.instructions.get(j)))return false;return true;});
  return peephole(m)||change;
 }
 static void aliases(MethodNode m){
  int params=(m.access&ACC_STATIC)==0?1:0;for(Type t:Type.getArgumentTypes(m.desc))params+=t.getSize();
  for(int round=0;round<15;round++){
   Map<Integer,Integer> counts=new HashMap<>();for(AbstractInsnNode n:m.instructions)if(n instanceof VarInsnNode v&&v.getOpcode()>=ISTORE&&v.getOpcode()<=ASTORE)counts.merge(v.var,1,Integer::sum);else if(n instanceof IincInsnNode v)counts.merge(v.var,2,Integer::sum);
   boolean changed=false;
   for(AbstractInsnNode n:m.instructions.toArray())if(n instanceof VarInsnNode st&&st.getOpcode()>=ISTORE&&st.getOpcode()<=ASTORE&&counts.getOrDefault(st.var,0)==1&&n.getPrevious() instanceof VarInsnNode ld&&ld.getOpcode()==st.getOpcode()-ISTORE+ILOAD&&ld.var!=st.var&&counts.getOrDefault(ld.var,0)<=(ld.var<params?0:1)){
    for(AbstractInsnNode x:m.instructions)if(x instanceof VarInsnNode u&&u.var==st.var&&u.getOpcode()==ld.getOpcode())u.var=ld.var;
    m.instructions.remove(ld);m.instructions.remove(st);changed=true;
   }if(!changed)break;
  }
  Map<Integer,Integer> reads=new HashMap<>();for(AbstractInsnNode n:m.instructions)if(n instanceof VarInsnNode v&&v.getOpcode()>=ILOAD&&v.getOpcode()<=ALOAD)reads.merge(v.var,1,Integer::sum);else if(n instanceof IincInsnNode v)reads.merge(v.var,2,Integer::sum);
  for(AbstractInsnNode n:m.instructions.toArray())if(n instanceof VarInsnNode st&&st.getOpcode()>=ISTORE&&st.getOpcode()<=ASTORE&&reads.getOrDefault(st.var,0)==1&&n.getNext() instanceof VarInsnNode ld&&ld.getOpcode()==st.getOpcode()-ISTORE+ILOAD&&ld.var==st.var){m.instructions.remove(ld);m.instructions.remove(st);}
 }
 static void stripLabels(MethodNode m){Set<LabelNode> keep=new HashSet<>();for(AbstractInsnNode n:m.instructions){if(n instanceof JumpInsnNode j)keep.add(j.label);else if(n instanceof LookupSwitchInsnNode s){keep.add(s.dflt);keep.addAll(s.labels);}else if(n instanceof TableSwitchInsnNode s){keep.add(s.dflt);keep.addAll(s.labels);}}for(TryCatchBlockNode t:m.tryCatchBlocks){keep.add(t.start);keep.add(t.end);keep.add(t.handler);}m.localVariables=null;for(AbstractInsnNode n:m.instructions.toArray())if(n instanceof LineNumberNode||n instanceof FrameNode||n instanceof LabelNode l&&!keep.contains(l))m.instructions.remove(n);}
 static AbstractInsnNode follow(AbstractInsnNode n){Set<AbstractInsnNode> seen=new HashSet<>();while(n!=null&&seen.add(n)){if(n.getOpcode()<0)n=n.getNext();else if(n instanceof JumpInsnNode j&&j.getOpcode()==GOTO)n=j.label;else return n;}return n;}
 static LabelNode target(MethodNode m,LabelNode l){AbstractInsnNode n=follow(l);if(n==null)return l;AbstractInsnNode p=n.getPrevious();if(p instanceof LabelNode lab)return lab;LabelNode lab=new LabelNode();m.instructions.insertBefore(n,lab);return lab;}
 static String blockKey(LabelNode l,MethodNode m){AbstractInsnNode a=follow(l);if(a==null)return "end";if(a instanceof LdcInsnNode ld&&next(a) instanceof VarInsnNode st&&st.getOpcode()==ISTORE){AbstractInsnNode dest=follow(st.getNext());return ld.cst+":"+st.var+":"+m.instructions.indexOf(dest);}return "i"+m.instructions.indexOf(a);}
 static boolean peephole(MethodNode m){boolean any=false,change=true;int pass=0;while(change&&pass++<100){change=false;Set<Integer> used=new HashSet<>();for(AbstractInsnNode n:m.instructions)if(n instanceof VarInsnNode v&&v.getOpcode()>=ILOAD&&v.getOpcode()<=ALOAD)used.add(v.var);else if(n instanceof IincInsnNode v)used.add(v.var);
   for(AbstractInsnNode n:m.instructions.toArray()){
    int o=n.getOpcode();if(n instanceof VarInsnNode v&&o>=ISTORE&&o<=ASTORE&&!used.contains(v.var)){m.instructions.set(n,new InsnNode(o==LSTORE||o==DSTORE?POP2:POP));change=true;continue;}
    if(n instanceof JumpInsnNode j){LabelNode old=j.label;j.label=target(m,j.label);if(old!=j.label)change=true;if(o==GOTO){AbstractInsnNode dest=follow(j.label);if(dest!=null&&(dest.getOpcode()>=IRETURN&&dest.getOpcode()<=RETURN||dest.getOpcode()==ATHROW)){m.instructions.set(n,new InsnNode(dest.getOpcode()));change=true;continue;}}}
    if(n instanceof LookupSwitchInsnNode s){s.dflt=target(m,s.dflt);for(int z=0;z<s.labels.size();z++)s.labels.set(z,target(m,s.labels.get(z)));}else if(n instanceof TableSwitchInsnNode s){s.dflt=target(m,s.dflt);for(int z=0;z<s.labels.size();z++)s.labels.set(z,target(m,s.labels.get(z)));}
    if(o==POP||o==POP2){AbstractInsnNode p=n.getPrevious();if(p==null)continue;int po=p.getOpcode();boolean wide=po==LLOAD||po==DLOAD||po==LCONST_0||po==LCONST_1||po==DCONST_0||po==DCONST_1||p instanceof LdcInsnNode l&&(l.cst instanceof Long||l.cst instanceof Double);
     boolean push=p instanceof LdcInsnNode||icon(p)!=null||po==ACONST_NULL||po>=ILOAD&&po<=ALOAD||po>=LCONST_0&&po<=DCONST_1;
     if(push&&(wide==(o==POP2))){m.instructions.remove(n);m.instructions.remove(p);change=true;continue;}
     if(po==DUP&&o==POP){m.instructions.remove(n);m.instructions.remove(p);change=true;continue;}
     if(p instanceof MethodInsnNode call&&o==POP&&(xorMethods.contains(key(call.owner,call.name,call.desc))||call.owner.equals("bijwajihqirrnqvj/itjkfrmknyehiltk"))){for(Type t:Type.getArgumentTypes(call.desc))m.instructions.insertBefore(p,new InsnNode(POP));m.instructions.remove(p);m.instructions.remove(n);change=true;continue;}
     if(p instanceof MethodInsnNode call&&o==POP&&(arrayMethods.containsKey(key(call.owner,call.name,call.desc))||call.owner.equals("java/nio/ByteBuffer")&&(call.name.equals("wrap")||call.name.equals("asCharBuffer"))||call.owner.equals("java/nio/CharBuffer")&&call.name.equals("toString"))){InsnList pops=new InsnList();Type[] ts=Type.getArgumentTypes(call.desc);for(int j=ts.length-1;j>=0;j--)pops.add(new InsnNode(ts[j].getSize()==2?POP2:POP));if(call.getOpcode()!=INVOKESTATIC)pops.add(new InsnNode(POP));m.instructions.insertBefore(p,pops);m.instructions.remove(p);m.instructions.remove(n);change=true;continue;}
     if(p instanceof MethodInsnNode call&&call.name.equals("<init>")&&o==POP&&(call.owner.equals("java/io/IOException")||call.owner.equals("java/lang/RuntimeException")||call.owner.equals("java/lang/IllegalAccessException")||call.owner.equals("java/util/Random"))){AbstractInsnNode arg=p.getPrevious(),du=call.desc.equals("()V")?arg:arg==null?null:arg.getPrevious();if(du!=null&&du.getOpcode()==DUP&&du.getPrevious() instanceof TypeInsnNode nw&&nw.getOpcode()==NEW&&nw.desc.equals(call.owner)&&(call.desc.equals("()V")||arg instanceof LdcInsnNode)){m.instructions.remove(n);m.instructions.remove(p);if(arg!=du)m.instructions.remove(arg);m.instructions.remove(du);m.instructions.remove(nw);change=true;continue;}}
    }
    if(n instanceof JumpInsnNode j&&o==GOTO){AbstractInsnNode x=n.getNext();while(x!=null&&x.getOpcode()<0){if(x==j.label){m.instructions.remove(n);change=true;break;}x=x.getNext();}}
    if(n instanceof JumpInsnNode j&&o>=IF_ICMPEQ&&o<=IF_ICMPLE&&Integer.valueOf(0).equals(icon(n.getPrevious()))){AbstractInsnNode p=n.getPrevious();m.instructions.remove(p);j.setOpcode(o-IF_ICMPEQ+IFEQ);change=true;}
    if(n instanceof LookupSwitchInsnNode s){String k=blockKey(s.dflt,m);if(s.labels.stream().allMatch(l->blockKey(l,m).equals(k))){m.instructions.insertBefore(n,new InsnNode(POP));m.instructions.set(n,new JumpInsnNode(GOTO,s.dflt));change=true;}}
   }any|=change;
   for(TryCatchBlockNode t:m.tryCatchBlocks)t.handler=target(m,t.handler);
   stripLabels(m);aliases(m);
  }return any;}
 static void discover()throws Exception{
  for(ClassNode cn:classes.values()){
   boolean pair=cn.fields.stream().anyMatch(f->f.desc.equals("I")&&(f.access&ACC_TRANSIENT)!=0&&f.name.matches("[A-Za-z0-9]{10}"));
   for(FieldNode f:cn.fields)if(f.name.equals("nothing_to_see_here")||pair&&f.desc.equals("I")&&f.name.matches("[A-Za-z0-9]{10}"))noiseFields.add(key(cn.name,f.name,f.desc));
   for(MethodNode m:cn.methods)if(m.desc.equals("([BI)Ljava/lang/String;")||m.desc.equals("([B[BI)Ljava/lang/String;"))for(AbstractInsnNode n:m.instructions)if(n instanceof FieldInsnNode f&&f.owner.equals(cn.name)&&(f.desc.equals("[B")||f.desc.equals("Ljava/lang/String;"))&&f.name.matches("[a-z]{10}"))noiseFields.add(key(f.owner,f.name,f.desc));
  }
  for(ClassNode cn:classes.values())for(MethodNode m:cn.methods){List<AbstractInsnNode> is=new ArrayList<>();for(AbstractInsnNode n:m.instructions)if(n.getOpcode()>=0)is.add(n);
   if(m.desc.equals("(II)I")&&is.size()==4&&is.get(2).getOpcode()==IXOR)xorMethods.add(key(cn.name,m.name,m.desc));
   if(m.desc.equals("([BI)Ljava/lang/String;")||m.desc.equals("([B[BI)Ljava/lang/String;"))decryptMethods.add(key(cn.name,m.name,m.desc));
   if(m.desc.equals("()[B")&&is.size()>=3){try{Frame<BasicValue>[] fs=analyze(cn,m);for(int i=0;i<fs.length;i++)if(m.instructions.get(i).getOpcode()==ARETURN&&fs[i]!=null&&c(fs[i].getStack(0)) instanceof Bytes ar&&ar.set.cardinality()==ar.data.length)arrayMethods.put(key(cn.name,m.name,m.desc),ar.data.clone());}catch(Exception ignored){}}
  }
  for(ClassNode cn:classes.values())for(MethodNode m:cn.methods)if(m.name.equals("<clinit>"))for(AbstractInsnNode n:m.instructions)if(n instanceof FieldInsnNode f&&n.getOpcode()==PUTSTATIC&&f.desc.equals("[B")&&prev(n) instanceof MethodInsnNode call){byte[] ar=arrayMethods.get(key(call.owner,call.name,call.desc));if(ar!=null)keys.put(cn.name,ar);}
  for(ClassNode cn:classes.values())for(MethodNode m:cn.methods)if(m.name.equals("<clinit>"))for(AbstractInsnNode n:m.instructions)if(n instanceof MethodInsnNode call&&call.owner.equals("java/nio/ByteBuffer")&&call.name.equals("wrap")&&prev(n) instanceof MethodInsnNode factory){byte[] ar=arrayMethods.get(key(factory.owner,factory.name,factory.desc));if(ar!=null)stringBlobs.put(cn.name,ar);}
  for(int pass=0;pass<3;pass++)for(ClassNode cn:classes.values())for(MethodNode m:cn.methods)if(m.name.equals("<clinit>")||m.name.equals("<init>")){
   try{Frame<BasicValue>[] fs=analyze(cn,m);for(int i=0;i<fs.length;i++){AbstractInsnNode n=m.instructions.get(i);if(n instanceof FieldInsnNode f&&(n.getOpcode()==PUTSTATIC||n.getOpcode()==PUTFIELD)&&f.desc.equals("I")&&noiseFields.contains(key(f.owner,f.name,f.desc))&&fs[i]!=null){Object v=c(fs[i].getStack(fs[i].getStackSize()-1));if(v instanceof Integer)fields.put(key(f.owner,f.name,f.desc),v);}}}catch(Exception ignored){}
  }
 }
 static void stripNoise(){
  for(ClassNode cn:classes.values())for(MethodNode m:cn.methods)if(m.name.equals("<clinit>")){
   for(AbstractInsnNode n:m.instructions.toArray())if(n instanceof FieldInsnNode f&&f.name.equals("nothing_to_see_here")){
    if(n.getOpcode()==GETSTATIC){AbstractInsnNode a=next(n),b=next(a),d=next(b);if(icon(a)!=null&&b instanceof LdcInsnNode&&d.getOpcode()==AASTORE){m.instructions.remove(n);m.instructions.remove(a);m.instructions.remove(b);m.instructions.remove(d);}}
    else if(n.getOpcode()==PUTSTATIC){AbstractInsnNode a=prev(n),b=prev(a);if(a.getOpcode()==ANEWARRAY&&icon(b)!=null){m.instructions.remove(n);m.instructions.remove(a);m.instructions.remove(b);}}
   }
  }
  for(int pass=0;pass<5;pass++){
   Set<String> methodRefs=new HashSet<>(),fieldReads=new HashSet<>();
   for(ClassNode cn:classes.values())for(MethodNode m:cn.methods)for(AbstractInsnNode n:m.instructions){if(n instanceof MethodInsnNode call)methodRefs.add(key(call.owner,call.name,call.desc));if(n instanceof FieldInsnNode f&&(n.getOpcode()==GETSTATIC||n.getOpcode()==GETFIELD))fieldReads.add(key(f.owner,f.name,f.desc));if(n instanceof InvokeDynamicInsnNode indy)for(Object a:indy.bsmArgs)if(a instanceof Handle h)methodRefs.add(key(h.getOwner(),h.getName(),h.getDesc()));}
   Set<String> removedFields=new HashSet<>();
   for(ClassNode cn:classes.values()){
    cn.methods.removeIf(m->{String k=key(cn.name,m.name,m.desc);return !methodRefs.contains(k)&&(decryptMethods.contains(k)||xorMethods.contains(k)||arrayMethods.containsKey(k));});
    cn.fields.removeIf(f->{String k=key(cn.name,f.name,f.desc);if(noiseFields.contains(k)&&!fieldReads.contains(k)){removedFields.add(k);return true;}return false;});
   }
   for(ClassNode cn:classes.values())for(MethodNode m:cn.methods){for(AbstractInsnNode n:m.instructions.toArray())if(n instanceof FieldInsnNode f&&removedFields.contains(key(f.owner,f.name,f.desc))){InsnList p=new InsnList();p.add(new InsnNode(Type.getType(f.desc).getSize()==2?POP2:POP));if(n.getOpcode()==PUTFIELD)p.add(new InsnNode(POP));m.instructions.insertBefore(n,p);m.instructions.remove(n);}peephole(m);}
  }
 }
 static void metadataAndEnums(){for(ClassNode cn:classes.values()){
  if(cn.superName.equals("java/lang/Record")){cn.access|=ACC_RECORD;cn.recordComponents=new ArrayList<>();for(FieldNode f:cn.fields)if((f.access&ACC_STATIC)==0&&(f.access&ACC_FINAL)!=0)cn.recordComponents.add(new RecordComponentNode(f.name,f.desc,f.signature));}
  for(MethodNode m:cn.methods){
   m.maxStack+=4;
   for(AbstractInsnNode n:m.instructions.toArray())if(n instanceof LdcInsnNode l&&l.cst instanceof Integer v&&v>=-1&&v<=5)m.instructions.set(n,new InsnNode(ICONST_0+v));
   if((cn.access&ACC_ENUM)!=0&&m.name.equals("<clinit>"))for(AbstractInsnNode n:m.instructions.toArray())if(n.getOpcode()==NEW&&n.getNext() instanceof VarInsnNode st&&st.getOpcode()==ASTORE&&st.getNext() instanceof VarInsnNode ld&&ld.getOpcode()==ALOAD&&ld.var==st.var){AbstractInsnNode x=ld.getNext();int steps=0;while(x!=null&&steps++<30){if(x instanceof MethodInsnNode call&&call.name.equals("<init>")&&call.owner.equals(cn.name)){AbstractInsnNode tail=x.getNext();if(tail instanceof VarInsnNode use&&use.var==st.var&&use.getOpcode()==ALOAD&&tail.getNext() instanceof FieldInsnNode f&&f.getOpcode()==PUTSTATIC){m.instructions.remove(st);m.instructions.set(ld,new InsnNode(DUP));m.instructions.remove(tail);}break;}x=x.getNext();}}
  }
 }}
 public static void main(String[] args)throws Exception{
  try(JarFile jar=new JarFile(args[0])){for(JarEntry e:Collections.list(jar.entries()))if(e.getName().endsWith(".class")){ClassNode cn=new ClassNode();new ClassReader(jar.getInputStream(e).readAllBytes()).accept(cn,ClassReader.SKIP_FRAMES);classes.put(cn.name,cn);}}
  for(ClassNode cn:classes.values())for(MethodNode m:cn.methods)directThrows(m);
  discover();System.out.println("keys="+keys.size()+" fields="+fields.size()+" xor="+xorMethods.size());
  for(ClassNode cn:classes.values()){int fail=0;for(MethodNode m:cn.methods)if(m.instructions.size()>0){try{for(int p=0;p<12;p++)if(!clean(cn,m))break;}catch(Exception ex){fail++;System.out.println("FAIL "+cn.name+"."+m.name+m.desc+" "+ex);}}System.out.println("CLEAN "+cn.name+" failures="+fail);}
  stripNoise();
  for(ClassNode cn:classes.values())for(MethodNode m:cn.methods)if(m.instructions.size()>0)for(int p=0;p<4;p++){try{if(!clean(cn,m))break;}catch(Exception ex){System.out.println("POST_FAIL "+cn.name+"."+m.name+" "+ex);break;}}
  stripNoise();
  metadataAndEnums();
  for(ClassNode cn:classes.values())for(MethodNode m:cn.methods)if(m.instructions.size()>0)try{new Analyzer<BasicValue>(new BasicVerifier()).analyze(cn.name,m);}catch(Exception ex){System.out.println("VERIFY_FAIL "+cn.name+"."+m.name+m.desc+" "+ex);}
  try(JarFile src=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Path.of(args[1])))){
   for(JarEntry en:Collections.list(src.entries())){if(en.isDirectory())continue;byte[] b;if(en.getName().endsWith(".class")){ClassNode cn=classes.get(en.getName().substring(0,en.getName().length()-6));ClassWriter cw=new ClassWriter(0);cn.accept(cw);b=cw.toByteArray();}else b=src.getInputStream(en).readAllBytes();out.putNextEntry(new JarEntry(en.getName()));out.write(b);out.closeEntry();}
  }
  System.out.println("folded="+folded+" branches="+branches+" dead="+dead+" exceptionEdges="+throwsFixed+" strings="+strings);
 }
}


