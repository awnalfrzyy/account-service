CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'users_role') THEN
        CREATE TYPE users_role AS ENUM ('ADMIN', 'USER');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'users_gender') THEN
        CREATE TYPE users_gender AS ENUM ('FEMALE', 'MALE', 'OTHER');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'platform') THEN
        CREATE TYPE platform AS ENUM ('MANUAL', 'GOOGLE', 'GITHUB', 'GITLAB');
    END IF;
END $$;

CREATE TABLE IF NOT EXISTS users_entity (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(50) NOT NULL,
    birthdate DATE,
    gender users_gender DEFAULT 'OTHER',
    
    last_ip VARCHAR(45),
    last_cell_model VARCHAR(255),
    place_name VARCHAR(255),
    latitude DECIMAL(9,6),
    longitude DECIMAL(10,6),
    formatted_address TEXT,
    
    photo_profile TEXT NULL,
    active BOOLEAN DEFAULT true NOT NULL,
    role users_role DEFAULT 'USER' NOT NULL,
    enter_via platform DEFAULT 'MANUAL' NOT NULL,
    
    last_active TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_sessions (
    session_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    refresh_token VARCHAR(512) UNIQUE,
    user_agent TEXT,
    ip_address VARCHAR(45),
    device_fingerprint VARCHAR(255),
    
    is_trusted_device BOOLEAN DEFAULT false,
    mfa_verified BOOLEAN DEFAULT false NOT NULL,
    revoked BOOLEAN DEFAULT false NOT NULL,
    mfa_challenge_token VARCHAR(255),
    
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_activity_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_user_sessions_user_id 
        FOREIGN KEY (user_id) REFERENCES users_entity(id) ON DELETE CASCADE
);

CREATE OR REPLACE FUNCTION trigger_set_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS set_timestamp ON users_entity;
CREATE TRIGGER set_timestamp
BEFORE UPDATE ON users_entity
FOR EACH ROW EXECUTE PROCEDURE trigger_set_timestamp();

CREATE INDEX IF NOT EXISTS idx_users_entity_email ON users_entity(email);
CREATE INDEX IF NOT EXISTS idx_users_entity_username ON users_entity(username);
CREATE INDEX IF NOT EXISTS idx_users_entity_active ON users_entity(active);

CREATE INDEX IF NOT EXISTS idx_user_sessions_user_id ON user_sessions(user_id);
CREATE INDEX IF NOT EXISTS idx_user_sessions_active_user 
    ON user_sessions(user_id) 
    WHERE revoked = false AND expires_at > NOW();

CREATE INDEX IF NOT EXISTS idx_user_sessions_expires_at ON user_sessions(expires_at);
CREATE INDEX IF NOT EXISTS idx_user_sessions_revoked ON user_sessions(revoked);
CREATE INDEX IF NOT EXISTS idx_user_sessions_last_activity_at ON user_sessions(last_activity_at);