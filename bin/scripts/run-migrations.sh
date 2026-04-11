#!/bin/bash

# Migration Runner Script
# This script executes all SQL migration files in order

set -e

# Database connection parameters
DB_HOST=${DB_HOST:-localhost}
DB_PORT=${DB_PORT:-5433}
DB_NAME=${DB_NAME:-strigoaccountdb}
DB_USER=${DB_USER:-strigo_account}
DB_PASS=${DB_PASSWORD:-Account4I24pJAMEGoJczyiABCD2010000000000000}

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
MIGRATIONS_DIR="$SCRIPT_DIR/../src/main/resources/migrations"

echo "🚀 Starting database migrations..."
echo "📊 Database: $DB_HOST:$DB_PORT/$DB_NAME"
echo "👤 User: $DB_USER"
echo "📁 Migrations directory: $MIGRATIONS_DIR"
echo ""

# Check if migrations directory exists
if [ ! -d "$MIGRATIONS_DIR" ]; then
    echo "❌ Migrations directory not found: $MIGRATIONS_DIR"
    exit 1
fi

# Wait for database to be ready
echo "⏳ Waiting for database to be ready..."
until pg_isready -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME"; do
    echo "Database not ready, waiting 2 seconds..."
    sleep 2
done
echo "✅ Database is ready!"

# Execute migrations in order
echo "📋 Executing migrations..."
for migration in $(ls -v "$MIGRATIONS_DIR"/V*.sql); do
    echo "🔄 Executing: $(basename "$migration")"
    
    # Execute migration
    PGPASSWORD="$DB_PASS" psql \
        -h "$DB_HOST" \
        -p "$DB_PORT" \
        -U "$DB_USER" \
        -d "$DB_NAME" \
        -f "$migration" \
        --set ON_ERROR_STOP=1 \
        --echo-all
    
    if [ $? -eq 0 ]; then
        echo "✅ Migration completed: $(basename "$migration")"
    else
        echo "❌ Migration failed: $(basename "$migration")"
        exit 1
    fi
    
    echo ""
done

echo "🎉 All migrations completed successfully!"

# Show table summary
echo "📊 Database schema summary:"
PGPASSWORD="$DB_PASS" psql \
    -h "$DB_HOST" \
    -p "$DB_PORT" \
    -U "$DB_USER" \
    -d "$DB_NAME" \
    -c "\dt" \
    --tuples-only

echo ""
echo "✨ Migration process completed!"
