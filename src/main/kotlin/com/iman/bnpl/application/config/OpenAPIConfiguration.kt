package com.iman.bnpl.application.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.servers.Server

@OpenAPIDefinition(
    info = Info(
        title = "BNPL",
        description = "API documentation",
        contact = Contact(
            name = "BNPL",
            url = "https://we-dont-have-domain-yet.com",
            email = "info@we-dont-have-domain-yet.com"
        ),
        license = License(
            name = "All rights reserved for BNPL Co.",
            url = "https://we-dont-have-domain-yet.com")
    ),
    servers = [Server(url = "/")],
    security = [SecurityRequirement(name = "bearerAuth")]
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    `in` = SecuritySchemeIn.HEADER
)
class OpenAPIConfiguration