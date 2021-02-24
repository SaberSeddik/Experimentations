package io.saber.experimentations.local.mtls;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

@Profile("client")
@Slf4j
@SpringBootApplication
public class Client {
    static {
        System.setProperty("spring.profiles.active", "client");
    }

    @Value("${client.ssl.trust-store-password}")
    private String trustStorePassword;
    @Value("${client.ssl.trust-store}")
    private Resource trustStore;
    @Value("${client.ssl.key-store-password}")
    private String keyStorePassword;
    @Value("${client.ssl.key-password}")
    private String keyPassword;
    @Value("${client.ssl.key-store}")
    private Resource keyStore;
    @Autowired
    private RestTemplate restTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Client.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void afterStart() {
        LOGGER.info("hello");
        var result = restTemplate.getForObject("https://localhost:8443/log/test", String.class);
        LOGGER.info("returned value ={}", result);
    }

    @Bean
    public RestTemplate restTemplate() throws Exception {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        restTemplate.setErrorHandler(
                new DefaultResponseErrorHandler() {
                    @Override
                    protected boolean hasError(HttpStatus statusCode) {
                        return false;
                    }
                });

        return restTemplate;
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() throws Exception {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    private HttpClient httpClient() throws Exception {
        // Load our keystore and truststore containing certificates that we trust.
        SSLContext sslcontext =
                SSLContexts.custom().loadTrustMaterial(trustStore.getFile(), trustStorePassword.toCharArray())
                        .loadKeyMaterial(keyStore.getFile(), keyStorePassword.toCharArray(),
                                keyPassword.toCharArray()).build();
        SSLConnectionSocketFactory sslConnectionSocketFactory =
                new SSLConnectionSocketFactory(sslcontext, new NoopHostnameVerifier());
        return HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
    }
}
