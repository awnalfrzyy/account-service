
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'users_entity' AND column_name = 'backup_codes') THEN
        ALTER TABLE users_entity ADD COLUMN backup_codes TEXT[];
    END IF;
END $$;


DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'users_entity' AND column_name = 'mfa_method') THEN
        ALTER TABLE users_entity ADD COLUMN mfa_method VARCHAR(20) DEFAULT 'TOTP';
    END IF;
END $$;


DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'users_entity' AND column_name = 'email_verified') THEN
        ALTER TABLE users_entity ADD COLUMN email_verified BOOLEAN DEFAULT false;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'users_entity' AND column_name = 'email_verification_token') THEN
        ALTER TABLE users_entity ADD COLUMN email_verification_token VARCHAR(255);
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'users_entity' AND column_name = 'email_verification_expires_at') THEN
        ALTER TABLE users_entity ADD COLUMN email_verification_expires_at TIMESTAMP;
    END IF;
END $$;


DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'users_entity' AND column_name = 'password_reset_token') THEN
        ALTER TABLE users_entity ADD COLUMN password_reset_token VARCHAR(255);
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'users_entity' AND column_name = 'password_reset_expires_at') THEN
        ALTER TABLE users_entity ADD COLUMN password_reset_expires_at TIMESTAMP;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'users_entity' AND column_name = 'password_changed_at') THEN
        ALTER TABLE users_entity ADD COLUMN password_changed_at TIMESTAMP;
    END IF;
END $$;


DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'users_entity' AND column_name = 'failed_login_attempts') THEN
        ALTER TABLE users_entity ADD COLUMN failed_login_attempts INTEGER DEFAULT 0;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'users_entity' AND column_name = 'account_locked_until') THEN
        ALTER TABLE users_entity ADD COLUMN account_locked_until TIMESTAMP;
    END IF;
END $$;


CREATE INDEX IF NOT EXISTS idx_users_entity_email_verification_token ON users_entity(email_verification_token);
CREATE INDEX IF NOT EXISTS idx_users_entity_password_reset_token ON users_entity(password_reset_token);
CREATE INDEX IF NOT EXISTS idx_users_entity_account_locked_until ON users_entity(account_locked_until);
CREATE INDEX IF NOT EXISTS idx_users_entity_mfa_method ON users_entity(mfa_method);


DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'chk_users_entity_mfa_method') THEN
        ALTER TABLE users_entity ADD CONSTRAINT chk_users_entity_mfa_method 
        CHECK (mfa_method IN ('TOTP', 'SMS', 'EMAIL', NULL));
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'chk_users_entity_failed_attempts') THEN
        ALTER TABLE users_entity ADD CONSTRAINT chk_users_entity_failed_attempts 
        CHECK (failed_login_attempts >= 0);
    END IF;
END $$;
