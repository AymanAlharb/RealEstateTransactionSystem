package com.ayman.realestatetransactionsystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(

        info = @Info(
                contact = @Contact(
                        name = "Ayman Alharbi",
                        email = "ayman.f.alharbi@gmail.con"
                ),
                description = "OpenApi documentation for Real Estate Management",
                title = "Real Estate Management",
                version = "0.1"
        ), security = {
        @SecurityRequirement(
                name = "bearerAuth")

}

)
@SecurityScheme(
        name = "BearAuth",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
