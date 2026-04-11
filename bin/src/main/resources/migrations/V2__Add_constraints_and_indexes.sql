
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint 
        WHERE conname = 'chk_users_entity_email' AND conrelid = 'users_entity'::regclass
    ) THEN
        ALTER TABLE users_entity 
        ADD CONSTRAINT chk_users_entity_email 
        CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$');
    END IF;
END $$;

-- Add password strength constraint (idempotent)
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint 
        WHERE conname = 'chk_users_entity_password_length' AND conrelid = 'users_entity'::regclass
    ) THEN
        ALTER TABLE users_entity 
        ADD CONSTRAINT chk_users_entity_password_length 
        CHECK (LENGTH(password) >= 8);
    END IF;
END $$;


DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint 
        WHERE conname = 'chk_users_entity_username_length' AND conrelid = 'users_entity'::regclass
    ) THEN
        ALTER TABLE users_entity 
        ADD CONSTRAINT chk_users_entity_username_length 
        CHECK (username IS NULL OR LENGTH(username) >= 3);
    END IF;
END $$;


DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint 
        WHERE conname = 'chk_user_sessions_expires_at' AND conrelid = 'user_sessions'::regclass
    ) THEN
        ALTER TABLE user_sessions 
        ADD CONSTRAINT chk_user_sessions_expires_at 
        CHECK (expires_at > created_at);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint 
        WHERE conname = 'chk_user_sessions_last_activity' AND conrelid = 'user_sessions'::regclass
    ) THEN
        ALTER TABLE user_sessions 
        ADD CONSTRAINT chk_user_sessions_last_activity 
        CHECK (last_activity_at IS NULL OR last_activity_at >= created_at);
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_users_entity_active_email ON users_entity(active, email) WHERE active = true;
CREATE INDEX IF NOT EXISTS idx_user_sessions_active ON user_sessions(revoked, expires_at) WHERE revoked = false;
