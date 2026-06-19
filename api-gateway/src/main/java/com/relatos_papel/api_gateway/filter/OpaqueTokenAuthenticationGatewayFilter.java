package com.relatos_papel.api_gateway.filter;

import com.relatos_papel.api_gateway.dto.UsersTokenResponse;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * Valida el token opaco (Authorization: Bearer) contra users y reenvía accessToken (JWT).
 * <p>Solo se registra si {@code app.security.require-access-token=true}. Si la propiedad no existe,
 * por defecto el filtro está activo. Pon {@code require-access-token: false} para Postman/demo sin users.
 */
@Component
@ConditionalOnProperty(prefix = "app.security", name = "require-access-token", havingValue = "true", matchIfMissing = true)
public class OpaqueTokenAuthenticationGatewayFilter implements GlobalFilter, Ordered {

    private static final String USERS_SERVICE_BASE = "http://users";

    private final WebClient usersWebClient;

    public OpaqueTokenAuthenticationGatewayFilter(WebClient.Builder webClientBuilder) {
        this.usersWebClient = webClientBuilder.baseUrl(USERS_SERVICE_BASE).build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (isPublic(request)) {
            return chain.filter(exchange);
        }

        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String opaque = resolveBearer(authorization);
        if (!StringUtils.hasText(opaque)) {
            return unauthorized(exchange, "Se requiere Authorization: Bearer con el token de sesion");
        }

        return usersWebClient.get()
                .uri("/api/v1/tokens/{tokenId}", opaque)
                .retrieve()
                .bodyToMono(UsersTokenResponse.class)
                .flatMap(body -> {
                    if (body == null || !StringUtils.hasText(body.accessToken())) {
                        return unauthorized(exchange, "Respuesta de validacion de token invalida");
                    }
                    ServerHttpRequest downstream = request.mutate()
                            .headers(h -> h.remove(HttpHeaders.AUTHORIZATION))
                            .header("accessToken", body.accessToken())
                            .build();
                    return chain.filter(exchange.mutate().request(downstream).build());
                })
                .onErrorResume(ex -> unauthorized(exchange, "Token invalido o expirado"));
    }

    private static boolean isPublic(ServerHttpRequest request) {
        HttpMethod method = request.getMethod();
        if (method == HttpMethod.OPTIONS) {
            return true;
        }
        String path = request.getURI().getPath();
        if (path == null) {
            path = "";
        }

        if (path.startsWith("/ws-api/")) {
            return true;
        }

        if (HttpMethod.POST.equals(method) && "/api/v1/tokens".equals(path)) {
            return true;
        }

        if (HttpMethod.POST.equals(method) && path.matches("/api/v1/tokens/.+/renewals")) {
            return true;
        }

        return HttpMethod.GET.equals(method)
                && ("/api/books".equals(path) || path.startsWith("/api/books/"));
    }

    private static String resolveBearer(String authorization) {
        if (authorization == null || !authorization.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return null;
        }
        return authorization.substring(7).trim();
    }

    private static Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }
}
