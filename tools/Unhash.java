import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.*;
import java.util.*;
import java.util.jar.*;
import java.nio.file.*;
public class Unhash implements Opcodes {
 static Map<String,ClassNode> classes=new LinkedHashMap<>();
 static MethodNode method;static Frame<SourceValue>[] frames;
 static int hash(int x){return x==0?0:((x*31)>>>4)%x^(x>>>16);}
 static AbstractInsnNode next(AbstractInsnNode n){do{n=n.getNext();}while(n!=null&&n.getOpcode()<0);return n;}
 static SourceValue stack(AbstractInsnNode n,int fromTop){Frame<SourceValue> f=frames[method.instructions.indexOf(n)];return f==null?null:f.getStack(f.getStackSize()-1-fromTop);}
 static Set<Integer> range(int lo,int hi){Set<Integer> r=new TreeSet<>();for(int i=lo;i<=hi;i++)r.add(i);return r;}
 static Set<Integer> values(SourceValue v,Set<AbstractInsnNode> seen){if(v==null||v.insns.isEmpty())return null;Set<Integer> result=new TreeSet<>();for(AbstractInsnNode n:v.insns){if(!seen.add(n))return null;Set<Integer> sub=one(n,seen);seen.remove(n);if(sub==null)return null;result.addAll(sub);if(result.size()>65536)return null;}return result;}
 static Set<Integer> one(AbstractInsnNode n,Set<AbstractInsnNode> seen){int o=n.getOpcode();Integer i=Clean.icon(n);if(i!=null)return Set.of(i);
  if(n instanceof VarInsnNode v&&o==ILOAD){Frame<SourceValue> f=frames[method.instructions.indexOf(n)];return f==null?null:values(f.getLocal(v.var),seen);}
  if(o==ISTORE)return values(stack(n,0),seen);
  if(o==I2B)return range(-128,127);if(o==I2C)return range(0,65535);if(o==I2S)return range(-32768,32767);
  if(n instanceof MethodInsnNode call&&Type.getReturnType(call.desc).getSort()==Type.CHAR)return range(0,65535);
  if(o==IAND){Set<Integer> mask=values(stack(n,0),seen);if(mask!=null&&mask.size()==1){int x=mask.iterator().next();if(x>=0&&x<1024&&(x&(x+1))==0)return range(0,x);}}
  if(n instanceof MethodInsnNode m&&m.name.equals("ordinal")&&m.desc.equals("()I")){ClassNode c=classes.get(m.owner);if(c!=null&&(c.access&ACC_ENUM)!=0){int count=(int)c.fields.stream().filter(f->(f.access&ACC_ENUM)!=0).count();Set<Integer> r=new TreeSet<>();for(int a=0;a<count;a++)r.add(a);return r;}}
  if(o==IREM){Set<Integer> divisor=values(stack(n,0),seen);if(divisor!=null&&divisor.size()==1){int d=divisor.iterator().next();if(d>0&&d<128){Set<Integer> r=new TreeSet<>();for(int a=1-d;a<d;a++)r.add(a);return r;}}}
  if(o==IALOAD){SourceValue arr=stack(n,1);FieldInsnNode f=findField(arr,new HashSet<>());if(f!=null){ClassNode c=classes.get(f.owner);if(c!=null&&(c.access&ACC_SYNTHETIC)!=0){int max=0;for(MethodNode m:c.methods)for(AbstractInsnNode a:m.instructions){Integer x=Clean.icon(a);if(x!=null&&x>=0&&x<256)max=Math.max(max,x);}Set<Integer> r=new TreeSet<>();for(int a=0;a<=max;a++)r.add(a);return r;}}}
  return null;
 }
 static FieldInsnNode findField(SourceValue v,Set<AbstractInsnNode> seen){if(v==null||v.insns.size()!=1)return null;AbstractInsnNode n=v.insns.iterator().next();if(!seen.add(n))return null;if(n instanceof FieldInsnNode f)return f;if(n instanceof VarInsnNode x){if(n.getOpcode()==ALOAD)return findField(frames[method.instructions.indexOf(n)].getLocal(x.var),seen);if(n.getOpcode()==ASTORE)return findField(stack(n,0),seen);}return null;}
 static AbstractInsnNode findXor(SourceValue v,Set<AbstractInsnNode> seen){if(v==null||v.insns.size()!=1)return null;AbstractInsnNode n=v.insns.iterator().next();if(!seen.add(n))return null;if(n.getOpcode()==IXOR)return n;if(n instanceof VarInsnNode x){if(n.getOpcode()==ILOAD)return findXor(frames[method.instructions.indexOf(n)].getLocal(x.var),seen);if(n.getOpcode()==ISTORE)return findXor(stack(n,0),seen);}return null;}
 static AbstractInsnNode origin(SourceValue v,Set<AbstractInsnNode> seen){if(v==null||v.insns.size()!=1)return null;AbstractInsnNode n=v.insns.iterator().next();if(!seen.add(n))return null;if(n instanceof VarInsnNode x){if(n.getOpcode()==ILOAD)return origin(frames[method.instructions.indexOf(n)].getLocal(x.var),seen);if(n.getOpcode()==ISTORE)return origin(stack(n,0),seen);}return n;}
 static Set<Integer> guardedStringDomain(SourceValue selector,LookupSwitchInsnNode sw,int salt){
  AbstractInsnNode root=origin(selector,new HashSet<>());if(!(root instanceof MethodInsnNode hashCode)||!hashCode.owner.equals("java/lang/String")||!hashCode.name.equals("hashCode"))return null;
  SourceValue receiver=stack(root,0);if(receiver.insns.size()!=1||!(receiver.insns.iterator().next() instanceof VarInsnNode original)||original.getOpcode()!=ALOAD)return null;
  int stores=0;for(AbstractInsnNode n:method.instructions)if(n instanceof VarInsnNode v&&v.var==original.var&&v.getOpcode()==ASTORE)stores++;if(stores>1)return null;
  AbstractInsnNode join=Clean.follow(sw.dflt);Set<Integer> hashes=new TreeSet<>();Map<Integer,LabelNode> targets=new HashMap<>();
  for(int i=0;i<sw.labels.size();i++){
   AbstractInsnNode a=Clean.follow(sw.labels.get(i));if(!(a instanceof VarInsnNode load)||load.getOpcode()!=ALOAD||load.var!=original.var)return null;
   AbstractInsnNode b=next(a);if(!(b instanceof LdcInsnNode literal)||!(literal.cst instanceof String str))return null;
   AbstractInsnNode cc=next(b);if(!(cc instanceof MethodInsnNode equals)||!equals.owner.equals("java/lang/String")||!equals.name.equals("equals"))return null;
   AbstractInsnNode d=next(cc);if(!(d instanceof JumpInsnNode jump)||jump.getOpcode()!=IFEQ||Clean.follow(jump.label)!=join)return null;
   AbstractInsnNode constant=next(d),store=next(constant);if(Clean.icon(constant)==null||!(store instanceof VarInsnNode st)||st.getOpcode()!=ISTORE||Clean.follow(store.getNext())!=join)return null;
   int hc=str.hashCode();if(hash(hc^salt)!=sw.keys.get(i))return null;if(targets.containsKey(hc)&&Clean.follow(targets.get(hc))!=a)return null;targets.put(hc,sw.labels.get(i));hashes.add(hc);
  }
  return hashes;
 }
 record Change(MethodInsnNode call,LookupSwitchInsnNode sw,AbstractInsnNode xor,AbstractInsnNode saltPush,int salt,Set<Integer> domain){}
 public static void main(String[] args)throws Exception{
  try(JarFile jar=new JarFile(args[0])){for(JarEntry e:Collections.list(jar.entries()))if(e.getName().endsWith(".class")){ClassNode c=new ClassNode();new ClassReader(jar.getInputStream(e).readAllBytes()).accept(c,ClassReader.SKIP_FRAMES);classes.put(c.name,c);}}
  List<String> audit=new ArrayList<>();audit.add("owner\tmethod\tdescriptor\tsalt\tstatus\tcases");int restored=0,left=0;
  for(ClassNode cn:classes.values())for(MethodNode m:cn.methods){if(m.instructions.size()==0)continue;for(AbstractInsnNode n:m.instructions.toArray())if(n instanceof TableSwitchInsnNode t){int[] ks=new int[t.labels.size()];for(int i=0;i<ks.length;i++)ks[i]=t.min+i;m.instructions.set(n,new LookupSwitchInsnNode(t.dflt,ks,t.labels.toArray(LabelNode[]::new)));}method=m;frames=new Analyzer<SourceValue>(new SourceInterpreter()).analyze(cn.name,m);List<Change> changes=new ArrayList<>();
   for(AbstractInsnNode n:m.instructions.toArray())if(n instanceof MethodInsnNode call&&call.owner.equals("bijwajihqirrnqvj/itjkfrmknyehiltk")&&call.name.equals("qfctmcaaxjcwxdcq")&&next(n) instanceof LookupSwitchInsnNode sw){
    AbstractInsnNode xor=findXor(stack(n,0),new HashSet<>());if(xor==null){left++;continue;}SourceValue top=stack(xor,0),other=stack(xor,1);Set<Integer> ss=values(top,new HashSet<>());if(ss==null||ss.size()!=1){left++;continue;}int salt=ss.iterator().next();Set<Integer> domain=values(other,new HashSet<>());AbstractInsnNode saltPush=top.insns.size()==1?top.insns.iterator().next():null;
    boolean guarded=false;if(domain==null){domain=guardedStringDomain(other,sw,salt);guarded=domain!=null;}
    if(domain!=null){changes.add(new Change(call,sw,xor,saltPush,salt,domain));restored++;audit.add(cn.name+"\t"+m.name+"\t"+m.desc+"\t"+salt+"\t"+(guarded?"guarded string selector restored":"bounded selector restored")+"\t"+(domain.size()>256?"range "+Collections.min(domain)+".."+Collections.max(domain):domain));}
    else {left++;List<String> cases=new ArrayList<>();for(MethodNode mm:cn.methods)for(AbstractInsnNode a:mm.instructions)if(a instanceof LdcInsnNode l&&l.cst instanceof String s){int h=hash(s.hashCode()^salt);if(sw.keys.contains(h)){String val=h+"="+s.replace("\t","\\t").replace("\n","\\n");if(!cases.contains(val))cases.add(val);}}audit.add(cn.name+"\t"+m.name+"\t"+m.desc+"\t"+salt+"\tunbounded string hash preserved\t"+String.join("; ",cases));}
   }
   Set<MethodInsnNode> calls=new HashSet<>();for(Change ch:changes)calls.add(ch.call);
   Set<AbstractInsnNode> removeXors=new HashSet<>();
   for(Change ch:changes){if(Clean.icon(ch.saltPush)==null)continue;AbstractInsnNode x=next(ch.xor);if(x==ch.call)removeXors.add(ch.xor);else if(x instanceof VarInsnNode st&&st.getOpcode()==ISTORE){boolean safe=true;for(AbstractInsnNode nn:m.instructions)if(nn instanceof VarInsnNode ld&&ld.getOpcode()==ILOAD&&ld.var==st.var){Frame<SourceValue> f=frames[m.instructions.indexOf(nn)];if(f!=null&&f.getLocal(ld.var).insns.contains(st)){if(f.getLocal(ld.var).insns.size()!=1||!(next(nn) instanceof MethodInsnNode cc)||!calls.contains(cc)){safe=false;break;}}}if(safe)removeXors.add(ch.xor);}}
   for(Change ch:changes){TreeMap<Integer,LabelNode> cases=new TreeMap<>();for(int v:ch.domain){int ix=ch.sw.keys.indexOf(hash(v^ch.salt));if(ix>=0)cases.put(v,ch.sw.labels.get(ix));}LookupSwitchInsnNode sw=new LookupSwitchInsnNode(ch.sw.dflt,cases.keySet().stream().mapToInt(Integer::intValue).toArray(),cases.values().toArray(LabelNode[]::new));m.instructions.set(ch.sw,sw);if(removeXors.contains(ch.xor))m.instructions.remove(ch.call);else{m.instructions.insertBefore(ch.call,new LdcInsnNode(ch.salt));m.instructions.set(ch.call,new InsnNode(IXOR));}m.maxStack+=2;}
   Set<AbstractInsnNode> removed=new HashSet<>();for(Change ch:changes)if(removeXors.contains(ch.xor)&&removed.add(ch.xor)){m.instructions.remove(ch.saltPush);m.instructions.remove(ch.xor);}
  }
  try(JarFile input=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Path.of(args[1])))){for(JarEntry en:Collections.list(input.entries())){if(en.isDirectory())continue;byte[] b;if(en.getName().endsWith(".class")){ClassNode c=classes.get(en.getName().substring(0,en.getName().length()-6));ClassWriter w=new ClassWriter(0);c.accept(w);b=w.toByteArray();for(MethodNode m:c.methods)if(m.instructions.size()>0)new Analyzer<BasicValue>(new BasicVerifier()).analyze(c.name,m);}else b=input.getInputStream(en).readAllBytes();out.putNextEntry(new JarEntry(en.getName()));out.write(b);out.closeEntry();}}
  Files.write(Path.of(args[2]),audit);System.out.println("restored="+restored+" preserved="+left);
 }
}
