package io.saber.experimentations.local.mtls;

import com.azure.security.keyvault.jca.KeyVaultJcaProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.PrivateKeyDetails;
import org.apache.http.ssl.PrivateKeyStrategy;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.net.Socket;
import java.security.KeyStore;
import java.security.Security;
import java.util.Map;

@Slf4j
@SpringBootApplication
public class Client {

    @Value("${azure.keyvault.uri}")
    private String keyvaultUri;
    @Value("${azure.keyvault.tenant-id}")
    private String tenantId;
    @Value("${azure.keyvault.client-id}")
    private String clientId;
    @Value("${azure.keyvault.client-secret}")
    private String clientSecret;

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
        System.setProperty("azure.keyvault.uri", keyvaultUri);
        System.setProperty("azure.keyvault.tenant-id", tenantId);
        System.setProperty("azure.keyvault.client-id", clientId);
        System.setProperty("azure.keyvault.client-secret", clientSecret);
        KeyVaultJcaProvider provider = new KeyVaultJcaProvider();
        Security.addProvider(provider);
        KeyStore ks = KeyStore.getInstance("AzureKeyVault");
        ks.load(null);
        SSLContext sslContext = SSLContexts.custom()
                .loadKeyMaterial(ks, null, new PrivateKeyStrategy() {
                    @Override
                    public String chooseAlias(Map<String, PrivateKeyDetails> aliases, Socket socket) {
                        return "client";
                    }
                })
                .loadTrustMaterial(ks, new TrustSelfSignedStrategy())
                .build();

        HostnameVerifier allowAll = (String hostName, SSLSession session) -> true;
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, allowAll);

        return HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();
    }

}
