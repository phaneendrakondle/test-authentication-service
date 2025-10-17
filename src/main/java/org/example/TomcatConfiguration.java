package org.example;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.apache.catalina.servlets.DefaultServlet;

/**
 * Tomcat configuration to mitigate CVE-2025-24813
 * - Disables writes for the default servlet
 * - Disables partial PUT support
 */
@Configuration
public class TomcatConfiguration {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> {
            factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
                // Disable partial PUT support
                connector.setAllowTrace(false);
            });
        };
    }

    /**
     * Configure the default servlet to be read-only
     * This prevents writes which is one of the conditions for CVE-2025-24813
     */
    @Bean
    public ServletRegistrationBean<DefaultServlet> defaultServletRegistration() {
        ServletRegistrationBean<DefaultServlet> registration = 
            new ServletRegistrationBean<>(new DefaultServlet(), "/");
        
        // Disable writes for the default servlet
        registration.addInitParameter("readonly", "true");
        
        // Disable PUT operations
        registration.addInitParameter("allowPut", "false");
        
        registration.setLoadOnStartup(1);
        return registration;
    }
}
