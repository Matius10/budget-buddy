package com.budgetbuddy.configuration;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.TomcatServletWebServerFactoryCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * We don't want to run everything on the same port as one of them will be exposed publicly.
 */
@Configuration
class PortConfiguration {

    private final String serverPort;
    private final String callbackPort;

    PortConfiguration(@Value("${server.port}") String serverPort, @Value("${server.callback.port}") String callbackPort) {
        this.serverPort = serverPort;
        this.callbackPort = callbackPort;
    }

    @Bean
    TomcatMultiConnectorServletWebServerFactoryCustomizer multiConnectorContainer() {
        final Connector[] additionalConnectors = additionalConnector();
        final ServerProperties serverProperties = new ServerProperties();
        return new TomcatMultiConnectorServletWebServerFactoryCustomizer(serverProperties, additionalConnectors);
    }

    private Connector[] additionalConnector() {
        if (!Objects.equals(serverPort, callbackPort)) {
            return new Connector[]{createConnector(callbackPort)};
        } else {
            return new Connector[]{};
        }
    }

    private Connector createConnector(final String port) {
        final Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(Integer.parseInt(port));
        return connector;
    }

    private static class TomcatMultiConnectorServletWebServerFactoryCustomizer extends TomcatServletWebServerFactoryCustomizer {
        private final Connector[] additionalConnectors;

        TomcatMultiConnectorServletWebServerFactoryCustomizer(ServerProperties serverProperties, Connector[] additionalConnectors) {
            super(serverProperties);
            this.additionalConnectors = additionalConnectors;
        }

        @Override
        public void customize(TomcatServletWebServerFactory factory) {
            super.customize(factory);

            if (additionalConnectors != null && additionalConnectors.length > 0) {
                factory.addAdditionalTomcatConnectors(additionalConnectors);
            }
        }
    }

}
