#!/usr/bin/env bash
set -euo pipefail

USUARIOS_URL="${USUARIOS_URL:-http://localhost:8081}"
CATALOGO_URL="${CATALOGO_URL:-http://localhost:8082}"
ROOT="$(cd "$(dirname "$0")/.." && pwd)"

python3 - "$USUARIOS_URL" "$CATALOGO_URL" "$ROOT/scripts/seed" <<'PY'
import json, sys, urllib.error, urllib.request

usuarios_url, catalogo_url, seed_dir = sys.argv[1:4]

def post(url, item, label):
    data = json.dumps(item).encode()
    req = urllib.request.Request(url, data=data, method="POST",
                                 headers={"Content-Type": "application/json"})
    try:
        with urllib.request.urlopen(req, timeout=8) as resp:
            print(f"  HTTP {resp.status}  {label}")
    except urllib.error.HTTPError as e:
        print(f"  HTTP {e.code}  {label}")
        if e.code not in (201, 409):
            raise SystemExit(e.read().decode())

print(f"Seed usuarios → {usuarios_url}")
for item in json.load(open(f"{seed_dir}/usuarios.json")):
    post(f"{usuarios_url}/api/usuarios", item, item["id"])

print(f"Seed productos → {catalogo_url}")
for item in json.load(open(f"{seed_dir}/productos.json")):
    post(f"{catalogo_url}/api/productos", item, f"{item['producto_id']} stock={item['stock']}")
PY

echo
echo "Seed listo:"
echo "  usr_demo"
echo "  prod_feliz   stock 5  → caso feliz"
echo "  prod_comp    stock 2  → compensación (forzar_fallo_pago)"
echo "  prod_agotado stock 0  → 409 sin stock"
echo "  usr_no_existe         → 404 (no se siembra a propósito)"
