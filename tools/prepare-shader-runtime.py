"""Flatten local Sodium bootstrap/nested packages for ModDev's development classloader only.
The result stays under ignored build/ and must never be distributed.
Usage: python tools/prepare-shader-runtime.py /path/to/sodium-neoforge.jar
"""
import io,sys,zipfile
from pathlib import Path
root=Path(__file__).resolve().parents[1]/'build/shader-runtime'
root.mkdir(parents=True,exist_ok=True)
data={}
with zipfile.ZipFile(sys.argv[1]) as source:
 for name in source.namelist():
  if name.startswith('META-INF/jarjar/') and name.endswith('.jar'):
   if Path(name).name.startswith('fabric-'):(root/Path(name).name).write_bytes(source.read(name))
   else:
    with zipfile.ZipFile(io.BytesIO(source.read(name))) as nested:
     for part in nested.namelist():
      if not part.endswith('/') and not part.startswith('META-INF/jarjar/'):data[part]=nested.read(part)
 for name in source.namelist():
  if not name.endswith('/') and not name.startswith('META-INF/jarjar/'):data.setdefault(name,source.read(name))
with zipfile.ZipFile(root/'sodium-dev-combined.jar','w',zipfile.ZIP_DEFLATED) as output:
 for name,content in data.items():output.writestr(name,content)
print('Prepared local development runtime; third-party jars are not bundled in the mod.')
