import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import org.objectweb.asm.*;
public class Reframe {
 public static void main(String[] a)throws Exception{try(JarFile in=new JarFile(a[0]);JarOutputStream out=new JarOutputStream(Files.newOutputStream(Path.of(a[1])))){for(JarEntry e:Collections.list(in.entries())){byte[] b=in.getInputStream(e).readAllBytes();if(e.getName().endsWith(".class")){ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_FRAMES);new ClassReader(b).accept(cw,ClassReader.SKIP_FRAMES);b=cw.toByteArray();}out.putNextEntry(new JarEntry(e.getName()));out.write(b);out.closeEntry();}}}
}
