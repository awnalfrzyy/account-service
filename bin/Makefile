# Makefile for Strigops Account Project
# Compatible with Windows, macOS, and Linux

OS := $(shell uname -s 2>/dev/null || echo Windows)

SHELL := /bin/bash

RED := \033[0;31m
GREEN := \033[0;32m
YELLOW := \033[1;33m
NC := \033[0m 

.PHONY: help install-deps docker-setup test run run-with-test build-and-run clean docker-up docker-down migrate dev setup logs db-info

help:
	@echo "$(GREEN)Available targets:$(NC)"
	@echo "  $(YELLOW)install-deps$(NC)    - Install project dependencies after cloning"
	@echo "  $(YELLOW)docker-setup$(NC)    - Setup and run Docker containers end-to-end"
	@echo "  $(YELLOW)test$(NC)             - Run tests only"
	@echo "  $(YELLOW)run$(NC)              - Run program without tests"
	@echo "  $(YELLOW)run-with-test$(NC)    - Run program with tests"
	@echo "  $(YELLOW)build-and-run$(NC)    - Build, run program, and offer cleanup option"
	@echo "  $(YELLOW)clean$(NC)            - Clean build artifacts"
	@echo "  $(YELLOW)docker-up$(NC)        - Start Docker services (PostgreSQL + Redis)"
	@echo "  $(YELLOW)docker-down$(NC)      - Stop Docker services"
	@echo "  $(YELLOW)migrate$(NC)          - Run database migrations"
	@echo "  $(YELLOW)dev$(NC)              - Start development environment (Docker + App)"
	@echo "  $(YELLOW)setup$(NC)            - Quick setup for new developers"
	@echo "  $(YELLOW)logs$(NC)             - Show Docker logs"
	@echo "  $(YELLOW)db-info$(NC)          - Show database connection info"

install-deps:
	@echo "$(GREEN)Installing dependencies...$(NC)"
ifeq ($(OS),Linux)
	./mvnw clean install
endif
ifeq ($(OS),Darwin)
	./mvnw clean install
endif
ifeq ($(OS),Windows)
	.\mvnw.cmd clean install
endif
	@echo "$(GREEN)Dependencies installed successfully!$(NC)"

docker-setup:
	@echo "$(GREEN)Setting up Docker containers...$(NC)"
	@echo "$(YELLOW)Make sure Docker and Docker Compose are installed.$(NC)"
	@echo "$(YELLOW)Starting containers...$(NC)"
	docker compose -f docker-compose.yaml up -d
	@echo "$(GREEN)Docker containers are running.$(NC)"
	@echo "$(YELLOW)To stop containers: docker compose -f docker-compose.yaml down$(NC)"

test:
	@echo "$(GREEN)Running tests...$(NC)"
ifeq ($(OS),Linux)
	./mvnw test
endif
ifeq ($(OS),Darwin)
	./mvnw test
endif
ifeq ($(OS),Windows)
	.\mvnw.cmd test
endif

run:
	@echo "$(GREEN)Running program without tests...$(NC)"
ifeq ($(OS),Linux)
	./mvnw spring-boot:run
endif
ifeq ($(OS),Darwin)
	./mvnw spring-boot:run
endif
ifeq ($(OS),Windows)
	.\mvnw.cmd spring-boot:run
endif

run-with-test:
	@echo "$(GREEN)Running program with tests...$(NC)"
ifeq ($(OS),Linux)
	./mvnw clean install
endif
ifeq ($(OS),Darwin)
	./mvnw clean install
endif
ifeq ($(OS),Windows)
	.\mvnw.cmd clean install
endif

build-and-run:
	@echo "$(GREEN)Building project without tests...$(NC)"
ifeq ($(OS),Linux)
	./mvnw clean package -DskipTests
	@echo "$(GREEN)Running JAR...$(NC)"
	java -jar target/account-0.0.1-SNAPSHOT.jar &
	@echo "$(YELLOW)Program is running in background.$(NC)"
	@echo "$(YELLOW)Do you want to clean up (kill Java processes)? (y/n): $(NC)"
	@read -p "" choice; \
	if [ "$$choice" = "y" ] || [ "$$choice" = "Y" ]; then \
		echo "$(RED)Cleaning up Java processes...$(NC)"; \
		ps -ef | grep java | grep -v grep | awk '{print $$2}' | xargs -r kill -9; \
		echo "$(GREEN)Cleanup completed.$(NC)"; \
	else \
		echo "$(YELLOW)Cleanup skipped.$(NC)"; \
	fi
endif
ifeq ($(OS),Darwin)
	./mvnw clean package -DskipTests
	@echo "$(GREEN)Running JAR...$(NC)"
	java -jar target/account-0.0.1-SNAPSHOT.jar &
	@echo "$(YELLOW)Program is running in background.$(NC)"
	@echo "$(YELLOW)Do you want to clean up (kill Java processes)? (y/n): $(NC)"
	@read -p "" choice; \
	if [ "$$choice" = "y" ] || [ "$$choice" = "Y" ]; then \
		echo "$(RED)Cleaning up Java processes...$(NC)"; \
		ps -ef | grep java | grep -v grep | awk '{print $$2}' | xargs -r kill -9; \
		echo "$(GREEN)Cleanup completed.$(NC)"; \
	else \
		echo "$(YELLOW)Cleanup skipped.$(NC)"; \
	fi
endif
ifeq ($(OS),Windows)
	.\mvnw.cmd clean package -DskipTests
	@echo "$(GREEN)Running JAR...$(NC)"
	@start /B java -jar target\account-0.0.1-SNAPSHOT.jar
	@echo "$(YELLOW)Program is running in background.$(NC)"
	@echo "$(YELLOW)Do you want to clean up (kill Java processes)? (y/n): $(NC)"
	@powershell -Command "$$choice = Read-Host; if ($$choice -eq 'y' -or $$choice -eq 'Y') { Write-Host '$(RED)Cleaning up Java processes...$(NC)'; taskkill /F /IM java.exe /T 2>nul; Write-Host '$(GREEN)Cleanup completed.$(NC)' } else { Write-Host '$(YELLOW)Cleanup skipped.$(NC)' }"
endif

clean:
	@echo "$(GREEN)Cleaning build artifacts...$(NC)"
ifeq ($(OS),Linux)
	./mvnw clean
endif
ifeq ($(OS),Darwin)
	./mvnw clean
endif
ifeq ($(OS),Windows)
	.\mvnw.cmd clean
endif
	@echo "$(GREEN)Clean completed.$(NC)"

# Start Docker services
docker-up:
	@echo "🐳 Starting Docker services..."
	docker compose up -d
	@echo "✅ Services started!"
	@echo "📊 PostgreSQL: localhost:5433"
	@echo "🔴 Redis: localhost:6380"

# Stop Docker services
docker-down:
	@echo "🛑 Stopping Docker services..."
	docker compose down
	@echo "✅ Services stopped!"

# Run database migrations
migrate:
	@echo "🔄 Running database migrations..."
	./scripts/run-migrations.sh

# Start development environment
dev: docker-up migrate
	@echo "🚀 Starting development environment..."
	@echo "⏳ Waiting for services to be ready..."
	sleep 5
	@echo "🎯 Starting application..."
	./mvnw spring-boot:run

# Quick setup for new developers
setup:
	@echo "🔧 Setting up development environment..."
	@if [ ! -f .env ]; then \
		cp .env.example .env; \
		echo "✅ Created .env from template"; \
		echo "⚠️  Please edit .env with your actual values"; \
	else \
		echo "✅ .env already exists"; \
	fi
	@mkdir -p logs
	@echo "✅ Setup complete!"

# Show logs
logs:
	docker compose logs -f

# Database connection info
db-info:
	@echo "📊 Database Connection Info:"
	@echo "Host: localhost:5433"
	@echo "Database: strigoaccountdb"
	@echo "User: strigo_account"
	@echo ""
	@echo "Connect with:"
	@echo "psql -h localhost -p 5433 -U strigo_account -d strigoaccountdb"