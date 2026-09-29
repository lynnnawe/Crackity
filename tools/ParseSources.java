import javax.tools.*;
import com.sun.source.util.*;
import java.nio.file.*;
import java.util.*;
public class ParseSources {
 public static void main(String[] args)throws Exception{
  JavaCompiler compiler=ToolProvider.getSystemJavaCompiler();DiagnosticCollector<JavaFileObject> diagnostics=new DiagnosticCollector<>();
  try(var fm=compiler.getStandardFileManager(diagnostics,null,java.nio.charset.StandardCharsets.UTF_8)){
   List<Path> paths;try(var walk=Files.walk(Path.of(args[0]))){paths=walk.filter(p->p.toString().endsWith(".java")).toList();}
   JavacTask task=(JavacTask)compiler.getTask(null,fm,diagnostics,List.of("-proc:none","--enable-preview","--source","25"),null,fm.getJavaFileObjectsFromPaths(paths));
   int count=0;for(var tree:task.parse())count++;
   for(var d:diagnostics.getDiagnostics())if(d.getKind()==Diagnostic.Kind.ERROR)System.out.println(d.getSource().getName()+":"+d.getLineNumber()+": "+d.getMessage(Locale.ROOT));
   System.out.println("Parsed files="+count+" syntax errors="+diagnostics.getDiagnostics().stream().filter(d->d.getKind()==Diagnostic.Kind.ERROR).count());
  }
 }
}
