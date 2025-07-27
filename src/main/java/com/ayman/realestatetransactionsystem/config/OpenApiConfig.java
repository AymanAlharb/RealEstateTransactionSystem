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
                description = "OpanApi documentation for real estate Management",
                title = "Real Estate Management",
                version = "0.1"
        ), security = {
        @SecurityRequirement(
                name = "bearerAuth")

}

)
@SecurityScheme(
        name = "BearAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
