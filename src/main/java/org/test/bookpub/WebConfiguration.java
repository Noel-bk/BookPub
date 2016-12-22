package org.test.bookpub;

import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.test.bookpub.formatters.BookFormatter;
import org.test.bookpub.repository.BookRepository;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by Noel on 12/19/16.
 */
@Configuration
@PropertySource("classpath:/tomcat.https.properties")
@EnableConfigurationProperties(WebConfiguration.TomcatSslConnectorProperties.class)
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private BookRepository bookRepository;

    @Bean
    public RemoteIpFilter remoteIpFilter() {
        return new RemoteIpFilter();
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        return new LocaleChangeInterceptor();
    }

    @Bean
    public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
        return new ByteArrayHttpMessageConverter();
    }

    @Bean
    public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
        return container -> container.setSessionTimeout(1, TimeUnit.MINUTES);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new BookFormatter(bookRepository));
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false)
            .setUseTrailingSlashMatch(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/internal/**")
            .addResourceLocations("classpath:/");
    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "custom.tomcat.https")
    public static class TomcatSslConnectorProperties {
        private Integer port;
        private Boolean ssl = true;
        private Boolean secure = true;
        private String scheme = "https";
        private File keystore;
        private String keystorePassword;

        public void configureConnector(Connector connector) {
            if (port != null)
                connector.setPort(port);
            if (secure != null)
                connector.setSecure(secure);
            if (scheme != null)
                connector.setScheme(scheme);
            if (ssl != null)
                connector.setProperty("SSLEnabled", ssl.toString());
            if (keystore != null && keystore.exists()) {
                connector.setProperty("keystoreFile", keystore.getAbsolutePath());
                connector.setProperty("keystorePassword", keystorePassword);
            }
        }
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer(TomcatSslConnectorProperties properties) {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.addAdditionalTomcatConnectors(createSslConnector(properties));
        return tomcat;
    }

    private Connector createSslConnector(TomcatSslConnectorProperties properties) {
        Connector connector = new Connector();
        properties.configureConnector(connector);
        return connector;
    }

}
