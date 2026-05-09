
# Settlement Service

A Spring Boot backend that simulates sports betting event outcome handling and bet settlement via **Kafka** and **RocketMQ**.

## Overview

Flow:
1. `POST /api/events/outcome` publishes event outcome to Kafka topic `event-outcomes`
2. Kafka consumer reads event outcome and finds matching pending bets
3. Settlement messages are produced to `bet-settlements` *(RocketMQ mocked in-process)*
4. Settlement consumer updates bet status to `WON` / `LOST`

## Tech Stack

- Java 21
- Spring Boot 4
- Kafka
- RocketMQ (mocked per assignment condition)
- H2 in-memory DB
- Docker / Docker Compose
- Actuator + Prometheus + Grafana
- Spotless + JaCoCo
- Swagger/OpenAPI

## Prerequisites

- Java 21
- Docker & Docker Compose
- Make (optional)

## Run

### Dev (app local, infra in Docker)

```shell
make dev
```

Starts:
- Kafka: `localhost:9092`
- Kafka UI (Kafbat): `http://localhost:8090`
- Prometheus: `http://localhost:9191`
- Grafana: `http://localhost:3000` (`admin/admin`)

App:
- `http://localhost:9090`

### Fully containerised

```shell
make containerised
```

Topics:
- `event-outcomes`

## API

Swagger:
- `http://localhost:9090/swagger-ui.html`

Publish outcome:
```shell
curl -X POST http://localhost:9090/api/events/outcome \
  -H "Content-Type: application/json" \
  -d '{"eventId":101,"eventName":"Football Match","winnerId":10}'
```

Fetch bets:
By event:
```shell
curl http://localhost:9090/api/bets/101
```

## Testing

```shell
make test
```

Coverage:
- `build/reports/jacoco/test/html/index.html`

## Observability endpoints

- `GET /actuator/health`
- `GET /actuator/metrics`
- `GET /actuator/prometheus`

## Code quality

```shell
make format
make check
```

## Cleanup

```shell
make clean
```