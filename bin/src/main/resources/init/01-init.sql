CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

SET timezone = 'UTC';

CREATE INDEX IF NOT EXISTS idx_temp_users_email ON users_entity(email);
CREATE INDEX IF NOT EXISTS idx_temp_users_username ON users_entity(username);
CREATE INDEX IF NOT EXISTS idx_temp_users_active ON users_entity(active);
CREATE INDEX IF NOT EXISTS idx_temp_users_session_session_id ON user_sessions(session_id);
CREATE INDEX IF NOT EXISTS idx_temp_users_session_user_id ON user_sessions(user_id);
CREATE INDEX IF NOT EXISTS idx_temp_users_session_expires_at ON user_sessions(expires_at);

CREATE TABLE IF NOT EXISTS app_config (
    key VARCHAR(100) PRIMARY KEY,
    value TEXT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO app_config (key, value, description) VALUES 
    ('db_version', '1.0.0', 'Database schema version'),
    ('app_name', 'Strigo Account', 'Application name'),
    ('environment', 'development', 'Current environment'),
    ('init_completed', 'true', 'Database initialization completed')
ON CONFLICT (key) DO NOTHING;

CREATE INDEX IF NOT EXISTS idx_app_config_key ON app_config(key);

DO $$
BEGIN
    RAISE NOTICE 'Database initialization completed successfully';
    RAISE NOTICE 'Extensions created: uuid-ossp';
    RAISE NOTICE 'Indexes prepared for migration';
    RAISE NOTICE 'Configuration table created';
END $$;
