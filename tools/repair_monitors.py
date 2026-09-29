"""Reconstruct Vineflower's explicit monitor pattern. Never executes target code."""
from pathlib import Path
import re
import sys

def matching_brace(text, opening):
    # Ignore quoted literals and comments while matching Java blocks.
    token = re.compile(r'"(?:\\.|[^"\\])*"|\'(?:\\.|[^\'\\])*\'|//[^\n]*|/\*[\s\S]*?\*/|[{}]')
    depth = 0
    for m in token.finditer(text, opening):
        if m[0] == '{': depth += 1
        elif m[0] == '}':
            depth -= 1
            if depth == 0: return m.start()
    raise ValueError('Unbalanced block')

def repair(root):
    changes = []
    pattern = re.compile(r'(?m)^( +)synchronized \((\w+)\)\{\} // \$VF: monitorenter[^\n]*\n')
    for path in sorted(Path(root).rglob('*.java')):
        text = path.read_text(encoding='utf-8')
        count = 0
        while matches := list(pattern.finditer(text)):
            m = matches[-1]
            following = re.match(r'\s*(?:(?:[\w.$<>?,\[\]]+\s+\w+;\s*)*)try \{', text[m.end():])
            if not following: raise ValueError(f'Unexpected monitor prelude: {path}:{m.start()}')
            opening = m.end() + following.end() - 1
            try_start = text.rfind('try', m.end(), opening)
            closing = matching_brace(text, opening)
            catch = re.match(r' catch \(Throwable (\w+)\) \{\s*// \$VF: monitorexit\s*throw \1;\s*\}', text[closing+1:])
            if not catch: raise ValueError(f'Unexpected monitor handler: {path}:{closing}')
            body = text[opening+1:closing]
            if '// $VF: monitorexit' not in body: raise ValueError('Missing normal monitor exit')
            prelude = text[m.end():try_start].strip()
            replacement = (m[1] + prelude + '\n\n' if prelude else '')
            replacement += m[1] + 'synchronized (' + m[2] + ') {' + body + '}'
            text = text[:m.start()] + replacement + text[closing+1+catch.end():]
            count += 1
        if count:
            text = re.sub(r'(?m)^\s*// \$VF: monitorexit[^\n]*\n', '\n', text)
            text = re.sub(r'(?m)^ +// \$VF: Could not create synchronized statement[^\n]*\n +// Please report this[^\n]*\n', '', text)
            if 'monitorenter' in text or 'monitorexit' in text: raise ValueError('Unrepaired monitor')
            path.write_text(text, encoding='utf-8')
            changes.append(f'{path.relative_to(root).as_posix()}\t{count}')
    return changes

if __name__ == '__main__':
    print('file\treconstructed_blocks')
    print('\n'.join(repair(Path(sys.argv[1]))))
