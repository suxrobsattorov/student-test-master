//package com.javacode;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.client.reactive.ClientHttpConnector;
//import org.springframework.http.client.reactive.ReactorClientHttpConnector;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.client.ClientRequest;
//import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
//import org.springframework.web.reactive.function.client.ExchangeStrategies;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//import reactor.netty.http.client.HttpClient;
//import reactor.netty.tcp.TcpClient;
//import uz.elektronhukumat.mipintegration.payload.CredentialsDTO;
//
//import java.time.LocalDateTime;
//
//@Configuration
//public class ServiceConfig {
//
//    private static final Log log = LogFactory.getLog(ServiceConfig.class);
//
//    @Value("${app.token-url}")
//    private String authorizeUrl;
//
//    @Value("${app.username}")
//    private String username;
//
//    @Value("${app.pd}")
//    private String pd;
//
//
//    @Bean("tokenWebClient")
//    WebClient tokenWebClient() {
//        return WebClient.builder()
//                .clientConnector(connector())
//                .build();
//    }
//
//
//    @Bean(name = "webClient")
//    WebClient webClient(@Qualifier("tokenWebClient") WebClient tokenWebClient) {
//        return WebClient.builder()
//                .clientConnector(connector())
//                .filter(renewTokenFilter(tokenWebClient))
//                .exchangeStrategies(ExchangeStrategies.builder()
//                        .codecs(configurer -> configurer
//                                .defaultCodecs()
//                                .maxInMemorySize(16 * 1024 * 1024))
//                        .build())
//                .build();
//    }
//
//    private ClientHttpConnector connector() {
//        return new ReactorClientHttpConnector(HttpClient.newConnection());
//    }
//
//    public ExchangeFilterFunction renewTokenFilter(@Qualifier("tokenWebClient") WebClient tokenWebClient) {
//        return (request, next) -> next.exchange(request).flatMap(response -> {
//            log.info(String.format("response.statusCode().value(): %d",response.statusCode().value()));
//            if (response.statusCode().value() == HttpStatus.UNAUTHORIZED.value() || response.statusCode().value() == HttpStatus.BAD_REQUEST.value()) {
//                return response.releaseBody()
//                        .then(renewToken(tokenWebClient))
//                        .flatMap(token -> {
//                            ClientRequest newRequest = ClientRequest.from(request)
//                                    .headers(headers -> headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + TokenObject.token)).build();
//                            return next.exchange(newRequest);
//                        });
//            } else {
//                return Mono.just(response);
//            }
//        });
//    }
//
//    private Mono<String> renewToken(@Qualifier("tokenWebClient") WebClient tokenWebClient) {
//
//        CredentialsDTO credentialsDTO = new CredentialsDTO(username, pd);
//
//        return tokenWebClient.post()
//                .uri(authorizeUrl)
//                .body(BodyInserters.fromValue(credentialsDTO))
//                .retrieve()
//                .bodyToMono(String.class).map(v -> {
//                            TokenObject.token = v;
//                            return v;
//                        }
//                );
//    }
//
//}
