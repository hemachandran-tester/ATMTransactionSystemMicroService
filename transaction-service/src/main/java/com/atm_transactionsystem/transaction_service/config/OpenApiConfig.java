package com.atm_transactionsystem.transaction_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "ATM Transaction System API",
                version = "1.0",
                description = "Microservice APIs for ATM Transaction System",
                contact = @Contact(
                        name = "Hemachandran",
                        email = "your@email.com"
                )
        )
)
public class OpenApiConfig {
}