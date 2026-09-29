import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.commons.*;
import java.util.*;
import java.util.jar.*;
import java.nio.file.*;
import java.io.*;

public class Rename implements Opcodes {
 static Map<String,ClassNode> classes=new LinkedHashMap<>();
 static Map<String,String> names=new LinkedHashMap<>(),methods=new LinkedHashMap<>(),fields=new LinkedHashMap<>(),reasons=new HashMap<>();
 static String key(String c,String n,String d){return c+"\t"+n+"\t"+d;}
 static boolean shortName(String n){return n.matches("[a-zA-Z]{1,2}");}
 static String cap(String x){return x.isEmpty()?x:Character.toUpperCase(x.charAt(0))+x.substring(1);}
 static String camel(String s){StringBuilder b=new StringBuilder();for(String part:s.split("[^a-zA-Z0-9]+"))if(!part.isEmpty())b.append(cap(part));return b.toString();}
 static List<AbstractInsnNode> real(MethodNode m){List<AbstractInsnNode> r=new ArrayList<>();for(AbstractInsnNode n:m.instructions)if(n.getOpcode()>=0)r.add(n);return r;}
 static String findMethod(String owner,String name,String desc){String k=key(owner,name,desc);if(methods.containsKey(k))return methods.get(k);ClassNode c=classes.get(owner);if(c!=null){String s=findMethod(c.superName,name,desc);if(s!=null)return s;for(String in:c.interfaces){s=findMethod(in,name,desc);if(s!=null)return s;}}return null;}
 static String findField(String owner,String name,String desc){String k=key(owner,name,desc);if(fields.containsKey(k))return fields.get(k);ClassNode c=classes.get(owner);return c==null?null:findField(c.superName,name,desc);}
 static String typeName(String d){Type t=Type.getType(d);if(t.getSort()==Type.ARRAY)return typeName(t.getElementType().getDescriptor())+"Array";if(t.getSort()==Type.OBJECT){String s=t.getInternalName();s=names.getOrDefault(s,s);return s.substring(Math.max(s.lastIndexOf('/'),s.lastIndexOf('$'))+1);}return t.getClassName();}
 static void put(Map<String,String> map,String key,String name,String reason){map.put(key,name);reasons.put(key,reason);}
 public static void main(String[] args)throws Exception{
  try(JarFile jar=new JarFile(args[0])){for(JarEntry e:Collections.list(jar.entries()))if(e.getName().endsWith(".class")){ClassNode c=new ClassNode();new ClassReader(jar.getInputStream(e).readAllBytes()).accept(c,0);classes.put(c.name,c);}}
  for(String line:Files.readAllLines(Path.of(args[2]))){if(line.isBlank()||line.startsWith("#"))continue;String[] p=line.split("\t");if(p[0].equals("C"))names.put(p[1],p[2]);else put(p[0].equals("F")?fields:methods,key(p[1],p[2],p[3]),p[4],"manual inference from implementation");}
  for(ClassNode c:classes.values()){
   if((c.access&ACC_ENUM)!=0)for(MethodNode m:c.methods)if(m.name.equals("<clinit>")){String last=null;for(AbstractInsnNode n:m.instructions){if(n instanceof LdcInsnNode l&&l.cst instanceof String s)last=s;if(n instanceof FieldInsnNode f&&n.getOpcode()==PUTSTATIC&&f.desc.equals("L"+c.name+";")&&last!=null&&last.matches("[A-Za-z_][A-Za-z_0-9]*"))put(fields,key(c.name,f.name,f.desc),last,"recovered enum name literal");}}
   for(MethodNode m:c.methods)if(!shortName(m.name)&&(m.name.startsWith("get")||m.name.startsWith("is"))){List<AbstractInsnNode> ns=real(m);if(ns.size()<=3)for(AbstractInsnNode n:ns)if(n instanceof FieldInsnNode f&&f.owner.equals(c.name)){String s=m.name.substring(m.name.startsWith("is")?2:3);if(!s.isEmpty())put(fields,key(c.name,f.name,f.desc),Character.toLowerCase(s.charAt(0))+s.substring(1),"surviving getter name");}}
   for(FieldNode f:c.fields)if(shortName(f.name)&&!fields.containsKey(key(c.name,f.name,f.desc))){String t=typeName(f.desc),n;if(f.desc.equals("L"+c.name+";")&&(f.access&ACC_STATIC)!=0)n="instance";else if(f.desc.equals("Lnet/minecraft/client/Minecraft;"))n="minecraft";else if(f.desc.equals("Lorg/slf4j/Logger;"))n="LOGGER";else if(f.desc.equals("Ljava/util/Random;"))n="random";else if(t.equals("boolean"))n="flag"+cap(f.name);else n=Character.toLowerCase(t.charAt(0))+t.substring(1)+cap(f.name);put(fields,key(c.name,f.name,f.desc),n,"descriptive type-based alias; purpose not established");}
  }
  for(ClassNode c:classes.values())for(MethodNode m:c.methods)if(shortName(m.name)&&!methods.containsKey(key(c.name,m.name,m.desc))){
   String inferred=null,reason="stable alias; purpose not established";Type ret=Type.getReturnType(m.desc);List<AbstractInsnNode> ns=real(m);
   if(ns.size()<=3&&Type.getArgumentTypes(m.desc).length==0)for(AbstractInsnNode n:ns)if(n instanceof FieldInsnNode f&&(n.getOpcode()==GETFIELD||n.getOpcode()==GETSTATIC)){String field=findField(f.owner,f.name,f.desc);if(field!=null){inferred=(ret.getSort()==Type.BOOLEAN?"is":"get")+cap(field);reason="field accessor";}}
   if(inferred==null&&Type.getArgumentTypes(m.desc).length==0&&ret.getSort()!=Type.VOID){String first=null;boolean setting=false;for(AbstractInsnNode n:ns){if(first==null&&n instanceof LdcInsnNode l&&l.cst instanceof String s)first=s;if(n instanceof MethodInsnNode call&&call.name.equals("a")&&call.desc.equals("(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;"))setting=true;}if(setting&&first!=null&&!camel(first).isEmpty()&&first.length()<55){inferred=(ret.getSort()==Type.BOOLEAN?"is":"get")+camel(first)+"Setting";reason="decoded configuration key";}}
   if(inferred==null)inferred=(Character.isUpperCase(m.name.charAt(0))?"methodUpper":"method")+cap(m.name);
   String proposed=inferred;int j=2;while(methodCollision(c,m,inferred))inferred=proposed+j++;
   put(methods,key(c.name,m.name,m.desc),inferred,reason);
  }
  for(ClassNode c:classes.values()){Set<String> used=new HashSet<>();for(FieldNode f:c.fields){String k=key(c.name,f.name,f.desc),n=fields.getOrDefault(k,f.name);if(!used.add(n)){n=n+"_"+f.name;int i=2;while(!used.add(n))n=n+i++;fields.put(k,n);}}}
  // Keep explicit parent mappings consistent for inherited calls and overrides.
  for(int p=0;p<3;p++)for(ClassNode c:classes.values())for(MethodNode m:c.methods)if(shortName(m.name)&&(m.access&(ACC_STATIC|ACC_PRIVATE))==0){String parent=findMethod(c.superName,m.name,m.desc);if(parent!=null)methods.put(key(c.name,m.name,m.desc),parent);}
  Remapper remap=new Remapper(ASM9){public String map(String s){return names.getOrDefault(s,s);}public String mapFieldName(String o,String n,String d){String s=findField(o,n,d);return s==null?n:s;}public String mapMethodName(String o,String n,String d){String s=findMethod(o,n,d);return s==null?n:s;}public String mapRecordComponentName(String o,String n,String d){String s=findField(o,n,d);return s==null?n:s;}};
  try(JarFile input=new JarFile(args[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Path.of(args[1])))){
   for(JarEntry en:Collections.list(input.entries())){if(en.isDirectory())continue;String name=en.getName();byte[] b;if(name.endsWith(".class")){ClassNode c=classes.get(name.substring(0,name.length()-6));ClassWriter w=new ClassWriter(0);c.accept(new ClassRemapper(w,remap));b=w.toByteArray();name=remap.map(c.name)+".class";}else b=input.getInputStream(en).readAllBytes();out.putNextEntry(new JarEntry(name));out.write(b);out.closeEntry();}
  }
  Set<String> actualFields=new HashSet<>(),actualMethods=new HashSet<>();for(ClassNode c:classes.values()){for(FieldNode f:c.fields)actualFields.add(key(c.name,f.name,f.desc));for(MethodNode m:c.methods)actualMethods.add(key(c.name,m.name,m.desc));}
  List<String> lines=new ArrayList<>();lines.add("kind\towner\toriginal\tdescriptor\treadable\tbasis");for(var en:names.entrySet())lines.add("class\t"+en.getKey()+"\t"+en.getKey()+"\t\t"+en.getValue()+"\tmanual inference from implementation");for(var en:fields.entrySet())if(actualFields.contains(en.getKey()))lines.add("field\t"+en.getKey()+"\t"+en.getValue()+"\t"+reasons.get(en.getKey()));for(var en:methods.entrySet())if(actualMethods.contains(en.getKey()))lines.add("method\t"+en.getKey()+"\t"+en.getValue()+"\t"+reasons.get(en.getKey()));Files.write(Path.of(args[3]),lines);System.out.println("classes="+names.size()+" fields="+fields.keySet().stream().filter(actualFields::contains).count()+" methods="+methods.keySet().stream().filter(actualMethods::contains).count());
 }
 static boolean methodCollision(ClassNode c,MethodNode m,String name){String args=m.desc.substring(0,m.desc.indexOf(')')+1);for(MethodNode other:c.methods)if(other!=m&&other.desc.startsWith(args)&&name.equals(methods.getOrDefault(key(c.name,other.name,other.desc),other.name)))return true;return false;}
}
