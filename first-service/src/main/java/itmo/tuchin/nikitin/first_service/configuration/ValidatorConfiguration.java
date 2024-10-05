package itmo.tuchin.nikitin.first_service.configuration;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.ServerConnector;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
public class ValidatorConfiguration {
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Bean
    JettyServerCustomizer disableSniHostCheck() {
        return (server) -> {
            for (Connector connector : server.getConnectors()) {
                if (connector instanceof ServerConnector serverConnector) {
                    HttpConnectionFactory connectionFactory = serverConnector
                            .getConnectionFactory(HttpConnectionFactory.class);
                    if (connectionFactory != null) {
                        SecureRequestCustomizer secureRequestCustomizer = connectionFactory.getHttpConfiguration()
                                .getCustomizer(SecureRequestCustomizer.class);
                        if (secureRequestCustomizer != null) {
                            secureRequestCustomizer.setSniHostCheck(false);
                        }
                    }
                }
            }
        };
    }
}
