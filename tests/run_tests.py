"""Execute only our synthetic fixtures, never the supplied mod. Requires JDK 25."""
from pathlib import Path
import argparse, importlib.util, os, subprocess
p=argparse.ArgumentParser(description=__doc__)
p.add_argument('dependencies',type=Path)
p.add_argument('output',type=Path)
a=p.parse_args()
base=Path(__file__).resolve().parent
tools=base.parent/'tools'
out=a.output.resolve();out.mkdir(parents=True,exist_ok=False)
deps=a.dependencies.resolve()
classes=out/'classes';classes.mkdir()
toolclasses=out/'tools';toolclasses.mkdir()
cp=os.pathsep.join([str(toolclasses),str(deps/'*')])
def run(*args):
    return subprocess.check_output([str(x) for x in args],stderr=subprocess.STDOUT,text=True)
run('javac','-cp',deps/'*','-d',toolclasses,tools/'Clean.java',tools/'Unhash.java',base/'Reframe.java')
run('javac','-g:none','-d',classes,base/'Fixture.java',base/'bijwajihqirrnqvj/itjkfrmknyehiltk.java')
run('jar','cf',out/'fixture.jar','-C',classes,'.')
before=run('java','-Xverify:all','-cp',out/'fixture.jar','Fixture').strip()
(out/'clean.log').write_text(run('java','-cp',cp,'Clean',out/'fixture.jar',out/'clean.jar'))
(out/'switches.log').write_text(run('java','-cp',cp,'Unhash',out/'clean.jar',out/'unhash.jar',out/'switches.tsv'))
run('java','-cp',os.pathsep.join([cp,str(classes)]),'Reframe',out/'unhash.jar',out/'reframed.jar')
after=run('java','-Xverify:all','-cp',out/'reframed.jar','Fixture').strip()
assert before==after=='-1153849246760534588',(before,after)
print('Transformation fixture digests match:',before)

fixture='''public class MonitorFixture {
   static final Object lock = new Object();
   static int check(int x) {
      Object monitor = lock;
      synchronized (monitor){} // $VF: monitorenter
      int answer;
      try {
         if (!Thread.holdsLock(lock)) throw new AssertionError("missing lock");
         if (x < 0) {
            // $VF: monitorexit
            return -1;
         }
         if (x == 0) throw new IllegalStateException();
         answer = x + 1;
         // $VF: monitorexit
      } catch (Throwable throwable) {
         // $VF: monitorexit
         throw throwable;
      }
      if (Thread.holdsLock(lock)) throw new AssertionError("unreleased lock");
      return answer;
   }
   public static void main(String[] args) {
      if (check(4) != 5 || check(-4) != -1) throw new AssertionError();
      try { check(0); throw new AssertionError(); } catch (IllegalStateException expected) {}
      if (Thread.holdsLock(lock)) throw new AssertionError("exception leaked lock");
      System.out.println("monitor reconstruction passed");
   }
}
'''
source=out/'monitor-source';source.mkdir()
(source/'MonitorFixture.java').write_text(fixture)
spec=importlib.util.spec_from_file_location('repair_monitors',tools/'repair_monitors.py')
module=importlib.util.module_from_spec(spec);spec.loader.exec_module(module)
repairs=module.repair(source)
assert repairs==['MonitorFixture.java\t1'],repairs
run('javac','-d',classes,source/'MonitorFixture.java')
print(run('java','-Xverify:all','-cp',classes,'MonitorFixture').strip())
