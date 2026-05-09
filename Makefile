.PHONY: help dev containerised clean test check format stop

help:
	@echo ""
	@echo "Usage:"
	@echo "  make dev            - Start infra (Kafka + init + UI + observability) and run app locally"
	@echo "  make containerised  - Build and run everything in Docker"
	@echo "  make stop           - Stop local bootRun process"
	@echo "  make clean          - Stop containers and clean Gradle build"
	@echo "  make test           - Run tests"
	@echo "  make check          - Spotless check + tests"
	@echo "  make format         - Apply Spotless formatting"
	@echo ""

dev:
	docker compose up -d kafka kafbat prometheus grafana
	./gradlew bootRun

containerised:
	docker compose up --build -d

stop:
	@pkill -f "gradle.*bootRun" || true
	@pkill -f "settlement.*jar" || true

clean:
	docker compose down -v
	./gradlew clean

test:
	./gradlew test

check:
	./gradlew spotlessCheck test

format:
	./gradlew spotlessApply