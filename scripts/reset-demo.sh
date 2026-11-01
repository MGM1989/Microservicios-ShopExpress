#!/usr/bin/env bash
# Reinicia datos de la demo (Dynamo in-memory + pedidos Mongo) y carga el seed.
# No reconstruye imágenes. Para código nuevo: docker compose -f docker-compose.app.yml up --build -d
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

echo "==> Levantando stack"
COMPOSE_FILES=(-f docker-compose.app.yml)
if [[ -f docker-compose.presentacion.yml ]]; then
  COMPOSE_FILES+=(-f docker-compose.presentacion.yml)
fi
docker compose "${COMPOSE_FILES[@]}" up -d

echo "==> Vaciando DynamoDB Local (in-memory) y recreando tablas"
docker restart shopexpress-dynamo >/dev/null
sleep 3
docker restart shopexpress-usuarios shopexpress-catalogo >/dev/null

echo "==> Vaciando pedidos en Mongo"
docker exec shopexpress-mongo mongosh --quiet pedidos --eval 'db.pedidos.deleteMany({})' >/dev/null

wait_http() {
  local url="$1"
  local name="$2"
  local i
  for i in $(seq 1 40); do
    if curl -sf "$url" >/dev/null; then
      echo "  $name UP"
      return 0
    fi
    sleep 1
  done
  echo "TIMEOUT esperando $name ($url)" >&2
  docker logs --tail 40 "$3" >&2 || true
  return 1
}

echo "==> Esperando health"
wait_http http://localhost:8081/api/health usuarios shopexpress-usuarios
wait_http http://localhost:8082/api/health catalogo shopexpress-catalogo
wait_http http://localhost:8083/api/health pedidos shopexpress-pedidos
wait_http http://localhost:8080 presentacion shopexpress-presentacion || true

echo "==> Cargando seed"
"$ROOT/scripts/seed-demo.sh"

echo
echo "Demo lista."
echo "  Sitio        http://localhost:8080"
echo "  Pedidos      http://localhost:8083/swagger-ui.html"
echo "  Dynamo Admin http://localhost:8001"
echo "  Compass      mongodb://localhost:27017  db pedidos"
