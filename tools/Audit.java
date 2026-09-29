import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.*;
import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import java.io.*;
public class Audit implements Opcodes {
 public static void main(String[] args)throws Exception{
  int classes=0,methods=0,fields=0,insns=0,errors=0,hashCalls=0,decoders=0;
  List<String> inventory=new ArrayList<>();inventory.add("class\tmethod\tdescriptor\tinstructions\tverification");
  try(JarFile jar=new JarFile(args[0])){for(JarEntry e:Collections.list(jar.entries()))if(e.getName().endsWith(".class")){ClassNode c=new ClassNode();new ClassReader(jar.getInputStream(e).readAllBytes()).accept(c,ClassReader.SKIP_FRAMES);classes++;fields+=c.fields.size();Set<String> fieldsSeen=new HashSet<>(),methodsSeen=new HashSet<>();
   for(FieldNode f:c.fields)if(!fieldsSeen.add(f.name)){System.out.println("DUPLICATE_FIELD "+c.name+" "+f.name);errors++;}
   for(MethodNode m:c.methods){methods++;String sig=m.name+m.desc.substring(0,m.desc.indexOf(')')+1);if(!methodsSeen.add(sig)&&(m.access&ACC_BRIDGE)==0){System.out.println("RETURN_ONLY_OVERLOAD "+c.name+" "+sig);}
    int count=0;for(AbstractInsnNode n:m.instructions)if(n.getOpcode()>=0){count++;if(n instanceof MethodInsnNode call&&(call.owner.equals("bijwajihqirrnqvj/itjkfrmknyehiltk")||call.owner.equals("need/clarity/deobfuscation/SwitchHash")))hashCalls++;}
    if(m.desc.equals("([BI)Ljava/lang/String;")||m.desc.equals("([B[BI)Ljava/lang/String;"))decoders++;
    insns+=count;String status="no code";if(count>0){try{new Analyzer<BasicValue>(new BasicVerifier()).analyze(c.name,m);status="PASS";}catch(Exception ex){status="FAIL "+ex;errors++;System.out.println(c.name+" "+m.name+m.desc+" "+status);}}
    inventory.add(c.name+"\t"+m.name+"\t"+m.desc+"\t"+count+"\t"+status);
   }
  }}
  if(args.length>1)Files.write(Path.of(args[1]),inventory);
  System.out.println("classes="+classes+" methods="+methods+" fields="+fields+" instructions="+insns+" errors="+errors+" hashCalls="+hashCalls+" decoderMethods="+decoders);
 }
}
