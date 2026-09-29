"""Reproduce the static recovery for this exact clarity.jar. Requires Python 3 and JDK 25.
Downloads pinned tooling. Does not execute or classload any target class.
"""
from pathlib import Path
import argparse, hashlib, json, os, shutil, subprocess, urllib.request, zipfile

parser=argparse.ArgumentParser(description=__doc__)
parser.add_argument('jar',type=Path)
parser.add_argument('output',type=Path,help='A NEW output directory')
args=parser.parse_args()
expected='b3ee22c8321677289ba6db4e145eb5744623a5574a9bc4a66b1aa8ddfc1c1cc3'
actual=hashlib.sha256(args.jar.read_bytes()).hexdigest()
if actual!=expected: raise SystemExit('Input hash differs; this transformer is tailored to the supplied JAR.')
base=Path(__file__).resolve().parent
target=args.output.resolve()
target.mkdir(parents=True,exist_ok=False)
scratch=target/'intermediates'; scratch.mkdir()
deps=scratch/'dependencies'; deps.mkdir()
classes=scratch/'tool-classes'; classes.mkdir()
def run(argv,log):
    print(log,flush=True)
    with (target/log).open('w',encoding='utf-8') as out:
        subprocess.run([str(a) for a in argv],stdout=out,stderr=subprocess.STDOUT,check=True)
for tool in json.loads((base/'toolchain.json').read_text()):
    p=deps/tool['filename']
    urllib.request.urlretrieve(tool['url'],p)
    if hashlib.sha256(p.read_bytes()).hexdigest()!=tool['sha256']: raise SystemExit('Tool checksum mismatch: '+tool['filename'])
cp=os.pathsep.join([str(classes),str(deps/'*')])
run(['javac','-cp',str(deps/'*'),'-d',classes,*[base/n for n in ['Clean.java','Unhash.java','Rename.java','Audit.java','ParseSources.java']]],'compile-tools.log')
run(['java','-Xmx3g','-cp',cp,'Clean',args.jar.resolve(),scratch/'cleaned.jar'],'clean.log')
run(['java','-cp',cp,'Unhash',scratch/'cleaned.jar',scratch/'unhashed.jar',target/'switches.tsv'],'switches.log')
run(['java','-cp',cp,'Rename',scratch/'unhashed.jar',scratch/'readable.jar',base/'manual-mappings.tsv',target/'mappings.tsv'],'rename.log')
decompiled=scratch/'decompiled'; decompiled.mkdir()
run(['java','-Xmx3g','-jar',deps/'vineflower.jar','--folder','--thread-count=4','--max-time-per-method=8','--remove-synthetic=0','--variable-renaming=tiny','--rename-parameters','--log-level=WARN',scratch/'readable.jar',decompiled],'decompile.log')
run([os.sys.executable,base/'repair_font.py',decompiled],'font-repair.log')
run([os.sys.executable,base/'repair_monitors.py',decompiled],'monitor-repairs.tsv')
for p in decompiled.rglob('*.java'):
    dest=target/'src/main/java'/p.relative_to(decompiled)
    dest.parent.mkdir(parents=True,exist_ok=True); shutil.copy2(p,dest)
with zipfile.ZipFile(args.jar) as jar:
    for entry in jar.infolist():
        if entry.is_dir() or entry.filename.endswith('.class'): continue
        dest=target/'src/main/resources'/entry.filename
        if not dest.resolve().is_relative_to(target): raise ValueError('Unsafe archive path')
        dest.parent.mkdir(parents=True,exist_ok=True); dest.write_bytes(jar.read(entry))
run(['java','-cp',cp,'Audit',scratch/'readable.jar',target/'method-audit.tsv'],'audit.log')
run(['java','-cp',cp,'ParseSources',target/'src/main/java'],'parse.log')
if 'errors=0 hashCalls=0 decoderMethods=0' not in (target/'audit.log').read_text(): raise SystemExit('Inspect audit.log: validation failed')
if 'Parsed files=217 syntax errors=0' not in (target/'parse.log').read_text(): raise SystemExit('Inspect parse.log: validation failed')
print('Recovered primary source and resources. This is not a verified buildable project.')
