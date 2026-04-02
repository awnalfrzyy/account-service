package strigops.account.internal.infrastructure.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.validation.ValidationFeature;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        packages("strigops.account.internal.presentation");
        register(ValidationFeature.class);
    }
}
