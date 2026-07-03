package com.atm_transactionsystem.api_gateway.config;

import org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouterFunction<ServerResponse> authServiceRoute() {
        return GatewayRouterFunctions.route("auth-service")
                .route(RequestPredicates.path("/auth/**"), HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("AUTH-SERVICE"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> customerServiceRoute() {
        return GatewayRouterFunctions.route("customer-service")
                .route(RequestPredicates.path("/customers/**"), HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("CUSTOMER-SERVICE"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> accountServiceRoute() {
        return GatewayRouterFunctions.route("account-service")
                .route(RequestPredicates.path("/accounts/**"), HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("ACCOUNT-SERVICE"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> transactionServiceRoute() {
        return GatewayRouterFunctions.route("transaction-service")
                .route(RequestPredicates.path("/transactions/**"), HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("TRANSACTION-SERVICE"))
                .build();
    }
}