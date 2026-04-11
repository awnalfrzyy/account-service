CREATE TABLE IF NOT EXISTS oauth_providers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL UNIQUE,
    client_id VARCHAR(255) NOT NULL,
    client_secret VARCHAR(255) NOT NULL,
    authorization_url VARCHAR(500),
    token_url VARCHAR(500),
    user_info_url VARCHAR(500),
    scopes TEXT,
    active BOOLEAN DEFAULT true NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS user_oauth_accounts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    provider_id UUID NOT NULL,
    provider_user_id VARCHAR(255) NOT NULL,
    access_token TEXT,
    refresh_token TEXT,
    token_expires_at TIMESTAMP,
    email VARCHAR(255),
    name VARCHAR(255),
    avatar_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_oauth_accounts_user_id 
        FOREIGN KEY (user_id) REFERENCES users_entity(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_oauth_accounts_provider_id 
        FOREIGN KEY (provider_id) REFERENCES oauth_providers(id) ON DELETE CASCADE,
    CONSTRAINT uk_user_oauth_accounts_provider_user 
        UNIQUE (provider_id, provider_user_id)
);

INSERT INTO oauth_providers (name, client_id, client_secret, authorization_url, token_url, user_info_url, scopes) VALUES 
    ('GOOGLE', 'placeholder', 'placeholder',
     'https://accounts.google.com/o/oauth2/v2/auth',
     'https://oauth2.googleapis.com/token',
     'https://www.googleapis.com/oauth2/v2/userinfo',
     'openid email profile'),
    ('GITHUB', 'placeholder', 'placeholder',
     'https://github.com/login/oauth/authorize',
     'https://github.com/login/oauth/access_token',
     'https://api.github.com/user',
     'user:email')
ON CONFLICT (name) DO NOTHING;

CREATE INDEX IF NOT EXISTS idx_oauth_providers_name ON oauth_providers(name);
CREATE INDEX IF NOT EXISTS idx_oauth_providers_active ON oauth_providers(active);

CREATE INDEX IF NOT EXISTS idx_user_oauth_accounts_user_id ON user_oauth_accounts(user_id);
CREATE INDEX IF NOT EXISTS idx_user_oauth_accounts_provider_id ON user_oauth_accounts(provider_id);
CREATE INDEX IF NOT EXISTS idx_user_oauth_accounts_provider_user ON user_oauth_accounts(provider_id, provider_user_id);
CREATE INDEX IF NOT EXISTS idx_user_oauth_accounts_email ON user_oauth_accounts(email);

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'update_oauth_providers_updated_at') THEN
        CREATE TRIGGER update_oauth_providers_updated_at 
            BEFORE UPDATE ON oauth_providers 
            FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'update_user_oauth_accounts_updated_at') THEN
        CREATE TRIGGER update_user_oauth_accounts_updated_at 
            BEFORE UPDATE ON user_oauth_accounts 
            FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
    END IF;
END $$;
